package mediamine.services;

import mediamine.data.SampleData;
import mediamine.model.Media;
import mediamine.m6.RandomizedQuickSort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Ranking Service. Uses Randomized QuickSort internally — user does NOT pick.
 * Supports sort by Views, Popularity, or (simulated) Search Frequency.
 */
public class RankingService {

    public enum SortBy { VIEWS, POPULARITY, SEARCH_FREQ }

    public static class RankedMedia {
        public final int rank;
        public final Media media;
        public final long sortValue;
        public RankedMedia(int r, Media m, long v) { this.rank = r; this.media = m; this.sortValue = v; }
    }

    public List<RankedMedia> rank(SortBy by, int topK) {
        List<Media> list = new ArrayList<>(SampleData.getSampleMedia());
        long[] key = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Media m = list.get(i);
            switch (by) {
                case VIEWS:   key[i] = m.getViews(); break;
                case POPULARITY: key[i] = m.getPopularity(); break;
                case SEARCH_FREQ:
                    // synthetic search-freq = views*3 + popularity*7 + len(transcript)
                    key[i] = (long) m.getViews() * 3L + (long) m.getPopularity() * 7L
                           + (long) m.getTranscript().length();
                    break;
            }
        }
        // sort ascending by key, then reverse to descending (randomized quicksort in place via index list)
        List<Integer> idx = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) idx.add(i);
        RandomizedQuickSort.sort(idx, Comparator.comparingLong(a -> key[a]));
        List<RankedMedia> out = new ArrayList<>();
        int top = Math.min(topK, list.size());
        for (int i = 0; i < top; i++) {
            int position = list.size() - 1 - i; // descending
            int orig = idx.get(position);
            out.add(new RankedMedia(i + 1, list.get(orig), key[orig]));
        }
        return out;
    }
}
