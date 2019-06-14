package cn.piesat.minemonitor.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import cn.piesat.minemonitor.R;


/**
 * Created by limxing on 16/1/7.
 */
public class ProgressLoadView extends ImageView {

	private float degrees = 0f;
	private Matrix max;
	private int width;
	private int height;
	private Bitmap bitmap;

	public ProgressLoadView(Context context) {
		super(context);
		init();
	}

	public ProgressLoadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProgressLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			degrees += 30f;
			max.setRotate(degrees, width, height);
			setImageMatrix(max);
			if (degrees == 360) {
				degrees = 0;
			}
		}
	};

	private void init() {
		setScaleType(ScaleType.MATRIX);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ajax_loader);
		setImageBitmap(bitmap);
//		setBackgroundColor(Color.TRANSPARENT);
		max = new Matrix();

		width = bitmap.getWidth() / 2;
		height = bitmap.getHeight() / 2;
		Timer time = new Timer();
		time.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 0, 80);
	}

}
