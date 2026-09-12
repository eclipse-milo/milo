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
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.methods.PublishedEventsTypeModifyFieldSelectionDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PublishedEventsTypeModifyFieldSelectionHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.1</a>
 */
public interface PublishedEventsType extends PublishedDataSetType {
  QualifiedProperty<NodeId> PUB_SUB_EVENT_NOTIFIER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EventNotifier",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<SimpleAttributeOperand[]> SELECTED_FIELDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SelectedFields",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=601"),
          1,
          SimpleAttributeOperand[].class);

  QualifiedProperty<ContentFilter> FILTER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Filter",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=586"),
          -1,
          ContentFilter.class);

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
  @Nullable NodeId getPubSubEventNotifier();

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
  void setPubSubEventNotifier(@Nullable NodeId value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  PropertyType getPubSubEventNotifierNode();

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
  @Nullable SimpleAttributeOperand @Nullable [] getSelectedFields();

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
  void setSelectedFields(@Nullable SimpleAttributeOperand @Nullable [] value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  PropertyType getSelectedFieldsNode();

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
  @Nullable ContentFilter getFilter();

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
  void setFilter(@Nullable ContentFilter value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  PropertyType getFilterNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getModifyFieldSelectionMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindModifyFieldSelection(
      MethodBindings bindings, PublishedEventsTypeModifyFieldSelectionHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.4.2
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindModifyFieldSelectionDetailed(
      MethodBindings bindings, PublishedEventsTypeModifyFieldSelectionDetailedHandler handler)
      throws UaException;
}
