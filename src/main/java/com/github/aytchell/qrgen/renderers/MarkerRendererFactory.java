package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.MarkerStyle;
import com.github.aytchell.qrgen.renderers.marker.MarkerRenderer;

public class MarkerRendererFactory {
    public static MarkerRenderer create(MarkerStyle markerStyle) {
        return new MarkerRenderer();
    }
}
