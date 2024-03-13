package dev.klepto.kweb3.core.rpc;

import com.google.gson.Gson;
import dev.klepto.kweb3.core.Web3Error;
import dev.klepto.kweb3.core.config.Web3Endpoint;
import dev.klepto.kweb3.core.rpc.api.*;

/**
 * Represents Ethereum RPC API implementing all the supported methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcApi extends
        EthCall,
        EthGasPrice,
        EthEstimateGas,
        EthSendRawTransaction,
        EthBlockNumber,
        EthGetLogs {

    /**
     * {@link Gson} instance for json encoding/decoding.
     */
    Gson GSON = new Gson();

    /**
     * Default JSON RPC version.
     */
    String JSON_VERSION = "2.0";

    /**
     * Creates a new RPC API instance for the specified transport and URL.
     *
     * @param endpoint the endpoint to create the RPC API for
     * @return a new RPC API instance
     */
    static RpcApi create(Web3Endpoint endpoint) {
        return switch (endpoint.transport()) {
            case WEBSOCKET -> new WebsocketApiClient(endpoint);
            default -> throw new Web3Error("Unsupported transport: {}", endpoint.transport());
        };
    }

}
