package com.baolar.fiveinarow;

public class Point {
    public int x;
    public int y;
    Point() {
        x = y = 0;
    }
    Point(Point p) {
        x = p.x;
        y = p.y;
    }
    Point (int xx, int yy) {
        x = xx;
        y = yy;
    }
}