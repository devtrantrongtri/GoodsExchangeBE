package com.uth.BE.Config;

public class SecurityEndpoints {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/user/sign-up",  // register
            "/user/getAllUser",
            "/auth/authenticate",
            "products/**",
            "/auth/authenticate", // login

    };

    public static final String[] ADMIN_ENDPOINTS = {
            "/admin/**",

    };

    public static final String[] MODERATOR_ENDPOINTS = {
            "/user/moderator/**",
            "/user/getUserById/**", // get user detail
    };

    public static final String[] CLIENT_ENDPOINTS = {
            "/chat/between/**",
            "user/**",
            "/userProfiles/**"
    };
}
