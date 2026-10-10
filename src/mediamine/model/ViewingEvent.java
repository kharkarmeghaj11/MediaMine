package mediamine.model;

/** A single streaming viewing event: VIEW / PLAY / PAUSE / SEEK / STOP on a media item. */
public class ViewingEvent {
    public static final String VIEW = "VIEW";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String SEEK = "SEEK";
    public static final String STOP = "STOP";

    public final long seqNum;
    public final String mediaId;
    public final String type; // one of VIEW/PLAY/PAUSE/SEEK/STOP
    public final long timestampMs;

    public ViewingEvent(long seqNum, String mediaId, String type, long timestampMs) {
        this.seqNum = seqNum;
        this.mediaId = mediaId;
        this.type = type;
        this.timestampMs = timestampMs;
    }

    @Override public String toString() {
        return String.format("#%05d %-5s %s @ %d", seqNum, type, mediaId, timestampMs);
    }
}
