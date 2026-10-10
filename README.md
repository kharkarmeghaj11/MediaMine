# MEDIAMINE - Intelligent Media Analytics Platform

## 1. Project Title
**MediaMine** - An Intelligent Media Analytics Platform built using Data Structures and Algorithms in Java.

## 2. Introduction
MediaMine is a terminal-based media analytics platform built to demonstrate data structures and algorithms in a media domain. It manages sample news articles, videos, transcripts, categories, distribution channels, and viewing events. M1-M6 are integrated through user-facing operations; users do not choose algorithms directly.

## 3. Problem Statement
Given a collection of media records with transcripts, we need to:
- Search media content efficiently using string-matching algorithms
- Compare transcripts for similarity using suffix structures
- Correct user search queries and transcript typos using edit distance
- Select optimal content categories under constraints using DP
- Find the optimal order of chained media-processing operations
- Analyze media delivery capacity and channel assignments
- Evaluate content constraints and conflicts
- Rank media, sample event streams, and aggregate analytics

## 4. Objectives
1. Implement core string pattern-matching algorithms (M1).
2. Implement suffix structures for transcript comparison (M2).
3. Implement dynamic-programming algorithms for correction and optimization (M3).
4. Route operations automatically to suitable algorithms.
5. Keep the terminal workflows beginner-friendly and easy to explain in a viva.

## 5. Scope
- **In Scope**: M1-M6, sample data, automatic algorithm routing, terminal workflows, and calculated reports.
- **Out of Scope**: Database, cloud services, REST APIs, and login systems.

## 6. Technologies Used
- Java (JDK 8+)
- Eclipse IDE
- Java terminal application (UTF-8 console)
- Java Collections Framework
- Java concurrency (`ForkJoinPool` / `RecursiveTask`)
- CSV sample data

## 7. M1 Description - String Algorithms
Applies pattern-matching algorithms to media search.

| Algorithm         | Use in MediaMine |
|-------------------|------------------|
| **KMP**           | Search keywords in transcripts with LPS preprocessing |
| **Z-Function**    | Find repeated phrases and prefix patterns in transcripts |
| **Rabin-Karp**    | Search media IDs / patterns using rolling hash |
| **Aho-Corasick**  | Multi-keyword search on all transcripts simultaneously |

## 8. M2 Description - Suffix Structures / Document Similarity
Builds suffix-based indexes to compare and analyze transcripts.

| Algorithm             | Use in MediaMine |
|-----------------------|------------------|
| **Suffix Array**      | Build sorted suffix index of a transcript; enable binary search |
| **LCP**               | Pairwise longest common prefix between two transcripts |
| **Kasai**             | Build the LCP array from a Suffix Array; find longest repeated substring |
| **Suffix Automaton**  | Compact automaton for fast substring search across transcripts |

## 9. M3 Description - Advanced Dynamic Programming
Uses DP for correction and optimization problems.

| Algorithm                 | Use in MediaMine |
|---------------------------|------------------|
| **Levenshtein**           | Suggest correction for misspelled search queries |
| **Damerau-Levenshtein**   | Handle transposition typos (teh → the) |
| **Weighted Edit Distance**| Correct transcript errors with weighted operation costs |
| **Bitmask DP**            | Select best k media categories with maximum total score |
| **Matrix Chain**          | Optimize order of chained media-processing operations |

## 10. Algorithms Used
1. **M1**: KMP, Z-Function, Rabin-Karp, Aho-Corasick
2. **M2**: Suffix Array, LCP, Kasai, Suffix Automaton
3. **M3**: Levenshtein, Damerau-Levenshtein, Weighted Edit Distance, Bitmask DP, Matrix Chain Multiplication
4. **M4**: Ford-Fulkerson, Edmonds-Karp, Dinic, Bipartite Matching, Min-Cut
5. **M5**: SAT, 3-SAT, 3-SAT-to-Clique, Clique, Independent Set, Vertex Cover, Vertex Cover 2-Approximation
6. **M6**: Randomized QuickSort, Reservoir Sampling, Miller-Rabin, Blelloch Scan, Parallel Reduce, Brent analysis

## 11. System Architecture
```
MediaMine/
├── src/
│   └── mediamine/
│       ├── Main.java                 ← Terminal menu and self-tests
│       ├── data/SampleData.java      ← Media, constraints, and generated events
│       ├── model/                    ← Media, channels, constraints, events
│       ├── m1/                       ← String algorithms
│       ├── m2/                       ← Suffix structures
│       ├── m3/                       ← Dynamic programming
│       ├── m4/                       ← Flow, matching, and min-cut
│       ├── m5/                       ← SAT and graph optimization
│       ├── m6/                       ← Randomized and parallel algorithms
│       ├── services/                 ← Routing and operation-level integration
│       └── ui/TerminalUI.java        ← Terminal formatting helpers
├── data/media.csv
├── demo_inputs.txt
└── run_demo.bat
```

## 12. Example Calculated Results
- The sample distribution graph has 11 vertices and 15 directed links; its maximum flow is 1,080 units.
- Six eligible media compete for five channels, so maximum matching assigns five and leaves one unassigned.
- Streaming Analytics processes a generated 10,000-event stream and maintains a reservoir sample of 10 events.
- Media views, event totals, ranking, min-cut, SAT assignments, and other report values are calculated at runtime.
- Select **Technical Details** in the menu to see the operation-to-algorithm mapping. Select **System Status** to run module smoke tests.

## 13. How to Run
**Windows:** Run `run_demo.bat` from the project root. It compiles `Main.java` and its source dependencies from `src`, then runs the terminal demo using `demo_inputs.txt`.

**Eclipse:** Import the existing project or create a Java project with `src` as the source folder. Keep the package hierarchy under `src/mediamine`, open `mediamine.Main`, then choose **Run As → Java Application**. No external libraries are required. Use a UTF-8-capable Eclipse console for box-drawing characters.

## 14. Implementation Notes
- M4-M6 algorithms are implemented in the project without external graph, SAT, sorting, or prefix-sum libraries.
- SAT, clique, independent-set, and exact vertex-cover routines are intended for small educational instances.
- Streaming events and analytics use deterministic generated sample input; watch time is an eight-minute estimate per `PLAY` event.
- Brent's theorem output is an educational estimate, not a wall-clock benchmark.
- The optional Swing classes remain in the repository, but the supported application flow is the terminal menu.

---
*MediaMine demonstrates integrated M1-M6 algorithms over sample media analytics workflows.*
