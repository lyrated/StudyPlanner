package com.studyplanner.gui.topics;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;

import java.util.HashMap;
import java.util.List;

public class TopicsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnClickDeleteListener {
        void onClickDeleteListener(Topic Topic);
    }

    private OnClickDeleteListener deleteListener;
    private Context mContext;
    private List<Topic> mTopics;
    private HashMap<Long, Subject> mSubjects = new HashMap<>();
    private boolean showDate = false;

    public TopicsListAdapter(Context context, OnClickDeleteListener listener) {
        this.deleteListener = listener;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TopicsViewHolder.create(parent, mTopics, mContext, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mTopics != null) {
            TopicsViewHolder viewHolder = (TopicsViewHolder) holder;
            viewHolder.bind(mTopics.get(position), position, mSubjects, showDate);
            viewHolder.setListeners();
        }
    }

    @Override
    public int getItemCount() {
        if (mTopics != null) {
            return mTopics.size();
        }
        return 0;
    }

    public void setParams(List<Topic> topics, List<Subject> subjects, boolean showDate) {
        mTopics = topics;
        if (subjects != null) {
            for (Subject s : subjects) {
                mSubjects.put(s.getSubjectId(), s);
            }
        }
        this.showDate = showDate;
        notifyDataSetChanged();
    }
}