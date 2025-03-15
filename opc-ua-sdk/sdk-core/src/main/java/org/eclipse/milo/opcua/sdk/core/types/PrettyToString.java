/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import java.util.LinkedHashMap;

class PrettyToString {

  private PrettyToString() {}

  /**
   * Pretty print a {@link DynamicStructType} instance.
   *
   * @param struct the {@link DynamicStructType} to pretty print.
   * @return a pretty printed string representation of the {@link DynamicStructType}.
   */
  static String toStringPretty(DynamicStructType struct) {
    return toStringPretty(struct, 0);
  }

  private static String toStringPretty(DynamicStructType struct, int indentLevel) {
    String indent = "  ".repeat(indentLevel);
    String childIndent = "  ".repeat(indentLevel + 1);
    StringBuilder sb = new StringBuilder();

    sb.append(DynamicStructType.class.getSimpleName()).append("[\n");
    sb.append(childIndent)
        .append("browseName=")
        .append(struct.getDataType().getBrowseName().toParseableString())
        .append(",\n");
    sb.append(childIndent)
        .append("dataTypeId=")
        .append(struct.getDataType().getNodeId().toParseableString())
        .append(",\n");
    sb.append(childIndent).append("members={\n");

    joinMembersPretty(struct.getMembers(), sb, indentLevel + 2);

    sb.append(childIndent).append("}\n");
    sb.append(indent).append("]");

    return sb.toString();
  }

  private static void joinMembersPretty(
      LinkedHashMap<String, Object> members, StringBuilder sb, int indentLevel) {

    String indent = "  ".repeat(indentLevel);
    boolean first = true;

    for (var entry : members.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (!first) {
        sb.append(",\n");
      }
      first = false;

      sb.append(indent).append(key).append("=");

      if (value instanceof DynamicStructType struct) {
        sb.append(toStringPretty(struct, indentLevel));
      } else if (value instanceof DynamicEnumType
          || value instanceof DynamicUnionType
          || value instanceof DynamicOptionSetType) {
        sb.append(value);
      } else if (value instanceof Object[]) {
        formatArray((Object[]) value, sb, indentLevel);
      } else {
        sb.append(value);
      }
    }

    if (!members.isEmpty()) {
      sb.append("\n");
    }
  }

  private static void formatArray(Object[] array, StringBuilder sb, int indentLevel) {
    String indent = "  ".repeat(indentLevel + 1);

    sb.append("[\n");

    for (int i = 0; i < array.length; i++) {
      Object value = array[i];

      sb.append(indent);

      if (value instanceof DynamicStructType struct) {
        sb.append(toStringPretty(struct, indentLevel + 1));
      } else {
        sb.append(value);
      }

      if (i < array.length - 1) {
        sb.append(",");
      }

      sb.append("\n");
    }

    sb.append("  ".repeat(indentLevel)).append("]");
  }
}
