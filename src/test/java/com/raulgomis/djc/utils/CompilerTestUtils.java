package com.raulgomis.djc.utils;

import com.raulgomis.djc.DynamicCompilerUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class CompilerTestUtils {

    private static final String SPLIT_EXPRESSION = "\\.";

    private CompilerTestUtils() {
    }

    static CompilerTestInput getSource(String fileName) {
        try {
            URI uri = java.util.Objects.requireNonNull(DynamicCompilerUtils.class.getResource("/" + fileName)).toURI();
            Path path = Paths.get(uri);

            String source = Files.readString(path, StandardCharsets.UTF_8);
            String className = path.getFileName().toString().split(SPLIT_EXPRESSION)[0];

            return new CompilerTestInput(className, source);
        } catch (URISyntaxException | IOException e) {
            return CompilerTestInput.EMPTY;
        }
    }
}
