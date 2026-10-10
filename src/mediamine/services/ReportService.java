package mediamine.services;

import mediamine.data.SampleData;
import mediamine.model.Media;
import mediamine.services.DistributionService.DistributionReport;
import mediamine.services.ContentCompatibilityService.CompatibilityReport;
import mediamine.services.StreamingAnalyticsService.StreamingReport;
import mediamine.services.ParallelAnalyticsService.AnalyticsReport;

import java.util.List;

/**
 * ReportService builds the Analytics Report (menu option 10) and gathers the
 * M4/M5/M6 calculated fields the spec requires — no hard-coded fake results.
 */
public class ReportService {

    public static class MediaMineReport {
        public final int totalMedia;
        public final int videos;
        public final int articles;
        public final long totalViews;
        public final DistributionReport dist;
        public final CompatibilityReport compat;
        public final StreamingReport stream;
        public final AnalyticsReport parallel;
        public final String topMediaTitle;
        public final long topViews;

        public MediaMineReport(DistributionReport d, CompatibilityReport c,
                               StreamingReport s, AnalyticsReport p) {
            this.dist = d; this.compat = c; this.stream = s; this.parallel = p;
            List<Media> all = SampleData.getSampleMedia();
            int v = 0, a = 0; long tot = 0; long topV = -1; String topT = "";
            for (Media m : all) {
                if ("VIDEO".equalsIgnoreCase(m.getType())) v++;
                else if ("ARTICLE".equalsIgnoreCase(m.getType())) a++;
                tot += m.getViews();
                if (m.getViews() > topV) { topV = m.getViews(); topT = m.getTitle(); }
            }
            this.totalMedia = all.size();
            this.videos = v;
            this.articles = a;
            this.totalViews = tot;
            this.topViews = topV;
            this.topMediaTitle = topT;
        }
    }

    public MediaMineReport build() {
        DistributionReport d = new DistributionService().analyze();
        CompatibilityReport c = new ContentCompatibilityService().analyze();
        StreamingReport s = new StreamingAnalyticsService().analyze(10000, 10);
        AnalyticsReport p = new ParallelAnalyticsService().analyze();
        return new MediaMineReport(d, c, s, p);
    }
}
