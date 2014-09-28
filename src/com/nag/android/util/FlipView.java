package com.nag.android.util;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FlipView extends ImageView {

	private final int INTERVAL_DEFAULT = 500;
	private final String ATTR_IMAGES = "images";
	private final String ATTR_INTERVAL = "interval";
	private int[] images = null;
	private int current=0;
	private Timer timer = null;
	private Handler handler = new Handler();

	public FlipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (attrs!=null){
			if(!isInEditMode()){
				int id = attrs.getAttributeResourceValue(null, ATTR_IMAGES, 0);
				if(id!=0){// TODO
					TypedArray array = getResources().obtainTypedArray(id);
					images = new int[array.length()];
					for(int i=0; i<array.length(); ++i){
						images[i] = array.getResourceId(i, 0);
					}
					array.recycle();
				}
				start(attrs.getAttributeIntValue(null, ATTR_INTERVAL, INTERVAL_DEFAULT));
			}
		}
	}

	public void setImageResources(int id){
		TypedArray array = getResources().obtainTypedArray(id);
		images = new int[array.length()];
		for(int i=0; i<array.length(); ++i){
			images[i] = array.getResourceId(i, 0);
		}
		array.recycle();
	}

	private void start(int period){
		current=0;
		setImageResource(images[current]);
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				if (++current>=images.length) {
					current = 0;
				}
				handler.post(new Runnable(){
					@Override
					public void run() {
						invalidate();
					}});
			}}, 0, period);
	}
	
	public void stop(){
		timer.cancel();
		timer = null;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(images!=null){
			setImageResource(images[current]);
		}
	}
}
