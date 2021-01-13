package com.studyplanner.gui.subjects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studyplanner.R;
import com.studyplanner.data.entity.Subject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EditSubjectActivity extends AppCompatActivity {
    public static final String SUBJECT = "com.studyplanner.SUBJECT";

    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.ENGLISH);

    private long mSubjectId;
    private EditText mEditSubjectView;
    private EditText mEditExamDateView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditSubjectView = findViewById(R.id.edit_subject);
        mEditExamDateView = findViewById(R.id.edit_exam_date);

        // prepopulate data if activity started from recyclerview item
        Bundle data = getIntent().getExtras();
        if (data != null) {
            Subject subject = (Subject) data.getSerializable(SUBJECT);
            if (subject != null) {
                mSubjectId = subject.getSubjectId();
                mEditSubjectView.setText(subject.getName());

                String date = dateFormat.format(new Date(subject.getExamDate()));
                mEditExamDateView.setText(date);
            } else {
                mSubjectId = 0;
            }
        }

        // save button
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditSubjectView.getText()) || TextUtils.isEmpty(mEditExamDateView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String subjectName = mEditSubjectView.getText().toString();
                String examDateString = mEditExamDateView.getText().toString().replace("/", ".");
                Date examDate = null;
                try {
                    examDate = dateFormat.parse(examDateString);
                } catch (ParseException e) {
                    Toast.makeText(this, R.string.dateError, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (examDate == null) examDate = new Date();
                Subject newSubject = new Subject(subjectName, examDate.getTime());

                if (mSubjectId != 0) {
                    newSubject.setSubjectId(mSubjectId);
                }
                replyIntent.putExtra(SUBJECT, newSubject);

                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}