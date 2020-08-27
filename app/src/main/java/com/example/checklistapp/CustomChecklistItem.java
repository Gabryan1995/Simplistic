package com.example.checklistapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomChecklistItem extends LinearLayout implements View.OnClickListener {

    // TODO:
    // Add remove task button.
    
    ImageView mCheckboxImage;
    ImageView mDeleteButtonImage;
    EditText task;
    boolean isChecked;
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
        mDeleteButtonImage = new ImageView(context);
        task = new EditText(context);

        isChecked = false;
        mCheckboxImage.setBackgroundResource(R.drawable.ic_checkbox_deselected);
        mDeleteButtonImage.setBackgroundResource(R.drawable.ic_delete_opaque);
        task.setText("");
        task.setTextSize(22);
        task.setSingleLine(true);

        mCheckboxImage.setOnClickListener(this);
        task.setOnClickListener(this);

        setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams checkboxParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((LayoutParams) checkboxParams).gravity = Gravity.CENTER;

        ViewGroup.LayoutParams taskParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((LayoutParams) taskParams).weight = 1;

        addView(mCheckboxImage, checkboxParams);
        addView(task, taskParams);
        addView(mDeleteButtonImage, checkboxParams);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == task.getId()) {
            task.requestFocus();
            task.setCursorVisible(true);
        }
    }

    public void setTask(boolean checked, String newTask) {
        isChecked = checked;
        task.setText(newTask);
    }
}
