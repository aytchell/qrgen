package com.github.aytchell.qrgen.renderers;

import java.awt.*;

public class SmallRectanglesRenderer extends IndependentPixelsRenderer {
    @Override
    protected void drawActualShape(ImgParameters imgParams, Graphics2D gfx) {
        gfx.fillRect(0, 0, imgParams.getCellSize() - 1, imgParams.getCellSize() - 1);
    }
}
