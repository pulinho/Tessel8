package cz.pule.tessel8.geometry;

import android.graphics.Matrix;

/**
 * Created by pule on 7. 9. 2016.
 */
public class PointPair {

    public PointPair(final Point p1, final Point p2, final Matrix matrix){

        p2.setCoordsWithMatrix(p1.getX(), p1.getY(), matrix);

        p1.setOnPointMovedListener(new OnPointMovedListener() {
            @Override
            public void onPointMoved(float x, float y) {
                p2.setCoordsWithMatrix(x, y, matrix);
            }
        });

        final Matrix inverted = new Matrix();
        matrix.invert(inverted);

        p2.setOnPointMovedListener(new OnPointMovedListener() {
            @Override
            public void onPointMoved(float x, float y) {
                p1.setCoordsWithMatrix(x, y, inverted);
            }
        });
    }
}
