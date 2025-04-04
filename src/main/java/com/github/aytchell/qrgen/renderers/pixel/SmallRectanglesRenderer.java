package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.ImgParameters;

public class SmallRectanglesRenderer extends IndependentPixelRenderer {
    public SmallRectanglesRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    @Override
    protected String getSvgPath() {
        // a rectangle which is a bit smaller than the complete cell
        return "m 10,10 h 120 v 120 h -120 z";
    }
}
