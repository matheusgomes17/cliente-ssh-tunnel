package com.csv.vpn.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.EditTextPreference;
import com.csv.vpn.R;


public class SummaryEditTextPreference extends EditTextPreference {
    private CharSequence mDefaultSummary = getSummary();

    public SummaryEditTextPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextPreferenceStyle);
        mDefaultSummary = getSummary();
    }

    public SummaryEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setSummary(text);
    }

    @Override
    public void setSummary(CharSequence summary) {
        if (summary.toString().isEmpty()) {
            super.setSummary(mDefaultSummary);
        } else {
            super.setSummary(summary);
        }
    }
}
