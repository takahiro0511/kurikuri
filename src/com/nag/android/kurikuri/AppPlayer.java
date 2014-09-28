package com.nag.android.kurikuri;

import java.util.HashMap;
import java.util.Map;

import com.nag.android.kurikri.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class AppPlayer {
	private static final int NUM_OF_SOUND = CONTENTSID.values().length;
	private boolean mute = false;

	public enum CONTENTSID{
		KURI,
		CLEAR
	}
	private final SoundPool spool;
	private Map<CONTENTSID, Integer> contents = new HashMap<CONTENTSID, Integer>();
	public AppPlayer(Context context){
		spool = new SoundPool(NUM_OF_SOUND, AudioManager.STREAM_MUSIC, 0);
		contents.put(CONTENTSID.KURI, spool.load(context, R.raw.short01a, 1));
		contents.put(CONTENTSID.CLEAR, spool.load(context, R.raw.clear1, 1));
	}
	
	public void mute(boolean mute){
		this.mute = mute;
	}

	public void play(CONTENTSID id, float left, float right){
		if(!mute){
			spool.play(contents.get(id), left, right, 1, 0, 1f);
		}
	}
}
