package com.Shogi.Activity;

import com.Shogi.Activity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends Activity{
	
	@SuppressWarnings("unchecked")
	Class caller;
	final static String PREFS = "ShogiPreferences";
	//SharedPreferences settings;
	CheckBox moves, lang, bgmusic, checkWarn;
	
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.settings);
		Bundle extras = getIntent().getExtras();
		caller = (Class) extras.get("Caller");
		setDefaults();
	}
	
	private void setDefaults(){
		SharedPreferences settings = getSharedPreferences(PREFS,0);
		boolean state = settings.getBoolean("move", false);
		moves = (CheckBox) findViewById(R.id.legalmovescheck);
		moves.setChecked(state);
		state = settings.getBoolean("jap", true);
		lang = (CheckBox) findViewById(R.id.japanesenamescheck);
		lang.setChecked(state);
		state = settings.getBoolean("check", false);
		checkWarn = (CheckBox) findViewById(R.id.warncheckcheck);
		checkWarn.setChecked(state);
		state = settings.getBoolean("music", true);
		bgmusic = (CheckBox) findViewById(R.id.soundcheck);
		bgmusic.setChecked(state);
	}
	
	public void submit(View v){
		SharedPreferences settings = getSharedPreferences(PREFS,0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("move", moves.isChecked());
		editor.putBoolean("jap", lang.isChecked());
		editor.putBoolean("check", checkWarn.isChecked());
		editor.putBoolean("music", bgmusic.isChecked());
		editor.commit();
		Intent i = new Intent(v.getContext(),caller);
		startActivity(i);
	}

}
