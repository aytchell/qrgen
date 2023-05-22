package com.github.aytchell.qrgen.renderers;

import lombok.Value;

@Value
public class ImgParameters {
    int cellSize;
    int matrixWidthInCells;
    int firstCellX;
    int firstCellY;
    int onColor;
    int offColor;
    int markerColor;
}
