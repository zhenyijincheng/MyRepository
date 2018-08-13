package com.toy.qiaodata.geometrylibrary.object;

import com.toy.qiaodata.geometrylibrary.exception.GeometryException;
import com.toy.qiaodata.geometrylibrary.util.MathUtils;

/**
 * create by YANG on 2018/7/23 16:34
 */
public class Circle {
    private float radius;
    private float a;
    private float b;

    public Circle(float radius, float a, float b) {
        this.radius = radius >= 0 ? radius : 0;
        this.a = a;
        this.b = b;
    }

    public Circle(int radius, int a, int b) {
        this.radius = radius >= 0 ? radius : 0;
        this.a = a;
        this.b = b;
    }


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {

        this.a = a;
    }

    public float getB() {
        return b;
    }

    public void setB(float centerY) {
        this.b = centerY;
    }

    /**
     *
     * @param valueX
     * @param valueY
     * @return (x - a)^2 + (y - b)^2 - radius^2, > 0 在圆外；= 0 在圆上； < 0 在圆内。
     */
    public double isInCircle(float valueX,float valueY) {
        return MathUtils.pow2(valueY - b) + MathUtils.pow2(valueX - a) - MathUtils.pow2(radius);
    }

    public double[] getX(double y) throws Exception {
        if ((y < b - radius) || (y > b + radius)) {
            throw new GeometryException("输入的y不在取值该圆的值域内");
        } else {
            double[] args = new double[2];

            double sqrt = Math.sqrt(MathUtils.pow2(radius) - MathUtils.pow2(y - b));

            args[0] = a + sqrt;
            args[1] = a - sqrt;

            return args;
        }
    }

    public double[] getY(double x) throws GeometryException {
        if ((x < a - radius) || (x > a + radius)) {
            throw new GeometryException("输入的x不在取值该圆的值域内");
        } else {
            double[] args = new double[2];

            double sqrt = Math.sqrt(MathUtils.pow2(radius) - MathUtils.pow2(x - a));

            args[0] = b + sqrt;
            args[1] = b - sqrt;

            return args;
        }
    }

    public void setCircle(float radius, float a, float b) {
        this.radius = radius;
        this.a = a;
        this.b = b;
    }
}
