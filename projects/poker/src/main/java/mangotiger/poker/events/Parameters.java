/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.poker.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A way to name parameters so we can assign a regular expression back references correctly when creating an event via
 * reflection.
 * @author Tom Gagnier
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface Parameters {
  /**
   * A space delimited string mapping the constructor parameters to names.
   * The first name is for the first constructor arg, the second name for the second arguement and so on.
   */
  Type[] value();
}
