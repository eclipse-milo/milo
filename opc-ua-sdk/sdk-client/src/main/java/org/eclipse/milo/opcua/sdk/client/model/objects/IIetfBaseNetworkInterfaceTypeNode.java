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
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeNode;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class IIetfBaseNetworkInterfaceTypeNode extends BaseInterfaceTypeNode
    implements IIetfBaseNetworkInterfaceType {
  public IIetfBaseNetworkInterfaceTypeNode(
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
  public static ClientViews createViews(IIetfBaseNetworkInterfaceTypeNode node) {
    Objects.requireNonNull(node, "node");
    return ClientViews.forNode(node.client, node);
  }

  @Override
  public @Nullable InterfaceAdminStatus getAdminStatus() throws UaException {
    BaseDataVariableTypeNode node = getAdminStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"
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
              "AdminStatus: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof InterfaceAdminStatus)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "AdminStatus: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus or"
                    + " Int32, got "
                    + value);
          }
          if (InterfaceAdminStatus.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "AdminStatus: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof InterfaceAdminStatus
                ? (InterfaceAdminStatus) value
                : InterfaceAdminStatus.from((Integer) value);
      }
    }
    return (InterfaceAdminStatus) convertedValue;
  }

  @Override
  public void setAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException {
    BaseDataVariableTypeNode node = getAdminStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable InterfaceAdminStatus readAdminStatus() throws UaException {
    return ClientMembers.await(readAdminStatusAsync(), false);
  }

  @Override
  public void writeAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException {
    try {
      StatusCode statusCode = writeAdminStatusAsync(value).get();
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
  public CompletableFuture<? extends @Nullable InterfaceAdminStatus> readAdminStatusAsync() {
    return getAdminStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                            + " i=24148) on "
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
                          "AdminStatus: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof InterfaceAdminStatus)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "AdminStatus: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                                + " or Int32, got "
                                + value);
                      }
                      if (InterfaceAdminStatus.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "AdminStatus: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof InterfaceAdminStatus
                            ? (InterfaceAdminStatus) value
                            : InterfaceAdminStatus.from((Integer) value);
                  }
                }
                return (InterfaceAdminStatus) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeAdminStatusAsync(
      @Nullable InterfaceAdminStatus adminStatus) {
    return getAdminStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                            + " i=24148) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(adminStatus));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getAdminStatusNode() throws UaException {
    return ClientMembers.await(getAdminStatusNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getAdminStatusNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AdminStatus",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"));
  }

  @Override
  public @Nullable InterfaceOperStatus getOperStatus() throws UaException {
    BaseDataVariableTypeNode node = getOperStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"
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
              "OperStatus: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof InterfaceOperStatus)) {
          if (!(value instanceof Integer)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "OperStatus: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus or"
                    + " Int32, got "
                    + value);
          }
          if (InterfaceOperStatus.from((Integer) value) == null) {
            throw new UaException(
                StatusCodes.Bad_OutOfRange,
                "OperStatus: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof InterfaceOperStatus
                ? (InterfaceOperStatus) value
                : InterfaceOperStatus.from((Integer) value);
      }
    }
    return (InterfaceOperStatus) convertedValue;
  }

  @Override
  public void setOperStatus(@Nullable InterfaceOperStatus value) throws UaException {
    BaseDataVariableTypeNode node = getOperStatusNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable InterfaceOperStatus readOperStatus() throws UaException {
    return ClientMembers.await(readOperStatusAsync(), false);
  }

  @Override
  public void writeOperStatus(@Nullable InterfaceOperStatus value) throws UaException {
    try {
      StatusCode statusCode = writeOperStatusAsync(value).get();
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
  public CompletableFuture<? extends @Nullable InterfaceOperStatus> readOperStatusAsync() {
    return getOperStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                            + " i=24148) on "
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
                          "OperStatus: ValueRank=-1 does not permit rank " + rank);
                    }
                    if (value != null && !((Object) value instanceof InterfaceOperStatus)) {
                      if (!(value instanceof Integer)) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_TypeMismatch,
                            "OperStatus: expected"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus"
                                + " or Int32, got "
                                + value);
                      }
                      if (InterfaceOperStatus.from((Integer) value) == null) {
                        throw new UaRuntimeException(
                            StatusCodes.Bad_OutOfRange,
                            "OperStatus: unknown"
                                + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus"
                                + " value "
                                + value);
                      }
                    }
                    convertedValue =
                        value == null || value instanceof InterfaceOperStatus
                            ? (InterfaceOperStatus) value
                            : InterfaceOperStatus.from((Integer) value);
                  }
                }
                return (InterfaceOperStatus) convertedValue;
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeOperStatusAsync(
      @Nullable InterfaceOperStatus operStatus) {
    return getOperStatusNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                            + " i=24148) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(operStatus));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public BaseDataVariableTypeNode getOperStatusNode() throws UaException {
    return ClientMembers.await(getOperStatusNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableTypeNode> getOperStatusNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OperStatus",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"));
  }

  @Override
  public @Nullable String getPhysAddress() throws UaException {
    BaseDataVariableTypeNode node = getPhysAddressNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPhysAddress(@Nullable String value) throws UaException {
    BaseDataVariableTypeNode node = getPhysAddressNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable String readPhysAddress() throws UaException {
    return ClientMembers.await(readPhysAddressAsync(), false);
  }

  @Override
  public void writePhysAddress(@Nullable String value) throws UaException {
    try {
      StatusCode statusCode = writePhysAddressAsync(value).get();
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
  public CompletableFuture<? extends @Nullable String> readPhysAddressAsync() {
    return getPhysAddressNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                            + " i=24148) on "
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
  public CompletableFuture<StatusCode> writePhysAddressAsync(@Nullable String physAddress) {
    return getPhysAddressNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                            + " i=24148) on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(physAddress));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getPhysAddressNode() throws UaException {
    return ClientMembers.await(getPhysAddressNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableTypeNode> getPhysAddressNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PhysAddress",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"));
  }

  @Override
  public @Nullable ULong getSpeed() throws UaException {
    AnalogUnitTypeNode node = getSpeedNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
              + " on "
              + getNodeId());
    }
    return (ULong) node.getValue().getValue().getValue();
  }

  @Override
  public void setSpeed(@Nullable ULong value) throws UaException {
    AnalogUnitTypeNode node = getSpeedNode();
    if (node == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new Variant(value));
  }

  @Override
  public @Nullable ULong readSpeed() throws UaException {
    return ClientMembers.await(readSpeedAsync(), false);
  }

  @Override
  public void writeSpeed(@Nullable ULong value) throws UaException {
    try {
      StatusCode statusCode = writeSpeedAsync(value).get();
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
  public CompletableFuture<? extends @Nullable ULong> readSpeedAsync() {
    return getSpeedNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
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
                return (ULong) v.getValue().getValue();
              } catch (UaRuntimeException e) {
                throw new CompletionException(new UaException(e));
              }
            });
  }

  @Override
  public CompletableFuture<StatusCode> writeSpeedAsync(@Nullable ULong speed) {
    return getSpeedNodeAsync()
        .thenCompose(
            node -> {
              if (node == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
                            + " on "
                            + getNodeId()));
              }
              try {
                DataValue value = DataValue.valueOnly(new Variant(speed));
                return node.writeAttributeAsync(AttributeId.Value, value);
              } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  @Override
  public AnalogUnitTypeNode getSpeedNode() throws UaException {
    return ClientMembers.await(getSpeedNodeAsync(), false);
  }

  @Override
  public CompletableFuture<? extends AnalogUnitTypeNode> getSpeedNodeAsync() {
    return ClientMembers.lookup(
        client,
        getNodeId(),
        AnalogUnitTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Speed",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"));
  }
}
