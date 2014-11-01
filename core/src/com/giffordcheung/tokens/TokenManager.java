package com.giffordcheung.tokens;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

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
	@SuppressWarnings("unchecked")
	public void addToken(Token pending) {
		Point center = pending.uncenter(new Point((int)pending.getButton().getX(),(int)pending.getButton().getY()));
		Point border_location = calculateClosestBorderLocation(center);
		// Move to edge
		//pending.moveTo(border_location.x, border_location.y, (float).25); // typecasting because we don't mind if it just truncates
		// add to array list
		tokens.add(pending);
		pending.index = tokens.indexOf(pending);
		/*
		Collections.sort(tokens, new Comparator<Token>() {
			// TODO Test this sort function
			@Override
			/**
			 * t1 > t2, positive.
			 we expect that the tokens are pushed to the edge of a screen.
			 we are comparing a box in clockwise order like this:
			 1234
			 e  5
			 d  6
			 c  7
			 ba98
			 * @param t1 Token
			 * @param t2 Token
			 * @return negative, positive, zero as a comparator
			 
			public int compare(Token t1, Token t2) {
			
				int t1_x = (int) t1.getButton().getX();
				int t1_y = (int) t1.getButton().getY();
				int t2_x = (int) t2.getButton().getX();
				int t2_y = (int) t2.getButton().getY();

				boolean edge1 = top_edge(t1);// ? 0x1 : 0x0);// | (left_edge(t1) ? 0x2 : 0x0);
				boolean edge2 = top_edge(t2);// ? 0x1 : 0x0); // | (left_edge(t2) ? 0x2 : 0x0);
				
				if (edge1 && edge2) {
					//both top edge, so the difference between x values standard
					return t1_x - t2_x;
				} else if (edge1) {
					//only t1 is top, so t1 must be smaller
					return -1;
				} else if (edge2) {
					//only t2 is top, so t2 is smaller
					return 1;
				}
				
				edge1 = left_edge(t1);
				edge2 = left_edge(t2);
				
				if (edge1 && edge2) {
					//both left edges, so the difference between y values reversed
					return t2_y - t1_y;
				} else if (edge1) {
					// only t1 is left edge, so t1 is bigger
					return 1;
				} else if (edge2) {
					// only t2 is left edge, so t2 is bigger
					return -1;
				}
				
				// If we've reached this point, the both t1 and t2 are in the right edge or bottom edge
				if (t1_y == t2_y) {
					// 
					return t2_x - t1_x;
				} else {
					return t2_y - t1_y;
				}
			}
			public boolean top_edge(Token t) {
				return t.getButton().getY() < t.getButton().getHeight();
			}
			public boolean left_edge(Token t) {
				return t.getButton().getX() < t.getButton().getHeight();
			}
		});
		*/
		reOrderTokensAroundScreen();
	}
	
	public void reOrderTokensAroundScreen() {

		// make sure we have a multiple of 4 for the num_tokens calculation
		int placeholder_size = tokens.size();
		if (placeholder_size % 4 > 0) placeholder_size += 4 - (placeholder_size % 4);
		
		double dbl_num_tokens_along_x_axis = numTokensX(token_application.viewport.getScreenWidth(), token_application.viewport.getScreenHeight(), (double)placeholder_size);
		double dbl_num_tokens_along_y_axis = numTokensY(dbl_num_tokens_along_x_axis, (double)placeholder_size);
		int num_tokens_along_x_axis = 1 + (int) Math.round(dbl_num_tokens_along_x_axis);
		int num_tokens_along_y_axis = 1 + (int) Math.round(dbl_num_tokens_along_y_axis);
		
		
		int x_interval = token_application.viewport.getScreenWidth() / num_tokens_along_x_axis;
		int y_interval = token_application.viewport.getScreenHeight() / num_tokens_along_y_axis;
		
		int x = 0;
		int y;

		for (int i = 0; i < tokens.size(); i += 1) {


			if (i < num_tokens_along_x_axis) {
				// Tokens along the top from left corner to almost the right corner
				y = (int) Token.background_height / 2; // y
				x = i * x_interval;
				// adjustments for corner
				if (i == 0) x += (int) (Token.background_width / 2);
			} else if ( i < num_tokens_along_x_axis + num_tokens_along_y_axis) {
				// Tokens along the right, from top right corner down to almost the bottom
				y = (i - num_tokens_along_x_axis) * y_interval;
				x = token_application.viewport.getScreenWidth() - (int) Token.background_width / 2; // x
				// adjustments for corner
				if (i == num_tokens_along_x_axis) y += (int) (Token.background_height / 2);
			} else if ( i < 2*num_tokens_along_x_axis + num_tokens_along_y_axis) {
				// Tokens along the bottom, from the lower right corner to almost the left
				y = token_application.viewport.getScreenHeight() - (int) (Token.background_height / 2); // checked works
				x = (num_tokens_along_x_axis * x_interval) - ((i-(num_tokens_along_x_axis + num_tokens_along_y_axis)) * x_interval);
				if (i == num_tokens_along_x_axis + num_tokens_along_y_axis) {
					x -= (int) (Token.background_width / 2);
				}
			} else {
				// Tokens along the left, from the lower left corner to almost the top
				y = (num_tokens_along_y_axis * y_interval) - ((i-(2*num_tokens_along_x_axis + num_tokens_along_y_axis))*y_interval);
				x = (int) Token.background_width / 2; // checked works
				if (i == 2*num_tokens_along_x_axis + num_tokens_along_y_axis) {
					y -= (int) (Token.background_height / 2);
				}
			} 
			((Token)tokens.get(i)).moveTo(x,token_application.viewport.getScreenHeight()-y, (float)0.25);
		}
		return;
	}

	/**
	 * Helper function to calculate how many tokens should fit around a screen
	 * Run this first before running distY
	 * 
	 * @param w screen width
	 * @param h screen height
	 * @param n number of tokens
	 * @return
	 */
	public double numTokensX(double w, double h, double n) {
		if (n%4 != 0 ) return -1;
		return(w*(.5*n-1)-h)/(h+w);
	}
	
	/**
	 * 
	 * Helper function to calculate how many tokens should fit around a screen
	 * Given a number of on one wall of x, calculate the recommended number of tokens along the ceiling and floor of y
	 * @param x
	 * @param n
	 * @return
	 */
	public double numTokensY(double x, double n) {
		if (n%4 != 0 ) return -1;
		return (n - 2*x - 4)/2;
	}
	
	
}
