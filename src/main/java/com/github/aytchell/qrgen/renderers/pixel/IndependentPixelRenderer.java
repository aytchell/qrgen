package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;

import java.awt.*;

public abstract class IndependentPixelRenderer extends PixelRenderer {
    public IndependentPixelRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    protected abstract void drawActualShape(ImgParameters imgParams, Graphics2D gfx);

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        drawActualShape(imgParams, gfx);
    }
}
