package cn.piesat.minemonitor.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.piesat.minemonitor.R;


/**
 * 顶部indicator
 *
 *
 */
public class TopIndicatorView extends LinearLayout {

	@SuppressWarnings("unused")
	private static final String TAG = "TopIndicator";

	private List<CheckedTextView> mCtvList;
	/** 顶部菜单的文字数组 */
	private String[] mLabels;
	private int mScreenWidth;
	private int mUnderLineWidth;
	private View mUnderLine;
	// 底部线条移动初始位置
	private int mUnderLineFromX = 0;
	private Context m_context;

	@SuppressLint("NewApi") public TopIndicatorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_context = context;
		// init(context);
	}
	/**
	 * 在布局中定义，用findViewById生成的时候执行此构造方法
	 *
	 * @param context
	 * @param attrs
	 * @param
	 */
	public TopIndicatorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopIndicatorView(Context context) {
		super(context);
		// init(context);
	}
	public void setLabels(String[] arr) {
		mLabels = arr;
		init();
	}
	public void setLabels(int arrayResId) {
		mLabels = m_context.getResources().getStringArray(arrayResId);
		init();
	}
	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		mCtvList = new ArrayList<CheckedTextView>();
		// D
		setBackgroundColor(Color.WHITE);
		mScreenWidth = m_context.getResources().getDisplayMetrics().widthPixels;
		// 计算线的长度
		mUnderLineWidth = mScreenWidth / mLabels.length;

		mUnderLine = new View(m_context);
		mUnderLine.setBackgroundColor(Color.rgb(6, 140, 239));
		mUnderLine.setBackgroundResource(R.color.bright_blue);
		// 线的Layout
		LayoutParams underLineParams = new LayoutParams(
				mUnderLineWidth, 4);
		underLineParams.gravity = Gravity.BOTTOM;

		// 标题文字layout
		LinearLayout labelLayout = new LinearLayout(m_context);
		LayoutParams labelLayoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		labelLayout.setOrientation(LinearLayout.HORIZONTAL);
		//
		LayoutParams linLabelParam = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		linLabelParam.weight = 1.0f;
		linLabelParam.gravity = Gravity.CENTER;
		// 添加选项文字
		addView(labelLayout, labelLayoutParams);
		addView(mUnderLine, underLineParams);// 添加下划线
		int len = mLabels.length;
		LayoutInflater inflater = LayoutInflater.from(m_context);
		for (int i = 0; i < len; i++) {
			final int index = i;
			View linLabelView = inflater.inflate(R.layout.top_indicator_item,
					null);
			CheckedTextView checkedTextView = (CheckedTextView) linLabelView
					.findViewById(R.id.ctv_indicator);
			checkedTextView.setText(mLabels[i]);
			checkedTextView.setTextSize(15);
			// checkedTextView.setTextSize(15);
			// checkedTextView.setGravity(Gravity.CENTER|Gravity.BOTTOM);
			labelLayout.addView(linLabelView, linLabelParam);
			checkedTextView.setTag(index);

			mCtvList.add(checkedTextView);

			linLabelView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (null != mTabListener) {
						mTabListener.onIndicatorSelected(index);
						setTabsDisplay(m_context, index);
					}
				}
			});
			// // 初始化 底部菜单选中状态,默认第一个选中
			if (i == 0) {
				checkedTextView.setChecked(true);
				// checkedTextView.setTextColor(Color.rgb(255, 255, 255));
				// checkedTextView.setBackgroundResource(R.drawable.bg_ctv_left_seletor);
			} else {
				checkedTextView.setChecked(false);
				// checkedTextView.setTextColor(Color.rgb(19, 12, 14));
				// checkedTextView.setBackgroundResource(R.drawable.bg_ctv_right_seletor);
			}
		}

		setTabsDisplay(m_context, 0);
	}

	/**
	 * 设置底部导航中图片显示状态和字体颜色
	 */
	public void setTabsDisplay(Context context, int checkedIndex) {
		int size = mCtvList.size();
		for (int i = 0; i < size; i++) {
			CheckedTextView checkedTextView = mCtvList.get(i);
			if ((Integer) (checkedTextView.getTag()) == checkedIndex) {
				checkedTextView.setChecked(true);
				checkedTextView.setTextColor(getResources().getColor(R.color.bright_blue));//选中
//				 checkedTextView.setBackgroundResource(R.drawable.bg_shape_oval_pressed);
//				 checkedTextView.setTextColor(context.getResources().getColor(R.color.theme_color));//new
				// 选中
			} else {
				checkedTextView.setChecked(false);
				checkedTextView.setTextColor(Color.rgb(0, 0, 0));//未选中
//				checkedTextView.setTextColor(context.getResources().getColor(R.color.theme_color1));//未选中
				// checkedTextView.setBackgroundResource(R.drawable.bg_shape_oval_normal);
				// checkedTextView.setTextColor(context.getResources().getColor(R.color.theme_color));//new
				// 未选中
			}
		}
		invalidate();
		// 下划线动画
		setUnderLineAnimation(checkedIndex);
	}

	private void setUnderLineAnimation(int index) {
		TranslateAnimation animation = new TranslateAnimation(mUnderLineFromX,
				index * mUnderLineWidth, 0, 0);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(150);
		mUnderLine.startAnimation(animation);
		mUnderLineFromX = index * mUnderLineWidth;
	}

	// 回调接口
	private OnTopIndicatorListener mTabListener;

	public interface OnTopIndicatorListener {

		void onIndicatorSelected(int index);
	}

	public void setOnTopIndicatorListener(OnTopIndicatorListener listener) {
		this.mTabListener = listener;
	}

}
