package com.nag.android.kurikuri;

import com.nag.android.kurikri.R;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.app.Activity;

public class SettingsActivity extends Activity{

	private Properties properties = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingsview);
		properties = Properties.getInstance(this);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initNumberPicker((NumberPicker)findViewById(R.id.numberPickerDragLength4Shuffle),properties.getDragLength4Shuffle(), 2, 100);
		initNumberPicker((NumberPicker)findViewById(R.id.numberPickerTaps4Shuffle),properties.getTaps4Shuffle(), 1, 25);
		((CheckBox)findViewById(R.id.checkBoxMuteSound)).setChecked(properties.getMuteSound());
	}

	private void initNumberPicker(NumberPicker np, int value, int min, int max){
		np.setMinValue(min);
		np.setMaxValue(max);
		np.setWrapSelectorWheel(false);
		np.setValue(value);
	}

	@Override
	protected void onPause(){
		properties.putDragLength4Shuffle(((NumberPicker)findViewById(R.id.numberPickerDragLength4Shuffle)).getValue());
		properties.putTaps4Shuffle(((NumberPicker)findViewById(R.id.numberPickerTaps4Shuffle)).getValue());
		properties.putMuteSound(((CheckBox)findViewById(R.id.checkBoxMuteSound)).isChecked());
		super.onPause();
	}
}
