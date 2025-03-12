/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class QualifiedNameConversions {

  private QualifiedNameConversions() {}

  @Nullable
  static String qualifiedNameToString(@NonNull QualifiedName name) {
    return name.name();
  }

  @NonNull
  static LocalizedText qualifiedNameToLocalizedText(@NonNull QualifiedName name) {
    return new LocalizedText("", name.name());
  }

  @Nullable
  static Object convert(@NonNull Object o, OpcUaDataType targetType, boolean implicit) {
    if (o instanceof QualifiedName) {
      QualifiedName name = (QualifiedName) o;

      return implicit ? implicitConversion(name, targetType) : explicitConversion(name, targetType);
    } else {
      return null;
    }
  }

  @Nullable
  static Object explicitConversion(@NonNull QualifiedName name, OpcUaDataType targetType) {
    return implicitConversion(name, targetType);
  }

  @Nullable
  static Object implicitConversion(@NonNull QualifiedName name, OpcUaDataType targetType) {
    // @formatter:off
    switch (targetType) {
      case String:
        return qualifiedNameToString(name);
      case LocalizedText:
        return qualifiedNameToLocalizedText(name);
      default:
        return null;
    }
    // @formatter:on
  }
}
