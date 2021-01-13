package com.studyplanner.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "subject")
public class Subject implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long subjectId;

    private String name;

    private long examDate;

    @Ignore
    public Subject(String name, long date) {
        this.name = name;
        // we only need a date without time (seconds)
        date = date - (date % 86400000);
        this.examDate = date;
    }

    public Subject() {}

    public long getSubjectId() {
        return this.subjectId;
    }

    public String getName() {
        return name;
    }

    public long getExamDate() {
        return examDate;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExamDate(long examDate) {
        this.examDate = examDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return subjectId == subject.subjectId &&
                Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @NonNull
    @Ignore
    @Override
    public String toString() {
        return name;
    }
}
