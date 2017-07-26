package org.dom4j.util;

import org.testng.annotations.Test;

import static org.dom4j.util.StringUtils.endsWithWhitespace;
import static org.dom4j.util.StringUtils.startsWithWhitespace;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test of utility class StringUtils.
 *
 * @author  Marián Petráš
 */
public class StringUtilsTest {

    @Test
    public void testStartsWithWhitespace_empty() {
        assertFalse(startsWithWhitespace(""));
    }

    @Test
    public void testStartsWithWhitespace_nonEmpty() {
        assertTrue (startsWithWhitespace(" "));
        assertFalse(startsWithWhitespace("alpha"));
        assertTrue (startsWithWhitespace(" alpha"));
        assertFalse(startsWithWhitespace("alpha "));
        assertTrue (startsWithWhitespace(" alpha "));
    }

    @Test
    public void testEndsWithWhitespace_empty() {
        assertFalse(endsWithWhitespace(""));
    }

    @Test
    public void testEndsWithWhitespace_nonEmpty() {
        assertTrue (endsWithWhitespace(" "));
        assertFalse(endsWithWhitespace("alpha"));
        assertFalse(endsWithWhitespace(" alpha"));
        assertTrue (endsWithWhitespace("alpha "));
        assertTrue (endsWithWhitespace(" alpha "));
    }

}
