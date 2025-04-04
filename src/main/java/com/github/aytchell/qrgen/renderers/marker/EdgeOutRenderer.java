package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.common.ImgParameters;

import java.awt.*;

public class EdgeOutRenderer extends MarkerRenderer {
    private final SingleEdgeRenderer edgeRenderer = new SingleEdgeRenderer();

    @Override
    protected void renderTopLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        edgeRenderer.renderSingleMarker(gfx, cellSize, imgParams);
    }

    @Override
    protected void renderTopRightMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderRotated(Math.PI / 2.0, gfx, cellSize, imgParams, edgeRenderer::renderSingleMarker);
    }

    @Override
    protected void renderBottomLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderRotated(Math.PI / -2.0, gfx, cellSize, imgParams, edgeRenderer::renderSingleMarker);
    }
}
