package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.1</a>
 */
public enum BrokerTransportQualityOfService implements UaEnumeratedType {
  NotSpecified(0),

  BestEffort(1),

  AtLeastOnce(2),

  AtMostOnce(3),

  ExactlyOnce(4);

  private final int value;

  BrokerTransportQualityOfService(int value) {
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

  public static @Nullable BrokerTransportQualityOfService from(int value) {
    switch (value) {
      case 0:
        return NotSpecified;
      case 1:
        return BestEffort;
      case 2:
        return AtLeastOnce;
      case 3:
        return AtMostOnce;
      case 4:
        return ExactlyOnce;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NotSpecified"),
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BestEffort"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AtLeastOnce"),
          new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AtMostOnce"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ExactlyOnce")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15008");
  }
}
