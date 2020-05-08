package io.jafka.jeos.util;

import io.jafka.jeos.convert.Packer;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
import io.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Signer {

    /**
     * 签名
     * @param chainId
     * @param transaction
     * @param privateKey
     * @return
     */
    public static PushTransactionRequest sign(String chainId, PackedTransaction transaction, String privateKey) {

        PushTransactionRequest req = new PushTransactionRequest();
        Raw raw = Packer.packPackedTransaction(chainId, transaction);
        raw.pack(ByteBuffer.allocate(33).array());// TODO: what's this?
        String hash = KeyUtil.signHash(privateKey, raw.bytes());
        req.setTransaction(transaction);
        req.setSignatures(Arrays.asList(hash));
        return req;
    }
}
