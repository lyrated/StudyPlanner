package com.studyplanner.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.studyplanner.data.AppRoomDatabase;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.dao.SubjectDao;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SubjectRepository {

    private SubjectDao subjectDao;
    private LiveData<List<Subject>> allSubjects;

    public SubjectRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);

        subjectDao = db.subjectDao();
        allSubjects = subjectDao.getAll();
    }

    public Subject get(long subjectId) {
        Subject subject = null;

        Future<Subject> future = AppRoomDatabase.databaseWriteExecutor.submit(() -> subjectDao.get(subjectId));
        try {
            subject = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return subject;
    }

    public LiveData<List<Subject>> getAll() {
        return allSubjects;
    }

    public long insert(Subject subject) {
        long rowId = 0;

        Future<Long> future = AppRoomDatabase.databaseWriteExecutor.submit(() -> subjectDao.insert(subject));
        try {
            rowId = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return rowId;
    }

    public void update(Subject subject) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            subjectDao.update(subject);
        });
    }

    public void delete(Subject subject) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            subjectDao.delete(subject);
        });
    }
}

