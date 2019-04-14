package com.dobe.redis.enums;

import java.util.Objects;
import java.util.stream.Stream;

public enum TypeEnum {
    /**单实例**/
    SINGLE("SINGLE", "单机"),
    /**集群**/
    CLUSTER("CLUSTER", "集群"),
    /**哨兵模式**/
    SENTIAL("SENTIAL", "哨兵(暂不支持)");

    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    TypeEnum(String name, String desc) {
        this.name = name;
    }

    public static TypeEnum parse(String name) {
        return Stream.of(TypeEnum.values()).filter(o -> Objects.equals(name, o.getName())).findAny().orElse(null);
    }
    
}
