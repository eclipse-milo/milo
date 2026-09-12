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
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeCloseAndUpdateOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ConfigurationFileType extends FileType {
  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<UInteger> CURRENT_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<Double> ACTIVITY_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActivityTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<NodeId> SUPPORTED_DATA_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedDataType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastUpdateTime();

  /** Sets the existing node's local value. */
  void setLastUpdateTime(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastUpdateTimeNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentVersion();

  /** Sets the existing node's local value. */
  void setCurrentVersion(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCurrentVersionNode();

  /** Gets the existing node's local value. */
  @Nullable Double getActivityTimeout();

  /** Sets the existing node's local value. */
  void setActivityTimeout(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getActivityTimeoutNode();

  /** Gets the existing node's local value. */
  @Nullable NodeId getSupportedDataType();

  /** Sets the existing node's local value. */
  void setSupportedDataType(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportedDataTypeNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getConfirmUpdateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConfirmUpdate(MethodBindings bindings, ConfirmUpdateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConfirmUpdateDetailed(
      MethodBindings bindings, ConfirmUpdateDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getCloseAndUpdateMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndUpdate(MethodBindings bindings, CloseAndUpdateHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCloseAndUpdateDetailed(
      MethodBindings bindings, CloseAndUpdateDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3 */
  @FunctionalInterface
  interface ConfirmUpdateHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UUID updateId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3 */
  @FunctionalInterface
  interface ConfirmUpdateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UUID updateId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2 */
  @FunctionalInterface
  interface CloseAndUpdateHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    ConfigurationFileTypeCloseAndUpdateOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable UInteger versionToUpdate,
        @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
        @Nullable Double revertAfterTime,
        @Nullable Double restartDelayTime)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2 */
  @FunctionalInterface
  interface CloseAndUpdateDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<ConfigurationFileTypeCloseAndUpdateOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable UInteger fileHandle,
        @Nullable UInteger versionToUpdate,
        @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
        @Nullable Double revertAfterTime,
        @Nullable Double restartDelayTime)
        throws UaException;
  }
}
