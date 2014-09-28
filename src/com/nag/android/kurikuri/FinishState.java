package com.nag.android.kurikuri;

import android.view.View;

import com.nag.android.kurikri.R;

public class FinishState implements State {
	private final ResourceManager rm;
	public FinishState(ResourceManager rm){
		this.rm = rm;
		init();
	}

	private void init() {
		rm.getPlayer().play(AppPlayer.CONTENTSID.CLEAR, 1.0f, 1.0f);
		rm.getSignBoard().setImageResources(R.array.flip_images_congraturation);
		rm.getScoreBoard().setText(rm.getScore().toString());
		rm.getSignBoard().setVisibility(View.VISIBLE);
		rm.getScoreBoard().setVisibility(View.VISIBLE);
	}

	@Override
	public State actionDown(float sx, float sy) {
		return new ReadyState(rm);
	}

	@Override
	public void actionMove(float sx, float sy) {
		// TODO Auto-generated method stub

	}

	@Override
	public State actionUp(float sx, float sy) {
		// TODO Auto-generated method stub
		return this;
	}

}
