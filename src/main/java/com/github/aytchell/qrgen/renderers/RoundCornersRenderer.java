package com.github.aytchell.qrgen.renderers;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundCornersRenderer extends IndependentPixelsRenderer {
    @Override
    protected void drawActualShape(ImgParameters imgParams, Graphics2D gfx) {
        double cellSize = imgParams.getCellSize();
        double cornerRadius = cellSize * 2 / 3;
        gfx.fill(new RoundRectangle2D.Double(1, 1, cellSize, cellSize, cornerRadius, cornerRadius));
    }
}
