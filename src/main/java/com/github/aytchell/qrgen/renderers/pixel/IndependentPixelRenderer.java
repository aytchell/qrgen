package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;

import java.awt.*;

public abstract class IndependentPixelRenderer extends PixelRenderer {
    private final Image qrPixel;

    public IndependentPixelRenderer(ImgParameters imgParams) {
        super(imgParams, false, false);
        qrPixel = createImageForPixel(null, this::renderActiveShape);
    }

    @Override
    public Image renderPixel(PixelContext context) {
        if (context.isSet()) {
            return qrPixel;
        } else {
            return null;
        }
    }

    protected abstract void drawActualShape(ImgParameters imgParams, Graphics2D gfx);

    @Override
    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) {
        drawActualShape(imgParams, gfx);
    }
}
