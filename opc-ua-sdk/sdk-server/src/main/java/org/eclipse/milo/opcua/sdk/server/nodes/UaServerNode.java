/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import java.util.List;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

public interface UaServerNode extends Node {

  /**
   * @return the {@link UaNodeContext} for this {@link UaServerNode}.
   */
  UaNodeContext getNodeContext();

  /**
   * Add a {@link Reference} to this node.
   *
   * @param reference the {@link Reference} to add.
   */
  void addReference(Reference reference);

  /**
   * Remove a {@link Reference} from this node.
   *
   * @param reference to remove.
   */
  void removeReference(Reference reference);

  /**
   * @return a {@link List} of this node's {@link Reference}s.
   */
  List<Reference> getReferences();

  /**
   * Get an attribute of this node, considering an {@link AccessContext}.
   *
   * @param context the {@link AccessContext} to consider.
   * @param attributeId the {@link AttributeId} to get.
   * @return the attribute value.
   */
  @Nullable Object getAttribute(AccessContext context, AttributeId attributeId);

  /**
   * Read an attribute of this node, considering an {@link AccessContext}.
   *
   * <p>This is similar to {@link #getAttribute(AccessContext, AttributeId)} except the underlying
   * implementation is allowed to throw {@link UaException} to indicate failure of some kind.
   *
   * @param context the {@link AccessContext} to consider.
   * @param attributeId the {@link AttributeId} to read.
   * @return the attribute value.
   * @throws UaException if the attribute cannot be read.
   */
  @Nullable Object readAttribute(AccessContext context, AttributeId attributeId) throws UaException;

  /**
   * Set an attribute of this node, considering an {@link AccessContext}.
   *
   * @param context the {@link AccessContext} to consider when setting the attribute.
   * @param attributeId the {@link AttributeId} to set.
   * @param value the new value to set for the attribute.
   */
  void setAttribute(AccessContext context, AttributeId attributeId, @Nullable Object value);

  /**
   * Write an attribute of this node, considering an {@link AccessContext}.
   *
   * <p>This is similar to {@link #setAttribute(AccessContext, AttributeId, Object)} except the
   * underlying implementation is allowed to throw {@link UaException} to indicate failure of some
   * kind.
   *
   * @param context the {@link AccessContext} to consider.
   * @param attributeId the {@link AttributeId} to write.
   * @param value the new value to set for the attribute.
   * @throws UaException if the attribute cannot be written.
   */
  void writeAttribute(AccessContext context, AttributeId attributeId, @Nullable Object value)
      throws UaException;
}
