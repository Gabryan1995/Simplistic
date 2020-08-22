package com.example.checklistapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomChecklistItem extends LinearLayout implements View.OnClickListener {

    ImageView mCheckboxImage;
    EditText task;
    private boolean isChecked;
    Context context;

    CustomChecklistItem(Context context) {
        super(context);
        this.context = context;
        init();
    }

    CustomChecklistItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    CustomChecklistItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        mCheckboxImage = new ImageView(context);
        task = new EditText(context);

        isChecked = false;
        mCheckboxImage.setBackgroundResource(R.drawable.ic_checkbox_deselected);
        task.setText("");

        task.setDuplicateParentStateEnabled(true);
        task.setSingleLine(true);

        mCheckboxImage.setOnClickListener(this);
        task.setOnClickListener(this);

        setOrientation(LinearLayout.HORIZONTAL);
        addView(mCheckboxImage, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(task, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onClick(View view) {
        if (view == mCheckboxImage) {
            isChecked = !isChecked;
            if (isChecked) {
                mCheckboxImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_selected));
            } else {
                mCheckboxImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_deselected));
            }
        }
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setTask(String newTask) {
        task.setText(newTask);
    }
}
