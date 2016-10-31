package com.example.admin.jufan.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CurstomViewPager extends ViewPager {

	public CurstomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CurstomViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}
}
