package com.smilevchy.http.protocol;

public enum HttpResponseStatusCode {
	STATUS_CODE_404(404),
	STATUS_CODE_501(501);
	
	private int code = -1;
	
	HttpResponseStatusCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
