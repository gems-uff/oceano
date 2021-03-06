package br.uff.ic.oceano.core.dao.controle.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface MetodoRecuperaUnico
{
    String namedQuery() default "";
}