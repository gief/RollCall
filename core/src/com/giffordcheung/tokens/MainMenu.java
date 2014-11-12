package com.giffordcheung.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Manages menu buttons and options for TokenApplication
 * Starts with a menu button in the center of the screen 
 * 
 * TODO:
 * Main menu button
 * + Camera button
 * + Random pick button (e.g. start player)
 * + Random order button?
 * 
 * Use a tree structure to represent internal button structure
 * Have cool and fancy menu transitions someday
 * Be data driven -- allow conversions to tree structure from JSON, use existing standards
 * @author Gifford Cheung
 *
 */
public class MainMenu {
	/* where to add and remove the menu buttons */
	private Table display_table;
	private TextButton _lazy_main;
	private TextButton _lazy_pick;
	private TextButton _lazy_camera;
	private TextButton _lazy_back;
	int diameter;
	
	public MainMenu() {
		this.display_table = TokenApplication.main.permena_display_table;
		diameter = 150; // TODO calculate based on screensize
	}
	public void initialize() {
		// Replicated from Token, TODO: create a new class to hold this style
		Pixmap pixmap = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);

        //Draw a circle about the middle
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 2);
		pixmap.setColor(Color.BLACK);
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
		
		// Create main menu button
		_lazy_main = new TextButton("Menu", skin);
		_lazy_main.addListener( new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				TokenApplication.main.main_menu.pressed(MenuItem.MAIN);
			}
		});
		
		// Lazy: Create Pick button
		_lazy_pick = new TextButton("Random Pick", skin);
		_lazy_pick.addListener( new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				TokenApplication.main.main_menu.pressed(MenuItem.PICK);
			}
		});
		
		// Lazy: Create Camera button
		_lazy_camera = new TextButton("Camera", skin);
		_lazy_camera.addListener( new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				TokenApplication.main.main_menu.pressed(MenuItem.CAMERA);
			}
		});
		
		// Lazy: Create Camera button
		_lazy_back = new TextButton("Back", skin);
		_lazy_back.addListener( new ChangeListener(){
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				TokenApplication.main.main_menu.pressed(MenuItem.BACK);
			}
		});
		
		display_table.addActor(_lazy_main);

		// do this automagically with a list of "visible buttons"
		_lazy_main.setPosition(TokenApplication.main.viewport.getScreenWidth() / 2, 
				TokenApplication.main.viewport.getScreenHeight() / 2, Align.center);
		_lazy_back.setPosition(TokenApplication.main.viewport.getScreenWidth() / 2, 
				TokenApplication.main.viewport.getScreenHeight() / 2, Align.center);
		_lazy_pick.setPosition(TokenApplication.main.viewport.getScreenWidth() / 2, 
				200 + TokenApplication.main.viewport.getScreenHeight() / 2, Align.center);
		_lazy_camera.setPosition(TokenApplication.main.viewport.getScreenWidth() / 2, 
				-200 + TokenApplication.main.viewport.getScreenHeight() / 2, Align.center);
		

	}
	
	public enum MenuItem {
	    MAIN,
	    PICK, CAMERA, BACK
	}
	
	protected void pressed(MenuItem item) {
		switch(item) {
		case MAIN:
			display_table.removeActor(_lazy_main);
			display_table.addActor(_lazy_pick);
			display_table.addActor(_lazy_camera);
			display_table.addActor(_lazy_back);
			Log.log("Main!");
			break;
		case BACK:
			display_table.addActor(_lazy_main);
			display_table.removeActor(_lazy_pick);
			display_table.removeActor(_lazy_camera);
			display_table.removeActor(_lazy_back);
			break;
		case PICK:
			TokenApplication.main.token_manager.pick();
			pressed(MenuItem.BACK);
			break;
		case CAMERA:
			TokenApplication.main.picture_manager.camera();
			pressed(MenuItem.BACK);
			break;
		default:
			Log.log("Menu: default");
			break;
		}
	}
	

}
