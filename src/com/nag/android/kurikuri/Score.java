package com.nag.android.kurikuri;

import com.nag.android.kurikri.R;

import android.content.Context;

class Score{
	private static final int NOT_STARTED_YET = -1;
	
	private final String label_shuffle_length;
	private final String label_shuffle_times;
	private final String label_result_taps;
	private final String label_result_time;
	private final String label_unit_second;
	private long start = NOT_STARTED_YET;
	private long time = NOT_STARTED_YET;

	private int shuffle_times;
	private int shuffle_length;
	private int taps;

	Score(Context context){
		this.label_shuffle_times = context.getString(R.string.label_score_shuffle_times);
		this.label_shuffle_length = context.getString(R.string.label_score_shuffle_length);
		this.label_result_taps = context.getString(R.string.label_score_result_taps);
		this.label_result_time = context.getString(R.string.label_score_result_time);
		this.label_unit_second = context.getString(R.string.label_score_unit_second);
	}
	public void countUp(){
		++taps;
	}

	public void start(int shuffle_times, int shuffle_length){
		if(start==-1){
			this.shuffle_times = shuffle_times;
			this.shuffle_length = shuffle_length;
			start = System.currentTimeMillis();
			time = 0;
		}
	}

	public void finish(){
		countUp();
		time = (System.currentTimeMillis()-start);
		start = NOT_STARTED_YET;
	}

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(label_shuffle_times);
		sb.append(shuffle_times);
		sb.append("\r\n");

		sb.append(label_shuffle_length);
		sb.append(shuffle_length);
		sb.append("\r\n");

		sb.append(label_result_taps);
		sb.append(taps);
		sb.append("\r\n");

		sb.append(label_result_time);
		sb.append(time/1000);
		sb.append(label_unit_second);

		return sb.toString();
	}
}