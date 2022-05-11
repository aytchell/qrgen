# QR code generator

This library provides a class which is capable of generating QR codes. It is 
a thin layer on top the [ZXing](https://github.com/zxing/zxing) generator
which does the heavily lifting of actually computing the dot matrix. This
lib then adds the functionality to make image files.

This lib is heavily inspired by [QRGen](http://kenglxn.github.io/QRGen/).
But since the project seems to be unmaintained since two year plus I'd like
to have some structural changes I wrote this new lib instead. The main
differences to QRGen are:
  - you can configure a QrGenerator and then generate multiple QR codes from
    it instead of repeating the configuration for each QR code
  - this lib can overlay a logo over the QR code
  - no leaking of ZXing provided types
  - no support for Android

## Introduction

When starting with class `QrGenerator` usage of this lib is quite
self-explanatory (there's also javadoc available):

```java
        final QrGenerator generator = new QrGenerator()
                .withSize(300, 300)
                .withMargin(3)
                .as(ImageType.PNG)
                .withCharset(StandardCharsets.UTF_8);

        final Path img = generator
                .writeToTmpFile("Hello, World!");
```

## Maven

```xml
    <dependency>
        <groupId>com.github.aytchell</groupId>
        <artifactId>qrgen</artifactId>
        <version>0.9.0</version>
    </dependency>
```

## Configuration

This section lists the possible configurations for the created QR codes. The
method calls to configure the generator can be chained and the resulting
configuration is collected by the instance. So to make things easier all
examples build on the same instance of `QrGenerator`.

```java
        final QrGenerator generator = new QrGenerator();
```

### Output file type

The file format of the resulting QR code image can be selected to be
`png`, `gif`, `bmp` or `jpeg`:

```java
        generator.as(ImageType.PNG);
```

### Size and margin of the code

The size of the resulting image (in pixels) can be configured as well
as a "quiet zone" around the code. This margin is an aid for QR code
scanners to easier distinguished the code from the surrounding environment.

The unit of the parameter for the margin's size is "number of dots used in
the QR code" so the actual size of the margin in pixels depends on the size
of the complete image as well as on the complexity of the QR code.

```java
        generator
                .withSize(300, 300)
                .withMargin(3);
```

### Used colors

Usually QR codes are displayed in black and white as this gives the best
contrast and thus help a scanner in reading it. But there might be times,
when want to have different colors. The first parameter sets the color of the
"active parts" of the code while the second parameter sets the color of
the "space between the dots".

```java
        final ArgbValue daffodil = new ArgbValue(0xaa, 0xff, 0xff, 0x31);
        final ArgbValue oxfordBlue = new ArgbValue(0xff, 0x00, 0x21, 0x47);

        generator.withColors(daffodil, oxfordBlue);
```

If this is too verbose for you it's also possible to directly use integers
denoting the argb values. The `QrGenerator` will store both variants in the
same way.

```java
        final int daffodil = 0xaaffff31;
        final int oxfordBlue = 0xff002147;

        generator.withColors(daffodil, oxfordBlue);
```
### Error correction level

// TODO: describe

```java
        generator
                .withErrorCorrection(ErrorCorrectionLevel.Q)
```


### Logo overlay

The generator is able to overlay the QR code with a centered logo. The file
format of the logo can be any of `gif`, `png`, `bmp` or `jpeg`. Alpha channel
is respected if the file format supports it.

Note however that the lib does not scale the logo to "fit" (whatever that
means); the logo will cover information from the QR code and increasing the
error correction level should be considered.

It's up to the caller to ensure that the QR code can be read with the
logo above it.

```java
        try (final InputStream logo =
                QrCodeExample.class.getResourceAsStream("github_logo.png")) {
            // in real code check for null ...
            generator.withLogo(logo);
        }

        final Path img = generator
                .writeToTmpFile("Hello, Github!");
```

### Character set of the encoded message

// TODO: describe

```java
        generator
                .withCharset(StandardCharsets.UTF_8);
```

## Actually write the QR code

// TODO: describe

## License

Apache 2.0 License

Created and maintained by [Hannes Lerchl](mailto:hannes.lerchl@googlemail.com)

Feel free to send in pull requests. Please also add unit tests and adapt the
README if appropriate.
