package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.PixelStyle;

public class RendererFactory {
    public static QrCodeRenderer createRenderer(PixelStyle style) {
        switch (style) {
            case RECTANGLES: return new DefaultRenderer();
            case SMALL_RECTANGLES: return new GenericQrMatrixRenderer(PixelStyle.SMALL_RECTANGLES);
            case DOTS: return new GenericQrMatrixRenderer(PixelStyle.DOTS);
            case ROUND_CORNERS: return new GenericQrMatrixRenderer(PixelStyle.ROUND_CORNERS);
        }
        return new DefaultRenderer();
    }
}
