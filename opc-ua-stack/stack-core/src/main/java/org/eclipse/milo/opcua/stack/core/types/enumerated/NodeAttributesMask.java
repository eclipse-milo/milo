/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.enumerated;

import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.jspecify.annotations.Nullable;

public enum NodeAttributesMask implements UaEnumeratedType {
  None(0),

  AccessLevel(1),

  ArrayDimensions(2),

  BrowseName(4),

  ContainsNoLoops(8),

  DataType(16),

  Description(32),

  DisplayName(64),

  EventNotifier(128),

  Executable(256),

  Historizing(512),

  InverseName(1024),

  IsAbstract(2048),

  MinimumSamplingInterval(4096),

  NodeClass(8192),

  NodeId(16384),

  Symmetric(32768),

  UserAccessLevel(65536),

  UserExecutable(131072),

  UserWriteMask(262144),

  ValueRank(524288),

  WriteMask(1048576),

  Value(2097152),

  DataTypeDefinition(4194304),

  RolePermissions(8388608),

  AccessRestrictions(16777216),

  All(33554431),

  BaseNode(26501220),

  Object(26501348),

  ObjectType(26503268),

  Variable(26571383),

  VariableType(28600438),

  Method(26632548),

  ReferenceType(26537060),

  View(26501356);

  private final int value;

  NodeAttributesMask(int value) {
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

  public static @Nullable NodeAttributesMask from(int value) {
    return switch (value) {
      case 0 -> None;
      case 1 -> AccessLevel;
      case 2 -> ArrayDimensions;
      case 4 -> BrowseName;
      case 8 -> ContainsNoLoops;
      case 16 -> DataType;
      case 32 -> Description;
      case 64 -> DisplayName;
      case 128 -> EventNotifier;
      case 256 -> Executable;
      case 512 -> Historizing;
      case 1024 -> InverseName;
      case 2048 -> IsAbstract;
      case 4096 -> MinimumSamplingInterval;
      case 8192 -> NodeClass;
      case 16384 -> NodeId;
      case 32768 -> Symmetric;
      case 65536 -> UserAccessLevel;
      case 131072 -> UserExecutable;
      case 262144 -> UserWriteMask;
      case 524288 -> ValueRank;
      case 1048576 -> WriteMask;
      case 2097152 -> Value;
      case 4194304 -> DataTypeDefinition;
      case 8388608 -> RolePermissions;
      case 16777216 -> AccessRestrictions;
      case 33554431 -> All;
      case 26501220 -> BaseNode;
      case 26501348 -> Object;
      case 26503268 -> ObjectType;
      case 26571383 -> Variable;
      case 28600438 -> VariableType;
      case 26632548 -> Method;
      case 26537060 -> ReferenceType;
      case 26501356 -> View;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "None"),
          new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AccessLevel"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ArrayDimensions"),
          new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BrowseName"),
          new EnumField(8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ContainsNoLoops"),
          new EnumField(16L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DataType"),
          new EnumField(32L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Description"),
          new EnumField(64L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DisplayName"),
          new EnumField(128L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "EventNotifier"),
          new EnumField(256L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Executable"),
          new EnumField(512L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Historizing"),
          new EnumField(1024L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "InverseName"),
          new EnumField(2048L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IsAbstract"),
          new EnumField(
              4096L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "MinimumSamplingInterval"),
          new EnumField(8192L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NodeClass"),
          new EnumField(16384L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NodeId"),
          new EnumField(32768L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Symmetric"),
          new EnumField(
              65536L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "UserAccessLevel"),
          new EnumField(
              131072L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "UserExecutable"),
          new EnumField(
              262144L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "UserWriteMask"),
          new EnumField(524288L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ValueRank"),
          new EnumField(1048576L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "WriteMask"),
          new EnumField(2097152L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Value"),
          new EnumField(
              4194304L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DataTypeDefinition"),
          new EnumField(
              8388608L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "RolePermissions"),
          new EnumField(
              16777216L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AccessRestrictions"),
          new EnumField(33554431L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "All"),
          new EnumField(26501220L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BaseNode"),
          new EnumField(26501348L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Object"),
          new EnumField(
              26503268L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ObjectType"),
          new EnumField(26571383L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Variable"),
          new EnumField(
              28600438L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "VariableType"),
          new EnumField(26632548L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Method"),
          new EnumField(
              26537060L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ReferenceType"),
          new EnumField(26501356L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "View")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=348");
  }
}
