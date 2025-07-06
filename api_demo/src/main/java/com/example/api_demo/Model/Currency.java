package com.example.api_demo.model;

public enum Currency {
    USD("美元"),
    GBP("英鎊"),
    EUR("歐元");

    private final String zhName;

    Currency(String zhName) {
        this.zhName = zhName;
    }

    public String getZhName() {
        return zhName;
    }

    public static String getZhNameByCode(String code) {
        if (code == null) {
            return "";
        }

        for (Currency c : values()) {
            if (c.name().equalsIgnoreCase(code)) {
                return c.getZhName();
            }
        }
        return code; // 找不到就回傳原代碼
    }
}