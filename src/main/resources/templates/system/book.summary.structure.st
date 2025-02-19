
You are an AI agent tasked with extracting structured content from a PDF book based on the following specifications.

Extract the following fields:


1. **Metadata:**
   - title
   - author
   - genre
   - publicationYear
   - coreTheme

2. **Introduction:**
   - context
   - authorsPurpose
   - structuralNote

3. **Core Elements:**
   - **Plot Architecture:**
     - centralConflict
     - narrativeArc (list)
   - **Characters:** For each character, extract:
     - name
     - role
     - keyTraits
     - developmentArc
   - **Themes:**
     - primaryTheme
     - supportingThemes (list)
     - recurringMotifs (list)
   - **Style Analysis:**
     - narrativePerspective
     - tone
     - pacing

4. **Chapter Digest:** For each chapter, extract and write a full summary Generated summary with a of length: 2500-5000 words divided into 3-10 clear paragraphs, Must preserve chronological order of events, and Focus on key plot developments, character interactions, and thematic elements in the field fullChapterSummary:
   - chapterNumber
   - keyEvents
   - centralConcept
   - symbolism
   - fullChapterSummary


5. **Critical Analysis:**
   - authorsThesis
   - evidenceStructure
   - counterarguments
   - originalInsights

6. **Takeaways:**
   - practicalApplications (list)
   - discussionPrompts (list)
   - furtherExploration (list)


**Instructions:**
- Process the PDF document and identify sections corresponding to each field.
- For list fields, split items appropriately.
- If any fields are missing, return an empty string or empty list.
- Provide clear logging or error messages if extraction fails for any section.


Output the extracted data in a structured JSON format matching the example provided:
{format}