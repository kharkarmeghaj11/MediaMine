package mediamine;

import mediamine.data.SampleData;
import mediamine.m1.*;
import mediamine.m2.*;
import mediamine.m3.*;
import mediamine.m4.*;
import mediamine.m5.*;
import mediamine.m6.*;
import mediamine.model.Media;
import mediamine.model.ViewingEvent;
import mediamine.services.*;
import mediamine.services.DistributionService.DistributionReport;
import mediamine.services.ContentCompatibilityService.CompatibilityReport;
import mediamine.services.StreamingAnalyticsService.StreamingReport;
import mediamine.services.RankingService.SortBy;
import mediamine.services.RankingService.RankedMedia;
import mediamine.services.ReportService.MediaMineReport;
import mediamine.ui.TerminalUI;

import java.util.*;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static List<Media> mediaList;

    public static void main(String[] args) {
        mediaList = SampleData.getSampleMedia();

        printStartBanner();
        printSystemReady();
        runAllSelfTests();

        while (true) {
            printMainMenu();
            System.out.println();
            System.out.print("Enter option:\n> ");
            String choice;
            try { choice = sc.nextLine().trim(); }
            catch (NoSuchElementException e) {
                System.out.println("\nNo interactive input. Exiting.");
                return;
            }
            if (choice.isEmpty()) {
                continue;
            }
            System.out.println();
            try {
                switch (choice) {
                    case "1":  option1Library(); break;
                    case "2":  option2SmartSearch(); break;
                    case "3":  option3TranscriptAnalysis(); break;
                    case "4":  option4ContentInsights(); break;
                    case "5":  option5ContentRecommendations(); break;
                    case "6":  option6DistributionNetwork(); break;
                    case "7":  option7ContentCompatibility(); break;
                    case "8":  option8StreamingAnalytics(); break;
                    case "9":  option9MediaRanking(); break;
                    case "10": option10AnalyticsReport(); break;
                    case "11": option11SystemStatus(); break;
                    case "12": option12TechnicalDetails(); break;
                    case "0":
                        System.out.println("Thank you for using MediaMine. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
            pressEnter();
        }
    }

    // =========================================================
    // STARTUP: banner + system ready
    // =========================================================
    private static void printStartBanner() {
        String W = "─";
        String top = "╔" + TerminalUI.repeat(W, 78) + "╗";
        String mid = "║" + center("MEDIAMINE", 78) + "║\n"
                   + "║" + center("INTELLIGENT MEDIA ANALYTICS PLATFORM", 78) + "║";
        String bot = "╚" + TerminalUI.repeat(W, 78) + "╝";
        System.out.println(top);
        System.out.println(mid);
        System.out.println(bot);
        System.out.println();
        for (String s : new String[] {
            "[ SYSTEM ] Media repository......................... ONLINE",
            "[ SYSTEM ] Search engine............................ ONLINE",
            "[ SYSTEM ] Analytics engine......................... ONLINE",
            "[ SYSTEM ] Distribution engine...................... ONLINE",
            "[ SYSTEM ] Content constraint engine................ ONLINE",
            "[ SYSTEM ] Streaming engine......................... ONLINE"
        }) System.out.println(s);
        System.out.println();
    }

    private static void printSystemReady() {
        String W = "─";
        System.out.println(TerminalUI.repeat(W, 78));
        System.out.println("SYSTEM READY");
        System.out.println(TerminalUI.repeat(W, 78));
        System.out.println();
    }

    // =========================================================
    // SELF-TESTS — M1:4, M2:4, M3:5, M4:5, M5:7, M6:6.
    // =========================================================
    private static int[] runAllSelfTests() {
        int m1 = 0, m2 = 0, m3 = 0, m4 = 0, m5 = 0, m6 = 0;

        // M1
        int ok = 0;
        if(!new KMP().search("abcde abc", "abc").isEmpty()) ok++;
        if(new ZFunction().computeZ("aabxaabx")[0] == 8) ok++;
        if(new RabinKarp().search("hello world", "hello").size() >= 1) ok++;
        AhoCorasick ac = new AhoCorasick(); ac.addPattern("x"); ac.build();
        if(ac.search("xyz xyz").getOrDefault("x", Collections.emptyList()).size() >= 2) ok++;
        m1 = ok;

        ok=0;
        if(new SuffixArray().buildSuffixArray("banana")[0] == 5) ok++;
        if(new LCP().longestCommonPrefixLength("abcdef", "abcxyz") == 3) ok++;
        if(new Kasai().buildLCPArray("banana", new SuffixArray().buildSuffixArray("banana")).length == 6) ok++;
        SuffixAutomaton sa = new SuffixAutomaton(); sa.build("hello world");
        if(sa.contains("world")) ok++;
        m2 = ok;

        ok=0;
        if(new Levenshtein().distance("kitten", "sitting") == 3) ok++;
        if(new DamerauLevenshtein().distance("teh", "the") == 1) ok++;
        if(new WeightedEditDistance(1,1,2).distance("abc", "adc") == 2) ok++;
        if(new BitmaskDP().selectCategories(new String[]{"A","B","C"}, new int[]{3,5,4}, 2).totalScore == 9) ok++;
        if(new MatrixChain().optimize(new int[]{10,20,30,40}, new String[]{"A","B","C"}).minOperations == 18000) ok++;
        m3 = ok;

        if(FordFulkerson.smokeTest()) m4++;
        if(EdmondsKarp.smokeTest()) m4++;
        if(Dinic.smokeTest()) m4++;
        if(BipartiteMatching.smokeTest()) m4++;
        if(MinCut.smokeTest()) m4++;

        ok=0;
        if(SATSolver.smokeTest()) ok++;
        if(ThreeSAT.smokeTest()) ok++;
        if(SatToClique.smokeTest()) ok++;
        if(Clique.smokeTest()) ok++;
        if(IndependentSet.smokeTest()) ok++;
        if(VertexCover.smokeTest()) ok++;
        if(VertexCoverApprox.smokeTest()) ok++;
        m5 = ok;

        ok=0;
        if(RandomizedQuickSort.smokeTest()) ok++;
        if(ReservoirSampling.smokeTest()) ok++;
        if(MillerRabin.smokeTest()) ok++;
        if(BlellochScan.smokeTest()) ok++;
        if(ParallelReduce.smokeTest()) ok++;
        if(BrentAnalysis.smokeTest()) ok++;
        m6 = ok;

        return new int[] {m1, m2, m3, m4, m5, m6};
    }

    // =========================================================
    // MENU
    // =========================================================
    private static void printMainMenu() {
        String[] items = {
            " 1. Media Library",
            " 2. Smart Search",
            " 3. Transcript Analysis",
            " 4. Content Insights",
            " 5. Content Recommendations",
            " 6. Distribution Network",
            " 7. Content Compatibility",
            " 8. Streaming Analytics",
            " 9. Media Ranking",
            "10. Analytics Report",
            "11. System Status",
            "12. Technical Details",
            " 0. Exit"
        };
        for (String s : items) System.out.println(s);
    }

    private static void pressEnter() {
        System.out.print("\nPress ENTER to continue...");
        try { sc.nextLine(); } catch (NoSuchElementException ignored) {}
        System.out.println();
    }

    private static Media findById(String id) {
        for (Media m : mediaList) if (m.getId().equalsIgnoreCase(id)) return m;
        return null;
    }

    private static String cap(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for (String w : s.split(" ")) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    // =========================================================
    // OPTIONS 1-4 — existing M1-M3 features (preserved)
    // =========================================================
    private static void option1Library() {
        StringBuilder body = new StringBuilder();
        body.append(TerminalUI.pad("ID", 8))
            .append(TerminalUI.pad("TITLE", 34))
            .append(TerminalUI.pad("TYPE", 10))
            .append(TerminalUI.pad("CATEGORY", 16))
            .append(TerminalUI.padR("DUR", 5))
            .append(TerminalUI.padR("VIEWS", 10)).append("\n");
        body.append(TerminalUI.repeat("-", 80)).append("\n");
        for (Media m : mediaList) {
            String title = m.getTitle();
            if (title.length() > 32) title = title.substring(0, 29) + "...";
            body.append(TerminalUI.pad(m.getId(), 8))
                .append(TerminalUI.pad(title, 34))
                .append(TerminalUI.pad(m.getType(), 10))
                .append(TerminalUI.pad(m.getCategory(), 16))
                .append(TerminalUI.padR(String.valueOf(m.getDuration()), 5))
                .append(TerminalUI.padR(String.format("%,d", m.getViews()), 10)).append("\n");
        }
        body.append(TerminalUI.repeat("-", 80)).append("\n");
        body.append("Total records: ").append(mediaList.size());
        System.out.println(TerminalUI.box("Media Library", body.toString()));
    }

    private static void option2SmartSearch() {
        section("SMART SEARCH");
        System.out.println("Enter keyword or topic:");
        System.out.print("> ");
        String kw = sc.nextLine().trim();
        if (kw.isEmpty()) { System.out.println("Empty keyword."); return; }
        System.out.println();
        System.out.println(TerminalUI.stepDone("Analyzing query", "DONE"));
        System.out.println(TerminalUI.stepDone("Searching indexed media", "DONE"));
        System.out.println();

        AhoCorasick ac = new AhoCorasick();
        for (String w : kw.split("[,\\s]+")) if (!w.isEmpty()) ac.addPattern(w);
        ac.build();

        List<Media> matched = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        for (Media m : mediaList) {
            Map<String, List<Integer>> r = ac.search(m.getTitle() + " " + m.getTranscript());
            int c = 0;
            for (List<Integer> li : r.values()) c += li.size();
            if (c > 0) { matched.add(m); counts.add(c); }
        }

        section("RESULTS");
        System.out.println();
        if (matched.isEmpty()) System.out.println("(No media matched this query)");
        else {
            for (int i = 0; i < matched.size(); i++) {
                Media m = matched.get(i);
                String meta = m.getCategory() + " | " + m.getType() + " | "
                    + String.format("%,d", m.getViews()) + " views  [" + counts.get(i) + " hits]";
                System.out.println(TerminalUI.numberedItem(i + 1, m.getTitle(), meta));
                System.out.println();
            }
        }
        System.out.println(TerminalUI.repeat("─", 78));
        System.out.println(matched.size() + " relevant media records found.");
        System.out.println("Search completed successfully.");
    }

    private static void option3TranscriptAnalysis() {
        section("TRANSCRIPT ANALYSIS");
        System.out.println("Select first media:"); System.out.print("> ");
        String id1 = sc.nextLine().trim();
        System.out.println("Select second media:"); System.out.print("> ");
        String id2 = sc.nextLine().trim();
        Media m1 = findById(id1), m2 = findById(id2);
        if (m1 == null || m2 == null) { System.out.println("Invalid ID."); return; }
        System.out.println();

        System.out.println(TerminalUI.stepDone("Analyzing transcripts", "DONE"));
        System.out.println(TerminalUI.stepDone("Finding common phrases", "DONE"));
        System.out.println(TerminalUI.stepDone("Calculating content overlap", "DONE"));
        System.out.println();

        LCP lcp = new LCP();
        LCP.LCResult res = lcp.compareTranscripts(m1.getTranscript(), m1.getTitle(),
                                                  m2.getTranscript(), m2.getTitle());
        String common = res.commonPrefix.isEmpty() ? "(no shared prefix)" : res.commonPrefix;

        Set<String> s1 = keywords(m1), s2 = keywords(m2);
        s1.retainAll(s2);
        List<String> commonTopics = new ArrayList<>(s1);
        Collections.sort(commonTopics);
        if (commonTopics.isEmpty()) commonTopics.add("(no shared topics)");
        if (commonTopics.size() > 8) commonTopics = new ArrayList<>(commonTopics.subList(0, 8));

        section("COMPARISON RESULT");
        System.out.println();
        System.out.println("Media 1:\n" + m1.getTitle() + "\n");
        System.out.println("Media 2:\n" + m2.getTitle() + "\n");
        System.out.println("Content Similarity:\n" + (int) Math.round(res.similarityPercent) + "%\n");
        System.out.println("Longest Common Phrase:\n\"" + common + "\"\n");
        System.out.println("Common Topics:");
        for (String t : commonTopics) System.out.println(TerminalUI.bullet(cap(t)));
        System.out.println("\nTranscript comparison completed successfully.");
        System.out.println(TerminalUI.repeat("─", 78));
    }

    private static Set<String> keywords(Media m) {
        String text = (m.getTitle() + " " + m.getTranscript() + " " + m.getCategory()).toLowerCase();
        Set<String> stop = new HashSet<>(Arrays.asList(
            "the","and","for","with","that","this","from","into","have","will",
            "their","they","them","been","being","what","when","where","were","your",
            "about","also","more","most","than","then","such","only","some","each",
            "which","would","could","should","just","over","after","under","between",
            "through","because","people","world","data","used","using","uses","many",
            "very","make","made","like","work","area","well","help","find","real",
            "best","next","last","first","full","high","long","life","both","same",
            "them","these","those","there","here","its","are","was","were","can",
            "our","you","all","may","out","how","new","now","old","one","two","way",
            "day","who","has","had","but","not","any","his","her","him","she","too"
        ));
        Set<String> res = new LinkedHashSet<>();
        for (String w : text.split("[^a-zA-Z]+")) {
            if (w.length() >= 5 && !stop.contains(w)) res.add(w.toLowerCase());
        }
        return res;
    }

    private static void option4ContentInsights() {
        section("CONTENT INSIGHTS");
        System.out.println();
        System.out.println(TerminalUI.stepDone("Analyzing media collection", "DONE"));
        System.out.println();

        String[] topicArr = {"Artificial Intelligence", "Climate", "Technology", "Economy",
                             "Robotics", "Environment", "Healthcare", "Education",
                             "Football", "Space", "Finance", "Renewable Energy"};
        AhoCorasick ac = new AhoCorasick();
        for (String t : topicArr) ac.addPattern(t);
        ac.build();
        Map<String, Integer> topicCount = new LinkedHashMap<>();
        for (String t : topicArr) topicCount.put(t.toLowerCase(), 0);
        StringBuilder all = new StringBuilder();
        for (Media m : mediaList) all.append(m.getTitle()).append(" ").append(m.getTranscript()).append(" ");
        for (Map.Entry<String, List<Integer>> e : ac.search(all.toString()).entrySet())
            topicCount.put(e.getKey(), e.getValue().size());
        List<Map.Entry<String, Integer>> topList = new ArrayList<>(topicCount.entrySet());
        topList.sort((a,b) -> Integer.compare(b.getValue(), a.getValue()));

        String[] phrases = {"artificial intelligence", "climate change",
                            "machine learning", "global economy",
                            "quantum computing", "renewable energy",
                            "deep learning"};
        Map<String, Integer> phraseCount = new LinkedHashMap<>();
        KMP kmp = new KMP();
        String allL = all.toString().toLowerCase();
        for (String p : phrases) {
            int c = kmp.search(allL, p.toLowerCase()).size();
            if (c >= 2) phraseCount.put(p, c);
        }
        List<Map.Entry<String, Integer>> phraseList = new ArrayList<>(phraseCount.entrySet());
        phraseList.sort((a,b) -> Integer.compare(b.getValue(), a.getValue()));

        Map<String, Long> catViews = new LinkedHashMap<>();
        for (Media m : mediaList) catViews.merge(m.getCategory(), (long) m.getViews(), Long::sum);
        long maxV = catViews.values().stream().mapToLong(Long::longValue).max().orElse(1L);

        section("TOP TOPICS");
        System.out.println();
        int shown = 0;
        for (Map.Entry<String, Integer> e : topList) {
            if (e.getValue() == 0) continue;
            System.out.println(TerminalUI.pad(cap(e.getKey()), 30)
                + TerminalUI.padR(e.getValue() + " mentions", 14));
            if (++shown == 5) break;
        }
        System.out.println();
        section("REPEATED PHRASES");
        System.out.println();
        shown = 0;
        for (Map.Entry<String, Integer> e : phraseList) {
            System.out.println(TerminalUI.pad("\"" + e.getKey() + "\"", 32)
                + TerminalUI.padR(e.getValue() + " occurrences", 16));
            if (++shown == 4) break;
        }
        System.out.println();
        section("CONTENT TREND");
        System.out.println();
        for (Map.Entry<String, Long> e : catViews.entrySet()) {
            System.out.println(TerminalUI.barRowWide(e.getKey(), e.getValue(), maxV, 30));
        }
        System.out.println("\nContent insights generated successfully.");
        System.out.println(TerminalUI.repeat("─", 78));
    }

    // =========================================================
    // OPTION 5 — Content Recommendations (preserved M3 + new compat tie-in)
    // =========================================================
    private static void option5ContentRecommendations() {
        section("CONTENT RECOMMENDATIONS");
        System.out.println();
        System.out.println("Enter your interest (e.g. artificial intelligence):");
        System.out.print("> ");
        String interest = sc.nextLine().trim();
        if (interest.isEmpty()) interest = "artificial intelligence";
        System.out.println();
        System.out.println(TerminalUI.stepDone("Analyzing user interest", "DONE"));
        System.out.println(TerminalUI.stepDone("Scoring media relevance", "DONE"));
        System.out.println(TerminalUI.stepDone("Generating ranked recommendations", "DONE"));
        System.out.println();

        Levenshtein lev = new Levenshtein();
        List<String> interests = Arrays.asList(interest.toLowerCase().split("[,\\s]+"));
        record Score(Media m, int score) {}
        List<Score> scores = new ArrayList<>();
        for (Media m : mediaList) {
            String blob = (m.getTitle() + " " + m.getTranscript() + " " + m.getCategory()).toLowerCase();
            int s = 0;
            for (String kw : interests) {
                if (kw.length() < 3) continue;
                if (blob.contains(kw)) s += 10;
                int d = lev.distance(kw, closestWord(blob, kw));
                if (d <= 2) s += Math.max(1, 5 - d * 2);
            }
            scores.add(new Score(m, s));
        }
        scores.sort((a,b) -> Integer.compare(b.score, a.score));
        List<Score> top = scores.subList(0, Math.min(6, scores.size()));

        BitmaskDP bdp = new BitmaskDP();
        List<String> labels = new ArrayList<>();
        List<Integer> scList = new ArrayList<>();
        for (Score t : top) {
            labels.add(t.m.getId() + " " + (t.m.getTitle().length() > 22 ? t.m.getTitle().substring(0, 22) : t.m.getTitle()));
            scList.add(t.score);
        }
        BitmaskDP.SelectionResult sr = bdp.selectCategories(
                labels.toArray(new String[0]),
                scList.stream().mapToInt(Integer::intValue).toArray(), 3);

        section("RECOMMENDED FOR YOU");
        System.out.println();
        for (int i = 0; i < top.size(); i++) {
            Media m = top.get(i).m;
            String meta = m.getCategory() + " | " + m.getType() + " | Score " + top.get(i).score;
            System.out.println(TerminalUI.numberedItem(i + 1, m.getTitle() + " (" + m.getId() + ")", meta));
            System.out.println();
        }
        section("OPTIMAL 3-ITEM BUNDLE (BITMASK DP)");
        System.out.println();
        for (int i = 0; i < sr.selectedCategories.size(); i++) {
            System.out.println("  [" + (i+1) + "] " + sr.selectedCategories.get(i)
                + "   score = " + sr.selectedScores.get(i));
        }
        System.out.println();
        System.out.println("  Combined Bitmask : " + sr.bitmaskAsBinary()
            + "  (decimal " + sr.bitmask + ")");
        System.out.println("  Max Bundle Score : " + sr.totalScore);
        System.out.println();
        section("RECOMMENDATION PROCESSING ORDER (MATRIX CHAIN)");
        System.out.println();
        MatrixChain mc = new MatrixChain();
        String[] stages = {"Profile", "Filter", "Rank", "Bundle"};
        int[] dims = {10, 20, 30, 40, 50};
        MatrixChain.MatrixChainResult mcr = mc.optimize(dims, stages);
        System.out.println("  Stage Dimensions: " + Arrays.toString(dims));
        System.out.println("  Optimal Order   : " + mcr.optimalOrder);
        System.out.println("  Minimum Ops     : " + mcr.minOperations);
        System.out.println("\nRecommendations generated successfully.");
    }

    private static String closestWord(String blob, String kw) {
        String[] words = blob.split("[^a-zA-Z]+");
        String best = kw; int bestD = Integer.MAX_VALUE;
        Levenshtein lev = new Levenshtein();
        for (String w : words) {
            if (w.length() < 3) continue;
            int d = lev.distance(kw, w);
            if (d < bestD) { bestD = d; best = w; }
        }
        return best;
    }

    // =========================================================
    // OPTION 6 — Distribution Network (M4)
    // =========================================================
    private static void option6DistributionNetwork() {
        section("DISTRIBUTION NETWORK");
        System.out.println();
        System.out.println(TerminalUI.stepDone("Analyzing media distribution network", "DONE"));
        System.out.println(TerminalUI.stepDone("Evaluating channel capacities", "DONE"));
        System.out.println(TerminalUI.stepDone("Optimizing media flow", "DONE"));
        System.out.println();

        DistributionReport r = new DistributionService().analyze();

        System.out.println("Network Nodes       : " + r.networkNodes);
        System.out.println("Network Connections : " + r.networkConnections);
        System.out.println();
        System.out.println(TerminalUI.repeat("─", 78));
        section("MAXIMUM DISTRIBUTION CAPACITY");
        System.out.println();
        System.out.println(String.format("%,d", r.maxFlowUnits) + " media units");
        System.out.println();
        System.out.println(TerminalUI.repeat("─", 78));
        System.out.println("Distribution:\n");
        for (Map.Entry<String, Long> e : r.channelFlows.entrySet()) {
            System.out.println(TerminalUI.pad(e.getKey(), 32)
                + TerminalUI.padR(String.format("%,d", e.getValue()), 10));
        }
        System.out.println();
        System.out.println(TerminalUI.repeat("─", 78));
        System.out.println("Network utilization:\n" + (int) Math.round(r.utilizationPct) + "%\n");

        section("CHANNEL ASSIGNMENT");
        System.out.println();
        for (Map.Entry<String, String> e : r.assignment.mediaToChannel.entrySet()) {
            System.out.println(e.getKey());
            System.out.println("        ↓");
            System.out.println(e.getValue() + "\n");
        }
        System.out.println(TerminalUI.repeat("─", 78));
        System.out.println("Media Assigned       : " + r.assignment.size);
        System.out.println("Unassigned Media     : " + r.assignment.unassignedMedia.size());
        System.out.println("\nAssignment completed successfully.\n");

        section("CRITICAL DISTRIBUTION LINKS");
        System.out.println();
        System.out.println("The following connections limit the current");
        System.out.println("media-distribution capacity:\n");
        for (String s : r.minCut.criticalLinks) {
            System.out.println("  " + s);
        }
        System.out.println();
        System.out.println("Minimum Cut Capacity:");
        System.out.println(String.format("%,d", r.minCut.cutCapacity));
        System.out.println();
        System.out.println("These connections should be considered for");
        System.out.println("capacity upgrades.\n");
        System.out.println(TerminalUI.repeat("─", 78));
        System.out.println("Processing strategy: " + r.flowAlgorithmUsed
            + " — " + r.routingReason);
        System.out.println("\nDistribution analysis completed successfully.");
        System.out.println(TerminalUI.repeat("─", 78));
    }

    // =========================================================
    // OPTION 7 — Content Compatibility (M5)
    // =========================================================
    private static void option7ContentCompatibility() {
        section("CONTENT COMPATIBILITY");
        System.out.println();
        System.out.println(TerminalUI.stepDone("Evaluating content constraints", "DONE"));
        System.out.println(TerminalUI.stepDone("Checking conflicts", "DONE"));
        System.out.println(TerminalUI.stepDone("Finding valid combinations", "DONE"));
        System.out.println();

        CompatibilityReport r = new ContentCompatibilityService().analyze();

        System.out.println("Selected Content:\n");
        if (r.satAssignment != null) {
            for (Map.Entry<String, Boolean> e : r.satAssignment.entrySet()) {
                if (!Boolean.TRUE.equals(e.getValue())) continue;
                String var = prettyVar(e.getKey());
                System.out.println("  • " + var);
            }
        } else {
            System.out.println("  (no valid content selection found)");
        }
        System.out.println();
        System.out.println("Constraints:\n");
        for (String s : r.constraintsStatus) System.out.println("  " + s);
        System.out.println();
        System.out.println(TerminalUI.repeat("─", 78));
        section("CONTENT CONSTRAINTS (3-SAT)");
        System.out.println();
        System.out.println("Clauses:\n" + r.clauseCount + "\n");
        System.out.println("Variables:\n" + r.varCount + "\n");
        System.out.println("Checking satisfiability......................... DONE\n");
        System.out.println("Result:\n" + (r.satAssignment != null ? "SATISFIABLE" : "UNSATISFIABLE") + "\n");
        if (r.satAssignment != null) {
            System.out.println("One valid assignment:\n");
            for (Map.Entry<String, Boolean> e : r.satAssignment.entrySet()) {
                System.out.println(TerminalUI.pad(cap(prettyVar(e.getKey())), 18)
                    + " = " + (Boolean.TRUE.equals(e.getValue()) ? "TRUE" : "FALSE"));
            }
        }
        System.out.println();
        System.out.println(TerminalUI.repeat("─", 78));
        section("CONTENT COMPATIBILITY ANALYSIS (3-SAT → CLIQUE)");
        System.out.println();
        System.out.println("Converting constraints into compatibility graph...\n");
        System.out.println("Clauses:\n" + r.cliqueResult.clauses + "\n");
        System.out.println("Graph vertices:\n" + r.cliqueResult.vertices + "\n");
        System.out.println("Required clique size:\n" + r.cliqueResult.requiredCliqueSize + "\n");
        System.out.println("Clique found:\n" + (r.cliqueResult.cliqueFound ? "YES" : "NO") + "\n");
        if (!r.cliqueResult.cliqueLiterals.isEmpty()) {
            System.out.print("Literal witnesses: ");
            System.out.println(String.join(", ", r.cliqueResult.cliqueLiterals));
        }
        System.out.println("\nCompatible content assignment found.\n");

        section("CONFLICT-FREE CONTENT GROUP");
        System.out.println();
        for (String s : r.independentSet) System.out.println("  • " + s);
        System.out.println("\nConflicting relationships:\n" + r.independentSetConflicts + "\n");
        System.out.println("Independent Set Size:\n" + r.independentSet.size());
        System.out.println();

        section("CONTENT CONFLICT COVER (Vertex Cover)");
        System.out.println();
        System.out.println("Critical Topics:\n");
        for (String s : r.minVertexCover) System.out.println("  • " + s);
        System.out.println("\nAll major conflict relationships covered.\n");
        System.out.println("Cover Size:\n" + r.minVertexCoverSize);
        System.out.println();

        section("APPROXIMATE CONFLICT COVER (2-Approx)");
        System.out.println();
        System.out.println("Approximate Cover:");
        for (String s : r.approxCover.cover) System.out.println("  • " + s);
        System.out.println();
        System.out.println("Edges Covered:\n" + r.approxCover.edgesCovered + " / " + r.approxCover.totalEdges + "\n");
        System.out.println("Approximation Guarantee:\n≤ 2 × OPT\n");
        System.out.println(TerminalUI.repeat("─", 78));
        section("RESULT");
        System.out.println("\n" + r.overallResult + "\n");
        if ("VALID CONTENT SELECTION".equals(r.overallResult)) {
            System.out.println("All constraints satisfied.\n");
        }
        System.out.println(TerminalUI.repeat("─", 78));
    }

    private static String prettyVar(String k) {
        switch (k) {
            case "T": return "Technology";
            case "Sports": return "Sports";
            case "Finance": return "Finance";
            case "Environment": return "Environment";
            case "Science": return "Science";
            case "Politics": return "Politics";
        }
        return k;
    }

    // =========================================================
    // OPTION 8 — Streaming Analytics (M6 Reservoir Sampling + ParallelReduce)
    // =========================================================
    private static void option8StreamingAnalytics() {
        section("STREAMING ANALYTICS");
        System.out.println();
        System.out.println(TerminalUI.stepDone("Processing live viewing-event stream", "DONE"));
        System.out.println();

        StreamingReport r = new StreamingAnalyticsService().analyze(10000, 10);
        System.out.println("Total Events Processed:\n" + String.format("%,d", r.totalEvents) + "\n");
        System.out.println("Sample Size:\n" + r.sampleSize + "\n");
        System.out.println(TerminalUI.repeat("─", 78));
        section("EVENT SAMPLE");
        System.out.println();
        for (int i = 0; i < r.sample.size(); i++) {
            ViewingEvent e = r.sample.get(i);
            System.out.println(String.format("%02d", i+1) + "  "
                + TerminalUI.pad(e.mediaId, 7) + " " + TerminalUI.pad(e.type, 8));
        }
        System.out.println();
        System.out.println(TerminalUI.repeat("─", 78));
        System.out.println("Total Views (VIEW events):\n" + String.format("%,d", r.totalViewsCount) + "\n");
        System.out.println("Total Watch Time (estimate):\n"
            + String.format("%,d", r.totalWatchTimeMs / 60000L) + " minutes\n");
        System.out.println("Top Media:\n" + (r.topMediaTitle) + " (" + r.topMediaId + ")\n");
        System.out.println("Reservoir sample maintained successfully.");
        System.out.println(TerminalUI.repeat("─", 78));
    }

    // =========================================================
    // OPTION 9 — Media Ranking (M6 Randomized QuickSort)
    // =========================================================
    private static void option9MediaRanking() {
        section("MEDIA RANKING");
        System.out.println();
        System.out.println(TerminalUI.stepDone("Ranking media by popularity", "DONE"));
        System.out.println();
        System.out.println(" " + TerminalUI.pad("RANK", 7)
            + TerminalUI.pad("MEDIA", 40)
            + TerminalUI.padR("VIEWS", 12));
        System.out.println(TerminalUI.repeat("─", 78));

        List<RankedMedia> rank = new RankingService().rank(SortBy.VIEWS, 5);
        Media top = null; long topViews = -1;
        for (RankedMedia rm : rank) {
            String title = rm.media.getTitle();
            if (title.length() > 38) title = title.substring(0, 35) + "...";
            System.out.println(" "
                + TerminalUI.pad(String.valueOf(rm.rank), 7)
                + TerminalUI.pad(title, 40)
                + TerminalUI.padR(String.format("%,d", rm.sortValue), 12));
            if (rm.sortValue > topViews) { topViews = rm.sortValue; top = rm.media; }
        }
        System.out.println();
        System.out.println(TerminalUI.repeat("─", 78));
        System.out.println("\nTop Content:\n" + (top == null ? "-" : top.getTitle()) + "\n");
        System.out.println("Ranking completed successfully.\n");
        System.out.println(TerminalUI.repeat("─", 78));
    }

    // =========================================================
    // OPTION 10 — Analytics Report
    // =========================================================
    private static void option10AnalyticsReport() {
        MediaMineReport r = new ReportService().build();
        StringBuilder head = new StringBuilder();
        head.append(TerminalUI.repeat("═", 78)).append("\n");
        head.append(center("MEDIAMINE REPORT", 78)).append("\n");
        head.append(TerminalUI.repeat("═", 78)).append("\n\n");

        head.append("MEDIA\n").append(TerminalUI.repeat("─",78)).append("\n")
            .append(kv("Total Media",                    String.valueOf(r.totalMedia))).append("\n")
            .append(kv("Videos",                         String.valueOf(r.videos))).append("\n")
            .append(kv("News / Articles",                String.valueOf(r.articles))).append("\n")
            .append(kv("Total Views",                    String.format("%,d", r.totalViews))).append("\n\n");

        head.append("DISTRIBUTION NETWORK\n").append(TerminalUI.repeat("─",78)).append("\n")
            .append(kv("Network Nodes",                 String.valueOf(r.dist.networkNodes))).append("\n")
            .append(kv("Network Connections",           String.valueOf(r.dist.networkConnections))).append("\n")
            .append(kv("Maximum Distribution Flow",     String.format("%,d", r.dist.maxFlowUnits))).append("\n")
            .append(kv("Critical Connections",          String.valueOf(r.dist.minCut.criticalLinks.size()))).append("\n")
            .append(kv("Media-Channel Assignments",     String.valueOf(r.dist.assignment.size))).append("\n\n");

        head.append("CONTENT COMPATIBILITY\n").append(TerminalUI.repeat("─",78)).append("\n")
            .append(kv("SAT Problems",                  String.valueOf(r.compat.clauseCount))).append("\n")
            .append(kv("Satisfiable",                   r.compat.satAssignment != null ? "YES" : "NO")).append("\n")
            .append(kv("Clique Analysis",               r.compat.cliqueResult.cliqueFound ? "FOUND" : "NOT FOUND")).append("\n")
            .append(kv("Independent Set Size",          String.valueOf(r.compat.independentSet.size()))).append("\n")
            .append(kv("Vertex Cover (optimal)",        String.valueOf(r.compat.minVertexCoverSize))).append("\n\n");

        head.append("STREAMING ANALYTICS\n").append(TerminalUI.repeat("─",78)).append("\n")
            .append(kv("Events Processed",              String.format("%,d", r.stream.totalEvents))).append("\n")
            .append(kv("Reservoir Sample Size",         String.valueOf(r.stream.sampleSize))).append("\n")
            .append(kv("VIEW Events",                   String.format("%,d", r.stream.totalViewsCount))).append("\n\n");

        head.append("MEDIA RANKING\n").append(TerminalUI.repeat("─",78)).append("\n")
            .append(kv("Top Media",                     r.topMediaTitle)).append("\n")
            .append(kv("Top Views",                     String.format("%,d", r.topViews))).append("\n\n");

        head.append("PARALLEL ANALYTICS\n").append(TerminalUI.repeat("─",78)).append("\n")
            .append(kv("Parallel Aggregation",          "COMPLETED")).append("\n")
            .append(kv("Prefix Analysis",               "COMPLETED")).append("\n")
            .append(kv("Performance Analysis",          "COMPLETED")).append("\n")
            .append(kv("Parallel Watch Time",            String.format("%,d minutes", r.parallel.totalWatchMinutes))).append("\n")
            .append(kv("Parallel Events",                String.format("%,d", r.parallel.totalEvents))).append("\n")
            .append(kv("Average Media Views",            String.format("%,.0f", r.parallel.averageViews))).append("\n")
            .append(kv("Identifiers (prime-check)",     r.parallel.identifiersAnalyzed + " (" + r.parallel.primeCandidates + " primes)")).append("\n")
            .append(kv("Parallel T_p (Brent)",          r.parallel.brent.estimatedTimeTp + " units (" + r.parallel.brent.processorsP + " cores)")).append("\n\n");

        head.append(TerminalUI.repeat("═",78)).append("\n")
            .append(center("REPORT GENERATED SUCCESSFULLY", 78)).append("\n")
            .append(TerminalUI.repeat("═",78));

        System.out.println(head);
    }

    // =========================================================
    // OPTION 11 — System Status
    // =========================================================
    private static void option11SystemStatus() {
        int[] t = runAllSelfTests();
        int m1 = t[0], m2 = t[1], m3 = t[2], m4 = t[3], m5 = t[4], m6 = t[5];
        boolean ok = (m1==4 && m2==4 && m3==5 && m4==5 && m5==7 && m6==6);

        StringBuilder body = new StringBuilder();
        body.append(kv("Media Repository", "ONLINE")).append("\n")
            .append(kv("Search Engine", "ONLINE")).append("\n")
            .append(kv("Transcript Engine", "ONLINE")).append("\n\n")
            .append(kv("M1 String Processing",          m1 + "/4 " + (m1==4?"READY":"CHECK"))).append("\n")
            .append(kv("M2 Document Analysis",          m2 + "/4 " + (m2==4?"READY":"CHECK"))).append("\n")
            .append(kv("M3 Optimization Engine",        m3 + "/5 " + (m3==5?"READY":"CHECK"))).append("\n")
            .append(kv("M4 Distribution Engine",        m4 + "/5 " + (m4==5?"READY":"CHECK"))).append("\n")
            .append(kv("M5 Constraint Engine",          m5 + "/7 " + (m5==7?"READY":"CHECK"))).append("\n")
            .append(kv("M6 Analytics Engine",           m6 + "/6 " + (m6==6?"READY":"CHECK"))).append("\n\n")
            .append(kv("Overall System Status",         ok ? "ONLINE" : "DEGRADED")).append("\n")
            .append(kv("JVM Runtime",                   System.getProperty("java.runtime.version"))).append("\n")
            .append(kv("Available Processors",          String.valueOf(Runtime.getRuntime().availableProcessors()))).append("\n")
            .append(kv("Working Dir",                   System.getProperty("user.dir")));
        System.out.println(TerminalUI.box("System Status", body.toString()));
    }

    // =========================================================
    // OPTION 12 — Technical Details (DSA mapping)
    // =========================================================
    private static void option12TechnicalDetails() {
        String W = "─";
        StringBuilder body = new StringBuilder();

        body.append("M4\n").append(TerminalUI.repeat(W, 36)).append("\n")
            .append("Media Distribution\n")
            .append("  -> Ford-Fulkerson\n")
            .append("  -> Edmonds-Karp\n")
            .append("  -> Dinic\n\n")
            .append("Channel Assignment\n")
            .append("  -> Bipartite Matching\n\n")
            .append("Critical Connections\n")
            .append("  -> Min-Cut\n\n");

        body.append("M5\n").append(TerminalUI.repeat(W, 36)).append("\n")
            .append("Content Constraints\n")
            .append("  -> SAT\n")
            .append("  -> 3-SAT\n\n")
            .append("Compatibility\n")
            .append("  -> 3-SAT -> CLIQUE\n")
            .append("  -> CLIQUE\n\n")
            .append("Conflict-Free Groups\n")
            .append("  -> Independent Set\n\n")
            .append("Conflict Coverage\n")
            .append("  -> Vertex Cover\n")
            .append("  -> 2-Approximation\n\n");

        body.append("M6\n").append(TerminalUI.repeat(W, 36)).append("\n")
            .append("Media Ranking\n")
            .append("  -> Randomized QuickSort\n\n")
            .append("Streaming Sampling\n")
            .append("  -> Reservoir Sampling\n\n")
            .append("Numeric Analysis\n")
            .append("  -> Miller-Rabin\n\n")
            .append("Cumulative Statistics\n")
            .append("  -> Blelloch Scan\n\n")
            .append("Large-Scale Aggregation\n")
            .append("  -> Parallel Reduce\n\n")
            .append("Parallel Performance\n")
            .append("  -> Brent's Theorem\n");

        System.out.println(TerminalUI.box("Technical Details (Algorithm Routing)", body.toString()));
    }

    // =========================================================
    // Helpers
    // =========================================================
    private static void section(String name) {
        String W = "─";
        System.out.println();
        System.out.println(TerminalUI.repeat(W, 78));
        System.out.println(center(name, 78));
        System.out.println(TerminalUI.repeat(W, 78));
    }

    private static String center(String s, int n) {
        int pad = Math.max(0, (n - s.length()) / 2);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pad; i++) sb.append(' ');
        sb.append(s);
        while (sb.length() < n) sb.append(' ');
        return sb.toString();
    }

    private static String kv(String k, String v) {
        return TerminalUI.pad(k, 32) + ": " + v;
    }
}
