package com.example.checklistapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.res.ResourcesCompat;

public class CustomChecklistItem extends LinearLayout implements View.OnClickListener {

    // TODO:
    // Add remove task button.
    
    ImageView mCheckboxImage;
    ImageView mDeleteButtonImage;
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
        mDeleteButtonImage = new ImageView(context);
        task = new EditText(context);

        isChecked = false;
        mCheckboxImage.setBackgroundResource(R.drawable.ic_checkbox_deselected);
        mDeleteButtonImage.setBackgroundResource(R.drawable.ic_delete_opaque);
        task.setText("");
        task.setTextSize(22);
        task.setSingleLine(true);
        task.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
        if (view == mCheckboxImage) {
            isChecked = !isChecked;
            if (isChecked) {
                mCheckboxImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_selected));
                task.setEnabled(false);
            } else {
                mCheckboxImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_deselected));
                task.setEnabled(true);
            }
        } else if (view.getId() == task.getId()) {
            task.requestFocus();
            task.setCursorVisible(true);
        }
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setTask(String newTask) {
        task.setText(newTask);
    }
}
