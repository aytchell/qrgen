package com.github.aytchell.qrgen;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static com.google.zxing.EncodeHintType.*;

public class QrGenerator implements Cloneable {
    private ImageType imageType;
    private int width;
    private int height;
    private BufferedImage logo;
    private HashMap<EncodeHintType, Object> hints = new HashMap<>();
    private MatrixToImageConfig colorConfig;
    private final QRCodeWriter writer;

    public QrGenerator() {
        imageType = ImageType.PNG;
        width = 200;
        height = 200;

        logo = null;
        colorConfig = new MatrixToImageConfig();
        writer = new QRCodeWriter();

        setDefaultHints();
    }

    private QrGenerator(QrGenerator orig) {
        // copy primitive types
        this.imageType = orig.imageType;
        this.width = orig.width;
        this.height = orig.height;

        // We never change these (only replace them); so we can reference them in the clone
        this.logo = orig.logo;
        this.colorConfig = orig.colorConfig;

        // class QRCodeWriter doesn't hold state; we can simply reference it
        this.writer = orig.writer;

        //noinspection unchecked
        this.hints = (HashMap<EncodeHintType, Object>) orig.hints.clone();
    }

    private void setDefaultHints() {
        withErrorCorrection(ErrorCorrectionLevel.L);
        withCharset(StandardCharsets.UTF_8);
        withMargin(4);
    }

    @Override
    public QrGenerator clone() {
        try {
            super.clone();
            return new QrGenerator(this);
        } catch (CloneNotSupportedException e) {
            // I'm quite sure that class Object supports 'clone()' so
            // instead of throwing we'll return null
            return null;
        }
    }

    public QrGenerator as(ImageType imageType) {
        this.imageType = imageType;
        return this;
    }

    public QrGenerator withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public QrGenerator withColors(ArgbValue onColor, ArgbValue offColor) {
        return withColors(
                onColor.getRawValue(),
                offColor.getRawValue());
    }

    public QrGenerator withColors(int onColor, int offColor) {
        this.colorConfig = new MatrixToImageConfig(onColor, offColor);
        return this;
    }

    public QrGenerator withCharset(Charset charset) {
        return withHint(CHARACTER_SET, charset.name());
    }

    public QrGenerator withErrorCorrection(ErrorCorrectionLevel level) {
        return withHint(ERROR_CORRECTION, level.getZxingLevel());
    }

    /**
     * The size of a white frame added around the generated QR code
     *
     * To improve readability for scanners a QR code usually has a white margin
     * surrounding it. The size of this margin can be set here. If a negative
     * value is given we'll fall back to ZXing's standard (which is four).
     *
     * @param margin the thickness of the surrounding white frame. The unit for
     *               this value is "number of code pixels"; so the actual size depends
     *               on the size of the QR code
     * @return The same generator instance but now with a hint for the margin
     */
    public QrGenerator withMargin(int margin) {
        if (margin < 0) {
            return withHint(MARGIN, null);
        } else {
            return withHint(MARGIN, margin);
        }
    }

    public QrGenerator withLogo(Path logoFile) throws IOException {
        this.logo = readLogo(logoFile);
        return this;
    }

    public QrGenerator withLogo(InputStream logoStream) throws IOException {
        this.logo = readLogo(logoStream);
        return this;
    }

    public Path writeToTmpFile(String payload)
            throws IOException, QrGenerationException {
        return writeToTmpFile(payload, null);
    }

    public Path writeToTmpFile(String payload, String tmpFilePrefix)
            throws IOException, QrGenerationException {
        try {
            final Path tmpFile = createTempFile(tmpFilePrefix);
            writeQrCodeToFile(tmpFile, payload);
            return tmpFile;
        } catch (WriterException e) {
            throw new QrGenerationException(e);
        }
    }

    private void writeQrCodeToFile(Path tmpFile, String payload)
            throws WriterException, IOException {
        final BufferedImage image = generateImage(payload);
        if (!ImageIO.write(image, imageType.name(), tmpFile.toFile())) {
            throw new IOException("Could not write an image of format " + imageType.name() +
                    " to " + tmpFile);
        }
    }

    private BufferedImage readLogo(Path logoFile) throws IOException {
        try (InputStream in = Files.newInputStream(logoFile)) {
            return readLogo(in);
        }
    }

    private BufferedImage readLogo(InputStream logoStream) throws IOException {
        return ImageIO.read(logoStream);
    }

    private QrGenerator withHint(EncodeHintType hintType, Object value) {
        if (value == null) {
            hints.remove(hintType);
        } else {
            hints.put(hintType, value);
        }
        return this;
    }

    private Path createTempFile(final String prefix) throws IOException {
        if (prefix == null)
            return createTempFile("qr_");

        final String suffix = "." + imageType.name().toLowerCase();

        final Path path = Files.createTempFile(prefix, suffix);
        path.toFile().deleteOnExit();
        return path;
    }

    private BufferedImage generateImage(String payload) throws WriterException {
        final BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, width, height, hints);

        final BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(matrix, colorConfig);
        if (logo == null) {
            return qrCodeImage;
        } else {
            return mergeLogoIntoQrCode(qrCodeImage);
        }
    }

    private BufferedImage mergeLogoIntoQrCode(BufferedImage qrcode) {
        // compute width and height of the resulting image
        // usually the logo is smaller than the QR code, but you never know ...
        final int width = Math.max(qrcode.getWidth(), logo.getWidth());
        final int height = Math.max(qrcode.getHeight(), logo.getHeight());

        final BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // compute origin of logo so that it appears centered in the QR code
        final int logoX = Math.max(0, (qrcode.getWidth() - logo.getWidth()) / 2);
        final int logoY = Math.max(0, (qrcode.getHeight() - logo.getHeight()) / 2);

        final Graphics gfx = combined.getGraphics();
        gfx.drawImage(qrcode, 0, 0, null);
        gfx.drawImage(logo, logoX, logoY, null);
        gfx.dispose();

        return combined;
    }
}
