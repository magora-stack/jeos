package io.jafka.jeos.convert;

import io.jafka.jeos.core.common.Authorization;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
import io.jafka.jeos.core.request.chain.json2bin.*;
import io.jafka.jeos.core.response.chain.account.PermissionLevel;
import io.jafka.jeos.core.response.chain.account.RequiredAuth;
import io.jafka.jeos.util.Raw;
import io.jafka.jeos.util.ecc.Hex;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

/**
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年9月6日
 */
public class Packer {

    public static String packTransfer(TransferArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getFrom());
        raw.packName(arg.getTo());
        raw.packAsset(arg.getQuantity());
        raw.pack(arg.getMemo());
        return raw.toHex();
    }

    public static String packBuyrambytes(BuyRamArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getPayer());
        raw.packName(arg.getReceiver());
        raw.pack(arg.getBytes());
        return raw.toHex();
    }

    public static String packDelegatebw(DelegatebwArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getFrom());
        raw.packName(arg.getReceiver());
        raw.packAsset(arg.getStakeNetQuantity());
        raw.packAsset(arg.getStakeCpuQuantity());
        raw.packVarint32(arg.getTransfer());
        return raw.toHex();
    }

    public static String packUpdateAuth(UpdateAuthArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getAccount());
        raw.packName(arg.getPermission());
        raw.packName(arg.getParent());

        RequiredAuth auth = arg.getAuth();

        // owner
        raw.packUint32(auth.getThreshold());
        // ownwer.keys
        raw.packVarint32(auth.getKeys().size());
        auth.getKeys().forEach(k -> {
            raw.packPublicKey(k.getKey());
            raw.packUint16(k.getWeight());
        });

        // ownwer.accounts
        raw.packVarint32(auth.getAccounts().size());
        auth.getAccounts().forEach(a -> {
            raw.packName(a.getPermission().getActor());
            raw.packName(a.getPermission().getPermission());
            raw.packUint16(a.getWeight());
        });

        // ownwer.waits
        raw.packVarint32(auth.getWaits().size());
        auth.getWaits().forEach(w -> {
            raw.packUint32(w.getWeightSec());
            raw.packUint16(w.getWeight());
        });

        return raw.toHex();
    }

    public static String packCreateAccount(CreateAccountArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getCreator());
        raw.packName(arg.getName());

        Arrays.asList(arg.getOwner(), arg.getActive()).stream().filter(x -> x != null).forEach(r -> {
            // owner
            raw.packUint32(r.getThreshold());
            // ownwer.keys
            raw.packVarint32(r.getKeys().size());
            r.getKeys().forEach(k -> {
                raw.packPublicKey(k.getKey());
                raw.packUint16(k.getWeight());
            });
            // ownwer.accounts
            raw.packVarint32(r.getAccounts().size());
            r.getAccounts().forEach(a -> {
                raw.packName(a.getPermission().getActor());
                raw.packName(a.getPermission().getPermission());
                raw.packUint16(a.getWeight());
            });
            // ownwer.waits
            raw.packVarint32(r.getWaits().size());
            r.getWaits().forEach(w -> {
                raw.packUint32(w.getWeightSec());
                raw.packUint16(w.getWeight());
            });
        });
        return raw.toHex();
    }

    public static Raw packPackedTransaction(String chainId, PackedTransaction t) {
        Raw raw = new Raw();
        //chain
        raw.pack(Hex.toBytes(chainId));
        //expiration
        raw.packUint32(t.getExpiration().toEpochSecond(ZoneOffset.ofHours(0)));
        //ref_block_num
        raw.packUint16(t.getRefBlockNum().intValue());
        //ref_block_prefix
        raw.packUint32(t.getRefBlockPrefix());
        //max_net_usage_words
        raw.packVarint32(t.getMaxNetUsageWords());
        //max_cpu_usage_ms
        raw.packUint8(t.getMaxCpuUsageMs());//TODO: what the type?
        //delay_sec
        raw.packVarint32(t.getDelaySec());
        //context_free_actions
        raw.packVarint32(t.getContextFreeActions().size());
        //TODO: getContextFreeActions

        //actions
        raw.packVarint32(t.getActions().size());
        t.getActions().forEach(a -> {
            //action.account
            raw.packName(a.getAccount())
                    .packName(a.getName())
                    .packVarint32(a.getAuthorization().size());

            //action.authorization
            a.getAuthorization().forEach(au -> {
                raw.packName(au.getActor())
                        .packName(au.getPermission());
            });
            //action.data
            byte[] dat = Hex.toBytes(a.getData());
            raw.packVarint32(dat.length);
            raw.pack(dat);
        });
        return raw;
    }


    public static String packProposeArg(ProposeArg arg) {
        Raw raw = new Raw();
        //The account proposing a transaction
        raw.packName(arg.getProposer());
        //The name of the proposal (should be unique for proposer)
        raw.packName(arg.getProposalName());
        //Permission levels expected to approve the proposal
        List<Authorization> requests = arg.getRequests();
        raw.packVarint32(requests.size());
        requests.forEach(a -> {
            raw.packName(a.getActor());
            raw.packName(a.getPermission());
        });

        //----------------------------------------------
        // Proposed transaction
        PackedTransaction trx = arg.getTrx();
        //expiration
        raw.packUint32(trx.getExpiration().toEpochSecond(ZoneOffset.ofHours(0)));
        //ref_block_num
        raw.packUint16(trx.getRefBlockNum().intValue());
        //ref_block_prefix
        raw.packUint32(trx.getRefBlockPrefix());
        //max_net_usage_words
        raw.packVarint32(trx.getMaxNetUsageWords());
        //max_cpu_usage_ms
        raw.packUint8(trx.getMaxCpuUsageMs());//TODO: what the type?
        //delay_sec
        raw.packVarint32(trx.getDelaySec());
        //context_free_actions
        raw.packVarint32(trx.getContextFreeActions().size());
        //TODO: getContextFreeActions

        //actions
        raw.packVarint32(trx.getActions().size());
        trx.getActions().forEach(a -> {
            //action.account
            raw.packName(a.getAccount())
                    .packName(a.getName())
                    .packVarint32(a.getAuthorization().size());

            //action.authorization
            a.getAuthorization().forEach(au -> {
                raw.packName(au.getActor())
                        .packName(au.getPermission());
            });
            //action.data
            byte[] dat = Hex.toBytes(a.getData());
            raw.packVarint32(dat.length);
            raw.pack(dat);
        });
        //transaction_extensions
        raw.packVarint32(trx.getTransactionExtensions().size());

        return raw.toHex();
    }

    public static String packApproveArg(ApproveArg arg) {
        Raw raw = new Raw();
        //The account proposing a transaction
        raw.packName(arg.getProposer());
        //The name of the proposal (should be unique for proposer)
        raw.packName(arg.getProposalName());
        //Permission levels expected to approve the proposal
        PermissionLevel permissionLevel = arg.getPermissionLevel();
        raw.packName(permissionLevel.getActor());
        raw.packName(permissionLevel.getPermission());

        return raw.toHex();
    }

    public static String packExexPropose(ExecPropose arg) {
        Raw raw = new Raw();
        //The account proposing a transaction
        raw.packName(arg.getProposer());
        //The name of the proposal (should be unique for proposer)
        raw.packName(arg.getProposalName());
        //- Transaction's checksum
        raw.packName(arg.getExecuter());

        return raw.toHex();
    }


}
