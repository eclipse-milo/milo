/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.8">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.8</a>
 */
public class LogRecordMask extends OptionSetUI32<LogRecordMask.Field> {
  public LogRecordMask(UInteger value) {
    super(value);
  }

  public boolean getEventType() {
    return get(Field.EventType);
  }

  public boolean getSourceNode() {
    return get(Field.SourceNode);
  }

  public boolean getSourceName() {
    return get(Field.SourceName);
  }

  public boolean getTraceContext() {
    return get(Field.TraceContext);
  }

  public boolean getAdditionalData() {
    return get(Field.AdditionalData);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<LogRecordMask.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LogRecordMask.class.getSimpleName() + "[", "]");
    joiner.add("eventType=" + getEventType());
    joiner.add("sourceNode=" + getSourceNode());
    joiner.add("sourceName=" + getSourceName());
    joiner.add("traceContext=" + getTraceContext());
    joiner.add("additionalData=" + getAdditionalData());
    return joiner.toString();
  }

  public static LogRecordMask of(LogRecordMask.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new LogRecordMask(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    EventType(0),

    SourceNode(1),

    SourceName(2),

    TraceContext(3),

    AdditionalData(4);

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
