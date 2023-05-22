package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.PixelContext;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
public abstract class PixelRenderer {
    private final ImgParameters imgParams;
    private final boolean willWriteActivePixel;
    private final boolean willWriteInactivePixel;

    public Image renderPixel(PixelContext context) {
        if (context.isSet()) {
            if (willWriteActivePixel)
                return createImageForPixel(context, this::renderActiveShape);
        } else {
            if (willWriteInactivePixel)
                return createImageForPixel(context, this::renderInactiveShape);
        }
        return null;
    }

    protected void renderActiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) { }

    protected void renderInactiveShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx) { }

    protected Image createImageForPixel(PixelContext context, ShapeRenderer shapeRenderer) {
        BufferedImage img = new BufferedImage(
                imgParams.getCellSize(), imgParams.getCellSize(), BufferedImage.TYPE_INT_ARGB);

        int onColor = imgParams.getOnColor();

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setColor(new Color(onColor, true));
        shapeRenderer.drawActualShape(imgParams, context, gfx);
        gfx.dispose();

        return img;
    }

    @FunctionalInterface
    protected interface ShapeRenderer {
        void drawActualShape(ImgParameters imgParams, PixelContext context, Graphics2D gfx);
    }
}
