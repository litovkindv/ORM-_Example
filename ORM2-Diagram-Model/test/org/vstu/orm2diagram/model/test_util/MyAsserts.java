package org.vstu.orm2diagram.model.test_util;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Litovkin Dmitry
 */
public class MyAsserts {
    public static void assertStreamEquals(Stream<?> s1, Stream<?> s2) {
        Iterator<?> iter1 = s1.iterator(), iter2 = s2.iterator();
        while (iter1.hasNext() && iter2.hasNext())
            assertSame(iter1.next(), iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }
}
