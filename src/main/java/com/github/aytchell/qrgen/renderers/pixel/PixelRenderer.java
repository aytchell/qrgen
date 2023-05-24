package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
public abstract class PixelRenderer {
    private final ImgParameters imgParams;

    public void renderPixel(PixelContext context, Graphics2D gfx) {
        if (context.isSet()) {
            renderActiveShape(imgParams, context, gfx);
        } else {
            renderInactiveShape(imgParams, context, gfx);
        }
    }

    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) { }

    protected void renderInactiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) { }
}
