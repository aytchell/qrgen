package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.PixelStyle;

public class RendererFactory {
    public static QrCodeRenderer createRenderer(PixelStyle style) {
        switch (style) {
            case RECTANGLES: return new DefaultRenderer();
            case SMALL_RECTANGLES: return new SmallRectanglesRenderer();
            case DOTS: return new CirclesRenderer();
            case ROUND_CORNERS: return new RoundCornersRenderer();
        }
        return new DefaultRenderer();
    }
}
