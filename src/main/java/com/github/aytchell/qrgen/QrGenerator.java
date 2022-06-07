package com.github.aytchell.qrgen;

import com.github.aytchell.qrgen.renderers.CirclesRenderer;
import com.github.aytchell.qrgen.renderers.DefaultRenderer;
import com.github.aytchell.qrgen.renderers.QrCodeRenderer;
import com.github.aytchell.qrgen.renderers.SmallRectanglesRenderer;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;

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

/**
 * Class to generate image files containing QR codes for a given payload
 *
 * Instances of this class can create QR codes in a variety of different
 * image file formats. The appearance and characteristics of the generated
 * QR code can be configured via chained method calls.
 * <p>
 * Once an instance is completely configured it can be used several times
 * for creation of QR codes. No need for a new instance or another
 * configuration phase.
 */
public class QrGenerator implements Cloneable {
    public static final int MAX_PAYLOAD_SIZE_FOR_L = 7089;
    public static final int MAX_PAYLOAD_SIZE_FOR_M = 5596;
    public static final int MAX_PAYLOAD_SIZE_FOR_Q = 3993;
    public static final int MAX_PAYLOAD_SIZE_FOR_H = 3057;

    private ImageFileType imageType;
    private int width;
    private int height;
    private BufferedImage logo;
    private HashMap<EncodeHintType, Object> hints = new HashMap<>();
    private MatrixToImageConfig colorConfig;
    private QrCodeRenderer renderer;

    /**
     * Create a QR code generator with default values
     * <p>
     * The configuration of the generator can be changed with the provided
     * methods. If configuration is done, the instance can be stored and
     * generate one or several QR codes with a given payload.
     *
     * @see QrGenerator#writeToTmpFile(String)
     */
    public QrGenerator() {
        imageType = ImageFileType.PNG;
        width = 200;
        height = 200;

        logo = null;
        colorConfig = new MatrixToImageConfig();
        renderer = new DefaultRenderer();

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

        // QrCodeRenderer doesn't hold state so we can use the same instance
        this.renderer = orig.renderer;

        //noinspection unchecked
        this.hints = (HashMap<EncodeHintType, Object>) orig.hints.clone();
    }

    private void setDefaultHints() {
        withErrorCorrection(ErrorCorrectionLevel.L);
        withCharset(StandardCharsets.UTF_8);
        withMargin(4);
    }

    /**
     * Creates a deep copy of the instance
     * <p>
     * In case you need two QrGenerator|s which nearly share the same configuration you can
     * use this method to create a clone and change the clone where it should differ.
     * The advantage compared to simply creating two instances is, that some immutable
     * members (e.g. the logo to be applied or the color values) are shared between the
     * two instances.
     *
     * @return An identical clone which can be changed without affecting the original instance
     */
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

    /**
     * Configure the file format of the generated file
     * <p>
     * The default file type if a fresh QrGenerator is used is PNG
     *
     * @param imageType the image file to be produced by this instance
     * @return this instance so that config calls can be chained
     */
    public QrGenerator as(ImageFileType imageType) {
        this.imageType = imageType;
        return this;
    }

