package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.common.ImgParameters;

import java.awt.*;

public class RoundOutRenderer extends MarkerRenderer {
    private final SingleRoundRenderer roundRenderer = new SingleRoundRenderer();

    @Override
    protected void renderTopLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderRotated(Math.PI, gfx, cellSize, imgParams, roundRenderer::renderSingleMarker);
    }

    @Override
    protected void renderTopRightMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderRotated(Math.PI / -2.0, gfx, cellSize, imgParams, roundRenderer::renderSingleMarker);
    }

    @Override
    protected void renderBottomLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderRotated(Math.PI / 2.0, gfx, cellSize, imgParams, roundRenderer::renderSingleMarker);
    }
}
