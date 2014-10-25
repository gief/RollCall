package com.giffordcheung.tokens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.g2d.*;

public class PictureManager
{
	private Texture texture;
	private TextureRegion backgroundTexture;
	private int x_offset,y_offset;
	public PictureManager()
	{}

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	public Texture getTexture()
	{
		return texture;
	}
	
	public Texture newCircularCrop(Point center, int radius) {
		// we need to account for when center.x or center.y is negative.
		
		//Log.log("CROPPIN + point" + center.x + "  radius" + radius);
		
		int x_offset = (center.x < 0 ? Math.abs(center.x) : 0);
		int y_offset = (center.y < 0 ? Math.abs(center.y) : 0);
		
		TextureData texturedata = texture.getTextureData();
		texturedata.prepare();
		Pixmap bigpixmap = texturedata.consumePixmap();
		//Log.log(texturedata.getFormat().toString());
		Pixmap cropped_pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
		Pixmap big = new Pixmap(texture.getWidth(), texture.getHeight(), Pixmap.Format.RGBA8888);
		
		// Black background for crop (this black is in case the x_offset > 0)
		cropped_pixmap.setColor(Color.BLACK);
		cropped_pixmap.fillRectangle(0, 0, cropped_pixmap.getWidth(), cropped_pixmap.getHeight());
		cropped_pixmap.drawPixmap(bigpixmap, x_offset, y_offset, center.x+x_offset, center.y+y_offset, radius * 2-x_offset, radius * 2-y_offset);// 0, 0, radius *2 , radius * 2);
		cropped_pixmap.setColor(Color.WHITE);
		cropped_pixmap.drawCircle(radius, radius, radius - 2);
		cropped_pixmap.drawCircle(radius, radius, radius - 3);
		cropped_pixmap.setColor(Color.BLUE);
		cropped_pixmap.drawCircle(radius, radius, radius - 4);
		
		Pixmap.setBlending(Pixmap.Blending.None);
		cropped_pixmap.setColor(Color.CLEAR);
		for (int i = -1; i <= radius; i += 1) {
			cropped_pixmap.drawCircle(radius, radius, radius +i);
			cropped_pixmap.drawCircle(radius+1, radius, radius +i+1);
			cropped_pixmap.drawCircle(radius-1, radius, radius +i+1);
		}
		big.dispose();
		Texture new_crop = new Texture(cropped_pixmap);
		cropped_pixmap.dispose();
		
		return new_crop;
	}
	
	public void initialize()
	{
		//Load the Default picture of tokens
		texture = new Texture(Gdx.files.internal("default.jpg"));
		backgroundTexture = new TextureRegion(texture);//, 0, 0, 2048, 563);
		x_offset = ((Double) Math.floor(Gdx.graphics.getWidth()/2)).intValue() - ((Double)Math.floor(backgroundTexture.getRegionWidth()/2)).intValue();
		y_offset = ((Double) Math.floor(Gdx.graphics.getHeight()/2)).intValue() - ((Double)Math.floor(backgroundTexture.getRegionHeight()/2)).intValue();
		
	}
	
	public void draw(SpriteBatch batch)
	{
		//Background, centered
		//No scaling since we expect
		//to replace this image with a camera 
		//picture that is the same size as the screen
		batch.draw(backgroundTexture, x_offset, y_offset);
//					((Double) Math.floor(Gdx.graphics.getWidth()/2)).intValue() - ((Double)Math.floor(backgroundTexture.getRegionWidth()/2)).intValue(),
//				    ((Double) Math.floor(Gdx.graphics.getHeight()/2)).intValue() - ((Double)Math.floor(backgroundTexture.getRegionHeight()/2)).intValue());
		
	}
	
	public Point unOffset(Point p) {
		Point offsetted = new Point(
			p.x - x_offset,
			p.y - y_offset);
		return offsetted;
	}
		
	
}