    /**
     * Configure the size of the generated QR code image
     * <p>
     * The default size if a fresh QrGenerator is used is 200 x 200
     *
     * @param width requested image width in pixel
     * @param height requested image height in pixel
     * @return this instance so that config calls can be chained
     * @see QrGenerator#withMargin(int)
     */
    public QrGenerator withSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Select the colors to be used for drawing the QR code
     * <p>
     * This is a convenience wrapper for {@link QrGenerator#withColors(int, int)} to
     * have "more readable" color values in the code. You can use {@link ArgbValue}
     * for colors with an alpha channel or {@link RgbValue} for opaque colors.
     * <p>
     * The default colors if a fresh QrGenerator is used are black and white.
     *
     * @param onColor the color of "the pixels" (usually black)
     * @param offColor the color of "the empty space" (usually white)
     * @return this instance so that config calls can be chained
     * @see QrGenerator#withColors(int, int)
     */
    public QrGenerator withColors(ArgbValue onColor, ArgbValue offColor) {
        return withColors(
                onColor.getRawValue(),
                offColor.getRawValue());
    }

    /**
     * Select the colors to be used for drawing the QR code
     * <p>
     * The given parameters are integers with the highest value byte contains
     * the alpha channel, followed by bytes for red, green and blue. Each
     * byte can have values from 0 to 255 (0xff). So these colors are very
     * similar to those used in HTML or image processing tools - just packed
     * into an int.
     * <p>
     * There's also a convenience wrapper to have "more readable" color
     * values in the code.
     * <p>
     * The default colors if a fresh QrGenerator is used are black and white.
     *
     * @param onColor the color of "the pixels" (usually black)
     * @param offColor the color of "the empty space" (usually white)
     * @return this instance so that config calls can be chained
     * @see QrGenerator#withColors(ArgbValue, ArgbValue)
     */
    public QrGenerator withColors(int onColor, int offColor) {
        this.colorConfig = new MatrixToImageConfig(onColor, offColor);
        return this;
    }

    /**
     * Select the character set for encoding the payload in the QR code
     * <p>
     * Java strings are internally encoded with UTF-16. Since QR codes can
     * only contain a limited amount of data this might be too verbose
     * for your needs. Use this method to configure the character set
     * to which the payload should be converted before creating a QR code
     * from it.
     * <p>
     * The default encoding if a fresh QrGenerator is used is UTF-8.
     *
     * @param charset the character set used to encode the payload
     * @return this instance so that config calls can be chained
     */
    public QrGenerator withCharset(Charset charset) {
        return withHint(CHARACTER_SET, charset.name());
    }

    /**
     * Set the error correction level for the resulting QR code
     * <p>
     * QR codes can be written with different levels of redundancy. This is
     * useful if the (physically printed) QR code is partially destroyed or
     * covered. The higher the error correction level the better a QR code
     * will "survive" damages.
     * <p>
     * A higher error correction level should also be taken into account if
     * a logo should be applied onto the QR code - this is nothing else than
     * covering parts of the code with "unrelated pixels" (from a scanner's
     * point of view).
     * <p>
     * Note however that a QR code with a higher error correction level will
     * contain "more pixels" thus having a different appearance.
     * <p>
     * The default level if a fresh QrGenerator is used is "L"
     *
     * @param level the error correction level to choose
     * @return this instance so that config calls can be chained
     * @see QrGenerator#withLogo(Path)
     * @see QrGenerator#withLogo(InputStream)
     */
    public QrGenerator withErrorCorrection(ErrorCorrectionLevel level) {
        return withHint(ERROR_CORRECTION, level.getZxingLevel());
    }

    /**
     * The size of a white frame added around the generated QR code
     *
     * To improve readability for scanners a QR code usually has a white margin
     * surrounding it. The size of this margin can be set here. If a negative
     * value is given we'll fall back to the default.
     * <p>
     * The default margin if a fresh QrGenerator is used is 4.
     *
     * @param margin the thickness of the surrounding white frame. The unit for
     *               this value is "number of code pixels"; so the actual size depends
     *               on the size of the QR code
     * @return this instance so that config calls can be chained
     * @see QrGenerator#withSize(int, int)
     */
    public QrGenerator withMargin(int margin) {
        if (margin < 0) {
            return withHint(MARGIN, null);
        } else {
            return withHint(MARGIN, margin);
        }
    }

    public QrGenerator withPixelStyle(PixelStyle style) {
        switch (style) {
            case RECTANGLES:
                renderer = new DefaultRenderer();
                break;
            case SMALL_RECTANGLES:
                renderer = new SmallRectanglesRenderer();
                break;
            case DOTS:
                renderer = new CirclesRenderer();
                break;
        }
        return this;
    }

    /**
     * Configure a logo to be added centered as overlay
     * <p>
     * The image contained in the given file will be read and applied to the
     * generated QR code as a centered overlay. This method has been tested
     * to work with gif, png, bmp and jpeg files. Depending on your JDK it
     * might also work with other file formats (see documentation of
     * {@code ImageIO.read()}).
     * <p>
     * Note that the applied logo will cover parts of the QR code thus
     * "damaging" information. Using a higher error correction level should
     * be taken into account.
     * <p>
     * Note further that the logo will not be scaled. It's the caller's
     * responsibility that sizes of QR code and logo fit and the resulting
     * QR code is still readable by scanners.
     *
     * @param logoFile an image file with a logo inside that will be
     *                 overlayed onto the QR code
     * @return this instance so that config calls can be chained
     * @throws IOException in case the given logo file couldn't be read.
     *      This might be due to a file not found or not readable as
     *      well as an unknown image file type.
     */
    public QrGenerator withLogo(Path logoFile) throws IOException {
        this.logo = readLogo(logoFile);
        return this;
    }

    /**
     * Configure a logo to be added centered as overlay
     * <p>
     * Version of {@link QrGenerator#withLogo(Path)} which takes an
     * {@code InputStream}. See there for detailed information.
     *
     * @param logoStream an InputStream with the content of an image file
     * @return this instance so that config calls can be chained
     * @throws IOException in case the given logo couldn't be read.
     *      This might be due to a problem with the given InputStream as
     *      well as an unknown image file type.
     */
    public QrGenerator withLogo(InputStream logoStream) throws IOException {
        this.logo = readLogo(logoStream);
        return this;
    }

    /**
     * Actually create q QR code with the given configuration and payload
     * <p>
     * This method takes the configuration collected up to here (by using
     * the other methods) and a payload string and creates an image file
     * containing a QR code.
     * <p>
     * The resulting file is created in the global tmp directory of the
     * underlying OS with no specific file permissions (so most probably
     * it is readable by anyone). The file will be deleted on exit of the
     * JVM but of course you can (and maybe should) delete it as soon as
     * you no longer need it. Simply renaming or moving the file to some
     * "local storage" might be no good idea because the JVM will delete
     * it sooner or later.
     * <p>
     * Note that calling this method does not change the configuration.
     * So you can keep the instance and generate more QR codes without
     * repeating the configuration steps.
     *
     * @param payload the string to be encoded into a QR code
     *
     * @return an image file showing a QR code
     * @throws IOException thrown in case something goes wrong while handling
     *      the tmp file or producing the requested image format
     * @throws QrGenerationException thrown in case the computation of the
     *      QR code goes wrong. This is independent of file operations or
     *      image formats.
     * @see QrGenerator#writeToTmpFile(String, String)
     */
    public Path writeToTmpFile(String payload)
            throws IOException, QrGenerationException {
        return writeToTmpFile(payload, null);
    }

    /**
     * Actually create q QR code with the given configuration and payload
     * <p>
     * This method does the same as {@link QrGenerator#writeToTmpFile(String)}.
     * The only difference is that you can give an additional prefix for the
     * name of the created tmp file.
     *
     * @param payload the string to be encoded into a QR code
     * @param tmpFilePrefix a prefix for the name of the created tmp file
     *
     * @return an image file showing a QR code
     * @throws IOException thrown in case something goes wrong while handling
     *      the tmp file or producing the requested image format
     * @throws QrGenerationException thrown in case the computation of the
     *      QR code goes wrong. This is independent of file operations or
     *      image formats.
     * @see QrGenerator#writeToTmpFile(String)
     */
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
        final BufferedImage img = ImageIO.read(logoStream);
        if (img == null) {
            throw new IOException("Can't parse given logo file");
        }
        return img;
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

    private int measurePositionMarker(BitMatrix matrix) {
        final BitArray row = matrix.getRow(0, null);
        for (int i = 0; i < row.getSize(); ++i) {
            if (!row.get(i)) {
                // this first bit not part of the position marker
                return i;
            }
        }

        // ugh, the complete line is black??
        return 0;
    }

    public static class Measurements {
        int codeSize;
        int positionMarkerSize;
    }

    private BufferedImage generateImage(String payload) throws WriterException {
        final BufferedImage qrCodeImage = renderer.encodeAndRender(payload, colorConfig, width, height, hints);
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