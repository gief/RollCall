package com.giffordcheung.tokens;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PictureManager 
{
	private Texture texture;
	private TextureRegion backgroundTextureRegion;
	private int x_offset,y_offset;
	private FileHandle background_file_handle;
	private boolean updated_background_file_handle = false;
	private Sprite background_sprite;
	//private Image background_image;
	
	
	public PictureManager()
	{
		File imagesFolder = new File(Environment.getExternalStorageDirectory(), "/TokenBgs");
		if (!imagesFolder.exists()) {
			Boolean success = imagesFolder.mkdirs();
			if (!success) return;
		}
	    File image = new File(imagesFolder, "temp.jpg");
	    if (!image.exists()) {
	    	try {
				image.createNewFile();
			} catch (IOException e) {
				return;
			}
	    }
	    
	    background_file_handle = Gdx.files.external("TokenBgs/temp.jpg"); 
		
	}
	
	public void initialize() {
		loadBackground(Gdx.files.internal("default.jpg"));
	}
	
	public void loadBackground(FileHandle file) {
		texture = new Texture(file);
		backgroundTextureRegion = new TextureRegion(texture);//, 0, 0, 2048, 563);
		background_sprite = new Sprite(backgroundTextureRegion);
		
		x_offset = ((Double) Math.floor(Gdx.graphics.getWidth()/2)).intValue() - ((Double)Math.floor(backgroundTextureRegion.getRegionWidth()/2)).intValue();
		y_offset = ((Double) Math.floor(Gdx.graphics.getHeight()/2)).intValue() - ((Double)Math.floor(backgroundTextureRegion.getRegionHeight()/2)).intValue();
		background_sprite.setX(x_offset);
		background_sprite.setY(y_offset);
		bigpixmap = null;
	}
	
	public boolean isUpdatedBackground() {
		return updated_background_file_handle;
	}
	
	public void setUpdatedBackground(Boolean value) {
		updated_background_file_handle = value;
	}

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	public Texture getTexture()
	{
		return texture;
	}
	public Pixmap bigpixmap;

	public void generateBigPixmap () {
		// pixmap for little circles
		TextureData texturedata = texture.getTextureData();
		texturedata.prepare();
		bigpixmap = texturedata.consumePixmap();
	}

	
	public Texture newCircularCrop(Point center, int radius) {
		if (bigpixmap == null) {
			generateBigPixmap();
		}
		// we need to account for when center.x or center.y is negative.
		int x_offset = (center.x < 0 ? Math.abs(center.x) : 0);
		int y_offset = (center.y < 0 ? Math.abs(center.y) : 0);
		
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
	
	
	public void loadCameraBackground() {
		this.loadBackground(background_file_handle);
		//this.loadBackground(Gdx.files.external("camera.jpg"));
	}
	
	
	public void draw(SpriteBatch batch)
	{
		background_sprite.draw(batch);

		//batch.draw(backgroundTextureRegion, x_offset, y_offset);
//					((Double) Math.floor(Gdx.graphics.getWidth()/2)).intValue() - ((Double)Math.floor(backgroundTexture.getRegionWidth()/2)).intValue(),
//				    ((Double) Math.floor(Gdx.graphics.getHeight()/2)).intValue() - ((Double)Math.floor(backgroundTexture.getRegionHeight()/2)).intValue());
		
	}
	
	public Point unOffset(Point p) {
		Point offsetted = new Point(
			p.x - x_offset,
			p.y - y_offset);
		return offsetted;
	}

	public void camera() {
		
		// WORK THIS IN IN THE FUTURE hasSystemFeature(PackageManager.FEATURE_CAMERA);
		// http://developer.android.com/guide/topics/media/camera.html#intents
		 // create Intent to take a picture and return control to the calling application
	    Uri uriSavedImage = Uri.fromFile(background_file_handle.file());
	    //Log.log(TokenApplication.main.toString());
	    if (TokenApplication.main.actionResolver != null) {
	    	TokenApplication.main.getActionResolver().requestPicture(uriSavedImage);
	    }
	    
	}

	public void cameraCallback() {
	
		// TODO Auto-generated method stub
		// Get the dimensions of the bitmap
		int targetW = Gdx.graphics.getWidth(); // GL20.GL_MAX_TEXTURE_SIZE; //
		int targetH = Gdx.graphics.getHeight(); //GL20.GL_MAX_TEXTURE_SIZE; //
		
		
		
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + background_file_handle.path(), bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	    
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + background_file_handle.path(), bmOptions);
	    FileOutputStream fOut;
		try {

			fOut = (FileOutputStream) background_file_handle.write(false);
			//fOut = new FileOutputStream(background_file_handle.file());
		    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
		    fOut.flush();
		    fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		// Warning this is being called outside the OpenGL ES
		//this.background_file_handle.copyTo(Gdx.files.internal("camera.jpg"));
		this.updated_background_file_handle = true;
		
	}
}
