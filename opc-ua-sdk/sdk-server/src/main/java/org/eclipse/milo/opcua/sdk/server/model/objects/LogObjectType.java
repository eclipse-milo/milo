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
import org.eclipse.milo.opcua.sdk.core.model.methods.LogObjectTypeGetRecordsOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface LogObjectType extends BaseObjectType {
  QualifiedProperty<UInteger> MAX_RECORDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxRecords",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Double> MAX_STORAGE_DURATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxStorageDuration",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UShort> MINIMUM_SEVERITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MinimumSeverity",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxRecords();

  /** Sets the existing node's local value. */
  void setMaxRecords(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxRecordsNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxStorageDuration();

  /** Sets the existing node's local value. */
  void setMaxStorageDuration(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxStorageDurationNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getMinimumSeverity();

  /** Sets the existing node's local value. */
  void setMinimumSeverity(@Nullable UShort value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMinimumSeverityNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getGetRecordsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetRecords(MethodBindings bindings, GetRecordsHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetRecordsDetailed(MethodBindings bindings, GetRecordsDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getReleaseContinuationPointMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReleaseContinuationPoint(
      MethodBindings bindings, ReleaseContinuationPointHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReleaseContinuationPointDetailed(
      MethodBindings bindings, ReleaseContinuationPointDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3 */
  @FunctionalInterface
  interface GetRecordsHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    LogObjectTypeGetRecordsOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable DateTime startTime,
        @Nullable DateTime endTime,
        @Nullable UInteger maxReturnRecords,
        @Nullable UShort minimumSeverity,
        @Nullable LogRecordMask requestMask,
        @Nullable ByteString continuationPointIn)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3 */
  @FunctionalInterface
  interface GetRecordsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<LogObjectTypeGetRecordsOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable DateTime startTime,
        @Nullable DateTime endTime,
        @Nullable UInteger maxReturnRecords,
        @Nullable UShort minimumSeverity,
        @Nullable LogRecordMask requestMask,
        @Nullable ByteString continuationPointIn)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4 */
  @FunctionalInterface
  interface ReleaseContinuationPointHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString continuationPointIn)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part26/5.4 */
  @FunctionalInterface
  interface ReleaseContinuationPointDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString continuationPointIn)
        throws UaException;
  }
}
