package com.ust.entitygenerator;

/**
 * This throws when there is a code generation issue.
 */
public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException(String message) {
        super(message);
    }
}
