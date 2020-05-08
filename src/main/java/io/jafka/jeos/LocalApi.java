package io.jafka.jeos;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jafka.jeos.core.common.Authorization;
import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
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

    PackedTransaction transfer(SignArg arg, String account, String from, String to, String quantity, String memo);

    PackedTransaction buyRam(SignArg arg, String payer, String receiver, int ramBytes);

    PackedTransaction delegate(SignArg arg, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity);

    PackedTransaction createAccount(SignArg arg,  String creator, String name, String owner, String active, int ramBytes, String stakeNetQuantity, String stakeCpuQuantity);

    default ObjectMapper getObjectMapper() {
        return EosApiServiceGenerator.getMapper();
    }

    PackedTransaction updateAuth(SignArg arg, String account, String publicKey, String permission);

    PackedTransaction updateMultipleAuth(SignArg arg, String account, Long threshold, List<String> keys, String permission);

    PackedTransaction propose(SignArg arg,  String account, String proposalName, List<Authorization> requests, PackedTransaction trx, String permission);

    PackedTransaction approve(SignArg arg,  String account, String proposer, String proposalName, String permission);

    PackedTransaction execPropose(SignArg arg, String account, String proposer, String proposalName, String permission);
}
