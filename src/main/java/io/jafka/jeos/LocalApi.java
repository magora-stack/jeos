package io.jafka.jeos;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jafka.jeos.core.common.Authorization;
import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
import io.jafka.jeos.core.request.chain.json2bin.TransferArg;
import io.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import io.jafka.jeos.core.response.chain.account.Key;
import io.jafka.jeos.core.response.chain.account.PermissionLevel;
import io.jafka.jeos.impl.EosApiServiceGenerator;

import java.util.List;

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

    PackedTransaction createTransfer(SignArg arg, String contract, String from, String to, String quantity, String memo);

    PushTransactionRequest buyRam(SignArg arg, String privateKey, String payer, String receiver, int ramBytes);

    PushTransactionRequest delegate(SignArg arg, String privateKey, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity);

    PushTransactionRequest createAccount(SignArg arg, String privateKey, String creator, String name, String owner, String active, int ramBytes, String stakeNetQuantity, String stakeCpuQuantity);

    default ObjectMapper getObjectMapper() {
        return EosApiServiceGenerator.getMapper();
    }

    PushTransactionRequest updateAuth(SignArg arg, String privateKey, String account, String publicKey, String permission);

    PushTransactionRequest updateMultipleAuth(SignArg arg, String privateKey, String account, Long threshold, List<String> keys, String permission);

    PushTransactionRequest propose(SignArg arg, String privateKey, String account, String proposalName, List<Authorization> requests, PackedTransaction trx, String permission);

    PushTransactionRequest approve(SignArg arg, String privateKey, String account,String proposer, String proposalName, String permission);

    PushTransactionRequest execPropose(SignArg arg, String privateKey, String account, String proposer, String proposalName, String permission);
}
