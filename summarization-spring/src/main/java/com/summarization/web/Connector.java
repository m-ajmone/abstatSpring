package com.summarization.web;

import java.io.InputStream;

public interface Connector {

	InputStream query(String path, QueryString queryString) throws Exception;

}
