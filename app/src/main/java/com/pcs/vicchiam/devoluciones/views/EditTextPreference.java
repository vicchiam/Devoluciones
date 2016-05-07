package com.pcs.vicchiam.devoluciones.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by vicchiam on 06/05/2016.
 *
 * Adapted of the EditTextPreference class for show your value
 */
public class EditTextPreference extends android.preference.EditTextPreference {

    public EditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        return this.getText();
    }

}
