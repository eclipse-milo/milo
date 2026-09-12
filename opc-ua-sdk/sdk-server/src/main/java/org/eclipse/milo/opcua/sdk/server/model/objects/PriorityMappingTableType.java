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

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.PriorityMappingEntryType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PriorityMappingTableType extends BaseObjectType {
  QualifiedProperty<PriorityMappingEntryType[]> PRIORITY_MAPPPING_ENTRIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PriorityMapppingEntries",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=25220"),
          1,
          PriorityMappingEntryType[].class);

  /** Gets the existing node's local value. */
  @Nullable PriorityMappingEntryType @Nullable [] getPriorityMapppingEntries();

  /** Sets the existing node's local value. */
  void setPriorityMapppingEntries(@Nullable PriorityMappingEntryType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPriorityMapppingEntriesNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddPriorityMappingEntryMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPriorityMappingEntry(
      MethodBindings bindings, AddPriorityMappingEntryHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPriorityMappingEntryDetailed(
      MethodBindings bindings, AddPriorityMappingEntryDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getDeletePriorityMappingEntryMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeletePriorityMappingEntry(
      MethodBindings bindings, DeletePriorityMappingEntryHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDeletePriorityMappingEntryDetailed(
      MethodBindings bindings, DeletePriorityMappingEntryDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3 */
  @FunctionalInterface
  interface AddPriorityMappingEntryHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel,
        @Nullable UByte priorityValuePcp,
        @Nullable UInteger priorityValueDscp)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.3 */
  @FunctionalInterface
  interface AddPriorityMappingEntryDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel,
        @Nullable UByte priorityValuePcp,
        @Nullable UInteger priorityValueDscp)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4 */
  @FunctionalInterface
  interface DeletePriorityMappingEntryHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.2/#5.5.2.4 */
  @FunctionalInterface
  interface DeletePriorityMappingEntryDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String mappingUri,
        @Nullable String priorityLabel)
        throws UaException;
  }
}
