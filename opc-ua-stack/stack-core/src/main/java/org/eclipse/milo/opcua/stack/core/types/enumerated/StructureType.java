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
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.3</a>
 */
public enum StructureType implements UaEnumeration {
    Structure(0),

    StructureWithOptionalFields(1),

    Union(2),

    StructureWithSubtypedValues(3),

    UnionWithSubtypedValues(4);

    private final int value;

    StructureType(int value) {
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

    public static @Nullable StructureType from(int value) {
        switch (value) {
            case 0:
                return Structure;
            case 1:
                return StructureWithOptionalFields;
            case 2:
                return Union;
            case 3:
                return StructureWithSubtypedValues;
            case 4:
                return UnionWithSubtypedValues;
            default:
                return null;
        }
    }

    public static EnumDefinition definition() {
        return new EnumDefinition(new EnumField[]{
            new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Structure"),
            new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "StructureWithOptionalFields"),
            new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Union"),
            new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "StructureWithSubtypedValues"),
            new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "UnionWithSubtypedValues")
        });
    }

    public static final class Codec extends GenericDataTypeCodec<StructureType> {
        @Override
        public Class<StructureType> getType() {
            return StructureType.class;
        }

        @Override
        public StructureType decodeType(SerializationContext context, UaDecoder decoder) {
            return decoder.readEnum(null, StructureType.class);
        }

        @Override
        public void encodeType(SerializationContext context, UaEncoder encoder, StructureType value) {
            encoder.writeEnum(null, value);
        }
    }

    public static final class TypeInfo {
        public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=98");
    }
}
