package com.example.backend.dto;

public class Auth_dto {

    public static class SignUp_Req {
        public String name;
        public String mail;
        public String pass;
    }

    public static class SignUp_Res {
        public boolean success;
        public String message;
    }

    public static class SignIn_Req {
        public String mail;
        public String pass;
    }

    public static class Signin_Res {
        public boolean success;
        public String message;
    }
}