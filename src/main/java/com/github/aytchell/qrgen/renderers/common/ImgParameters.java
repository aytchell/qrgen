package com.github.aytchell.qrgen.renderers.common;

import com.github.aytchell.qrgen.utils.ColorConfig;

import java.awt.*;

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

    public Color getOnColorForAwt() {
        return colorConfig.getOnColorForAwt();
    }

    public Color getOffColorForAwt() {
        return colorConfig.getOffColorForAwt();
    }

    public Color getOuterMarkerColorForAwt() {
        return colorConfig.getOuterMarkerColorForAwt();
    }

    public Color getInnerMarkerColorForAwt() {
        return colorConfig.getInnerMarkerColorForAwt();
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
