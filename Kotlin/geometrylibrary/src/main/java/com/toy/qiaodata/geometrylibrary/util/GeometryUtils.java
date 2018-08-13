package com.toy.qiaodata.geometrylibrary.util;

import com.toy.qiaodata.geometrylibrary.exception.GeometryException;
import com.toy.qiaodata.geometrylibrary.object.Circle;
import com.toy.qiaodata.geometrylibrary.object.Line;
import com.toy.qiaodata.geometrylibrary.object.Point;

/**
 * create by YANG on 2018/7/30 18:09
 */
public class GeometryUtils {
    private GeometryUtils() {}

    public static Point[] getCrossoverPoint(Line line, Circle circle) {
        if (line == null && circle == null) {
            return null;
        }
        if (line.getB() != 0) {
            double a = 1 + MathUtils.pow2(line.getA()) / MathUtils.pow2(line.getB());
            double b = 2 * (((line.getA() / line.getB()) * ((line.getC() / line.getB()) + circle.getB())) -  circle.getA());
            double c = MathUtils.pow2(circle.getA()) + MathUtils.pow2(circle.getB() + line.getC()/line.getB()) - MathUtils.pow2(circle.getRadius());
            if (a == 0) {
                if (b == 0) {
                    return null;
                }
                double x = - c / b;
                double y = line.getY(x);
                Point[] points = new Point[2];
                points[0] = new Point(x,y);
                points[1] = points[0];
                return points;
            } else {
                double z = MathUtils.pow2(b) - 4 * a * c;
                if (z >= 0) {
                    double x1 = (-b + Math.sqrt(z)) / (2 * a);
                    double x2 = (-b - Math.sqrt(z)) / (2 * a);
                    double y1 = line.getY((float) x1);
                    double y2 = line.getY((float) x2);
                    Point[] points = new Point[2];
                    points[0] = new Point(x1,y1);
                    points[1] = new Point(x2,y2);
                    return points;
                } else {
                    return null;
                }
            }
        } else {
            double x = line.getX(1);
            try {
                double[] yArray = circle.getY(x);
                if (yArray != null && yArray.length == 2 ) {
                    Point[] points = new Point[2];
                    points[0] = new Point(x,yArray[0]);
                    points[1] = new Point(x,yArray[1]);
                    return points;
                } else {
                    return null;
                }
            } catch (GeometryException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static double getTwoPointDistance(Point point1,Point point2) {
        try {
            return Math.sqrt(MathUtils.pow2(point1.getX() - point2.getX()) + MathUtils.pow2(point1.getY() - point2.getY()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double getTwoPointDistance(double x1,double y1,double x2,double y2) {
        try {
            return Math.sqrt(MathUtils.pow2(x1 -x2) + MathUtils.pow2(y1 - y2));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Point getTwoPointCenterPoint(double x1, double y1, double x2, double y2) {
        return new Point((x1 + x2) /2,(y1 + y2) / 2);
    }
}
