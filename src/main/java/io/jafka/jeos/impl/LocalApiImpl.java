package io.jafka.jeos.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jafka.jeos.LocalApi;
import io.jafka.jeos.convert.Packer;
import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
import io.jafka.jeos.core.common.transaction.TransactionAction;
import io.jafka.jeos.core.common.transaction.TransactionAuthorization;
import io.jafka.jeos.core.request.chain.json2bin.BuyRamArg;
import io.jafka.jeos.core.request.chain.json2bin.CreateAccountArg;
import io.jafka.jeos.core.request.chain.json2bin.DelegatebwArg;
import io.jafka.jeos.core.request.chain.json2bin.TransferArg;
import io.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import io.jafka.jeos.core.response.chain.account.*;
import io.jafka.jeos.util.KeyUtil;
import io.jafka.jeos.util.Raw;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年9月6日
 */
public class LocalApiImpl implements LocalApi {

    @Override
    public String createPrivateKey() {
        return KeyUtil.createPrivateKey();
    }

    @Override
    public String toPublicKey(String privateKey) {
        return KeyUtil.toPublicKey(privateKey);
    }

    @Override
    public PushTransactionRequest transfer(SignArg arg, String privateKey, String account, String from, String to, String quantity, String memo) {
        // ① pack transfer data
        TransferArg transferArg = new TransferArg(from, to, quantity, memo);
        String transferData = Packer.packTransfer(transferArg);

        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(from, "active"));

        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction(account, "transfer", authorizations, transferData)
        );
        // ⑤ build the packed transaction
        PackedTransaction packedTransaction = new PackedTransaction();
        packedTransaction.setExpiration(arg.getHeadBlockTime().plusSeconds(arg.getExpiredSecond()));
        packedTransaction.setRefBlockNum(arg.getLastIrreversibleBlockNum());
        packedTransaction.setRefBlockPrefix(arg.getRefBlockPrefix());

        packedTransaction.setMaxNetUsageWords(0);
        packedTransaction.setMaxCpuUsageMs(0);
        packedTransaction.setDelaySec(0);
        packedTransaction.setActions(actions);

        String hash = sign(privateKey, arg, packedTransaction);
        PushTransactionRequest req = new PushTransactionRequest();
        req.setTransaction(packedTransaction);
        req.setSignatures(Arrays.asList(hash));
        return req;
    }


    @Override
    public PushTransactionRequest buyRam(SignArg arg, String privateKey, String contractAccount, String payer, String receiver, int ramBytes) {
        // ① pack transfer data
        BuyRamArg buyRamArg = new BuyRamArg(payer, receiver, ramBytes);
        String buyRamData = Packer.packBuyrambytes(buyRamArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(payer, "active"));
        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction(contractAccount, "buyrambytes", authorizations, buyRamData)
        );
        // ⑤ build the packed transaction
        PackedTransaction packedTransaction = new PackedTransaction();
        packedTransaction.setExpiration(arg.getHeadBlockTime().plusSeconds(arg.getExpiredSecond()));
        packedTransaction.setRefBlockNum(arg.getLastIrreversibleBlockNum());
        packedTransaction.setRefBlockPrefix(arg.getRefBlockPrefix());

        packedTransaction.setMaxNetUsageWords(20);
        packedTransaction.setMaxCpuUsageMs(1);
        packedTransaction.setDelaySec(1);
        packedTransaction.setActions(actions);

        String hash = sign(privateKey, arg, packedTransaction);
        PushTransactionRequest req = new PushTransactionRequest();
        req.setTransaction(packedTransaction);
        req.setSignatures(Arrays.asList(hash));
        return req;
    }


    @Override
    public PushTransactionRequest delegate(SignArg arg, String privateKey, String contractAccount, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity) {
        // ① pack transfer data
        Long transfer = 1L;
        if (from.equals(receiver)) {
            transfer = 0L;
        }
        DelegatebwArg delegatebwArg = new DelegatebwArg(from, receiver, stakeNetQuantity, stakeCpuQuantity, transfer);
        String delegateData = Packer.packDelegatebw(delegatebwArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(from, "active"));
        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction(contractAccount, "delegatebw", authorizations, delegateData)
        );
        // ⑤ build the packed transaction
        PackedTransaction packedTransaction = new PackedTransaction();
        packedTransaction.setExpiration(arg.getHeadBlockTime().plusSeconds(arg.getExpiredSecond()));
        packedTransaction.setRefBlockNum(arg.getLastIrreversibleBlockNum());
        packedTransaction.setRefBlockPrefix(arg.getRefBlockPrefix());

        packedTransaction.setMaxNetUsageWords(24);
        packedTransaction.setMaxCpuUsageMs(1);
        packedTransaction.setDelaySec(1);
        packedTransaction.setActions(actions);

        String hash = sign(privateKey, arg, packedTransaction);
        PushTransactionRequest req = new PushTransactionRequest();
        req.setTransaction(packedTransaction);
        req.setSignatures(Arrays.asList(hash));
        return req;
    }

    @Override
    public PushTransactionRequest createAccount(SignArg arg, String privateKey, String contractAccount, String creator, String name, String owner, String active, int ramBytes, String stakeNetQuantity, String stakeCpuQuantity) {
        // ① pack transfer data
        RequiredAuth ownerAuth = new RequiredAuth(1L, Arrays.asList(new Key(owner, 1)), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        RequiredAuth activeAuth = new RequiredAuth(1L, Arrays.asList(new Key(active, 1)), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        CreateAccountArg createAccountArg = new CreateAccountArg(creator, name, ownerAuth, activeAuth);
        String createData = Packer.packCreateAccount(createAccountArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(creator, "active"));
        // ④ build the all actions
        String buyRamData = Packer.packBuyrambytes(new BuyRamArg(creator, name, ramBytes));
        String delegateData = Packer.packDelegatebw(new DelegatebwArg(creator, name, stakeNetQuantity, stakeCpuQuantity, 1L));
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction(contractAccount, "newaccount", authorizations, createData),
                new TransactionAction(contractAccount, "buyrambytes", authorizations, buyRamData),
                new TransactionAction(contractAccount, "delegatebw", authorizations, delegateData)
        );

        // ⑤ build the packed transaction
        PackedTransaction packedTransaction = new PackedTransaction();
        packedTransaction.setExpiration(arg.getHeadBlockTime().plusSeconds(arg.getExpiredSecond()));
        packedTransaction.setRefBlockNum(arg.getLastIrreversibleBlockNum());
        packedTransaction.setRefBlockPrefix(arg.getRefBlockPrefix());

        packedTransaction.setMaxNetUsageWords(47);
        packedTransaction.setMaxCpuUsageMs(1);
        packedTransaction.setDelaySec(1);
        packedTransaction.setActions(actions);

        String hash = sign(privateKey, arg, packedTransaction);
        PushTransactionRequest req = new PushTransactionRequest();
        req.setTransaction(packedTransaction);
        req.setSignatures(Arrays.asList(hash));
        return req;
    }


    private String sign(String privateKey, SignArg arg, PackedTransaction t) {

        Raw raw = Packer.packPackedTransaction(arg.getChainId(), t);
        raw.pack(ByteBuffer.allocate(33).array());// TODO: what's this?
        String hash = KeyUtil.signHash(privateKey, raw.bytes());
        return hash;
    }
}
