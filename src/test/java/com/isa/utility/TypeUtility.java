package com.isa.utility;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TypeUtility {

    public static <T extends Enum<T>> T findEnumInsensitiveCase(Class<T> enumType, String name) {
        for (T constant : enumType.getEnumConstants()) {
            if (constant.toString().equalsIgnoreCase(name)) {
                return constant;
            }
        }
        log.error("No enum constant {}.{}", enumType.getCanonicalName(), name);
        throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + " " + name);
    }

}
