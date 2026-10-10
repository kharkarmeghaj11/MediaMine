package mediamine.services;

import mediamine.data.SampleData;
import mediamine.model.Media;
import mediamine.model.ViewingEvent;
import mediamine.m6.*;

import java.util.*;

/**
 * Streaming Analytics:
 *   - generate 10,000 viewing events (SampleData.generateViewingEvents)
 *   - maintain reservoir sample (k=10) of them using ReservoirSampling
 *   - aggregate view counts per media (top media in sample stream)
 */
public class StreamingAnalyticsService {

    public static class StreamingReport {
        public final int totalEvents;
        public final int sampleSize;
        public final List<ViewingEvent> sample;
        public final long totalViewsCount;  // number of VIEW events
        public final long totalWatchTimeMs; // synthetic watch-time estimate
        public final Map<String, Long> mediaToViewCount; // mediaId -> VIEW events
        public final String topMediaId;
        public final String topMediaTitle;

        public StreamingReport(int totalEvents, int sampleSize, List<ViewingEvent> sample,
                               long tvc, long wtMs, Map<String, Long> mvc,
                               String topId, String topTitle) {
            this.totalEvents = totalEvents;
            this.sampleSize = sampleSize;
            this.sample = sample;
            this.totalViewsCount = tvc;
            this.totalWatchTimeMs = wtMs;
            this.mediaToViewCount = mvc;
            this.topMediaId = topId;
            this.topMediaTitle = topTitle;
        }
    }

    public StreamingReport analyze(int eventCount, int reservoirK) {
        List<ViewingEvent> events = SampleData.generateViewingEvents(eventCount);
        ReservoirSampling<ViewingEvent> rs = new ReservoirSampling<>(reservoirK);
        long views = 0;
        long watch = 0;
        Map<String, Long> mvc = new LinkedHashMap<>();
        for (ViewingEvent e : events) {
            rs.accept(e);
            if (ViewingEvent.VIEW.equals(e.type)) {
                views++;
                mvc.merge(e.mediaId, 1L, Long::sum);
            }
            if (ViewingEvent.PLAY.equals(e.type)) {
                // assume 8-minute avg watch = 480000ms per PLAY
                long w = 480_000L;
                watch += w;
            }
        }
        // top media by VIEW events
        String topId = null;
        long topC = -1;
        for (Map.Entry<String, Long> en : mvc.entrySet()) {
            if (en.getValue() > topC) { topC = en.getValue(); topId = en.getKey(); }
        }
        String topTitle = "(none)";
        for (Media m : SampleData.getSampleMedia()) {
            if (m.getId().equals(topId)) { topTitle = m.getTitle(); break; }
        }
        return new StreamingReport(events.size(), rs.sampleSize(), rs.getSample(),
                                   views, watch, mvc, topId, topTitle);
    }
}
