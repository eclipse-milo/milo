/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class LldpPortInformationTypeNode extends BaseObjectTypeNode
    implements LldpPortInformationType {
  public LldpPortInformationTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        client,
        nodeId,
        nodeClass,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  /**
   * Creates an independently owned view context retaining this exact existing node and its client.
   * Cached attributes remain shared even after SDK cache eviction. Close the returned context when
   * its views are no longer needed.
   */
  public static ClientViews createViews(LldpPortInformationTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable String getIetfBaseNetworkInterfaceName() throws UaException {
    PropertyTypeNode node = getIetfBaseNetworkInterfaceNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IetfBaseNetworkInterfaceName (declaration i=19010, owner"
              + " i=19009) on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setIetfBaseNetworkInterfaceName(@Nullable String value) throws UaException {
    PropertyTypeNode node = getIetfBaseNetworkInterfaceNameNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IetfBaseNetworkInterfaceName (declaration i=19010, owner"
              + " i=19009) on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readIetfBaseNetworkInterfaceName() throws UaException {
    return ClientMembers.await(readIetfBaseNetworkInterfaceNameAsync(), false);
  }

  @Override
  public void writeIetfBaseNetworkInterfaceName(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writeIetfBaseNetworkInterfaceNameAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readIetfBaseNetworkInterfaceNameAsync() {
    return getIetfBaseNetworkInterfaceNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:IetfBaseNetworkInterfaceName (declaration"
                            + " i=19010, owner i=19009) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (String) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeIetfBaseNetworkInterfaceNameAsync(
      @Nullable String ietfBaseNetworkInterfaceName) {
    return getIetfBaseNetworkInterfaceNameNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:IetfBaseNetworkInterfaceName (declaration"
                            + " i=19010, owner i=19009) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(ietfBaseNetworkInterfaceName));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getIetfBaseNetworkInterfaceNameNode() throws UaException {
    return ClientMembers.await(getIetfBaseNetworkInterfaceNameNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getIetfBaseNetworkInterfaceNameNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "IetfBaseNetworkInterfaceName",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:IetfBaseNetworkInterfaceName (declaration i=19010, owner"
                + " i=19009)"));
  }

  @Override
  public @Nullable UByte @Nullable [] getDestMacAddress() throws UaException {
    PropertyTypeNode node = getDestMacAddressNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DestMacAddress (declaration i=19011, owner i=19009)"
              + " on "
              + getNodeId());
    }
    return (UByte[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setDestMacAddress(@Nullable UByte @Nullable [] value) throws UaException {
    PropertyTypeNode node = getDestMacAddressNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DestMacAddress (declaration i=19011, owner i=19009)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable UByte @Nullable [] readDestMacAddress() throws UaException {
    return ClientMembers.await(readDestMacAddressAsync(), false);
  }

  @Override
  public void writeDestMacAddress(@Nullable UByte @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeDestMacAddressAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte @Nullable []> readDestMacAddressAsync() {
    return getDestMacAddressNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DestMacAddress (declaration i=19011, owner"
                            + " i=19009) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (UByte[]) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeDestMacAddressAsync(
      @Nullable UByte @Nullable [] destMacAddress) {
    return getDestMacAddressNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:DestMacAddress (declaration i=19011, owner"
                            + " i=19009) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(destMacAddress));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getDestMacAddressNode() throws UaException {
    return ClientMembers.await(getDestMacAddressNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getDestMacAddressNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DestMacAddress",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DestMacAddress (declaration i=19011, owner i=19009)"));
  }

  @Override
  public @Nullable PortIdSubtype getPortIdSubtype() throws UaException {
    PropertyTypeNode node = getPortIdSubtypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19012, owner i=19009)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "PortIdSubtype: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof PortIdSubtype)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "PortIdSubtype: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype or Int32,"
                    + " got "
                    + value);
          }
          if (PortIdSubtype.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "PortIdSubtype: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof PortIdSubtype
                ? (PortIdSubtype) value
                : PortIdSubtype.from((Integer) value);
      }
    }
    return (PortIdSubtype) convertedValue;
  }

  @Override
  public void setPortIdSubtype(@Nullable PortIdSubtype value) throws UaException {
    PropertyTypeNode node = getPortIdSubtypeNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19012, owner i=19009)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable PortIdSubtype readPortIdSubtype() throws UaException {
    return ClientMembers.await(readPortIdSubtypeAsync(), false);
  }

  @Override
  public void writePortIdSubtype(@Nullable PortIdSubtype value) throws UaException {
    try {
      StatusCode statusCode = writePortIdSubtypeAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable PortIdSubtype> readPortIdSubtypeAsync() {
    return getPortIdSubtypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19012, owner"
                            + " i=19009) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                Object value = v.getValue().getValue();
                Object convertedValue;
                {
                  if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
                    convertedValue = null;
                  } else {
                    Object elements =
                        value instanceof Matrix ? ((Matrix) value).getElements() : value;
                    int rank =
                        value instanceof Matrix
                            ? ((Matrix) value).getValueRank()
                            : ArrayUtil.getValueRank(value);
                    boolean permitted = rank == -1;
                    if (!permitted) {
                      throw new UaRuntimeException(
                          StatusCodes.Bad_TypeMismatch,
                          "PortIdSubtype: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof PortIdSubtype)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "PortIdSubtype: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype"
                                + " or Int32, got "
                                + value);
                      }
                      if (PortIdSubtype.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "PortIdSubtype: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof PortIdSubtype
                            ? (PortIdSubtype) value
                            : PortIdSubtype.from((Integer) value);
                  }
                }
                return (PortIdSubtype) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePortIdSubtypeAsync(
      @Nullable PortIdSubtype portIdSubtype) {
    return getPortIdSubtypeNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19012, owner"
                            + " i=19009) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(portIdSubtype));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPortIdSubtypeNode() throws UaException {
    return ClientMembers.await(getPortIdSubtypeNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPortIdSubtypeNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PortIdSubtype",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PortIdSubtype (declaration i=19012, owner i=19009)"));
  }

  @Override
  public @Nullable String getPortId() throws UaException {
    PropertyTypeNode node = getPortIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortId (declaration i=19013, owner i=19009)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortId(@Nullable String value) throws UaException {
    PropertyTypeNode node = getPortIdNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortId (declaration i=19013, owner i=19009)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readPortId() throws UaException {
    return ClientMembers.await(readPortIdAsync(), false);
  }

  @Override
  public void writePortId(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writePortIdAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readPortIdAsync() {
    return getPortIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PortId (declaration i=19013, owner i=19009)"
                            + " on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (String) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePortIdAsync(@Nullable String portId) {
    return getPortIdNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PortId (declaration i=19013, owner i=19009)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(portId));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public PropertyTypeNode getPortIdNode() throws UaException {
    return ClientMembers.await(getPortIdNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getPortIdNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PortId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:PortId (declaration i=19013, owner i=19009)"));
  }

  @Override
  public @Nullable String getPortDescription() throws UaException {
    PropertyTypeNode node = getPortDescriptionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortDescription (declaration i=19014, owner i=19009)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPortDescription(@Nullable String value) throws UaException {
    PropertyTypeNode node = getPortDescriptionNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PortDescription (declaration i=19014, owner i=19009)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readPortDescription() throws UaException {
    return ClientMembers.await(readPortDescriptionAsync(), false);
  }

  @Override
  public void writePortDescription(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writePortDescriptionAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readPortDescriptionAsync() {
    return getPortDescriptionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PortDescription (declaration i=19014, owner"
                            + " i=19009) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (String) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writePortDescriptionAsync(@Nullable String portDescription) {
    return getPortDescriptionNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PortDescription (declaration i=19014, owner"
                            + " i=19009) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(portDescription));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getPortDescriptionNode() throws UaException {
    return ClientMembers.await(getPortDescriptionNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getPortDescriptionNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PortDescription",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:PortDescription (declaration i=19014, owner i=19009)"));
  }

  @Override
  public @Nullable LldpManagementAddressTxPortType @Nullable [] getManagementAddressTxPort()
      throws UaException {
    PropertyTypeNode node = getManagementAddressTxPortNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ManagementAddressTxPort (declaration i=19015, owner"
              + " i=19009) on "
              + getNodeId());
    }
    return (LldpManagementAddressTxPortType[])
        decodeValue(
            node.getValue().getValue().getValue(),
            LldpManagementAddressTxPortType.class,
            ValueRanks.OneDimension);
  }

  @Override
  public void setManagementAddressTxPort(
      @Nullable LldpManagementAddressTxPortType @Nullable [] value) throws UaException {
    PropertyTypeNode node = getManagementAddressTxPortNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ManagementAddressTxPort (declaration i=19015, owner"
              + " i=19009) on "
              + getNodeId());
    }
    node.setValue(
        new Variant(
            encodeValue(value, LldpManagementAddressTxPortType.class, ValueRanks.OneDimension)));
  }

  @Override
  public @Nullable LldpManagementAddressTxPortType @Nullable [] readManagementAddressTxPort()
      throws UaException {
    return ClientMembers.await(readManagementAddressTxPortAsync(), false);
  }

  @Override
  public void writeManagementAddressTxPort(
      @Nullable LldpManagementAddressTxPortType @Nullable [] value) throws UaException {
    try {
      StatusCode statusCode = writeManagementAddressTxPortAsync(value).get();
      if (statusCode != null && !statusCode.isGood()) {
        throw new UaException(statusCode);
      }
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  @Override
  public CompletableFuture<? extends @Nullable LldpManagementAddressTxPortType @Nullable []>
      readManagementAddressTxPortAsync() {
    return getManagementAddressTxPortNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ManagementAddressTxPort (declaration i=19015,"
                            + " owner i=19009) on "
                            + getNodeId()));
              }
              return node.readAttributeAsync(AttributeId.Value);
            })
        .thenApply(
            v -> {
              if (!v.getStatusCode().isGood()) {
                throw new CompletionException(new UaException(v.getStatusCode()));
              }
              try {
                return (LldpManagementAddressTxPortType[])
                    decodeValue(
                        v.getValue().getValue(),
                        LldpManagementAddressTxPortType.class,
                        ValueRanks.OneDimension);
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeManagementAddressTxPortAsync(
      @Nullable LldpManagementAddressTxPortType @Nullable [] managementAddressTxPort) {
    return getManagementAddressTxPortNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ManagementAddressTxPort (declaration i=19015,"
                            + " owner i=19009) on "
                            + getNodeId()));
              }
              try {
                DataValue value =
                    DataValue.valueOnly(
                        new Variant(
                            encodeValue(
                                managementAddressTxPort,
                                LldpManagementAddressTxPortType.class,
                                ValueRanks.OneDimension)));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable PropertyTypeNode getManagementAddressTxPortNode() throws UaException {
    return ClientMembers.await(getManagementAddressTxPortNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getManagementAddressTxPortNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ManagementAddressTxPort",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ManagementAddressTxPort (declaration i=19015, owner"
                + " i=19009)"));
  }

  @Override
  public @Nullable FolderTypeNode getRemoteSystemsDataNode() throws UaException {
    return ClientMembers.await(getRemoteSystemsDataNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable FolderTypeNode> getRemoteSystemsDataNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "RemoteSystemsData",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:RemoteSystemsData (declaration i=19016, owner i=19009)"));
  }
}
