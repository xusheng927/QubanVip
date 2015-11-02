package com.quban.qubanvip;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.quban.qubanvip.widget.CircleImageView;

public class BaseActivity extends Activity {
	public String networkError = "网络不给力";
	private Dialog dialog;// 网络加载dialog
	public BaseApplication myApp;// 全局变量

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myApp = (BaseApplication) getApplicationContext();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		myApp.mRequestQueue.cancelAll(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("onStart");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("onPause");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("onDestroy");
	}

	/**
	 * toast
	 */
//	public void toast(String str) {
//		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//	}

	private static Toast toast = null;
	/**
	 * 短暂显示Toast消息
	 * 
	 * @param message
	 */
	public void toast(String message) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) view.findViewById(R.id.toast_message);
		text.setText(message);
		if (toast == null) {
			toast = new Toast(this);
		}
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, 0, 300);
		toast.setView(view);
		toast.show();
	}

	/**
	 * 自定义dialog
	 */
	public void createProgressdialog(Context context) {
		if (dialog != null) {
			dialog.cancel();
		}
//		if(dialog == null || (dialog != null && !dialog.isShowing())){
			dialog = new Dialog(context, R.style.CustomDialog);
			dialog.setCanceledOnTouchOutside(false);// 点击周围不消失
			dialog.setCancelable(true);// 点击返回键消失
			View view = LayoutInflater.from(context).inflate(
					R.layout.dialog_loading, null);
			ImageView image = (ImageView) view.findViewById(R.id.imageview);
			Animation operatingAnim = AnimationUtils.loadAnimation(context,
					R.anim.loading_dialog);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
			image.startAnimation(operatingAnim);
			dialog.setContentView(view);
			dialog.show();
//		}
	}

	public void dismissDialog() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (dialog.isShowing() && dialog != null) {
					dialog.cancel();
				}
			}
		}, 10);
	}

	/**
	 * 网络请求string_dialog提示
	 */
	public void getNetwork(Context context, String url, final JSONObject jo,
			final Handler hander) {
		createProgressdialog(context);
		System.out.println("网络请求数据>>>>" + jo.toString());
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Listener<String>() {
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						dismissDialog();
						Message message = new Message();
						Bundle bd = new Bundle();
						bd.putString("request", arg0);
						message.setData(bd);
						hander.sendMessage(message);
						System.out.println("网络返回数据>>>>" + arg0);
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						dismissDialog();
						toast(networkError);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Information", jo.toString());
				return params;
			}
		};
		myApp.mRequestQueue.add(request);
	}

	/**
	 * 网络请求string_没有dialog提示
	 */
	public void getNetwork2(Context context, String url, final JSONObject jb,
			final Handler hander) {
		System.out.println("网络请求数据>>>>" + jb.toString());
		StringRequest request = new StringRequest(Request.Method.POST, url,
				new Listener<String>() {
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						Message message = new Message();
						Bundle bd = new Bundle();
						bd.putString("request", arg0);
						message.setData(bd);
						hander.sendMessage(message);
						System.out.println("网络返回数据>>>>" + arg0);
					}
				}, new ErrorListener() {
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						toast(networkError);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Information", jb.toString());
				return params;
			}
		};
		myApp.mRequestQueue.add(request);
	}

	/**
	 * 单加载图片
	 */
	public void getNetWork_image(String url, final ImageView iv) {
		ImageRequest imgRequest = new ImageRequest(url,
				new Response.Listener<Bitmap>() {
					public void onResponse(Bitmap arg0) {
						// TODO Auto-generated method stub
						iv.setImageBitmap(arg0);
					}
				}, 0, 0, Config.ARGB_8888, new ErrorListener() {
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						toast("没有相应的图片哦");
					}
				});
		myApp.mRequestQueue.add(imgRequest);
	}

	/**
	 * 单加载图片——圆形控件
	 */
	public void getNetWork_CircularImage(String url, final CircleImageView iv) {
		ImageRequest imgRequest = new ImageRequest(url,
				new Response.Listener<Bitmap>() {
					public void onResponse(Bitmap arg0) {
						// TODO Auto-generated method stub
						iv.setImageBitmap(arg0);
					}
				}, 0, 0, Config.ARGB_8888, new ErrorListener() {
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						// toast(networkError);
					}
				});
		myApp.mRequestQueue.add(imgRequest);
	}
	
}
