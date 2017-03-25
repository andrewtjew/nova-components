package org.nova.http.server.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam
{
	final static String AT_LEAST_ONE_SEGMENT="+";
	final static String ANY_SEGMENT="*";
	
	String value();
}
