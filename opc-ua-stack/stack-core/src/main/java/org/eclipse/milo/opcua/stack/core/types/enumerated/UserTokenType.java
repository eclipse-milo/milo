package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.serialization.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jetbrains.annotations.Nullable;

/**
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.43">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.43</a>
 */
public enum UserTokenType implements UaEnumeratedType {
    Anonymous(0),

    UserName(1),

    Certificate(2),

    IssuedToken(3);

    private final int value;

    UserTokenType(int value) {
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

    public static @Nullable UserTokenType from(int value) {
        switch (value) {
            case 0:
                return Anonymous;
            case 1:
                return UserName;
            case 2:
                return Certificate;
            case 3:
                return IssuedToken;
            default:
                return null;
        }
    }

    public static EnumDefinition definition() {
        return new EnumDefinition(new EnumField[]{
            new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Anonymous"),
            new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "UserName"),
            new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Certificate"),
            new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IssuedToken")
        });
    }

    public static final class TypeInfo {
        public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=303");
    }
}
