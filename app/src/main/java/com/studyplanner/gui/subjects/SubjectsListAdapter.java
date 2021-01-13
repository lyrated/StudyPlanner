package com.studyplanner.gui.subjects;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studyplanner.data.entity.Subject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SubjectsListAdapter extends RecyclerView.Adapter<SubjectsViewHolder> {
    public interface OnClickDeleteListener {
        void onClickDeleteListener(Subject subject);
    }

    private OnClickDeleteListener deleteListener;
    private Context mContext;
    private List<Subject> mSubjects;

    public SubjectsListAdapter(Context context, OnClickDeleteListener listener) {
        this.deleteListener = listener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SubjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SubjectsViewHolder.create(parent, mSubjects, mContext, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsViewHolder holder, int position) {
        if (mSubjects != null) {
            Subject current = mSubjects.get(position);

            // convert date to string
            SimpleDateFormat df = new SimpleDateFormat("E, dd.MM.yy", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = df.format(new Date(current.getExamDate()));

            holder.bind(current.getName(), date, position);
            holder.setListeners();
        }
    }

    @Override
    public int getItemCount() {
        if (mSubjects != null) {
            return mSubjects.size();
        }
        return 0;
    }

    public void setList(List<Subject> subjects) {
        mSubjects = subjects;
        notifyDataSetChanged();
    }
}