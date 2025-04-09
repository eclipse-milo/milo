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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.2</a>
 */
public enum NodeClass implements UaEnumeratedType {
  /** No value is specified. */
  Unspecified(0),

  /** The Node is an Object. */
  Object(1),

  /** The Node is a Variable. */
  Variable(2),

  /** The Node is a Method. */
  Method(4),

  /** The Node is an ObjectType. */
  ObjectType(8),

  /** The Node is a VariableType. */
  VariableType(16),

  /** The Node is a ReferenceType. */
  ReferenceType(32),

  /** The Node is a DataType. */
  DataType(64),

  /** The Node is a View. */
  View(128);

  private final int value;

  NodeClass(int value) {
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

  public static @Nullable NodeClass from(int value) {
    return switch (value) {
      case 0 -> Unspecified;
      case 1 -> Object;
      case 2 -> Variable;
      case 4 -> Method;
      case 8 -> ObjectType;
      case 16 -> VariableType;
      case 32 -> ReferenceType;
      case 64 -> DataType;
      case 128 -> View;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              0L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "No value is specified."),
              "Unspecified"),
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The Node is an Object."),
              "Object"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The Node is a Variable."),
              "Variable"),
          new EnumField(
              4L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The Node is a Method."),
              "Method"),
          new EnumField(
              8L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The Node is an ObjectType."),
              "ObjectType"),
          new EnumField(
              16L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The Node is a VariableType."),
              "VariableType"),
          new EnumField(
              32L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The Node is a ReferenceType."),
              "ReferenceType"),
          new EnumField(
              64L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The Node is a DataType."),
              "DataType"),
          new EnumField(
              128L, LocalizedText.NULL_VALUE, new LocalizedText("", "The Node is a View."), "View")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=257");
  }
}
