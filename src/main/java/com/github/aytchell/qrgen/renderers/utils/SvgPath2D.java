package com.github.aytchell.qrgen.renderers.utils;

import lombok.Getter;
import lombok.Value;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SvgPath2D {
    @Getter
    private final Path2D.Double path;
    private double xPos;
    private double yPos;

    public SvgPath2D() {
        this(0.0, 0.0);
    }

    public SvgPath2D(double xPos, double yPos) {
        this.path = new Path2D.Double();
        this.xPos = xPos;
        this.yPos = yPos;
        path.moveTo(xPos, yPos);
    }

    public static Shape drawSvgCommand(String svgCommand) {
        final SvgPath2D svg = new SvgPath2D();
        svg.d(svgCommand);
        svg.closePath();
        return svg.getPath();
    }

    public void M(double x, double y) {
        path.moveTo(x, y);
        xPos = x;
        yPos = y;
    }

    public void m(double dx, double dy) {
        path.moveTo(xPos + dx, yPos + dy);
        xPos += dx;
        yPos += dy;
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

    public void h(double dx) {
        path.lineTo(xPos + dx, yPos);
        xPos += dx;
    }

    public void z() {
        path.closePath();
    }

    public void closePath() {
        path.closePath();
    }

    public void d(String path) {
        while (path.length() > 0) {
            path = extractAndProcessCommand(path);
        }
    }

    private String extractAndProcessCommand(String path) {
        path = svgTrimFront(path);
        if (path.isEmpty()) return "";

        final char cmd = path.charAt(0);
        final String tail = svgTrimFront(path.substring(1));

        switch (cmd) {
            case 'm': {
                final Parameters params = extractDoubles(tail, 2);
                m(params.p.get(0), params.p.get(1));
                return params.tail;
            }
            case 'M': {
                final Parameters params = extractDoubles(tail, 2);
                M(params.p.get(0), params.p.get(1));
                return params.tail;
            }
            case 'c': {
                final Parameters params = extractModuloSixDoubles(tail);
                c(params.getP().toArray(new Double[0]));
                return params.tail;
            }
            case 'C': {
                final Parameters params = extractModuloSixDoubles(tail);
                C(params.getP().toArray(new Double[0]));
                return params.tail;
            }
            case 'z':
            case 'Z':
                z();
                return tail;
            case 'h': {
                final Parameters params = extractDoubles(tail, 1);
                h(params.p.get(0));
                return params.tail;
            }
            default:
                throw new IllegalArgumentException("Unknown svg command '" + cmd + "' found");
        }
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
            case 0: return new Parameters(new ArrayList<>(), string);
            case 1: return extractNextDouble(string);
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

    @Value
    private static class Parameters {
        List<Double> p;
        String tail;
    }
}
