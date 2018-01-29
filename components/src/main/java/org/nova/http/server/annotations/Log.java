package org.nova.http.server.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log
{
    boolean value() default true;
    boolean lastRequestsInMemory() default true;
    boolean requestHeaders() default true;
    boolean requestParameters() default true;
    boolean requestContent() default true;
    boolean responseHeaders() default true;
    boolean responseContent() default true;
}
