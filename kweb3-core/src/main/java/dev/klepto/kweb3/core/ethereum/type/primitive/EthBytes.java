package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import lombok.experimental.Delegate;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Container for <code>ethereum bytes</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthBytes extends Number implements EthNumeric<EthBytes>, EthNumeric.Math<EthBytes> {

    /**
     * Maximum bytes size for ethereum <code>bytes</code> type.
     */
    public static final int MAX_BYTE_SIZE = 32;

    /**
     * Indicator constants for dynamic ethereum <code>bytes</code> type.
     */
    public static final int DYNAMIC_BYTE_SIZE = 0;

    /**
     * Creates a new instance of uint256 with given {@link Number} value.
     *
     * @param number the value
     * @return a new instance of uint256 with given {@link Number} value
     */
    public static @NotNull EthBytes bytes(@NotNull Number number) {
        return new EthBytes(ValueRef.of(number), DYNAMIC_BYTE_SIZE);
    }

    /**
     * Creates a new instance of bytes with given {@link ValueRef} value.
     *
     * @param ref the value
     * @return a new instance of bytes with given {@link ValueRef} value
     */
    public static @NotNull EthBytes bytes(@NotNull ValueRef<?> ref) {
        return new EthBytes(ref, DYNAMIC_BYTE_SIZE);
    }

    /**
     * Creates a new instance of bytes with given hexadecimal {@link String} value.
     *
     * @param hex the value
     * @return a new instance of bytes with given hexadecimal {@link String} value
     */
    public static @NotNull EthBytes bytes(@NotNull String hex) {
        return new EthBytes(ValueRef.of(false, hex), DYNAMIC_BYTE_SIZE);
    }

    /**
     * Creates a new instance of bytes with given {@code byte array}  value.
     *
     * @param bytes the value
     * @return a new instance of bytes with given {@code byte array} value
     */
    public static @NotNull EthBytes bytes(byte @NotNull [] bytes) {
        return new EthBytes(ValueRef.of(false, bytes), DYNAMIC_BYTE_SIZE);
    }

    /**
     * Creates a new instance of bytes with given {@link ByteBuffer} value.
     *
     * @param buffer the value
     * @return a new instance of bytes with given {@link ByteBuffer} value
     */
    public static @NotNull EthBytes bytes(@NotNull ByteBuffer buffer) {
        return new EthBytes(ValueRef.of(false, buffer), DYNAMIC_BYTE_SIZE);
    }

    @Delegate
    @NotNull
    private final ValueRef<?> valueRef;
    private final int byteSize;

    private EthBytes(@NotNull ValueRef<?> valueRef, int byteSize) {
        this.valueRef = valueRef;
        this.byteSize = byteSize;
    }

    @Override
    public int size() {
        return byteSize;
    }

    @Override
    public int bitSize() {
        return byteSize * 8;
    }

    @Override
    public int byteSize() {
        return byteSize;
    }

    @Override
    public @Nullable InvalidValue validate() {
        if (byteSize > MAX_BYTE_SIZE || byteSize < DYNAMIC_BYTE_SIZE) {
            return InvalidValue.UNSUPPORTED_SIZE;
        } else if (byteSize != DYNAMIC_BYTE_SIZE && valueRef.toByteBuffer().remaining() > byteSize) {
            return InvalidValue.OUT_OF_RANGE;
        }
        return null;
    }

    @Override
    public EthBytes size(int size) {
        return new EthBytes(valueRef, size);
    }

    @Override
    public ValueRef<?> valueRef() {
        return valueRef;
    }

    @Override
    public @NotNull EthBytes valueRef(@NotNull ValueRef<?> valueRef) {
        return new EthBytes(valueRef, byteSize);
    }

    @Override
    @NotNull
    public String toString() {
        val sizeString = byteSize > DYNAMIC_BYTE_SIZE ? byteSize : "";
        return "bytes" + sizeString + "(" + this.toHex() + ")";
    }

    @Override
    public int hashCode() {
        return toBigInteger().hashCode();
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
        if (!(object instanceof EthBytes other)) {
            return false;
        }
        return this.toHex().equals(other.toHex());
    }

    @Override
    public int intValue() {
        return toInt();
    }

    @Override
    public long longValue() {
        return toLong();
    }

    @Override
    public float floatValue() {
        return toFloat();
    }

    @Override
    public double doubleValue() {
        return toDouble();
    }
}
