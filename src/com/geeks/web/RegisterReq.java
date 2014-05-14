package com.geeks.web;

public class RegisterReq extends WebRequest {
	private final String REGIS = HOST + "register/";
	
	private String phone_number;
	
	public RegisterReq(WebResponse webRes) {
		super(webRes);
	}
	
	public void regisThread(final String number) {
		phone_number = "phone_number=" + number;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				int ret = handleRequest(REGIS, phone_number);
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
