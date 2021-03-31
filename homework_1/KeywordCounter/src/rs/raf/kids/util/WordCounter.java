package rs.raf.kids.util;

import rs.raf.kids.core.Res;

public class WordCounter {

    public int count(String text, String word) {
        String sep = String.format(Res.FORMAT_KEYWORD, word);
        String[] counts = text.split(sep);

        return counts.length - 1;
    }
}
