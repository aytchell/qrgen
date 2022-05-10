package com.github.aytchell.qrgen;

public class ArgbValue {
    private final int rawValue;

    public ArgbValue(int transparency, int red, int green, int blue) {
        rawValue =
                (toUnsignedByte(transparency) << 24) |
                        (toUnsignedByte(red) << 16) |
                        (toUnsignedByte(green) <<  8) |
                        toUnsignedByte(blue);
    }

    public int getRawValue() {
        return rawValue;
    }

    private int toUnsignedByte(int value) {
        if (value < 0) return 0;
        if (value > 255) return 255;
        return value;
    }
}
