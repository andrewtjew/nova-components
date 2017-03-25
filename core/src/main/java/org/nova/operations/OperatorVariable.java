package org.nova.operations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OperatorVariable
{
	String description() default "";
	String minimum() default "";
	String maximum() default "";
	String alias() default "";
	Class<? extends Validator> validator() default DefaultValidator.class;
	String[] options() default "";
}
