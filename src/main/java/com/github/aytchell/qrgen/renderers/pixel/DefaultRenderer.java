package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;

import java.awt.*;

public class DefaultRenderer extends IndependentPixelRenderer {
    public DefaultRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    @Override
    protected void drawActualShape(ImgParameters imgParams, Graphics2D gfx) {
        gfx.fillRect(0, 0, imgParams.getCellSize(), imgParams.getCellSize());
    }
}
