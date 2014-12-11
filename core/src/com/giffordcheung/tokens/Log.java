package com.giffordcheung.tokens;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Log
{
	static public Boolean LOGGING = false;
	static private String out = "Tokens log.";
	static private Label out_label = new Label("Log", new LabelStyle(new BitmapFont(), Color.ORANGE));
	static private Button log_button = new Button();

	public static void setLogButton(Button log_button)
	{
		Log.log_button = log_button;
	}

	public static Button getLogButton()
	{
		return log_button;
	}
	
	public static void initializeLogButton(Skin skin, Texture texture) 
	{

		Pixmap pixmap = new Pixmap(125,125, Pixmap.Format.RGBA8888);
		
        //Draw a circle about the middle
        pixmap.setColor(Color.WHITE);
        pixmap.drawCircle(pixmap.getWidth()/2, pixmap.getHeight()/2, pixmap.getHeight()/2 - 2);

        Texture circle_texture = new Texture(pixmap);

        //It's the textures responsibility now... get rid of the pixmap
        pixmap.dispose();
		
		TextureAtlas texture_atlas = new TextureAtlas();
		texture_atlas.addRegion("region1", texture, 0,0,125,125);
		texture_atlas.addRegion("circle", circle_texture, 0,0,125,125);
		
		skin.addRegions(texture_atlas);
		// Store the default libgdx font under the name "default".
		skin.add("default", new BitmapFont());

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("circle", Color.WHITE);
		textButtonStyle.down = skin.newDrawable("region1", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("region1", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("region1", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		log_button = new TextButton("Click me!", skin);
		
		log_button.addListener( new ChangeListener(){
	@Override
	public void changed (ChangeEvent event, Actor actor) {
		System.out.println("Button Pressed");
		Log.setOut("boom");
		}
		});
	}
	//Todo file handle for error.log

	
	public static Label getOutLabel()
	{
		return out_label;
	}
	

	public static void setOut(String out)
	{
		Log.out = out;
		out_label.setText(out);
	}
	
	public static void log(String s){
		Log.out += s;
		out_label.setText(out);
	}
	
	public static void clearLog() {
		Log.out = "";
		out_label.setText(out);
	}

	public static String getOut()
	{
		return out;
	}
}
