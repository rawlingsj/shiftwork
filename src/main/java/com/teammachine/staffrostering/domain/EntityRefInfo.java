package com.teammachine.staffrostering.domain;

public class EntityRefInfo {

    private long id;

    private String code;

    public EntityRefInfo() {
        //for jackson deserialization
    }

    public EntityRefInfo(long id, String code) {
        this.id = id;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
