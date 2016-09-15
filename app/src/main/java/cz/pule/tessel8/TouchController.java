package cz.pule.tessel8;

import android.graphics.Matrix;
import android.view.MotionEvent;

/**
 * Created by pule on 9. 9. 2016.
 */
public class TouchController {

    private Matrix viewMatrix;
    private Matrix viewMatrixInverted;
    private GeometryManager geometryManager;

    private boolean rotoZoomActive = false;

    private float distance;
    private float angle;

    public TouchController(Matrix viewMatrix, GeometryManager geometryManager){
        this.viewMatrix = viewMatrix;
        this.geometryManager = geometryManager;

        viewMatrixInverted = new Matrix();
        viewMatrix.invert(viewMatrixInverted);
    }

    private float[] mapPointWithInvertedViewMatrix(float x, float y){

        float[] coords = new float[]{x, y};
        viewMatrixInverted.mapPoints(coords);

        return coords;
    }

    public void onTouchEvent(MotionEvent event){

        float[] coords;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                coords = mapPointWithInvertedViewMatrix(event.getX(), event.getY());
                geometryManager.touchDown(coords[0], coords[1]);
                break;
            case MotionEvent.ACTION_MOVE:
                if(rotoZoomActive) rotoZoom(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                else{
                    coords = mapPointWithInvertedViewMatrix(event.getX(), event.getY());
                    geometryManager.touchMove(coords[0], coords[1]);
                }
                break;
            case MotionEvent.ACTION_UP:
                geometryManager.touchUp();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                geometryManager.touchUp();
                rotoZoom(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                geometryManager.touchUp();
                rotoZoomActive = false;
                break;
        }
    }

    private void rotoZoom(float x1, float y1, float x2, float y2){

        if(!rotoZoomActive){
            rotoZoomActive = true;
            distance = getDistance(x1, y1, x2, y2);
            angle = getAngle(x1, y1, x2, y2);
            return;
        }

        //zoom
        float newDistance = getDistance(x1, y1, x2, y2);
        float scale = newDistance / distance;

        viewMatrix.preScale(scale, scale);

        distance = newDistance;

        //rotation
        float newAngle = getAngle(x1, y1, x2, y2);
        float rotate = newAngle - angle;
        if(rotate > 100 || rotate < -100) rotate -= 180;
        viewMatrix.preRotate(rotate);

        angle = newAngle;

        viewMatrix.invert(viewMatrixInverted);
    }

    private static float getAngle(float x1, float y1, float x2, float y2){

        float dx = x1 - x2;
        float dy = y1 - y2;

        return (float) Math.toDegrees(Math.atan(dy/dx));
    }

    private static float getDistance(float x1, float y1, float x2, float y2){

        float dx = x1 - x2;
        float dy = y1 - y2;

        return (float) Math.sqrt(dx*dx + dy*dy);
    }
}
