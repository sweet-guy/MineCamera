package com.wdcloud.minecamera;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Info:
 * Created by Umbrella.
 * CreateTime: 2019/10/9 18:24
 */
public class NiceSurfaceView extends SurfaceView {
    private Bitmap mEraserBitmap;
    private Canvas mEraserCanvas;
    private Paint mEraser;
    private float mDensity;
    private Context mContext;
    private float mRadius;
    private int mBackgroundColor;
    private float mRx;
    //默认在中心位置
    private float mRy;
    public NiceSurfaceView(Context context) {
        super(context);
        mContext = context;
    }

    public NiceSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NiceSurfaceView);
        mBackgroundColor = ta.getColor(R.styleable.NiceSurfaceView_background_color, -1);
        mRadius = ta.getFloat(R.styleable.NiceSurfaceView_hole_radius, 0);
        mRx = ta.getFloat(R.styleable.NiceSurfaceView_radius_x, 0);
        mRy = ta.getFloat(R.styleable.NiceSurfaceView_radius_y, 0);
        init(null, 0);
        ta.recycle();
    }

    public NiceSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mEraserBitmap.eraseColor(Color.TRANSPARENT);
        mEraserCanvas.drawColor(mBackgroundColor);
        mEraserCanvas.drawCircle(mRx, mRy, mRadius, mEraser);
        canvas.drawBitmap(mEraserBitmap, 0, 0, null);
    }
    private void init(AttributeSet attrs, int defStyle) {
        setWillNotDraw(false);
        mDensity = mContext.getResources().getDisplayMetrics().density;
        Point size = new Point();
        size.x = mContext.getResources().getDisplayMetrics().widthPixels;
        size.y = mContext.getResources().getDisplayMetrics().heightPixels;
        mRx = mRx * mDensity;
        mRy = mRy * mDensity;
        mRx = mRx != 0 ? mRx : size.x/2;
        mRy = mRy != 0 ? mRy : size.y/2;
        mRadius = mRadius != 0 ? mRadius : 150;
        mRadius = mRadius * mDensity;
        mBackgroundColor = mBackgroundColor != -1 ? mBackgroundColor : Color.parseColor("#B0E0E6");
        mEraserBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
        mEraserCanvas = new Canvas(mEraserBitmap);
        mEraser = new Paint();
        mEraser.setColor(0xFFFFFFFF);
        mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mEraser.setFlags(Paint.ANTI_ALIAS_FLAG);
    }
}
