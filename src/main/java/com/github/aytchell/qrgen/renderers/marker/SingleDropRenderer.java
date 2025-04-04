package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.utils.SvgPath2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static com.github.aytchell.qrgen.renderers.marker.MarkerRenderer.SIZE_OF_POSITION_MARKER;

public class SingleDropRenderer {
    private static final String OUTER_PATH = "m 110.496,502.5 h 0.102 z m 196.07854,24.07636 c -59.79911,0 " +
            "-111.97227,-21.85557 -155.07714,-64.95314 -0.0585,-0.0555 -0.11062,-0.11768 -0.17265,-0.17355 " +
            "-42.39186,-41.61096 -63.8871,-93.72913 -63.8871,-154.8989 0,-60.63322 -1.465216,-82.57983 " +
            "0.21528,-220.616692 108.81862,0.845474 158.33361,1.479962 218.92161,1.479962 62.03033,0 " +
            "112.98164,21.18772 155.77484,64.77704 42.62765,43.43049 64.24384,95.36184 64.24384,154.35969 " +
            "0,60.39149 -21.55774,112.56191 -64.07029,155.07245 -43.09424,43.10465 -95.56135,64.95315 " +
            "-155.94838,64.95315 z M 306.504,0 C 221.648,0 188.32915,-0.48676926 1.4487122,-1.4939293 " +
            "2.5517065,263.15953 0,222.754 0,306.5 c 0,85.477 30.1953,158.473 89.7461,216.961 60.1249,60.086 " +
            "133.0589,90.543 216.7579,90.543 84.414,0 157.715,-30.496 217.851,-90.645 59.485,-59.472 " +
            "89.649,-132.437 89.649,-216.859 0,-82.633 -30.098,-155.191 -89.453,-215.6641 C 465.383,30.5625 " +
            "392.02,0 306.504,0";

    protected void renderSingleMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        final int markerSize = cellSize * SIZE_OF_POSITION_MARKER;
        final int innerMarkerSize = cellSize * (SIZE_OF_POSITION_MARKER - 4);

        final AffineTransform transform = gfx.getTransform();

        final double factor = markerSize / 82.0;
        gfx.scale(factor, factor);
        gfx.transform(new AffineTransform(1.3333333, 0, 0, -1.3333333, 0, 81.866667));

        gfx.scale(0.1, 0.1);
        gfx.setColor(imgParams.getOuterMarkerColorForAwt());
        gfx.fill(SvgPath2D.drawSvgCommand(OUTER_PATH));
        gfx.setTransform(transform);

        gfx.setColor(imgParams.getInnerMarkerColorForAwt());
        gfx.fillOval(cellSize * 2, cellSize * 2, innerMarkerSize, innerMarkerSize);
    }
}
