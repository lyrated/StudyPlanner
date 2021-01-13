package com.studyplanner.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.studyplanner.repository.SubjectRepository;
import com.studyplanner.data.entity.Subject;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {

    private SubjectRepository mRepository;

    private final LiveData<List<Subject>> mAllSubjects;

    public SubjectViewModel (Application application) {
        super(application);
        mRepository = new SubjectRepository(application);
        mAllSubjects = mRepository.getAll();
    }

    public Subject get(long id) {
        return mRepository.get(id);
    }

    public LiveData<List<Subject>> getAll() {
        return mAllSubjects;
    }

    public long insert(Subject subject) {
        return mRepository.insert(subject);
    }

    public void update(Subject subject) {
        mRepository.update(subject);
    }

    public void delete(Subject subject) {
        mRepository.delete(subject);
    }
}
