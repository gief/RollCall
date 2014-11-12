package com.giffordc.token.android;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SurfaceView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.giffordcheung.tokens.ActionResolverAndroid;
import com.giffordcheung.tokens.ToastTest;
import com.giffordcheung.tokens.TokenApplication;

public class AndroidLauncher extends AndroidApplication {
	ActionResolverAndroid actionResolver;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		actionResolver = new ActionResolverAndroid(this);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TokenApplication(actionResolver), config);
		

        //initialize(new ToastTest(actionResolver));
	}
	
}
