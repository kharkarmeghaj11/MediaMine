package mediamine.data;

import mediamine.model.Media;
import mediamine.model.Channel;
import mediamine.model.ContentConstraint;
import mediamine.model.ViewingEvent;
import mediamine.model.ContentConstraint.Kind;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SampleData {

    public static List<Media> getSampleMedia() {
        List<Media> list = new ArrayList<>();

        list.add(new Media("M001", "AI in Education", "VIDEO", "Technology",
            "Artificial intelligence is changing education through personalized learning and intelligent tutoring systems. Machine learning algorithms adapt to student performance.",
            12, 1500));

        list.add(new Media("M002", "Climate Change Report", "VIDEO", "Environment",
            "Climate change is affecting global weather patterns. Rising temperatures cause melting ice caps and extreme climate events across the world.",
            15, 1200));

        list.add(new Media("M003", "Global Economy Update", "ARTICLE", "Finance",
            "The global economy shows mixed signals. Stock markets fluctuate while central banks adjust interest rates. Economic growth varies by region.",
            8, 2100));

        list.add(new Media("M004", "Football Championship", "VIDEO", "Sports",
            "The football championship final was thrilling. Both teams played exceptional football. The winning goal came in extra time with a brilliant strike.",
            20, 5000));

        list.add(new Media("M005", "Space Exploration", "VIDEO", "Science",
            "Space exploration enters a new era. Private companies launch rockets to orbit. Scientists search for signs of life on Mars and distant moons.",
            18, 3200));

        list.add(new Media("M006", "AI Healthcare", "ARTICLE", "Technology",
            "Artificial intelligence is changing healthcare through smart diagnosis and personalized treatment. Machine learning detects diseases early from medical scans.",
            10, 2800));

        list.add(new Media("M007", "Future of Robotics", "VIDEO", "Technology",
            "Robotics and automation transform industries. Robots assist in manufacturing surgery and space missions. Humanoid robots become more capable each year.",
            14, 1900));

        list.add(new Media("M008", "Global Climate Conference", "ARTICLE", "Environment",
            "World leaders meet at the global climate conference. Nations pledge to reduce carbon emissions. Renewable energy investment grows rapidly.",
            9, 1750));

        list.add(new Media("M009", "Stock Market Analysis", "VIDEO", "Finance",
            "Stock market analysis reveals key trends. Technology stocks lead while energy sectors lag. Investors diversify portfolios across global markets.",
            11, 2400));

        list.add(new Media("M010", "Olympics Highlights", "VIDEO", "Sports",
            "The Olympics delivered unforgettable moments. Athletes broke multiple world records. The opening ceremony celebrated culture and unity through sports.",
            25, 8500));

        list.add(new Media("M011", "Quantum Computing", "ARTICLE", "Science",
            "Quantum computing advances rapidly. Qubits enable calculations impossible for classical computers. Researchers achieve quantum supremacy milestones.",
            7, 1600));

        list.add(new Media("M012", "Renewable Energy Boom", "VIDEO", "Environment",
            "Renewable energy sees unprecedented growth. Solar and wind power become cheaper than coal. Battery technology improves energy storage capacity.",
            13, 2200));

        list.add(new Media("M013", "Basketball Finals", "ARTICLE", "Sports",
            "The basketball finals went to seven games. The MVP scored a record fifty points. Defense won championships with spectacular blocks and steals.",
            6, 4100));

        list.add(new Media("M014", "Crypto Market Trends", "ARTICLE", "Finance",
            "Cryptocurrency markets evolve. Bitcoin and altcoins see price volatility. Institutional adoption increases while regulations develop globally.",
            8, 3000));

        list.add(new Media("M015", "Genetic Research Breakthrough", "VIDEO", "Science",
            "Genetic research unlocks new treatments. CRISPR gene editing cures rare diseases. DNA sequencing becomes faster and affordable for all patients.",
            16, 2700));

        list.add(new Media("M016", "Smart Cities Initiative", "ARTICLE", "Technology",
            "Smart cities use IoT sensors everywhere. Traffic management reduces congestion. Energy grids become efficient through artificial intelligence and automation.",
            9, 1400));

        list.add(new Media("M017", "Ocean Conservation Efforts", "VIDEO", "Environment",
            "Ocean conservation efforts gain momentum. Marine protected areas expand. Plastic pollution reduction targets set for the next decade by global treaty.",
            12, 1850));

        list.add(new Media("M018", "Tennis Grand Slam", "VIDEO", "Sports",
            "The tennis grand slam final captivated audiences. The champion displayed incredible skill. Five-set matches showcase mental and physical endurance.",
            22, 4600));

        list.add(new Media("M019", "Banking Digital Transformation", "ARTICLE", "Finance",
            "Banking undergoes digital transformation. Mobile apps replace branches. AI chatbots handle customer queries. Blockchain technology secures all transactions.",
            7, 2000));

        list.add(new Media("M020", "Neuroscience Discoveries", "VIDEO", "Science",
            "Neuroscience reveals brain mechanisms. Mapping neural connections advances. Brain-computer interfaces help paralyzed patients communicate again.",
            15, 2300));

        for (Media m : list) autoAssignChannels(m);
        return list;
    }

    private static void autoAssignChannels(Media m) {
        String cat = m.getCategory();
        if ("Technology".equals(cat)) { m.addChannelEligibility("Technology Channel"); m.addChannelEligibility("Education Channel"); }
        else if ("Environment".equals(cat)) { m.addChannelEligibility("Environment Channel"); m.addChannelEligibility("Science Channel"); }
        else if ("Finance".equals(cat)) { m.addChannelEligibility("Finance Channel"); m.addChannelEligibility("News Channel"); }
        else if ("Sports".equals(cat)) { m.addChannelEligibility("Sports Channel"); m.addChannelEligibility("News Channel"); }
        else if ("Science".equals(cat)) { m.addChannelEligibility("Science Channel"); m.addChannelEligibility("Education Channel"); }
        else { m.addChannelEligibility("News Channel"); }
    }

    public static String[] getCategoryNames() {
        return new String[]{"Technology", "Science", "Sports", "Finance", "Environment", "Education"};
    }

    public static int[] getCategoryScores() {
        return new int[]{30, 25, 20, 35, 28, 22};
    }

    public static List<Channel> getChannels() {
        List<Channel> ch = new ArrayList<>();
        ch.add(new Channel("C1", "Technology Channel", 250000));
        ch.add(new Channel("C2", "Education Channel",  200000));
        ch.add(new Channel("C3", "Finance Channel",    200000));
        ch.add(new Channel("C4", "News Channel",       150000));
        ch.add(new Channel("C5", "Sports Channel",     180000));
        ch.add(new Channel("C6", "Science Channel",    100000));
        ch.add(new Channel("C7", "Environment Channel",90000));
        return ch;
    }

    public static List<ContentConstraint> getContentConstraints() {
        List<ContentConstraint> cc = new ArrayList<>();
        cc.add(new ContentConstraint(Kind.MUST_SELECT, "Technology", null));
        cc.add(new ContentConstraint(Kind.CANNOT_BOTH_BE_TRUE, "Sports", "Finance"));
        cc.add(new ContentConstraint(Kind.REQUIRES, "Environment", "Science"));
        cc.add(new ContentConstraint(Kind.INCOMPATIBLE, "Technology", "Politics"));
        return cc;
    }

    /** Canonical variables for SAT/3-SAT (Content Compatibility). */
    public static String[] getContentVariables() {
        return new String[]{"T", "Sports", "Finance", "Environment", "Science", "Politics"};
    }

    /** Generate N viewing events deterministically (same seed => same output each run). */
    public static List<ViewingEvent> generateViewingEvents(int count) {
        List<ViewingEvent> out = new ArrayList<>(count);
        Random rng = new Random(7L);
        String[] ids = new String[getSampleMedia().size()];
        int i = 0;
        for (Media m : getSampleMedia()) ids[i++] = m.getId();
        String[] types = { ViewingEvent.VIEW, ViewingEvent.PLAY, ViewingEvent.PAUSE,
                           ViewingEvent.SEEK, ViewingEvent.STOP, ViewingEvent.VIEW,
                           ViewingEvent.VIEW }; // VIEW more common
        long ts = 1_700_000_000_000L;
        for (long seq = 1; seq <= count; seq++) {
            String id = ids[rng.nextInt(ids.length)];
            String t = types[rng.nextInt(types.length)];
            ts += 20L + rng.nextInt(1000);
            out.add(new ViewingEvent(seq, id, t, ts));
        }
        return out;
    }
}
