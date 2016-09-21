package cz.pule.tessel8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pule on 7. 9. 2016.
 */
public class CanvasView extends View {

    private Matrix viewMatrix;
    private TouchController touchController;
    private GeometryManager geometryManager;

    public CanvasView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        geometryManager = new GeometryManager(metrics.density);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Bitmap mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);

        viewMatrix = new Matrix();
        viewMatrix.postTranslate(mCanvas.getWidth()/2, mCanvas.getHeight()/2);
        touchController = new TouchController(viewMatrix, geometryManager);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.concat(viewMatrix);
        geometryManager.drawGeometry(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchController.onTouchEvent(event);
        invalidate();
        return true;
    }

    public GeometryManager getGeometryManager() {
        return geometryManager;
    }
}
