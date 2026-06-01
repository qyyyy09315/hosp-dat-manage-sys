package com.sxt.common;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    private static final int BCRYPT_ROUNDS = 10;

    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    public static boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java PasswordUtils <password>");
            return;
        }
        System.out.println(hash(args[0]));
    }
}
