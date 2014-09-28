package com.nag.android.kurikuri;

interface State {
	State actionDown(float sx, float sy);
	void actionMove(float sx, float sy);
	State actionUp(float sx, float sy);
}