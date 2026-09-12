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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetWriterDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface WriterGroupType extends PubSubGroupType {
  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<Double> PUBLISHING_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> KEEP_ALIVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeepAliveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UByte> PRIORITY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Priority",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<String[]> LOCALE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LocaleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=295"),
          1,
          String[].class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable UShort getWriterGroupId();

  /** Sets the existing node's local value. */
  void setWriterGroupId(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getWriterGroupIdNode();

  /** Gets the existing node's local value. */
  @Nullable Double getPublishingInterval();

  /** Sets the existing node's local value. */
  void setPublishingInterval(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublishingIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable Double getKeepAliveTime();

  /** Sets the existing node's local value. */
  void setKeepAliveTime(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getKeepAliveTimeNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getPriority();

  /** Sets the existing node's local value. */
  void setPriority(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPriorityNode();

  /** Gets the existing node's local value. */
  @Nullable String @Nullable [] getLocaleIds();

  /** Sets the existing node's local value. */
  void setLocaleIds(@Nullable String @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLocaleIdsNode();

  /** Gets the existing node's local value. */
  @Nullable String getHeaderLayoutUri();

  /** Sets the existing node's local value. */
  void setHeaderLayoutUri(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getHeaderLayoutUriNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable WriterGroupTransportType getTransportSettingsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable WriterGroupMessageType getMessageSettingsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsWriterGroupType getDiagnosticsNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddDataSetWriterMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddDataSetWriter(MethodBindings bindings, AddDataSetWriterHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddDataSetWriterDetailed(
      MethodBindings bindings, AddDataSetWriterDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveDataSetWriterMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveDataSetWriter(MethodBindings bindings, RemoveDataSetWriterHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveDataSetWriterDetailed(
      MethodBindings bindings, RemoveDataSetWriterDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4 */
  @FunctionalInterface
  interface AddDataSetWriterHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable DataSetWriterDataType configuration)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.4 */
  @FunctionalInterface
  interface AddDataSetWriterDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable DataSetWriterDataType configuration)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5 */
  @FunctionalInterface
  interface RemoveDataSetWriterHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetWriterNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.5 */
  @FunctionalInterface
  interface RemoveDataSetWriterDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId dataSetWriterNodeId)
        throws UaException;
  }
}
