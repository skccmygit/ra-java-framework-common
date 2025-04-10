package com.skcc.ra.account.api.type;

public enum MsgFormType {

    SEND_AUTH_NUM("C001"),

    ACCOUNT_REQUEST_FINISH("C002"),

    ACCOUNT_CONFIRM_APPLY("C003"),

    ACCOUNT_CONFIRM_FINISH("C004"),

    ACCOUNT_APPROVE_APPLY("C005"), // 승인자는 IT담당이라 안보내고 있는듯

    ACCOUNT_APPROVE_FINISH("C006"),
    PASSWORD_INIT("C007"),
    ACCOUNT_REJECT("C008");

    private final String code;

    MsgFormType(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    }
