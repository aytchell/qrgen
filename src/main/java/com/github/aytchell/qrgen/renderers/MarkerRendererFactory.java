package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.config.MarkerStyle;
import com.github.aytchell.qrgen.renderers.marker.*;

public class MarkerRendererFactory {
    public static MarkerRenderer create(MarkerStyle markerStyle) {
        switch (markerStyle) {
            case RECTANGLES: return new RectangleRenderer();
            case ROUND_CORNERS: return new RoundCornersRenderer();
            case CIRCLES: return new CirclesRenderer();
            case DROP_IN: return new DropInRenderer();
            case DROP_OUT: return new DropOutRenderer();
        }
        throw new RuntimeException("case not handled");
    }
}
