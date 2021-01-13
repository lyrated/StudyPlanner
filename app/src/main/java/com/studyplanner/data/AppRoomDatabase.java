package com.studyplanner.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.studyplanner.data.dao.SubjectDao;
import com.studyplanner.data.dao.TopicDao;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Subject.class, Topic.class}, version = 2, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract SubjectDao subjectDao();
    public abstract TopicDao topicDao();

    private static volatile AppRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "studyplanner_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
