package chapter16.localization;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LocalizingNumbers {

    @Test
    void getLocalizedNumber() {
        var numb = NumberFormat.getInstance();      //default en_US locale
        var numb1 = NumberFormat.getNumberInstance();

        assertEquals("3,232,131.31", numb.format(3232131.31));
        assertEquals("3,232,131.31", numb1.format(3232131.31));     //same thing as above


        var curr = NumberFormat.getCurrencyInstance();
        assertEquals("$321.12", curr.format(321.1233));

        var percent = NumberFormat.getPercentInstance();
        assertEquals("312,412,400%", percent.format(3124124));
    }

    @Test
    void getLocalizedNumberWithLocale() {
        var numb = NumberFormat.getInstance(Locale.GERMANY);
        var numb1 = NumberFormat.getNumberInstance();

        assertEquals("3.232.131,31", numb.format(3232131.31));      //locale germany
        assertEquals("3,232,131.31", numb1.format(3232131.31));     //locale usa


        var curr = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        assertEquals("321,12 €", curr.format(321.1233));

        var percent = NumberFormat.getPercentInstance(Locale.CANADA_FRENCH);
        assertEquals("312 412 400 %", percent.format(3124124));
    }

    @Test
    void parseStringToLocaleNumber() throws ParseException {
        String s = "40.45";

        var en = NumberFormat.getInstance(Locale.US);
        assertEquals(40.45, en.parse(s));

        var fr = NumberFormat.getInstance(Locale.FRANCE);
        assertEquals(40L, fr.parse(s));
    }

    @Test
    void testDecimalFormat() {
        double d = 1234567.467;
        NumberFormat f1 = new DecimalFormat("###,###,###.0");
        System.out.println(f1.format(d));  // 1,234,567.5

        NumberFormat f2 = new DecimalFormat("000,000,000.00000");
        System.out.println(f2.format(d));  // 001,234,567.46700

        NumberFormat f3 = new DecimalFormat("$#,###,###.##");
        System.out.println(f3.format(d));  // $1,234,567.47
    }
}
