/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.nodes;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedFuture;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.AddressSpace.BrowseOptions;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.jspecify.annotations.Nullable;

public abstract class UaNode implements Node {

  private NodeId nodeId;
  private NodeClass nodeClass;
  private QualifiedName browseName;
  private LocalizedText displayName;
  private LocalizedText description;
  private UInteger writeMask;
  private UInteger userWriteMask;
  private RolePermissionType[] rolePermissions;
  private RolePermissionType[] userRolePermissions;
  private AccessRestrictionType accessRestrictions;

  protected final OpcUaClient client;

  /** Construct a {@link UaNode} using only attributes defined prior to OPC UA 1.04. */
  public UaNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask) {

    this.client = client;
    this.nodeId = nodeId;
    this.nodeClass = nodeClass;
    this.browseName = browseName;
    this.displayName = displayName;
    this.description = description;
    this.writeMask = writeMask;
    this.userWriteMask = userWriteMask;
  }

  /** Construct a {@link UaNode} using all attributes, including those defined by OPC UA 1.04. */
  public UaNode(
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
      AccessRestrictionType accessRestrictions) {

    this.client = client;
    this.nodeId = nodeId;
    this.nodeClass = nodeClass;
    this.browseName = browseName;
    this.displayName = displayName;
    this.description = description;
    this.writeMask = writeMask;
    this.userWriteMask = userWriteMask;
    this.rolePermissions = rolePermissions;
    this.userRolePermissions = userRolePermissions;
    this.accessRestrictions = accessRestrictions;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readNodeId()
   */
  @Override
  public synchronized NodeId getNodeId() {
    return nodeId;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readNodeClass()
   */
  @Override
  public synchronized NodeClass getNodeClass() {
    return nodeClass;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readBrowseName()
   */
  @Override
  public synchronized QualifiedName getBrowseName() {
    return browseName;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readDisplayName()
   */
  @Override
  public synchronized LocalizedText getDisplayName() {
    return displayName;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readDescription()
   */
  @Override
  @Nullable
  public synchronized LocalizedText getDescription() {
    return description;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readWriteMask()
   */
  @Override
  @Nullable
  public synchronized UInteger getWriteMask() {
    return writeMask;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readUserWriteMask()
   */
  @Override
  @Nullable
  public synchronized UInteger getUserWriteMask() {
    return userWriteMask;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readRolePermissions()
   */
  @Override
  public synchronized @Nullable RolePermissionType[] getRolePermissions() {
    return rolePermissions;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readUserRolePermissions()
   */
  @Override
  public synchronized @Nullable RolePermissionType[] getUserRolePermissions() {
    return userRolePermissions;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readAccessRestrictions()
   */
  @Override
  public synchronized @Nullable AccessRestrictionType getAccessRestrictions() {
    return accessRestrictions;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only update locally; it is not written to the server.
   *
   * @see #writeNodeId(NodeId)
   */
  @Override
  public synchronized void setNodeId(NodeId nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeNodeClass(NodeClass)
   */
  @Override
  public synchronized void setNodeClass(NodeClass nodeClass) {
    this.nodeClass = nodeClass;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeBrowseName(QualifiedName)
   */
  @Override
  public synchronized void setBrowseName(QualifiedName browseName) {
    this.browseName = browseName;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeDisplayName(LocalizedText)
   */
  @Override
  public synchronized void setDisplayName(LocalizedText displayName) {
    this.displayName = displayName;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeDescription(LocalizedText)
   */
  @Override
  public synchronized void setDescription(LocalizedText description) {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeWriteMask(UInteger)
   */
  @Override
  public synchronized void setWriteMask(UInteger writeMask) {
    this.writeMask = writeMask;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeUserWriteMask(UInteger)
   */
  @Override
  public synchronized void setUserWriteMask(UInteger userWriteMask) {
    this.userWriteMask = userWriteMask;
  }

  /**
   * {@inheritDoc} The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeRolePermissions(RolePermissionType[])
   */
  @Override
  public synchronized void setRolePermissions(RolePermissionType[] rolePermissions) {
    this.rolePermissions = rolePermissions;
  }

  /**
   * {@inheritDoc} The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeUserRolePermissions(RolePermissionType[])
   */
  @Override
  public synchronized void setUserRolePermissions(RolePermissionType[] userRolePermissions) {
    this.userRolePermissions = userRolePermissions;
  }

  /**
   * {@inheritDoc} The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeAccessRestrictions(AccessRestrictionType)
   */
  @Override
  public synchronized void setAccessRestrictions(AccessRestrictionType accessRestrictions) {
    this.accessRestrictions = accessRestrictions;
  }

  /**
   * Read the NodeId attribute for this Node from the server and update the local attribute if the
   * operation succeeds.
   *
   * @return the {@link NodeId} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public NodeId readNodeId() throws UaException {
    DataValue value = readAttribute(AttributeId.NodeId);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read NodeId failed");
    } else {
      NodeId nodeId = (NodeId) value.getValue().getValue();
      setNodeId(nodeId);
      return nodeId;
    }
  }

  /**
   * Read the NodeClass attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link NodeClass} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public NodeClass readNodeClass() throws UaException {
    DataValue value = readAttribute(AttributeId.NodeClass);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read NodeClass failed");
    } else {
      Integer i = (Integer) requireNonNullElse(value.getValue().getValue(), 0);
      NodeClass nodeClass = NodeClass.from(i);
      setNodeClass(nodeClass);
      return nodeClass;
    }
  }

  /**
   * Read the BrowseName attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link QualifiedName} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public QualifiedName readBrowseName() throws UaException {
    DataValue value = readAttribute(AttributeId.BrowseName);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read BrowseName failed");
    } else {
      QualifiedName browseName = (QualifiedName) value.getValue().getValue();
      setBrowseName(browseName);
      return browseName;
    }
  }

  /**
   * Read the DisplayName attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link LocalizedText} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public LocalizedText readDisplayName() throws UaException {
    DataValue value = readAttribute(AttributeId.DisplayName);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read DisplayName failed");
    } else {
      LocalizedText displayName = (LocalizedText) value.getValue().getValue();
      setDisplayName(displayName);
      return displayName;
    }
  }

  /**
   * Read the Description attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link LocalizedText} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  @Nullable
  public LocalizedText readDescription() throws UaException {
    DataValue value = readAttribute(AttributeId.Description);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read Description failed");
    } else {
      LocalizedText description = (LocalizedText) value.getValue().getValue();
      setDescription(description);
      return description;
    }
  }

  /**
   * Read the WriteMask attribute for this Node from the server and update the local attribute if
   * the operation succeeds.
   *
   * @return the {@link UInteger} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  @Nullable
  public UInteger readWriteMask() throws UaException {
    DataValue value = readAttribute(AttributeId.UserWriteMask);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read WriteMask failed");
    } else {
      UInteger writeMask = (UInteger) value.getValue().getValue();
      setWriteMask(writeMask);
      return writeMask;
    }
  }

  /**
   * Read the UserWriteMask attribute for this Node from the server and update the local attribute
   * if the operation succeeds.
   *
   * @return the {@link UInteger} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  @Nullable
  public UInteger readUserWriteMask() throws UaException {
    DataValue value = readAttribute(AttributeId.UserWriteMask);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read UserWriteMask failed");
    } else {
      UInteger userWriteMask = (UInteger) value.getValue().getValue();
      setUserWriteMask(userWriteMask);
      return userWriteMask;
    }
  }

  /**
   * Read the RolePermissions attribute for this Node from the server and update the local attribute
   * if the operation succeeds.
   *
   * @return the {@link RolePermissionType} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  @Nullable
  public RolePermissionType[] readRolePermissions() throws UaException {
    DataValue value = readAttribute(AttributeId.RolePermissions);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read RolePermissions failed");
    } else {
      RolePermissionType[] rolePermissions =
          cast(value.getValue().getValue(), RolePermissionType[].class);
      setRolePermissions(rolePermissions);
      return rolePermissions;
    }
  }

  /**
   * Read the UserRolePermissions attribute for this Node from the server and update the local
   * attribute if the operation succeeds.
   *
   * @return the {@link RolePermissionType} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  @Nullable
  public RolePermissionType[] readUserRolePermissions() throws UaException {
    DataValue value = readAttribute(AttributeId.UserRolePermissions);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read UserRolePermissions failed");
    } else {
      RolePermissionType[] userRolePermissions =
          cast(value.getValue().getValue(), RolePermissionType[].class);
      setUserRolePermissions(userRolePermissions);
      return userRolePermissions;
    }
  }

  /**
   * Read the AccessRestrictions attribute for this Node from the server and update the local
   * attribute if the operation succeeds.
   *
   * @return the {@link AccessRestrictionType} read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  @Nullable
  public AccessRestrictionType readAccessRestrictions() throws UaException {
    DataValue value = readAttribute(AttributeId.AccessRestrictions);

    StatusCode statusCode = value.getStatusCode();

    if (statusCode != null && statusCode.isBad()) {
      throw new UaException(statusCode, "read AccessRestriction failed");
    } else {
      AccessRestrictionType accessRestrictions =
          new AccessRestrictionType((UShort) value.getValue().getValue());
      setAccessRestrictions(accessRestrictions);
      return accessRestrictions;
    }
  }

  /**
   * Write a new NodeId attribute for this Node to the server and update the local attribute if the
   * operation succeeds.
   *
   * @param nodeId the {@link NodeId} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeNodeId(NodeId nodeId) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(nodeId));
    StatusCode statusCode = writeAttribute(AttributeId.NodeId, value);

    if (statusCode == null || statusCode.isGood()) {
      setNodeId(nodeId);
    } else {
      throw new UaException(statusCode, "write NodeId failed");
    }
  }

  /**
   * Write a new NodeClass attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param nodeClass the {@link NodeClass} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeNodeClass(NodeClass nodeClass) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(nodeClass));
    StatusCode statusCode = writeAttribute(AttributeId.NodeClass, value);

    if (statusCode == null || statusCode.isGood()) {
      setNodeClass(nodeClass);
    } else {
      throw new UaException(statusCode, "write NodeClass failed");
    }
  }

  /**
   * Write a new BrowseName attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param browseName the {@link QualifiedName} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeBrowseName(QualifiedName browseName) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(browseName));
    StatusCode statusCode = writeAttribute(AttributeId.BrowseName, value);

    if (statusCode == null || statusCode.isGood()) {
      setBrowseName(browseName);
    } else {
      throw new UaException(statusCode, "write BrowseName failed");
    }
  }

  /**
   * Write a new DisplayName attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param displayName the {@link LocalizedText} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeDisplayName(LocalizedText displayName) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(displayName));
    StatusCode statusCode = writeAttribute(AttributeId.DisplayName, value);

    if (statusCode == null || statusCode.isGood()) {
      setDisplayName(displayName);
    } else {
      throw new UaException(statusCode, "write DisplayName failed");
    }
  }

  /**
   * Write a new Description attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param description the {@link LocalizedText} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeDescription(LocalizedText description) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(description));
    StatusCode statusCode = writeAttribute(AttributeId.Description, value);

    if (statusCode == null || statusCode.isGood()) {
      setDescription(description);
    } else {
      throw new UaException(statusCode, "write Description failed");
    }
  }

  /**
   * Write a new WriteMask attribute for this Node to the server and update the local attribute if
   * the operation succeeds.
   *
   * @param writeMask the {@link UInteger} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeWriteMask(UInteger writeMask) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(writeMask));
    StatusCode statusCode = writeAttribute(AttributeId.WriteMask, value);

    if (statusCode == null || statusCode.isGood()) {
      setWriteMask(writeMask);
    } else {
      throw new UaException(statusCode, "write WriteMask failed");
    }
  }

  /**
   * Write a new UserWriteMask attribute for this Node to the server and update the local attribute
   * if the operation succeeds.
   *
   * @param userWriteMask the {@link UInteger} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeUserWriteMask(UInteger userWriteMask) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(userWriteMask));
    StatusCode statusCode = writeAttribute(AttributeId.UserWriteMask, value);

    if (statusCode == null || statusCode.isGood()) {
      setUserWriteMask(userWriteMask);
    } else {
      throw new UaException(statusCode, "write UserWriteMask failed");
    }
  }

  /**
   * Write a new RolePermissions attribute for this Node to the server and update the local
   * attribute if the operation succeeds.
   *
   * @param rolePermissions the {@link RolePermissionType} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeRolePermissions(RolePermissionType[] rolePermissions) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(rolePermissions));
    StatusCode statusCode = writeAttribute(AttributeId.RolePermissions, value);

    if (statusCode == null || statusCode.isGood()) {
      setRolePermissions(rolePermissions);
    } else {
      throw new UaException(statusCode, "write RolePermissions failed");
    }
  }

  /**
   * Write a new UserRolePermissions attribute for this Node to the server and update the local
   * attribute if the operation succeeds.
   *
   * @param userRolePermissions the {@link RolePermissionType} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeUserRolePermissions(RolePermissionType[] userRolePermissions)
      throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(userRolePermissions));
    StatusCode statusCode = writeAttribute(AttributeId.UserRolePermissions, value);

    if (statusCode == null || statusCode.isGood()) {
      setUserRolePermissions(userRolePermissions);
    } else {
      throw new UaException(statusCode, "write UserRolePermissions failed");
    }
  }

  /**
   * Write a new AccessRestrictions attribute for this Node to the server and update the local
   * attribute if the operation succeeds.
   *
   * @param accessRestrictions the {@link AccessRestrictionType} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeAccessRestrictions(AccessRestrictionType accessRestrictions) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(accessRestrictions.getValue()));
    StatusCode statusCode = writeAttribute(AttributeId.AccessRestrictions, value);

    if (statusCode == null || statusCode.isGood()) {
      setAccessRestrictions(accessRestrictions);
    } else {
      throw new UaException(statusCode, "write AccessRestrictions failed");
    }
  }

  /**
   * Read the attribute identified by an {@code attributeId} from the server.
   *
   * <p>This operation does not update the local attribute.
   *
   * @param attributeId the {@link AttributeId} of the attribute to read.
   * @return a {@link DataValue} containing the attribute value.
   * @throws UaException if a service-level error occurs.
   */
  public DataValue readAttribute(AttributeId attributeId) throws UaException {
    try {
      return readAttributeAsync(attributeId).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Write {@code value} to the attribute identified by {@code attributeId}.
   *
   * <p>This operation does not update the local attribute.
   *
   * @param attributeId the {@link AttributeId} of the attribute to write.
   * @param value a {@link DataValue} containing the attribute value.
   * @return the {@link StatusCode} from the write operation.
   * @throws UaException if a service-level error occurs.
   */
  public StatusCode writeAttribute(AttributeId attributeId, DataValue value) throws UaException {
    try {
      return writeAttributeAsync(attributeId, value).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * An asynchronous implementation of {@link #readAttribute(AttributeId)}.
   *
   * @return a CompletableFuture that completes successfully with the attribute value or completes
   *     exceptionally if a service-level error occurs.
   * @see #readAttribute(AttributeId)
   */
  public CompletableFuture<DataValue> readAttributeAsync(AttributeId attributeId) {
    ReadValueId readValueId =
        new ReadValueId(getNodeId(), attributeId.uid(), null, QualifiedName.NULL_VALUE);

    CompletableFuture<ReadResponse> future =
        client.readAsync(0.0, TimestampsToReturn.Both, List.of(readValueId));

    return future.thenApply(response -> requireNonNull(response.getResults())[0]);
  }

  /**
   * An asynchronous implementation of {@link #writeAttribute(AttributeId, DataValue)}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  public CompletableFuture<StatusCode> writeAttributeAsync(
      AttributeId attributeId, DataValue value) {
    var writeValue = new WriteValue(getNodeId(), attributeId.uid(), null, value);

    CompletableFuture<WriteResponse> future = client.writeAsync(List.of(writeValue));

    return future.thenApply(response -> requireNonNull(response.getResults())[0]);
  }

  /**
   * Call the Browse service to get this {@link UaNode}'s references, using the currently configured
   * {@link BrowseOptions} from the {@link AddressSpace}.
   *
   * @return a List of {@link ReferenceDescription}s.
   * @throws UaException if a service-level error occurs.
   * @see AddressSpace#getBrowseOptions()
   * @see AddressSpace#modifyBrowseOptions(Consumer)
   * @see AddressSpace#setBrowseOptions(BrowseOptions)
   */
  public List<ReferenceDescription> browse() throws UaException {
    return browse(client.getAddressSpace().getBrowseOptions());
  }

  /**
   * Call the Browse service to get this {@link UaNode}'s references.
   *
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a List of {@link ReferenceDescription}s.
   * @throws UaException if a service-level error occurs.
   */
  public List<ReferenceDescription> browse(BrowseOptions browseOptions) throws UaException {
    return client.getAddressSpace().browse(this, browseOptions);
  }

  /**
   * Call the Browse service to get this {@link UaNode}'s references, using the currently configured
   * {@link BrowseOptions} from the {@link AddressSpace}.
   *
   * <p>This call completes asynchronously.
   *
   * @return a CompletableFuture that completes successfully with the List of references or
   *     completes exceptionally if a service-level error occurs.
   * @see AddressSpace#getBrowseOptions()
   * @see AddressSpace#modifyBrowseOptions(Consumer)
   * @see AddressSpace#setBrowseOptions(BrowseOptions)
   */
  public CompletableFuture<List<ReferenceDescription>> browseAsync() {
    return browseAsync(client.getAddressSpace().getBrowseOptions());
  }

  /**
   * Call the Browse service to get this {@link UaNode}'s references.
   *
   * <p>This call completes asynchronously.
   *
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a CompletableFuture that completes successfully with the List of references or
   *     completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<List<ReferenceDescription>> browseAsync(BrowseOptions browseOptions) {
    return client.getAddressSpace().browseAsync(this, browseOptions);
  }

  /**
   * Call the Browse service to get the {@link UaNode}s referenced by this Node, using the currently
   * configured {@link BrowseOptions} from the {@link AddressSpace}.
   *
   * @return a List of {@link UaNode}s referenced by this Node.
   * @throws UaException if a service-level error occurs.
   * @see AddressSpace#getBrowseOptions()
   * @see AddressSpace#modifyBrowseOptions(Consumer)
   * @see AddressSpace#setBrowseOptions(BrowseOptions)
   */
  public List<? extends UaNode> browseNodes() throws UaException {
    return browseNodes(client.getAddressSpace().getBrowseOptions());
  }

  /**
   * Call the Browse service to get the {@link UaNode}s referenced by this Node.
   *
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a List of {@link UaNode}s referenced by this Node.
   * @throws UaException if a service-level error occurs.
   */
  public List<? extends UaNode> browseNodes(BrowseOptions browseOptions) throws UaException {
    return client.getAddressSpace().browseNodes(this, browseOptions);
  }

  /**
   * Call the Browse service to get the {@link UaNode}s referenced by this Node, using the currently
   * configured {@link BrowseOptions} from the {@link AddressSpace}.
   *
   * <p>This call completes asynchronously.
   *
   * @return a CompletableFuture that completes successfully with the List of {@link UaNode}s
   *     referenced by this Node or completes exceptionally if a service-level error occurs.
   * @see AddressSpace#getBrowseOptions()
   * @see AddressSpace#modifyBrowseOptions(Consumer)
   * @see AddressSpace#setBrowseOptions(BrowseOptions)
   */
  public CompletableFuture<List<? extends UaNode>> browseNodesAsync() {
    return browseNodesAsync(client.getAddressSpace().getBrowseOptions());
  }

  /**
   * Call the Browse service to get the {@link UaNode}s referenced by this Node.
   *
   * <p>This call completes asynchronously.
   *
   * @param browseOptions the {@link BrowseOptions} to browse with.
   * @return a CompletableFuture that completes successfully with the List of {@link UaNode}s
   *     referenced by this Node or completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<List<? extends UaNode>> browseNodesAsync(BrowseOptions browseOptions) {
    return client.getAddressSpace().browseNodesAsync(this, browseOptions);
  }

  /**
   * Refresh (read) the attributes identified by {@code attributeIds} and store their values
   * locally.
   *
   * @param attributeIds a Set identifying the attributes to refresh.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public List<DataValue> refresh(Set<AttributeId> attributeIds) throws UaException {
    try {
      return refreshAsync(attributeIds).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /** An asynchronous implementation of {@link #refresh(Set)}. */
  public CompletableFuture<List<DataValue>> refreshAsync(Set<AttributeId> attributeIds) {
    List<ReadValueId> readValueIds =
        attributeIds.stream()
            .map(id -> new ReadValueId(nodeId, id.uid(), null, QualifiedName.NULL_VALUE))
            .collect(Collectors.toList());

    CompletableFuture<ReadResponse> future =
        client.readAsync(0.0, TimestampsToReturn.Neither, readValueIds);

    return future.thenApply(
        response -> {
          DataValue[] values = requireNonNullElse(response.getResults(), new DataValue[0]);

          assert attributeIds.size() == values.length;

          Iterator<AttributeId> ai = attributeIds.iterator();
          Iterator<DataValue> vi = Arrays.stream(values).iterator();

          synchronized (UaNode.this) {
            if (ai.hasNext() && vi.hasNext()) {
              AttributeId attributeId = ai.next();
              DataValue value = vi.next();
              StatusCode statusCode = value.getStatusCode();

              if (statusCode == null || statusCode.isGood()) {
                setAttributeValue(attributeId, value);
              }
            }
          }

          return List.of(values);
        });
  }

  /**
   * Synchronize (write) the local attributes identified by {@code attributeIds} to the server.
   *
   * @param attributeIds a Set identifying the local attributes to write.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public List<StatusCode> synchronize(Set<AttributeId> attributeIds) throws UaException {
    try {
      return synchronizeAsync(attributeIds).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /** An asynchronous implementation of {@link #synchronize(Set)}. */
  public CompletableFuture<List<StatusCode>> synchronizeAsync(Set<AttributeId> attributeIds) {
    List<WriteValue> writeValues;

    synchronized (this) {
      writeValues =
          attributeIds.stream()
              .map(id -> new WriteValue(nodeId, id.uid(), null, getAttributeValue(id)))
              .collect(Collectors.toList());
    }

    return client
        .writeAsync(writeValues)
        .thenApply(
            response -> {
              StatusCode[] results = requireNonNullElse(response.getResults(), new StatusCode[0]);

              return List.of(results);
            });
  }

  /**
   * Make this Node instance the canonical instance of the Node identified this Node's {@link
   * NodeId}.
   *
   * <p>This Node will not expire from the cache until it is explicitly invalidated and all get or
   * create calls to the NodeCache will return this instance.
   *
   * <p>If a canonical instance already exists in the NodeCache then that instance is returned
   * instead.
   *
   * @return this {@link UaNode} if there was not already a canonical instance in the NodeCache,
   *     otherwise the other canonical instance from the NodeCache.
   */
  public UaNode canonicalize() {
    return client.getAddressSpace().getNodeCache().canonicalize(this);
  }

  /**
   * Invalidate this Node's entry in the NodeCache as well as its status as the canonical Node, if
   * applicable
   *
   * @return this {@link UaNode}.
   */
  public UaNode invalidate() {
    client.getAddressSpace().getNodeCache().invalidate(nodeId);
    return this;
  }

  protected DataValue getAttributeValue(AttributeId attributeId) {
    switch (attributeId) {
      case NodeId:
        return DataValue.valueOnly(new Variant(getNodeId()));
      case NodeClass:
        return DataValue.valueOnly(new Variant(getNodeClass().getValue()));
      case BrowseName:
        return DataValue.valueOnly(new Variant(getBrowseName()));
      case DisplayName:
        return DataValue.valueOnly(new Variant(getDisplayName()));
      case Description:
        return DataValue.valueOnly(new Variant(getDescription()));
      case WriteMask:
        return DataValue.valueOnly(new Variant(getWriteMask()));
      case UserWriteMask:
        return DataValue.valueOnly(new Variant(getUserWriteMask()));
      case RolePermissions:
        return DataValue.valueOnly(new Variant(getRolePermissions()));
      case UserRolePermissions:
        return DataValue.valueOnly(new Variant(getUserRolePermissions()));
      case AccessRestrictions:
        return DataValue.valueOnly(new Variant(getAccessRestrictions().getValue()));
      default:
        throw new IllegalArgumentException("attributeId: " + attributeId);
    }
  }

  protected void setAttributeValue(AttributeId attributeId, DataValue value) {
    switch (attributeId) {
      case NodeId:
        {
          setNodeId((NodeId) value.getValue().getValue());
          break;
        }
      case NodeClass:
        {
          Integer i = (Integer) requireNonNullElse(value.getValue().getValue(), 0);
          NodeClass nodeClass = NodeClass.from(i);
          setNodeClass(nodeClass);
          break;
        }
      case BrowseName:
        {
          setBrowseName((QualifiedName) value.getValue().getValue());
          break;
        }
      case DisplayName:
        {
          setDisplayName((LocalizedText) value.getValue().getValue());
          break;
        }
      case Description:
        {
          setDescription((LocalizedText) value.getValue().getValue());
          break;
        }
      case WriteMask:
        {
          setWriteMask((UInteger) value.getValue().getValue());
          break;
        }
      case UserWriteMask:
        {
          setUserWriteMask((UInteger) value.getValue().getValue());
          break;
        }
      case RolePermissions:
        {
          setRolePermissions((RolePermissionType[]) value.getValue().getValue());
          break;
        }
      case UserRolePermissions:
        {
          setUserRolePermissions((RolePermissionType[]) value.getValue().getValue());
          break;
        }
      case AccessRestrictions:
        {
          setAccessRestrictions(new AccessRestrictionType((UShort) value.getValue().getValue()));
          break;
        }
      default:
        throw new IllegalArgumentException("attributeId: " + attributeId);
    }
  }

  protected CompletableFuture<UaNode> getMemberNodeAsync(
      String namespaceUri, String name, ExpandedNodeId referenceTypeId, boolean includeSubtypes) {

    return readNamespaceIndex(namespaceUri)
        .thenCompose(
            index -> {
              QualifiedName qualifiedName = new QualifiedName(index, name);

              return findMemberNodeId(qualifiedName, referenceTypeId, includeSubtypes)
                  .thenCompose(
                      nodeXni -> {
                        AddressSpace addressSpace = client.getAddressSpace();

                        return addressSpace
                            .toNodeIdAsync(nodeXni)
                            .thenCompose(addressSpace::getNodeAsync)
                            .thenApply(UaNode.class::cast);
                      });
            });
  }

  protected CompletableFuture<ExpandedNodeId> findMemberNodeId(
      QualifiedName name, ExpandedNodeId referenceTypeId, boolean includeSubtypes) {

    // TODO cache BrowsePath translation or member nodes?

    List<BrowsePath> browsePaths = new ArrayList<>();

    try {
      browsePaths.add(
          new BrowsePath(
              getNodeId(),
              new RelativePath(
                  new RelativePathElement[] {
                    new RelativePathElement(
                        referenceTypeId.toNodeIdOrThrow(client.getNamespaceTable()),
                        false,
                        includeSubtypes,
                        name)
                  })));
    } catch (Exception e) {
      return failedFuture(e);
    }

    return client
        .translateBrowsePathsAsync(browsePaths)
        .thenCompose(
            r -> {
              BrowsePathResult result = requireNonNull(r.getResults())[0];

              if (result.getStatusCode().isGood()) {
                BrowsePathTarget[] targets = requireNonNull(result.getTargets());
                return completedFuture(targets[0].getTargetId());
              } else {
                return failedUaFuture(result.getStatusCode());
              }
            });
  }

  protected CompletableFuture<UShort> readNamespaceIndex(String namespaceUri) {
    UShort namespaceIndex = client.getNamespaceTable().getIndex(namespaceUri);

    if (namespaceIndex != null) {
      return completedFuture(namespaceIndex);
    } else {
      return client
          .readNamespaceTableAsync()
          .thenCompose(
              namespaceTable -> {
                UShort index = client.getNamespaceTable().getIndex(namespaceUri);
                if (index != null) {
                  return completedFuture(index);
                } else {
                  return failedFuture(new Exception("unknown namespace: " + namespaceUri));
                }
              });
    }
  }

  protected CompletableFuture<? extends UaNode> getComponentAsync(
      QualifiedName browseName, NodeClass nodeClass) {
    UInteger nodeClassMask = uint(nodeClass.getValue());
    UInteger resultMask = uint(BrowseResultMask.All.getValue());

    CompletableFuture<BrowseResult> future =
        client.browseAsync(
            new BrowseDescription(
                getNodeId(),
                BrowseDirection.Forward,
                NodeIds.HasComponent,
                false,
                nodeClassMask,
                resultMask));

    return future.thenCompose(
        result -> {
          ReferenceDescription[] references =
              requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

          Optional<CompletableFuture<? extends UaNode>> node =
              Stream.of(references)
                  .filter(r -> browseName.equals(r.getBrowseName()))
                  .flatMap(
                      r -> {
                        Optional<CompletableFuture<? extends UaNode>> opt =
                            r.getNodeId()
                                .toNodeId(client.getNamespaceTable())
                                .map(id -> client.getAddressSpace().getNodeAsync(id));

                        return opt.stream();
                      })
                  .findFirst();

          return node.orElse(failedUaFuture(StatusCodes.Bad_NotFound));
        });
  }

  protected CompletableFuture<PropertyTypeNode> getPropertyNodeAsync(
      QualifiedProperty<?> property) {
    return property
        .getQualifiedName(client.getNamespaceTable())
        .map(this::getPropertyNodeAsync)
        .orElse(failedUaFuture(StatusCodes.Bad_NotFound));
  }

  protected CompletableFuture<PropertyTypeNode> getPropertyNodeAsync(QualifiedName browseName) {
    UInteger nodeClassMask = uint(NodeClass.Variable.getValue());
    UInteger resultMask = uint(BrowseResultMask.BrowseName.getValue());

    CompletableFuture<BrowseResult> future =
        client.browseAsync(
            new BrowseDescription(
                getNodeId(),
                BrowseDirection.Forward,
                NodeIds.HasProperty,
                false,
                nodeClassMask,
                resultMask));

    return future.thenCompose(
        result -> {
          ReferenceDescription[] references =
              requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

          Optional<CompletableFuture<PropertyTypeNode>> node =
              Stream.of(references)
                  .filter(r -> browseName.equals(r.getBrowseName()))
                  .flatMap(
                      r -> {
                        Optional<CompletableFuture<PropertyTypeNode>> opt =
                            r.getNodeId()
                                .toNodeId(client.getNamespaceTable())
                                .map(
                                    id ->
                                        client
                                            .getAddressSpace()
                                            .getNodeAsync(id)
                                            .thenApply(n -> (PropertyTypeNode) n));

                        return opt.stream();
                      })
                  .findFirst();

          return node.orElse(failedUaFuture(StatusCodes.Bad_NotFound));
        });
  }

  public <T> CompletableFuture<T> getProperty(QualifiedProperty<T> property) {
    return getPropertyNodeAsync(property)
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value))
        .thenApply(value -> cast(value.getValue().getValue(), property.getJavaType()));
  }

  protected <T> CompletableFuture<StatusCode> setProperty(QualifiedProperty<T> property, T value) {
    return getPropertyNodeAsync(property)
        .thenCompose(
            node ->
                node.writeAttributeAsync(
                    AttributeId.Value, DataValue.valueOnly(new Variant(value))));
  }

  protected CompletableFuture<DataValue> readProperty(QualifiedProperty<?> property) {
    return getPropertyNodeAsync(property)
        .thenCompose(node -> node.readAttributeAsync(AttributeId.Value));
  }

  protected CompletableFuture<StatusCode> writeProperty(
      QualifiedProperty<?> property, DataValue value) {
    return getPropertyNodeAsync(property)
        .thenCompose(node -> node.writeAttributeAsync(AttributeId.Value, value));
  }

  /**
   * An implementation of cast with special handling for {@link UaEnumeratedType} and {@link
   * UaStructuredType} destination types.
   *
   * <p>If the destination type is a {@link UaEnumeratedType} and the from object is an Integer, an
   * attempt is made to convert the Integer into the corresponding UaEnumeration type.
   *
   * <p>If the destination type is a {@link UaStructuredType} and the from object is an {@link
   * ExtensionObject}, an attempt is made to decode the {@link ExtensionObject} into an object cast
   * to the type of {@code clazz}.
   *
   * @param o the Object to cast from.
   * @param clazz the type to cast {@code o} to.
   * @return the object after casting, or null if {@code o} is null.
   */
  protected <T> T cast(Object o, Class<T> clazz) {
    if (UaEnumeratedType.class.isAssignableFrom(clazz) && o instanceof Integer) {
      try {
        Object enumeration = clazz.getMethod("from", new Class[] {Integer.class}).invoke(null, o);

        return clazz.cast(enumeration);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        return null;
      }
    } else if (o instanceof ExtensionObject) {
      ExtensionObject xo = (ExtensionObject) o;
      Object decoded = xo.decode(client.getStaticEncodingContext());
      return clazz.cast(decoded);
    } else if (o instanceof ExtensionObject[]) {
      ExtensionObject[] xos = (ExtensionObject[]) o;
      Class<?> componentType = clazz.getComponentType();

      Object array = Array.newInstance(componentType, xos.length);

      for (int i = 0; i < xos.length; i++) {
        ExtensionObject xo = xos[i];

        Object decoded = xo.decode(client.getStaticEncodingContext());

        Array.set(array, i, componentType.cast(decoded));
      }

      return clazz.cast(array);
    } else {
      return clazz.cast(o);
    }
  }
}
