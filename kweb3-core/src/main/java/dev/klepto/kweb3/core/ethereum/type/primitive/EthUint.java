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
 * Container for <code>ethereum uint</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthUint extends Number implements EthNumeric<EthUint>, EthNumeric.Math<EthUint> {

    /**
     * Maximum bit size for ethereum <code>uint</code> type.
     */
    public static final int MAX_BIT_SIZE = 256;

    /**
     * Zero <code>uint</code> constant.
     */
    public static final EthUint ZERO = uint256(0);

    /**
     * One <code>uint</code> constant.
     */
    public static final EthUint ONE = uint256(1);

    /**
     * Two <code>uint</code> constant.
     */
    public static final EthUint TWO = uint256(2);

    /**
     * Ten <code>uint</code> constant.
     */
    public static final EthUint TEN = uint256(10);

    /**
     * Creates a new instance of uint256 with given {@link Number} value.
     *
     * @param number the value
     * @return a new instance of uint256 with given {@link Number} value
     */
    public static @NotNull EthUint uint256(@NotNull Number number) {
        return new EthUint(ValueRef.of(number), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of uint256 with given {@link ValueRef} value.
     *
     * @param ref the value
     * @return a new instance of uint256 with given {@link ValueRef} value
     */
    public static @NotNull EthUint uint256(@NotNull ValueRef<?> ref) {
        return new EthUint(ref, MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of uint256 with given hexadecimal {@link String} value.
     *
     * @param hex the value
     * @return a new instance of uint256 with given hexadecimal {@link String} value
     */
    public static @NotNull EthUint uint256(@NotNull String hex) {
        return new EthUint(ValueRef.of(false, hex), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of uint256 with given {@code byte array} value.
     *
     * @param bytes the value
     * @return a new instance of uint256 with given {@code byte array} value
     */
    public static @NotNull EthUint uint256(byte @NotNull [] bytes) {
        return new EthUint(ValueRef.of(false, bytes), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of uint256 with given {@link ByteBuffer} value.
     *
     * @param buffer the value
     * @return a new instance of uint256 with given {@link ByteBuffer} value
     */
    public static @NotNull EthUint uint256(@NotNull ByteBuffer buffer) {
        return new EthUint(ValueRef.of(false, buffer), MAX_BIT_SIZE);
    }

    @Delegate
    @NotNull
    private final ValueRef<?> valueRef;
    private final int bitSize;

    protected EthUint(@NotNull ValueRef<?> valueRef, int bitSize) {
        this.valueRef = valueRef;
        this.bitSize = bitSize;
    }

    @Override
    public InvalidValue validate() {
        val value = toBigInteger();
        if (value.bitLength() > bitSize || value.compareTo(BigInteger.ZERO) < 0) {
            return InvalidValue.OUT_OF_RANGE;
        }
        if (bitSize > MAX_BIT_SIZE || bitSize < 1) {
            return InvalidValue.UNSUPPORTED_SIZE;
        }
        return null;
    }

    @Override
    public EthUint size(int bitSize) {
        return new EthUint(valueRef, bitSize);
    }

    @Override
    public @NotNull ValueRef<?> valueRef() {
        return valueRef;
    }

    @Override
    public @NotNull EthUint valueRef(@NotNull ValueRef<?> valueRef) {
        return new EthUint(valueRef, bitSize);
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
    public String toString() {
        return "uint" + bitSize + "(" + toPlainString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(bitSize, toBigInteger());
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return EthNumeric.valueEquals(this, object);
    }

    public boolean matches(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthUint other)) {
            return false;
        }
        return bitSize == other.bitSize && toBigInteger().equals(other.toBigInteger());
    }

    /* Required by inheriting Number */
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
    public EthUint inc() {
        return plus(1);
    }

    // Kotlin operator function, cannot be inherited from an interface.
    public EthUint dec() {
        return minus(1);
    }

    public static final EthUint MIN_UINT = uint256(BigInteger.ZERO);

    public static final EthUint MAX_UINT8 = uint256(BigInteger.valueOf(2).pow(8).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT16 = uint256(BigInteger.valueOf(2).pow(16).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT24 = uint256(BigInteger.valueOf(2).pow(24).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT32 = uint256(BigInteger.valueOf(2).pow(32).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT40 = uint256(BigInteger.valueOf(2).pow(40).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT48 = uint256(BigInteger.valueOf(2).pow(48).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT56 = uint256(BigInteger.valueOf(2).pow(56).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT64 = uint256(BigInteger.valueOf(2).pow(64).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT72 = uint256(BigInteger.valueOf(2).pow(72).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT80 = uint256(BigInteger.valueOf(2).pow(80).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT88 = uint256(BigInteger.valueOf(2).pow(88).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT96 = uint256(BigInteger.valueOf(2).pow(96).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT104 = uint256(BigInteger.valueOf(2).pow(104).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT112 = uint256(BigInteger.valueOf(2).pow(112).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT120 = uint256(BigInteger.valueOf(2).pow(120).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT128 = uint256(BigInteger.valueOf(2).pow(128).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT136 = uint256(BigInteger.valueOf(2).pow(136).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT144 = uint256(BigInteger.valueOf(2).pow(144).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT152 = uint256(BigInteger.valueOf(2).pow(152).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT160 = uint256(BigInteger.valueOf(2).pow(160).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT168 = uint256(BigInteger.valueOf(2).pow(168).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT176 = uint256(BigInteger.valueOf(2).pow(176).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT184 = uint256(BigInteger.valueOf(2).pow(184).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT192 = uint256(BigInteger.valueOf(2).pow(192).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT200 = uint256(BigInteger.valueOf(2).pow(200).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT208 = uint256(BigInteger.valueOf(2).pow(208).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT216 = uint256(BigInteger.valueOf(2).pow(216).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT224 = uint256(BigInteger.valueOf(2).pow(224).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT232 = uint256(BigInteger.valueOf(2).pow(232).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT240 = uint256(BigInteger.valueOf(2).pow(240).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT248 = uint256(BigInteger.valueOf(2).pow(248).subtract(BigInteger.ONE));
    public static final EthUint MAX_UINT256 = uint256(BigInteger.valueOf(2).pow(256).subtract(BigInteger.ONE));
}
