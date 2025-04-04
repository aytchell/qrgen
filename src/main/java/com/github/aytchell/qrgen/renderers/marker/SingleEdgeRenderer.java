package com.github.aytchell.qrgen.renderers.marker;

import com.github.aytchell.qrgen.renderers.ImgParameters;
import com.github.aytchell.qrgen.renderers.utils.SvgPath2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static com.github.aytchell.qrgen.renderers.marker.MarkerRenderer.SIZE_OF_POSITION_MARKER;

public class SingleEdgeRenderer {
    private static final String OUTER_PATH = "m 0,-61.400002 l 0.12695312,42.358986 C 0.15829613,-8.5831545 " +
            "8.6827168,-0.02734375 19.140625,-0.02734375 h 23.21875 c 10.457908,0 19.013672,-8.55576375 " +
            "19.013672,-19.01367225 v -23.216796 c 0,-10.457909 -6.987637,-19.14219 -17.445546,-19.14219 z " +
            "m 42.359375,8.909768 c 5.74428,0.02177 10.230469,4.488101 10.230469,10.232422 v 23.216796 c " +
            "0,5.744321 -4.486148,10.2304691 -10.230469,10.2304691 h -23.21875 c -5.744321,0 " +
            "-10.2086292,-4.4861901 -10.2304687,-10.2304691 L 8.7825002,-52.617502 Z";

    private static final String INNER_PATH = "m 37.23047,-43.836525 l -19.658204,0.0088 l -0.0073,19.448731 " +
            "c -0.0015,3.561355 2.854626,6.89795 6.688477,6.89795 H 37.23047 c 3.83385,0 6.689941,-3.336595 " +
            "6.689941,-6.89795 v -12.556641 c 0,-3.561354 -2.856089,-6.902524 -6.689941,-6.900879 z";

    protected void renderSingleMarker(Graphics2D gfx, int cellSize, ImgParameters imgParams) {
        final int markerSize = cellSize * SIZE_OF_POSITION_MARKER;

        final AffineTransform transform = gfx.getTransform();

        final double factor = markerSize / 82.0;
        gfx.scale(factor, factor);
        gfx.transform(new AffineTransform(1.3333333, 0, 0, -1.3333333, 0, 81.866667));

        gfx.scale(1, -1);
        gfx.setColor(imgParams.getOuterMarkerColorForAwt());
        gfx.fill(SvgPath2D.drawSvgCommand(OUTER_PATH));

        gfx.setColor(imgParams.getInnerMarkerColorForAwt());
        gfx.fill(SvgPath2D.drawSvgCommand(INNER_PATH));

        gfx.setTransform(transform);
    }
}
