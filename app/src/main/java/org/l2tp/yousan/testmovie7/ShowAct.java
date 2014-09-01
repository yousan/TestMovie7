package org.l2tp.yousan.testmovie7;

import java.io.File;
import java.util.ArrayList;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.yousan.testmovie7.R;

import org.l2tp.yousan.testmovie7.SettingAct;

public class ShowAct extends Activity {

	int m_Positoin=0;
	ArrayList<String> mList =  new ArrayList<String>();
	private VideoView m_Video;
	String mDirName             = "/";
	static String mDirNameBase  = "/mnt/sdcard/sync";
	File mNextFile = new File("");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // sleepにしない
//    	Configuration config = getResources().getConfiguration();
//    	if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) { 
//    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
//    	}
//    	else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
//    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
//    	}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	try
    	{
//    		mDirName     = getIntent().getStringExtra( "dir_name"  );
    		mDirName     = mDirNameBase;
        	this.init_proc(mDirName);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	 // hello
    @Override
    protected void onPause() {
        super.onPause();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void init_proc(String sfnm) throws Exception{
		try
		{
			// String sMsg= getString(R.string.msg_note);
			// generateNotification(this, sMsg );
			
			m_Positoin= 0;
			mList = get_mp4Items();

			m_Video = (VideoView) findViewById(R.id.VideoView01);
			m_Video.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			MediaController mc = new MediaController(this);
	  		mc.setAnchorView(m_Video);
	  		//String fileFullPath = new String();
			m_Video.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					try {

						//mNextFile = mGetNextFile(mNextFile);
						//String fileFullPath = mNextFile.getAbsolutePath();
												
						//Log.d("log", "now playing " + fileFullPath);
						
				  		
						//m_Video.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
				  		//m_Video.setMediaController(null);
				  		//m_Video.setVideoPath("/sdcard/mv1/broken copy 2.mp4");
						//m_Video.start();
						proc_start();
					} catch(Exception e) {
					}
				}
			});
			m_Video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
		        public boolean onError(MediaPlayer mp, int what, int extra) {
		            try {
		    			//mNextFile = mGetNextFile(mNextFile);
		    			//String fileFullPath = mNextFile.getAbsolutePath();
		    			//fileFullPath = "/sdcard/mv1/broken copy 2.mp4"; 
		    			//Log.d("log", "now playing " + fileFullPath);
		    			
		    	  		
		    			//m_Video.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		    	  		//m_Video.setMediaController(null);
		    	  		//m_Video.setVideoPath("/sdcard/mv1/broken copy 2.mp4");
		    	  		//m_Video.setVideoPath( fileFullPath );
		    			//m_Video.start();
		            	Log.d("log", "Broken File");
		            	proc_start();
		            } catch (Exception e) {
		            }
		            //finish();
		            return true;
		        }
		    });

			//String dir= mDirName + mList.get(m_Positoin);
			proc_start();
		}catch(Exception e){
			throw e;
		}
	}
	
	// 現在再生中のファイル(メンバ変数(File)m_toPlay)を元に次のファイルを再生する。
	// メンバ変数はFileである。get_AllFilesで取られる配列では前後のファイルが消される可能性があり
	// 一体どこを再生していたか、というのを把握するためには配列の要素番号では不十分で、
	// フルパスのファイル名を元に一致させるしかない
	private File mGetNextFile(File currentFile)  {
		ArrayList<File> filesToPlay = get_AllFiles(mDirName);
		File ret = filesToPlay.get(0);
		int length = filesToPlay.size();
		mainloop: for (int i=0; i<length; i++) {
			try { // 配列外にアクセスするかも？
				Log.d("log", "checking match against " + filesToPlay.get(i).getAbsolutePath());
				String file1 = filesToPlay.get(i).getAbsolutePath(); 
				String file2 = currentFile.getAbsolutePath();
				if (filesToPlay.get(i).getAbsolutePath().equals(currentFile.getAbsolutePath())) { // 同じファイルが見つかった！
					if (i < length) { // 
						ret = filesToPlay.get(i+1);
						break mainloop;
					} else { // 最後のファイルだった場合には先頭へ
						ret = filesToPlay.get(0);
						break mainloop;
					}
				}
			} catch(Exception e) {
				
			}
		}
		return ret;
	}
	
	// 指定されたディレクトリに対して、すべてのディレクトリを再帰で潜ってファイル一覧を取得する。
	// ディレクトリは幅優先探索 返却するリストはファイルのみでディレクトリを含まない
	// 例: /hoge/01.mp4, /hoge/02.mp4, /hoge/fuga/01.mp4
    private ArrayList<File> get_AllFiles(String targetDirName) {
    	ArrayList<File> ret = new ArrayList<File>();
    	File targetDir = new File(targetDirName);
    	File[] filelist = targetDir.listFiles();
    	int imx = filelist.length;
    	for (int i=0; i<imx; i++) {
    		if (filelist[i].isFile()) {
    			//Log.d("log2",filelist[i].getAbsolutePath());
    			ret.add(filelist[i]);
    		} else if (filelist[i].isDirectory()) {
    			ArrayList<File> childDirRet = new ArrayList<File>();
    			childDirRet = get_AllFiles(filelist[i].getAbsolutePath());
    			long cmx = childDirRet.size();
    			for (int j=0; j<cmx; j++) {
        			//Log.d("log2", j + " " + cmx + " " + childDirRet.get(0).getName());
    				ret.add(childDirRet.get(j));
    			}
    		}
    	}
    	return ret;
    }
//    ArrayList<String> get_mp4Items( )throws Exception
//    {
//    	ArrayList<String> ret =  new ArrayList<String>();
//    	try
//    	{
//    		File dir = new File(mDirName);
//    		File[] filelist = dir.listFiles();
//    		int imx= filelist.length;
//			for(int i=0; i< imx; i++){
//				if(filelist[i].isFile()){
//					Log.d("log", filelist[i].getAbsoluteFile().toString());
//					ret.add( filelist[i].getName() ) ;
//				} else if (filelist[i].isDirectory()) {
//					
//				}
//			}
//    	}catch(Exception e){
//    		e.printStackTrace();
//    		throw e;
//    	}
//    	return ret;
//    }
    
    
    
    private  void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(context,  SettingAct.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }
	
    void proc_start() throws Exception{
    	try	{
			mNextFile = mGetNextFile(mNextFile);
			String fileFullPath = mNextFile.getAbsolutePath();
			Log.d("log", "now playing " + fileFullPath);
			
	  		
			m_Video.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	  		m_Video.setMediaController(null);
	  		m_Video.setVideoPath( fileFullPath );
			m_Video.start();
    	}catch(Exception e){
    		throw e;
    	}
    }
    
    ArrayList<String> get_mp4Items( )throws Exception
    {
    	ArrayList<String> ret =  new ArrayList<String>();
    	try
    	{
    		File dir = new File(mDirName);
    		File[] filelist = dir.listFiles();
    		int imx= filelist.length;
			for(int i=0; i< imx; i++){
				if(filelist[i].isFile()){
					//Log.d("log", filelist[i].getAbsoluteFile().toString());
					ret.add( filelist[i].getName() ) ;
				} else if (filelist[i].isDirectory()) {
					
				}
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		throw e;
    	}
    	return ret;
    }

}
