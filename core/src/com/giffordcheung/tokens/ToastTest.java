package com.giffordcheung.tokens;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;


public class ToastTest implements ApplicationListener, InputProcessor {
       GL20 gl;
       ActionResolver actionResolver;


      // This var is set by clicked item no. in MyListActivity (see below)
       public static int myListVar = 0;


      public ToastTest(ActionResolver actionResolver)
       {
               this.actionResolver = actionResolver;
       }
       
       @Override
       public void create() {
               gl = Gdx.app.getGraphics().getGL20();

              /*
              gl.glMatrixMode(GL20.GL_MODELVIEW);
               gl.glLoadIdentity();
*/

              actionResolver.showToast("Tap screen to open AlertBox", 5000);
               
               Gdx.input.setInputProcessor(this);
       }


      @Override
       public void render() {
               // clear screen and set it to white
               gl.glClearColor(1, 1, 1, 1);
               gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
       }


      @Override
       public boolean touchDown(int x, int y, int pointer, int button) {
               actionResolver.showAlertBox("AlertBox title", "AlertBox message", "Button text");
//             actionResolver.openUri("http://www.google.com/");
               return true;
       }


      @Override
       public boolean keyDown(int keyCode) {
               switch (keyCode) {
               // for ListActivity 

          case Input.Keys.MENU:
                       actionResolver.showMyList();
                       return true;
               }
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
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}