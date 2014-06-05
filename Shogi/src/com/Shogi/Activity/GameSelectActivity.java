package com.Shogi.Activity;

import com.Shogi.Activity.R;

import android.app.Activity;
import android.os.Bundle;

public class GameSelectActivity extends Activity{
	
	final String TAG = "MyActivity";
	
	public void onCreate(Bundle bundle){
		//Log.v(TAG, "In second Activity");
		super.onCreate(bundle);
		setContentView(R.layout.gameselect);
		
	}

}
