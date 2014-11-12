package com.giffordcheung.tokens;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;

public class ToastTestAndroidActivity extends AndroidApplication {
        ActionResolverAndroid actionResolver;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                actionResolver = new ActionResolverAndroid(this);
                
                // starts libGDX render thread
                initialize(new ToastTest(actionResolver));
        }
}