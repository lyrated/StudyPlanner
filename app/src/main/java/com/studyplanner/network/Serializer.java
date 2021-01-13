package com.studyplanner.network;


import android.util.Log;

import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Serializer {
    /**
     * Convert the topic and the subject it belongs into one string which can be sent via ASAP.
     *
     * @param topic   .
     * @param subject .
     * @return byte[] in this order: TOPICNAME > TOPICDATE > TOPICDURATION > SUBJECTNAME > SUBJECTDATE
     */
    public static byte[] serializeData(Topic topic, Subject subject) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream daos = new DataOutputStream(baos);

        daos.writeUTF(topic.getName());
        daos.writeLong(topic.getDate());
        daos.writeInt(topic.getDuration());
        daos.writeUTF(subject.getName());
        daos.writeLong(subject.getExamDate());

        return baos.toByteArray();
    }

    /**
     * Convert received String into a list of the topics that should be saved.
     *
     * @param data byte[] in this order: TOPICNAME > TOPICDATE > TOPICDURATION > SUBJECTNAME > SUBJECTDATE
     * @return HashMap of Topics and Subjects
     */
    public static HashMap<Topic, Subject> deserializeData(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dais = new DataInputStream(bais);
        HashMap<Topic, Subject> topicsReceived = new HashMap<>();
        Log.d(StudyPartnerController.class.getSimpleName(), "##### RECEIVED DATA");

        String topicName = dais.readUTF();
        Log.d(StudyPartnerController.class.getSimpleName(), "#### " + topicName);
        long topicDate = dais.readLong();
        int topicDuration = dais.readInt();
        String subjectName = dais.readUTF();
        long subjectDate = dais.readLong();
        Subject subject = new Subject(subjectName, subjectDate);
        Topic newTopic = new Topic(topicName, topicDate, 0, topicDuration);
        topicsReceived.put(newTopic, subject);

        return topicsReceived;
    }
}