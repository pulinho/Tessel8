package cz.pule.tessel8.geometry;

import android.graphics.Matrix;

/**
 * Created by pule on 7. 9. 2016.
 */
public class Point {

    private float x;
    private float y;

    private boolean stationary = false;

    private OnPointMovedListener onPointMovedListener;

    public Point(){
        this.x = 0;
        this.y = 0;
    }

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Point(float x, float y, boolean stationary){
        this.x = x;
        this.y = y;
        this.stationary = stationary;
    }

    public void setCoordsAndPropagateChange(float x, float y){

        if(this.x == x && this.y == y) return;

        this.x = x;
        this.y = y;

        if(onPointMovedListener != null) onPointMovedListener.onPointMoved(x, y);
    }

    public void setCoordsWithMatrix(float x, float y, Matrix matrix) {

        float[] coords = new float[]{x, y};
        matrix.mapPoints(coords);

        this.x = coords[0];
        this.y = coords[1];
    }

    public float getDistance(float x, float y){
        float dx = Math.abs(x - this.x);
        float dy = Math.abs(y - this.y);
        return (float) Math.sqrt((double)dx*dx + dy*dy);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isStationary() {
        return stationary;
    }

    public void setOnPointMovedListener(OnPointMovedListener onPointMovedListener) {
        this.onPointMovedListener = onPointMovedListener;
    }
}
