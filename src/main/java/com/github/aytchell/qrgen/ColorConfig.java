package com.github.aytchell.qrgen;

import lombok.Value;

import java.awt.image.BufferedImage;

@Value
public class ColorConfig {
    ArgbValue onColor;
    ArgbValue offColor;
    ArgbValue outerMarkerColor;
    ArgbValue innerMarkerColor;

    public ColorConfig(ArgbValue onColor, ArgbValue offColor) {
        this(onColor, offColor, onColor);
    }

    public ColorConfig(ArgbValue onColor, ArgbValue offColor, ArgbValue markerColor) {
        this(onColor, offColor, markerColor, markerColor);
    }

    public ColorConfig(ArgbValue onColor, ArgbValue offColor, ArgbValue outerMarkerColor, ArgbValue innerMarkerColor) {
        this.onColor = onColor;
        this.offColor = offColor;
        this.outerMarkerColor = outerMarkerColor;
        this.innerMarkerColor = innerMarkerColor;
    }

    public int determineImageType() {
        if (onColor.hasAlpha() || offColor.hasAlpha() || outerMarkerColor.hasAlpha() || innerMarkerColor.hasAlpha()) {
            return BufferedImage.TYPE_INT_ARGB;
        }
        return BufferedImage.TYPE_INT_RGB;
    }
}
