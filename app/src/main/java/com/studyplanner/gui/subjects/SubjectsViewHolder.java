package com.studyplanner.gui.subjects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.studyplanner.R;
import com.studyplanner.data.entity.Subject;

import java.util.List;

public class SubjectsViewHolder extends RecyclerView.ViewHolder {

    private final LinearLayout layoutItem;
    private final TextView subjectItemView;
    private final TextView subjectDetailsItemView;
    private final ImageView imgDelete;

    private List<Subject> mSubjects;
    private Context mContext;
    private SubjectsListAdapter.OnClickDeleteListener listener;
    private int mPosition;

    private SubjectsViewHolder(View itemView, List<Subject> subjects,
                               Context context, SubjectsListAdapter.OnClickDeleteListener listener) {
        super(itemView);
        mSubjects = subjects;
        mContext = context;
        this.listener = listener;

        layoutItem = itemView.findViewById(R.id.layoutItem);
        subjectItemView = itemView.findViewById(R.id.itemTextView);
        subjectDetailsItemView = itemView.findViewById(R.id.detailsTextView);
        imgDelete = itemView.findViewById(R.id.img_delete_item);
    }

    public void bind(String text, String details, int position) {
        subjectItemView.setText(text);
        subjectDetailsItemView.setText(details);
        mPosition = position;
    }

    static SubjectsViewHolder create(ViewGroup parent, List<Subject> subjects,
                                     Context context, SubjectsListAdapter.OnClickDeleteListener listener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new SubjectsViewHolder(view, subjects, context, listener);
    }

    public void setListeners() {
        // on click on layout item, start EditSubjectActivity
        layoutItem.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EditSubjectActivity.class);
            // send subject data to EditSubjectActivity
            intent.putExtra(EditSubjectActivity.SUBJECT, mSubjects.get(mPosition));
            ((Activity)mContext).startActivityForResult(intent, SubjectsActivity.NEW_SUBJECT_ACTIVITY_REQUEST_CODE);
        });

        imgDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickDeleteListener(mSubjects.get(mPosition));
            }
        });
    }
}
