package com.summarization.web;

import java.io.InputStream;

public interface Api {

	public InputStream get(RequestParameters parameters) throws Exception;
}
