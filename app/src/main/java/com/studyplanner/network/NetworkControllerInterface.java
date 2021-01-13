package com.studyplanner.network;

import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;

import java.io.IOException;
import java.util.HashMap;

public interface NetworkControllerInterface {

    /**
     * Use ASAP to send topics. The information is serialized first.
     *
     * @param topics A map of topics and their subject they belong to.
     * @param receiverId The ID of the recipient.
     * @throws ASAPException .
     */
    void sendTopics(HashMap<Topic, Subject> topics, CharSequence receiverId) throws ASAPException, IOException;


    /**
     * Use ASAP to receive messages, decode them into topics and save them.
     * If subject name does not exist in the database, a new subject will be created.
     *
     * @param asapMessages The received ASAPMessages
     * @throws IOException .
     */
    void receiveTopics(ASAPMessages asapMessages) throws IOException;
}
