package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import lombok.experimental.Delegate;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Container for <code>ethereum int</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthInt extends Number implements EthNumeric<EthInt>, EthNumeric.Math<EthInt> {

    /**
     * Maximum bit size for ethereum <code>int</code> type.
     */
    public static final int MAX_BIT_SIZE = 256;

    /**
     * Zero <code>int</code> constant.
     */
    public static final EthInt ZERO = int256(0);

    /**
     * One <code>int</code> constant.
     */
    public static final EthInt ONE = int256(1);

    /**
     * Two <code>int</code> constant.
     */
    public static final EthInt TWO = int256(2);

    /**
     * Ten <code>int</code> constant.
     */
    public static final EthInt TEN = int256(10);

    /**
     * Creates a new instance of int256 with given {@link Number} value.
     *
     * @param number the value
     * @return a new instance of int256 with given {@link Number} value
     */
    public static @NotNull EthInt int256(@NotNull Number number) {
        return new EthInt(ValueRef.of(number), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given {@link ValueRef} value.
     *
     * @param ref the value
     * @return a new instance of int256 with given {@link ValueRef} value
     */
    public static @NotNull EthInt int256(@NotNull ValueRef<?> ref) {
        return new EthInt(ref, MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given hexadecimal {@link String} value.
     *
     * @param hex the value
     * @return a new instance of int256 with given hexadecimal {@link String} value
     */
    public static @NotNull EthInt int256(@NotNull String hex) {
        return new EthInt(ValueRef.of(true, hex), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given {@code byte array} value.
     *
     * @param bytes the value
     * @return a new instance of int256 with given {@code byte array} value
     */
    public static @NotNull EthInt int256(byte @NotNull [] bytes) {
        return new EthInt(ValueRef.of(true, bytes), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given {@link ByteBuffer} value.
     *
     * @param buffer the value
     * @return a new instance of int256 with given {@link ByteBuffer} value
     */
    public static @NotNull EthInt int256(@NotNull ByteBuffer buffer) {
        return new EthInt(ValueRef.of(true, buffer), MAX_BIT_SIZE);
    }

    @Delegate
    @NotNull
    private final ValueRef<?> valueRef;
    private final int bitSize;

    private EthInt(@NotNull ValueRef<?> valueRef, int bitSize) {
        this.valueRef = valueRef;
        this.bitSize = bitSize;
    }

    @Override
    public InvalidValue validate() {
        val value = toBigInteger();
        if (value.bitLength() > bitSize) {
            return InvalidValue.OUT_OF_RANGE;
        }
        if (bitSize > MAX_BIT_SIZE || bitSize < 1) {
            return InvalidValue.UNSUPPORTED_SIZE;
        }
        return null;
    }

    @Override
    public EthInt size(int bitSize) {
        return new EthInt(valueRef, bitSize);
    }

    @Override
    public ValueRef<?> valueRef() {
        return valueRef;
    }

    @Override
    public @NotNull EthInt valueRef(@NotNull ValueRef<?> valueRef) {
        return new EthInt(valueRef, bitSize);
    }

    @Override
    public int size() {
        return bitSize;
    }

    @Override
    public int bitSize() {
        return bitSize;
    }

    @Override
    @NotNull
    public String toString() {
        return "int" + bitSize + "(" + toPlainString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(bitSize, toBigInteger());
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return EthNumeric.valueEquals(this, object);
    }

    public boolean matches(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthInt other)) {
            return false;
        }
        return bitSize == other.bitSize && toBigInteger().equals(other.toBigInteger());
    }

    // Required for Number implementation.
    @Override
    public int intValue() {
        return toInt();
    }

    // Required for Number implementation.
    @Override
    public long longValue() {
        return toLong();
    }

    // Required for Number implementation.
    @Override
    public float floatValue() {
        return toFloat();
    }

    // Required for Number implementation.
    @Override
    public double doubleValue() {
        return toDouble();
    }

    // Kotlin operator function, cannot be inherited from an interface.
    public EthInt inc() {
        return plus(1);
    }

    // Kotlin operator function, cannot be inherited from an interface.
    public EthInt dec() {
        return minus(1);
    }

    public static final EthInt MIN_INT8 = int256(BigInteger.valueOf(-2).pow(7));
    public static final EthInt MIN_INT16 = int256(BigInteger.valueOf(-2).pow(15));
    public static final EthInt MIN_INT24 = int256(BigInteger.valueOf(-2).pow(23));
    public static final EthInt MIN_INT32 = int256(BigInteger.valueOf(-2).pow(31));
    public static final EthInt MIN_INT40 = int256(BigInteger.valueOf(-2).pow(39));
    public static final EthInt MIN_INT48 = int256(BigInteger.valueOf(-2).pow(47));
    public static final EthInt MIN_INT56 = int256(BigInteger.valueOf(-2).pow(55));
    public static final EthInt MIN_INT64 = int256(BigInteger.valueOf(-2).pow(63));
    public static final EthInt MIN_INT72 = int256(BigInteger.valueOf(-2).pow(71));
    public static final EthInt MIN_INT80 = int256(BigInteger.valueOf(-2).pow(79));
    public static final EthInt MIN_INT88 = int256(BigInteger.valueOf(-2).pow(87));
    public static final EthInt MIN_INT96 = int256(BigInteger.valueOf(-2).pow(95));
    public static final EthInt MIN_INT104 = int256(BigInteger.valueOf(-2).pow(103));
    public static final EthInt MIN_INT112 = int256(BigInteger.valueOf(-2).pow(111));
    public static final EthInt MIN_INT120 = int256(BigInteger.valueOf(-2).pow(119));
    public static final EthInt MIN_INT128 = int256(BigInteger.valueOf(-2).pow(127));
    public static final EthInt MIN_INT136 = int256(BigInteger.valueOf(-2).pow(135));
    public static final EthInt MIN_INT144 = int256(BigInteger.valueOf(-2).pow(143));
    public static final EthInt MIN_INT152 = int256(BigInteger.valueOf(-2).pow(151));
    public static final EthInt MIN_INT160 = int256(BigInteger.valueOf(-2).pow(159));
    public static final EthInt MIN_INT168 = int256(BigInteger.valueOf(-2).pow(167));
    public static final EthInt MIN_INT176 = int256(BigInteger.valueOf(-2).pow(175));
    public static final EthInt MIN_INT184 = int256(BigInteger.valueOf(-2).pow(183));
    public static final EthInt MIN_INT192 = int256(BigInteger.valueOf(-2).pow(191));
    public static final EthInt MIN_INT200 = int256(BigInteger.valueOf(-2).pow(199));
    public static final EthInt MIN_INT208 = int256(BigInteger.valueOf(-2).pow(207));
    public static final EthInt MIN_INT216 = int256(BigInteger.valueOf(-2).pow(215));
    public static final EthInt MIN_INT224 = int256(BigInteger.valueOf(-2).pow(223));
    public static final EthInt MIN_INT232 = int256(BigInteger.valueOf(-2).pow(231));
    public static final EthInt MIN_INT240 = int256(BigInteger.valueOf(-2).pow(239));
    public static final EthInt MIN_INT248 = int256(BigInteger.valueOf(-2).pow(247));
    public static final EthInt MIN_INT256 = int256(BigInteger.valueOf(-2).pow(255));

    public static final EthInt MAX_INT8 = int256(BigInteger.valueOf(2).pow(7).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT16 = int256(BigInteger.valueOf(2).pow(15).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT24 = int256(BigInteger.valueOf(2).pow(23).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT32 = int256(BigInteger.valueOf(2).pow(31).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT40 = int256(BigInteger.valueOf(2).pow(39).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT48 = int256(BigInteger.valueOf(2).pow(47).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT56 = int256(BigInteger.valueOf(2).pow(55).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT64 = int256(BigInteger.valueOf(2).pow(63).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT72 = int256(BigInteger.valueOf(2).pow(71).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT80 = int256(BigInteger.valueOf(2).pow(79).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT88 = int256(BigInteger.valueOf(2).pow(87).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT96 = int256(BigInteger.valueOf(2).pow(95).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT104 = int256(BigInteger.valueOf(2).pow(103).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT112 = int256(BigInteger.valueOf(2).pow(111).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT120 = int256(BigInteger.valueOf(2).pow(119).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT128 = int256(BigInteger.valueOf(2).pow(127).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT136 = int256(BigInteger.valueOf(2).pow(135).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT144 = int256(BigInteger.valueOf(2).pow(143).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT152 = int256(BigInteger.valueOf(2).pow(151).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT160 = int256(BigInteger.valueOf(2).pow(159).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT168 = int256(BigInteger.valueOf(2).pow(167).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT176 = int256(BigInteger.valueOf(2).pow(175).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT184 = int256(BigInteger.valueOf(2).pow(183).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT192 = int256(BigInteger.valueOf(2).pow(191).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT200 = int256(BigInteger.valueOf(2).pow(199).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT208 = int256(BigInteger.valueOf(2).pow(207).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT216 = int256(BigInteger.valueOf(2).pow(215).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT224 = int256(BigInteger.valueOf(2).pow(223).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT232 = int256(BigInteger.valueOf(2).pow(231).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT240 = int256(BigInteger.valueOf(2).pow(239).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT248 = int256(BigInteger.valueOf(2).pow(247).subtract(BigInteger.ONE));
    public static final EthInt MAX_INT256 = int256(BigInteger.valueOf(2).pow(255).subtract(BigInteger.ONE));
}
