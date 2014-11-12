package com.giffordcheung.tokens;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MyListActivity extends ListActivity{


      /** Called when the activity is first created. */
       @Override
	public void onCreate(Bundle icicle) {
               super.onCreate(icicle);
               
               // Create an array of Strings that
                // will be put in our ListActivity 

          String[] items = new String[] {
                               "First",
                               "Second",
                               "Third"};


              // Create an ArrayAdapter, that will actually
                // make the Strings above appear in the ListView 

            this.setListAdapter(new ArrayAdapter<String>(this,
                               android.R.layout.simple_list_item_1, items));
       }


      @Override
       protected void onListItemClick(ListView l, View v, int position, long id) {


              // Vibrate for given milliseconds as user feedback
                // Set the right for this in AndroidManifest.xml 
               long milliseconds = 100;
               Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
               vibrator.vibrate(milliseconds);


              super.onListItemClick(l, v, position, id);
               
               // Get the item that was clicked
//             Object o = this.getListAdapter().getItem(position);
//             String keyword = o.toString();


              Intent toastTest = new Intent(this, ToastTestAndroidActivity.class);


               // given myListVar is a static variable in main class...
               ToastTest.myListVar = position;
               
               startActivity(toastTest);
       }


}