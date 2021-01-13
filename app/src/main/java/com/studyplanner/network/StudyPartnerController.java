package com.studyplanner.network;

import android.util.Log;
import android.widget.Toast;

import com.studyplanner.R;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;
import com.studyplanner.gui.studypartner.StudyPartnerActivity;
import com.studyplanner.viewmodel.SubjectViewModel;
import com.studyplanner.viewmodel.TopicViewModel;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.studyplanner.network.StudyPartnerApplication.ASAP_APPNAME;

public class StudyPartnerController implements NetworkControllerInterface {

    private StudyPartnerActivity activity;

    private SubjectViewModel svm;
    private TopicViewModel tvm;

    public StudyPartnerController(StudyPartnerActivity activity, SubjectViewModel svm, TopicViewModel tvm) {
        this.activity = activity;
        this.svm = svm;
        this.tvm = tvm;
    }

    public void sendTopics(HashMap<Topic, Subject> topics, CharSequence receiverId) throws ASAPException, IOException {
        CharSequence uri = "studyPartner://" + receiverId;

        Collection<CharSequence> recipients = new ArrayList<>();
        recipients.add(receiverId);
        activity.createClosedASAPChannel(ASAP_APPNAME, uri, recipients);
        for (Map.Entry<Topic, Subject> entry : topics.entrySet()) {
            byte[] data = Serializer.serializeData(entry.getKey(), entry.getValue());

            try {
                activity.sendASAPMessage(ASAP_APPNAME, uri, data, true);
            } catch (ASAPException e) {
                Toast.makeText(activity.getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
                Log.d(StudyPartnerController.class.getSimpleName(), "Could not send topics.");
            }
        }
        Toast.makeText(activity.getApplicationContext(), "Topics sent to study partner.", Toast.LENGTH_LONG).show();
    }

    public void receiveTopics(ASAPMessages asapMessages) throws IOException {
        Iterator<byte[]> iterator = asapMessages.getMessages();
        while (iterator.hasNext()) {
            byte[] data = iterator.next();
            HashMap<Topic, Subject> deserializedData = Serializer.deserializeData(data);
            this.saveReceivedData(deserializedData);
        }
        activity.finish();
    }

    protected void saveReceivedData(HashMap<Topic, Subject> data) {
        for (Map.Entry<Topic, Subject> entry : data.entrySet()) {
            Topic topic = entry.getKey();
            svm.getAll().observe(activity, subjects -> {
                // look up if this subject exists, else create a new one
                long subjectId = this.getExistingSubjectIdByName(subjects, entry.getValue().getName());
                if (subjectId == -1) {
                    subjectId = svm.insert(entry.getValue());
                    Log.d(StudyPartnerController.class.getSimpleName(), "+++++ Inserted new subject " + entry.getValue().getName());
                }
                topic.setSubject(subjectId);
                tvm.insert(topic);
                String msg = "Saved new topic \"" + topic.getName() + "\" from study partner.";
                Log.d(StudyPartnerController.class.getSimpleName(), msg);
                Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            });
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
