package com.studyplanner.gui.studypartner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.studyplanner.R;
import com.studyplanner.data.entity.Subject;
import com.studyplanner.data.entity.Topic;
import com.studyplanner.network.NetworkControllerInterface;
import com.studyplanner.network.StudyPartnerController;
import com.studyplanner.viewmodel.SubjectViewModel;
import com.studyplanner.viewmodel.TopicViewModel;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.asap.apps.ASAPMessageReceivedListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import static com.studyplanner.network.StudyPartnerApplication.ASAP_APPNAME;

public class StudyPartnerActivity extends StudyPartnerRootActivity {
    public static class StudyPartnerMessageReceivedListener implements ASAPMessageReceivedListener {
        private NetworkControllerInterface studyPartnerController;

        public StudyPartnerMessageReceivedListener(NetworkControllerInterface controller) {
            studyPartnerController = controller;
        }

        @Override
        public void asapMessagesReceived(ASAPMessages asapMessages) throws IOException {
            Log.d(this.getClass().getSimpleName(), "asapMessageReceived");
            try {
                studyPartnerController.receiveTopics(asapMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    SubjectViewModel svm;
    TopicViewModel tvm;

    private StudyPartnerController studyPartnerController;
    private ASAPMessageReceivedListener messageReceivedListener;
    private HashMap<Topic, Subject> topicsToSend = new HashMap<>();

    private TextView peerListTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_partner);

        svm = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(SubjectViewModel.class);
        tvm = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(TopicViewModel.class);
        studyPartnerController = new StudyPartnerController(this, svm, tvm);

        // button onclicklisteners
        View buttonStartConnection = findViewById(R.id.button_start_connection);
        View buttonStopConnection = findViewById(R.id.button_stop_connection);

        buttonStartConnection.setOnClickListener(view -> {
            Log.d(this.getLogStart(), "Start bluetooth connection button pressed.");
            super.startBluetooth();
        });

        buttonStopConnection.setOnClickListener(view -> {
            Log.d(this.getLogStart(), "Stop bluetooth connection button pressed.");
            super.stopBluetooth();
        });

        // set listener to get informed about newly arrived messages
        messageReceivedListener = new StudyPartnerMessageReceivedListener(studyPartnerController);
        this.getASAPApplication().addASAPMessageReceivedListener(
                ASAP_APPNAME, messageReceivedListener);

        this.setTopicList();

        peerListTextView = findViewById(R.id.text_peers_list);
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("My ID: ");
        sb.append(this.getASAPApplication().getOwnerID());
        peerListTextView.setText(sb.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getASAPApplication().removeASAPMessageReceivedListener(
                ASAP_APPNAME, messageReceivedListener);
    }

    /**
     * Show checkboxes for each topic and set the according onClickListener to put the selected
     * topics into the topicsReceived HashMap.
     */
    private void setTopicList() {
        tvm.getAll().observe(this, topics -> {
            TextView title = findViewById(R.id.title_select_topics);
            if (topics.size() == 0) {
                title.setText(R.string.no_topics);
            } else {
                LinearLayout layout = findViewById(R.id.show_topics_layout);
                title.setText(R.string.title_select_topics);
                for (Topic topic : topics) {
                    Subject subject = svm.get(topic.getSubject());
                    String text;
                    if (subject != null) {
                        text = "[" + subject.getName() + "] ";
                        text += topic.getName();
                        SimpleDateFormat df = new SimpleDateFormat("E, dd.MM.yy", Locale.ENGLISH);
                        df.setTimeZone(TimeZone.getTimeZone("UTC"));
                        text += " | " + df.format(new Date(topic.getDate()));
                        CheckBox cb = new CheckBox(this);
                        cb.setTextSize(18);
                        cb.setText(text);
                        layout.addView(cb);
                        cb.setOnClickListener(view -> {
                            if (cb.isChecked()) {
                                topicsToSend.put(topic, subject);
                            } else {
                                topicsToSend.remove(topic);
                            }
                        });
                    } else {
                        Toast.makeText(this.getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void asapNotifyOnlinePeersChanged(Set<CharSequence> onlinePeerList) {
        super.asapNotifyOnlinePeersChanged(onlinePeerList);
        LinearLayout peerListLayout = findViewById(R.id.show_recipients_layout);

        StringBuilder sb = new StringBuilder();
        sb.append(peerListTextView.getText());
        sb.append("\n");

        if(onlinePeerList == null || onlinePeerList.size() < 1) {
            sb.append(R.string.peers_title);
            peerListTextView.setText(sb.toString());
            peerListLayout.setVisibility(View.GONE);
        } else {
            sb.append("\n");
            sb.append("Click on the ID of your study partner to send selected topics:");
            peerListTextView.setText(sb.toString());

            for (CharSequence peer : onlinePeerList) {
                Button button = new Button(this);
                button.setText(peer);
                button.setOnClickListener(view -> {
                    try {
                        studyPartnerController.sendTopics(topicsToSend, peer);
                        this.finish();
                    } catch (ASAPException | IOException e) {
                        e.printStackTrace();
                    }
                });
                peerListLayout.addView(button);
            }
        }
        peerListTextView.refreshDrawableState();
        peerListLayout.refreshDrawableState();
    }

    @Override
    public void asapNotifyBTEnvironmentStarted() {
        super.asapNotifyBTEnvironmentStarted();
        Log.d(this.getLogStart(), "got notified: bluetooth on");
        Toast.makeText(this, "bluetooth on", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void asapNotifyBTEnvironmentStopped() {
        super.asapNotifyBTEnvironmentStopped();
        Log.d(this.getLogStart(), "got notified: bluetooth off");
        Toast.makeText(this, "bluetooth off", Toast.LENGTH_SHORT).show();
    }
}