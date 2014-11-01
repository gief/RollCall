package com.giffordcheung.tokens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;

/**
 * RollCall is a board game utility that combines photography with a simple player tracker + random number generator. 
 * The primary use scenario is:
 * 
 * A "Game Facilitator" sits down to play with his friends. He takes a picture of all of the player tokens together
 * then touches each token to identify the four players (red, blue, green, yellow) 
 * Each touch creates a new circle (snapshot) of the token picture and (based on the x,y position) will move the tokens
 * to the edges of the screen. 
 * The facilitator now can drag the tokens to reorder them around the border of the screen to represent the
 * players locations in real life.
 * Shake the screen to start a random number generator.
 * Answer shown on the screen with an excited message
 * Press a delete key to remove the message
 * 
 * Future work: reordering the tokens
 * 
 * 
 * Cognitive analysis:
 * Closeness of mapping
 * Consistency
 * Diffuseness/tersness
 * Error-prone
 * Hard mental op - easy
 * Hidden dependencies - none
 * Juxtaposability - Visual control of image
 * Premature commitment - having to choose the picture to dictate the screen location -- future work: "freeze the token and have a 2nd operation to move to an edge"
 * Progressive evaluation -- see above
 * Role-expressiveness, cool.
 * 2ndary notation -- future: click on a token and add a name in text
 * Viscosity - future work
 * 
 * 
 * TODO:
 * [ ] Draw token 
 * - Ok if image is out of bounds
 * [ ] Move token to edge
 * - Calculate "closestEdgeCoordinates"
 * [ ] Detect shake
 * [ ] Run random selector
 * [ ] generate ["winner"] button
 * [ ] display winner "delete" button
 * [ ] display token "delete" button
 *  
 * 
 * @author Gifford Cheung
 *
 */

public class TokenApplication implements ApplicationListener
{
	OrthographicCamera camera;
	PictureManager picture_manager;
	Stage stage;
	ScreenViewport viewport;
	int viewport_x,viewport_y;
    SpriteBatch batch;
	PolygonSpriteBatch polybatch;
	TokenManager token_manager;
	TouchPad touch_pad;
	Table token_display_table;
	InputMultiplexer input_multiplexer;
	
    @Override
    public void create()
	{
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera); 
		picture_manager = new PictureManager();
		resetGame();
		
		picture_manager.initialize();
		
		// next steps
		batch = new SpriteBatch();
		polybatch = new PolygonSpriteBatch();
		
		//what happens on a reset()?
		stage = new Stage(viewport, batch); //also pass the singelton batch here. Try just to use onee batch to have a good performance.
		token_display_table = new Table();
		token_display_table.setFillParent(true);
		stage.addActor(token_display_table);
		
		
		touch_pad = new TouchPad(this);
		token_manager = new TokenManager(this);
		//Input Processors
		input_multiplexer = new InputMultiplexer();
		// order matters: https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/InputMultiplexer.java
		input_multiplexer.addProcessor(stage);		
		input_multiplexer.addProcessor(touch_pad);
		Gdx.input.setInputProcessor(input_multiplexer);
		

		
		//Gdx.input.setInputProcessor(stage);
		
		if (Log.LOGGING) {
			Skin skin = new Skin();
			/*
			 * Log.initializeLogButton(skin,picture_manager.getTexture());
		

			token_display_table.addActor(Log.getLogButton());
			
			Token test_token = new Token();
			test_token.initializePending(null, null);
			token_display_table.addActor(test_token.getButton());
			
			//Gdx.input.setInputProcessor(Log.getLogButton());
			 */
			stage.addActor(Log.getOutLabel());
			Log.getOutLabel().setY(35);
			Log.setOut(" y="+Log.getOutLabel().getY());
			MoveToAction action = new MoveToAction();
			action.setPosition(0, viewport.getScreenHeight()-45);
			action.setDuration(35);
			Log.getOutLabel().addAction(action);
			
			
			action = new MoveToAction();
			action.setPosition(600, 800);
			action.setDuration(1);
			Log.getLogButton().addAction(action);
		}
	}

	
    @Override
    public void render()
	{
    	//Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    	Gdx.gl.glClearColor(Color.CLEAR.r, Color.CLEAR.g, Color.CLEAR.b, Color.CLEAR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
        batch.begin();
		picture_manager.draw(batch);
        batch.end();

		//Log.setOut(" y="+Log.getOutLabel().getY());			
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
    }

	private void resetGame()
	{
		configureCamera();
	}

	private void configureCamera()
	{
	}

	@Override
    public void dispose()
	{
        batch.dispose();
    }

    @Override
    public void resize(int width, int height)
	{
		//resetGame();
		//viewport.update(width, height);
		stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause()
	{
    }

    @Override
    public void resume()
	{
    }
}

