package com.github.aytchell.qrgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class QRGeneratorTest {
    private static Stream<Integer> imageSizesForSizeTest() {
        final List<Integer> sizes = new ArrayList<>();
        sizes.add(100);
        sizes.add(300);
        sizes.add(650);
        sizes.add(900);
        return sizes.stream();
    }

    @Test
    void cloneIsPossible() {
        // we only check that a clone is produced (not Exception, no null return value)
        final QRGenerator gen = new QRGenerator();
        final QRGenerator clone = gen.clone();
        assertNotNull(clone);
    }

    @ParameterizedTest
    @MethodSource("imageSizesForSizeTest")
    void sizeIsRespected(Integer size) throws IOException, QRGenerationException {
        final QRGenerator gen = new QRGenerator()
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
    void generatorCanCreatePngFiles() throws IOException, QRGenerationException {
        final QRGenerator gen = new QRGenerator().as(ImageType.PNG);
        final Path path = gen.writeToTmpFile("Hello PNG file");
        try {
            final ImageType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageType.PNG, type);
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void generatorCanCreateBmpFiles() throws IOException, QRGenerationException {
        final QRGenerator gen = new QRGenerator().as(ImageType.BMP);
        final Path path = gen.writeToTmpFile("Hello BMP file");
        try {
            final ImageType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageType.BMP, type);
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void generatorCanCreateGifFiles() throws IOException, QRGenerationException {
        final QRGenerator gen = new QRGenerator().as(ImageType.GIF);
        final Path path = gen.writeToTmpFile("Hello GIF file");
        try {
            final ImageType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageType.GIF, type);
        } finally {
            path.toFile().delete();
        }
    }

    @Test
    void generatorCanCreateJpgFiles() throws IOException, QRGenerationException {
        final QRGenerator gen = new QRGenerator().as(ImageType.JPG);
        final Path path = gen.writeToTmpFile("Hello JPG file");
        try {
            final ImageType type = TestUtilities.findOutImageTypeOfFile(path);
            assertEquals(ImageType.JPG, type);
        } finally {
            path.toFile().delete();
        }
    }
}
