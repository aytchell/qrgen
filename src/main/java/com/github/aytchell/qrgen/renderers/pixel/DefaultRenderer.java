package com.github.aytchell.qrgen.renderers.pixel;

import com.github.aytchell.qrgen.renderers.common.ImgParameters;

public class DefaultRenderer extends IndependentPixelRenderer {
    public DefaultRenderer(ImgParameters imgParams) {
        super(imgParams);
    }

    // the base class has a default implementation which already renders a filled cell
    // so nothing to be done here
}
