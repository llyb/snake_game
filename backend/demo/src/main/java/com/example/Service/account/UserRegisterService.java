package com.example.Service.account;

import java.util.Map;

public interface UserRegisterService {
    public Map<String, String> register(String username, String password, String confirmedPassword);
}
