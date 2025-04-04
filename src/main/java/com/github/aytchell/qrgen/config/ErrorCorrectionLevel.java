package com.github.aytchell.qrgen.config;

/**
 * The error correction level for a QR code
 * <p>
 * The specification defines four values for error correction level.
 * The levels describe a level of redundancy of the encoded information.
 * Higher redundancy causes bigger QR codes but also better resilience
 * against (physical) damage or occlusion of the displayed QR code
 */
public enum ErrorCorrectionLevel {
    /**
     * L = ~7% correction
     */
    L(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L),

    /**
     * M = ~15% correction
     */
    M(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M),

    /**
     * Q = ~25% correction
     */
    Q(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.Q),

    /**
     * H = ~30% correction
     */
    H(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H);

    private final com.google.zxing.qrcode.decoder.ErrorCorrectionLevel zxingLevel;

    ErrorCorrectionLevel(com.google.zxing.qrcode.decoder.ErrorCorrectionLevel level) {
        this.zxingLevel = level;
    }

    /**
     * Returns the equivalent correction level to be given to ZXing
     * <p>
     * Used internally when calling ZXing for creating the bit matrix of the QR code
     *
     * @return Equivalent error correction value which is understood by ZXing
     */
    public com.google.zxing.qrcode.decoder.ErrorCorrectionLevel getZxingLevel() {
        return zxingLevel;
    }
}
