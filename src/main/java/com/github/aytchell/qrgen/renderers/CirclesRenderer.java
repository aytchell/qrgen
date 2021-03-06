package com.github.aytchell.qrgen.renderers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CirclesRenderer extends IndependentPixelsRenderer {
    @Override
    protected BufferedImage drawPixelTemplate(ImgParameters imgParams) {
        BufferedImage img = new BufferedImage(imgParams.cellSize, imgParams.cellSize, BufferedImage.TYPE_INT_ARGB);

        int onColor = imgParams.onColor;

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setColor(new Color(onColor, true));
        gfx.fillOval(0, 0, imgParams.cellSize, imgParams.cellSize);
        gfx.dispose();

        return img;
    }
}
