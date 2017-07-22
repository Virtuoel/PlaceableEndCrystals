package net.thedragonteam.pec.utils;

import java.util.Objects;

public class Utils {

    public static boolean is(String a, String object) {
        return Objects.equals(a, object);
    }

    public static boolean is(String a, String b, String object) {
        return is(a, object) && is(b, object);
    }

    public static boolean is(String a, String b, String c, String object) {
        return is(a, object) && is(b, object) && is(c, object);
    }

    public static boolean isNull(Object a) {
        return a == null;
    }

    public static boolean isNotNull(Object a) {
        return a != null;
    }
}
