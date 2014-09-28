package com.nag.android.kurikuri;

import com.nag.android.kurikri.R;
import com.nag.android.util.PreferenceHelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity{

	private WebView webview = null;
	public static String REQUESTED_URL = "requestedurl";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		String url = getIntent().getStringExtra(REQUESTED_URL);
		if(url!=null){
			webview = (WebView)findViewById(R.id.webView1);
			webview.loadUrl(url);
		}
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.equals("nagapp://close_window")){
					finish();
					return true;
				}else if (url.startsWith("nagapp://close_window_do_not_ask_again")){
					PreferenceHelper.getInstance(WebViewActivity.this).putBoolean(MainActivity.INTRODUCTION_DO_NOT_ASK_AGAIN, true);
					finish();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK &&  webview.canGoBack() ) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
