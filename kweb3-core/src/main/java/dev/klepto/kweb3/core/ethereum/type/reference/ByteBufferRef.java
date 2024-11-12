package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.util.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A weak reference to {@link ByteBuffer} representing an {@link EthNumeric} value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ByteBufferRef implements ValueRef<ByteBuffer> {

    private final boolean signed;
    private final ByteBuffer value;

    public ByteBufferRef(boolean signed, ByteBuffer value) {
        this.signed = signed;
        this.value = value.asReadOnlyBuffer();
    }

    @Override
    public boolean toBoolean() {
        return toByte() == 1;
    }

    @Override
    public byte toByte() {
        return isReadable() ? value.get(value.position()) : 0;
    }

    @Override
    public short toShort() {
        return isReadable(Short.BYTES) ? value.getShort(value.position()) : toByte();
    }

    @Override
    public int toInt() {
        return isReadable(Integer.BYTES) ? value.getInt(value.position()) : toShort();
    }

    @Override
    public long toLong() {
        return isReadable(Long.BYTES) ? value.getLong(value.position()) : toInt();
    }

    @Override
    public float toFloat() {
        return isReadable(Float.BYTES) ? (float) toInt() : (float) toShort();
    }

    @Override
    public double toDouble() {
        return isReadable(Double.BYTES) ? (double) toLong() : (double) toFloat();
    }

    @Override
    public BigInteger toBigInteger() {
        if (!isReadable()) {
            return BigInteger.ZERO;
        }
        if (signed) {
            return new BigInteger(toByteArray());
        } else {
            return new BigInteger(1, toByteArray());
        }
    }

    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(toBigInteger());
    }

    @Override
    public String toHex() {
        return isReadable() ? Hex.toHex(toByteArray()) : EMPTY_HEX;
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return value;
    }

    @Override
    public byte[] toByteArray() {
        if (!isReadable()) {
            return EMPTY;
        }
        byte[] bytes = new byte[value.remaining()];
        value.get(value.position(), bytes);
        return bytes;
    }

    @Override
    public String toPlainString() {
        return toBigInteger().toString();
    }

    private boolean isReadable() {
        return isReadable(1);
    }

    private boolean isReadable(int bytes) {
        return value.remaining() >= bytes;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ByteBufferRef bufferRef) {
            return value.equals(bufferRef.value);
        }
        return false;
    }

    private static final byte[] EMPTY = new byte[0];
    private static final String EMPTY_HEX = "0x0";
}
