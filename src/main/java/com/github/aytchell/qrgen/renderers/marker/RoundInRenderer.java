package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.ImgParameters;

import java.awt.*;

public class RoundInRenderer extends MarkerRenderer {
    private final SingleRoundRenderer roundRenderer = new SingleRoundRenderer();

    @Override
    protected void renderTopLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        roundRenderer.renderSingleMarker(gfx, cellSize, imgParams);
    }

    @Override
    protected void renderTopRightMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderRotated(Math.PI / 2.0, gfx, cellSize, imgParams, roundRenderer::renderSingleMarker);
    }

    @Override
    protected void renderBottomLeftMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        renderRotated(Math.PI / -2.0, gfx, cellSize, imgParams, roundRenderer::renderSingleMarker);
    }
}
