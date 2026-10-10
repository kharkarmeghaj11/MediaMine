package mediamine.model;

/** Distribution channel (e.g. Technology Channel, Sports Channel). */
public class Channel {
    public final String id;
    public final String name;
    public final long minViewsPerWeek; // minimum throughput/audience requirement

    public Channel(String id, String name, long minViewsPerWeek) {
        this.id = id;
        this.name = name;
        this.minViewsPerWeek = minViewsPerWeek;
    }
}
