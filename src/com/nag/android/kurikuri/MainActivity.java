package com.nag.android.kurikuri;

import java.io.IOException;
import java.io.InputStream;

import com.nag.android.kurikri.R;
import com.nag.android.util.FlipView;
import com.nag.android.util.PreferenceHelper;

import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity implements OnTouchListener, ResourceManager{

	public static String INTRODUCTION_DO_NOT_ASK_AGAIN = "INTRODUCTION_DO_NOT_ASK_AGAIN";
	private static final int REQUEST_GALLERY = 1231;
	private static final int NUM_OF_ICON=6;
	private Board board = null;
	private AppPlayer player = null; 
	private Score score = null;
	private Properties prop;
	private State state;
	AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RelativeLayout view=(RelativeLayout)findViewById(R.id.RelativeLayoutMain);
		view.setOnTouchListener(this);
		adView = new AdView(this);
		adView.setAdUnitId(this.getResources().getString(R.string.admob_id));
		adView.setAdSize(AdSize.BANNER);
		FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
		adParams.gravity = (Gravity.BOTTOM|Gravity.CENTER);
		addContentView(adView, adParams);

		adView.loadAd(new AdRequest.Builder().build());
		findViewById(R.id.flipViewCongratulation).setVisibility(View.GONE);
		findViewById(R.id.textViewScore).setVisibility(View.GONE);
		prop = Properties.getInstance(this);
		score = new Score(this);
		player = new AppPlayer(this);
		board = new Board(this, player, NUM_OF_ICON);

		state = new ReadyState(this);

		showIntroduction();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
			try {
				InputStream in = getContentResolver().openInputStream(data.getData());
				RelativeLayout view=(RelativeLayout)findViewById(R.id.RelativeLayoutMain);
				board.load(view, BitmapFactory.decodeStream(in), getOrientation(data));
				in.close();
			} catch (Exception e) {
				// TODO
			}
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus){
		board.init((RelativeLayout)findViewById(R.id.RelativeLayoutMain));
	}

	@Override
	protected void onResume(){
		player.mute(prop.getMuteSound());
		super.onResume();
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float sx = event.getX();
		float sy = event.getY();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			state = state.actionDown(sx, sy);
			break;
		case MotionEvent.ACTION_MOVE:
			state.actionMove(sx, sy);
			break;
		case MotionEvent.ACTION_UP:
			state = state.actionUp(sx, sy);
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		v.performClick();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() )
		{
		case R.id.action_select_image:
			selectImage();
			state = new ReadyState(this);
			break;
		case R.id.action_reset:
//			board.shuffle(prop.getTaps4Shuffle(), prop.getDragLength4Shuffle());
			state = new ReadyState(this);
//			board.reset();
//			score.finish();
			break;
		case R.id.action_settings:
		{
			Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.action_help:
		{
			Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
			intent.putExtra(WebViewActivity.REQUESTED_URL, "file:///android_asset/jp/help.html");
			startActivity(intent);
			break;
		}
		default:
			assert( false );
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showIntroduction() {
		if(!PreferenceHelper.getInstance(this).getBoolean(INTRODUCTION_DO_NOT_ASK_AGAIN, false)){
			Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
			intent.putExtra(WebViewActivity.REQUESTED_URL, "file:///android_asset/jp/introduction.html");
			startActivity(intent);
		}
	}

	private int getOrientation(Intent intent){
		String[] columns = {MediaStore.Images.Media.DATA };  
		Cursor c = getContentResolver().query(intent.getData(), columns, null, null, null);  
		c.moveToFirst();  
		ExifInterface exifInterface;
		try {
			exifInterface = new ExifInterface(c.getString(0));
			String str=exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
			switch(Integer.parseInt(str)){
			default:
				assert(false);
			case 1:
				return 0;
			case 3:
				return 180;
			case 6:
				return 90;
			case 8:
				return 270;
			}
			
		} catch (IOException e) {
			return 0;
		}
	}

	private void selectImage(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_GALLERY);
	}

	@Override
	public Board getBoard() {
		return board;
	}

	@Override
	public AppPlayer getPlayer() {
		return player;
	}

	@Override
	public FlipView getSignBoard() {
		return (FlipView)findViewById(R.id.flipViewCongratulation);
	}

	@Override
	public TextView getScoreBoard() {
		return (TextView)findViewById(R.id.textViewScore);
	}

	@Override
	public Score getScore() {
		return score;
	}

	@Override
	public Properties getProperties() {
		return prop;
	}
}
