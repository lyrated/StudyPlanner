package com.studyplanner.gui.topics;

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
import com.studyplanner.data.entity.Topic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TopicsViewHolder extends RecyclerView.ViewHolder {

    private final LinearLayout layoutItem;
    private final TextView topicItemView;
    private final TextView topicDetailsItemView;
    private final ImageView imgDelete;

    private List<Topic> mTopics;
    private Context mContext;
    private TopicsListAdapter.OnClickDeleteListener listener;
    private int mPosition;

    private TopicsViewHolder(View itemView, List<Topic> Topics,
                               Context context, TopicsListAdapter.OnClickDeleteListener listener) {
        super(itemView);
        mTopics = Topics;
        mContext = context;
        this.listener = listener;

        layoutItem = itemView.findViewById(R.id.layoutItem);
        topicItemView = itemView.findViewById(R.id.itemTextView);
        topicDetailsItemView = itemView.findViewById(R.id.detailsTextView);
        imgDelete = itemView.findViewById(R.id.img_delete_item);
    }

    public void bind(Topic topic, int position, HashMap<Long, Subject> subjects, boolean showDate) {
        long subjectId = topic.getSubject();
        Subject subject = subjects.get(subjectId);
        String subjectName = "";
        if (subject != null) {
            subjectName = "[" + subject.getName() + "] ";
        }

        String complete = "";
        if (topic.isCompleted()) {
            complete = "(DONE) ";
        }

        String topicItemViewText = complete + subjectName + topic.getName();
        topicItemView.setText(topicItemViewText);

        String topicDetailsText = topic.getDuration() + " min";
        if (showDate) {
            SimpleDateFormat df = new SimpleDateFormat("E, dd.MM.yy", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            topicDetailsText += " | " + df.format(new Date(topic.getDate()));
        }
        topicDetailsItemView.setText(topicDetailsText);
        mPosition = position;
    }

    static TopicsViewHolder create(ViewGroup parent, List<Topic> Topics,
                                     Context context, TopicsListAdapter.OnClickDeleteListener listener) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new TopicsViewHolder(view, Topics, context, listener);
    }

    public void setListeners() {
        // on click on layout item, start EditTopicActivity
        layoutItem.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EditTopicActivity.class);
            // send Topic data to EditTopicActivity
            intent.putExtra(EditTopicActivity.TOPIC, mTopics.get(mPosition));
            ((Activity)mContext).startActivityForResult(intent, TopicsActivity.NEW_TOPIC_ACTIVITY_REQUEST_CODE);
        });

        imgDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClickDeleteListener(mTopics.get(mPosition));
            }
        });
    }
}
