package com.github.aytchell.qrgen.renderers;

import java.awt.*;

public class SmallRectanglesRenderer extends IndependentPixelsRenderer {
    @Override
    protected void drawActualShape(ImgParameters imgParams, Graphics2D gfx) {
        gfx.fillRect(0, 0, imgParams.cellSize - 1, imgParams.cellSize - 1);
    }
}
