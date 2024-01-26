package dev.klepto.kweb3;

import lombok.val;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Asynchronous kweb3 result backed by {@link CompletableFuture}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3Result<T> {

    /**
     * Creates new, uncompleted result of given type.
     *
     * @return uncompleted web3 result
     */
    public static <T> Web3Result<T> create() {
        return new Web3Result<>(new CompletableFuture<>());
    }

    private final CompletionStage<T> stage;

    private Web3Result(CompletionStage<T> stage) {
        this.stage = stage;
    }

    /**
     * Completes the result with the given value.
     *
     * @param value the result completion value
     */
    public void complete(T value) {
        stage.toCompletableFuture().complete(value);
    }

    /**
     * Completes the result with given {@link Throwable} to indicate an error.
     *
     * @param cause the cause of the error
     */
    public void completeExceptionally(Throwable cause) {
        stage.toCompletableFuture().completeExceptionally(cause);
    }

    /**
     * Cancels the result by completing it with {@link Web3Error}.
     */
    public void cancel() {
        completeExceptionally(new Web3Error("Result cancelled"));
    }

    /**
     * Waits if necessary for the result to complete, and then retrieves it.
     *
     * @return the result
     */
    public T get() {
        try {
            return stage.toCompletableFuture().get();
        } catch (Exception cause) {
            throw new Web3Error(cause);
        }
    }

    /**
     * Registers a consumer to be called when result is complete.
     *
     * @param consumer the result consumer
     */
    public Web3Result<T> get(Consumer<T> consumer) {
        val newStage = stage.whenComplete((result, error) -> {
            if (result != null) {
                consumer.accept(result);
            }
        });
        return new Web3Result<>(newStage);
    }

    /**
     * Registers a consumer to be called when result is complete.
     *
     * @param consumer the result consumer
     */
    public Web3Result<T> error(Consumer<Throwable> consumer) {
        val newStage = stage.whenComplete((result, error) -> {
            if (error != null) {
                consumer.accept(error);
            }
        });
        return new Web3Result<>(newStage);
    }

    /**
     * Creates a new Web3Result that maps the result using the given mapping function.
     *
     * @param mapper the mapping function
     * @return new Web3Result that will produce remapped result
     */
    public <R> Web3Result<R> map(Function<T, R> mapper) {
        val newStage = stage.thenApply(mapper);
        return new Web3Result<>(newStage);
    }


}
