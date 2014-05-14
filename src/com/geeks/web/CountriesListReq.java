package com.geeks.web;

public class CountriesListReq extends WebRequest {
	private final String COUNTRIES = HOST + "countrycode/";
	private String arguments;
	
	public CountriesListReq(WebResponse webRes) {
        super(webRes);
	}

	public void countriesListThread() {
		arguments = "list=all"; // by default
		new Thread(new Runnable() {
			@Override
			public void run() {
				processRequest();
			}
		}).start();
	}
	
	public void countryCodeThread(final String name) {
		arguments = "country_name=" + name;
		new Thread(new Runnable() {
			@Override
			public void run() {
				processRequest();
			}
		}).start();
	}
	
	private void processRequest() {
        int ret = handleRequest(COUNTRIES, arguments);
        if (ret < 0) {
        	wResponse.failHandle(ret);
        	return;
        } else if (jsonret != null) {
        	handleJSON();
        }
	}
}
