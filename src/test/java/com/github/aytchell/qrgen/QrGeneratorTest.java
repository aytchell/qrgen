package com.github.aytchell.qrgen;

import com.github.aytchell.qrgen.config.ErrorCorrectionLevel;
import com.github.aytchell.qrgen.config.ImageFileType;
import com.github.aytchell.qrgen.config.PixelStyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class QrGeneratorTest {
    private static Stream<Integer> imageSizesForSizeTest() {
        final List<Integer> sizes = new ArrayList<>();
        sizes.add(100);
        sizes.add(300);
        sizes.add(650);
        sizes.add(900);
        return sizes.stream();
    }

    private static Stream<SizeAndLevel> maxSizesForLevels() {
        final List<SizeAndLevel> sizes = new ArrayList<>();
        sizes.add(new SizeAndLevel(QrGenerator.MAX_PAYLOAD_SIZE_FOR_L, ErrorCorrectionLevel.L));
        sizes.add(new SizeAndLevel(QrGenerator.MAX_PAYLOAD_SIZE_FOR_M, ErrorCorrectionLevel.M));
        sizes.add(new SizeAndLevel(QrGenerator.MAX_PAYLOAD_SIZE_FOR_Q, ErrorCorrectionLevel.Q));
        sizes.add(new SizeAndLevel(QrGenerator.MAX_PAYLOAD_SIZE_FOR_H, ErrorCorrectionLevel.H));
        return sizes.stream();
    }

    @Test
    void cloneIsPossible() {
        // we only check that a clone is produced (not Exception, no null return value)
        final QrGenerator gen = new QrGenerator();
        final QrGenerator clone = gen.clone();
        assertNotNull(clone);
    }

    @ParameterizedTest
    @MethodSource("imageSizesForSizeTest")
    void sizeIsRespected(Integer size) throws IOException, QrGenerationException, QrConfigurationException {
        final QrGenerator gen = new QrGenerator()
                .withSize(size, size);
        final Path path = gen.writeToTmpFile("Hello, World!");
        try {
            TestUtilities.assertFileExistsAndNotEmpty(path);
            final BufferedImage img = TestUtilities.readProducedFile(path);
            assertEquals(size, img.getWidth());
            assertEquals(size, img.getHeight());
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void generatorCanCreatePngFiles() throws IOException, QrGenerationException {
        final QrGenerator gen = new QrGenerator().as(ImageFileType.PNG);
        final Path path = gen.writeToTmpFile("Hello PNG file");
        try {
            final ImageFileType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageFileType.PNG, type);
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void generatorCanCreateBmpFiles() throws IOException, QrGenerationException {
        final QrGenerator gen = new QrGenerator().as(ImageFileType.BMP);
        final Path path = gen.writeToTmpFile("Hello BMP file");
        try {
            final ImageFileType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageFileType.BMP, type);
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void generatorCanCreateGifFiles() throws IOException, QrGenerationException {
        final QrGenerator gen = new QrGenerator().as(ImageFileType.GIF);
        final Path path = gen.writeToTmpFile("Hello GIF file");
        try {
            final ImageFileType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageFileType.GIF, type);
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void generatorCanCreateJpgFiles() throws IOException, QrGenerationException {
        final QrGenerator gen = new QrGenerator().as(ImageFileType.JPG);
        final Path path = gen.writeToTmpFile("Hello JPG file");
        try {
            final ImageFileType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageFileType.JPG, type);
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void renderRoundCorners() throws QrGenerationException, IOException, QrConfigurationException {
        final QrGenerator gen = new QrGenerator()
                .as(ImageFileType.PNG)
                .withSize(400, 400)
                .withPixelStyle(PixelStyle.ROUND_CORNERS)
                .withMargin(2)
                .withErrorCorrection(ErrorCorrectionLevel.Q);
        final Path path = gen.writeToTmpFile("https://github.com/aytchell/qrgen");
        assertNotNull(path);
    }

    @ParameterizedTest
    @MethodSource("maxSizesForLevels")
    void maxPayloadsWork(SizeAndLevel sizeAndLevel) throws QrGenerationException, IOException {
        generatePayloadWithLvl(sizeAndLevel.size, sizeAndLevel.lvl);
    }

    @ParameterizedTest
    @MethodSource("maxSizesForLevels")
    void tooBigPayloadsFail(SizeAndLevel sizeAndLevel) throws IOException {
        try {
            generatePayloadWithLvl(sizeAndLevel.size + 1, sizeAndLevel.lvl);
            fail("QrGenerator should fail to generate code for too big payload");
        } catch (QrGenerationException e) {
            // as expected
        }
    }

    void generatePayloadWithLvl(int payloadSize, ErrorCorrectionLevel lvl) throws QrGenerationException, IOException {
        final QrGenerator gen = new QrGenerator()
                .withErrorCorrection(lvl);
        final String payload = Payload.getWithLength(payloadSize);
        final Path path = gen.writeToTmpFile(payload);
        assertNotNull(path);
    }

    private static class SizeAndLevel {
        final int size;
        final ErrorCorrectionLevel lvl;

        public SizeAndLevel(int size, ErrorCorrectionLevel lvl) {
            this.size = size;
            this.lvl = lvl;
        }
    }
}
