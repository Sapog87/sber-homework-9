package org.sber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamsTest {

    @Test
    @DisplayName("фильтрация работает корректно")
    void filterNotEvenAges() {
        Map<String, Person> mapFromStream = Streams.of(List.of(
                        new Person("name1", 1),
                        new Person("name2", 2),
                        new Person("name3", 3),
                        new Person("name4", 4)))
                .filter(person -> person.getAge() % 2 == 0)
                .toMap(Person::getName, Function.identity());

        Map<String, Person> mapExpected = Map.of(
                "name2", new Person("name2", 2),
                "name4", new Person("name4", 4)
        );

        assertEquals(mapExpected, mapFromStream);
    }

    @Test
    @DisplayName("если в фильтр передать () -> false, то результат будет пустым")
    void filterToEmptyMap() {
        Map<String, Person> mapFromStream = Streams.of(List.of(
                        new Person("name1", 1),
                        new Person("name2", 2),
                        new Person("name3", 3),
                        new Person("name4", 4)))
                .filter(person -> false)
                .toMap(Person::getName, Function.identity());

        Map<String, Person> mapExpected = Map.of();

        assertEquals(mapExpected, mapFromStream);
    }

    @Test
    @DisplayName("трансформация List<Person> в Map<String, String>")
    void transformPersonListToStringToStringMap() {
        Map<String, String> mapFromStream = Streams.of(List.of(
                        new Person("name1", 1),
                        new Person("name2", 2),
                        new Person("name3", 3)))
                .transform(Person::getName)
                .toMap(Function.identity(), Function.identity());

        Map<String, String> mapExpected = Map.of(
                "name1", "name1",
                "name2", "name2",
                "name3", "name3"
        );

        assertEquals(mapExpected, mapFromStream);
    }

    @Test
    @DisplayName("toMap работает корректно")
    void toMapWorksFine() {
        Map<String, Person> mapFromStream = Streams.of(List.of(
                        new Person("name1", 1),
                        new Person("name2", 2),
                        new Person("name3", 3)))
                .toMap(Person::getName, Function.identity());

        Map<String, Person> mapExpected = Map.of(
                "name1", new Person("name1", 1),
                "name2", new Person("name2", 2),
                "name3", new Person("name3", 3)
        );

        assertEquals(mapExpected, mapFromStream);
    }

    @Test
    @DisplayName("если изначальная коллекция пустая, то и результирующая тоже пустая")
    void toMapOfEmptyStreamReturnsEmptyMap() {
        Map<Person, Person> mapFromStream = Streams
                .of(Collections.<Person>emptyList())
                .toMap(Function.identity(), Function.identity());

        Map<Person, Person> mapExpected = Map.of();

        assertEquals(mapExpected, mapFromStream);
    }

    @Test
    @DisplayName("кидает исключение если в toMap встречаются дубликаты ключей")
    void toMapThrowsExceptionWhenKeysDuplicated() {
        Streams<Person> mapFromStream = Streams.of(List.of(
                new Person("name2", 1),
                new Person("name2", 2)));

        assertThrows(IllegalArgumentException.class,
                () -> mapFromStream.toMap(Person::getName, Function.identity()));
    }
}