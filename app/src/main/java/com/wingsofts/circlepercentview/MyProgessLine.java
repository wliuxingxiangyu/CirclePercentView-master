package com.wingsofts.circlepercentview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by mi on 16-11-2.
 */
public class MyProgessLine  extends View {

    //需要执行动画的参数名
    private static final String PROGRESS_PROPERTY = "progress";

    private Paint paint;// 画笔

    RectF rectF;

    private int bmColor;// 底部横线颜色
    private float bmHight;// 底部横线高度
    private int color;// 进度条颜色
    private float hight;// 进度条高度

    protected float progress;

    public void setColor(int color) {
        this.color = color;
    }

    public MyProgessLine(Context context) {
        this(context, null);
    }

    public MyProgessLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgessLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        paint = new Paint();
        rectF = new RectF();

        TypedArray mTypedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.MyProgressLine, defStyleAttr, 0);

        bmColor = mTypedArray.getColor(R.styleable.MyProgressLine_myProgressLine_bmColor,
                Color.GRAY);
        bmHight = mTypedArray
                .getDimension(R.styleable.MyProgressLine_myProgressLine_bmHight, 2);
        color = mTypedArray.getColor(R.styleable.MyProgressLine_myProgressLine_color,
                Color.BLUE);
        hight = mTypedArray.getDimension(R.styleable.MyProgressLine_myProgressLine_hight, 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setColor(bmColor);
        paint.setStrokeCap(Paint.Cap.SQUARE);// 圆角
        // paint.setStyle(Paint.Style.FILL); // 设置实心
        paint.setStrokeWidth(bmHight); // 设置笔画的宽度
        paint.setAntiAlias(true); // 消除锯齿

        rectF.set(0, 0, getWidth(), bmHight);
        //canvas.drawRoundRect(rectF, bmHight / 2, bmHight / 2, paint);
        canvas.drawRect(0, 0, getWidth(), bmHight, paint);

        paint.setColor(color);
        paint.setStrokeWidth(hight); // 设置笔画的宽度

        rectF.set(0, 0, progress, bmHight);
        //矩形
//      canvas.drawRoundRect(rectF, hight / 2, hight / 2, paint);
        canvas.drawRect(0, 0, progress, bmHight, paint);

    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress * getWidth() / 100;
        invalidate();
    }

    /**
     * 赋值+执行动画
     *
     * @param progressText 进度 float
     */
    public void dodo(float progressText) {
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this, PROGRESS_PROPERTY,
                0f, progressText);
        progressAnimation.setDuration(1000);//动画耗时
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.playTogether(progressAnimation);
        animation.start();
    }

}
