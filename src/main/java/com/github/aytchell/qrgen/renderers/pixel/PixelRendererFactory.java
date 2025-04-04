package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.config.PixelStyle;
import com.github.aytchell.qrgen.renderers.common.ImgParameters;

public class PixelRendererFactory {
    public static PixelRenderer generate(PixelStyle pixelStyle, ImgParameters imgParams) {
        switch (pixelStyle) {
            case RECTANGLES:
                return new DefaultRenderer(imgParams);
            case SMALL_RECTANGLES:
                return new SmallRectanglesRenderer(imgParams);
            case DOTS:
                return new CirclesRenderer(imgParams);
            case ROUND_CORNERS:
                return new RoundCornersRenderer(imgParams);
            case ROWS:
                return new RowsRenderer(imgParams);
            case COLUMNS:
                return new ColumnsRenderer(imgParams);
            case SNAKES:
                return new SnakesRenderer(imgParams);
            case WATER:
                return new WaterRenderer(imgParams);
        }
        throw new RuntimeException("case not handled");
    }
}
