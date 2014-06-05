package com.Shogi.View;

import game.Board;
import pieces.Piece;
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

public class ShogiBoardView extends View{
	
	private float cellWidth, cellHeight;
	private int selectedRow, selectedCol;
	private Paint cellBG, cellSelectedBG, boardLine;
	Board game;

	public ShogiBoardView(Context context) {
		super(context, null);
		selectedRow = -1;
		selectedCol = -1;
		setPaints();
		game = new Board();
		game.defaultFill();
	}
	
	public ShogiBoardView(Context context, AttributeSet attrs){
		super(context,attrs);
		setPaints();
		selectedRow = -1;
		selectedCol = -1;
		game = new Board();
		game.defaultFill();
	}
	
	public void setBoard(Board b){
		game = b;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		super.onTouchEvent(event);
		//Log.v("MyActivity", "Touch Event");
		float x = event.getX();
		float y = event.getY();
		switch(event.getAction()) {
		case(MotionEvent.ACTION_UP):
			select(x,y);
		}	
		return true;
	}
	
	public void select(float x, float y){
			if(selectedCol == (int) (x/cellWidth) && selectedRow == (int) (y/cellHeight)){
				deselect();
			}
			else{
				if(selectedCol == -1 || selectedRow == -1){
					selectedCol = (int) (x/cellWidth);
					selectedRow = (int) (y/cellHeight);
					game.setBoardSelected(selectedRow, selectedCol);
					if(!game.getCell(selectedRow, selectedCol).isOccupied(game.getTurn())){
						deselect();
					}
				}
				else{
					if(game.move(selectedCol, selectedRow, (int) (x/cellWidth), (int) (y/cellHeight)))
						deselect();
				}
			}
			invalidate();
	}

	private void deselect() {
		selectedCol = -1;
		selectedRow = -1;
		game.setBoardSelected(selectedRow, selectedCol);
	}
	
	public void setPaints(){
		cellBG = new Paint();
		cellSelectedBG = new Paint();
		boardLine = new Paint();
		cellBG.setColor(Color.rgb(113, 26, 0));
		cellSelectedBG.setColor(Color.rgb(205, 102, 0));
		boardLine.setColor(Color.BLACK);
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = -1, height = -1;
        
        final int SIZE = Math.min(widthSize, heightSize);
        width = SIZE;
        height = SIZE;
        
        cellWidth = (width - getPaddingLeft() - getPaddingRight()) / 9.0f;
        cellHeight = (height - getPaddingTop() - getPaddingBottom()) / 9.0f;
        
        setMeasuredDimension(width, height);
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		int width = getWidth() - getPaddingRight();
        int height = getHeight() - getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        
        for(int row=0;row<9;row++){
        	for(int col=0;col<9;col++){
        		int cellLeft = Math.round((col * cellWidth) + paddingLeft);
                int cellTop = Math.round((row * cellHeight) + paddingTop);
                //Log.v("MyActivity", row + " " + col + " " + selectedRow + " " + selectedCol);
                if(row == selectedRow && col == selectedCol)
                	canvas.drawRect(cellLeft, cellTop, cellLeft+cellWidth, cellTop+cellHeight, cellSelectedBG);
                else
                	canvas.drawRect(cellLeft, cellTop, cellLeft+cellWidth, cellTop+cellHeight, cellBG);
                if(game.getCell(row, col).isOccupied()){
                	Rect dest = new Rect(cellLeft, cellTop, (int) (cellLeft+cellWidth), (int) (cellTop+cellHeight));
                	canvas.drawBitmap(drawPiece(row, col), null, dest, boardLine);
                }
        	}
        }
        // draw vertical lines
        for (int c=0; c <= 9; c++) {
                float x = (c * cellWidth) + paddingLeft;
                canvas.drawLine(x, paddingTop, x, height, boardLine);
        } 
        // draw horizontal lines
        for (int r=0; r <= 9; r++) {
                float y = r * cellHeight + paddingTop;
                canvas.drawLine(paddingLeft, y, width, y, boardLine);
        }
	}
	
	public Board getBoard(){
		return game;
	}
	
	public void boardChanged(){
		invalidate();
	}
	
	public Bitmap drawPiece(int row, int col){
		Piece p = game.getCell(row, col).getOccupant();
		BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(p.getId(false));
		Bitmap piece = bd.getBitmap();
		if(p.getDir()==1){
			Bitmap temp = piece;
			Matrix m = new Matrix();
			m.postRotate(180);
			piece = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, true);
		}
		return piece;
	}

}
