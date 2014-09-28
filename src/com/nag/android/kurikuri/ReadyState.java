package com.nag.android.kurikuri;

import android.view.View;

import com.nag.android.kurikri.R;

class ReadyState implements State{
	private final ResourceManager rm;
	public ReadyState(ResourceManager rm){
		this.rm = rm;
		init();
	}

	private void init(){
		rm.getSignBoard().setImageResources(R.array.flip_images_ready);
		rm.getSignBoard().setVisibility(View.VISIBLE);
		rm.getScoreBoard().setVisibility(View.GONE);
		rm.getBoard().reset();
	}

	@Override
	public State actionDown(float sx, float sy) {
		return this;
	}

	@Override
	public void actionMove(float sx, float sy) {
	}

	@Override
	public State actionUp(float sx, float sy) {
		return new GameState(rm);
	}
	
}
