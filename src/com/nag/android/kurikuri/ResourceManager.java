package com.nag.android.kurikuri;

import android.widget.TextView;
import com.nag.android.util.FlipView;

interface ResourceManager{
	Board getBoard();
	Properties getProperties();
	AppPlayer getPlayer();
	FlipView getSignBoard();
	TextView getScoreBoard();
	Score getScore();
}
