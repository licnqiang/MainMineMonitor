package cn.piesat.minemonitor.mapdata.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.piesat.minemonitor.R;


/**
 * Created by Administrator on 2017/5/25.
 */

public class PointShapeView extends View {

    private Paint mPaint;
    private int mSolidColor;//填充色;
    private int mStrokeColor;//边框颜色;
    private float mStrokeWidth;//边框宽度;
    private boolean isDrawStroke;//是否绘制边框;
    private PointShape mPointShape;

    private static final PointShape[] mPointShapes = {PointShape.TRIANGLE, PointShape.SQUARE, PointShape.ROUND};

    Path mPath;

    public enum PointShape {
        TRIANGLE(0), SQUARE(1), ROUND(2), LINE(3);

        PointShape(int ni) {
            nativeInt = ni;
        }

        final int nativeInt;
    }

    public PointShapeView(Context context) {
        this(context, null);
    }

    public PointShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
    }

    public PointShape getmPointShape() {
        return mPointShape;
    }

    public void setmPointShape(PointShape mPointShape) {
        this.mPointShape = mPointShape;
        invalidate();
    }

    public int getmSolidColor() {
        return mSolidColor;
    }

    public void setmSolidColor(int mSolidColor) {
        this.mSolidColor = mSolidColor;
        invalidate();
    }

    public int getmStrokeColor() {
        return mStrokeColor;
    }

    public void setmStrokeColor(int mStrokeColor) {
        this.mStrokeColor = mStrokeColor;
        invalidate();
    }

    public boolean isDrawStroke() {
        return isDrawStroke;
    }

    public void setDrawStroke(boolean drawStroke) {
        isDrawStroke = drawStroke;
        invalidate();
    }

    private void initAttrs(AttributeSet attrs, int defStyleAttr) {
        mPointShape = PointShape.TRIANGLE;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PointShapeView, defStyleAttr, 0);
        mSolidColor = typedArray.getColor(R.styleable.PointShapeView_solid_color, getContext().getResources().getColor(R.color.colorAccent));
        mStrokeColor = typedArray.getColor(R.styleable.PointShapeView_stroke_color, getContext().getResources().getColor(R.color.colorAccent));
        int index = typedArray.getInt(R.styleable.PointShapeView_point_shape, 2);
        mPointShape = mPointShapes[index];
        typedArray.recycle();

        mStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getContext().getResources().getDisplayMetrics());
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        isDrawStroke = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPointShape == PointShape.TRIANGLE) {//画三角形
            drawTriangle(canvas, mPaint, mPath);
        } else if (mPointShape == PointShape.ROUND) {//画圆形
            drawRound(canvas, mPaint);
        } else if (mPointShape == PointShape.SQUARE) {//画正方形
            drawSquare(canvas, mPaint, mPath);
        } else if (mPointShape == PointShape.LINE) {//画线
            drawLine(canvas, mPaint);
        }
    }

    /**
     * 画正方形
     *
     * @param canvas
     * @param paint
     * @param path
     */
    private void drawSquare(Canvas canvas, Paint paint, Path path) {
        paint.setColor(mSolidColor);
        paint.setStyle(Paint.Style.FILL);
        path.reset();
        path.moveTo(getPaddingLeft(), getPaddingTop());
        path.lineTo(getPaddingLeft(), getMeasuredHeight() - getPaddingBottom());
        path.lineTo(getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        path.lineTo(getMeasuredWidth() - getPaddingRight(), getPaddingTop());
        path.close();
        canvas.drawPath(path, paint);
        if (isDrawStroke) {
            paint.setColor(mStrokeColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(mStrokeWidth);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 画圆形
     *
     * @param canvas
     * @param paint
     */
    private void drawRound(Canvas canvas, Paint paint) {
        paint.setColor(mSolidColor);
        paint.setStyle(Paint.Style.FILL);
        float radius = getMeasuredWidth() / 2 - getPaddingRight();
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint);
        if (isDrawStroke) {
            paint.setColor(mStrokeColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint);
        }
    }

    /**
     * 画三角形
     *
     * @param canvas
     * @param paint
     * @param path
     */
    private void drawTriangle(Canvas canvas, Paint paint, Path path) {
        paint.setColor(mSolidColor);
        paint.setStyle(Paint.Style.FILL);
        path.reset();
        path.moveTo(getMeasuredWidth() / 2, getPaddingTop());
        path.lineTo(getPaddingLeft(), getMeasuredHeight() - getPaddingBottom());
        path.lineTo(getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
        path.close();
        canvas.drawPath(path, paint);
        if (isDrawStroke) {
            paint.setColor(mStrokeColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(mStrokeWidth);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 画线
     * @param canvas
     * @param paint
     */
    private void drawLine(Canvas canvas, Paint paint) {
        paint.setColor(mSolidColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        canvas.drawLine(getPaddingLeft(), getPaddingTop() + getMeasuredHeight() / 2, getMeasuredWidth() - getPaddingRight(), getPaddingTop() + getMeasuredHeight() / 2, paint);
        if (isDrawStroke) {
            paint.setColor(mStrokeColor);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(getPaddingLeft(), getPaddingTop() + getMeasuredHeight() / 2, getMeasuredWidth() - getPaddingRight(), getPaddingTop() + getMeasuredHeight() / 2, paint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) + getPaddingLeft() + getPaddingRight();

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom();


        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);
    }
}
