package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.colors.QrColor;
import com.github.aytchell.qrgen.config.ColorConfig;

public class ImgParameters {
    private final int cellSize;
    private final int matrixWidthInCells;
    private final int firstCellX;
    private final int firstCellY;
    private final ColorConfig colorConfig;

    public ImgParameters(
            int cellSize,
            int matrixWidthInCells,
            int firstCellX,
            int firstCellY,
            ColorConfig colorConfig) {
        this.cellSize = cellSize;
        this.matrixWidthInCells = matrixWidthInCells;
        this.firstCellX = firstCellX;
        this.firstCellY = firstCellY;
        this.colorConfig = colorConfig;
    }

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

    public int getCellSize() {
        return cellSize;
    }

    public int getMatrixWidthInCells() {
        return matrixWidthInCells;
    }

    public int getFirstCellX() {
        return firstCellX;
    }

    public int getFirstCellY() {
        return firstCellY;
    }
}
