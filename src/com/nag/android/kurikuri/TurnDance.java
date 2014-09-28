package com.nag.android.kurikuri;

import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class TurnDance implements Dance {

	@Override
	public void dance(ImageView[] images) {
		AnimationSet set=null;
		for(int i=0; i<images.length; ++i){
			set=new AnimationSet(true);
			set.addAnimation(new AlphaAnimation(0.0f, 1.0f));
			set.addAnimation(new RotateAnimation(360.0f, 0.0f));
			set.setDuration(100*i);
			images[i].setAnimation(set);
			set.start();
		}
	}
}
