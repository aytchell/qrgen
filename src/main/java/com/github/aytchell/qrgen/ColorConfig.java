package com.github.aytchell.qrgen;

import lombok.Value;

@Value
public class ColorConfig {
    int onColor;
    int offColor;
    int markerColor;

    public ColorConfig(int onColor, int offColor) {
        this.onColor = onColor;
        this.offColor = offColor;
        this.markerColor = onColor;
    }

    public ColorConfig(int onColor, int offColor, int markerColor) {
        this.onColor = onColor;
        this.offColor = offColor;
        this.markerColor = markerColor;
    }
}
