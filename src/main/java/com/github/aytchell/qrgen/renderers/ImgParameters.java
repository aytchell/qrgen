package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.colors.QrColor;
import com.github.aytchell.qrgen.ColorConfig;
import lombok.Value;

@Value
public class ImgParameters {
    int cellSize;
    int matrixWidthInCells;
    int firstCellX;
    int firstCellY;
    ColorConfig colorConfig;

    QrColor getOnColor() {
        return colorConfig.getOnColor();
    }

    public QrColor getOffColor() {
        return colorConfig.getOffColor();
    }

    public QrColor getOuterMarkerColor() {
        return colorConfig.getOuterMarkerColor();
    }

    public QrColor getInnerMarkerColor() {
        return colorConfig.getInnerMarkerColor();
    }
}
