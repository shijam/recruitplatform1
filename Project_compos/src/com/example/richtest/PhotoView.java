
package com.example.richtest;

/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.richtest.PhotoViewAttacher.OnMatrixChangedListener;
import com.example.richtest.PhotoViewAttacher.OnPhotoTapListener;
import com.example.richtest.PhotoViewAttacher.OnViewTapListener;

public class PhotoView extends ImageView implements IPhotoView {

    private final PhotoViewAttacher mAttacher;

    private ScaleType mPendingScaleType;

    private static Matrix matrix = new Matrix();
    private static Matrix photoMatrix = new Matrix();
    private static Matrix baseMatrix = new Matrix();
    private static Matrix oldMatrix = new Matrix();

    private static PhotoView instance;

    private float myscale;
    private final int LINE = 2;

    public static Path mPath = new Path();

    private final Paint mGesturePaint = new Paint();

    private Context context;

    private float mX;
    private float mY;
    private float endX;
    private float endY;

    private boolean isError = false;

    private SimpleSampleActivity activity;
    private OnDrawFinishListener onfinishListener;

    public PhotoView(Context context) {
        this(context, null);
        instance = this;
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Style.STROKE);
        int LineW = SimpleSampleActivity.isHD?5:3;
        mGesturePaint.setStrokeWidth(LineW);
        mGesturePaint.setColor(Color.RED);
        this.context = context;
    }

    public PhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
        instance = this;
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Style.STROKE);
        int LineW = SimpleSampleActivity.isHD?5:3;
        mGesturePaint.setStrokeWidth(LineW);
        mGesturePaint.setColor(Color.RED);
        this.context = context;
    }

    public PhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        super.setScaleType(ScaleType.MATRIX);
        mAttacher = new PhotoViewAttacher(this);

        if (null != mPendingScaleType) {
            setScaleType(mPendingScaleType);
            mPendingScaleType = null;
        }
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Style.STROKE);
        int LineW = SimpleSampleActivity.isHD?5:3;
        mGesturePaint.setStrokeWidth(LineW);
        mGesturePaint.setColor(Color.RED);
        instance = this;
        this.context = context;
    }

    /**
     * @deprecated use {@link #setRotationTo(float)}
     */
    @Override
    public void setPhotoViewRotation(float rotationDegree) {
        mAttacher.setRotationTo(rotationDegree);
    }

    @Override
    public void setRotationTo(float rotationDegree) {
        mAttacher.setRotationTo(rotationDegree);
    }

    @Override
    public void setRotationBy(float rotationDegree) {
        mAttacher.setRotationBy(rotationDegree);
    }

    @Override
    public boolean canZoom() {
        return mAttacher.canZoom();
    }

    @Override
    public RectF getDisplayRect() {
        return mAttacher.getDisplayRect();
    }

    @Override
    public Matrix getDisplayMatrix() {
        return mAttacher.getDrawMatrix();
    }

    @Override
    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return mAttacher.setDisplayMatrix(finalRectangle);
    }

    @Override
    @Deprecated
    public float getMinScale() {
        return getMinimumScale();
    }

    @Override
    public float getMinimumScale() {
        return mAttacher.getMinimumScale();
    }

    @Override
    @Deprecated
    public float getMidScale() {
        return getMediumScale();
    }

    @Override
    public float getMediumScale() {
        return mAttacher.getMediumScale();
    }

    @Override
    @Deprecated
    public float getMaxScale() {
        return getMaximumScale();
    }

    @Override
    public float getMaximumScale() {
        return mAttacher.getMaximumScale();
    }

    @Override
    public float getScale() {
        return mAttacher.getScale();
    }

    @Override
    public ScaleType getScaleType() {
        return mAttacher.getScaleType();
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
    @Deprecated
    public void setMinScale(float minScale) {
        setMinimumScale(minScale);
    }

    @Override
    public void setMinimumScale(float minimumScale) {
        mAttacher.setMinimumScale(minimumScale);
    }

    @Override
    @Deprecated
    public void setMidScale(float midScale) {
        setMediumScale(midScale);
    }

    @Override
    public void setMediumScale(float mediumScale) {
        mAttacher.setMediumScale(mediumScale);
    }

    @Override
    @Deprecated
    public void setMaxScale(float maxScale) {
        setMaximumScale(maxScale);
    }

    @Override
    public void setMaximumScale(float maximumScale) {
        mAttacher.setMaximumScale(maximumScale);
    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        mAttacher.setOnMatrixChangeListener(listener);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        mAttacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mAttacher.setOnPhotoTapListener(listener);
    }

    @Override
    public OnPhotoTapListener getOnPhotoTapListener() {
        return mAttacher.getOnPhotoTapListener();
    }

    @Override
    public void setOnViewTapListener(OnViewTapListener listener) {
        mAttacher.setOnViewTapListener(listener);
    }

    @Override
    public OnViewTapListener getOnViewTapListener() {
        return mAttacher.getOnViewTapListener();
    }

    @Override
    public void setScale(float scale) {
        mAttacher.setScale(scale);
    }

    @Override
    public void setScale(float scale, boolean animate) {
        mAttacher.setScale(scale, animate);
    }

    @Override
    public void setScale(float scale, float focalX, float focalY,
            boolean animate) {
        mAttacher.setScale(scale, focalX, focalY, animate);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (null != mAttacher) {
            mAttacher.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
    }

    @Override
    public void setZoomable(boolean zoomable) {
        mAttacher.setZoomable(zoomable);
    }

    @Override
    public Bitmap getVisibleRectangleBitmap() {
        return mAttacher.getVisibleRectangleBitmap();
    }

    @Override
    public void setZoomTransitionDuration(int milliseconds) {
        mAttacher.setZoomTransitionDuration(milliseconds);
    }

    @Override
    public IPhotoView getIPhotoViewImplementation() {
        return mAttacher;
    }

    @Override
    public void setOnDoubleTapListener(
            GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        mAttacher.setOnDoubleTapListener(newOnDoubleTapListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttacher.cleanup();
        super.onDetachedFromWindow();
    }

    public static void setM(Matrix matrixx) {
        // matrix.set(new Matrix(oldMatrix));
        // matrix.set(baseMatrix);
        // matrix.postConcat(matrixx);
        // oldMatrix = matrixx;
        matrix.set(matrixx);
        // matrix = matrixx;
        instance.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (SimpleSampleActivity.isEdit) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath = new Path();
                    touchDown(event);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (!isError) {
                        touchMove(event);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                	float x = endX-mX;
                	float y = endY-mY;
                	float len = (x>y)?x:y;
                    if (!isError&&len>3) {
                        SimpleSampleActivity.tempPathList.add(mPath);
                        PointF end = new PointF(endX, endY);
                        activity.addTips(this, end);
                    }
                    isError = false;
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void touchDown(MotionEvent event) {


        float x = event.getX();
        float y = event.getY();

        if (getDisplayRect().left > 0) {
//            if (x < getDisplayRect().left) {
//                Toast.makeText(context, "errror!getDisplayRect().left x= "+x+"|"+getDisplayRect().left, Toast.LENGTH_SHORT).show();
//                isError = true;
//                return;
//            } else if (x > getDisplayRect().right) {
//                Toast.makeText(context, "errror getDisplayRect().right x= "+x+"|"+getDisplayRect().right, Toast.LENGTH_SHORT).show();
//                isError = true;
//                return;
//            }
        }
        if (getDisplayRect().top > 0) {
            if (y < getDisplayRect().top) {
                Toast.makeText(context, "errror getDisplayRect().top x= "+x+"|"+getDisplayRect().top, Toast.LENGTH_SHORT).show();
                isError = true;
                return;
            } else if (y > getDisplayRect().bottom) {
                Toast.makeText(context, "errro rgetDisplayRect().bottom x= "+x+"|"+getDisplayRect().bottom, Toast.LENGTH_SHORT).show();
                isError = true;
                return;
            }
        }
        mX = (x - SimpleSampleActivity.xxx) / myscale;
        mY = (y - SimpleSampleActivity.yyy) / myscale;
        // mPath.moveTo(x, y);
        endX = (x - SimpleSampleActivity.xxx) / myscale;
        endY = (y - SimpleSampleActivity.yyy) / myscale;
    }

    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        if (getDisplayRect().left > 0) {
            if (x < getDisplayRect().left) {
                return;
            } else if (x > getDisplayRect().right) {
                return;
            }
        }
        if (getDisplayRect().top > 0) {
            if (y < getDisplayRect().top) {
                return;
            } else if (y > getDisplayRect().bottom) {
                return;
            }
        }


        final float previousX = endX;
        final float previousY = endY;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);

        if (dx >= 3 || dy >= 3) {

            mPath = new Path();
            mPath.moveTo(mX, mY);
            endX = (x - SimpleSampleActivity.xxx) / myscale;
            endY = (y - SimpleSampleActivity.yyy) / myscale;
            mPath.lineTo(endX, endY);


            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (SimpleSampleActivity.isEdit) {
            canvas.concat(photoMatrix);
        } else {
            canvas.concat(matrix);
        }

        canvas.drawPath(mPath, mGesturePaint);

        if (SimpleSampleActivity.tempPathList != null) {
            for (Path pp : SimpleSampleActivity.tempPathList) {
                canvas.drawPath(pp, mGesturePaint);

            }
        }
    }

    public void setInit() {

        mPath = new Path();
        isError = false;
        if (SimpleSampleActivity.tempPathList == null) {
            SimpleSampleActivity.tempPathList = new ArrayList<Path>();
        }
    }

    public void uodoLine() {
        if (mPath != null) {
            mPath.reset();
        }
        if (SimpleSampleActivity.tempPathList != null
                && !SimpleSampleActivity.tempPathList.isEmpty()) {
            activity.delLastTempButton(SimpleSampleActivity.news
                    .get(SimpleSampleActivity.tempPathList.size() - 1), SimpleSampleActivity.tempPathList.size() - 1);
        }
    }

    public void delLine(int index) {
        if (mPath != null) {
            mPath.reset();
        }
        if (SimpleSampleActivity.tempPathList != null
                && !SimpleSampleActivity.tempPathList.isEmpty()) {
            SimpleSampleActivity.tempPathList.remove(index);
            invalidate();
        }
    }

    public void delLastLine() {
        if (mPath != null) {
            mPath.reset();
        }
        if (SimpleSampleActivity.tempPathList != null
                && !SimpleSampleActivity.tempPathList.isEmpty()) {
            SimpleSampleActivity.tempPathList
                    .remove(SimpleSampleActivity.tempPathList.size() - 1);
            invalidate();
        }
    }

    public Bitmap saveBmp(boolean isHD) {

    	invalidate();
        Bitmap mbmp = Bitmap.createBitmap(isHD?2048:1024, isHD?1400:700,
        		Config.ARGB_8888);
        Canvas canvas = new Canvas(mbmp);
        draw(canvas);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return mbmp;
    }

    public OnDrawFinishListener getOnfinishListener() {
        return onfinishListener;
    }

    public void setOnfinishListener(OnDrawFinishListener onfinishListener) {
        this.onfinishListener = onfinishListener;
    }

    public SimpleSampleActivity getActivity() {
        return activity;
    }

    public void setActivity(SimpleSampleActivity activity) {
        this.activity = activity;
    }

    public float getMyscale() {
        return myscale;
    }

    public void setMyscale(float myscale) {
        this.myscale = myscale;
    }

    public void refreshMatrix() {
        photoMatrix = new Matrix(matrix);
    }
}
