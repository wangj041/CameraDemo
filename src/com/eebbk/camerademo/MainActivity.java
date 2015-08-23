package com.eebbk.camerademo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.eebbk.utils.DatabaseManager;
import com.eebbk.utils.DateFormatUtils;
import com.eebbk.utils.PicDatabase;

public class MainActivity extends Activity implements OnClickListener {

	private Button mTakePicture;
	private SurfaceView mSurView;
	private Camera mCamera;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//创建数据库
		DatabaseManager.getInstance(this);
		
		mTakePicture = (Button) findViewById(R.id.btn_take_picture);
		mTakePicture.setOnClickListener(this);
		
		
		mSurView = (SurfaceView) findViewById(R.id.sv_show_picture);
		mSurView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurView.getHolder().setFixedSize(240, 320);
		mSurView.getHolder().setKeepScreenOn(true);
		mSurView.getHolder().addCallback(new SurfaceCallback());
		
		
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_take_picture:

			mCamera.takePicture(null, null, new MyPictureCallback());//拍照啦
			Log.i("aaa", "调用了拍照的方法");

			break;
		}

	}

	//SurfaceView的回调函数
	class SurfaceCallback implements Callback{

		//类创建的时候调用，做一些初始化的工作
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {  
				mCamera = Camera.open(); // 打开摄像头  
				mCamera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象 
				Parameters mParamters = mCamera.getParameters(); // 获取各项参数  
				mParamters.setPictureFormat(ImageFormat.JPEG); // 设置图片格式 
				mParamters.set("orientation", "portrait");
				mParamters.set("rotation", 90);//设置显示的图片是正的
				
				
				
				
				mCamera.setDisplayOrientation(90);//设置拍照时预览内容是正的
				mCamera.setParameters(mParamters);//照相机应用这些设置
				
				mCamera.startPreview(); // 开始预览  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  

		}
		//SurfaceView发生改变的时候自动调用
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

			Parameters mParamters = mCamera.getParameters(); // 获取各项参数  
//			mParamters.setPictureFormat(ImageFormat.JPEG); // 设置图片格式 
//			//mParamters.set("orientation", "portrait");
//			mParamters.set("rotation", 90);
			mParamters.setPreviewSize(width, height); // 设置预览大小  
			mParamters.setPreviewFrameRate(11);  //设置每秒显示4帧  
			mParamters.setPictureSize(width, height); // 设置保存的图片尺寸  
			mParamters.setJpegQuality(100); // 设置照片质量 
			//mCamera.setParameters(mParamters);
			//			try {
			//				mCamera.setPreviewDisplay(holder);
			//			} catch (IOException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
			//			mCamera.startPreview();
			
			

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(mCamera != null){
				mCamera.release();
				mCamera = null;
			}

		}

	}



	//图片的回调函数
	class MyPictureCallback implements PictureCallback{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			//Toast.makeText(MainActivity.this, "拍完照片啦！", Toast.LENGTH_SHORT).show();
//			saveImageToSDcard(data);
			new MyTask().execute(data);
			
			Log.i("aaa", "拍完照片了！");
			Intent i = new Intent(MainActivity.this,PictureInfo.class);
			i.putExtra("picture", data);
			startActivity(i);
			//camera.startPreview();


		}

	}


	
	
	//保存数据到文件夹中
	public boolean saveImageToSDcard(byte[] bytes){
		//在根目录下创建一个文件夹，用来存储图片
		
		String path = Environment.getExternalStorageDirectory().getPath()+File.separatorChar+"MyCamera"+File.separatorChar;
		File mFileFolder = new File(path);
		//如果目录不存在，创建多级目录
				if(!mFileFolder.exists()){
					mFileFolder.mkdirs();
				}
		String mFileName = "IMG"+DateFormatUtils.formatCurrentTimeToString(System.currentTimeMillis())+".jpg";
		File mFile = new File(mFileFolder, mFileName);
		ContentValues cv = new ContentValues();
		cv.put(PicDatabase.Pic_Name, mFileName);
		cv.put(PicDatabase.Pic_Path, path+mFileName);
		DatabaseManager.insertDB(PicDatabase.Table_Name_Pic, null, cv);
		//保存图片到文件夹中
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(mFile);
				fos.write(bytes);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	
	//异步消息处理
	class MyTask extends AsyncTask<byte[], Void, Boolean>{
		
		//在后台任务开始执行前调用，用于进行一些    界面上的    初始化
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		//执行后台费时操作
		@Override
		protected Boolean doInBackground(byte[]... params) {
			boolean saved = saveImageToSDcard(params[0]);
			if(!saved){
				return false;
			}
			
			return true;
			
		}
		
		//每当后台任务调用了publicProgress，这个方法就会被调用一次，用于更新UI
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		//当后台任务执行完毕并   通过return语句  进行返回时，这个方法就会被调用,可以更新UI，进行一些善后工作
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				Toast.makeText(MainActivity.this, "图片后台保存成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(MainActivity.this, "图片后台保存失败", Toast.LENGTH_SHORT).show();
			}
			
		}

		
	}
	


}
