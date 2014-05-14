package com.geeks.web;

import org.json.JSONObject;

/**
 * Each WebRequest should have one WebResponse listener.
 * Depending on request, we override parseJSON appropriately.
 * 
 */
public interface WebResponse {
	public void successHandle(JSONObject json);
    public void failHandle(int i);
}
