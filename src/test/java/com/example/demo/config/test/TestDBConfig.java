package com.example.demo.config.test;

public class TestDBConfig {
    public static final String DB_NAME = System.getenv().getOrDefault("TEST_DB_NAME", "test");
    public static final String USERNAME = System.getenv().getOrDefault("TEST_DB_USER", "testUser");
    public static final String PASSWORD = System.getenv().getOrDefault("TEST_DB_PASS", "78qorthd!");
}