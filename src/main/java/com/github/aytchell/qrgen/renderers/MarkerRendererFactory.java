package com.github.aytchell.qrgen.renderers;

import com.github.aytchell.qrgen.MarkerStyle;

public class MarkerRendererFactory {
    public static MarkerRenderer create(MarkerStyle markerStyle) {
        return new MarkerRenderer();
    }
}
