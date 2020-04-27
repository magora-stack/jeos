package io.jafka.jeos;

import com.sun.javafx.binding.StringFormatter;
import io.jafka.jeos.core.common.Authorization;
import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
import io.jafka.jeos.core.request.chain.json2bin.TransferArg;
import io.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import io.jafka.jeos.core.request.history.TransactionRequest;
import io.jafka.jeos.core.response.chain.account.Key;
import io.jafka.jeos.core.response.chain.transaction.PushedTransaction;
import io.jafka.jeos.util.KeyUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Test {

    private static void generatePrivateKey() {
        String privateKey = KeyUtil.createPrivateKey();
        String publicKey = KeyUtil.toPublicKey(privateKey);
        System.out.println("private_key: " + privateKey);
        System.out.println("public_key: " + publicKey);
    }

    private static void transfer(EosApi api, String privateKey, String from, String to, BigDecimal value, String memo) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            String amount = new DecimalFormat("##0.0000 EOS").format(value);
            PushTransactionRequest req = localApi.transfer(arg, privateKey, "eosio.token", from, to, amount, memo);
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createAccount(EosApi api, String privateKey, String creator, String name, String owner, String active) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            PushTransactionRequest req = localApi.createAccount(arg, privateKey, creator, name, owner, active, 5000, "1.0000 EOS", "1.0000 EOS");
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void buyRam(EosApi api, String privateKey, String payer, String receiver, int ramBytes) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            PushTransactionRequest req = localApi.buyRam(arg, privateKey, payer, receiver, ramBytes);
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void delegate(EosApi api, String privateKey, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            PushTransactionRequest req = localApi.delegate(arg, privateKey, from, receiver, stakeNetQuantity, stakeCpuQuantity);
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateAuth(EosApi api, String privateKey, String account, String publicKey, String permission) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            PushTransactionRequest req = localApi.updateAuth(arg, privateKey, account, publicKey, permission);
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateMultipleAuth(EosApi api, String privateKey, String account, long threshold, List<String> accounts, String permission) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            PushTransactionRequest req = localApi.updateMultipleAuth(arg, privateKey, account, threshold, accounts, permission);
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void proposeTransfer(EosApi api, String privateKey, String proposer, String proposalName, String from, String to, BigDecimal value, String memo, List<String> accounts) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            List<Authorization> requests = new ArrayList<>();
            for (String account : accounts) {
                Authorization auth = new Authorization();
                auth.setActor(account);
                auth.setPermission("active");
                requests.add(auth);
            }
            String amount = new DecimalFormat("##0.0000 EOS").format(value);
            //交易有效期24小时
            PackedTransaction trx = localApi.createTransfer(api.getSignArg(24 * 3600), "eosio.token", from, to, amount, memo);
            PushTransactionRequest req = localApi.propose(arg, privateKey, proposer, proposalName, requests, trx, "active");
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void approveTransfer(EosApi api, String privateKey, String approver, String proposer, String proposalName) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            PushTransactionRequest req = localApi.approve(arg, privateKey, approver, proposer, proposalName, "active");
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void execPropose(EosApi api, String privateKey, String executer, String proposer, String proposalName) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            PushTransactionRequest req = localApi.execPropose(arg, privateKey, executer, proposer, proposalName, "active");
            PushedTransaction tx = api.pushTransaction(req);
            if (tx != null && tx.getTransactionId() != null) {
                System.out.println(tx.getTransactionId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //generatePrivateKey();
        /*EosApi api = EosApiFactory.create("https://jungle2.cryptolions.io");
        //System.out.println(api.getChainInfo());

        delegate(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking111","0.5000 EOS","0.5000 EOS");
        //createAccount(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking333", "EOS76F9dmfGQRuukSpnkag7M8MnK5ASodrW3t1939hJ5PQKMeHsPA", "EOS76F9dmfGQRuukSpnkag7M8MnK5ASodrW3t1939hJ5PQKMeHsPA");*/
        EosApi api = EosApiFactory.create("https://api.jungle.alohaeos.com");
        //EosApi api = EosApiFactory.create("https://jungle.cryptolions.io");
        //EosApi api = EosApiFactory.create("https://eos.newdex.one");
        //System.out.println(api.getChainInfo());
        //buyRam(api, "5K3iYbkjpvZxxAJyHQVAtJmCi4CXetBKq3CfcboMz21Y5Pjvovo", "bihuexwallet", "bihuexwallet",15000);
        //delegate(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking444","1.5000 EOS","1.5000 EOS");
        //updateAuth(api,"5JhoFQMN9xWGMFfUDJHHPVsuSytSDot8Q5TNv3rN6VVGPbjTrGN","carilking444","EOS82Psyaqk86jbSdSmGzonNCUHsBp1Xj42q37g6UkiA1UhzLe57j","active");
        //updateAuth(api,"5JrTcSsUmzoLDxsNFcpGBRt2Wd488qTmHp5yfBPy71MbxaqSJ4g","carilking555","EOS5X6Sbmbc2zaJ8EHNZmdSnA26DsuTim59pdUNiNd34HugzvTp5m","active");
        //transfer(api,"5JhoFQMN9xWGMFfUDJHHPVsuSytSDot8Q5TNv3rN6VVGPbjTrGN", "carilking444","carilking111",BigDecimal.valueOf(0.01),"喵喵喵~");
        //Object obj=api.getTransaction(new TransactionRequest("e5aeee319e8c767cdda35a3b6d460328f958833e58723bc18581765494018700"));
        //System.out.println(obj);
        List<String> accounts = new ArrayList<>();
        accounts.add("carilking111");
        accounts.add("carilking444");
        //updateMultipleAuth(api, "5JPNqMSZ8M567hgDGW9CmD9vr2RaDm1eWpJqHaHa2S5xKTMmFKm", "heydoojqgege", 2, accounts, "active");
        //proposeTransfer(api, "5K3iYbkjpvZxxAJyHQVAtJmCi4CXetBKq3CfcboMz21Y5Pjvovo", "bihuexwallet", "firstmsig152", "heydoojqgege", "carilking222", BigDecimal.valueOf(0.2), "test1", accounts);
        //approveTransfer(api,"5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn","carilking111","bihuexwallet","firstmsig151");
        //execPropose(api,"5K3iYbkjpvZxxAJyHQVAtJmCi4CXetBKq3CfcboMz21Y5Pjvovo","bihuexwallet","bihuexwallet","firstmsig151");
    }
}
