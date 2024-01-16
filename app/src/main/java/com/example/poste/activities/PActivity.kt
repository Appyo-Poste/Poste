package com.example.poste.activities

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * Custom created AppCompatActivity class which we use to
 * add and implement multiple helper methods.
 *
 * @see AppCompatActivity
 *
 * @author Jacob Paulin
 */
abstract class PActivity : AppCompatActivity() {
    /**
     * onCreate method for the PActivity class.
     * This is where the code in this class starts from.
     *
     * @param savedInstanceState Bundle argument passed though from parent class
     */
    protected open fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Utility method used to quickly log stuff to console.
     *
     * @param message The message to log to console
     */
    fun log(message: String?) {
        Log.i(this.getLocalClassName(), message!!)
    }

    /**
     * Quickly retrieves a word from the strings.xml file and
     * gives the option to capitalize the first letter as well.
     *
     * @param string The string to retrieve
     * @param capitalize If the first letter should be capitalized
     * @return String with or without a capital first letter
     */
    fun word(@StringRes string: Int, capitalize: Boolean): String? {
        return if (capitalize) capitalize(getString(string)) else getString(string)
    }

    /**
     * Converts a float number to a dp number for XML use.
     *
     * @param flt The float number to convert.
     * @return Your float in dp
     */
    fun floatToDp(flt: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            flt,
            this@PActivity.getResources().getDisplayMetrics()
        )
    }

    companion object {
        /**
         * Capitalized the first letter of a string.
         *
         * @param string The string you wish to capitalize
         * @return Your string with a capital for the first letter
         */
        fun capitalize(string: String?): String? {
            return if (string == null || string.isEmpty()) string else string.substring(0, 1)
                .uppercase(
                    Locale.getDefault()
                ) + string.substring(1)
        }
    }
}