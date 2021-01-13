package com.studyplanner.gui.topics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.studyplanner.R;
import com.studyplanner.data.entity.Topic;
import com.studyplanner.gui.studypartner.StudyPartnerInitialActivity;
import com.studyplanner.gui.subjects.SubjectsActivity;
import com.studyplanner.viewmodel.SubjectViewModel;
import com.studyplanner.viewmodel.TopicViewModel;

public class TopicsActivity extends AppCompatActivity implements TopicsListAdapter.OnClickDeleteListener {

    private TopicViewModel topicViewModel;
    private TopicsListAdapter adapterToday;
    private TopicsListAdapter adapterFuture;
    public static final int NEW_TOPIC_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        topicViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(TopicViewModel.class);

        this.setViews();
    }

    private void setViews() {
        Button empty = findViewById(R.id.emptyView);

        // Floating action button (add new topic)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(TopicsActivity.this, EditTopicActivity.class);
            startActivityForResult(intent, NEW_TOPIC_ACTIVITY_REQUEST_CODE);
        });

        // set Today's RecyclerView and TopicListAdapter
        RecyclerView recyclerViewToday = findViewById(R.id.topic_today_recyclerview);
        adapterToday = new TopicsListAdapter(this, this);
        recyclerViewToday.setAdapter(adapterToday);
        recyclerViewToday.setLayoutManager(new LinearLayoutManager(this));
        TextView titleToday = findViewById(R.id.title_today);
        View line1 = findViewById(R.id.view1);

        // set Future's RecyclerView and TopicListAdapter
        RecyclerView recyclerViewFuture = findViewById(R.id.topic_future_recyclerview);
        adapterFuture = new TopicsListAdapter(this, this);
        recyclerViewFuture.setAdapter(adapterFuture);
        recyclerViewFuture.setLayoutManager(new LinearLayoutManager(this));
        TextView titleFuture = findViewById(R.id.title_future);
        View line2 = findViewById(R.id.view2);

        topicViewModel.getAllFromToday().observe(this, topicsToday -> {
            topicViewModel.getAllFutureTopics().observe(this, topicsFuture -> {
                // Show help text if no subject was added and hide FloatingActionButton
                SubjectViewModel subjectViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(SubjectViewModel.class);
                subjectViewModel.getAll().observe(this, subjects -> {
                    if (subjects.isEmpty()) {
                        fab.setVisibility(View.GONE);
                        empty.setText(R.string.no_subjects);
                        empty.setOnClickListener( view -> {
                            Intent intent = new Intent(TopicsActivity.this, SubjectsActivity.class);
                            startActivityForResult(intent, NEW_TOPIC_ACTIVITY_REQUEST_CODE);
                        });
                    } else {
                        fab.setVisibility(View.VISIBLE);
                        empty.setText(R.string.no_topics);
                        empty.setOnClickListener( view -> {
                            Intent intent = new Intent(TopicsActivity.this, EditTopicActivity.class);
                            startActivity(intent);
                        });
                    }

                    // today recyclerview
                    if (topicsToday.isEmpty()) {
                        recyclerViewToday.setVisibility(View.GONE);
                        titleToday.setVisibility(View.GONE);
                        line1.setVisibility(View.GONE);
                    } else {
                        adapterToday.setParams(topicsToday, subjects, false);
                        recyclerViewToday.setVisibility(View.VISIBLE);
                        titleToday.setVisibility(View.VISIBLE);
                        line1.setVisibility(View.VISIBLE);
                    }

                    // future recyclerview
                    if (topicsFuture.isEmpty()) {
                        recyclerViewFuture.setVisibility(View.GONE);
                        titleFuture.setVisibility(View.GONE);
                        line2.setVisibility(View.GONE);
                    } else {
                        adapterFuture.setParams(topicsFuture, subjects, true);
                        recyclerViewFuture.setVisibility(View.VISIBLE);
                        titleFuture.setVisibility(View.VISIBLE);
                        line2.setVisibility(View.VISIBLE);
                    }

                    empty.setVisibility(View.GONE);

                    // empty message
                    if (topicsToday.isEmpty() && topicsFuture.isEmpty()) {
                        empty.setVisibility(View.VISIBLE);
                    }
                });
            });
        });
    }


    //////////////////////////////////////// EDIT TOPIC ////////////////////////////////////////

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TOPIC_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Topic topic = (Topic) data.getSerializableExtra(EditTopicActivity.TOPIC);
            if (topic != null) {
                if (topic.getTopicId() == 0) {
                    // insert
                    topicViewModel.insert(topic);
                } else {
                    // subject already exists -> update
                    topicViewModel.update(topic);
                }
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }


    //////////////////////////////////////// MENU ////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.new_topic:
                intent = new Intent(TopicsActivity.this, EditTopicActivity.class);
                this.startActivityForResult(intent, NEW_TOPIC_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.all_subjects:
                intent = new Intent(TopicsActivity.this, SubjectsActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.study_partner:
                intent = new Intent(TopicsActivity.this, StudyPartnerInitialActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickDeleteListener(Topic topic) {
        topicViewModel.delete(topic);
    }
}