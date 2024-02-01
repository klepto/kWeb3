package dev.klepto.kweb3.rpc.api;

import dev.klepto.kweb3.Web3Result;
import dev.klepto.kweb3.rpc.RpcMethod;
import dev.klepto.kweb3.rpc.RpcRequest;
import dev.klepto.kweb3.rpc.RpcResponse;
import lombok.val;

/**
 * Implementation of Ethereum RPC API <code>eth_sendRawTransaction</code> method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthSendRawTransaction extends RpcMethod {

    /**
     * Submits a pre-signed transaction for broadcast to the Ethereum network.
     *
     * @param data the signed transaction data (typically signed with a library, using your private key)
     * @return the transaction hash, or the zero hash if the transaction is not yet available
     */
    default Web3Result<String> ethSendRawTransaction(String data) {
        val request = new RpcRequest()
                .withMethod("eth_sendRawTransaction")
                .withParams(new Request(data));

        return request(request)
                .map(RpcResponse::result);
    }

    /**
     * Represents <code>eth_sendRawTransaction</code> request object.
     *
     * @param data the signed transaction data (typically signed with a library, using your private key)
     */
    record Request(String data) {
    }

}