package com.github.aytchell.qrgen.renderers.common;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SvgPath2D {
    private final Path2D.Double path;
    private double xPos;
    private double yPos;
    private double xStart;
    private double yStart;

    public SvgPath2D() {
        this(0.0, 0.0);
    }

    public SvgPath2D(double xPos, double yPos) {
        this.path = new Path2D.Double();
        M(xPos, yPos);
    }

    public static Shape drawSvgCommand(String svgCommand) {
        final SvgPath2D svg = new SvgPath2D();
        svg.d(svgCommand);
        svg.closePath();
        return svg.getPath();
    }

    public Path2D.Double getPath() {
        return path;
    }

    public void M(double x, double y) {
        xPos = x;
        yPos = y;
        moveTo();
    }

    public void m(double dx, double dy) {
        xPos += dx;
        yPos += dy;
        moveTo();
    }

    public void C(Double... coords) {
        if (coords.length % 6 != 0) {
            throw new IllegalArgumentException("Wrong number of coordinates given to curve command");
        }

        path.curveTo(
                coords[0], coords[1],
                coords[2], coords[3],
                coords[4], coords[5]
        );
        xPos = coords[4];
        yPos = coords[5];

        if (coords.length > 6) {
            C(Arrays.stream(coords, 6, coords.length).toArray(Double[]::new));
        }
    }

    public void c(Double... coords) {
        if (coords.length % 6 != 0) {
            throw new IllegalArgumentException("Wrong number of coordinates given to curve command");
        }

        path.curveTo(
                coords[0] + xPos, coords[1] + yPos,
                coords[2] + xPos, coords[3] + yPos,
                coords[4] + xPos, coords[5] + yPos
        );
        xPos += coords[4];
        yPos += coords[5];

        if (coords.length > 6) {
            c(Arrays.stream(coords, 6, coords.length).toArray(Double[]::new));
        }
    }

    public void l(double dx, double dy) {
        xPos += dx;
        yPos += dy;
        lineTo();
    }

    public void L(double x, double y) {
        xPos = x;
        yPos = y;
        lineTo();
    }

    public void H(double x) {
        xPos = x;
        lineTo();
    }

    public void h(double dx) {
        xPos += dx;
        lineTo();
    }

    private void V(double y) {
        yPos = y;
        lineTo();
    }

    private void v(double dy) {
        yPos += dy;
        lineTo();
    }

    private void lineTo() {
        path.lineTo(xPos, yPos);
    }

    private void moveTo() {
        path.moveTo(xPos, yPos);
        xStart = xPos;
        yStart = yPos;
    }

    public void closePath() {
        z();
    }

    public void z() {
        path.closePath();
        xPos = xStart;
        yPos = yStart;
    }

    public void d(String path) {
        while (!path.isEmpty()) {
            path = extractAndProcessCommand(path);
        }
    }

    private String extractAndProcessCommand(String path) {
        path = svgTrimFront(path);
        if (path.isEmpty()) return "";

        final char cmd = path.charAt(0);
        final String tail = svgTrimFront(path.substring(1));

        switch (cmd) {
            case 'm':
                return commandWithTwoParams(tail, this::m);
            case 'M':
                return commandWithTwoParams(tail, this::M);
            case 'l':
                return commandWithTwoParams(tail, this::l);
            case 'L':
                return commandWithTwoParams(tail, this::L);
            case 'c': {
                final Parameters params = extractModuloSixDoubles(tail);
                c(params.p.toArray(new Double[0]));
                return params.tail;
            }
            case 'C': {
                final Parameters params = extractModuloSixDoubles(tail);
                C(params.p.toArray(new Double[0]));
                return params.tail;
            }
            case 'z':
            case 'Z':
                z();
                return tail;
            case 'V':
                return commandWithOneParam(tail, this::V);
            case 'v':
                return commandWithOneParam(tail, this::v);
            case 'H':
                return commandWithOneParam(tail, this::H);
            case 'h':
                return commandWithOneParam(tail, this::h);
            default:
                throw new IllegalArgumentException("Unknown svg command '" + cmd + "' found");
        }
    }

    private String commandWithOneParam(String tail, Consumer<Double> fct) {
        final Parameters params = extractDoubles(tail, 1);
        fct.accept(params.p.get(0));
        return params.tail;
    }

    private String commandWithTwoParams(String tail, BiConsumer<Double, Double> fct) {
        final Parameters params = extractDoubles(tail, 2);
        fct.accept(params.p.get(0), params.p.get(1));
        return params.tail;
    }

    private String svgTrimFront(String string) {
        for (int idx = 0; idx < string.length(); ++idx) {
            switch (string.charAt(idx)) {
                case ' ':
                case ',':
                    break;
                default:
                    return string.substring(idx);
            }
        }
        return "";
    }

    private Parameters extractDoubles(String string, int numToExtract) {
        switch (numToExtract) {
            case 0:
                return new Parameters(new ArrayList<>(), string);
            case 1:
                return extractNextDouble(string);
            default:
                final Parameters firstParam = extractNextDouble(string);
                final Parameters others = extractDoubles(firstParam.tail, numToExtract - 1);
                others.p.add(0, firstParam.p.get(0));
                return others;
        }
    }

    private Parameters extractNextDouble(String string) {
        string = svgTrimFront(string);
        if (string.isEmpty()) {
            return new Parameters(new ArrayList<>(), "");
        }

        final Scanner scanner = new Scanner(string);
        scanner.useDelimiter("[, ]+");
        final String token = scanner.next("^[+\\-]?[0-9]{1,13}(\\.[0-9]*)?(e[+\\\\-]?[0-9]{1,3})?");
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Didn't find expected double value");
        }

        final List<Double> p = new ArrayList<>(1);
        p.add(Double.parseDouble(token));
        return new Parameters(p, string.substring(token.length()));
    }

    private Parameters extractModuloSixDoubles(String string) {
        final Parameters params = extractDoubles(string, 6);
        final String tail = svgTrimFront(params.tail);
        if (tail.isEmpty() || tail.matches("^[^0-9+\\-].*")) {
            return params;
        }
        final Parameters others = extractModuloSixDoubles(tail);
        return new Parameters(
                Stream.concat(params.p.stream(), others.p.stream())
                        .collect(Collectors.toList()),
                others.tail);
    }

    private static class Parameters {
        final List<Double> p;
        final String tail;

        private Parameters(List<Double> p, String tail) {
            this.p = p;
            this.tail = tail;
        }
    }
}
