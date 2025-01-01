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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part3/8.60">https://reference.opcfoundation.org/v105/Core/docs/Part3/8.60</a>
 */
public class AttributeWriteMask extends OptionSetUI32<AttributeWriteMask.Field> {
  public AttributeWriteMask(UInteger value) {
    super(value);
  }

  public boolean getAccessLevel() {
    return get(Field.AccessLevel);
  }

  public boolean getArrayDimensions() {
    return get(Field.ArrayDimensions);
  }

  public boolean getBrowseName() {
    return get(Field.BrowseName);
  }

  public boolean getContainsNoLoops() {
    return get(Field.ContainsNoLoops);
  }

  public boolean getDataType() {
    return get(Field.DataType);
  }

  public boolean getDescription() {
    return get(Field.Description);
  }

  public boolean getDisplayName() {
    return get(Field.DisplayName);
  }

  public boolean getEventNotifier() {
    return get(Field.EventNotifier);
  }

  public boolean getExecutable() {
    return get(Field.Executable);
  }

  public boolean getHistorizing() {
    return get(Field.Historizing);
  }

  public boolean getInverseName() {
    return get(Field.InverseName);
  }

  public boolean getIsAbstract() {
    return get(Field.IsAbstract);
  }

  public boolean getMinimumSamplingInterval() {
    return get(Field.MinimumSamplingInterval);
  }

  public boolean getNodeClass() {
    return get(Field.NodeClass);
  }

  public boolean getNodeId() {
    return get(Field.NodeId);
  }

  public boolean getSymmetric() {
    return get(Field.Symmetric);
  }

  public boolean getUserAccessLevel() {
    return get(Field.UserAccessLevel);
  }

  public boolean getUserExecutable() {
    return get(Field.UserExecutable);
  }

  public boolean getUserWriteMask() {
    return get(Field.UserWriteMask);
  }

  public boolean getValueRank() {
    return get(Field.ValueRank);
  }

  public boolean getWriteMask() {
    return get(Field.WriteMask);
  }

  public boolean getValueForVariableType() {
    return get(Field.ValueForVariableType);
  }

  public boolean getDataTypeDefinition() {
    return get(Field.DataTypeDefinition);
  }

  public boolean getRolePermissions() {
    return get(Field.RolePermissions);
  }

  public boolean getAccessRestrictions() {
    return get(Field.AccessRestrictions);
  }

  public boolean getAccessLevelEx() {
    return get(Field.AccessLevelEx);
  }

  @Override
  public UInteger getValue() {
    return (UInteger) value;
  }

  @Override
  public Set<AttributeWriteMask.Field> toSet() {
    return Arrays.stream(Field.values()).filter(this::get).collect(Collectors.toSet());
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AttributeWriteMask.class.getSimpleName() + "[", "]");
    joiner.add("accessLevel=" + getAccessLevel());
    joiner.add("arrayDimensions=" + getArrayDimensions());
    joiner.add("browseName=" + getBrowseName());
    joiner.add("containsNoLoops=" + getContainsNoLoops());
    joiner.add("dataType=" + getDataType());
    joiner.add("description=" + getDescription());
    joiner.add("displayName=" + getDisplayName());
    joiner.add("eventNotifier=" + getEventNotifier());
    joiner.add("executable=" + getExecutable());
    joiner.add("historizing=" + getHistorizing());
    joiner.add("inverseName=" + getInverseName());
    joiner.add("isAbstract=" + getIsAbstract());
    joiner.add("minimumSamplingInterval=" + getMinimumSamplingInterval());
    joiner.add("nodeClass=" + getNodeClass());
    joiner.add("nodeId=" + getNodeId());
    joiner.add("symmetric=" + getSymmetric());
    joiner.add("userAccessLevel=" + getUserAccessLevel());
    joiner.add("userExecutable=" + getUserExecutable());
    joiner.add("userWriteMask=" + getUserWriteMask());
    joiner.add("valueRank=" + getValueRank());
    joiner.add("writeMask=" + getWriteMask());
    joiner.add("valueForVariableType=" + getValueForVariableType());
    joiner.add("dataTypeDefinition=" + getDataTypeDefinition());
    joiner.add("rolePermissions=" + getRolePermissions());
    joiner.add("accessRestrictions=" + getAccessRestrictions());
    joiner.add("accessLevelEx=" + getAccessLevelEx());
    return joiner.toString();
  }

  public static AttributeWriteMask of(AttributeWriteMask.Field... fields) {
    long bits = 0L;

    for (Field f : fields) {
      bits |= (1L << f.bitIndex);
    }

    return new AttributeWriteMask(UInteger.valueOf(bits));
  }

  public enum Field implements OptionSetUInteger.BitIndex {
    AccessLevel(0),

    ArrayDimensions(1),

    BrowseName(2),

    ContainsNoLoops(3),

    DataType(4),

    Description(5),

    DisplayName(6),

    EventNotifier(7),

    Executable(8),

    Historizing(9),

    InverseName(10),

    IsAbstract(11),

    MinimumSamplingInterval(12),

    NodeClass(13),

    NodeId(14),

    Symmetric(15),

    UserAccessLevel(16),

    UserExecutable(17),

    UserWriteMask(18),

    ValueRank(19),

    WriteMask(20),

    ValueForVariableType(21),

    DataTypeDefinition(22),

    RolePermissions(23),

    AccessRestrictions(24),

    AccessLevelEx(25);

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
