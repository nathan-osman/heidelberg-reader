package com.nathanosman.heidelbergreader;

import java.util.HashMap;
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
}
