package io.jafka.jeos;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import io.jafka.jeos.impl.EosApiServiceGenerator;

/**
 * Local API with EOS without RPC
 *
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年9月6日
 */
public interface LocalApi {

    String createPrivateKey();

    String toPublicKey(String privateKey);

    PushTransactionRequest transfer(SignArg arg, String privateKey, String account, String from, String to, String quantity, String memo);

    PushTransactionRequest buyRam(SignArg arg, String privateKey,String contractAccount, String payer, String receiver, int ramBytes);

    PushTransactionRequest delegate(SignArg arg, String privateKey, String contractAccount, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity);

    PushTransactionRequest createAccount(SignArg arg, String privateKey, String contractAccount, String creator, String name, String owner, String active, int ramBytes, String stakeNetQuantity, String stakeCpuQuantity);

    default ObjectMapper getObjectMapper() {
        return EosApiServiceGenerator.getMapper();
    }
}
