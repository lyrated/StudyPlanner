package com.studyplanner.gui.subjects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
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
import android.widget.Toast;

import com.studyplanner.R;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.viewmodel.SubjectViewModel;

public class SubjectsActivity extends AppCompatActivity implements SubjectsListAdapter.OnClickDeleteListener {

    private SubjectViewModel subjectViewModel;
    private SubjectsListAdapter adapter;
    public static final int NEW_SUBJECT_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        // back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set RecyclerView and SubjectListAdapter
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new SubjectsListAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // this button is shown when list is empty
        Button empty = findViewById(R.id.emptySubjectsView);
        empty.setOnClickListener( view -> {
            Intent intent = new Intent(SubjectsActivity.this, EditSubjectActivity.class);
            this.startActivityForResult(intent, NEW_SUBJECT_ACTIVITY_REQUEST_CODE);
        });

        // use SubjectViewModel to set recyclerView
        subjectViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(SubjectViewModel.class);
        subjectViewModel.getAll().observe(this, subjects -> {
            // show button if list is empty, else show recyclerview
            if (subjects.isEmpty()) {
                empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setList(subjects);
            }
        });
    }

    /////////////////////////////////// EDIT SUBJECT RESULT ///////////////////////////////////

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SUBJECT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Subject subject = (Subject) data.getSerializableExtra(EditSubjectActivity.SUBJECT);

            if (subject.getSubjectId() == 0) {
                // insert
                subjectViewModel.insert(subject);
            } else {
                // subject already exists -> update
                subjectViewModel.update(subject);
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
        inflater.inflate(R.menu.all_subjects_menu, menu);
        findViewById(R.id.new_subject);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.new_subject:
                intent = new Intent(SubjectsActivity.this, EditSubjectActivity.class);
                this.startActivityForResult(intent, NEW_SUBJECT_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickDeleteListener(Subject subject) {
        subjectViewModel.delete(subject);
    }
}