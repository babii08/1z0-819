package chapter16.localization;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestResourceBundle {

    @Test
    void testResourceBundle() {
        var rb = ResourceBundle.getBundle("Zoop", Locale.FRANCE);
        assertEquals("bonjour", rb.getString("hello"));
    }

    @Test
    void testResourceBundleKeySet() {
        var rb = ResourceBundle.getBundle("Zoop");
        assertEquals(Set.of("hello", "open"), rb.keySet());
    }
}
