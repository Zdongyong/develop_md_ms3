package com.moredian.common.view.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * 源码参考
 * https://github.com/hehonghui/ripplelayout/
 * */
public class RippleLayout extends RelativeLayout {

    private static final int DEFAULT_RIPPLE_COUNT = 3;
    private static final int DEFAULT_DURATION_TIME = 2200;
    private static final int DEFAULT_CIRCLE_DURATION_TIME = 1600;
    private static final int DEFAULT_START_DELAY_TIME[] = {0, 1200};

    private static final float DEFAULT_SCALE = 1.6f;
    private static final int DEFAULT_RIPPLE_COLOR = Color.rgb(0x09, 0xbd, 0xc6);
    private static final int DEFAULT_STROKE_WIDTH = 5;
    private static final int DEFAULT_RADIUS = 145; //dp

    // 波纹颜色
    private int mRippleColor = DEFAULT_RIPPLE_COLOR;
    private float mStrokeWidth = DEFAULT_STROKE_WIDTH;

    // 波纹半径
    private float mRippleRadius;

    //动画总时长
    private int mAnimDuration = DEFAULT_DURATION_TIME;

    //波纹时长
    private int mCircleDuration = DEFAULT_CIRCLE_DURATION_TIME;

    //波纹圈数
    private int mRippleViewNums = DEFAULT_RIPPLE_COUNT;

    //波纹放大倍数
    private float mRippleScale = DEFAULT_SCALE;

    private boolean animationRunning = false;

    private Paint mPaint = new Paint();

    //动画集,执行缩放、alpha动画,使得背景色渐变
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    //动画列表,保存几个动画
    private ArrayList<Animator> mAnimatorList = new ArrayList<Animator>();

    private LayoutParams mRippleViewParams;

    //初始波纹的直径
    private int mRippleSide;

    public RippleLayout(Context context) {
        super(context);
        init(context, null);
    }

    public RippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        initPaint();
        initRippleViewLayoutParams();
        generateRippleViews();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mStrokeWidth = 0;
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mRippleColor);
    }

    private void initRippleViewLayoutParams() {
        // ripple view的大小为 半径 + 笔宽的两倍
//        int rippleSide = (int) (2 * (mRippleRadius + mStrokeWidth));
//        mRippleViewParams = new LayoutParams(rippleSide, rippleSide);
        mRippleViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // 居中显示
        mRippleViewParams.addRule(CENTER_IN_PARENT, TRUE);

        mRippleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, DEFAULT_RADIUS, getResources().getDisplayMetrics());

        mRippleSide = (int) (2 * (mRippleRadius + mStrokeWidth));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = (int) (mRippleRadius * 2 * DEFAULT_SCALE);
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (int) (mRippleRadius * 2 * DEFAULT_SCALE);
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));

//        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 初始化RippleViews，并且将动画设置到RippleView上,使之在x, y不断扩大,并且背景色逐渐淡化
     */
    private void generateRippleViews() {

        //加一个没有动画的圆
        addView(new RippleView(getContext()), mRippleViewParams);

        // 添加RippleView
        for (int i = 0; i < mRippleViewNums; i++) {
            RippleView rippleView = new RippleView(getContext());
            addView(rippleView, mRippleViewParams);

            //动画延时开始
            long startDelay = 0;
            if(DEFAULT_START_DELAY_TIME.length > i) {
                startDelay = DEFAULT_START_DELAY_TIME[i];
            }

            // 添加动画
            addAnimToRippleView(rippleView, mCircleDuration, startDelay);
        }

        mAnimatorSet.setDuration(mAnimDuration);
        mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        // x, y, alpha动画一块执行
        mAnimatorSet.playTogether(mAnimatorList);
    }

    private Runnable repeatRunnable = new Runnable() {
        @Override
        public void run() {
            if(animationRunning) {
                mAnimatorSet.start();
            }
        }
    };

    /**
     * 为每个RippleView添加动画效果,并且设置动画延时,每个视图启动动画的时间不同,就会产生波纹
     *
     * @param rippleView
     * @param startDelay
     */
    private void addAnimToRippleView(final RippleView rippleView, long duration, long startDelay) {
        // x轴的缩放动画
        final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "scaleX",
                1.0f, mRippleScale);
        scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
        scaleXAnimator.setStartDelay(startDelay);
        scaleXAnimator.setDuration(duration);
        mAnimatorList.add(scaleXAnimator);

        // y轴的缩放动画
        final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "scaleY",
                1.0f, mRippleScale);
        scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
        scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleYAnimator.setStartDelay(startDelay);
        scaleYAnimator.setDuration(duration);
        mAnimatorList.add(scaleYAnimator);

        // 颜色的alpha渐变动画
        final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(
                rippleView, "alpha", 1.0f, 0f);
        alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
        alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        alphaAnimator.setDuration(duration);
        alphaAnimator.setStartDelay(startDelay);
        mAnimatorList.add(alphaAnimator);
    }

    private void makeRippleViewsVisible() {
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = this.getChildAt(i);
            if (childView instanceof RippleView) {
                childView.setVisibility(VISIBLE);
            }
        }
    }

    /** 开始波纹动画*/
    public void startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            makeRippleViewsVisible();
            mAnimatorSet.start();
            animationRunning = true;
        }
    }

    /** 停止波纹动画*/
    public void stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            animationRunning = false;
            removeCallbacks(repeatRunnable);
            mAnimatorSet.end();
        }
    }

    public boolean isRippleAnimationRunning() {
        return animationRunning;
    }

    /**
     * RippleView产生波纹效果, 默认不可见,当启动动画时才设置为可见
     *
     * @author mrsimple
     */
    private class RippleView extends View {

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int radius = mRippleSide / 2;
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - mStrokeWidth, mPaint);
        }
    }
}
