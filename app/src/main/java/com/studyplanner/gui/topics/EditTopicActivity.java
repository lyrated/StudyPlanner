package com.studyplanner.gui.topics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.studyplanner.data.entity.Subject;
import com.studyplanner.R;
import com.studyplanner.data.entity.Topic;
import com.studyplanner.viewmodel.SubjectViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EditTopicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TOPIC = "com.studyplanner.TOPIC";
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    private long mTopicId;
    private EditText mEditTopicView;
    private EditText mEditDateView;
    private Spinner mChooseSubjectView;
    private EditText mEditDurationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // save button
        final Button button = findViewById(R.id.button_save_topic);
        button.setOnClickListener(view -> this.buttonOnClickListenerCallback(false));

        // finish button
        final Button buttonFinish = findViewById(R.id.button_finish);
        buttonFinish.setOnClickListener(view -> this.buttonOnClickListenerCallback(true));

        mEditTopicView = findViewById(R.id.edit_topic);
        mEditDateView = findViewById(R.id.edit_date);
        mChooseSubjectView = findViewById(R.id.choose_subject);
        mEditDurationView = findViewById(R.id.edit_duration);

        // prepopulate data if activity started from recyclerview item
        Bundle data = getIntent().getExtras();
        if (data != null) {
            Topic topic = (Topic) data.getSerializable(TOPIC);
            if (topic != null) {
                mTopicId = topic.getTopicId();
                mEditTopicView.setText(topic.getName());
                // date
                String date = dateFormat.format(new Date(topic.getDate()));
                mEditDateView.setText(date);
                // duration
                mEditDurationView.setText(String.valueOf(topic.getDuration()));
            } else {
                Log.d(EditTopicActivity.class.getSimpleName(), "Error getting topic");
            }
        } else {
            mTopicId = 0;
        }

        // set spinner for subjects
        SubjectViewModel subjectViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(SubjectViewModel.class);
        subjectViewModel.getAll().observe(this, subjects -> {
            ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
            mChooseSubjectView.setAdapter(adapter);
            // set default value
            if (data != null) {
                for (Subject s : subjects) {
                    Topic topic = (Topic) data.getSerializable(TOPIC);
                    if (topic != null && topic.getSubject() == s.getSubjectId()) {
                        int position = subjects.indexOf(s);
                        mChooseSubjectView.setSelection(position);
                        break;
                    }
                }
            }
        });

        // hide complete-button if a new topic is created
        if (mTopicId == 0) {
            buttonFinish.setVisibility(View.GONE);
            button.setLayoutParams(
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            );
        } else {
            buttonFinish.setVisibility(View.VISIBLE);
        }
    }

    private void buttonOnClickListenerCallback(boolean completed) {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditTopicView.getText()) || TextUtils.isEmpty(mEditDateView.getText())
                || TextUtils.isEmpty(mEditDurationView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String topicName = mEditTopicView.getText().toString();
            Subject subject = (Subject) mChooseSubjectView.getSelectedItem();
            String durationText = mEditDurationView.getText().toString();
            int duration = Integer.parseInt(durationText);
            // date
            String dateString = mEditDateView.getText().toString().replace("/", ".");
            Date date = new Date();
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                Toast.makeText(this, R.string.dateError, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            if (date == null) date = new Date();

            Topic newTopic = new Topic(topicName, date.getTime(), subject.getSubjectId(), duration);
            newTopic.setTopicId(mTopicId);
            newTopic.setCompleted(completed);
            replyIntent.putExtra(TOPIC, newTopic);

            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////// SPINNER //////////////////////////////////////////
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}