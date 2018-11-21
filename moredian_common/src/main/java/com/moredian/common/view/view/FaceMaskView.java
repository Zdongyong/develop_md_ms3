
package com.moredian.common.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.megvii.bean.FaceInfo;
import com.moredian.securitygate.R;

public class FaceMaskView extends View {

    Paint localPaint = null;
    RectF mFaceRect = new RectF();
    RectF mDrawRect = null;
    private boolean isFrontalCamera = true;
    private Bitmap faceBitmap;
    private Bitmap leftBitmap;
    private Bitmap rightBitmap;
    private Bitmap curBitmap;
    private boolean isShowTips;
    private float left;
    private float top;
    private FaceInfo faceInfo;

    private int mWidth = -1;
    private int mHeight = -1;

    private static final int BG_PADDING = 68;

    public FaceMaskView(Context context) {
        super(context);
        init(context);
    }

    public FaceMaskView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {
        mDrawRect = new RectF();
        localPaint = new Paint();
        faceBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.scan);
        leftBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tips_on_left);
        rightBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tips_on_right);
    }

    public void setShowTips(boolean showTips) {
        isShowTips = showTips;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
        mFaceRect = faceInfo.position;
        postInvalidate();
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFrontal(boolean isFrontal) {
        this.isFrontalCamera = isFrontal;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFaceRect == null)
            return;
//        if(String.valueOf(AppSingleton.getInstance().getCameraFacingBack()).equals(KeyManager.BACK_CAMERA)) {
//            if (isFrontalCamera) {
//                mDrawRect.set(getWidth() * (1 - mFaceRect.right), getHeight()
//                                * mFaceRect.top, getWidth() * (1 - mFaceRect.left),
//                        getHeight()
//                                * mFaceRect.bottom);
//            } else {
//                mDrawRect.set(getWidth() * mFaceRect.left, getHeight() * mFaceRect.top, getWidth()
//                        * mFaceRect.right, getHeight() * mFaceRect.bottom);
//            }
//        } else {
//            if (!isFrontalCamera) {
//                mDrawRect.set(getWidth() * (1 - mFaceRect.right), getHeight()
//                                * mFaceRect.top, getWidth() * (1 - mFaceRect.left),
//                        getHeight()
//                                * mFaceRect.bottom);
//            } else {
//                mDrawRect.set(getWidth() * mFaceRect.left, getHeight() * mFaceRect.top, getWidth()
//                        * mFaceRect.right, getHeight() * mFaceRect.bottom);
//            }
//        }
//        canvas.drawBitmap(faceBitmap, null, mDrawRect, localPaint);
//        if (isShowTips) {
//            if ((mDrawRect.right + BG_PADDING + rightBitmap.getWidth()) > getWidth()) {
//                left = mDrawRect.left - leftBitmap.getWidth() - BG_PADDING;
//                curBitmap = leftBitmap;
//            } else {
//                left = mDrawRect.right + BG_PADDING;
//                curBitmap = rightBitmap;
//            }
//            top = mDrawRect.centerY() - curBitmap.getHeight() / 2;
//            // 绘制背景
//            canvas.drawBitmap(curBitmap, left, top, localPaint);
//        }
    }



}
