package mediamine.m1;

import java.util.*;

public class AhoCorasick {

    private static class Node {
        Map<Character, Integer> children = new HashMap<>();
        int failLink = -1;
        List<Integer> output = new ArrayList<>();
    }

    private List<Node> trie;
    private List<String> patterns;

    public AhoCorasick() {
        trie = new ArrayList<>();
        trie.add(new Node());
        patterns = new ArrayList<>();
    }

    public void addPattern(String pattern) {
        patterns.add(pattern.toLowerCase());
        int current = 0;
        String p = pattern.toLowerCase();
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (!trie.get(current).children.containsKey(c)) {
                trie.add(new Node());
                trie.get(current).children.put(c, trie.size() - 1);
            }
            current = trie.get(current).children.get(c);
        }
        trie.get(current).output.add(patterns.size() - 1);
    }

    public void build() {
        Queue<Integer> queue = new LinkedList<>();
        for (char c : trie.get(0).children.keySet()) {
            int child = trie.get(0).children.get(c);
            trie.get(child).failLink = 0;
            queue.add(child);
        }
        while (!queue.isEmpty()) {
            int r = queue.poll();
            for (char c : trie.get(r).children.keySet()) {
                int u = trie.get(r).children.get(c);
                queue.add(u);
                int v = trie.get(r).failLink;
                while (v != -1 && !trie.get(v).children.containsKey(c)) {
                    v = trie.get(v).failLink;
                    if (v == -1) break;
                }
                if (v == -1 || !trie.get(v).children.containsKey(c)) {
                    trie.get(u).failLink = 0;
                } else {
                    trie.get(u).failLink = trie.get(v).children.get(c);
                    for (int out : trie.get(trie.get(u).failLink).output) {
                        if (!trie.get(u).output.contains(out)) {
                            trie.get(u).output.add(out);
                        }
                    }
                }
            }
        }
    }

    public Map<String, List<Integer>> search(String text) {
        Map<String, List<Integer>> result = new LinkedHashMap<>();
        for (String p : patterns) result.put(p, new ArrayList<>());
        if (text == null || text.isEmpty()) return result;
        String t = text.toLowerCase();
        int current = 0;
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            while (current != 0 && !trie.get(current).children.containsKey(c)) {
                current = trie.get(current).failLink;
                if (current == -1) { current = 0; break; }
            }
            if (current == -1) current = 0;
            if (trie.get(current).children.containsKey(c)) {
                current = trie.get(current).children.get(c);
            }
            for (int patternIdx : trie.get(current).output) {
                String found = patterns.get(patternIdx);
                result.get(found).add(i - found.length() + 1);
            }
        }
        return result;
    }

    public Map<String, Integer> countAll(String text) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Map<String, List<Integer>> result = search(text);
        for (Map.Entry<String, List<Integer>> e : result.entrySet()) {
            counts.put(e.getKey(), e.getValue().size());
        }
        return counts;
    }

    public String getTimeComplexity() {
        return "O(n + m + z), where n = text length, m = sum of patterns, z = matches";
    }

    public String getSpaceComplexity() {
        return "O(m) for the automaton trie";
    }

    public String getExplanation() {
        return "Aho-Corasick builds a trie with failure links (like KMP on multiple patterns). It searches " +
               "all patterns simultaneously in one pass through the text, making it ideal for multi-keyword search.";
    }
}
