package com.example.integration.enumerator;

public enum AIModel {
    GPT_4o("gpt-4o"),
    GPT_4_TURBO("gpt-4-turbo"),
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),
    GPT_4o_mini("gpt-4o-mini");

    private final String versionName;

    AIModel(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }
}
