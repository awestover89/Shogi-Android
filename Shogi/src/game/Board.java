package game;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pieces.Bishop;
import pieces.GoldGeneral;
import pieces.King;
import pieces.Knight;
import pieces.Lance;
import pieces.Pawn;
import pieces.Piece;
import pieces.Rook;
import pieces.SilverGeneral;

import com.Shogi.Event.BoardEvent;
import com.Shogi.Event.BoardEventListener;

public class Board {
	
	private Cell[][] spaces;
	private final int SIZE = 9;
	private int turn, handIndex, placeX, placeY;
	private Hand hands[];
	Piece lastPieceMoved;
	private List<BoardEventListener> _listeners = new ArrayList<BoardEventListener>();

	public Board(){
		spaces = new Cell[SIZE][SIZE];
		turn = -1;
		handIndex = -1;
		placeX = -1;
		placeY = -1;
		hands = new Hand[2];
		hands[0] = new Hand(-1);
		hands[1] = new Hand(1);
		for(int i = 0;i<SIZE;i++)
			for(int j=0;j<SIZE;j++)
				spaces[i][j] = new Cell();
	}
	
	public synchronized void addBoardEventListener(BoardEventListener listener)  {
		_listeners.add(listener);
	}
	  
	public synchronized void removeBoardEventListener(BoardEventListener listener)   {
		_listeners.remove(listener);
	}
	
	private synchronized void fireEvent(int id){
		BoardEvent event = new BoardEvent(this);
		event.setId(id);
		Iterator<BoardEventListener> i = _listeners.iterator();
		while(i.hasNext())  {
			i.next().onBoardEvent(event);
			//Log.v("MyActivity", "Cap fired");
		}
	}
	
	public int getTurn(){
		return turn;
	}
	
	public void endTurn(){
		turn*=-1;
		fireEvent(BoardEvent.TURN_END);
	}
	
	public void defaultFill(){
		spaces[0][4].setOccupant(new King(0,4));
		spaces[8][4].setOccupant(new King(8,4));
		spaces[0][3].setOccupant(new GoldGeneral(0,3));
		spaces[0][5].setOccupant(new GoldGeneral(0,5));
		spaces[8][3].setOccupant(new GoldGeneral(8,3));
		spaces[8][5].setOccupant(new GoldGeneral(8,5));
		spaces[0][2].setOccupant(new SilverGeneral(0,2));
		spaces[0][6].setOccupant(new SilverGeneral(0,6));
		spaces[8][2].setOccupant(new SilverGeneral(8,2));
		spaces[8][6].setOccupant(new SilverGeneral(8,6));
		spaces[0][1].setOccupant(new Knight(0,1));
		spaces[0][7].setOccupant(new Knight(0,7));
		spaces[8][1].setOccupant(new Knight(8,1));
		spaces[8][7].setOccupant(new Knight(8,7));
		spaces[0][0].setOccupant(new Lance(0,0));
		spaces[0][8].setOccupant(new Lance(0,8));
		spaces[8][0].setOccupant(new Lance(8,0));
		spaces[8][8].setOccupant(new Lance(8,8));
		spaces[1][1].setOccupant(new Bishop(1,1));
		spaces[7][7].setOccupant(new Bishop(7,7));
		spaces[7][1].setOccupant(new Rook(7,1));
		spaces[1][7].setOccupant(new Rook(1,7));
		for(int i=0;i<9;i++){
			spaces[2][i].setOccupant(new Pawn(2,i));
			spaces[6][i].setOccupant(new Pawn(6,i));
		}
	}
	
	public Cell getCell(int x, int y){
		return spaces[x][y];
	}
	
	public Board deepCopy(){
		Board temp = new Board();
		for(int i=0;i<SIZE;i++){
			for(int j=0;j<SIZE;j++){
				Cell cellToCopy = getCell(i,j);
				temp.getCell(i,j).populate(cellToCopy);
			}
		}
		return temp;
	}
	
	public boolean pathIsClear(Piece p, int x, int y){
		int[][] path = p.getPath(x,y);
		if(path.length == 0)
			return false;
		for(int i=0;i<path.length-1;i++){
			if(getCell(path[i][0],path[i][1]).isOccupied()){
				return false;
			}
		}
		if(getCell(path[path.length-1][0],path[path.length-1][1]).isOccupied(p.getDir())){
			return false;
		}
		return true;
	}

	public boolean move(int oldCol, int oldRow, int newCol, int newRow) {
		if(oldCol <0 || oldCol > 8 || oldRow <0 || oldRow > 8 || newCol <0 || newCol > 8 || newRow <0 || newRow > 8)
			return false;
		Cell old = getCell(oldRow, oldCol);
		Cell dest = getCell(newRow, newCol);
		Piece p = old.getOccupant();
		if(p.moveIsLegal(newRow, newCol)){
			if(pathIsClear(p, newRow, newCol)){
				lastPieceMoved = p;
				p.move(newRow, newCol);
				old.removeOccupant();
				if(dest.isOccupied(turn*-1))
					capture(dest.getOccupant(), turn);
				dest.setOccupant(p);
				if(!p.isPromoted()){
				if((p.getDir() == -1 && p.getX() < 3) || (p.getDir() == 1 && p.getX() > 5)){
					if(p.shouldPromote())
						promote(true);
					else
						fireEvent(BoardEvent.PROMO);
				}
				else
					endTurn();
				}
				else
					endTurn();
				return true;
			}
		}
		return false;
	}
	
	public void promote(boolean b){
		if(b){
			Cell cell = getCell(lastPieceMoved.getX(), lastPieceMoved.getY());
			cell.removeOccupant();
			cell.setOccupant(lastPieceMoved.flip());
		}
		endTurn();
	}
	
	public boolean place(int handIndex, int x, int y){
		Cell toPlace = getCell(x,y);
		if(toPlace.isOccupied())
			return false;
		if(turn == -1)
			toPlace.setOccupant(hands[0].playPiece(handIndex));
		else
			toPlace.setOccupant(hands[1].playPiece(handIndex));
		toPlace.getOccupant().setPosition(x,y);
		fireEvent(BoardEvent.DROP);
		endTurn();
		return true;
	}

	private void capture(Piece piece, int player) {
		piece.setDir(player);
		Piece p = piece;
		if(piece.isPromoted())
			p = piece.flip();
		if(player == -1)
			hands[0].addToHand(p);
		else
			hands[1].addToHand(p);
		//Log.v("MyActivity", "Capture init");
		fireEvent(BoardEvent.CAPTURE);
	}
	
	public void selectedFromHand(int id){
		handIndex = id;
		if(handIndex != -1 && placeX != -1 && placeY != -1)
			place(handIndex, placeX, placeY);
	}
	
	public void setBoardSelected(int x, int y){
		placeX = x;
		placeY = y;
		if(handIndex != -1 && placeX != -1 && placeY != -1)
			place(handIndex, placeX, placeY);
	}
	
	public Hand[] getHands(){
		return hands;
	}
}
