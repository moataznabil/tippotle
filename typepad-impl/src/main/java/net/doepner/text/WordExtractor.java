package net.doepner.text;

import org.guppy4j.log.Log;
import org.guppy4j.log.LogProvider;

import static net.doepner.text.CharConditions.IS_NUMBER_PART;
import static net.doepner.text.CharConditions.IS_WORD_PART;
import static org.guppy4j.log.Log.Level.info;

public final class WordExtractor implements WordProvider {

    private static final Character NO_CHARACTER = null;

    private final Log log;
    private final TextProvider textProvider;

    public WordExtractor(LogProvider logProvider, TextProvider textProvider) {
        log = logProvider.getLog(getClass());
        this.textProvider = textProvider;
    }

    @Override
    public String word(Integer position) {
        if (position == null) {
            return null;
        }
        final int pos = position.intValue();
        final String word = findSequence(IS_WORD_PART, pos);
        final String result = word.isEmpty() ? findSequence(IS_NUMBER_PART, pos) : word;
        log.as(info, "Word '{}' at position {}", result, pos);
        return result;
    }

    @Override
    public Character character(Integer position) {
        if (position == null) {
            return NO_CHARACTER;
        }
        final int pos = position.intValue();
        final String text = textProvider.getText();
        if (pos <= 0 || text.length() < pos) {
            return NO_CHARACTER;
        }
        final char ch = text.charAt(pos - 1);
        log.as(info, "Character '{}' at position {}", ch, pos);
        return ch;
    }

    private String findSequence(CharCondition cond, int position) {
        final int start = getStart(cond, position);
        final int end = getEnd(cond, position - 1);

        return start < end ? textProvider.getText().substring(start, end) : "";
    }

    private int getStart(CharCondition cond, int position) {
        final String text = textProvider.getText();

        int pos = position;
        while (pos > 0 && cond.matches(text.charAt(pos - 1))) {
            pos--;
        }
        return pos;
    }

    private int getEnd(CharCondition cond, int position) {
        final String text = textProvider.getText();

        int pos = position;
        while (pos >= 0 && pos < text.length() && cond.matches(text.charAt(pos))) {
            pos++;
        }
        return pos;
    }
}
