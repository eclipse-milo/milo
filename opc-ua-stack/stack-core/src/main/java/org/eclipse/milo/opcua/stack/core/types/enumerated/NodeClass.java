package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.serialization.SerializationContext;
import org.eclipse.milo.opcua.stack.core.serialization.UaDecoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaEncoder;
import org.eclipse.milo.opcua.stack.core.serialization.UaEnumeration;
import org.eclipse.milo.opcua.stack.core.serialization.codecs.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jetbrains.annotations.Nullable;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.2</a>
 */
public enum NodeClass implements UaEnumeration {
    /**
     * No value is specified.
     */
    Unspecified(0),

    /**
     * The Node is an Object.
     */
    Object(1),

    /**
     * The Node is a Variable.
     */
    Variable(2),

    /**
     * The Node is a Method.
     */
    Method(4),

    /**
     * The Node is an ObjectType.
     */
    ObjectType(8),

    /**
     * The Node is a VariableType.
     */
    VariableType(16),

    /**
     * The Node is a ReferenceType.
     */
    ReferenceType(32),

    /**
     * The Node is a DataType.
     */
    DataType(64),

    /**
     * The Node is a View.
     */
    View(128);

    private final int value;

    NodeClass(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public ExpandedNodeId getTypeId() {
        return TypeInfo.TYPE_ID;
    }

    public static @Nullable NodeClass from(int value) {
        switch (value) {
            case 0:
                return Unspecified;
            case 1:
                return Object;
            case 2:
                return Variable;
            case 4:
                return Method;
            case 8:
                return ObjectType;
            case 16:
                return VariableType;
            case 32:
                return ReferenceType;
            case 64:
                return DataType;
            case 128:
                return View;
            default:
                return null;
        }
    }

    public static EnumDefinition definition() {
        return new EnumDefinition(new EnumField[]{
            new EnumField(0L, LocalizedText.NULL_VALUE, new LocalizedText("", "No value is specified."), "Unspecified"),
            new EnumField(1L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is an Object."), "Object"),
            new EnumField(2L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is a Variable."), "Variable"),
            new EnumField(4L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is a Method."), "Method"),
            new EnumField(8L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is an ObjectType."), "ObjectType"),
            new EnumField(16L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is a VariableType."), "VariableType"),
            new EnumField(32L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is a ReferenceType."), "ReferenceType"),
            new EnumField(64L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is a DataType."), "DataType"),
            new EnumField(128L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is a View."), "View")
        });
    }

    public static final class Codec extends GenericDataTypeCodec<NodeClass> {
        @Override
        public Class<NodeClass> getType() {
            return NodeClass.class;
        }

        @Override
        public NodeClass decodeType(SerializationContext context, UaDecoder decoder) {
            return decoder.readEnum(null, NodeClass.class);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder, NodeClass value) {
            encoder.writeEnum(null, value);
        }
    }

    public static final class TypeInfo {
        public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=257");
    }
}
