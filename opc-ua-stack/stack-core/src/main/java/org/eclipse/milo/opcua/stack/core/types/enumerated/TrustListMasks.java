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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.2/#7.8.2.7</a>
 */
public enum TrustListMasks implements UaEnumeratedType {
  /** No fields are provided. */
  None(0),

  /** The TrustedCertificates are provided. */
  TrustedCertificates(1),

  /** The TrustedCrls are provided. */
  TrustedCrls(2),

  /** The IssuerCertificates are provided. */
  IssuerCertificates(4),

  /** The IssuerCrls are provided. */
  IssuerCrls(8),

  /** All fields are provided. */
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
    return switch (value) {
      case 0 -> None;
      case 1 -> TrustedCertificates;
      case 2 -> TrustedCrls;
      case 4 -> IssuerCertificates;
      case 8 -> IssuerCrls;
      case 15 -> All;
      default -> null;
    };
  }

  public static EnumDefinition definition() {
    return new EnumDefinition(
        new EnumField[] {
          new EnumField(
              0L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "No fields are provided."),
              "None"),
          new EnumField(
              1L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The TrustedCertificates are provided."),
              "TrustedCertificates"),
          new EnumField(
              2L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The TrustedCrls are provided."),
              "TrustedCrls"),
          new EnumField(
              4L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The IssuerCertificates are provided."),
              "IssuerCertificates"),
          new EnumField(
              8L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "The IssuerCrls are provided."),
              "IssuerCrls"),
          new EnumField(
              15L,
              LocalizedText.NULL_VALUE,
              new LocalizedText("", "All fields are provided."),
              "All")
        });
  }

  public static final class TypeInfo {
    public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=12552");
  }
}
