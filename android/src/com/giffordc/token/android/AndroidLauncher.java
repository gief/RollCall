package com.giffordc.token.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.giffordcheung.tokens.TokenApplication;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TokenApplication(), config);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    //inflater.inflate(R.menu.game_menu, menu);
	    return true;
	}
}
