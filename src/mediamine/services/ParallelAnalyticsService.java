package mediamine.services;

import mediamine.data.SampleData;
import mediamine.m6.*;
import mediamine.model.Media;
import mediamine.model.ViewingEvent;

import java.util.List;

/**
 * Parallel Analytics:
 *   - Blelloch Scan (prefix sum) for daily views / trends
 *   - Parallel Reduce (ForkJoinPool) for totals (Total Views, Watch Time, Events)
 *   - Miller-Rabin for large ID prime-test utility
 *   - Brent's Theorem for an educational T_p estimate
 */
public class ParallelAnalyticsService {

    public static class AnalyticsReport {
        public final long[] dailyViews;   // length = 5: Mon..Fri
        public final long[] cumulativeViews; // inclusive scan
        public final long totalViews;
        public final long totalWatchMinutes;
        public final long totalEvents;
        public final double averageViews;

        // Miller-Rabin stats
        public final int identifiersAnalyzed;
        public final int largeValuesProcessed;
        public final int primeCandidates;
        public final int compositeValues;

        // Brent
        public final BrentAnalysis brent;

        public AnalyticsReport(long[] dv, long[] cum, long tv, long twm, long te,
                               double av, int ids, int larges, int primes, int comps,
                               BrentAnalysis b) {
            this.dailyViews = dv;
            this.cumulativeViews = cum;
            this.totalViews = tv;
            this.totalWatchMinutes = twm;
            this.totalEvents = te;
            this.averageViews = av;
            this.identifiersAnalyzed = ids;
            this.largeValuesProcessed = larges;
            this.primeCandidates = primes;
            this.compositeValues = comps;
            this.brent = b;
        }
    }

    public AnalyticsReport analyze() {
        long[] daily = {100L, 150L, 200L, 250L, 300L};

        BlellochScan bs = new BlellochScan();
        long[] cum = bs.inclusiveScan(daily);

        // ParalleReduce total views across entire library + totals
        List<Media> all = SampleData.getSampleMedia();
        long[] arrViews = new long[all.size()];
        for (int i = 0; i < all.size(); i++) {
            arrViews[i] = all.get(i).getViews();
        }
        long totalViews = ParallelReduce.sum(arrViews);
        long totalMedia = all.size();
        double avg = (totalMedia == 0) ? 0 : (1.0 * totalViews / totalMedia);

        List<ViewingEvent> events = SampleData.generateViewingEvents(10000);
        long[] eventCounts = new long[events.size()];
        long[] watchMinutes = new long[events.size()];
        for (int i = 0; i < events.size(); i++) {
            eventCounts[i] = 1L;
            watchMinutes[i] = ViewingEvent.PLAY.equals(events.get(i).type) ? 8L : 0L;
        }
        long totalEvents = ParallelReduce.sum(eventCounts);
        long totalWatchMinutes = ParallelReduce.sum(watchMinutes);

        // Miller-Rabin: prime test on a list of large IDs derived from each media (popularity*100003 + hash of id)
        MillerRabin mr = new MillerRabin();
        int idsN = all.size();
        int primes = 0, composites = 0;
        for (Media m : all) {
            long hash = (m.getId().hashCode() & 0xFFFFFFFFL);
            long n = Math.abs(((long) m.getPopularity()) * 100003L + hash + 1);
            if (n < 2) n = 7;
            if (mr.isPrime(n)) primes++; else composites++;
        }

        // Brent analysis:
        // Educational estimate for processing the same generated 10,000-event stream.
        // This is not a precise wall-clock benchmark; span is a model input, not measured time.
        // Processors = Runtime.getRuntime().availableProcessors()
        long W = totalEvents;
        long S = 500L;
        int P = Math.max(1, Runtime.getRuntime().availableProcessors());
        BrentAnalysis b = new BrentAnalysis(W, S, P);

        return new AnalyticsReport(daily, cum, totalViews, totalWatchMinutes,
            totalEvents, avg, idsN, idsN, primes, composites, b);
    }
}
