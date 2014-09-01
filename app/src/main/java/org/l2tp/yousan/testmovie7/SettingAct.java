package org.l2tp.yousan.testmovie7;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.yousan.testmovie7.R;

public class SettingAct extends Activity {

	EditText mEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		mEditText = (EditText)this.findViewById(R.id.txt_fnm);
		mEditText.setSelection(0);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public void onClick_save(View v){
		 String sdir= mEditText.getText().toString();
		
		 /*
		 Intent intent = new Intent(getApplicationContext(), ShowAct.class);
		 intent.putExtra("dir_name"  , sdir);
		 startActivity(intent);
		  * */
	}
}
