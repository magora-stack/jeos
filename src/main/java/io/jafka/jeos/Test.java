package io.jafka.jeos;

import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import io.jafka.jeos.core.response.chain.account.Key;
import io.jafka.jeos.core.response.chain.transaction.PushedTransaction;
import io.jafka.jeos.util.KeyUtil;

import java.util.ArrayList;
import java.util.List;


public class Test {

    private static void generatePrivateKey() {
        String privateKey = KeyUtil.createPrivateKey();
        String publicKey = KeyUtil.toPublicKey(privateKey);
        System.out.println("private_key: " + privateKey);
        System.out.println("public_key: " + publicKey);
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

    private static void updateMultipleAuth(EosApi api, String privateKey, String account, long threshold, List<String> keys, String permission) {

        try {
            SignArg arg = api.getSignArg(1200);
            LocalApi localApi = EosApiFactory.createLocalApi();
            List<Key> pubkeys = new ArrayList<>();
            for (String key : keys) {
                pubkeys.add(new Key(key, 1));
            }
            PushTransactionRequest req = localApi.updateMultipleAuth(arg, privateKey, account, threshold, pubkeys, permission);
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
        //buyRam(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking111",5000);
        delegate(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking111","0.5000 EOS","0.5000 EOS");
        //createAccount(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking333", "EOS76F9dmfGQRuukSpnkag7M8MnK5ASodrW3t1939hJ5PQKMeHsPA", "EOS76F9dmfGQRuukSpnkag7M8MnK5ASodrW3t1939hJ5PQKMeHsPA");*/
        //EosApi api = EosApiFactory.create("https://api.jungle.alohaeos.com");
        EosApi api = EosApiFactory.create("https://eos.newdex.one");
        //System.out.println(api.getChainInfo());
        //delegate(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking444","1.5000 EOS","1.5000 EOS");
        //updateAuth(api,"5JhoFQMN9xWGMFfUDJHHPVsuSytSDot8Q5TNv3rN6VVGPbjTrGN","carilking444","EOS82Psyaqk86jbSdSmGzonNCUHsBp1Xj42q37g6UkiA1UhzLe57j","active");
        //updateAuth(api,"5JPNqMSZ8M567hgDGW9CmD9vr2RaDm1eWpJqHaHa2S5xKTMmFKm","heydoojqgege","EOS761qgPS6EtSW8hw8EEoQFMdkMuFKyobeMMw7ankQHL8md2ddAh","active");
        List<String> pubkeys = new ArrayList<>();
        pubkeys.add("EOS79XjXkqRuY4Somb3V7aY9CTQHks8dVFyWFN4mHmqgx2xxK1hrF");
        pubkeys.add("EOS5bJrCaRBMeQYWu4Gy8oR48TgnxxYuSkex16obWMf8EtzmMh5jH");
        updateMultipleAuth(api, "5JPNqMSZ8M567hgDGW9CmD9vr2RaDm1eWpJqHaHa2S5xKTMmFKm", "heydoojqgege", 2, pubkeys, "active");
    }
}
