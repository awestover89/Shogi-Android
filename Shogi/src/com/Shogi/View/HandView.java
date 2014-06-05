package com.Shogi.View;

import pieces.Piece;
import game.Board;
import game.Hand;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.Shogi.Event.BoardEvent;
import com.Shogi.Event.BoardEventListener;

public class HandView extends View implements BoardEventListener{
	
	Board b;
	int takenW, takenH, labelH, widthSize, heightSize, height, width, numCols;
	float pieceWidth, pieceHeight;
	Rect selection;
	int[][] cutoffs;
	int selected = -1;
	int player = 0;
	Paint selectedBG;

	public HandView(Context context) {
		super(context, null);
		selectedBG = new Paint();
		selectedBG.setColor(Color.rgb(205, 102, 0));
	}
	
	public HandView(Context context, AttributeSet attrs){
		super(context, attrs);
		selectedBG = new Paint();
		selectedBG.setColor(Color.rgb(205, 102, 0));
	}
	
	public void setBoard(Board b){
		this.b = b;
		b.addBoardEventListener(this);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		//Log.v("MyActivity", x+" "+y + " " + widthSize + " " + heightSize + " " + width + " " + height + " " + pieceWidth + " " + pieceHeight);
		switch(event.getAction()) {
		case(MotionEvent.ACTION_UP):
			for(int i=0;i<cutoffs.length;i++){
				if(x < cutoffs[i][0] && y < cutoffs[i][1]){
					//Log.v("MyActivity", "Selected "+ selected + " "+ i);
					if(selected == i)
						selected = -1;
					else
						selected = i;
					b.selectedFromHand(selected);
					invalidate();
					break;
				}	
			}
		}
		return true;
	}
	
	public void setTakenSize(int w, int h, int labelH){
		takenW = w;
		takenH = h;
		this.labelH = labelH;
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        height = 0;
        width = 0;
        //check if handview is right of the board
        if((heightSize-takenH) > (widthSize - takenW)){
        	height = heightSize - takenH - labelH;
        	width = widthSize;
            pieceWidth = width / 4.0f;
            pieceHeight = height / 5.0f;
            numCols = 4;
        }
        //or below it
        else{
        	height = heightSize - labelH;
        	width = widthSize - takenW;
        	pieceWidth = width / 6.0f;
            pieceHeight = height / 3.0f;
            numCols = 6;
        }
        
        
        setMeasuredDimension(width, height);
	}
	
	public void onDraw(Canvas canvas){
		Hand hand = b.getHands()[player];
		cutoffs = new int[hand.getNumPieces()][2];
		for(int i=0;i<hand.getNumPieces();i++){
			Piece p = hand.getPiece(i);
			BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(p.getId(false));
			int pieceLeft = (int) (widthSize - width + getPaddingLeft() + ((i%numCols)*pieceWidth));
			int pieceTop = (int) (heightSize - height + getPaddingTop() + ((int)(i/numCols)*pieceHeight));
			cutoffs[i][0] = (int) (pieceLeft + pieceWidth);
			cutoffs[i][1] = (int) (pieceTop+pieceHeight);
			Rect dest = new Rect(pieceLeft, pieceTop, (int) (pieceLeft + pieceWidth), (int) (pieceTop+pieceHeight));
			if(selected == i)
				canvas.drawRect(dest, selectedBG);
			Bitmap temp = bd.getBitmap();
			Bitmap piece = temp;
			if(p.getDir()==1){
				Matrix m = new Matrix();
				m.postRotate(180);
				piece = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, true);
			}
			canvas.drawBitmap(piece, null, dest, null);
		}
	}

	@Override
	public void onBoardEvent(BoardEvent e) {
		//Log.v("MyActivity", "Capture event receieved");
		if(e.getId() == BoardEvent.CAPTURE || e.getId() == BoardEvent.DROP){
			selected = -1;
			b.selectedFromHand(selected);
			invalidate();
		}
		else if(e.getId() == BoardEvent.TURN_END){
			player = player^1;
			invalidate();
		}
	}


}
