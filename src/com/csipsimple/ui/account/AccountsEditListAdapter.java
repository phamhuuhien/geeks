/**
 * Copyright (C) 2010-2012 Regis Montoya (aka r3gis - www.r3gis.fr)
 * This file is part of CSipSimple.
 *
 *  CSipSimple is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  If you own a pjsip commercial license you can also redistribute it
 *  and/or modify it under the terms of the GNU Lesser General Public License
 *  as an android library.
 *
 *  CSipSimple is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CSipSimple.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.csipsimple.ui.account;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.geeks.R;
import com.geeks.helper.CommonHelper;
import com.geeks.sip.MainActivity;
import com.csipsimple.api.SipProfile;
import com.csipsimple.utils.AccountListUtils;
import com.csipsimple.utils.AccountListUtils.AccountStatusDisplay;
import com.csipsimple.utils.Log;
import com.csipsimple.wizards.WizardUtils;
import com.csipsimple.wizards.WizardUtils.WizardInfo;

public class AccountsEditListAdapter extends SimpleCursorAdapter implements OnClickListener {

	private static Handler mHandler = new Handler();
	
	public static final class AccountListItemViews {
		public TextView labelView;
		public TextView statusView;
		public View indicator;
		public View grabber;
		public CheckBox activeCheckbox;
		public ImageView barOnOff;
		public TextView balance;
	}

	private boolean draggable = false;

	public static final class AccountRowTag {
		public long accountId;
		public boolean activated;
	}

	private OnCheckedRowListener checkListener;

	public interface OnCheckedRowListener {
		void onToggleRow(AccountRowTag tag);
	}

	private static final String THIS_FILE = "AccEditListAd";

	public AccountsEditListAdapter(Context context, Cursor c) {
		super(context,
				R.layout.accounts_edit_list_item, c,
				new String[] {
				SipProfile.FIELD_DISPLAY_NAME
		},
		new int[] {
				R.id.AccTextView
		}, 0);
	}

	public void setOnCheckedRowListener(OnCheckedRowListener l) {
		checkListener = l;
	}

	private AccountListItemViews tagRowView(View view) {
		AccountListItemViews tagView = new AccountListItemViews();
		tagView.labelView = (TextView) view.findViewById(R.id.AccTextView);
		tagView.indicator = view.findViewById(R.id.indicator);
		tagView.grabber = view.findViewById(R.id.grabber);
		tagView.activeCheckbox = (CheckBox) view.findViewById(R.id.AccCheckBoxActive);
		tagView.statusView = (TextView) view.findViewById(R.id.AccTextStatusView);
		tagView.barOnOff = (ImageView) tagView.indicator.findViewById(R.id.bar_onoff);
		tagView.balance = (TextView) view.findViewById(R.id.AccTextBalance);

		view.setTag(tagView);

		tagView.indicator.setOnClickListener(this);

		return tagView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);

		AccountListItemViews tagView = (AccountListItemViews) view.getTag();
		if (tagView == null) {
			tagView = tagRowView(view);
		}

		// Get the view object and account object for the row
		final SipProfile account = new SipProfile(cursor);
				AccountRowTag tagIndicator = new AccountRowTag();
				tagIndicator.accountId = account.id;
				tagIndicator.activated = account.active;
				tagView.indicator.setTag(tagIndicator);

				tagView.indicator.setVisibility(draggable ? View.GONE : View.VISIBLE);
				tagView.grabber.setVisibility(draggable ? View.VISIBLE : View.GONE);

				// Get the status of this profile

				tagView.labelView.setText(account.display_name);
				
				final TextView balance = tagView.balance;
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						getBalance(account.display_name, balance);
					}}).start();

				// Update status label and color
				if (account.active) {
					AccountStatusDisplay accountStatusDisplay = AccountListUtils.getAccountDisplay(context,
							account.id);
					tagView.statusView.setText(accountStatusDisplay.statusLabel);
					tagView.labelView.setTextColor(accountStatusDisplay.statusColor);

					// Update checkbox selection
					tagView.activeCheckbox.setChecked(true);
					tagView.barOnOff.setImageResource(accountStatusDisplay.checkBoxIndicator);

				} else {
					tagView.statusView.setText(R.string.acct_inactive);
					tagView.labelView.setTextColor(mContext.getResources().getColor(
							R.color.account_inactive));

					// Update checkbox selection
					tagView.activeCheckbox.setChecked(false);
					tagView.barOnOff.setImageResource(R.drawable.ic_indicator_off);

				}

				// Update account image
				final WizardInfo wizardInfos = WizardUtils.getWizardClass(account.wizard);
				if (wizardInfos != null) {
					tagView.activeCheckbox.setBackgroundResource(wizardInfos.icon);
				}
	}

	public static void getBalance(final String username, final TextView balance) {
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost("http://173.198.254.66:8000/pbx/checkBalance/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("username", username);

			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
			// CONVERT RESPONSE TO STRING
			final String result = EntityUtils.toString(response.getEntity());
			final JSONObject jsonObj = new JSONObject(result);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					try {
						int code = jsonObj.getInt("error_code");
						String message = jsonObj.getString("message");
						if(code == 0) {
							balance.setText(message);
						} else {
							
						}
					} catch (Exception e) {

					}
				}});
		} catch (final Exception e) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					
				}});
			//Log.e("TAG", e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		Object tag = v.getTag();
		Log.d(THIS_FILE, "Clicked on ...");
		if (checkListener != null && tag != null) {
			checkListener.onToggleRow((AccountRowTag) tag);
		}
	}

	/**
	 * Set draggable mode of the adapter Ie show or hide the grabber icon
	 * 
	 * @param aDraggable if true we enter dragging mode
	 */
	public void setDraggable(boolean aDraggable) {
		draggable = aDraggable;
		notifyDataSetChanged();
	}

	/**
	 * Toggle dragable mode
	 * 
	 * @see AccountsEditList#setDraggable(boolean aDraggable)
	 */
	public void toggleDraggable() {
		setDraggable(!draggable);
	}

	/**
	 * Get draggable mode of the adapter
	 * 
	 * @return true if in dragging mode
	 * @see AccountsEditList#setDraggable(boolean aDraggable)
	 */
	public boolean isDraggable() {
		return draggable;
	}

}
