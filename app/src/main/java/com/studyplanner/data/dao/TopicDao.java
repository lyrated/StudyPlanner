package com.studyplanner.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.studyplanner.data.entity.Topic;

import java.util.List;

@Dao
public interface TopicDao {

    @Insert
    void insert(Topic topic);

    @Update
    void update(Topic topic);

    @Delete
    void delete(Topic topic);

    @Query("SELECT * FROM topic WHERE topicId=:id")
    Topic get(long id);

    @Query("SELECT * FROM topic ORDER BY date ASC")
    LiveData<List<Topic>> getAll();

    @Query("SELECT * FROM topic WHERE date(date/1000, 'unixepoch') = date('now') ORDER BY date ASC")
    LiveData<List<Topic>> getAllFromToday();

    @Query("SELECT * FROM topic WHERE date(date/1000, 'unixepoch') > date('now') ORDER BY date ASC")
    LiveData<List<Topic>> getAllAfterToday();
}