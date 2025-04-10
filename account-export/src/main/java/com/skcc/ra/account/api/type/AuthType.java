package com.skcc.ra.account.api.type;

public enum AuthType {

    IT_ADMIN("C001"),

    AUTH_CONFIRM("C002"),

    AUTH_APPROVE("C003"),

    AUTH_INDIV("C004"),

    AUTH_RPA("C005"),

    TSE_SMALL_TEAM_LEADER("S037"),

    TSE_TEAM_MEMBER("S038"),

    BP_COMMON("S040"),

    KNOWLEDGE_MANAGEMENT_BP_TSE_SEARCH("I016"),

    AUTH_DEVELOPER("C999"),

    AUTH_DEFAULT("Z001");

    private final String code;

    AuthType(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

}
