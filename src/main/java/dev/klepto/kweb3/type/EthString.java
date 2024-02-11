package dev.klepto.kweb3.type;

import org.jetbrains.annotations.NotNull;

/**
 * Represents ethereum <code>string</code> data type.
 *
 * @param value the string value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthString(String value) implements EthType {

    /**
     * Empty string constant.
     */
    public static final EthString EMPTY = string("");

    @Override
    public String toString() {
        return "string(" + value + ")";
    }

    /* Solidity style string initializers */
    @NotNull
    public static EthString string(@NotNull String value) {
        return new EthString(value);
    }

}
