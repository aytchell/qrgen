package com.github.aytchell.qrgen;

import com.github.aytchell.qrgen.config.ImageFileType;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtilities {
    private static final byte bx89 = toByte(0x89);
    private static final byte bxff = toByte(0xff);
    private static final byte bxd8 = toByte(0xd8);
    private static final byte bxe0 = toByte(0xe0);

    private static final byte[] MAGIC_BYTES_BMP = { 0x42, 0x4d };               // ascii 'BM'
    private static final byte[] MAGIC_BYTES_GIF = { 0x47, 0x49, 0x46, 0x38 };   // ascii 'GIF8'
    private static final byte[] MAGIC_BYTES_PNG = { bx89, 0x50, 0x4e, 0x47 };   // ascii '.PNG'
    private static final byte[] MAGIC_BYTES_JPG = { bxff, bxd8, bxff, bxe0 };   // ascii '....'

    private static byte toByte(int value) {
        if (value <= 127) return (byte)value;
        return (byte)(value - 256);
        // 128(int) = 0x80 = -128(byte) = 128 - 256
        // 129(int) = 0x81 = -127(byte) = 129 - 256
        // 130(int) = 0x82 = -126(byte) = 130 - 256
        // 131(int) = 0x83 = -125(byte) = 131 - 256
        // 132(int) = 0x84 = -124(byte) = 132 - 256
        // 133(int) = 0x85 = -123(byte) = 133 - 256
        // 134(int) = 0x86 = -122(byte) = 134 - 256
        // 135(int) = 0x87 = -121(byte) = 135 - 256
    }

    @Test
    void byteConversionsForMagicNumbersAreCorrect() {
        assertEquals(0x89, ((int)toByte(0x89) & 0xff));
        assertEquals(0xFF, ((int)toByte(0xFF) & 0xff));
        assertEquals(0xd8, ((int)toByte(0xd8) & 0xff));
        assertEquals(0xe0, ((int)toByte(0xe0) & 0xff));
    }


    public static BufferedImage readProducedFile(Path path) throws IOException {
        final BufferedImage img = ImageIO.read(path.toFile());
        assertNotNull(img);
        return img;
    }

    public static ImageFileType findOutImageTypeOfFile(Path path) throws IOException {
        try (final InputStream input = Files.newInputStream(path)) {
            final int NUMBER_MAGIC_BYTES = MAGIC_BYTES_GIF.length;

            // read the first bytes of the file and compare with well-known magic byte sequences
            final byte[] magic = new byte[NUMBER_MAGIC_BYTES];
            final int bytesRead = input.read(magic);
            assertEquals(NUMBER_MAGIC_BYTES, bytesRead);

            // the following checks only work properly if the arrays have the same size
            assertEquals(NUMBER_MAGIC_BYTES, MAGIC_BYTES_GIF.length);
            assertEquals(NUMBER_MAGIC_BYTES, MAGIC_BYTES_PNG.length);
            assertEquals(NUMBER_MAGIC_BYTES, MAGIC_BYTES_JPG.length);

            if (Arrays.equals(magic, MAGIC_BYTES_PNG)) {
                return ImageFileType.PNG;
            }
            if (Arrays.equals(magic, MAGIC_BYTES_JPG)) {
                return ImageFileType.JPG;
            }
            if (Arrays.equals(magic, MAGIC_BYTES_GIF)) {
                return ImageFileType.GIF;
            }

            // the magic byte sequence of BMP is shorter
            assertTrue(NUMBER_MAGIC_BYTES > MAGIC_BYTES_BMP.length);
            final byte[] bmpMagic = new byte[MAGIC_BYTES_BMP.length];
            System.arraycopy(magic, 0, bmpMagic, 0, MAGIC_BYTES_BMP.length);

            if (Arrays.equals(bmpMagic, MAGIC_BYTES_BMP)) {
                return ImageFileType.BMP;
            }

            return null;
        }
    }

    public static void assertFileExistsAndNotEmpty(Path file200) throws IOException {
        assertNotNull(file200);
        assertTrue(file200.toFile().exists());
        final BasicFileAttributes attrs = Files.readAttributes(file200, BasicFileAttributes.class);
        assertTrue(attrs.isRegularFile());
        assertTrue(attrs.size() > 0);
    }
}
