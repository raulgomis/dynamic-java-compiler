package com.raulgomis.djc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DynamicCompilerUtilsTest {

    private static Stream<Arguments> createURIInput() throws URISyntaxException {
        return Stream.of(
            Arguments.of("Home/Raul/Uri", new URI("Home/Raul/Uri")),
            Arguments.of("file://host/path", new URI("file://host/path"))
        );
    }
    @ParameterizedTest
    @MethodSource("createURIInput")
    void createURI(String input, URI expected) {
        assertEquals(expected, DynamicCompilerUtils.createURI(input));
    }

    @Test
    void createURIException() {
        assertThrows(RuntimeException.class, () -> DynamicCompilerUtils.createURI("::&"));
    }


    private static Stream<Arguments> getQualifiedClassNameInput() {
        return Stream.of(
            Arguments.of("org.name", "ClassName", "org.name.ClassName"),
            Arguments.of("", "ClassName", "ClassName"),
            Arguments.of(null, "ClassName", "ClassName")
        );
    }
    @ParameterizedTest
    @MethodSource("getQualifiedClassNameInput")
    void getQualifiedClassName(String packageName, String className, String expected) {
        assertEquals(expected, DynamicCompilerUtils.getQualifiedClassName(packageName, className));
    }

    private static Stream<Arguments> getClassNameWithExtInput() {
        return Stream.of(
            Arguments.of("Test", "Test.java"),
            Arguments.of("Test/Test/Test", "Test/Test/Test.java"),
            Arguments.of("Test.Inner", "Test.Inner.java")
        );
    }
    @ParameterizedTest
    @MethodSource("getClassNameWithExtInput")
    void getClassNameWithExt(String input, String expected) {
        assertEquals(expected, DynamicCompilerUtils.getClassNameWithExt(input));
    }


    private static Stream<Arguments> isEmptyInput() {
        return Stream.of(
            Arguments.of("org.name", false),
            Arguments.of("", true),
            Arguments.of(null, true)
        );
    }
    @ParameterizedTest
    @MethodSource("isEmptyInput")
    void isEmpty(String input, boolean expected) {
        assertEquals(expected, DynamicCompilerUtils.isEmpty(input));
    }
}