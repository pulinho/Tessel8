package cz.pule.tessel8;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

import cz.pule.tessel8.geometry.Line;
import cz.pule.tessel8.geometry.Point;

/**
 * Created by pule on 7. 9. 2016.
 */
public class GeometryManager {

    private static final float PICKING_WIDTH = 30.0f; // todo: density
    private static final int GRID_WIDTH = 40;

    private ArrayList<Point> pointList = new ArrayList<>(); // todo: remove, use lineList only
    private ArrayList<Line> lineList = new ArrayList<>();

    private Point pickedPoint;
    private Paint paint;

    private Matrix gridMatrix1;
    //private Matrix gridMatrix2;
    private Matrix gridHalfInverted = new Matrix();
    private Matrix gridNextLine = new Matrix();

    public GeometryManager(){

        GeometryInitiator initiator = new GeometryInitiator(this);
        initiator.triangle1();

        initPaint();
    }

    private void initPaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);
    }

    public void addLine(Line l){
        lineList.add(l);
    }

    public void addPoint(Point p){
        pointList.add(p);
    }

    public void insertPointBetween(Point point, Point between1, Point between2){

        int i1 = pointList.indexOf(between1);
        int i2 = pointList.indexOf(between2);
        int index = (i1==0 && i2!= 1) || (i2==0 && i1 != 1) ? pointList.size() : Math.max(i1, i2);

        pointList.add(index, point);
    }

    public void drawGeometry(Canvas canvas){

        if(pointList.size() == 0) return;

        Path path = new Path();

        Point first = pointList.get(0);
        path.moveTo(first.getX(), first.getY());

        for(Point p : pointList){
            path.lineTo(p.getX(), p.getY());
        }

        path.lineTo(first.getX(), first.getY());

        //canvas.drawPath(path, paint);

        drawGrid(path, canvas);
    }

    private void drawGrid(Path path, Canvas canvas){

        if(gridMatrix1 == null) return;

        canvas.concat(gridHalfInverted);

        for(int i = 0; i < GRID_WIDTH; i++){

            for(int j = 0; j < GRID_WIDTH; j++){
                canvas.drawPath(path, paint);
                canvas.concat(gridMatrix1);
            }

            canvas.concat(gridNextLine);
        }
    }

    public void setGridMatrices(Matrix gm1, Matrix gm2) {
        this.gridMatrix1 = gm1;
        //this.gridMatrix2 = gm2;

        Matrix inv1 = new Matrix();
        Matrix inv2 = new Matrix();

        gm1.invert(inv1);
        gm2.invert(inv2);

        Matrix inv = new Matrix();
        inv.setConcat(inv1, inv2);

        // todo: there's probably a better way to do following
        for(int i = 0; i < GRID_WIDTH; i++){
            if(i < GRID_WIDTH /2) gridHalfInverted.postConcat(inv);
            gridNextLine.postConcat(inv1);
        }
        gridNextLine.postConcat(gm2);
    }

    public void touchDown(float x, float y){

        pickedPoint = pickPoint(x, y);
        if(pickedPoint != null) return;

        Line pickedLine = pickLine(x, y);
        if(pickedLine != null){
            Point p1 = pickedLine.getP1();
            Point p2 = pickedLine.getP2();

            pickedPoint = pickedLine.splitAndPropagate(x, y);

            insertPointBetween(pickedPoint, p1, p2);
        }
    }

    public void touchMove(float x, float y){
        if(pickedPoint != null) pickedPoint.setCoordsAndPropagateChange(x, y);
    }

    public void touchUp(){
        pickedPoint = null;
    }

    private Line pickLine(float x, float y){

        Line closestLine = null;
        float closestDistance = PICKING_WIDTH;

        for(Line l : lineList){
            float distance = l.getDistance(x, y);

            if(distance < closestDistance){
                closestLine = l;
                closestDistance = distance;
            }
        }

        return closestLine;
    }

    private Point pickPoint(float x, float y){

        Point closestPoint = null;
        float closestDistance = PICKING_WIDTH;

        for(Point p : pointList){

            if(p.isStationary()) continue;

            float distance = p.getDistance(x, y);

            if(distance < closestDistance){
                closestPoint = p;
                closestDistance = distance;
            }
        }

        return closestPoint;
    }
}
