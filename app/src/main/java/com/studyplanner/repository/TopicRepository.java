package com.studyplanner.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.studyplanner.data.AppRoomDatabase;
import com.studyplanner.data.entity.Topic;
import com.studyplanner.data.dao.TopicDao;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TopicRepository {

    private TopicDao topicDao;
    private LiveData<List<Topic>> allTopics;

    public TopicRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);

        topicDao = db.topicDao();
        allTopics = topicDao.getAll();
    }

    public LiveData<List<Topic>> getAll() {
        return allTopics;
    }

    public Topic get(long topicId) {
        Topic topic = null;

        Future<Topic> future = AppRoomDatabase.databaseWriteExecutor.submit(() -> topicDao.get(topicId));
        try {
            topic = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return topic;
    }

    /**
     * @return All topics from today's date.
     */
    public LiveData<List<Topic>> getAllFromToday() {
        return topicDao.getAllFromToday();
    }

    /**
     * @return All future topics after today's date.
     */
    public LiveData<List<Topic>> getAllFutureTopics() {
        return topicDao.getAllAfterToday();
    }

    public void insert(Topic topic) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            topicDao.insert(topic);
        });
    }

    public void update(Topic topic) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            topicDao.update(topic);
        });
    }

    public void delete(Topic topic) {
        AppRoomDatabase.databaseWriteExecutor.execute(() -> {
            topicDao.delete(topic);
        });
    }

}
