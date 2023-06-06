package frc.robot.utils;

public class Vector2d {
    private final double x;
    private final double y;
    private final double magnitude;
    private final double angle;

    public Vector2d(Point point) {
        if (point instanceof CartesianPoint) {
            this.x = point.var1;
            this.y = point.var2;
            this.magnitude = getMagnitude(x, y);
            this.angle = getAngle(x, y);
        } else {
            this.x = point.var1 * Math.cos(point.var2);
            this.y = point.var1 * Math.sin(point.var2);
            this.magnitude = point.var1;
            this.angle = point.var2;
        }
    }
    private double getMagnitude(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    private double getAngle(double x, double y) {
        if (x >= 0.0) {
            return Math.atan(y / x);
        } else {
            return Math.atan(y / x) + Math.PI;
        }
    }
    public Vector2d rotate(double angle) {
        return new Vector2d(new PolarPoint(magnitude, this.angle + angle));
    }
    private abstract static class Point {
        public double var1;
        public double var2;
    }
    public static class CartesianPoint extends Point {
        public CartesianPoint(double x, double y) {
            var1 = x;
            var2 = y;
        }
    }
    public static class PolarPoint extends Point {
        public PolarPoint(double r, double theta) {
            var1 = r;
            var2 = theta;
        }
    }
    public Vector2d scale(double i, double j) {
        double x = this.x * i;
        double y = this.y * j;
        return new Vector2d(new CartesianPoint(x, y));
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}
