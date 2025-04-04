package com.github.aytchell.qrgen.renderers;

import com.google.zxing.common.BitArray;

public class PixelContext {
    private final int rowWidth;
    private final BitArray top;
    private final BitArray mid;
    private final BitArray bottom;

    private int column = 0;

    PixelContext(int rowWidth, BitArray top, BitArray mid, BitArray bottom) {
        this.rowWidth = rowWidth;
        this.top = top;
        this.mid = mid;
        this.bottom = bottom;
    }

    public void shiftRight() {
        ++column;
    }

    public boolean isSet() {
        return mid.get(column);
    }

    public boolean isNeighbourSet(Direction direction) {
        switch (direction) {
            case NW:
                return top != null && (column != 0 && top.get(column - 1));
            case N:
                return top != null && top.get(column);
            case NE:
                return top != null && (column < (rowWidth - 1) && top.get(column + 1));
            case W:
                return column != 0 && mid.get(column - 1);
            case E:
                return column < (rowWidth - 1) && mid.get(column + 1);
            case SW:
                return bottom != null && (column != 0 && bottom.get(column - 1));
            case S:
                return bottom != null && bottom.get(column);
            case SE:
                return bottom != null && (column < (rowWidth - 1) && bottom.get(column + 1));
        }
        throw new RuntimeException("case not handled");
    }

    public enum Direction {
        NW, N, NE,
        W, E,
        SW, S, SE
    }
}
