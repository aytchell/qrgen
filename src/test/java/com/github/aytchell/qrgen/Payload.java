package com.github.aytchell.qrgen;

import java.nio.charset.StandardCharsets;

public class Payload {
    private static final String count50 = "012345678901234567890123456789012345678901234567890123456789";
    private static final String count200 = count50 + count50 + count50 + count50;
    private static final String count1000 = count200 + count200 + count200 + count200 + count200;
    private static final String count5000 = count1000 + count1000 + count1000 + count1000 + count1000;
    private static final String count10000 = count5000 + count5000;
    private static final byte[] raw10000 = count10000.getBytes(StandardCharsets.UTF_8);

    public static String getWithLength(int length) {
        if (length > raw10000.length)
            throw new RuntimeException("requested paylaod size too long");

        return new String(raw10000, 0, length, StandardCharsets.UTF_8);
    }
}
