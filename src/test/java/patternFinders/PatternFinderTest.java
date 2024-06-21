package patternFinders;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatternFinderTest {

    @Test
    void containsPattern() {

        String content = "DL-Leucine (D10 98%)";
        String pattern = "([dD][0-9]+)";
        boolean containsDeuterium = PatternFinder.containsPattern(content,pattern);
        assertTrue(containsDeuterium);
    }

    @Test
    void containsPatternFalse() {

        String content = "DL-Leucine (D-10 98%)";
        String pattern = "([dD][0-9]+)";
        boolean containsDeuterium = PatternFinder.containsPattern(content,pattern);
        assertFalse(containsDeuterium);
    }

}