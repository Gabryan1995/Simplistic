package com.example.checklistapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

public class CustomChecklistItem extends AppCompatEditText {

    Drawable mCheckboxButtonImage;
    private boolean isChecked;

    private void init() {
        mCheckboxButtonImage = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_checkbox_deselected, null);
        setCompoundDrawablesRelativeWithIntrinsicBounds(mCheckboxButtonImage, null, null, null);
        isChecked = false;

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((getCompoundDrawablesRelative()[0] != null)) {
                    float checkboxButtonStart;

                    checkboxButtonStart = mCheckboxButtonImage.getIntrinsicWidth() + getPaddingStart();

                    if (event.getX() < checkboxButtonStart) {
                        isChecked = !isChecked;
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (isChecked) {
                            mCheckboxButtonImage = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_checkbox_selected, null);
                        } else {
                            mCheckboxButtonImage = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_checkbox_deselected, null);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    void setChecked(boolean checked) {
        isChecked = checked;
    }

    public CustomChecklistItem(Context context) {
        super(context);
        init();
    }

    public CustomChecklistItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CustomChecklistItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
