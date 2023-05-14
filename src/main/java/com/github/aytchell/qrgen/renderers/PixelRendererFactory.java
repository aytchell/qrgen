package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.PixelStyle;

public class PixelRendererFactory {
    public static PixelRenderer generate(PixelStyle pixelStyle, ImgParameters imgParams) {
        switch (pixelStyle) {
            case DOTS: return new CirclesRenderer(imgParams);
            case SMALL_RECTANGLES: return new SmallRectanglesRenderer(imgParams);
            case ROUND_CORNERS: return new RoundCornersRenderer(imgParams);
            case ROWS: return new RowsRenderer(imgParams);
            case SNAKES: return new SnakesRenderer(imgParams);
            case WATER: return new WaterRenderer(imgParams);
        }
        throw new RuntimeException("case not handled");
    }
}
