/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

public enum TrustListMasks implements UaEnumeratedType {
  None(0),

  TrustedCertificates(1),

  TrustedCrls(2),

  IssuerCertificates(4),

  IssuerCrls(8),

  All(15);

  private final int value;

  TrustListMasks(int value) {
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

  public static @Nullable TrustListMasks from(int value) {
    switch (value) {
      case 0:
        return None;
      case 1:
        return TrustedCertificates;
      case 2:
        return TrustedCrls;
      case 4:
        return IssuerCertificates;
      case 8:
        return IssuerCrls;
      case 15:
        return All;
      default:
        return null;
    }
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "None"),
          new EnumField(
              1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "TrustedCertificates"),
          new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "TrustedCrls"),
          new EnumField(
              4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IssuerCertificates"),
          new EnumField(8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IssuerCrls"),
          new EnumField(15L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "All")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=12552");
  }
}
