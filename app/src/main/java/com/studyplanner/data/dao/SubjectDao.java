package com.studyplanner.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.studyplanner.data.entity.Subject;

import java.util.List;

@Dao
public interface SubjectDao {

    @Insert
    long insert(Subject subject);

    @Update
    void update(Subject subject);

    @Delete
    void delete(Subject subject);

    @Query("SELECT * FROM subject WHERE subjectId=:id")
    Subject get(long id);

    @Query("SELECT * FROM subject")
    LiveData<List<Subject>> getAll();
}
