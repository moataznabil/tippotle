package net.doepner.text;

/**
 * Determines the word at a specified text position
 */
public interface WordProvider {

    String word(Integer position);

    Character character(Integer position);
}
