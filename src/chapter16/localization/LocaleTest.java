package chapter16.localization;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocaleTest {

    @Test
    void testLocale() {

        assertEquals("en_US", Locale.getDefault().toString());

        assertEquals("de", Locale.GERMAN.toString());
        assertEquals("de_DE", Locale.GERMANY.toString());

        //creating locale using constructor
        var franceLocale = new Locale("fr_FR");
        assertEquals("fr_fr", franceLocale.toString());
    }
}
