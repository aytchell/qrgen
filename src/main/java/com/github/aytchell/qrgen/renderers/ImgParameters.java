package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.ArgbValue;
import com.github.aytchell.qrgen.ColorConfig;
import lombok.Value;

@Value
public class ImgParameters {
    int cellSize;
    int matrixWidthInCells;
    int firstCellX;
    int firstCellY;
    ColorConfig colorConfig;

    ArgbValue getOnColor() {
        return colorConfig.getOnColor();
    }

    public ArgbValue getOffColor() {
        return colorConfig.getOffColor();
    }

    public ArgbValue getMarkerColor() {
        return colorConfig.getMarkerColor();
    }
}
