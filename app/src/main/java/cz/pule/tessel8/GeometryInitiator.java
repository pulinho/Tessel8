package cz.pule.tessel8;

import android.graphics.Matrix;

import cz.pule.tessel8.geometry.Line;
import cz.pule.tessel8.geometry.LinePair;
import cz.pule.tessel8.geometry.Point;

/**
 * Created by pule on 8. 9. 2016.
 */
public class GeometryInitiator {

    private GeometryManager gm;

    public GeometryInitiator(GeometryManager gm){
        this.gm = gm;
    }

    public void square1(){

        Point p1 = new Point(-200, -200, true);
        Point p2 = new Point(-200, 200, true);
        Point p3 = new Point(200, 200, true);
        Point p4 = new Point(200, -200, true);

        Line l1 = new Line(p1, p2);
        Line l2 = new Line(p2, p3);
        Line l3 = new Line(p4, p3);
        Line l4 = new Line(p1, p4);

        gm.addLine(l1);
        gm.addLine(l2);
        gm.addLine(l3);
        gm.addLine(l4);

        Matrix matrix = new Matrix();
        matrix.postTranslate(400, 0);

        Matrix matrix2 = new Matrix();
        matrix2.postTranslate(0, -400);

        new LinePair(l1, l3, matrix, gm);
        new LinePair(l2, l4, matrix2, gm);

        gm.addPoint(p1);
        gm.addPoint(p2);
        gm.addPoint(p3);
        gm.addPoint(p4);

        //==

        Matrix gm1 = new Matrix();
        gm1.postTranslate(400, 400);
        Matrix gm2 = new Matrix();
        gm2.postTranslate(-400, 400);

        gm.setGridMatrices(gm1, gm2);
    }

    public void triangle1(){

        Point p1 = new Point(-200, 170, true);
        Point p2 = new Point(0, 170, true);
        Point p3 = new Point(200, 170, true);

        Line l1 = new Line(p1, p2);
        Line l2 = new Line(p3, p2);

        gm.addLine(l1);
        gm.addLine(l2);

        Matrix matrix = new Matrix();
        matrix.postRotate(180, 0, 170);

        new LinePair(l1, l2, matrix, gm);

        //=====

        Point p4 = new Point(-100, 0, true);
        Point p5 = new Point(0, -170, true);

        Line l3 = new Line(p1, p4);
        Line l4 = new Line(p5, p4);

        gm.addLine(l3);
        gm.addLine(l4);

        Matrix matrix2 = new Matrix();
        matrix2.postRotate(180, -100, 0);

        new LinePair(l3, l4, matrix2, gm);

        //==

        Point p6 = new Point(100, 0, true);

        Line l5 = new Line(p5, p6);
        Line l6 = new Line(p3, p6);

        gm.addLine(l5);
        gm.addLine(l6);

        Matrix matrix3 = new Matrix();
        matrix3.postRotate(180, 100, 0);

        new LinePair(l5, l6, matrix3, gm);

        gm.addPoint(p1);
        gm.addPoint(p2);
        gm.addPoint(p3);
        gm.addPoint(p6);
        gm.addPoint(p5);
        gm.addPoint(p4);

        // ==

        Matrix gm1 = new Matrix();
        gm1.postTranslate(400, 0);
        Matrix gm2 = new Matrix();
        gm2.postTranslate(200, 340);

        gm.setGridMatrices(gm1, gm2);
    }
}
