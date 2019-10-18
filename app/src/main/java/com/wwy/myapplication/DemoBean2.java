package com.wwy.myapplication;

public class DemoBean2 {
    public String name;
    public Class<?> clazz;

    public DemoBean2(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
       return name;
    }
}