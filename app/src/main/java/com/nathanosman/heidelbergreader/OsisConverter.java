package com.nathanosman.heidelbergreader;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Convert references to their OSIS equivalent
 *
 * This enables links to be opened directly in Bible apps like YouVersion.
 */
class OsisConverter {

    private static final Map<String, String> sConversion = new HashMap<>();
    static {

        // Old testament books
        sConversion.put("Genesis", "GEN");
        sConversion.put("Exodus", "EXO");
        sConversion.put("Leviticus", "LEV");
        sConversion.put("Numbers", "NUM");
        sConversion.put("Deuteronomy", "DEU");
        sConversion.put("Joshua", "JOS");
        sConversion.put("Judges", "JDG");
        sConversion.put("Ruth", "RUT");
        sConversion.put("1 Samuel", "1SA");
        sConversion.put("2 Samuel", "2SA");
        sConversion.put("1 Kings", "1KI");
        sConversion.put("2 Kings", "2KI");
        sConversion.put("1 Chronicles", "1CH");
        sConversion.put("2 Chronicles", "2CH");
        sConversion.put("Ezra", "EZR");
        sConversion.put("Nehemiah", "NEH");
        sConversion.put("Esther", "EST");
        sConversion.put("Job", "JOB");
        sConversion.put("Psalm", "PSA");
        sConversion.put("Proverbs", "PRO");
        sConversion.put("Ecclesiastes", "ECC");
        sConversion.put("Song of Solomon", "SNG");
        sConversion.put("Isaiah", "ISA");
        sConversion.put("Jeremiah", "JER");
        sConversion.put("Lamentations", "LAM");
        sConversion.put("Ezekiel", "EZK");
        sConversion.put("Daniel", "DAN");
        sConversion.put("Hosea", "HOS");
        sConversion.put("Joel", "JOL");
        sConversion.put("Amos", "AMO");
        sConversion.put("Obadiah", "OBA");
        sConversion.put("Jonah", "JON");
        sConversion.put("Micah", "MIC");
        sConversion.put("Nahum", "NAM");
        sConversion.put("Habakkuk", "HAB");
        sConversion.put("Zephaniah", "ZEP");
        sConversion.put("Haggai", "HAG");
        sConversion.put("Zechariah", "ZEC");
        sConversion.put("Malachi", "MAL");

        // New testament books
        sConversion.put("Matthew", "MAT");
        sConversion.put("Mark", "MRK");
        sConversion.put("Luke", "LUK");
        sConversion.put("John", "JHN");
        sConversion.put("Acts", "ACT");
        sConversion.put("Romans", "ROM");
        sConversion.put("1 Corinthians", "1CO");
        sConversion.put("2 Corinthians", "2CO");
        sConversion.put("Galatians", "GAL");
        sConversion.put("Ephesians", "EPH");
        sConversion.put("Philippians", "PHP");
        sConversion.put("Colossians", "COL");
        sConversion.put("1 Thessalonians", "1TH");
        sConversion.put("2 Thessalonians", "2TH");
        sConversion.put("1 Timothy", "1TI");
        sConversion.put("2 Timothy", "2TI");
        sConversion.put("Titus", "TIT");
        sConversion.put("Philemon", "PHM");
        sConversion.put("Hebrews", "HEB");
        sConversion.put("James", "JAS");
        sConversion.put("1 Peter", "1PE");
        sConversion.put("2 Peter", "2PE");
        sConversion.put("1 John", "1JN");
        sConversion.put("2 John", "2JN");
        sConversion.put("3 John", "3JN");
        sConversion.put("Jude", "JUD");
        sConversion.put("Revelation", "REV");
    }

    /**
     * Convert a reference to OSIS
     * @param reference text reference
     * @return OSIS equivalent for the reference or null
     */
    @Nullable static String convert(String reference) {

        List<String> osisReferences = new ArrayList<>();

        // Split into [book]:*
        String[] bookSplit = reference.trim().split("\\s");
        if (bookSplit.length != 2) {
            return null;
        }

        // Lookup the book
        String bookName = sConversion.get(bookSplit[0]);
        if (bookName == null) {
            return null;
        }

        // Split numbers into [chapter]:[verse(s)]
        String[] numericalSplit = bookSplit[1].split(":");
        if (numericalSplit.length != 2) {
            return null;
        }
        String chapter = numericalSplit[0];

        // Split the verses into a comma-separated list
        for (String verseRange : numericalSplit[1].split(",")) {

            // Each range consists of a start and (optional) finish
            String[] rangeSplit = verseRange.split("-");
            if (rangeSplit.length > 2) {
                return null;
            }

            // Parse the start and end points
            int start, end;
            try {
                start = Integer.parseInt(rangeSplit[0]);
                end = rangeSplit.length == 2 ? Integer.parseInt(rangeSplit[1]) : start;
            } catch (NumberFormatException e) {
                return null;
            }

            // Ensure that end is greater or equal to the start verse
            if (end < start) {
                return null;
            }

            // Add each verse in the range
            for (int i = start; i <= end; i++) {
                osisReferences.add(
                        String.format(Locale.getDefault(), "%s.%s.%d", bookName, chapter, i)
                );
            }
        }

        // Concatenate the list
        return TextUtils.join("+", osisReferences);
    }
}
