package io.jafka.jeos;

import io.jafka.jeos.core.common.SignArg;
import io.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import io.jafka.jeos.core.response.chain.transaction.PushedTransaction;
import io.jafka.jeos.util.KeyUtil;


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
            PushTransactionRequest req = localApi.createAccount(arg, privateKey, "eosio", creator, name, owner, active, 5000, "1.0000 EOS", "1.0000 EOS");
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
            PushTransactionRequest req = localApi.buyRam(arg, privateKey,"eosio",  payer, receiver, ramBytes);
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
            PushTransactionRequest req = localApi.delegate(arg, privateKey,"eosio",  from, receiver, stakeNetQuantity,stakeCpuQuantity);
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
        EosApi api = EosApiFactory.create("https://jungle2.cryptolions.io");
        //System.out.println(api.getChainInfo());
        //buyRam(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking111",5000);
        delegate(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking111","0.5000 EOS","0.5000 EOS");
        //createAccount(api, "5KD2hyi84H46ND8citJr6L84mYegnX1UKw9osLnF3qpcjoeRAAn", "carilking111", "carilking333", "EOS76F9dmfGQRuukSpnkag7M8MnK5ASodrW3t1939hJ5PQKMeHsPA", "EOS76F9dmfGQRuukSpnkag7M8MnK5ASodrW3t1939hJ5PQKMeHsPA");

    }
}
