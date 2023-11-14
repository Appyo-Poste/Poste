package com.example.poste.activities;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Custom created AppCompatActivity class which we use to
 * add and implement multiple helper methods.
 *
 * @see AppCompatActivity
 * @author Jacob Paulin
 */
public abstract class PActivity extends AppCompatActivity {

    /**
     * onCreate method for the PActivity class.
     * This is where the code in this class starts from.
     *
     * @param savedInstanceState Bundle argument passed though from parent class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Utility method used to quickly log stuff to console.
     *
     * @param message The message to log to console
     */
    public final void log(String message) {
        Log.i(this.getLocalClassName(), message);
    }

    /**
     * Quickly retrieves a word from the strings.xml file and
     * gives the option to capitalize the first letter as well.
     *
     * @param string The string to retrieve
     * @param capitalize If the first letter should be capitalized
     * @return String with or without a capital first letter
     */
    public final String word(@StringRes int string, Boolean capitalize) {
        return (capitalize) ? capitalize(getString(string)) : getString(string);
    }

    /**
     * Capitalized the first letter of a string.
     *
     * @param string The string you wish to capitalize
     * @return Your string with a capital for the first letter
     */
    public static String capitalize(String string) {
        return (string == null || string.isEmpty()) ? string : string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /**
     * Converts a float number to a dp number for XML use.
     *
     * @param flt The float number to convert.
     * @return Your float in dp
     */
    public final Float floatToDp(float flt) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, flt, PActivity.this.getResources().getDisplayMetrics());
    }
}
