package dev.klepto.kweb3.core.ethereum.type;

import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Represents an ethereum data type that's backed by numeric value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthNumeric<T extends EthNumeric<T>> extends EthValue, ValueRef<Object> {

    /**
     * Returns the value size in either bits or bytes, depending on ethereum type naming convention.
     *
     * @return the value size
     */
    int size();

    /**
     * The value size in bytes.
     *
     * @return the size in bytes
     */
    int bitSize();

    /**
     * The value size in bytes.
     *
     * @return the size in bytes
     */
    default int byteSize() {
        return bitSize() / 8;
    }

    /**
     * Creates a new instance of numeric type with given bit or byte size (depending on ethereum type naming
     * convention).
     *
     * @param size the size in bits or bytes
     * @return a new instance of numeric type with given size
     */
    T size(int size);

    /**
     * Returns the value of this numeric value as {@link ValueRef}.
     *
     * @return the value as value reference
     */
    ValueRef<?> valueRef();

    /**
     * Creates a new instance of numeric type with given value reference.
     *
     * @param valueRef the value reference
     * @return a new instance of numeric type with given value reference
     */
    @NotNull
    T valueRef(@NotNull ValueRef<?> valueRef);

    /**
     * Returns `true` if given numeric value is equal to value of given object.
     *
     * @param numeric the numeric value
     * @param object  the object to compare
     * @return `true` if given numeric value is equal to value of given object, `false` otherwise
     */
    static boolean valueEquals(Math<?> numeric, Object object) {
        if (object instanceof EthNumeric<?> other) {
            if (numeric.valueRef().getClass() == other.valueRef().getClass()) {
                return numeric.valueRef().equals(other.valueRef());
            }
        }
        if (object instanceof Number number) {
            return numeric.equals(number);
        }
        return false;
    }

    /**
     * Implementation of uniform mathematical operations for {@link EthNumeric} values.
     *
     * @param <T> the ethereum type of the numeric value
     */
    interface Math<T extends EthNumeric<T>> extends EthNumeric<T>, Comparable<Number> {
        /**
         * Returns true if this numeric value is equal to the specified number.
         *
         * @param number the number to compare
         * @return true if this numeric value is equal to the specified number
         */
        default boolean equals(Number number) {
            return compareTo(number) == 0;
        }

        /**
         * Compares this numeric value with another number value.
         *
         * @param other the other number value
         * @return -1, 0 or 1 as this numeric value is numerically less than, equal to, or greater than
         * <code>other</code>.
         */
        @Override
        default int compareTo(@NotNull Number other) {
            return toBigInteger().compareTo(parseBigDecimal(other).toBigInteger());
        }

        /**
         * Adds the specified number to this numeric value.
         *
         * @param other the number to add
         * @return a new instance of numeric type with the result of the addition
         */
        @NotNull
        default T plus(@NotNull Number other) {
            val result = toBigDecimal().add(parseBigDecimal(other));
            return valueRef(ValueRef.of(result));
        }

        /**
         * Subtracts the specified number from this numeric value.
         *
         * @param other the number to subtract
         * @return a new instance of numeric type with the result of the subtraction
         */
        @NotNull
        default T minus(@NotNull Number other) {
            val result = toBigDecimal().subtract(parseBigDecimal(other));
            return valueRef(ValueRef.of(result));
        }

        /**
         * Multiplies this numeric value by the specified number.
         *
         * @param other the number to multiply
         * @return a new instance of numeric type with the result of the multiplication
         */
        @NotNull
        default T times(@NotNull Number other) {
            val result = parseBigDecimal(this).multiply(parseBigDecimal(other));
            return valueRef(ValueRef.of(result));
        }

        /**
         * Divides this numeric value by the specified number.
         *
         * @param other the number to divide
         * @return a new instance of numeric type with the result of the division
         */
        @NotNull
        default T div(@NotNull Number other) {
            val result = parseBigDecimal(this).divide(parseBigDecimal(other), RoundingMode.FLOOR);
            return valueRef(ValueRef.of(result));
        }

        /**
         * Returns the remainder of the division of this numeric value by the specified number.
         *
         * @param other the number to divide by
         * @return a new instance of numeric type with the remainder of the division
         */
        @NotNull
        default T rem(@NotNull Number other) {
            val result = parseBigDecimal(this).remainder(parseBigDecimal(other));
            return valueRef(ValueRef.of(result));
        }

        /**
         * Returns this numeric value raised to the power of the specified number.
         *
         * @param exponent the power to raise this numeric value to
         * @return a new instance of numeric type with the result of the power operation
         */
        @NotNull
        default T pow(int exponent) {
            val result = parseBigDecimal(this).pow(exponent);
            return valueRef(ValueRef.of(result));
        }

        /**
         * Returns this numeric value.
         *
         * @return a new instance of this numeric value
         */
        @NotNull
        default T unaryPlus() {
            return valueRef(ValueRef.of(toBigDecimal()));
        }

        /**
         * Returns the negation of this numeric value.
         *
         * @return a new instance of numeric type with the negation of this value
         */
        @NotNull
        default T unaryMinus() {
            val result = toBigDecimal().negate();
            return valueRef(ValueRef.of(result));
        }

        /**
         * Returns the value of this numeric value, throwing an exception if the value overflows a {@code long}.
         *
         * @return the argument as a {@code long}.
         * @throws ArithmeticException if the {@code argument} overflows an long
         */
        default long longValueExact() {
            return toBigDecimal().longValueExact();
        }

        /**
         * Returns the value of this numeric value, throwing an exception if the value overflows an {@code int}.
         *
         * @return the argument as an {@code int}.
         * @throws ArithmeticException if the {@code argument} overflows an int
         */
        default int intValueExact() {
            return toBigDecimal().intValueExact();
        }

        /**
         * Returns the value of this numeric value, throwing an exception if the value overflows a {@code short}.
         *
         * @return the argument as a {@code short}.
         * @throws ArithmeticException if the {@code argument} overflows a short
         */
        default short shortValueExact() {
            return toBigDecimal().shortValueExact();
        }

        /**
         * Returns the value of this numeric value, throwing an exception if the value overflows a {@code byte}.
         *
         * @return the argument as a {@code byte}.
         * @throws ArithmeticException if the {@code argument} overflows a byte
         */
        default byte byteValueExact() {
            return toBigDecimal().byteValueExact();
        }

        /**
         * Parses the specified number as {@link BigDecimal}.
         *
         * @param number the number to parse
         * @return the number as big decimal
         */
        private static BigDecimal parseBigDecimal(EthNumeric<?> number) {
            return parseBigDecimal(number.toBigDecimal());
        }

        /**
         * Parses the specified number as {@link BigDecimal}.
         *
         * @param number the number to parse
         * @return the number as big decimal
         */
        private static BigDecimal parseBigDecimal(Number number) {
            var result = (BigDecimal) null;
            if (number instanceof BigDecimal value) {
                result = value;
            } else if (number instanceof BigInteger value) {
                result = new BigDecimal(value);
            } else if (number instanceof Float value) {
                result = BigDecimal.valueOf(value);
            } else if (number instanceof Double value) {
                result = BigDecimal.valueOf(value);
            } else if (number instanceof EthNumeric<?> value) {
                result = value.toBigDecimal();
            } else {
                result = new BigDecimal(number.longValue());
            }
            if (result.scale() < DEFAULT_PRECISION) {
                return result.setScale(DEFAULT_PRECISION, RoundingMode.FLOOR);
            }
            return result;
        }

        /**
         * Default precision for numeric values.
         */
        int DEFAULT_PRECISION = 18;
    }

}
