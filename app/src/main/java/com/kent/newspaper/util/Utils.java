package com.kent.newspaper.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * author Kent
 * date 2018/8/13 013
 * version 1.0
 */
public class Utils {

    public static int getAttrValue(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }

}
