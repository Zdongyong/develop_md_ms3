package com.moredian.common.view.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.moredian.securitygate.R;
import com.moredian.securitygate.util.CommonUtil;

/**
 * 创建日期：2018/8/13 on 14:44
 * 描述：
 * 作者：zhudongyong
 */

@SuppressLint("AppCompatCustomView")
public class FaceView extends ImageView {
    private static final String TAG = FaceView.class.getSimpleName();
    private Context mContext;
    private Paint mLinePaint;
    private Face[] mFaces;
    private Matrix mMatrix = new Matrix();
    private RectF mRect = new RectF();
    private Drawable mFaceIndicator = null;
    private Bitmap mFaceBitmap = null;
    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initPaint();
        mContext = context;
        mFaceIndicator = getResources().getDrawable(R.mipmap.sign_gate);
        mFaceBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.sign);
    }


    public void setFaces(Face[] faces){
        this.mFaces = faces;
        invalidate();
    }

    public void clearFaces(){
        mFaces = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if(mFaces == null || mFaces.length < 1){
            return;
        }
        boolean isMirror = false;
        int Id = CommonUtil.FindBackCamera();
        if(Id == CameraInfo.CAMERA_FACING_BACK){
            isMirror = false; //后置Camera无需mirror
        }else if(Id == CameraInfo.CAMERA_FACING_FRONT){
            isMirror = true;  //前置Camera需要mirror
        }
        CommonUtil.prepareMatrix(mMatrix, isMirror, 90, getWidth(), getHeight());
        canvas.save();
        mMatrix.postRotate(0); //Matrix.postRotate默认是顺时针
        canvas.rotate(-0);   //Canvas.rotate()默认是逆时针
        for(int i = 0; i< mFaces.length; i++){
            mRect.set(mFaces[i].rect);
            mMatrix.mapRect(mRect);
//            方法一：
//            mFaceIndicator.setBounds(Math.round(mRect.left), Math.round(mRect.top),
//                    Math.round(mRect.right), Math.round(mRect.bottom));
//            mFaceIndicator.draw(canvas);
//            方法二：
            canvas.drawBitmap(mFaceBitmap,null,mRect,mLinePaint);
        }
        canvas.restore();
        super.onDraw(canvas);
    }

    private void initPaint(){
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        int color = Color.rgb(9, 189, 198);
//        mLinePaint.setColor(color);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(8f);
        mLinePaint.setAlpha(180);
    }


}
