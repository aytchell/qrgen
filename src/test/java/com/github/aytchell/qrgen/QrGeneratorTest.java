package com.github.aytchell.qrgen;

import com.google.zxing.WriterException;
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
public class QrGeneratorTest {
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
        final QrGenerator gen = new QrGenerator();
        final QrGenerator clone = gen.clone();
        assertNotNull(clone);
    }

    @ParameterizedTest
    @MethodSource("imageSizesForSizeTest")
    void sizeIsRespected(Integer size) throws IOException, QrGenerationException {
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

    /*
    too big: payload len 7090; lvl L
    too big: payload len 5597; lvl M
    too big: payload len 3994; lvl Q
    too big: payload len 3058; lvl H
     */
}
