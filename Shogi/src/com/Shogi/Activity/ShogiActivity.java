package com.Shogi.Activity;

import pieces.Dragon;
import pieces.Piece;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ShogiActivity extends Activity {
	
	final String TAG = "MyActivity";
	final static String PREFS = "ShogiPreferences";
	final int MENU_SETTINGS = 1;
	final int MENU_TUTORIAL = 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        test(new Dragon(4,4));
    }
   
    public void start(View target){
    	//Log.v(TAG,"Start");
    	//Intent myIntent = new Intent(target.getContext(),GameSelectActivity.class);
    	Intent myIntent = new Intent(target.getContext(),GameBoardActivity.class);
		startActivityForResult(myIntent,0);
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
    	menu.add(0, MENU_SETTINGS, 0, "Settings");
    	menu.add(0, MENU_TUTORIAL, 0, "Tutorial");
    	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_SETTINGS:
            Intent i = new Intent(this,SettingsActivity.class);
            i.putExtra("Caller", ShogiActivity.class);
            startActivity(i);
            return true;
        case MENU_TUTORIAL:
            
            return true;
        }
        return false;
    }

	private void test(Piece p) {
		int path[][] = p.getPath(6,6);
		for(int i=0;i<path.length;i++)
			Log.v(TAG, path[i][0]+" "+path[i][1]);
		path = p.getPath(2,2);
		for(int i=0;i<path.length;i++)
			Log.v(TAG,path[i][0]+" "+path[i][1]);
		path = p.getPath(6,2);
		for(int i=0;i<path.length;i++)
			Log.v(TAG,path[i][0]+" "+path[i][1]);
		path = p.getPath(2,6);
		for(int i=0;i<path.length;i++)
			Log.v(TAG,path[i][0]+" "+path[i][1]);
		path = p.getPath(2,4);
		for(int i=0;i<path.length;i++)
			Log.v(TAG,path[i][0]+" "+path[i][1]);
		path = p.getPath(6,4);
		for(int i=0;i<path.length;i++)
			Log.v(TAG,path[i][0]+" "+path[i][1]);
		path = p.getPath(4,6);
		for(int i=0;i<path.length;i++)
			Log.v(TAG,path[i][0]+" "+path[i][1]);
		path = p.getPath(4,2);
		for(int i=0;i<path.length;i++)
			Log.v(TAG,path[i][0]+" "+path[i][1]);
	}
}