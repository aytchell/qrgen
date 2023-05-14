package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.PixelStyle;

public class RendererFactory {
    public static QrCodeRenderer createRenderer(PixelStyle style) {
        if (style == PixelStyle.RECTANGLES) {
            return new DefaultRenderer();
        }
        return new GenericQrMatrixRenderer(style);
    }
}
