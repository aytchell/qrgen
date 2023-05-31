package com.github.aytchell.qrgen.config;

import com.github.aytchell.qrgen.colors.QrColor;
import lombok.Value;

import java.awt.image.BufferedImage;

@Value
public class ColorConfig {
    QrColor onColor;
    QrColor offColor;
    QrColor outerMarkerColor;
    QrColor innerMarkerColor;

    public ColorConfig(QrColor onColor, QrColor offColor) {
        this(onColor, offColor, onColor);
    }

    public ColorConfig(QrColor onColor, QrColor offColor, QrColor markerColor) {
        this(onColor, offColor, markerColor, markerColor);
    }

    public ColorConfig(QrColor onColor, QrColor offColor, QrColor outerMarkerColor, QrColor innerMarkerColor) {
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
