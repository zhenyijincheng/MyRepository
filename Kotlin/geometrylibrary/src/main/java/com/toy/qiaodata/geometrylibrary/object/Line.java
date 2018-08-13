package com.toy.qiaodata.geometrylibrary.object;

/**
 * create by YANG on 2018/7/17 10:22
 */
public class Line {


    private double a;
    private double b;
    private double c;

    public Line(double x1, double y1, double x2, double y2) {

        if (x1 == x2) {
            a = 1;
            b = 0;
            c = -x1;
        } else {
            a = (y2 - y1) / (x2 - x1);
            c = y1 - a * x1;
            b = -1;

        }
    }

    public Line(double k, double c) {
        a = k;
        b = -1;
        this.c = c;
    }

    public Line(double k,double x,double y) {
        if (k == Double.MAX_VALUE) {
            a = -1;
            c = x;
            b = 0;
        } else {
            a = k;
            c = y - a * x;
            b = -1;
        }
    }


    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }


    public double getK() {
        if (b != 0) {
            return - a / b;
        } else {
            return - Double.MAX_VALUE;
        }
    }

    public double getY(double x) {
        if (b == 0) {
            return Double.MAX_VALUE;
        } else {
            return (x * a + c) / -b;
        }
    }

    public double getX(double y) {
        if (a == 0) {
            return Double.MAX_VALUE;
        } else {
            return -(b / a * y + c / a);
        }
    }

    public Line getVerticalLine(double x,double y) {
        if (a == 0) {
            return new Line(Double.MAX_VALUE,x,y);
        } else {
            return new Line(-1/a,x,y);
        }
    }

}
