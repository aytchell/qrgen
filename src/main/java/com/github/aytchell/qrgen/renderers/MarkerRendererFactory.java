package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.MarkerStyle;
import com.github.aytchell.qrgen.renderers.marker.MarkerRenderer;
import com.github.aytchell.qrgen.renderers.marker.RectangleRenderer;

public class MarkerRendererFactory {
    public static MarkerRenderer create(MarkerStyle markerStyle) {
        switch (markerStyle) {
            case RECTANGLE: return new RectangleRenderer();
        }
        throw new RuntimeException("case not handled");
    }
}
