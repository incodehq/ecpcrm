package org.incode.eurocommercial.ecpcrm.camel.processor.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

    public static <T> T coalesce (final T x, final T other) {
        return x != null? x : other;
    }

}
