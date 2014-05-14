package com.geeks.sip;

import android.app.Activity;
import android.view.View;

public abstract class CustomActivity extends Activity implements
		View.OnClickListener {

	protected abstract void initComponents();

	protected abstract void addListenner();

}
