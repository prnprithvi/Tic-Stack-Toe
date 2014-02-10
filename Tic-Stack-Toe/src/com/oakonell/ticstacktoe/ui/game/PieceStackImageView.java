package com.oakonell.ticstacktoe.ui.game;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class PieceStackImageView extends ImageView {

	public PieceStackImageView(Context context) {
		super(context);
	}

	public PieceStackImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PieceStackImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int size = 0;
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
		int heigthWithoutPadding = height - getPaddingTop()
				- getPaddingBottom();

		// set the dimensions
		if (widthWithoutPadding > heigthWithoutPadding) {
			size = heigthWithoutPadding;
		} else {
			size = widthWithoutPadding;
		}

		setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size
				+ getPaddingTop() + getPaddingBottom());
		if (listener != null) {
			listener.onMeasureCalled(this, size, width, height);
		}
		Log.i("PieceStackImageView", "onMeasure, size=" + size);

	}

	public interface OnMeasureDependent {
		void onMeasureCalled(PieceStackImageView squareRelativeLayoutView,
				int size, int origWidth, int origHeight);

	}

	private OnMeasureDependent listener;

	public void setOnMeasureDependent(OnMeasureDependent listener) {
		this.listener = listener;
	}
}
