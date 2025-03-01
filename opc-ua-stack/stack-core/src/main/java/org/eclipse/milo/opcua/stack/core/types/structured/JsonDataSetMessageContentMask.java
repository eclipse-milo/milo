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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.3.1</a>
 */
public class JsonDataSetMessageContentMask
    extends OptionSetUI32<JsonDataSetMessageContentMask.Field> {
  public JsonDataSetMessageContentMask(UInteger value) {
    super(value);
  }

  public boolean getDataSetWriterId() {
    return get(Field.DataSetWriterId);
  }

  public boolean getMetaDataVersion() {
    return get(Field.MetaDataVersion);
  }

  public boolean getSequenceNumber() {
    return get(Field.SequenceNumber);
  }

  public boolean getTimestamp() {
    return get(Field.Timestamp);
  }

  public boolean getStatus() {
    return get(Field.Status);
  }

  public boolean getMessageType() {
    return get(Field.MessageType);
  }

  public boolean getDataSetWriterName() {
    return get(Field.DataSetWriterName);
  }

  public boolean getFieldEncoding1() {
    return get(Field.FieldEncoding1);
  }

  public boolean getPublisherId() {
    return get(Field.PublisherId);
  }

  public boolean getWriterGroupName() {
    return get(Field.WriterGroupName);
  }

  public boolean getMinorVersion() {
    return get(Field.MinorVersion);
  }

  public boolean getFieldEncoding2() {
    return get(Field.FieldEncoding2);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<JsonDataSetMessageContentMask.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonDataSetMessageContentMask.class.getSimpleName() + "[", "]");
    joiner.add("dataSetWriterId=" + getDataSetWriterId());
    joiner.add("metaDataVersion=" + getMetaDataVersion());
    joiner.add("sequenceNumber=" + getSequenceNumber());
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("status=" + getStatus());
    joiner.add("messageType=" + getMessageType());
    joiner.add("dataSetWriterName=" + getDataSetWriterName());
    joiner.add("fieldEncoding1=" + getFieldEncoding1());
    joiner.add("publisherId=" + getPublisherId());
    joiner.add("writerGroupName=" + getWriterGroupName());
    joiner.add("minorVersion=" + getMinorVersion());
    joiner.add("fieldEncoding2=" + getFieldEncoding2());
    return joiner.toString();
  }

  public static JsonDataSetMessageContentMask of(JsonDataSetMessageContentMask.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new JsonDataSetMessageContentMask(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    DataSetWriterId(0),

    MetaDataVersion(1),

    SequenceNumber(2),

    Timestamp(3),

    Status(4),

    MessageType(5),

    DataSetWriterName(6),

    FieldEncoding1(7),

    PublisherId(8),

    WriterGroupName(9),

    MinorVersion(10),

    FieldEncoding2(11);

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
