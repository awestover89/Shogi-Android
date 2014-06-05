package game;

import java.util.LinkedList;

import pieces.Piece;

public class Hand {
	
	int player;
	LinkedList<Piece> pieces;
	
	public Hand(int player){
		this.player = player;
		pieces = new LinkedList<Piece>();
	}
	
	public void addToHand(Piece p){
		pieces.add(p);
	}
	
	public Piece getPiece(int index){
		return pieces.get(index);
	}
	
	public int getNumPieces(){
		return pieces.size();
	}
	
	public Piece playPiece(int index){
		return pieces.remove(index);
	}

}
