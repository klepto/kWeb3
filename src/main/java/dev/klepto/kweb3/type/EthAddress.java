package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.Hex;
import dev.klepto.kweb3.util.hash.Keccak256;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static dev.klepto.kweb3.util.Conditions.require;
import static dev.klepto.kweb3.util.Hex.stripPrefix;
import static dev.klepto.kweb3.util.Hex.toBigInteger;

/**
 * Represents ethereum <code>address</code> data type.
 *
 * @param value the integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthAddress(BigInteger value) implements EthNumericType {

    /**
     * Zero address constant.
     */
    public static final EthAddress ZERO = address("0x0");

    public EthAddress {
        require(value.bitLength() <= size(), "Malformed address: {}", Hex.toHex(value));
    }

    @Override
    public int size() {
        return 160;
    }

    @Override
    public String toString() {
        return "address(" + toChecksumHex() + ")";
    }

    /* Solidity style address initializers */

    /**
     * Converts {@link BigInteger} to ethereum address type.
     */
    public static EthAddress address(BigInteger value) {
        return new EthAddress(value);
    }

    /**
     * Converts hexadecimal string to ethereum address type.
     */
    public static EthAddress address(String hex) {
        hex = stripPrefix(Keccak256.keccak256Checksum(hex));
        return address(toBigInteger(hex));
    }

    /**
     * Converts ethereum {@link EthUint} to ethereum address type.
     */
    public static EthAddress address(EthUint value) {
        checkArgument(value.size() == 160, "Only uint160 can be converted to address");
        return address(value.value());
    }

}
