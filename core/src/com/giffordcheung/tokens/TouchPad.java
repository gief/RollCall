package com.giffordcheung.tokens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
/*
Handles input processes  
*/
public class TouchPad implements InputProcessor
{
	
    private TokenApplication token_application;
	
	public TouchPad(TokenApplication token_application) {
		this.token_application = token_application; // We can use a event based mechanism if necessary
	}
	
    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Token new_token = token_application.token_manager.initializePendingToken(pointer);
		Drawable drawable = null;
		BitmapFont font = new BitmapFont();
		new_token.initializePending(drawable, font);
		Log.setOut("initialized" + new_token.index +"   "+ screenX + " " + screenY + " " + pointer + " " + button);
        token_application.token_display_table.addActor(new_token.getButton());
		new_token.moveTo(screenX,token_application.viewport.getScreenHeight() - screenY, 0);
		return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Debugging info
    	Log.setOut("u" + screenX + " " + screenY + " " + pointer + " " + button);
		
    	// What Token is pending?
    	Token pending = token_application.token_manager.getPendingToken(pointer);
		// Calculate the token location to take a snapshot of the underlying picture
    	Point adjusted = token_application.picture_manager.unOffset(new Point(screenX, screenY));
		adjusted = pending.recenter(adjusted);
		// Ask PictureManager for a new Texture that fits
		pending.setTextureClip(token_application.picture_manager.newCircularCrop(adjusted, ((Double)Math.floor(pending.getBackgroundWidth() / 2)).intValue()));

		token_application.token_manager.addToken(pending);
		
		
		return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
		Log.setOut("X" + screenX + " " + screenY + " " + pointer);
//		MoveToAction action = new MoveToAction();
//		action.setPosition(screenX, viewport.getScreenHeight()-screenY);
//		action.setDuration(0);
		//Log.getLogButton().addAction(action);
		// Move the pender
		token_application.token_manager.movePendingToken(pointer,screenX,token_application.viewport.getScreenHeight()-screenY);
		return true;
    }

	@Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}