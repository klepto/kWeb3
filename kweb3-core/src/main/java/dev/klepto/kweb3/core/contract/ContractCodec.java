package dev.klepto.kweb3.core.contract;


import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.abi.descriptor.EthArrayTypeDescriptor;
import dev.klepto.kweb3.core.abi.descriptor.EthSizedTypeDescriptor;
import dev.klepto.kweb3.core.abi.descriptor.EthTupleTypeDescriptor;
import dev.klepto.kweb3.core.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.core.contract.annotation.ArraySize;
import dev.klepto.kweb3.core.contract.annotation.ValueSize;
import dev.klepto.kweb3.core.type.*;
import dev.klepto.unreflect.MethodAccess;
import dev.klepto.unreflect.ParameterAccess;
import dev.klepto.unreflect.UnreflectType;
import dev.klepto.unreflect.property.Reflectable;
import lombok.val;

import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.klepto.kweb3.core.util.Conditions.require;
import static dev.klepto.unreflect.Unreflect.reflect;

/**
 * Handles encoding/decoding of {@link EthType} JVM types for use with {@link ContractExecutor}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractCodec {

    /**
     * Parses a tuple type descriptor for a given list of {@link Reflectable} types. List can contain method parameters,
     * class fields or methods themselves in-order to infer their ABI signature based on their JVM types.
     *
     * @param reflectables the list of reflectables (methods, fields, parameters)
     * @return the ABI-compatible type descriptor
     */
    public static TypeDescriptor parseTupleDescriptor(List<? extends Reflectable> reflectables) {
        val values = reflectables.stream()
                .map(ContractCodec::parseDescriptor)
                .collect(toImmutableList());

        return new EthTupleTypeDescriptor(values);
    }

    /**
     * Parses a type descriptor for a given {@link Reflectable}, usually {@link MethodAccess} for contract return type
     * description, or {@link ParameterAccess} for contract input type description.
     *
     * @param reflectable the reflectable for ABI type inference
     * @return the ABI-compatible type descriptor
     */
    public static TypeDescriptor parseDescriptor(Reflectable reflectable) {
        val arraySizeAnnotation = reflectable.annotation(ArraySize.class);
        val valueSizeAnnotation = reflectable.annotation(ValueSize.class);
        val arraySize = arraySizeAnnotation != null ? arraySizeAnnotation.value() : -1;
        val valueSize = valueSizeAnnotation != null ? valueSizeAnnotation.value() : -1;
        return parseDescriptor(reflectable.type(), valueSize, arraySize);
    }

    /**
     * Parses a type descriptor of a given {@link EthType} represented by {@link UnreflectType} with a given value size
     * and array size. Used for annotation processing.
     *
     * @param type      the ethereum data type
     * @param valueSize the value size, or -1 if size is not specified
     * @param arraySize the array size , or -1 if size is not specified
     * @return the ABI-compatible type descriptor
     */
    public static TypeDescriptor parseDescriptor(UnreflectType type, int valueSize, int arraySize) {
        if (type.matchesExact(Web3Result.class)) {
            val genericType = type.genericType();
            require(genericType != null, "Contract functions with Web3Result return type must have a generic type.");
            return parseDescriptor(genericType, valueSize, arraySize);
        }

        if (type.matchesExact(EthArray.class)) {
            return parseArrayDescriptor(type, arraySize, valueSize);
        } else if (!type.matches(EthType.class)) {
            return parseTupleDescriptor(type);
        }

        // Hard-coded defaults, not a huge fan.
        if (valueSize == -1) {
            if (type.matches(EthUint.class) || type.matches(EthInt.class)) {
                valueSize = 256;
            } else if (type.matches(EthBytes.class)) {
                valueSize = 32;
            }
        }

        return new EthSizedTypeDescriptor(type, valueSize);
    }

    /**
     * Parses a type descriptor for {@link EthArray} by inferring its generic type.
     *
     * @param type      the type representing ethereum array
     * @param valueSize the value size, or -1 if size is not specified
     * @param arraySize the array size , or -1 if size is not specified
     * @return the ABI-compatible array type descriptor
     */
    private static TypeDescriptor parseArrayDescriptor(UnreflectType type, int valueSize, int arraySize) {
        require(type.matchesExact(EthArray.class), "Not EthArray type.");

        val componentType = type.genericType();
        require(componentType != null, "EthArray must contain generic type.");

        val typeDescriptor = parseDescriptor(componentType, valueSize, arraySize);
        return new EthArrayTypeDescriptor(typeDescriptor, arraySize);
    }

    /**
     * Parses a tuple type descriptor for any JVM type based on its fields. Mainly used in encoding/decoding of
     * structs.
     *
     * @param type the JVM type containing ethereum data fields
     * @return the ABI-compatible array type descriptor
     */
    private static TypeDescriptor parseTupleDescriptor(UnreflectType type) {
        return parseTupleDescriptor(type.reflect().fields().toList());
    }

    /**
     * Decodes tuple values into specified JVM container {@link UnreflectType}. Used for struct and multiple value
     * return decoding.
     *
     * @param type  the JVM container
     * @param tuple the tuple values
     * @return the decoded JVM container containing tuple values
     */
    public static Object decodeTupleContainer(UnreflectType type, EthTuple tuple) {
        val container = type.allocate();
        val fields = reflect(container).fields().toList();
        require(fields.size() == tuple.size(), "Tuple container size mismatch: {}", type);

        for (var i = 0; i < tuple.size(); i++) {
            val field = fields.get(i);
            val value = tuple.get(i);
            val decodedValue = value instanceof EthTuple valueTuple
                    ? decodeTupleContainer(field.type(), valueTuple)
                    : value;
            field.set(decodedValue);
        }

        return container;
    }

}