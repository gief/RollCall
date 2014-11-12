package com.giffordcheung.tokens;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Toast;


public class ActionResolverAndroid implements ActionResolver {
       Handler uiThread;
       Context appContext;


      public ActionResolverAndroid(Context appContext) {
               uiThread = new Handler();
               this.appContext = appContext;
       }

      @Override
       public void showAlertBox(final String alertBoxTitle,
                       final String alertBoxMessage, final String alertBoxButtonText) {
               uiThread.post(new Runnable() {
                       @Override
					public void run() {
                               new AlertDialog.Builder(appContext)
                                               .setTitle(alertBoxTitle)
                                               .setMessage(alertBoxMessage)
                                               .setNeutralButton(alertBoxButtonText,
                                                               new DialogInterface.OnClickListener() {
                                                                       @Override
																	public void onClick(DialogInterface dialog,
                                                                                       int whichButton) {
                                                                       }
                                                               }).create().show();
                       }
               });
       }


      @Override
       public void openUri(String uri) {
               Uri myUri = Uri.parse(uri);
               Intent intent = new Intent(Intent.ACTION_VIEW, myUri);
               appContext.startActivity(intent);
       }


      @Override
       public void showMyList() {
               appContext.startActivity(new Intent(this.appContext, MyListActivity.class));
       }


	@Override
	public void showToast(final CharSequence toastMessage, final int toastDuration) {
        uiThread.post(new Runnable() {
            @Override
			public void run() {
                    Toast.makeText(appContext, toastMessage, toastDuration)
                                    .show();
            }
    });
		
	}

	@Override
	public void requestPicture(Uri uriSavedImage) {
		int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
	    // start the image capture Intent
	    // java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare();
	    
	    if (intent.resolveActivity(appContext.getPackageManager()) != null) {
		    ((Activity)appContext).startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    }
	}
}
