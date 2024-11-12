package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.util.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A weak reference to data structure representing an {@link EthNumeric} value.
 *
 * @author Augustinas R. <http://github.com/klepto>
 */
public interface ValueRef<T> {

    /**
     * Creates a new reference to a given value.
     *
     * @param value the value to reference
     * @return a new reference to the given value
     */
    static NumberRef of(Number value) {
        return new NumberRef(value);
    }

    /**
     * Creates a new reference to a given value.
     *
     * @param value the value to reference
     * @return a new reference to the given value
     */
    static EthNumericRef of(EthNumeric<?> value) {
        return new EthNumericRef(value);
    }

    /**
     * Creates a new reference to a given value.
     *
     * @param value the value to reference
     * @return a new reference to the given value
     */
    static ByteBufferRef of(boolean signed, String value) {
        return of(signed, Hex.toByteArray(value));
    }

    /**
     * Creates a new reference to a given value.
     *
     * @param value the value to reference
     * @return a new reference to the given value
     */
    static ByteBufferRef of(boolean signed, byte[] value) {
        return new ByteBufferRef(signed, ByteBuffer.wrap(value));
    }

    /**
     * Creates a new reference to a given value.
     *
     * @param value the value to reference
     * @return a new reference to the given value
     */
    static ByteBufferRef of(boolean signed, ByteBuffer value) {
        return new ByteBufferRef(signed, value);
    }
    
    /**
     * Converts the value to a {@link Boolean}.
     *
     * @return value as a {@link Boolean}
     */
    boolean toBoolean();

    /**
     * Converts the value to a {@link Byte}.
     *
     * @return value as a {@link Byte}
     */
    byte toByte();

    /**
     * Converts the value to a {@link Short}.
     *
     * @return value as a {@link Short}
     */
    short toShort();

    /**
     * Converts the value to a {@link Integer}.
     *
     * @return value as a {@link Integer}
     */
    int toInt();

    /**
     * Converts the value to a {@link Long}.
     *
     * @return value as a {@link Long}
     */
    long toLong();

    /**
     * Converts the value to a {@link Float}.
     *
     * @return value as a {@link Float}
     */
    float toFloat();

    /**
     * Converts the value to a {@link Double}.
     *
     * @return value as a {@link Double}
     */
    double toDouble();

    /**
     * Converts the value to a {@link BigInteger}.
     *
     * @return value as a {@link BigInteger}
     */
    BigInteger toBigInteger();

    /**
     * Converts the value to a {@link BigDecimal}.
     *
     * @return value as a {@link BigDecimal}
     */
    BigDecimal toBigDecimal();

    /**
     * Converts the value to a hexadecimal {@link String}.
     *
     * @return value as a hexadecimal {@link String}
     */
    String toHex();

    /**
     * Converts the value to a {@link ByteBuffer}.
     *
     * @return value as a {@link ByteBuffer}
     */
    ByteBuffer toByteBuffer();

    /**
     * Converts the value to a byte array.
     *
     * @return value as a byte array
     */
    byte[] toByteArray();

    /**
     * Converts the value to a plain {@link String}.
     *
     * @return value as a plain {@link String}
     */
    String toPlainString();

}
