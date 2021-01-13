package com.studyplanner.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.studyplanner.data.dao.SubjectDao;
import com.studyplanner.data.dao.TopicDao;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;

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
public class TopicDataTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private AppRoomDatabase mDatabase;
    private TopicDao mTopicDao;
    private long mSubjectId;

    private static final String TEST_NAME = "Math homework";

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
        SubjectDao subjectDao = mDatabase.subjectDao();
        mSubjectId = subjectDao.insert(newSubject);

        mTopicDao = mDatabase.topicDao();
        Topic topic = new Topic(TEST_NAME, new Date().getTime(), mSubjectId, 60);
        mTopicDao.insert(topic);
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getAllTopicsTest() throws InterruptedException {
        List<Topic> topics = LiveDataTestUtil.getValue(mTopicDao.getAll());

        Assert.assertEquals(1, topics.size());
    }

    @Test
    public void getTopicByIdTest() throws InterruptedException {
        Topic topic = LiveDataTestUtil.getValue(mTopicDao.getAll()).get(0);
        long id = topic.getTopicId();

        Assert.assertEquals(topic, mTopicDao.get(id));
    }

    @Test
    public void getTopicByIdTestNegative() throws InterruptedException {
        Assert.assertNull(mTopicDao.get(123456));
    }

    @Test
    public void getAllTopicsFromTodayTest() throws InterruptedException {
        List<Topic> allTopicsFromToday = LiveDataTestUtil.getValue(mTopicDao.getAllFromToday());

        Assert.assertEquals(1, allTopicsFromToday.size());
        Assert.assertEquals(TEST_NAME, allTopicsFromToday.get(0).getName());
    }

    @Test
    public void getAllTopicsFromTodayTestNegative() throws InterruptedException {
        Topic oldTopic = new Topic("Geometry", 1609455600000L, mSubjectId, 60);
        mTopicDao.insert(oldTopic);
        List<Topic> allTopicsFromToday = LiveDataTestUtil.getValue(mTopicDao.getAllFromToday());

        Assert.assertNotEquals(2, allTopicsFromToday.size());
        Assert.assertNotEquals(0, allTopicsFromToday.size());
        Assert.assertNotEquals("Geometry", allTopicsFromToday.get(0).getName());
    }

    @Test
    public void getAllTopicsAfterTodayTest() throws InterruptedException {
        Topic futureTopic = new Topic("Algebra", 1611792000000L, mSubjectId, 60);
        mTopicDao.insert(futureTopic);
        List<Topic> allTopicsAfterToday = LiveDataTestUtil.getValue(mTopicDao.getAllAfterToday());

        Assert.assertEquals(1, allTopicsAfterToday.size());
        Assert.assertEquals("Algebra", allTopicsAfterToday.get(0).getName());
    }

    @Test
    public void getAllTopicsAfterTodayTestNegative() throws InterruptedException {
        Topic futureTopic = new Topic("Algebra", 1611792000000L, mSubjectId, 60);
        mTopicDao.insert(futureTopic);
        List<Topic> allTopicsAfterToday = LiveDataTestUtil.getValue(mTopicDao.getAllAfterToday());

        Assert.assertNotEquals(2, allTopicsAfterToday.size());
        Assert.assertNotEquals(0, allTopicsAfterToday.size());
    }
}