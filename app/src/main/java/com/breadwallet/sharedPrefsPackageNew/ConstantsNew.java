package com.breadwallet.sharedPrefsPackageNew;

public class ConstantsNew {


    public static String WALLET_CREATED_KEY = "walletCreatedKey";

    public static String CREATED = "0";

    public static String PIN_KEY = "pin";

    public static String STATUS = "status";

    public static String ONLINE = "online";

    public static String OFFLINE = "offline";

    public static String LOGIN_CREATE_KEY = "loginCreateKey";

    public static String LOGIN_VALUE = "login";

    public static String CREATE_VALUE = "create";

    public static String WALLET_NAME_KEY = "wallet";

    public static String COlOR_ID = "color";

    public static String ADDRESS = "address";

    public static String BALANCE_SAVE_NODE_WALLET = "balance";

    public static String TRANSACTION_OBJECT = "transaction";

    public static String ON_STOP_KEY= "onStopKey";

    public static String ON_STOP = "onStop";

    public static String SECRET = "";

    public static String USD_BALANCE = "usdBalance";

    public static String USD_VALUE = "usdValue";




    /*** URLs ***/

//    public static String CREATE_SAVE_NODE_WALLET_URL = "http://144.217.182.179:8090/api/savenode/CreateWallet";
    public static String CREATE_SAVE_NODE_WALLET_URL = "https://savenode.vaival.com/savenode/api/CreateWallet";
//    public static String CREATE_SAVE_NODE_WALLET_URL = "https://sandbox.tranzfirst.com/coin/savenode/api/CreateWallet";

//    public static String GET_BALANCE_SAVE_NODE = "http://144.217.182.179:8090/api/savenode/getBalance/";
    public static String GET_BALANCE_SAVE_NODE = "https://savenode.vaival.com/savenode/api/getBalance/";

//    public static String TRANSFER_SAVE_NODE = "http://144.217.182.179:8090/api/savenode/transfer";
    public static String TRANSFER_SAVE_NODE = "https://savenode.vaival.com/savenode/api/transfer";

    public static String CREATE_WALLET_BTC = "http://144.217.182.179:8085/api/btcmain/create_wallet";

    public static String GET_BALANCE_BTC = "http://144.217.182.179:8085/api/btcmain/getBalance/";

    public static String TRANSFER_BTC = "http://144.217.182.179:8085/api/btcmain/transfer";

    public static String GET_BALANCE_BTC_TEST_NET = "http://144.217.182.179:8085/api/btc/getBalance/";

    public static String CREATE_WALLET_BTC_TEST_NET = "http://144.217.182.179:8085/api/btc/create_wallet";

    public static String TRANSFER_BTC_TEST_NET = "http://144.217.182.179:8085/api/btc/transfer";

    public static String CREATE_WALLET_BCH_TEST_NET = "http://144.217.182.179:8085/api/testbch/create_wallet";

    public static String GET_BALANCE_BCH_TEST_NET = "http://144.217.182.179:8085/api/testbch/getBalance/";

    public static String TRANSFER_BCH_TEST_NET = "http://144.217.182.179:8085/api/testbch/transfer";

    public static String USD_BTC = "https://api.cryptonator.com/api/ticker/usd-btc";

    public static String USD_BCH = "https://api.cryptonator.com/api/ticker/usd-btc";

    public static String USD_ETH = "https://api.cryptonator.com/api/ticker/usd-btc";



    /*** wallet information ***/

    // save node

    public static String SAVE_NODE_WALLET_OBJECT_KEY = "saveNodeWallet";

    public static String SAVE_NODE_WALLET_BALANCE_KEY = "saveNodeWalletBalance";

    public static String SAVE_NODE_WALLET_ADDRESS = "saveNodeWalletAddress";

    public static String SAVE_NODE_BALANCE_USD = "saveNodeBalanceUsd";

    //btc

    public static String BTC_WALLET_PRIVATE_ADDRESS = "btcWalletPrivateKey";

    public static String BTC_WALLET_BALANCE_KEY = "btcWalletBalanceKey";

    public static String BTC_WALLET_PUBLIC_ADDRESS = "btcWalletPublicKey";

    public static String BTC_BALANCE_USD = "btchBalanceUsd";


    //bch

    public static String BCH_WALLET_PRIVATE_ADDRESS = "bchWalletPrivateKey";

    public static String BCH_WALLET_BALANCE_KEY = "bchWalletBalanceKey";

    public static String BCH_WALLET_PUBLIC_ADDRESS = "bchWalletPublicKey";

    public static String BCH_BALANCE_USD = "bchBalanceUsd";

}
