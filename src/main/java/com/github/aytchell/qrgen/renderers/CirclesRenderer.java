package com.github.aytchell.qrgen.renderers;

import java.awt.*;

public class CirclesRenderer extends IndependentPixelsRenderer {
    @Override
    protected void drawActualShape(ImgParameters imgParams, Graphics2D gfx) {
        gfx.fillOval(0, 0, imgParams.cellSize, imgParams.cellSize);
    }
}
