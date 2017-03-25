package org.nova.http.server;

public enum Abnormal
{
	BAD_CONTENT,
	BAD_HEADER,
	BAD_COOKIE,
	BAD_PATH,
	BAD_QUERY,
	BAD_SESSION,
	
	NO_DECODER,
	NO_LOCK,
	NO_STATE,
//	NO_ENCODER,
	NO_WRITER,
	NO_READER, //
	PROCESSING,  //500
}
