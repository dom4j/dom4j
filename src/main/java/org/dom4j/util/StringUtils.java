package org.dom4j.util;

/**
 * Contains utilities related to strings.
 *
 * @author  Marián Petráš
 */
public final class StringUtils {

    private StringUtils() {}

    /**
     * Finds out if the given character sequence starts with a whitespace
     * character.
     *
     * @return  {@code true} if the given character sequence is not empty
     *          and starts with a whitespace character; {@code false} otherwise
     * @exception  NullPointerException  if the given character sequence is
     *             {@code null}
     */
    public static boolean startsWithWhitespace(final CharSequence charSeq) {
        if (charSeq.length() == 0) {
            return false;
        }
        return Character.isWhitespace(charSeq.charAt(0));
    }

    /**
     * Finds out if the given character sequence ends with a whitespace
     * character.
     *
     * @return  {@code true} if the given character sequence is not empty
     *          and ends with a whitespace character; {@code false} otherwise
     * @exception  NullPointerException  if the given character sequence is
     *             {@code null}
     */
    public static boolean endsWithWhitespace(final CharSequence charSeq) {
        if (charSeq.length() == 0) {
            return false;
        }
        return Character.isWhitespace(charSeq.charAt(charSeq.length() - 1));
    }

}
