/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.10">https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.10</a>
 */
public interface KeyCredentialDeletedAuditEventType extends KeyCredentialAuditEventType {
  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable String getResourceUri();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setResourceUri(@Nullable String value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  PropertyType getResourceUriNode();
}
