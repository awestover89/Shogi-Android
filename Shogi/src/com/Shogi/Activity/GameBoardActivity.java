package com.Shogi.Activity;

import game.Board;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.Shogi.Event.BoardEvent;
import com.Shogi.Event.BoardEventListener;
import com.Shogi.View.HandView;
import com.Shogi.View.ShogiBoardView;

public class GameBoardActivity extends Activity implements BoardEventListener{
	
	ShogiBoardView sbv;
	HandView hv;
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		this.setContentView(R.layout.board_view);
		sbv = (ShogiBoardView) this.findViewById(R.id.boardview);
		hv = (HandView) this.findViewById(R.id.handview);
		TextView tv = (TextView) this.findViewById(R.id.playerhandtext);
		if(b!=null){
			sbv.setBoard((Board) getLastNonConfigurationInstance());
		}
		sbv.getBoard().addBoardEventListener(this);
		hv.setBoard(sbv.getBoard());
		hv.setTakenSize(sbv.getWidth(), sbv.getHeight(), tv.getHeight());
	}
	
	@Override  
    public Object onRetainNonConfigurationInstance() 
    {   
        return(sbv.getBoard());   
    }

	@Override
	public void onBoardEvent(BoardEvent e) {
		if(e.getId() == BoardEvent.PROMO)
			showDialog(1);
	}
	
	protected Dialog onCreateDialog(int id){
		switch(id){
			case 1:
				 return new AlertDialog.Builder(this).setTitle("Would you like to promote this piece?")
				 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	sbv.getBoard().promote(true);
	                    	sbv.boardChanged();
	                    }
	                })
	             .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	sbv.getBoard().promote(false);
	                    }
	                })
	                .create();
	        }
	        return null; 
	}


}
