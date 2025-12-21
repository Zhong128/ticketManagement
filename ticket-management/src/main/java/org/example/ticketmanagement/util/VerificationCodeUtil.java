package org.example.ticketmanagement.util;

import java.util.Random;

public class VerificationCodeUtil {

    private static final String NUMBERS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS_AND_LETTERS = NUMBERS + LETTERS;

    /**
     * 生成纯数字验证码
     */
    public static String generateNumericCode(int length) {
        return generateCode(NUMBERS, length);
    }

    /**
     * 生成字母和数字混合验证码
     */
    public static String generateMixedCode(int length) {
        return generateCode(NUMBERS_AND_LETTERS, length);
    }

    private static String generateCode(String source, int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(source.length());
            code.append(source.charAt(index));
        }

        return code.toString();
    }

    /**
     * 生成6位数字验证码（最常用）
     */
    public static String generateSixDigitCode() {
        return generateNumericCode(6);
    }

    /**
     * 生成4位数字验证码
     */
    public static String generateFourDigitCode() {
        return generateNumericCode(4);
    }
}