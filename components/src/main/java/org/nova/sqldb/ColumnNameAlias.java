package org.nova.sqldb;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.FIELD,ElementType.PARAMETER})
public @interface ColumnNameAlias
{
    String value();
}
