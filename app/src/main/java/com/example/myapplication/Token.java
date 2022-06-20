package com.example.myapplication;

import com.google.android.gms.tasks.Task;

public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public Token(Task<String> token) {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
