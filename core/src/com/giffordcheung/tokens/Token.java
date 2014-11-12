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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

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

		this.button = new TextButton("test", skin);
		this.button.setUserObject(this);
		this.button.addListener( new ChangeListener(){
				@Override
				public void changed (ChangeEvent event, Actor actor) {
					Log.setOut("boom " + ((Token) actor.getUserObject()).index);
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
		
		//test delete this
		//this.button = new TextButton("uppity", skin);
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
		Log.log("normal!");
	}

	public void showAsPicked() {
		Log.log("picked! " + this.button.getX());
	}
	
}
