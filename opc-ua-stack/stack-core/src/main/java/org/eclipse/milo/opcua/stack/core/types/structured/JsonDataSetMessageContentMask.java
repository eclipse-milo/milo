/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

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

  public boolean getReversibleFieldEncoding() {
    return get(Field.ReversibleFieldEncoding);
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
    joiner.add("reversibleFieldEncoding=" + getReversibleFieldEncoding());
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

    ReversibleFieldEncoding(7);

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
