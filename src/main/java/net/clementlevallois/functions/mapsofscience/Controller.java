/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package net.clementlevallois.functions.mapsofscience;

import java.io.IOException;

/**
 *
 * @author LEVALLOIS
 */
public class Controller {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException, Exception {
        String worksFileString = "all-works.json";
        String worksWellFormatted = "all-works-well-formatted.json";
        String journalsAndAuthorsPerWork = "journals-and-authors-per-work.txt";
        String journalsAndAuthors = "all-journals-and-their-authors.txt";

        /**
         * Takes almost a day and 96,000 API calls.
         * Generates a 65Gb file.
         */
//        APICallsToOpenAlex apiCalls = new APICallsToOpenAlex();
//        apiCalls.getAllWorksPagedWithCursor(worksFileString);
        

        /**
         * Adds a couple of characters to the 65Gb file to make sure it is a compliant json file.
         * Almost instantaneous operation.
         */
//        ProcessingWorks processingWorks = new ProcessingWorks();
//        processingWorks.formatAsJsonArray(works, worksWellFormatted);


        /**
         * Filters the json file to retain only what we need: journal ids and author ids.
         * Takes approx 10 minutes for 20 million works, which is a 65Gb file.
         * Returns a file of 1.2Gb approx.
         */
//        JsonStreamingParser parser = new JsonStreamingParser();
//        parser.parseJournalIdsAndAuthorIds(worksWellFormatted, journalsAndAuthorsPerWork);

        /**
         * Reorganizes journal ids and author ids to end up with:
         * for each line, a journal id followed by all author ids who have published in it.
         * Takes 3 to 10 minutes and needs 4GB of max heap size (add a -Xmx4g paramater in the command line launching the java program).
         * Produces a file close to 1Gb in size.
         */
        CreateListOfJournalsWithTheirAuthors createList = new CreateListOfJournalsWithTheirAuthors();
        createList.doAllOps(journalsAndAuthorsPerWork, journalsAndAuthors);

    }
}
