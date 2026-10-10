package mediamine.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Media {
    private String id;
    private String title;
    private String type;
    private String category;
    private String transcript;
    private int duration;
    private int views;
    private int popularity;            // derived score (default: views / 10)
    private Set<String> channelEligibility; // channel names this media can run on

    public Media(String id, String title, String type, String category,
                 String transcript, int duration, int views) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.category = category;
        this.transcript = transcript;
        this.duration = duration;
        this.views = views;
        this.popularity = Math.max(1, views / 10);
        this.channelEligibility = new LinkedHashSet<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getViews() { return views; }
    public void setViews(int views) {
        this.views = views;
        if (this.popularity <= 0) this.popularity = Math.max(1, views / 10);
    }

    public int getPopularity() { return popularity; }
    public void setPopularity(int p) { this.popularity = p; }

    public Set<String> getChannelEligibility() {
        return Collections.unmodifiableSet(channelEligibility);
    }
    public void addChannelEligibility(String channelName) {
        if (channelName != null && !channelName.isEmpty()) this.channelEligibility.add(channelName);
    }

    @Override
    public String toString() {
        return id + " - " + title + " (" + type + ", " + category + ")";
    }
}
