package com.geeks.web;

public class VerifyReq extends WebRequest {
	private final String VERIFY = HOST + "verify/";
	private String args;
	
	public VerifyReq(WebResponse webRes) {
        super(webRes);
	}
	
	public void verifyThread(final String n, final String c) {
		StringBuilder sb = new StringBuilder();
		sb.append("phone_number=").append(n);
		sb.append("&code=").append(c);
		args = sb.toString();
		new Thread(new Runnable() {
			@Override
			public void run() {
				int ret = handleRequest(VERIFY, args);
		        if (ret < 0) {
		        	wResponse.failHandle(ret);
		        	return;
		        } else if (jsonret != null) {
		        	handleJSON();
		        }
			}
		}).start();
	}
}
