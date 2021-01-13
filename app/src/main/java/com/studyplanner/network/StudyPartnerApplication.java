package com.studyplanner.network;

import android.app.Activity;

import net.sharksystem.asap.ASAP;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.asap.android.apps.ASAPComponentNotYetInitializedException;

import java.util.ArrayList;
import java.util.Collection;

public class StudyPartnerApplication extends ASAPApplication {

    public static final String ASAP_APPNAME = "x-studyPartner";
    private static StudyPartnerApplication instance = null;
    private CharSequence id;

    public static StudyPartnerApplication getASAPApplication() {
        if(StudyPartnerApplication.instance == null) {
            throw new ASAPComponentNotYetInitializedException("ASAP Example Application not yet initialized");
        }
        return StudyPartnerApplication.instance;
    }

    public static StudyPartnerApplication initializeStudyPartnerASAPApp(Activity initialActivity) {
        if(StudyPartnerApplication.instance == null) {
            Collection<CharSequence> formats = new ArrayList<>();
            formats.add(ASAP_APPNAME);

            // create object - set up application side
            StudyPartnerApplication.instance = new StudyPartnerApplication(formats, initialActivity);

            // step 2 - launch ASAPService
            StudyPartnerApplication.instance.startASAPApplication();
        } // else - already initialized - nothing happens.

        return StudyPartnerApplication.instance;
    }

    private StudyPartnerApplication(Collection<CharSequence> formats, Activity initialActivity) {
        super(formats, initialActivity);
        this.id = ASAP.createUniqueID();
    }

    public CharSequence getOwnerID() {
        return this.id;
    }

}
