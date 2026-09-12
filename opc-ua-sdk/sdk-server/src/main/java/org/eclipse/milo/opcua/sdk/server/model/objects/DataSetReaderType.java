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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FieldTargetDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DataSetReaderType extends BaseObjectType {
  QualifiedProperty<Object> PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<UShort> WRITER_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "WriterGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> DATA_SET_WRITER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetWriterId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<DataSetMetaDataType> DATA_SET_META_DATA =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMetaData",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14523"),
          -1,
          DataSetMetaDataType.class);

  QualifiedProperty<DataSetFieldContentMask> DATA_SET_FIELD_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetFieldContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15583"),
          -1,
          DataSetFieldContentMask.class);

  QualifiedProperty<Double> MESSAGE_RECEIVE_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageReceiveTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<UInteger> KEY_FRAME_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "KeyFrameCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> HEADER_LAYOUT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HeaderLayoutUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<EndpointDescription[]> SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<KeyValuePair[]> DATA_SET_READER_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetReaderProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  /** Gets the existing node's local value. */
  @Nullable Object getPublisherId();

  /** Sets the existing node's local value. */
  void setPublisherId(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublisherIdNode();

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
  @Nullable UShort getDataSetWriterId();

  /** Sets the existing node's local value. */
  void setDataSetWriterId(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetWriterIdNode();

  /** Gets the existing node's local value. */
  @Nullable DataSetMetaDataType getDataSetMetaData();

  /** Sets the existing node's local value. */
  void setDataSetMetaData(@Nullable DataSetMetaDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetMetaDataNode();

  /** Gets the existing node's local value. */
  @Nullable DataSetFieldContentMask getDataSetFieldContentMask();

  /** Sets the existing node's local value. */
  void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetFieldContentMaskNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMessageReceiveTimeout();

  /** Sets the existing node's local value. */
  void setMessageReceiveTimeout(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMessageReceiveTimeoutNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getKeyFrameCount();

  /** Sets the existing node's local value. */
  void setKeyFrameCount(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getKeyFrameCountNode();

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

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode();

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityModeNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityGroupId();

  /** Sets the existing node's local value. */
  void setSecurityGroupId(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityGroupIdNode();

  /** Gets the existing node's local value. */
  @Nullable EndpointDescription @Nullable [] getSecurityKeyServices();

  /** Sets the existing node's local value. */
  void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityKeyServicesNode();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getDataSetReaderProperties();

  /** Sets the existing node's local value. */
  void setDataSetReaderProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetReaderPropertiesNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetReaderTransportType getTransportSettingsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable DataSetReaderMessageType getMessageSettingsNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubDiagnosticsDataSetReaderType getDiagnosticsNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscribedDataSetType getSubscribedDataSetNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getCreateTargetVariablesMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateTargetVariables(
      MethodBindings bindings, CreateTargetVariablesHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateTargetVariablesDetailed(
      MethodBindings bindings, CreateTargetVariablesDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getCreateDataSetMirrorMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateDataSetMirror(MethodBindings bindings, CreateDataSetMirrorHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindCreateDataSetMirrorDetailed(
      MethodBindings bindings, CreateDataSetMirrorDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5 */
  @FunctionalInterface
  interface CreateTargetVariablesHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable StatusCode @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.5 */
  @FunctionalInterface
  interface CreateTargetVariablesDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable StatusCode @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ConfigurationVersionDataType configurationVersion,
        @Nullable FieldTargetDataType @Nullable [] targetVariablesToAdd)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6 */
  @FunctionalInterface
  interface CreateDataSetMirrorHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String parentNodeName,
        @Nullable RolePermissionType @Nullable [] rolePermissions)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.8/#9.1.8.6 */
  @FunctionalInterface
  interface CreateDataSetMirrorDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String parentNodeName,
        @Nullable RolePermissionType @Nullable [] rolePermissions)
        throws UaException;
  }
}
