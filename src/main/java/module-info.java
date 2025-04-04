module qrgen {
    requires com.google.zxing;
    requires java.desktop;

    exports com.github.aytchell.qrgen;
    exports com.github.aytchell.qrgen.config;
    exports com.github.aytchell.qrgen.colors;
    exports com.github.aytchell.qrgen.exceptions;
}
