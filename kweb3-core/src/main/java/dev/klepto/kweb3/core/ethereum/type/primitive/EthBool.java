package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Container for <code>ethereum bool</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthBool extends EthUint {

    /**
     * True bool constant.
     */
    public static final EthBool TRUE = bool(true);

    /**
     * False bool constant.
     */
    public static final EthBool FALSE = bool(false);

    /**
     * Creates a new instance of bool with given {@code boolean} value.
     *
     * @param value the boolean value
     * @return a new instance of bool with given {@code boolean} value
     */
    public static EthBool bool(boolean value) {
        return new EthBool(ValueRef.of(value ? 1 : 0));
    }

    /**
     * Creates a new instance of bool with given {@link Number} value.
     *
     * @param number the value
     * @return a new instance of bool with given {@link Number} value
     */
    public static EthBool bool(Number number) {
        return new EthBool(ValueRef.of(number));
    }

    /**
     * Creates a new instance of bool with given {@link ValueRef} value.
     *
     * @param ref the value
     * @return a new instance of bool with given {@link ValueRef} value
     */
    public static EthBool bool(ValueRef<?> ref) {
        return new EthBool(ref);
    }

    /**
     * Creates a new instance of bool with given hexadecimal {@link String} value.
     *
     * @param hex the value
     * @return a new instance of bool with given hexadecimal {@link String} value
     */
    public static EthBool bool(String hex) {
        return new EthBool(ValueRef.of(false, hex));
    }

    /**
     * Creates a new instance of bool with given {@code byte array} value.
     *
     * @param bytes the value
     * @return a new instance of bool with given {@code byte array} value
     */
    public static @NotNull EthBool bool(byte @NotNull [] bytes) {
        return new EthBool(ValueRef.of(false, bytes));
    }

    /**
     * Creates a new instance of bool with given {@link ByteBuffer} value.
     *
     * @param buffer the value
     * @return a new instance of bool with given {@link ByteBuffer} value
     */
    public static EthBool bool(ByteBuffer buffer) {
        return new EthBool(ValueRef.of(false, buffer));
    }

    private EthBool(ValueRef<?> valueRef) {
        super(valueRef, 8);
    }

    public boolean check() {
        return equals(1);
    }

    @Override
    @Nullable
    public InvalidValue validate() {
        val value = toBigInteger();
        if (!value.equals(BigInteger.ONE) && !value.equals(BigInteger.ZERO)) {
            return InvalidValue.OUT_OF_RANGE;
        }
        return null;
    }

    @Override
    @NotNull
    public String toString() {
        return "bool(" + check() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(check());
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return EthNumeric.valueEquals(this, object);
    }

    @Override
    public boolean matches(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthBool other)) {
            return false;
        }
        return check() == other.check();
    }

}
