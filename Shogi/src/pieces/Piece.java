package pieces;

import java.util.LinkedList;

public interface Piece {

	void move(int newX, int newY);
	LinkedList<Integer>[][] legalMoves();
	String getJapaneseName();
	String getEnglishName();
	boolean moveIsLegal(int newX,int newY);
	int getX();
	int getY();
	int[][] getPath(int x, int y);
	int getDir();
	int getId(boolean japanese);
	Piece deepCopy();
	Piece flip();
	void setDir(int dir);
	boolean isPromoted();
	boolean shouldPromote();
	void setPosition(int x, int y);
	
	//Bitmap getImage();  //from android SDK Bitmap class
	//void setImage(Bitmap img); 
}
