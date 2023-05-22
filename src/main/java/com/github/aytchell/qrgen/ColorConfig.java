package com.github.aytchell.qrgen;

import lombok.Value;

import java.awt.image.BufferedImage;

@Value
public class ColorConfig {
    ArgbValue onColor;
    ArgbValue offColor;
    ArgbValue markerColor;

    public ColorConfig(ArgbValue onColor, ArgbValue offColor) {
        this.onColor = onColor;
        this.offColor = offColor;
        this.markerColor = onColor;
    }

    public ColorConfig(ArgbValue onColor, ArgbValue offColor, ArgbValue markerColor) {
        this.onColor = onColor;
        this.offColor = offColor;
        this.markerColor = markerColor;
    }

    public int getRawOnColor() {
        return onColor.getRawValue();
    }

    public int getRawOffColor() {
        return offColor.getRawValue();
    }

    public int getRawMarkerColor() {
        return markerColor.getRawValue();
    }

    public int determineImageType() {
        if (onColor.hasAlpha() || offColor.hasAlpha() || markerColor.hasAlpha()) {
            return BufferedImage.TYPE_INT_ARGB;
        }
        return BufferedImage.TYPE_INT_RGB;
    }
}
