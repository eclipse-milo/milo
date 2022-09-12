package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.serialization.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jetbrains.annotations.Nullable;

public enum OpenFileMode implements UaEnumeratedType {
    Read(1),

    Write(2),

    EraseExisting(4),

    Append(8);

    private final int value;

    OpenFileMode(int value) {
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

    public static @Nullable OpenFileMode from(int value) {
        switch (value) {
            case 1:
                return Read;
            case 2:
                return Write;
            case 4:
                return EraseExisting;
            case 8:
                return Append;
            default:
                return null;
        }
    }

    public static EnumDefinition definition() {
        return new EnumDefinition(new EnumField[]{
            new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Read"),
            new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Write"),
            new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "EraseExisting"),
            new EnumField(8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Append")
        });
    }

    public static final class TypeInfo {
        public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=11939");
    }
}
