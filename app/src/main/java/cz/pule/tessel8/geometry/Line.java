package cz.pule.tessel8.geometry;

import android.graphics.Matrix;

/**
 * Created by pule on 7. 9. 2016.
 */
public class Line {

    private Point p1;
    private Point p2;
    private OnLineSplitListener onLineSplitListener;

    public Line(Point p1, Point p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    /**
     * Returns the closest distance to line from specified coordinates.
     *
     * @param x - coordinate x from which the distance to line is computed
     * @param y - coordinate y
     * @return computed distance
     */
    public float getDistance(float x, float y){

        float lineLength = this.getLength();

        Matrix m = new Matrix();

        // next three lines get the transformation matrix m so that
        // the transformed line's p1 = (0, 0) and p2 = (lineLength, 0)
        float[] src = new float[]{p1.getX(), p1.getY(), p2.getX(), p2.getY()};
        float[] dst = new float[]{0, 0, lineLength, 0};
        m.setPolyToPoly(src, 0, dst, 0, 2);

        //now we apply the matrix on point (x, y)
        float[] coords = new float[]{x, y};
        m.mapPoints(coords);

        //now we can easily measure the point's closest distance from the line
        if(coords[0] >= 0 && coords[0] <= lineLength)
            return Math.abs(coords[1]);
        if(coords[0] < 0)
            return (float) Math.sqrt(coords[0]*coords[0] + coords[1]*coords[1]);

        float dX = coords[0] - lineLength;
        return (float) Math.sqrt(dX*dX + coords[1]*coords[1]);
    }

    public Point splitAndPropagate(float x, float y){
        Point p = new Point(x, y);

        Line newLine = new Line(p, this.p2);
        this.p2 = p;

        if(onLineSplitListener != null) onLineSplitListener.onLineSplit(newLine);
        return p;
    }

    public Line splitWithPoint(Point p){

        Line newLine = new Line(p, this.p2);
        this.p2 = p;

        return newLine;
    }

    public float getLength(){

        float lineDX = p2.getX() - p1.getX();
        float lineDY = p2.getY() - p1.getY();

        return (float) Math.sqrt(lineDX*lineDX + lineDY*lineDY);
    }

    public void setOnLineSplitListener(OnLineSplitListener onLineSplitListener){
        this.onLineSplitListener = onLineSplitListener;
    }
}
