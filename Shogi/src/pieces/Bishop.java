package pieces;

import java.util.LinkedList;
import com.Shogi.Activity.R;

public class Bishop implements Piece {
	
	private int curX, curY, dir;
	public final boolean PROMOTED = false;
	
	public Bishop(int startX, int startY){
		curX = startX;
		curY = startY;
		if(curX < 3)
			dir = 1;
		else
			dir = -1;
	}
	
	public Bishop(Piece b){
		curX = b.getX();
		curY = b.getY();
		dir = b.getDir();
	}

	@Override
	public String getEnglishName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJapaneseName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Integer>[][] legalMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void move(int newX, int newY) {
		if(moveIsLegal(newX,newY)){
			curX = newX;
			curY = newY;
		}
	}

	@Override
	public boolean moveIsLegal(int newX, int newY) {
		if(newX < 0 || newX > 8 || newY < 0 || newY > 8)
			return false;
		
		// A bishop can move any number of free squares 
		// along any one of the four diagonal directions.
		
		// Movement along diagonal means the difference
		// in X is equal to the difference in Y
		
		if(Math.abs(curX - newX) == Math.abs(curY - newY))
			return true;
		
		return false;
	}

	@Override
	public int getX() {
		return curX;
	}

	@Override
	public int getY() {
		return curY;
	}

	@Override
	public int[][] getPath(int x, int y) {
		if(!moveIsLegal(x,y))
			return new int[0][0];
		int numMoves = Math.max(Math.abs(x-curX), Math.abs(y-curY));
		int[][] path = new int[numMoves][2];
		for(int i=0;i<numMoves;i++){
			if(x-curX > 0)
				path[i][0] = (i+1)+curX;
			else if(x-curX < 0)
				path[i][0] = -1*(i+1)+curX;
			else
				path[i][0] = curX;
			if(y-curY > 0)
				path[i][1] = (i+1)+curY;
			else if(y-curY < 0)
				path[i][1] = -1*(i+1)+curY;
			else
				path[i][1] = curY;
		}
		
		return path;
	}

	@Override
	public int getDir() {
		return dir;
	}

	@Override
	public Piece deepCopy() {
		return new Bishop(this);
	}

	@Override
	public void setDir(int dir) {
		this.dir = dir;
	}

	@Override
	public int getId(boolean japanese) {
		if(japanese)
			return R.drawable.bishopjp;
		return R.drawable.bishopeng;
	}

	@Override
	public Piece flip() {
		return new Horse(this);
	}

	@Override
	public boolean isPromoted() {
		return PROMOTED;
	}

	@Override
	public boolean shouldPromote() {
		return false;
	}

	@Override
	public void setPosition(int x, int y) {
		curX = x;
		curY = y;
	}

}
