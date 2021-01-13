package com.studyplanner.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "topic", foreignKeys = {
        @ForeignKey(entity = Subject.class,
                parentColumns = "subjectId",
                childColumns = "subject",
                onDelete = ForeignKey.CASCADE)
}, indices = {@Index(value = "subject")})
public class Topic implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long topicId;

    private String name;

    private long date;

    private long subject;

    private int duration;

    private boolean completed;

    // constructor with date as long
    @Ignore
    public Topic(String name, long date, long subject, int duration) {
        // we only need a date without time (seconds)
        date = date - (date % 86400000);

        this.name = name;
        this.date = date;
        this.subject = subject;
        this.duration = duration;
        this.completed = false;
    }

    public Topic() {}

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getSubject() {
        return subject;
    }

    public void setSubject(long subject) {
        this.subject = subject;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Ignore
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return (topicId == topic.topicId &&
                Objects.equals(name, topic.name));
    }

    @Ignore
    @Override
    public int hashCode() {
        return Objects.hash(topicId);
    }

    @NonNull
    @Ignore
    @Override
    public String toString() {
        return name;
    }
}

