package com.github.aytchell.qrgen;

public enum ErrorCorrectionLevel {
    /** L = ~7% correction */
    L(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L),

    /** M = ~15% correction */
    M(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M),

    /** Q = ~25% correction */
    Q(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.Q),

    /** H = ~30% correction */
    H(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);

    private final com.google.zxing.qrcode.decoder.ErrorCorrectionLevel zxingLevel;

    ErrorCorrectionLevel(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel level)  {
        this.zxingLevel = level;
    }

    public com.google.zxing.qrcode.decoder.ErrorCorrectionLevel getZxingLevel() {
        return zxingLevel;
    }
}
