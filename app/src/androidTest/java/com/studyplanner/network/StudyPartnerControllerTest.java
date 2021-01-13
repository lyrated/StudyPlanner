package com.studyplanner.network;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.studyplanner.data.AppRoomDatabase;
import com.studyplanner.data.LiveDataTestUtil;
import com.studyplanner.data.dao.SubjectDao;
import com.studyplanner.data.dao.TopicDao;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;
import com.studyplanner.gui.studypartner.StudyPartnerActivity;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.apps.ASAPMessageReceivedListener;
import net.sharksystem.asap.apps.ASAPSimplePeer;
import net.sharksystem.asap.apps.mock.ASAPPeerMock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.studyplanner.network.NetworkControllerTestUtil.APP_NAME;

@RunWith(AndroidJUnit4.class)
public class StudyPartnerControllerTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private AppRoomDatabase mDatabase;
    private SubjectDao mSubjectDao;
    private TopicDao mTopicDao;

    private Topic topic;
    private Subject subject;

    private static final String T_NAME = "Testing";
    private static final long T_DATE = new Date(1611792000000L).getTime();
    private static final int T_DURATION = 60;
    private static final String S_NAME = "GMA";
    private static final long S_DATE = new Date(1611792000000L).getTime();

    @Before
    public void setUp() throws Exception {
        // database set up instead of using viewmodels
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AppRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        mSubjectDao = mDatabase.subjectDao();
        mTopicDao = mDatabase.topicDao();

        subject = new Subject(S_NAME, S_DATE);
        subject.setSubjectId(1);
        topic = new Topic(T_NAME, T_DATE, subject.getSubjectId(), T_DURATION);
        topic.setTopicId(1);
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }

    @Test
    public void sendASAPDataTest() throws IOException, ASAPException, InterruptedException {
        List<Topic> t = LiveDataTestUtil.getValue(mTopicDao.getAll());
        List<Subject> s = LiveDataTestUtil.getValue(mSubjectDao.getAll());
        Assert.assertEquals(0, s.size());
        Assert.assertEquals(0, t.size());

        // ALICE
        ASAPPeerMock aliceMock = new ASAPPeerMock("Alice");
        ASAPSimplePeer alicePeer = aliceMock;

        // BOB
        ASAPPeerMock bobMock = new ASAPPeerMock("Bob");
        ASAPSimplePeer bobPeer = bobMock;

        // controllers
        NetworkControllerTestUtil controller = new NetworkControllerTestUtil(aliceMock, mSubjectDao, mTopicDao);
        NetworkControllerTestUtil bobController = new NetworkControllerTestUtil(bobMock, mSubjectDao, mTopicDao);

        // alice sends topics
        HashMap<Topic, Subject> data = new HashMap<>();
        data.put(topic, subject);
        controller.sendTopics(data, bobMock.getPeerName());

        // message received listeners
        ASAPMessageReceivedListener messageReceivedListener = new StudyPartnerActivity.StudyPartnerMessageReceivedListener(controller);
        aliceMock.addASAPMessageReceivedListener(APP_NAME, messageReceivedListener);

        ASAPMessageReceivedListener bobMessageReceivedListener = new StudyPartnerActivity.StudyPartnerMessageReceivedListener(bobController);
        bobMock.addASAPMessageReceivedListener(APP_NAME, bobMessageReceivedListener);

        // simulate ASAP encounter
        Thread.sleep(50);
        aliceMock.startEncounter(bobMock);

        Thread.sleep(300);

        bobMock.stopEncounter(aliceMock);

        Thread.sleep(300);

        t = LiveDataTestUtil.getValue(mTopicDao.getAll());
        s = LiveDataTestUtil.getValue(mSubjectDao.getAll());
        Assert.assertEquals(1, t.size());
        Assert.assertEquals(T_NAME, t.get(0).getName());
        Assert.assertEquals(T_DATE, t.get(0).getDate());
        Assert.assertEquals(T_DURATION, t.get(0).getDuration());
        Assert.assertEquals(S_NAME, s.get(0).getName());
        Assert.assertEquals(S_DATE, s.get(0).getExamDate());
    }

    @Test
    public void getExistingSubjectIdByNameTest() throws InterruptedException {
        mSubjectDao.insert(subject);
        List<Subject> subjects = LiveDataTestUtil.getValue(mSubjectDao.getAll());
        ASAPPeerMock peerMock = new ASAPPeerMock();
        NetworkControllerTestUtil controller = new NetworkControllerTestUtil(peerMock, mSubjectDao, mTopicDao);

        long subjectId = controller.getExistingSubjectIdByName(subjects, S_NAME);

        Assert.assertEquals(1, subjectId);
    }
}
