package cz.pule.tessel8.geometry;

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

    public float getDistance(float x, float y){

        float lineDX = p2.getX() - p1.getX();
        float lineDY = p2.getY() - p1.getY();

        float lineAngle = (float) -Math.atan(lineDY/lineDX);

        // Coordinate X2 of line, which was translated and rotated so that X1=0, Y1=0 and Y2=0
        float transformedP2X = (float)(lineDX * Math.cos(lineAngle) - lineDY * Math.sin(lineAngle));

        float relativeX = x - p1.getX();
        float relativeY = y - p1.getY();

        // X and Y transformed accordingly to line
        float transformedX = (float)(relativeX * Math.cos(lineAngle) - relativeY * Math.sin(lineAngle));
        float transformedY = (float)(relativeX * Math.sin(lineAngle) + relativeY * Math.cos(lineAngle));

        if(transformedP2X < 0){
            transformedP2X *= -1;
            transformedX *= -1;
        }

        if(transformedX >= 0 && transformedX <= transformedP2X)
            return Math.abs(transformedY);
        if(transformedX < 0)
            return (float) Math.sqrt(transformedX*transformedX + transformedY*transformedY);

        float deltaX = transformedX - transformedP2X;
        return (float) Math.sqrt(deltaX*deltaX + transformedY*transformedY);
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

    public void setOnLineSplitListener(OnLineSplitListener onLineSplitListener){
        this.onLineSplitListener = onLineSplitListener;
    }
}
