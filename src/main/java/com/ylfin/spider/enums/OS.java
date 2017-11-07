package com.ylfin.spider.enums;

public enum OS {
    WINDOWS("windows"),MAC("mac"),LINUX("linux"),UNKNOWN("未知操作系统");

    private String desc ;

    OS(String desc) {
        this.desc =desc;
    }


    public String getDesc() {
        return desc;
    }

    public static OS getPlatform(){
        String name = System.getProperty("os.name").toLowerCase();
        for (OS os : OS.values()) {
            if (name.startsWith(os.getDesc()))
                return os;
        }
        return OS.UNKNOWN;
    }
}
