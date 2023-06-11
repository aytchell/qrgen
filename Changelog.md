# Changelog

This application is the main (and at least for the MVP the only) backend of
the blackpin messenger. It is written in Java / Spring Boot.

All notable changes to this project will be documented in this file.

The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

- ...

## [2.0.0] - 2023-06-11

### Added

- It is now possible to render the three markers of the generated QR codes
  in different styles. The markers of the QR code can now be drawn as
    - rectangles
    - rectangles with rounded corners
    - circles
    - 'raindrops' appearing to fall inward
    - 'raindrops' appearing to fall outward
    - rectangles with one rounded edge at the outer corner
    - rectangles with one rounded edge at the inner corner
    - rectangles with rounded edge except one sharp edge at the outer corner
    - rectangles with rounded edge except one sharp edge at the inner corner

### Changed

- When giving a color as parameter you can now choose between ARGB,
  RGBA, RGB, HSLA and HSL
- It is now possible to select extra colors for the three QR code's markers.
  The inner and the outer parts of the markers can be rendered with
  different colors (if requested).
- Added new style options for generated QR codes. The "pixels" of the QR code
  can now also be
    - rectangles with rounded corners or
    - they can be merged in a way
        - to form rows or columns
        - to form snake-like structures
        - to form structures like water (with adhesion)

### Removed

- the colors to be used can no longer be given as raw integer values.
  Instead you have to feed the `withColors(...)` methods whith instances
  of `ArgbValue`, `RgbaValue`, `RgbValue`, `HslaValue` or `HslValue`

## [1.1.1] - 2023-05-09

### Fixed

- Classes ArgbValue, RgbValue and ImgParameter had no real 'equals()'
  method. Fixed this by introducing lombok

## [1.1.0] - 2022-06-07

### Added

- Added new configuration option for generated QR codes:
  the "pixels" of the QR code can now also be dots
- Added another option to draw "smaller (rectangular) pixels" so that a thin
  grid appears in between

### Changed

- Changed name of the enum to selsect generated file format from ImageType to
  ImageFileType

## [1.0.0] - 2022-05-17

### Added

- Started writing unit tests
- Added README.md
- Added javadoc

## [0.9.5] - 2022-05-10

### Added

- Implemented wrapper around [ZXing](https://github.com/zxing/zxing)
  for generating QR codes
- wrapper can be reused (for multiple payloads)
- generated QR code can be configured wth respect to
    - size of generate image
    - file type of produced image (BMP, PNG, JPG, GIF)
    - colors of "on" and "off" pixels
    - error correction level
    - character encoding of encoded payload
    - margin around the QR code (to help scanners)
- generator can place a logo centered over the QR code
