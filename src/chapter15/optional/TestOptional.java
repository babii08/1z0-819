package chapter15.optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestOptional {

    Optional<String> emptyOptional = Optional.empty();
    Optional<String> optional = Optional.of("testingOptional");


    @Test
    void testOptionalGet() {
        assertThrows(NoSuchElementException.class, () -> emptyOptional.get());
        assertEquals(optional.get(), "testingOptional");
    }

    @Test
    void testOptionalIsPresent() {
        assertFalse(emptyOptional.isPresent());
        assertTrue(optional.isPresent());
    }

    @Test
    void testOptionalIfPresent() {
        final var emptyList = new ArrayList<String>();
        final var list = new ArrayList<String>();
        emptyOptional.ifPresent(emptyList::add);
        optional.ifPresent(list::add);

        assertTrue(emptyList.isEmpty());
        assertFalse(list.isEmpty());
    }

    @Test
    void testOptionalOrElse() {
        assertEquals(emptyOptional.orElse("another"), "another");
        assertEquals(optional.orElse("another"), "testingOptional");
    }

    @Test
    void testOptionalOrElseGet() {
        assertEquals(emptyOptional.orElseGet(() -> "another"), "another");
        assertEquals(optional.orElseGet(() -> "another"), "testingOptional");
    }

    @Test
    void testOptionalOrElseThrow() {
        assertThrows(NoSuchElementException.class, () -> emptyOptional.orElseThrow());
        assertEquals(optional.orElseThrow(), "testingOptional");
    }
}
