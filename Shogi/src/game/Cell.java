package game;

import pieces.Piece;

public class Cell {
	
	private Piece occupant;

	public Cell(){
		occupant = null;
	}
	
	public boolean isOccupied(){
		return occupant != null;
	}
	
	public boolean isOccupied(int side){
		if(isOccupied())
			if(side == occupant.getDir())
				return true;
		return false;
	}

	public void populate(Cell cellToCopy) {
		if(cellToCopy.isOccupied()){
			Piece p = cellToCopy.getOccupant().deepCopy();
			setOccupant(p);
		}
	}
	
	public Piece getOccupant(){
		return occupant;
	}
	
	public void setOccupant(Piece p){
		occupant = p;
	}
	
	public void removeOccupant(){
		occupant = null;
	}
	
}
