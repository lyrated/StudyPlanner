package com.studyplanner.gui.studypartner;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.studyplanner.network.StudyPartnerApplication;

public class StudyPartnerInitialActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StudyPartnerApplication app = StudyPartnerApplication.initializeStudyPartnerASAPApp(this);

        // launch real first activity
        this.finish();
        Intent intent = new Intent(this, StudyPartnerActivity.class);
        this.startActivity(intent);
    }
}
