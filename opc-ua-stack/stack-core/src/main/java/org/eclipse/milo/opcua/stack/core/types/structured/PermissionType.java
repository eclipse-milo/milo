package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUI32;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part3/8.55">https://reference.opcfoundation.org/v105/Core/docs/Part3/8.55</a>
 */
public class PermissionType extends OptionSetUI32<PermissionType.Field> {
  public PermissionType(UInteger value) {
    super(value);
  }

  public boolean getBrowse() {
    return get(Field.Browse);
  }

  public boolean getReadRolePermissions() {
    return get(Field.ReadRolePermissions);
  }

  public boolean getWriteAttribute() {
    return get(Field.WriteAttribute);
  }

  public boolean getWriteRolePermissions() {
    return get(Field.WriteRolePermissions);
  }

  public boolean getWriteHistorizing() {
    return get(Field.WriteHistorizing);
  }

  public boolean getRead() {
    return get(Field.Read);
  }

  public boolean getWrite() {
    return get(Field.Write);
  }

  public boolean getReadHistory() {
    return get(Field.ReadHistory);
  }

  public boolean getInsertHistory() {
    return get(Field.InsertHistory);
  }

  public boolean getModifyHistory() {
    return get(Field.ModifyHistory);
  }

  public boolean getDeleteHistory() {
    return get(Field.DeleteHistory);
  }

  public boolean getReceiveEvents() {
    return get(Field.ReceiveEvents);
  }

  public boolean getCall() {
    return get(Field.Call);
  }

  public boolean getAddReference() {
    return get(Field.AddReference);
  }

  public boolean getRemoveReference() {
    return get(Field.RemoveReference);
  }

  public boolean getDeleteNode() {
    return get(Field.DeleteNode);
  }

  public boolean getAddNode() {
    return get(Field.AddNode);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<PermissionType.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PermissionType.class.getSimpleName() + "[", "]");
    joiner.add("browse=" + getBrowse());
    joiner.add("readRolePermissions=" + getReadRolePermissions());
    joiner.add("writeAttribute=" + getWriteAttribute());
    joiner.add("writeRolePermissions=" + getWriteRolePermissions());
    joiner.add("writeHistorizing=" + getWriteHistorizing());
    joiner.add("read=" + getRead());
    joiner.add("write=" + getWrite());
    joiner.add("readHistory=" + getReadHistory());
    joiner.add("insertHistory=" + getInsertHistory());
    joiner.add("modifyHistory=" + getModifyHistory());
    joiner.add("deleteHistory=" + getDeleteHistory());
    joiner.add("receiveEvents=" + getReceiveEvents());
    joiner.add("call=" + getCall());
    joiner.add("addReference=" + getAddReference());
    joiner.add("removeReference=" + getRemoveReference());
    joiner.add("deleteNode=" + getDeleteNode());
    joiner.add("addNode=" + getAddNode());
    return joiner.toString();
  }

  public static PermissionType of(PermissionType.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new PermissionType(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    Browse(0),

    ReadRolePermissions(1),

    WriteAttribute(2),

    WriteRolePermissions(3),

    WriteHistorizing(4),

    Read(5),

    Write(6),

    ReadHistory(7),

    InsertHistory(8),

    ModifyHistory(9),

    DeleteHistory(10),

    ReceiveEvents(11),

    Call(12),

    AddReference(13),

    RemoveReference(14),

    DeleteNode(15),

    AddNode(16);

    private final int bitIndex;

    Field(int bitIndex) {
      this.bitIndex = bitIndex;
    }

    @Override
    public int getBitIndex() {
      return bitIndex;
    }
  }
}
