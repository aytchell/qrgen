package com.github.aytchell.qrgen.renderers;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class IndependentPixelRenderer implements PixelRenderer {
    private final Image qrPixel;

    public IndependentPixelRenderer(ImgParameters imgParams) {
        qrPixel = drawPixelTemplate(imgParams);
    }

    @Override
    public Image renderPixel() {
        return qrPixel;
    }

    protected abstract void drawActualShape(ImgParameters imgParams, Graphics2D gfx);

    private BufferedImage drawPixelTemplate(ImgParameters imgParams) {
        BufferedImage img = new BufferedImage(
                imgParams.getCellSize(), imgParams.getCellSize(), BufferedImage.TYPE_INT_ARGB);

        int onColor = imgParams.getOnColor();

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setColor(new Color(onColor, true));
        drawActualShape(imgParams, gfx);
        gfx.dispose();

        return img;
    }
}
