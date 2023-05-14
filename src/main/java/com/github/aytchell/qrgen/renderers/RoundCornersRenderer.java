package com.github.aytchell.qrgen.renderers;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class RoundCornersRenderer extends IndependentPixelsRenderer {
    @Override
    protected BufferedImage drawPixelTemplate(ImgParameters imgParams) {
        BufferedImage img = new BufferedImage(imgParams.cellSize, imgParams.cellSize, BufferedImage.TYPE_INT_ARGB);

        int onColor = imgParams.onColor;

        final Graphics2D gfx = img.createGraphics();
        gfx.setComposite(AlphaComposite.Src);
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setColor(new Color(onColor, true));
        double cellSize = imgParams.cellSize - 2;
        double cornerRadius = (cellSize * 2 / 3);
        gfx.fill(new RoundRectangle2D.Double(1, 1, cellSize, cellSize, cornerRadius, cornerRadius));
        gfx.dispose();

        return img;
    }
}
