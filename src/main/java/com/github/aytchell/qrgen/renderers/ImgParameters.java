package com.github.aytchell.qrgen.renderers;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
class ImgParameters {
    int cellSize;
    int matrixWidthInCells;
    int firstCellX;
    int firstCellY;
    int onColor;
    int offColor;

    public ImgParameters(
            int cellSize, int matrixWidthInCells, int firstX, int firstY, int onColor, int offColor) {
        this.cellSize = cellSize;
        this.matrixWidthInCells = matrixWidthInCells;
        this.firstCellX = firstX;
        this.firstCellY = firstY;
        this.onColor = onColor;
        this.offColor = offColor;
    }
}
