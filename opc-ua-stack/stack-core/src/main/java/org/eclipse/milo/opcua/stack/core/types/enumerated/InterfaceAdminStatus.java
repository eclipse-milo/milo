package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.1/#5.3.1.2</a>
 */
public enum InterfaceAdminStatus implements UaEnumeratedType {
  /** Ready to pass packets. */
  Up(0),

  /** Not ready to pass packets and not in some test mode. */
  Down(1),

  /** In some test mode. */
  Testing(2);

  private final int value;

  InterfaceAdminStatus(int value) {
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

  public static @Nullable InterfaceAdminStatus from(int value) {
    switch (value) {
      case 0:
        return Up;
      case 1:
        return Down;
      case 2:
        return Testing;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              0L, LocalizedText.NULL_VALUE, new LocalizedText("", "Ready to pass packets."), "Up"),
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Not ready to pass packets and not in some test mode."),
              "Down"),
          new EnumField(
              2L, LocalizedText.NULL_VALUE, new LocalizedText("", "In some test mode."), "Testing")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=24212");
  }
}
