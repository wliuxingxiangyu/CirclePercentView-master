package com.wingsofts.circlepercentview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by mi on 16-11-2.
 */
public class MyCircle extends View {
    /**
     * 画笔对象的引用
     */
    private Paint[] paints;
    RectF oval;
    /**
     * 圆环的颜色
     */
    private int roundColor;
    /**
     * 圆环的宽度
     */
    private float roundWidth;
    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 移动
     */
    Scroller scroller;

    public MyCircle(Context context) {
        this(context, null);
    }

    public MyCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        paints = new Paint[4];
        for (int i = 0; i < paints.length; i++) {
            paints[i] = new Paint();
        }
        oval = new RectF();

        TypedArray mTypedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.myRound, defStyleAttr, 0);

        roundColor = mTypedArray.getColor(R.styleable.myRound_myRoundColor,
                Color.GRAY);
        roundWidth = mTypedArray.getDimension(R.styleable.myRound_myRoundWidth,
                3);
        roundProgressColor = mTypedArray.getColor(
                R.styleable.myRound_myRoundProgressColor, Color.RED);
        mTypedArray.recycle();

//		AccelerateInterpolator localAccelerateInterpolator = new AccelerateInterpolator();
//		this.scroller = new Scroller(context, localAccelerateInterpolator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        float centre = getWidth() / 2; // 获取圆心的x坐标
        float radius = (centre - roundWidth / 2); // 圆环的半径

        paints[0].setColor(roundColor); // 设置圆环的颜色
        paints[0].setStyle(Paint.Style.STROKE); // 设置空心
        paints[0].setStrokeWidth(roundWidth); // 设置圆环的宽度
        paints[0].setAntiAlias(true); // 消除锯齿
        paints[0].setStrokeCap(Paint.Cap.ROUND);// 圆角

        canvas.drawCircle(centre, centre, radius, paints[0]); // 画出圆环

        paints[0].setColor(roundProgressColor);
        // 用于定义的圆弧的形状和大小的界限.指定圆弧的外轮廓矩形区域
        // 椭圆的上下左右四个点(View 左上角为0)
        oval.set(centre - radius, centre - radius, centre + radius, centre
                + radius);

        //画圆弧
        canvas.drawArc(oval, -90, progress, false, paints[0]);

        /**
         * 画进度百分比的text
         */
        paints[0].setStrokeWidth(0);
        paints[0].setColor(roundColor);
        paints[0].setTextSize(14);
        paints[0].setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
        float textWidth = paints[0].measureText(progressText + "%");
        canvas.drawText(progressText + "%", centre - textWidth / 2,
                centre + 14 / 2, paints[0]); // 画出进度百分比
    }

    public static final String PROGRESS_PROPERTY = "progress";

    protected float progress;
    protected float progressText;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress * 360 / 100;
        invalidate();// UI thread
        // postInvalidate();//non-UI thread.
    }

    public void dodo(float progressText, float progress) {
        this.progressText = progressText;
        this.progress = progress;

        //也可使用3.0的AnimationSet实现
//		AnimationSet set = new AnimationSet(true);
//		set.setRepeatCount(AnimationSet.INFINITE);
//		set.setInterpolator(new AccelerateDecelerateInterpolator());
//		set.start();
//		this.setAnimation(set);

        //4.0以上，在AnimationSet基础上封装的，遗憾的是没有Repeat
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this, "progress", 0f, progress);
        progressAnimation.setDuration(700);// 动画执行时间

		/*
		 * AccelerateInterpolator　　　　　 					加速，开始时慢中间加速
		 * DecelerateInterpolator　　　 　　					减速，开始时快然后减速
		 * AccelerateDecelerateInterolator　 					先加速后减速，开始结束时慢，中间加速
		 * AnticipateInterpolator　　　　　　	 				反向 ，先向相反方向改变一段再加速播放
		 * AnticipateOvershootInterpolator　					反向加超越，先向相反方向改变，再加速播放，会超出目的值然后缓慢移动至目的值
		 * BounceInterpolator　　　　　　　						跳跃，快到目的值时值会跳跃，如目的值100，后面的值可能依次为85，77，70，80，90，100
		 * CycleIinterpolator　　　　　　　　 					循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2 *
		 * mCycles * Math.PI * input) LinearInterpolator　　　 线性，线性均匀改变
		 * OvershottInterpolator　　　　　　 					超越，最后超出目的值然后缓慢改变到目的值
		 * TimeInterpolator　　　　　　　　　 						一个接口，允许你自定义interpolator，以上几个都是实现了这个接口
		 */
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.playTogether(progressAnimation);//动画同时执行,可以做多个动画
        animation.start();
    }
}