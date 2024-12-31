/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import java.util.EnumSet;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public enum WriteMask {
  AccessLevel(1),
  ArrayDimensions(1 << 1),
  BrowseName(1 << 2),
  ContainsNoLoops(1 << 3),
  DataType(1 << 4),
  Description(1 << 5),
  DisplayName(1 << 6),
  EventNotifier(1 << 7),
  Executable(1 << 8),
  Historizing(1 << 9),
  InverseName(1 << 10),
  IsAbstract(1 << 11),
  MinimumSamplingInterval(1 << 12),
  NodeClass(1 << 13),
  NodeId(1 << 14),
  Symmetric(1 << 15),
  UserAccessLevel(1 << 16),
  UserExecutable(1 << 17),
  UserWriteMask(1 << 18),
  ValueRank(1 << 19),
  WriteMask(1 << 20),
  ValueForVariableType(1 << 21),
  DataTypeDefinition(1 << 22),
  RolePermissions(1 << 23),
  AccessRestrictions(1 << 24),
  AccessLevelEx(1 << 25);

  public static final Set<WriteMask> NONE = Set.of();

  private final int value;

  WriteMask(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static EnumSet<WriteMask> fromMask(int accessLevel) {
    EnumSet<WriteMask> e = EnumSet.noneOf(WriteMask.class);
    for (WriteMask wm : values()) {
      if ((wm.value & accessLevel) != 0) {
        e.add(wm);
      }
    }
    return e;
  }

  public static EnumSet<WriteMask> fromMask(UInteger accessLevel) {
    return fromMask(accessLevel.intValue());
  }
}
