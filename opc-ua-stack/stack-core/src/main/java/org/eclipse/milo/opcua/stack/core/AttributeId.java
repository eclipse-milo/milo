/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public enum AttributeId {
  NodeId(1),
  NodeClass(2),
  BrowseName(3),
  DisplayName(4),
  Description(5),
  WriteMask(6),
  UserWriteMask(7),
  IsAbstract(8),
  Symmetric(9),
  InverseName(10),
  ContainsNoLoops(11),
  EventNotifier(12),
  Value(13),
  DataType(14),
  ValueRank(15),
  ArrayDimensions(16),
  AccessLevel(17),
  UserAccessLevel(18),
  MinimumSamplingInterval(19),
  Historizing(20),
  Executable(21),
  UserExecutable(22),
  DataTypeDefinition(23),
  RolePermissions(24),
  UserRolePermissions(25),
  AccessRestrictions(26),
  AccessLevelEx(27);

  public static final Set<AttributeId> BASE_ATTRIBUTES;

  public static final Set<AttributeId> DATA_TYPE_ATTRIBUTES;

  public static final Set<AttributeId> METHOD_ATTRIBUTES;

  public static final Set<AttributeId> OBJECT_ATTRIBUTES;

  public static final Set<AttributeId> OBJECT_TYPE_ATTRIBUTES;

  public static final Set<AttributeId> REFERENCE_TYPE_ATTRIBUTES;

  public static final Set<AttributeId> VARIABLE_ATTRIBUTES;

  public static final Set<AttributeId> VARIABLE_TYPE_ATTRIBUTES;

  public static final Set<AttributeId> VIEW_ATTRIBUTES;

  static {
    var baseAttributes = new LinkedHashSet<AttributeId>();
    baseAttributes.add(NodeId);
    baseAttributes.add(NodeClass);
    baseAttributes.add(BrowseName);
    baseAttributes.add(DisplayName);
    baseAttributes.add(Description);
    baseAttributes.add(WriteMask);
    baseAttributes.add(UserWriteMask);
    baseAttributes.add(RolePermissions);
    baseAttributes.add(UserRolePermissions);
    baseAttributes.add(AccessRestrictions);
    BASE_ATTRIBUTES = Collections.unmodifiableSet(baseAttributes);

    var dataTypeAttributes = new LinkedHashSet<>(baseAttributes);
    dataTypeAttributes.add(IsAbstract);
    dataTypeAttributes.add(DataTypeDefinition);
    DATA_TYPE_ATTRIBUTES = Collections.unmodifiableSet(dataTypeAttributes);

    var methodAttributes = new LinkedHashSet<>(baseAttributes);
    methodAttributes.add(Executable);
    methodAttributes.add(UserExecutable);
    METHOD_ATTRIBUTES = Collections.unmodifiableSet(methodAttributes);

    var objectAttributes = new LinkedHashSet<>(baseAttributes);
    objectAttributes.add(EventNotifier);
    OBJECT_ATTRIBUTES = Collections.unmodifiableSet(objectAttributes);

    var objectTypeAttributes = new LinkedHashSet<>(baseAttributes);
    objectTypeAttributes.add(IsAbstract);
    OBJECT_TYPE_ATTRIBUTES = Collections.unmodifiableSet(objectTypeAttributes);

    var referenceTypeAttributes = new LinkedHashSet<>(baseAttributes);
    referenceTypeAttributes.add(IsAbstract);
    referenceTypeAttributes.add(Symmetric);
    referenceTypeAttributes.add(InverseName);
    REFERENCE_TYPE_ATTRIBUTES = Collections.unmodifiableSet(referenceTypeAttributes);

    var variableAttributes = new LinkedHashSet<>(baseAttributes);
    variableAttributes.add(Value);
    variableAttributes.add(DataType);
    variableAttributes.add(ValueRank);
    variableAttributes.add(ArrayDimensions);
    variableAttributes.add(AccessLevel);
    variableAttributes.add(UserAccessLevel);
    variableAttributes.add(MinimumSamplingInterval);
    variableAttributes.add(Historizing);
    variableAttributes.add(AccessLevelEx);
    VARIABLE_ATTRIBUTES = Collections.unmodifiableSet(variableAttributes);

    var variableTypeAttributes = new LinkedHashSet<>(baseAttributes);
    variableTypeAttributes.add(Value);
    variableTypeAttributes.add(DataType);
    variableTypeAttributes.add(ValueRank);
    variableTypeAttributes.add(ArrayDimensions);
    variableTypeAttributes.add(IsAbstract);
    VARIABLE_TYPE_ATTRIBUTES = Collections.unmodifiableSet(variableTypeAttributes);

    var viewAttributes = new LinkedHashSet<>(baseAttributes);
    viewAttributes.add(ContainsNoLoops);
    viewAttributes.add(EventNotifier);
    VIEW_ATTRIBUTES = Collections.unmodifiableSet(viewAttributes);
  }

  private final int id;

  AttributeId(int id) {
    this.id = id;
  }

  public final int id() {
    return id;
  }

  public final UInteger uid() {
    return uint(id);
  }

  public static Optional<AttributeId> from(UInteger attributeId) {
    return from(attributeId.intValue());
  }

  public static Optional<AttributeId> from(int attributeId) {
    if (attributeId > 0 && attributeId <= values().length) {
      return Optional.of(values()[attributeId - 1]);
    } else {
      return Optional.empty();
    }
  }

  public boolean isEqual(UInteger attributeId) {
    return attributeId != null && isEqual(attributeId.intValue());
  }

  public boolean isEqual(int attributeId) {
    return id == attributeId;
  }

  /**
   * @param attributeId the id to test for validity.
   * @return {@code true} if {@code attributeId} is valid.
   */
  public static boolean isValid(UInteger attributeId) {
    return from(attributeId).isPresent();
  }

  /**
   * @param attributeId the id to test for validity.
   * @return {@code true} if {@code attributeId} is valid.
   */
  public static boolean isValid(int attributeId) {
    return from(attributeId).isPresent();
  }

  /**
   * Get the set of valid attributes for a {@link
   * org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass}.
   *
   * @param nodeClass the {@link org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass}.
   * @return the set of valid attributes for {@code nodeClass}.
   */
  public static Set<AttributeId> getAttributes(
      org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass nodeClass) {

    // @formatter:off
    switch (nodeClass) {
      case Object:
        return OBJECT_ATTRIBUTES;
      case Variable:
        return VARIABLE_ATTRIBUTES;
      case Method:
        return METHOD_ATTRIBUTES;
      case ObjectType:
        return OBJECT_TYPE_ATTRIBUTES;
      case VariableType:
        return VARIABLE_TYPE_ATTRIBUTES;
      case ReferenceType:
        return REFERENCE_TYPE_ATTRIBUTES;
      case DataType:
        return DATA_TYPE_ATTRIBUTES;
      case View:
        return VIEW_ATTRIBUTES;
      default:
        return Set.of();
    }
    // @formatter:on
  }
}
