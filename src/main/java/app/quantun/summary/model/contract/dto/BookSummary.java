package app.quantun.summary.model.contract.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookSummary {
    private Metadata metadata = new Metadata();
     private IntroductionBlueprint introduction = new IntroductionBlueprint();
    @Builder.Default private CoreElementsBreakdown coreElements = new CoreElementsBreakdown();
    @Builder.Default private List<Chapter> chapterDigest = new ArrayList<>();
    @Builder.Default private CriticalAnalysisFramework analysis = new CriticalAnalysisFramework();
    @Builder.Default private ActionableTakeawaySystem takeaways = new ActionableTakeawaySystem();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        private String title;
        private String author;
        private String genre;
        private String publicationYear;
        private String coreTheme;
        private String fullBookSummary;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IntroductionBlueprint {
        private String context;
        private String authorsPurpose;
        private String structuralNote;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CoreElementsBreakdown {
        @Builder.Default private PlotArchitecture plot = new PlotArchitecture();
        @Builder.Default private List<CharacterEntry> characters = new ArrayList<>();
        @Builder.Default private ThemeNetwork themes = new ThemeNetwork();
        @Builder.Default private StyleAnalysis style = new StyleAnalysis();

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PlotArchitecture {
            private String centralConflict;
            @Builder.Default private List<String> narrativeArc = new ArrayList<>();
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CharacterEntry {
            private String name;
            private String role;
            private String keyTraits;
            private String developmentArc;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ThemeNetwork {
            private String primaryTheme;
            @Builder.Default private List<String> supportingThemes = new ArrayList<>();
            @Builder.Default private List<String> recurringMotifs = new ArrayList<>();
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class StyleAnalysis {
            private String narrativePerspective;
            private String tone;
            private String pacing;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Chapter {
        private int chapterNumber;
        private String keyEvents;
        private String centralConcept;
        private String fullChapterSummary;
        private String symbolism;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CriticalAnalysisFramework {
        private String authorsThesis;
        private String evidenceStructure;
        private String counterarguments;
        private String originalInsights;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ActionableTakeawaySystem {
        @Builder.Default private List<String> practicalApplications = new ArrayList<>();
        @Builder.Default private List<String> discussionPrompts = new ArrayList<>();
        @Builder.Default private List<String> furtherExploration = new ArrayList<>();
    }
}

