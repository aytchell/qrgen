package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.config.MarkerStyle;

public class MarkerRendererFactory {
    public static MarkerRenderer create(MarkerStyle markerStyle) {
        switch (markerStyle) {
            case RECTANGLES:
                return new RectangleRenderer();
            case ROUND_CORNERS:
                return new RoundCornersRenderer();
            case CIRCLES:
                return new CirclesRenderer();
            case DROP_IN:
                return new DropInRenderer();
            case DROP_OUT:
                return new DropOutRenderer();
            case ROUND_IN:
                return new RoundInRenderer();
            case ROUND_OUT:
                return new RoundOutRenderer();
            case EDGE_IN:
                return new EdgeInRenderer();
            case EDGE_OUT:
                return new EdgeOutRenderer();
        }
        throw new RuntimeException("case not handled");
    }
}
