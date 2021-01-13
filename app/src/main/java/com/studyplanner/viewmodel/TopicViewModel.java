package com.studyplanner.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.studyplanner.repository.TopicRepository;
import com.studyplanner.data.entity.Topic;

import java.util.List;

public class TopicViewModel extends AndroidViewModel {

    private TopicRepository mRepository;

    private final LiveData<List<Topic>> mAllTopics;

    public TopicViewModel (Application application) {
        super(application);
        mRepository = new TopicRepository(application);
        mAllTopics = mRepository.getAll();
    }

    public Topic get(long id) {
        return mRepository.get(id);
    }

    public LiveData<List<Topic>> getAll() {
        return mAllTopics;
    }

    public LiveData<List<Topic>> getAllFromToday() {
        return mRepository.getAllFromToday();
    }

    public LiveData<List<Topic>> getAllFutureTopics() {
        return mRepository.getAllFutureTopics();
    }

    public void insert(Topic topic) {
        mRepository.insert(topic);
    }

    public void update(Topic topic) {
        mRepository.update(topic);
    }

    public void delete(Topic topic) {
        mRepository.delete(topic);
    }
}
