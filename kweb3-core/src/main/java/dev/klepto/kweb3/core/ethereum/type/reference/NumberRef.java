package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.util.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A weak reference to {@link Number} representing an {@link EthNumeric}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class NumberRef implements ValueRef<Number> {

    private final Number value;

    public NumberRef(Number value) {
        this.value = value;
    }

    @Override
    public boolean toBoolean() {
        return toByte() == 1;
    }

    @Override
    public byte toByte() {
        return value.byteValue();
    }

    @Override
    public short toShort() {
        return value.shortValue();
    }

    @Override
    public int toInt() {
        return value.intValue();
    }

    @Override
    public long toLong() {
        return value.longValue();
    }

    @Override
    public float toFloat() {
        return value.floatValue();
    }

    @Override
    public double toDouble() {
        return value.doubleValue();
    }

    @Override
    public BigInteger toBigInteger() {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toBigInteger();
        }
        return BigInteger.valueOf(toLong());
    }

    @Override
    public BigDecimal toBigDecimal() {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf(toDouble());
        }
        return new BigDecimal(toBigInteger());
    }

    @Override
    public String toHex() {
        return Hex.toHex(toByteArray());
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(toByteArray());
    }

    @Override
    public byte[] toByteArray() {
        return toBigInteger().toByteArray();
    }

    @Override
    public String toPlainString() {
        return toBigInteger().toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NumberRef numberRef) {
            return numberEquals(value, numberRef.value);
        }
        return false;
    }

    /**
     * Compares two numbers for equality.
     *
     * @param numberA number to compare
     * @param numberB number to compare
     * @return true if numbers are equal, false otherwise
     */
    private static boolean numberEquals(Number numberA, Number numberB) {
        if (numberA instanceof BigInteger && numberB instanceof BigInteger) {
            return numberA.equals(numberB);
        } else if (numberA instanceof BigDecimal && numberB instanceof BigDecimal) {
            return numberA.equals(numberB);
        } else if ((numberA instanceof Double || numberA instanceof Float)
                && (numberB instanceof Double || numberB instanceof Float)) {
            return numberA.doubleValue() == numberB.doubleValue();
        }
        return numberA.longValue() == numberB.longValue();
    }

}
