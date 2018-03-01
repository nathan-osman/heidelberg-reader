package com.nathanosman.heidelbergreader;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.IdRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Miscellaneous utility functions
 */
class Util {

    /**
     * Apply the given typeface to a collapsing toolbar layout
     * @param activity parent activity
     * @param fontId font resource ID
     * @param resId toolbar resource ID
     */
    static void applyTypeface(Activity activity, @FontRes int fontId, @IdRes int resId) {

        // Retrieve a typeface for the font
        Typeface typeface = ResourcesCompat.getFont(activity, fontId);

        // Find the collapsing toolbar item
        CollapsingToolbarLayout layout = activity.findViewById(resId);

        // Apply the typeface to the toolbar
        layout.setCollapsedTitleTypeface(typeface);
        layout.setExpandedTitleTypeface(typeface);
    }
}
