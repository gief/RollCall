package com.giffordcheung.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.giffordcheung.tokens.MainMenu.MenuItem;

public class Token
{
	private TextButton button;
	private TextureAtlas texture_atlas;
	private Skin skin;
	public int index;
	public static int background_width = 200;
	public static int background_height = 200;
	
	public Token() {
		texture_atlas = new TextureAtlas();
		skin = new Skin();
		// Store the default libgdx font under the name "default".
		skin.add("default_font", new BitmapFont());
	}

	public void setSkin(Skin skin)
	{
		this.skin = skin;
	}

	public Skin getSkin()
	{
		return skin;
	}

	public void setBackgroundHeight(int background_height)
	{
		Token.background_height = background_height;
	}

	public int getBackgroundHeight()
	{
		return background_height;
	}

	public void setBackgroundWidth(int background_width)
	{
		Token.background_width = background_width;
	}

	public int getBackgroundWidth()
	{
		return background_width;
	}

	public TextButton getButton()
	{
		return button;
	}

	
	//initialize button
	public Button initializePending(Drawable drawable, BitmapFont font) {
		
		Pixmap pixmap = new Pixmap(background_width, background_height, Pixmap.Format.RGBA8888);

        //Draw a circle about the middle
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 2);
		pixmap.setColor(Color.GRAY);
		pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 4);
        Pixmap.setBlending(Pixmap.Blending.None);
		pixmap.setColor(Color.CLEAR);
		pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 15);
        
		Texture circle_texture = new Texture(pixmap);

        //It's the textures responsibility now... get rid of the pixmap
        pixmap.dispose();
		
		texture_atlas.addRegion("circle", circle_texture, 0,0,background_width,background_height);
		skin.addRegions(texture_atlas);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		
		drawable = skin.newDrawable("circle", Color.WHITE);
		textButtonStyle.up = drawable; //skin.newDrawable("circle", Color.DARK_GRAY);
		textButtonStyle.down = drawable;//skin.newDrawable("region1", Color.DARK_GRAY);
		textButtonStyle.checked = drawable;//skin.newDrawable("region1", Color.BLUE);
		textButtonStyle.over = drawable;//skin.newDrawable("region1", Color.LIGHT_GRAY);
		textButtonStyle.font = font = skin.getFont("default_font"); 
		skin.add("default", textButtonStyle);

		this.button = new TextButton("", skin);
		this.button.setName("TokenButton" + this.toString());
		this.button.setUserObject(this);
		this.button.addListener( new ChangeListener(){
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					((Token) actor.getUserObject()).toggleMenu();
					//Log.setOut("boom " + ((Token) actor.getUserObject()).index);
				}
		});
		
		return this.button;
	}
	
	public void setTextureClip(Texture texture) {
		
		skin.add("token_background", new TextureRegion(texture, 0,0, background_width,background_height));		
		skin.addRegions(texture_atlas);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		Drawable drawable = skin.newDrawable("token_background", Color.WHITE);
		textButtonStyle.up = drawable;
		textButtonStyle.down = drawable;
		textButtonStyle.checked = drawable;
		textButtonStyle.over = drawable;
		textButtonStyle.font = skin.getFont("default_font"); 
		skin.add("default", textButtonStyle);
		button.setStyle(textButtonStyle);
		button.setVisible(true);
	}
	
	public void moveTo(int x, int y, float duration) {	
		MoveToAction action = new MoveToAction();
		Point center = recenter(new Point(x,y));
		action.setPosition(center.x, center.y);
		action.setDuration(duration);
		button.addAction(action);
	}
	
	public Point recenter(Point p) {
		int x = p.x - (int) Math.floor( background_width / 2);
		int y = p.y - (int) Math.floor( background_height / 2);
		return new Point(x,y);
	}
	
	public Point uncenter(Point p) {
		int x = p.x + (int) Math.floor( background_width / 2);
		int y = p.y + (int) Math.floor( background_height / 2);
		return new Point(x,y);
	}

	public void returnToNormal() {
		TokenApplication.main.token_manager.hidePick(this);
	}

	public void showAsPicked() {
		TokenApplication.main.token_manager.pick(this);
	}

	/* where to add and remove the menu buttons */
	private Table display_table;
	private TextButton _lazy_delete;
	private TextButton _lazy_roll_dice;
	private TextButton _lazy_toggle_pick;
	private TextButton _lazy_annotate;
	private int diameter = 80;
	
	public void initializeTokenMenu() {
		// Replicated from Token, TODO: create a new class to hold this style?

		this.display_table = TokenApplication.main.permena_display_table;
		
		Pixmap pixmap = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);

        //Draw a circle about the middle
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 2);
		pixmap.setColor(Color.GRAY);
		pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 4);
        
		Texture circle_texture = new Texture(pixmap);

        //It's the textures responsibility now... get rid of the pixmap
        pixmap.dispose();
		
		TextureAtlas texture_atlas = new TextureAtlas();
		texture_atlas.addRegion("circle", circle_texture, 0,0,diameter,diameter);
		Skin skin = new Skin();
		skin.add("default_font", new BitmapFont());
		skin.addRegions(texture_atlas);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		
		textButtonStyle.up = skin.newDrawable("circle", Color.WHITE); 
		textButtonStyle.down = skin.newDrawable("circle", Color.WHITE);
		textButtonStyle.checked = skin.newDrawable("circle", Color.WHITE);
		textButtonStyle.over = skin.newDrawable("circle", Color.WHITE);
		textButtonStyle.font = skin.getFont("default_font"); 
		skin.add("default", textButtonStyle);
		
		// Create delete button
		_lazy_delete = new TextButton("X", skin);
		_lazy_delete.setName("_lazy_delete" + this.toString()); // for use with findActor
		_lazy_delete.setUserObject(this);
		_lazy_delete.addListener( new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				((Token) actor.getUserObject()).pressed(TokenMenuItem.DELETE, (Token) actor.getUserObject());
			}
		});
		
		// Create clear button
		_lazy_toggle_pick = new TextButton("Toggle pick", skin);
		_lazy_toggle_pick.setName("_lazy_toggle_pick" + this.toString());
		_lazy_toggle_pick.setUserObject(this);
		_lazy_toggle_pick.addListener( new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				((Token) actor.getUserObject()).pressed(TokenMenuItem.TOGGLE_PICK, (Token) actor.getUserObject());
			}
		});
		
		setMenuButtonLocations();
	}
	

	public void setMenuButtonLocations() {
		if (_lazy_delete != null) {
			_lazy_delete.setX(this.button.getX(Align.left));
			_lazy_delete.setY(this.button.getY(Align.center) + diameter/2 + 2);
			_lazy_toggle_pick.setX(this.button.getX(Align.center) + diameter/2);
			_lazy_toggle_pick.setY(this.button.getY(Align.center) - diameter/2 + 2);
		}
	}

	protected void toggleMenu() {
		if (_lazy_delete == null) initializeTokenMenu();
		if (display_table.findActor(_lazy_delete.getName()) == null) {
			display_table.addActor(_lazy_delete);
			display_table.addActor(_lazy_toggle_pick);
			setMenuButtonLocations();
		} else {
			removeMenu();
		}
	}
	
	protected void removeMenu() {
		if (_lazy_delete != null && 
			display_table.findActor(_lazy_delete.getName()) != null) {
			display_table.removeActor(_lazy_delete);
			display_table.removeActor(_lazy_toggle_pick);
		}
	}

	
	public enum TokenMenuItem {
	    DELETE, ROLL_DICE, TOGGLE_PICK
	}
	
	protected void pressed(TokenMenuItem item, Token token) {
		this.toggleMenu();
		switch(item) {
		case DELETE:
			display_table.removeActor(this.button);
			TokenApplication.main.token_manager.delete(token);
			TokenApplication.main.token_manager.hideAllTokenMenus();
			//Log.log("delete this token");
			break;
		case TOGGLE_PICK:
			if (TokenApplication.main.token_manager.isPicked(this)) {
				TokenApplication.main.token_manager.hidePick(this);
			} else {
				TokenApplication.main.token_manager.pick(this);
			}
			// hide the buttons for the token
			break;
		}
	}
}
