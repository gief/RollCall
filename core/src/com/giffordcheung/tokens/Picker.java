package com.giffordcheung.tokens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Picker {
	private Image winner_image;
	
	public Image getWinnerImage() {
		if (winner_image == null) {
			/**
			 * Draw the image for the winner.
			 */
			Pixmap pixmap = new Pixmap(75, 75, Pixmap.Format.RGBA8888);

	        //Draw a circle about the middle
	        pixmap.setColor(Color.YELLOW);
	        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 2);
			pixmap.setColor(Color.BLACK);
			pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 4);
			pixmap.setColor(Color.YELLOW);
			pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 4);
	      
			Texture winner_texture = new Texture(pixmap);
			pixmap.dispose();
			winner_image = new Image(winner_texture);
		}
		return winner_image;
	}

	public void animatePickTo(Token picked) {
		MoveToAction action_center = new MoveToAction();
		action_center.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		action_center.setDuration((float) .3);

		MoveToAction action = new MoveToAction();
		action.setPosition(picked.getButton().getX(), picked.getButton().getY());
		action.setDuration((float) .25);
		
		SequenceAction sequential_action = new SequenceAction(action_center, action);
		getWinnerImage().addAction(sequential_action);
		// e.g. this.picker.getWinnerImage().setPosition(picked.getButton().getX(), picked.getButton().getY());
				
	}
}
