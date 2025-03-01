package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.7">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.7</a>
 */
public enum HistoryUpdateType implements UaEnumeratedType {
  /** Data was inserted. */
  Insert(1),

  /** Data was replaced. */
  Replace(2),

  /** Data was inserted or replaced. */
  Update(3),

  /** Data was deleted. */
  Delete(4);

  private final int value;

  HistoryUpdateType(int value) {
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

  public static @Nullable HistoryUpdateType from(int value) {
    switch (value) {
      case 1:
        return Insert;
      case 2:
        return Replace;
      case 3:
        return Update;
      case 4:
        return Delete;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              1L, LocalizedText.NULL_VALUE, new LocalizedText("", "Data was inserted."), "Insert"),
          new EnumField(
              2L, LocalizedText.NULL_VALUE, new LocalizedText("", "Data was replaced."), "Replace"),
          new EnumField(
              3L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "Data was inserted or replaced."),
              "Update"),
          new EnumField(
              4L, LocalizedText.NULL_VALUE, new LocalizedText("", "Data was deleted."), "Delete")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=11234");
  }
}
