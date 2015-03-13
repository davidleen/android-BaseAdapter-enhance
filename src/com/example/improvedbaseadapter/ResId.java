package com.example.improvedbaseadapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**

 * use annotation to define the class bind to a view that with an id;

 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ResId {

    int value();
}
