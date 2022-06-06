# Changelog

This application is the main (and at least for the MVP the only) backend of
the blackpin messenger. It is written in Java / Spring Boot.

All notable changes to this project will be documented in this file.

The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Added new configuration option for generated QR codes
    - the "pixels" of the QR code can now also be dots

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
