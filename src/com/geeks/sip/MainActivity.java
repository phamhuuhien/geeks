package com.geeks.sip;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.csipsimple.R;
import com.csipsimple.ui.SipHome;
import com.geeks.helper.CommonHelper;

public class MainActivity extends CustomActivity {
	//	private final String PHONENUMBER = "phonenumber";
	public static final String SIPUSER = "sipuser";
	public static final String SIPPASS = "sippass";

	private SharedPreferences mPrefs;
	private String sipuser, sippass;

	private Button id_btn_next;
	private EditText id_edt_phonenumber;

	private final Handler mHandler = new Handler();
	private ProgressDialog progressDialog;

	private void startSipHome() {
		Intent siphome = new Intent(MainActivity.this, SipHome.class);
		siphome.putExtra(SIPUSER, sipuser);
		siphome.putExtra(SIPPASS, sippass);
		startActivity(siphome);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(checkActivated()) {
			startSipHome();
		}
		setContentView(R.layout.activity_main);
		initComponents();
		addListenner();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_btn_next:
			if (CommonHelper.isNetworkAvailable(getApplicationContext())) {
				//registerThread();
				progressDialog = ProgressDialog.show(this, "", "Registering ...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						register(id_edt_phonenumber.getText().toString(), RandomAlphaNumericString(8));
					}}).start();
				Log.i("MainAct", "Register");
			} else {
				CommonHelper.showWarningDialog(MainActivity.this, getResources()
						.getString(R.string.noInternet));
				Log.i("MainAct", "NO internet");
			}
			break;
		default:
			break;
		}

	}

	public void register(final String username, final String password) {
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost("http://173.198.254.66:8000/pbx/register/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("username", username);
			obj.put("password", password);
			obj.put("vmpassword","1234");

			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
			// CONVERT RESPONSE TO STRING
			String result = EntityUtils.toString(response.getEntity());
			final JSONObject jsonObj = new JSONObject(result);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					progressDialog.dismiss();
					try {
						int code = jsonObj.getInt("error_code");
						String message = jsonObj.getString("message");
						if(code == 0) {
							SharedPreferences mPrefs = getSharedPreferences("LSprefs", 0);
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.clear();

							editor.putString(SIPUSER, username);
							editor.putString(SIPPASS, password);
							sipuser = username;
							sippass = password;
							editor.commit();
							startSipHome();
						} else {
							String newline = System.getProperty("line.separator");
							CommonHelper.showWarningDialog(MainActivity.this, "Error:" + code + newline + message);
						}
					} catch (Exception e) {

					}
				}});
			Log.e("TAG", "jsonObj="+jsonObj.toString());
		} catch (Exception e) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					progressDialog.dismiss();
				}});
			//Log.e("TAG", e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void initComponents() {
		id_btn_next = (Button) findViewById(R.id.id_btn_next);
		id_edt_phonenumber = (EditText) findViewById(R.id.id_edt_phonenumber);
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String myphonenumber = mTelephonyMgr.getLine1Number();
		id_edt_phonenumber.setText(myphonenumber);
	}

	@Override
	protected void addListenner() {
		id_btn_next.setOnClickListener(this);
	}

	private boolean checkActivated() {
		mPrefs = getSharedPreferences("LSprefs",0);
		sipuser = mPrefs.getString(SIPUSER, null);
		sippass = mPrefs.getString(SIPPASS, null);
		if (TextUtils.isEmpty(sipuser) || TextUtils.isEmpty(sippass)) {
			return false;
		} else {
			//myapp.sipUSR = sipuser;
			//myapp.sipPAS = sippass;
			return true;
		}
	}

	//	private void registerThread() {
	//		progressDialog = ProgressDialog.show(this, "", "Registering ...");
	//		WebResponse response = new WebResponse() {
	//			@Override
	//			public void successHandle(JSONObject json) {
	//				try {
	//					JSONObject data = json.getJSONObject("data");
	//					//					phone_number = data.getString("phone_number");
	//					//					sip_password = data.getString("sip_password");
	//					//                    sip_username = data.getString("sip_username");
	//					//					verify_code = data.getString("verify_code");
	//					//					Log.w("Register", "n " + phone_number + ", c " + verify_code);
	//				} catch (JSONException e) {
	//					e.printStackTrace();
	//				}
	//			}
	//			@Override
	//			public void failHandle(int i) {
	//				mHandler.post(new Runnable() {
	//					@Override
	//					public void run() {
	//						progressDialog.dismiss();
	//						//Toast toast = Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT);
	//						//toast.show();
	//						String pass = RandomAlphaNumericString(8);
	//					}
	//				});
	//			}
	//		};
	//		RegisterReq request = new RegisterReq(response);
	//		request.regisThread(id_edt_phonenumber.getText().toString());
	//	}

	public static String RandomAlphaNumericString(int size){
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String ret = "";
		int length = chars.length();
		for (int i = 0; i < size; i ++){
			ret += chars.split("")[ (int) (Math.random() * (length - 1)) ];
		}
		return ret;
	}

}
