package com.applesphere.applesphere_backend.dto;

public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private String role;
    private String plan;

    public AuthResponse(String token, String email, String name, String role, String plan) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
        this.plan = plan;
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getPlan() { return plan; }
}