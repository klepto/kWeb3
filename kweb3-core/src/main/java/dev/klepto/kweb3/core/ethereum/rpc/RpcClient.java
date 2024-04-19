package dev.klepto.kweb3.core.ethereum.rpc;

import com.google.common.util.concurrent.Uninterruptibles;
import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.chain.Web3Transport;
import dev.klepto.kweb3.core.ethereum.rpc.io.HttpConnection;
import dev.klepto.kweb3.core.ethereum.rpc.io.RpcConnection;
import dev.klepto.kweb3.core.ethereum.rpc.io.WebsocketConnection;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.RpcMessage;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.RpcProtocol;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.RpcRequest;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.RpcResponse;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Implementation of Ethereum RPC API client.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Slf4j
public class RpcClient implements Closeable, RpcProtocol {

    private final EndpointProvider endpointProvider;
    private final AtomicReference<RpcConnection> connection = new AtomicReference<>();

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final AtomicLong requestId = new AtomicLong(1);
    private final Set<Request> requests = ConcurrentHashMap.newKeySet();
    private final Set<Consumer<RpcMessage>> callbacks = ConcurrentHashMap.newKeySet();

    /**
     * Creates a new RPC client instance for the given endpoints.
     *
     * @param endpoints the endpoints this client can connect to
     */
    public RpcClient(@NotNull Web3Endpoint... endpoints) {
        this.endpointProvider = new EndpointProvider(Arrays.asList(endpoints));
    }

    /**
     * Gets the {@link Web3Endpoint} that this client currently connects to.
     *
     * @return the endpoint instance
     */
    public Web3Endpoint endpoint() {
        return endpointProvider.current();
    }

    /**
     * Gets the current {@link RpcConnection} instance.
     *
     * @return the connection instance
     */
    @Synchronized
    public RpcConnection connection() {
        val current = connection.get();
        if (current != null && current.isOpen()) {
            return current;
        }

        val endpoint = endpoint();
        require(endpoint != null, "No available endpoints to connect to.");
        val transport = endpoint.transport();
        require(transport == Web3Transport.HTTP || transport == Web3Transport.WEBSOCKET,
                "Unsupported transport type: {}", transport);
        
        val next = transport == Web3Transport.WEBSOCKET
                ? new WebsocketConnection(endpoint.url())
                : new HttpConnection(endpoint.url());

        next.setMessageCallback(this::onMessage);
        next.setErrorCallback(this::onError);
        next.setCloseCallback(this::onClose);
        next.open();
        connection.set(next);

        // Replay requests if connection was closed.
        if (current != null) {
            replay();
        }

        return next;
    }

    /**
     * Replays the current request queue to the current {@link RpcConnection}.
     */
    @Synchronized
    public void replay() {
        requests.forEach(request -> send(request.request));
    }

    /**
     * Sends a given request to the underlying {@link RpcConnection}.
     *
     * @param request the rpc request object
     */
    @Synchronized
    public void send(RpcRequest request) {
        val cooldown = endpoint().settings().requestCooldown();
        executor.submit(() -> {
            try {
                connection().send(request.serialize());
                if (cooldown != null) {
                    Uninterruptibles.sleepUninterruptibly(cooldown);
                }
            } catch (Throwable cause) {
                onError(cause);
            }
        });
    }

    /**
     * Creates and sends a new RPC request using implemented protocol.
     *
     * @param request the rpc request object
     * @return a {@link Web3Result} containing rpc response object that will be completed asynchronously
     */
    public @NotNull Web3Result<RpcResponse> request(@NotNull RpcRequest request) {
        request = request.withId(requestId.incrementAndGet());
        val result = new Web3Result<RpcResponse>();
        requests.add(new Request(request, result));
        send(request);
        return result;
    }

    /**
     * Called when new RPC response is received.
     *
     * @param response the rpc response
     */
    public void response(RpcResponse response) {
        val request = requests.stream()
                .filter(value -> value.request.id() == response.id())
                .findFirst()
                .orElse(null);
        if (request == null) {
            return;
        }
        requests.remove(request);

        val result = request.result();
        val error = response.error();
        if (error != null) {
            val cause = new Web3Error(
                    "RPC error occurred: [request: {}, error: {}]",
                    request.request.serialize(),
                    error.message()
            );
            result.completeExceptionally(cause);
            return;
        }
        result.complete(response);
    }

    /**
     * Called when new RPC message is received.
     *
     * @param message the rpc message
     */
    public void onMessage(String message) {
        val response = RpcProtocol.decode(message);
        if (response instanceof RpcResponse) {
            response((RpcResponse) response);
        }
        callbacks.forEach(callback -> callback.accept(response));
    }

    /**
     * Called when an error occurs during RPC message processing.
     *
     * @param cause the error cause
     */
    public void onError(Throwable cause) {
        if (cause instanceof Web3Error) {
            log.error("An RPC error occurred: {}", cause.getMessage());
            return;
        }
        endpointProvider.next();
        connection();
    }

    /**
     * Called when the connection is closed.
     */
    public void onClose() {
        connection();
    }

    /**
     * Adds a new message callback to this RPC client.
     *
     * @param callback the message callback to add
     */
    public void addCallback(Consumer<RpcMessage> callback) {
        callbacks.add(callback);
    }

    /**
     * Removes a message callback from this RPC client.
     *
     * @param callback the message callback to remove
     */
    public void removeCallback(Consumer<RpcMessage> callback) {
        callbacks.remove(callback);
    }

    /**
     * Closes the RPC client and all underlying connections.
     */
    @Override
    public void close() {
        val conn = connection.get();
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * Represents a request that was sent to the RPC server.
     *
     * @param request the request object
     * @param result  the result object
     */
    private record Request(RpcRequest request, Web3Result<RpcResponse> result) {
    }


}
