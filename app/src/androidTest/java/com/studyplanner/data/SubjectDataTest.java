package com.studyplanner.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.studyplanner.data.dao.SubjectDao;
import com.studyplanner.data.entity.Subject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SubjectDataTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private AppRoomDatabase mDatabase;
    private SubjectDao mSubjectDao;

    @Before
    public void setUp() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AppRoomDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        Subject newSubject = new Subject("Maths", new Date().getTime());
        mSubjectDao = mDatabase.subjectDao();
        mSubjectDao.insert(newSubject);
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getAllSubjectsTest() throws InterruptedException {
        List<Subject> subjects = LiveDataTestUtil.getValue(mSubjectDao.getAll());

        Assert.assertEquals(1, subjects.size());
    }

    @Test
    public void getAllSubjectsTestNegative() throws InterruptedException {
        List<Subject> subjects = LiveDataTestUtil.getValue(mSubjectDao.getAll());

        Assert.assertNotEquals(0, subjects.size());
    }

    @Test
    public void getSubjectByIdTest() throws InterruptedException {
        List<Subject> subjects = LiveDataTestUtil.getValue(mSubjectDao.getAll());
        long subjectId = subjects.get(0).getSubjectId();

        Assert.assertEquals(subjects.get(0), mSubjectDao.get(subjectId));
    }

    @Test
    public void getSubjectByIdTestNegative() {
        Assert.assertNull(mSubjectDao.get(12345));
    }

}
