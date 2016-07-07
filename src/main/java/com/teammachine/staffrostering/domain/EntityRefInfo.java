package com.teammachine.staffrostering.domain;

public class EntityRefInfo {

    private long id;

    public EntityRefInfo() {
        //for jackson deserialization
    }

    public EntityRefInfo(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
