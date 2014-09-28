package com.nag.android.kurikuri;

import android.content.Context;

import com.nag.android.util.PreferenceHelper;

class Properties {
	private final String PREF_DRAG_LENGTH_4_SHUFFLE = "pref_drag_length_4_shufle";
	private final String PREF_TAPS_4_SHUFFLE = "pref_taps_4_shufle";
	private final String MUTE_SOUND = "pref_mute_sound";
	private static Properties instance = null;
	static Properties getInstance(Context context){
		if(instance == null){
			instance = new Properties(context);
		}
		return instance;
	}

	private final PreferenceHelper pref;

	Properties(Context context){
		pref = PreferenceHelper.getInstance(context);
	}

	int getTaps4Shuffle(){
		return pref.getInt(PREF_TAPS_4_SHUFFLE, 1);
	}

	void putTaps4Shuffle(int value){
		pref.putInt(PREF_TAPS_4_SHUFFLE, value);
	}

	int getDragLength4Shuffle(){
		return pref.getInt(PREF_DRAG_LENGTH_4_SHUFFLE, 20);
	}

	void putDragLength4Shuffle(int value){
		pref.putInt(PREF_DRAG_LENGTH_4_SHUFFLE, value);
	}

	boolean getMuteSound(){
		return pref.getBoolean(MUTE_SOUND, false);
	}

	void putMuteSound(boolean value){
		pref.putBoolean(MUTE_SOUND, value);
	}
}
