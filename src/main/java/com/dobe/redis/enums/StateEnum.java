package com.dobe.redis.enums;

public enum StateEnum {
    
    RUNNING("1"),
    STOP("0");
    
    private String state;
    
    StateEnum(String state){
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
