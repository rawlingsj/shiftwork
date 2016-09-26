package com.teammachine.staffrostering.domain;

public class EntityRefInfo {

    private long id;

    private String code;

    private String name;

    public EntityRefInfo() {
        //for jackson deserialization
    }

    public EntityRefInfo(long id, String code) {
        this.id = id;
        this.code = code;
    }

    public EntityRefInfo(long id, String code, String name) {
        this(id, code);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
