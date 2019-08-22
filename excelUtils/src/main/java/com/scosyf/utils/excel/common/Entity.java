package com.scosyf.utils.excel.common;

/**
 * @program: commonUtils
 * @description: 模型
 * @author: kunbu
 * @create: 2019-08-22 10:41
 **/
public class Entity {

    private Integer id;
    private String name;
    private String address;
    private String desc;

    public Entity(Integer id, String name, String address, String desc) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
