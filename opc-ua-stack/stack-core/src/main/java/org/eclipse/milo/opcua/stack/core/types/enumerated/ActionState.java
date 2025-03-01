package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.11/#6.2.11.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.11/#6.2.11.2.1</a>
 */
public enum ActionState implements UaEnumeratedType {
  Idle(0),

  Executing(1),

  Done(2);

  private final int value;

  ActionState(int value) {
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

  public static @Nullable ActionState from(int value) {
    switch (value) {
      case 0:
        return Idle;
      case 1:
        return Executing;
      case 2:
        return Done;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Idle"),
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Executing"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Done")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=18595");
  }
}
