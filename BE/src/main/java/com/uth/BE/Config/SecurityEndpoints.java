package com.uth.BE.Config;

public class SecurityEndpoints {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/user/sign-up",
            "/user/getAllUser",
            "/auth/authenticate",
            "products/**"
    };

    public static final String[] ADMIN_ENDPOINTS = {
            "/user/admin/**",

    };

    public static final String[] MODERATOR_ENDPOINTS = {
            "/user/moderator/**"
    };

    public static final String[] CLIENT_ENDPOINTS = {
            "/user/user/**"
    };
}
