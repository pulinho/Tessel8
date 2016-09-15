package cz.pule.tessel8.geometry;

import android.graphics.Matrix;

import cz.pule.tessel8.GeometryManager;

/**
 * Created by pule on 8. 9. 2016.
 */
public class LinePair {

    public LinePair(final Line l1, final Line l2, final Matrix matrix, final GeometryManager gm){

        l1.setOnLineSplitListener(new OnLineSplitListener() {
            @Override
            public void onLineSplit(Line newLine) {
                gm.addLine(newLine);

                Point newPoint = newLine.getP1();

                Point pairPoint = new Point();
                new PointPair(newPoint, pairPoint, matrix);
                gm.insertPointBetween(pairPoint, l2.getP1(), l2.getP2());

                Line pairLine = l2.splitWithPoint(pairPoint);
                gm.addLine(pairLine);

                new LinePair(newLine, pairLine, matrix, gm);
            }
        });

        final Matrix inverted = new Matrix();
        matrix.invert(inverted);

        l2.setOnLineSplitListener(new OnLineSplitListener() {
            @Override
            public void onLineSplit(Line newLine) {
                gm.addLine(newLine);

                Point newPoint = newLine.getP1();

                Point pairPoint = new Point();
                new PointPair(newPoint, pairPoint, inverted);
                gm.insertPointBetween(pairPoint, l1.getP1(), l1.getP2());

                Line pairLine = l1.splitWithPoint(pairPoint);
                gm.addLine(pairLine);

                new LinePair(newLine, pairLine, inverted, gm);
            }
        });
    }
}
