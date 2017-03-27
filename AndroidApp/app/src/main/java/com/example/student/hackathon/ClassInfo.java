package com.example.student.hackathon;

import java.io.Serializable;

/**
 * Created by student on 3/25/2017.
 */

public class ClassInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private boolean shareSms;
    private boolean shareVideo;
    private boolean shareVoice;

    public ClassInfo(String id, String name, boolean shareSms, boolean shareVideo, boolean shareVoice)
    {
        this.id = id;
        this.name = name;
        this.shareSms = shareSms;
        this.shareVideo = shareVideo;
        this.shareVoice = shareVoice;
    }

    public String getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isShareSms() {
        return shareSms;
    }

    public void setShareSms(boolean shareSms) {
        this.shareSms = shareSms;
    }

    public boolean isShareVideo() {
        return shareVideo;
    }

    public void setShareVideo(boolean shareVideo) {
        this.shareVideo = shareVideo;
    }

    public boolean isShareVoice() {
        return shareVoice;
    }

    public void setShareVoice(boolean shareVoice) {
        this.shareVoice = shareVoice;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", shareSms=" + shareSms +
                ", shareVideo=" + shareVideo +
                ", shareVoice=" + shareVoice +
                '}';
    }
}
