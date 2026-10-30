package com.devops;

public class App {
    public static final String MESSAGE = "Bonjour et bon courage dans votre projet en DevOps";

    public static String getMessage() {
        return MESSAGE;
    }

    public static void main(String[] args) {
        System.out.println(getMessage());
    }
}
