package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.MarkerStyle;
import com.github.aytchell.qrgen.renderers.marker.*;

public class MarkerRendererFactory {
    public static MarkerRenderer create(MarkerStyle markerStyle) {
        switch (markerStyle) {
            case RECTANGLES: return new RectangleRenderer();
            case ROUND_CORNERS: return new RoundCornersRenderer();
            case CIRCLES: return new CirclesRenderer();
        }
        throw new RuntimeException("case not handled");
    }
}
