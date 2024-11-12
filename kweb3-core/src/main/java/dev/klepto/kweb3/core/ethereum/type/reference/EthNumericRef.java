package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A weak reference to {@link EthNumeric} representing another {@link EthNumeric}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthNumericRef implements ValueRef<EthNumeric<?>> {

    private final EthNumeric<?> value;

    public EthNumericRef(EthNumeric<?> value) {
        this.value = value;
    }

    @Override
    public boolean toBoolean() {
        return value.toBoolean();
    }

    @Override
    public byte toByte() {
        return value.toByte();
    }

    @Override
    public short toShort() {
        return value.toShort();
    }

    @Override
    public int toInt() {
        return value.toInt();
    }

    @Override
    public long toLong() {
        return value.toLong();
    }

    @Override
    public float toFloat() {
        return value.toFloat();
    }

    @Override
    public double toDouble() {
        return value.toDouble();
    }

    @Override
    public BigInteger toBigInteger() {
        return value.toBigInteger();
    }

    @Override
    public BigDecimal toBigDecimal() {
        return value.toBigDecimal();
    }

    @Override
    public String toHex() {
        return value.toHex();
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return value.toByteBuffer();
    }

    @Override
    public byte[] toByteArray() {
        return value.toByteArray();
    }

    @Override
    public String toPlainString() {
        return value.toPlainString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EthNumericRef numericRef) {
            return value.equals(numericRef.value);
        }
        return false;
    }
}
