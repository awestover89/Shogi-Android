package com.Shogi.Event;

import java.util.EventObject;

public class BoardEvent extends EventObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8454072615089648713L;
	
	public static final int CAPTURE = 1;
	public static final int PROMO = 2;
	public static final int DROP = 3;
	public static final int TURN_END = 4;
	
	public int id;

	public BoardEvent(Object source) {
		super(source);
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
}
