package com.example.Service.user.account;

import java.util.Map;

public interface UserLoginService {
    public Map<String, String> getToken(String username, String password);
}
