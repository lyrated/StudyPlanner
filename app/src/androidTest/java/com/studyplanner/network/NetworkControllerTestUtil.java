package com.studyplanner.network;

import android.util.Log;

import com.studyplanner.data.LiveDataTestUtil;
import com.studyplanner.data.dao.SubjectDao;
import com.studyplanner.data.dao.TopicDao;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.asap.apps.ASAPSimplePeer;
import net.sharksystem.asap.apps.mock.ASAPPeerMock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NetworkControllerTestUtil implements NetworkControllerInterface {

    public static final CharSequence APP_NAME = "application/x-controllerTest";
    public static final CharSequence URI = "network://test";

    private ASAPPeerMock peerMock;
    private ASAPSimplePeer simplePeer;

    private SubjectDao mSubjectDao;
    private TopicDao mTopicDao;

    public NetworkControllerTestUtil(ASAPPeerMock peerMock, SubjectDao subjectDao, TopicDao topicDao) {
        this.peerMock = peerMock;
        this.simplePeer = peerMock;

        mSubjectDao = subjectDao;
        mTopicDao = topicDao;
    }

    public void sendTopics(HashMap<Topic, Subject> topics, CharSequence receiverId) throws ASAPException, IOException {
        for (Map.Entry<Topic, Subject> entry : topics.entrySet()) {
            byte[] data = Serializer.serializeData(entry.getKey(), entry.getValue());

            try {
                simplePeer.sendASAPMessage(APP_NAME, URI, data);
                Log.d(NetworkControllerTestUtil.class.getSimpleName(), "Sending topics");
            } catch (ASAPException e) {
                Log.d(NetworkControllerTestUtil.class.getSimpleName(), "Could not send topics.");
            }
        }
    }

    /**
     * Use ASAP to receive messages, decode them into topics and save them.
     * If subject name does not exist in the database, a new subject will be created.
     *
     * @param asapMessages .
     * @throws IOException .
     */
    public void receiveTopics(ASAPMessages asapMessages) throws IOException {
        Iterator<byte[]> iterator = asapMessages.getMessages();
        while (iterator.hasNext()) {
            byte[] data = iterator.next();
            HashMap<Topic, Subject> deserializedData = Serializer.deserializeData(data);
            try {
                this.saveReceivedData(deserializedData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    protected void saveReceivedData(HashMap<Topic, Subject> data) throws InterruptedException {
        for (Map.Entry<Topic, Subject> entry : data.entrySet()) {
            Topic topic = entry.getKey();
            // look up if this subject exists, else create a new one
            List<Subject> subjects = LiveDataTestUtil.getValue(mSubjectDao.getAll());
            long subjectId = this.getExistingSubjectIdByName(subjects, entry.getValue().getName());
            if (subjectId == -1) {
                subjectId = mSubjectDao.insert(entry.getValue());
                Log.d(StudyPartnerController.class.getSimpleName(), "+++++ Inserted new subject " + entry.getValue().getName());
            }
            topic.setSubject(subjectId);
            mTopicDao.insert(topic);
            String msg = "Saved new topic \"" + topic.getName() + "\" from study partner.";
            Log.d(StudyPartnerController.class.getSimpleName(), msg);
        }
    }

    protected long getExistingSubjectIdByName(List<Subject> subjects, String name) {
        for (Subject s : subjects) {
            if (s.getName().toLowerCase().equals(name.toLowerCase())) {
                return s.getSubjectId();
            }
        }
        return -1;
    }
}
