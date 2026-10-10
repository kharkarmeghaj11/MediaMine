package mediamine.m2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuffixAutomaton {

    private static class State {
        int len;
        int link;
        Map<Character, Integer> next = new HashMap<>();
    }

    private List<State> states;
    private int last;

    public SuffixAutomaton() {
        states = new ArrayList<>();
        State s0 = new State();
        s0.len = 0;
        s0.link = -1;
        states.add(s0);
        last = 0;
    }

    public void build(String s) {
        for (int i = 0; i < s.length(); i++) {
            extend(s.toLowerCase().charAt(i));
        }
    }

    private void extend(char c) {
        int cur = states.size();
        State curState = new State();
        curState.len = states.get(last).len + 1;
        states.add(curState);
        int p = last;
        while (p != -1 && !states.get(p).next.containsKey(c)) {
            states.get(p).next.put(c, cur);
            p = states.get(p).link;
        }
        if (p == -1) {
            states.get(cur).link = 0;
        } else {
            int q = states.get(p).next.get(c);
            if (states.get(p).len + 1 == states.get(q).len) {
                states.get(cur).link = q;
            } else {
                int clone = states.size();
                State cloneState = new State();
                cloneState.len = states.get(p).len + 1;
                cloneState.next = new HashMap<>(states.get(q).next);
                cloneState.link = states.get(q).link;
                states.add(cloneState);
                while (p != -1 && states.get(p).next.get(c) == q) {
                    states.get(p).next.put(c, clone);
                    p = states.get(p).link;
                }
                states.get(q).link = clone;
                states.get(cur).link = clone;
            }
        }
        last = cur;
    }

    public boolean contains(String pattern) {
        if (pattern == null || pattern.isEmpty()) return false;
        String p = pattern.toLowerCase();
        int cur = 0;
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (!states.get(cur).next.containsKey(c)) {
                return false;
            }
            cur = states.get(cur).next.get(c);
        }
        return true;
    }

    public List<Integer> findMediaWithPattern(List<String> transcripts, List<String> titles,
                                              List<String> ids, String pattern) {
        List<Integer> matched = new ArrayList<>();
        for (int i = 0; i < transcripts.size(); i++) {
            SuffixAutomaton sa = new SuffixAutomaton();
            sa.build(transcripts.get(i));
            if (sa.contains(pattern)) {
                matched.add(i);
            }
        }
        return matched;
    }

    public int countStates() {
        return states.size();
    }

    public String getTimeComplexity() {
        return "O(n) construction; O(m) substring query";
    }

    public String getSpaceComplexity() {
        return "O(n), where n = string length (states = at most 2n-1)";
    }

    public String getExplanation() {
        return "Suffix Automaton is a compact automaton that encodes all substrings of a string. It has linear " +
               "size and allows extremely fast substring queries. Used here to quickly search content across transcripts.";
    }
}
