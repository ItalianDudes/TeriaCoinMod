package com.italiandudes.teriacoinmod.util;

public final class Defs {

    public static final String MOD_ID = "teriacoinmod";
    public static final String MOD_NAME = "TeriaCoinMod";
    public static final String VERSION = "1.1R";
    public static final String CLIENT = "com.italiandudes.teriacoinmod.proxy.ClientProxy";
    public static final String COMMON = "com.italiandudes.teriacoinmod.proxy.CommonProxy";

    public static final class TeriaProtocols {

        public static final int OK = 0;
        @SuppressWarnings("unused")
        public static final int INVALID_PROTOCOL = -100;
        public static final String TERIA_REGISTER = "REGISTER";

        public static final class TeriaRegisterCodes {
            public static final int INVALID_USER = -1;
            public static final int INVALID_PASSWORD = -2;
            public static final int UNSAFE_PASSWORD= -3;
            public static final int ALREADY_EXIST = -4;
        }

        public static final String TERIA_LOGIN = "LOGIN";

        public static final class TeriaLoginCodes {
            public static final int INVALID_CREDENTIALS = -1;
        }
        public static final String TERIA_LOGOUT = "LOGOUT";
        public static final String TERIA_BALANCE = "BALANCE";
        public static final String TERIA_SEND = "SEND";

        public static final class TeriaSendCodes {
            public static final int INSUFFICIENT_TC_AVAILABLE = -1;
            public static final int USERNAME_DOES_NOT_EXIST = -2;
            public static final int INVALID_TC_AMOUNT = -3;
        }

        public static final String TERIA_EXCHANGE_TC = "EXCHANGE_TC";

        public static final class TeriaExchangeTCCodes {
            public static final int INSUFFICIENT_TC_AVAILABLE = -1;
            public static final int ITEM_INDEX_NOT_FOUND = -2;
        }

        public static final String TERIA_EXCHANGE_ITEM = "EXCHANGE_ITEM";

        public static final class TeriaExchangeItemCodes {
            public static final int MISSING_REQUESTED_ITEM_AMOUT = -1;
        }

        public static final String TERIA_EXCHANGE_LIST = "EXCHANGE_LIST";

    }

    //PATHS
    public static final String LOG_DIR = "logs/";
    public static final String LOG_LATEST_FILE = LOG_DIR + "latest.log";

    //General Data Communication
    public static final int DEFAULT_CONNECTION_TIMEOUT = 60000; //Expressed in milliseconds

}
