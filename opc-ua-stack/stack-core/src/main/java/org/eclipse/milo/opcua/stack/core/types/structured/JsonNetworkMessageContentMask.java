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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.1.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.1.1</a>
 */
public class JsonNetworkMessageContentMask
    extends OptionSetUI32<JsonNetworkMessageContentMask.Field> {
  public JsonNetworkMessageContentMask(UInteger value) {
    super(value);
  }

  public boolean getNetworkMessageHeader() {
    return get(Field.NetworkMessageHeader);
  }

  public boolean getDataSetMessageHeader() {
    return get(Field.DataSetMessageHeader);
  }

  public boolean getSingleDataSetMessage() {
    return get(Field.SingleDataSetMessage);
  }

  public boolean getPublisherId() {
    return get(Field.PublisherId);
  }

  public boolean getDataSetClassId() {
    return get(Field.DataSetClassId);
  }

  public boolean getReplyTo() {
    return get(Field.ReplyTo);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<JsonNetworkMessageContentMask.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonNetworkMessageContentMask.class.getSimpleName() + "[", "]");
    joiner.add("networkMessageHeader=" + getNetworkMessageHeader());
    joiner.add("dataSetMessageHeader=" + getDataSetMessageHeader());
    joiner.add("singleDataSetMessage=" + getSingleDataSetMessage());
    joiner.add("publisherId=" + getPublisherId());
    joiner.add("dataSetClassId=" + getDataSetClassId());
    joiner.add("replyTo=" + getReplyTo());
    return joiner.toString();
  }

  public static JsonNetworkMessageContentMask of(JsonNetworkMessageContentMask.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new JsonNetworkMessageContentMask(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    NetworkMessageHeader(0),

    DataSetMessageHeader(1),

    SingleDataSetMessage(2),

    PublisherId(3),

    DataSetClassId(4),

    ReplyTo(5);

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
