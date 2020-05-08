package io.jafka.jeos.impl;

import io.jafka.jeos.LocalApi;
import io.jafka.jeos.convert.Packer;
import io.jafka.jeos.core.common.Authorization;
import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
import io.jafka.jeos.core.common.transaction.TransactionAction;
import io.jafka.jeos.core.common.transaction.TransactionAuthorization;
import io.jafka.jeos.core.request.chain.json2bin.*;
import io.jafka.jeos.core.response.chain.account.Key;
import io.jafka.jeos.core.response.chain.account.PermissionLevel;
import io.jafka.jeos.core.response.chain.account.PermissionLevelWeight;
import io.jafka.jeos.core.response.chain.account.RequiredAuth;
import io.jafka.jeos.util.KeyUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zarek
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

    /**
     * 转账
     *
     * @param arg
     * @param contract   合约名称
     * @param from       发起者
     * @param to         目标地址
     * @param quantity   数量
     * @param memo       备注
     * @return
     */
    @Override
    public PackedTransaction transfer(SignArg arg, String contract, String from, String to, String quantity, String memo) {
        // ① pack transfer data
        TransferArg transferArg = new TransferArg(from, to, quantity, memo);
        String transferData = Packer.packTransfer(transferArg);

        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(from, "active"));

        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction(contract, "transfer", authorizations, transferData)
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

        return packedTransaction;
    }


    /**
     * 购买内存
     *
     * @param arg
     * @param payer      执行者
     * @param receiver   接收者
     * @param ramBytes   内存字节数
     * @return
     */
    @Override
    public PackedTransaction buyRam(SignArg arg, String payer, String receiver, int ramBytes) {
        // ① pack transfer data
        BuyRamArg buyRamArg = new BuyRamArg(payer, receiver, ramBytes);
        String buyRamData = Packer.packBuyrambytes(buyRamArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(payer, "active"));
        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction("eosio", "buyrambytes", authorizations, buyRamData)
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

        return packedTransaction;
    }

    /**
     * 抵押CPU和NET
     *
     * @param arg
     * @param from             执行者
     * @param receiver         接收者
     * @param stakeNetQuantity 抵押EOS数量 （NET）
     * @param stakeCpuQuantity 抵押EOS数量 （CPU）
     * @return
     */
    @Override
    public PackedTransaction delegate(SignArg arg, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity) {
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
                new TransactionAction("eosio", "delegatebw", authorizations, delegateData)
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

        return packedTransaction;
    }

    /**
     * 创建账号
     *
     * @param arg
     * @param creator          创建者账号
     * @param name             被创建的账号名称
     * @param owner            公钥
     * @param active           公钥
     * @param ramBytes         初始购买内存字节数
     * @param stakeNetQuantity 抵押EOS数量（NET）
     * @param stakeCpuQuantity 抵押EOS数量（CPU）
     * @return
     */
    @Override
    public PackedTransaction createAccount(SignArg arg, String creator, String name, String owner, String active, int ramBytes, String stakeNetQuantity, String stakeCpuQuantity) {
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
                new TransactionAction("eosio", "newaccount", authorizations, createData),
                new TransactionAction("eosio", "buyrambytes", authorizations, buyRamData),
                new TransactionAction("eosio", "delegatebw", authorizations, delegateData)
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

        return packedTransaction;
    }

    /**
     * 修改单签权限
     *
     * @param arg
     * @param account    被修改者账号
     * @param publicKey  公钥
     * @param permission 修改的权限名
     * @return
     */
    @Override
    public PackedTransaction updateAuth(SignArg arg, String account, String publicKey, String permission) {
        // ① pack transfer data
        RequiredAuth auth = new RequiredAuth(1L, Arrays.asList(new Key(publicKey, 1)), Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        UpdateAuthArg updateAuthArg = new UpdateAuthArg(account, auth, "owner", permission);
        String updateAutheData = Packer.packUpdateAuth(updateAuthArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(account, "owner"));
        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction("eosio", "updateauth", authorizations, updateAutheData)
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

        return packedTransaction;
    }

    /**
     * 修改多签权限（修改Key）
     * @param arg
     * @param privateKey 私钥
     * @param account 被修改者账号
     * @param threshold 最低权重
     * @param pubkeys 公钥
     * @param permission 被修改的权限名
     * @return
     */
    /*@Override
    public PushTransactionRequest updateMultipleAuth(SignArg arg,  String account, Long threshold, List<String> pubkeys, String permission) {
        // ① pack transfer data
        List<Key> keys = new ArrayList<>();
        for (String key : pubkeys) {
            keys.add(new Key(key, 1));
        }
        RequiredAuth auth = new RequiredAuth(threshold, keys, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        UpdateAuthArg updateAuthArg = new UpdateAuthArg(account, auth, "owner", permission);
        String updateAutheData = Packer.packUpdateAuth(updateAuthArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(account, "owner"));
        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction("eosio", "updateauth", authorizations, updateAutheData)
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
    }*/

    /**
     * 修改多签权限（修改多账号）
     *
     * @param arg
     * @param account     被修改的账号
     * @param threshold   最低权重
     * @param accountList 账号数组
     * @param permission  被修改权限名
     * @return
     */
    @Override
    public PackedTransaction updateMultipleAuth(SignArg arg, String account, Long threshold, List<String> accountList, String permission) {
        // ① pack transfer data
        List<PermissionLevelWeight> accounts = new ArrayList<>();
        for (String acountname : accountList) {
            PermissionLevelWeight permissionLevelWeight = new PermissionLevelWeight();
            permissionLevelWeight.setPermission(new PermissionLevel(acountname, "active"));
            permissionLevelWeight.setWeight(1);
            accounts.add(permissionLevelWeight);
        }
        RequiredAuth auth = new RequiredAuth(threshold, Collections.EMPTY_LIST, accounts, Collections.EMPTY_LIST);
        UpdateAuthArg updateAuthArg = new UpdateAuthArg(account, auth, "owner", permission);
        String updateAutheData = Packer.packUpdateAuth(updateAuthArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Collections.singletonList(new TransactionAuthorization(account, "owner"));
        // ④ build the all actions
        List<TransactionAction> actions = Collections.singletonList(
                new TransactionAction("eosio", "updateauth", authorizations, updateAutheData)
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

        return packedTransaction;
    }

    /**
     * 发起提案
     *
     * @param arg
     * @param account      提案创建人
     * @param proposalName 提案名
     * @param requests     权限
     * @param trx
     * @param permission
     * @return
     */
    @Override
    public PackedTransaction propose(SignArg arg, String account, String proposalName, List<Authorization> requests, PackedTransaction trx, String permission) {
        // ① pack transfer data
        ProposeArg proposeArg = new ProposeArg(account, proposalName, requests, trx);
        String proposeArgData = Packer.packProposeArg(proposeArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Collections.singletonList(new TransactionAuthorization(account, permission));

        // ④ build the all actions
        List<TransactionAction> actions = Collections.singletonList(
                new TransactionAction("eosio.msig", "propose", authorizations, proposeArgData)
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

        return packedTransaction;
    }

    /**
     * 审批提案
     *
     * @param arg
     * @param account
     * @param proposalName
     * @param permission
     * @return
     */
    @Override
    public PackedTransaction approve(SignArg arg, String account, String proposer, String proposalName, String permission) {

        // ① pack transfer data
        PermissionLevel permissionLevel = new PermissionLevel(account, permission);
        ApproveArg approveArg = new ApproveArg(proposer, proposalName, permissionLevel);
        String proposeArgData = Packer.packApproveArg(approveArg);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(account, permission));

        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction("eosio.msig", "approve", authorizations, proposeArgData)
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

        return packedTransaction;
    }

    /**
     * 执行提案
     *
     * @param arg
     * @param account
     * @param proposalName
     * @param permission
     * @return
     */
    @Override
    public PackedTransaction execPropose(SignArg arg, String account, String proposer, String proposalName, String permission) {
        // ① pack transfer data
        ExecPropose execPropose = new ExecPropose(proposer, proposalName, account);
        String proposeArgData = Packer.packExexPropose(execPropose);
        // ③ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(account, permission));

        // ④ build the all actions
        List<TransactionAction> actions = Arrays.asList(
                new TransactionAction("eosio.msig", "exec", authorizations, proposeArgData)
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

        return packedTransaction;
    }
}
