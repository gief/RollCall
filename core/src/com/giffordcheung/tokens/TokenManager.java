package com.giffordcheung.tokens;

import java.util.*;

public class TokenManager
{
	private HashMap pending_tokens = new HashMap(8);
	private ArrayList tokens = new ArrayList(20);
    private TokenApplication token_application;
	
	public TokenManager(TokenApplication token_application) {
		this.token_application = token_application;
	}
	
	public Token initializePendingToken(int index) {
		Token token = new Token();
		// debugging
		token.index = index;
		pending_tokens.put(index, token);
		return token;
	}
	
	public Token movePendingToken(int index, int x, int y) {
		Token pending_token = getPendingToken(index);
		if (pending_token != null) {
			pending_token.moveTo(x,y,0);
		}
		return pending_token;
	}
	
	public Token getPendingToken(int index) {
		return (Token) pending_tokens.get(index);
	}

	public Point calculateClosestBorderLocation(Point point) {
		// given a point
		// calculate where it is in 
		//int offset =;
		int x = (point.x < token_application.viewport.getScreenWidth()/2 ? Token.background_width : token_application.viewport.getScreenWidth()) -   (int) Token.background_width / 2 ;
		//int y = point.y;
		return new Point(x, point.y);
	}

	/**
	 * All tokens will be automatically evenly arranged in a circle around the border. Priority towards the corners...
	 * @param pending
	 */
	public void addToken(Token pending) {
		Point center = pending.uncenter(new Point((int)pending.getButton().getX(),(int)pending.getButton().getY()));
		Point border_location = calculateClosestBorderLocation(center);
		
		// add to list
		
		// order entire list
		pending.moveTo(border_location.x, border_location.y, (float).5); // typecasting because we don't mind if it just truncates
	}
	
	
}
