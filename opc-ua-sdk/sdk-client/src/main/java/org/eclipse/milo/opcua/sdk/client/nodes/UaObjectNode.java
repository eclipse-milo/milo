/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.nodes;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedFuture;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedUaFuture;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.AddressSpace;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethod;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNode;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectNodeProperties;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NamingRuleType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.FutureUtils;

public class UaObjectNode extends UaNode implements ObjectNode {

  private UByte eventNotifier;

  /** Construct a {@link UaObjectNode} using only attributes defined prior to OPC UA 1.04. */
  public UaObjectNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      UByte eventNotifier) {

    super(
        client, nodeId, nodeClass, browseName, displayName, description, writeMask, userWriteMask);

    this.eventNotifier = eventNotifier;
  }

  /**
   * Construct a {@link UaObjectNode} using all attributes, including those defined by OPC UA 1.04.
   */
  public UaObjectNode(
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
        accessRestrictions);

    this.eventNotifier = eventNotifier;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The returned attribute is the most recently seen value; it is not read live from the server.
   *
   * @see #readEventNotifier()
   */
  @Override
  public synchronized UByte getEventNotifier() {
    return eventNotifier;
  }

  /**
   * {@inheritDoc}
   *
   * <p>The attribute is only updated locally; it is not written to the server.
   *
   * @see #writeEventNotifier(UByte)
   */
  @Override
  public synchronized void setEventNotifier(UByte eventNotifier) {
    this.eventNotifier = eventNotifier;
  }

  /**
   * Read the EventNotifier attribute for this Node from the server and update the local attribute
   * if the operation succeeds.
   *
   * @return the {@link UByte} read from the server.
   * @throws UaException if a service- or operation-level error occurs.s
   */
  public UByte readEventNotifier() throws UaException {
    DataValue value = readAttribute(AttributeId.EventNotifier);

    StatusCode statusCode = value.statusCode();

    if (statusCode.isBad()) {
      throw new UaException(statusCode, "read EventNotifier failed");
    } else {
      UByte eventNotifier = (UByte) value.value().value();
      setEventNotifier(eventNotifier);
      return eventNotifier;
    }
  }

  /**
   * Write a new EventNotifier attribute for this Node to the server and update the local attribute
   * if the operation succeeds.
   *
   * @param eventNotifier the {@link UByte} to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  public void writeEventNotifier(UByte eventNotifier) throws UaException {
    DataValue value = DataValue.valueOnly(new Variant(eventNotifier));
    StatusCode statusCode = writeAttribute(AttributeId.EventNotifier, value);

    if (statusCode == null || statusCode.isGood()) {
      setEventNotifier(eventNotifier);
    } else {
      throw new UaException(statusCode, "write EventNotifier failed");
    }
  }

  /**
   * Call the method named {@code methodName} on this Object, if it exists, using {@code inputs} as
   * the input argument values.
   *
   * @param methodName the name of the method to call.
   * @param inputs the input argument values.
   * @return the method's output argument values if the call was successful.
   * @throws UaException if an operation- or service-level error occurs or if a method named {@code
   *     methodName} could not be found.
   */
  public Variant[] callMethod(String methodName, Variant[] inputs) throws UaException {
    return getMethod(methodName).call(inputs);
  }

  /**
   * Call the method named {@code methodName} on this Object, if it exists, using {@code inputs} as
   * the input argument values.
   *
   * @param methodName the name of the method to call.
   * @param inputs the input argument values.
   * @return the method's output argument values if the call was successful.
   * @throws UaException if an operation- or service-level error occurs or if a method named {@code
   *     methodName} could not be found.
   */
  public Variant[] callMethod(QualifiedName methodName, Variant[] inputs) throws UaException {
    return getMethod(methodName).call(inputs);
  }

  /**
   * Call the method named {@code methodName} on this Object, if it exists, using {@code inputs} as
   * the input argument values.
   *
   * <p>This call completes asynchronously.
   *
   * @param methodName the name of the method to call.
   * @param inputs the input argument values.
   * @return a {@link CompletableFuture} that completes successfully with the method's output
   *     argument values if the call was successful, or completes exceptionally if an operation- or
   *     service-level error occurs or if a method named {@code methodName} could not be found.
   */
  public CompletableFuture<Variant[]> callMethodAsync(String methodName, Variant[] inputs) {
    return getMethodAsync(methodName).thenCompose(m -> m.callAsync(inputs));
  }

  /**
   * Call the method named {@code methodName} on this Object, if it exists, using {@code inputs} as
   * the input argument values.
   *
   * <p>This call completes asynchronously.
   *
   * @param methodName the name of the method to call.
   * @param inputs the input argument values.
   * @return a {@link CompletableFuture} that completes successfully with the method's output
   *     argument values if the call was successful, or completes exceptionally if an operation- or
   *     service-level error occurs or if a method named {@code methodName} could not be found.
   */
  public CompletableFuture<Variant[]> callMethodAsync(QualifiedName methodName, Variant[] inputs) {
    return getMethodAsync(methodName).thenCompose(m -> m.callAsync(inputs));
  }

  /**
   * Get the method named {@code methodName} on this Object, if it exists.
   *
   * @param methodName the name of the method.
   * @return a {@link UaMethod} for the method named {@code methodName}.
   * @throws UaException if an operation- or service-level error occurs or a method named {@code
   *     methodName} could not be found.
   */
  public UaMethod getMethod(String methodName) throws UaException {
    UShort namespaceIndex = getNodeId().getNamespaceIndex();

    return getMethod(new QualifiedName(namespaceIndex, methodName));
  }

  /**
   * Get the method named {@code methodName} on this Object, if it exists.
   *
   * @param methodName the name of the method.
   * @return a {@link UaMethod} for the method named {@code methodName}.
   * @throws UaException if an operation- or service-level error occurs or a method named {@code
   *     methodName} could not be found.
   */
  public UaMethod getMethod(QualifiedName methodName) throws UaException {
    try {
      return getMethodAsync(methodName).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get the method named {@code methodName} on this Object, if it exists.
   *
   * <p>This call completes asynchronously.
   *
   * @param methodName the name of the method.
   * @return a {@link CompletableFuture} that completes successfully with a {@link UaMethod} or
   *     completes exceptionally if an operation- or service-level error occurs or if a method named
   *     {@code methodName} could not be found.
   */
  public CompletableFuture<UaMethod> getMethodAsync(String methodName) {
    UShort namespaceIndex = getNodeId().getNamespaceIndex();

    return getMethodAsync(new QualifiedName(namespaceIndex, methodName));
  }

  /**
   * Get the method named {@code methodName} on this Object, if it exists.
   *
   * <p>This call completes asynchronously.
   *
   * @param methodName the name of the method.
   * @return a {@link CompletableFuture} that completes successfully with a {@link UaMethod} or
   *     completes exceptionally if an operation- or service-level error occurs or if a method named
   *     {@code methodName} could not be found.
   */
  public CompletableFuture<UaMethod> getMethodAsync(QualifiedName methodName) {
    UInteger nodeClassMask = uint(NodeClass.Method.getValue());
    UInteger resultMask = uint(BrowseResultMask.All.getValue());

    CompletableFuture<BrowseResult> future =
        client.browseAsync(
            new BrowseDescription(
                getNodeId(),
                BrowseDirection.Forward,
                NodeIds.HasComponent,
                true,
                nodeClassMask,
                resultMask));

    return future.thenCompose(
        result -> {
          StatusCode statusCode = result.getStatusCode();

          if (statusCode != null && statusCode.isGood()) {
            ReferenceDescription[] references =
                requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

            Optional<ExpandedNodeId> methodNodeId =
                Stream.of(references)
                    .filter(rd -> Objects.equals(methodName, rd.getBrowseName()))
                    .findFirst()
                    .map(ReferenceDescription::getNodeId);

            return methodNodeId
                .map(
                    xni -> {
                      AddressSpace addressSpace = client.getAddressSpace();

                      return addressSpace
                          .toNodeIdAsync(xni)
                          .thenCompose(
                              nodeId ->
                                  addressSpace
                                      .getNodeAsync(nodeId)
                                      .thenCompose(
                                          node -> {
                                            UaMethodNode methodNode = (UaMethodNode) node;

                                            CompletableFuture<Argument[]> inputArguments =
                                                methodNode
                                                    .readInputArgumentsAsync()
                                                    .exceptionally(t -> new Argument[0]);

                                            CompletableFuture<Argument[]> outputArguments =
                                                methodNode
                                                    .readOutputArgumentsAsync()
                                                    .exceptionally(t -> new Argument[0]);

                                            return inputArguments.thenCombine(
                                                outputArguments,
                                                (in, out) ->
                                                    new UaMethod(
                                                        client, this, methodNode, in, out));
                                          }));
                    })
                .orElse(
                    failedFuture(
                        new UaException(
                            StatusCodes.Bad_NotFound, "method not found: " + methodName)));
          } else {
            return failedFuture(new UaException(statusCode, "browsing for MethodNodes failed"));
          }
        });
  }

  /**
   * Get the {@link UaObjectNode} referenced by a HasComponent reference type with a browse name of
   * {@code name}, if it exists.
   *
   * @param name the browse name of the {@link UaObjectNode} component.
   * @return the {@link UaObjectNode} component, if it exists.
   * @throws UaException if the node could not be found or an operation- or service-level error
   *     occurs.
   */
  public UaObjectNode getObjectComponent(String name) throws UaException {
    try {
      return getObjectComponentAsync(name).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get the {@link UaObjectNode} referenced by a HasComponent reference type with a browse name of
   * {@code name}, if it exists.
   *
   * @param namespaceUri the namespace URI the browse name belongs to.
   * @param name the browse name of the {@link UaObjectNode} component.
   * @return the {@link UaObjectNode} component, if it exists.
   * @throws UaException if the node could not be found or an operation- or service-level error
   *     occurs.
   */
  public UaObjectNode getObjectComponent(String namespaceUri, String name) throws UaException {
    try {
      return getObjectComponentAsync(namespaceUri, name).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get the {@link UaObjectNode} referenced by a HasComponent reference type with a browse name of
   * {@code name}, if it exists.
   *
   * @param browseName the browse name of the {@link UaObjectNode} component.
   * @return the {@link UaObjectNode} component, if it exists.
   * @throws UaException if the node could not be found or an operation- or service-level error
   *     occurs.
   */
  public UaObjectNode getObjectComponent(QualifiedName browseName) throws UaException {
    try {
      return getObjectComponentAsync(browseName).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  public CompletableFuture<? extends UaObjectNode> getObjectComponentAsync(String name) {
    return getObjectComponentAsync(new QualifiedName(getNodeId().getNamespaceIndex(), name));
  }

  public CompletableFuture<? extends UaObjectNode> getObjectComponentAsync(
      String namespaceUri, String name) {
    UShort namespaceIndex = client.getNamespaceTable().getIndex(namespaceUri);

    if (namespaceIndex != null) {
      return getObjectComponentAsync(new QualifiedName(namespaceIndex, name));
    } else {
      return failedUaFuture(StatusCodes.Bad_NotFound);
    }
  }

  public CompletableFuture<? extends UaObjectNode> getObjectComponentAsync(
      QualifiedName browseName) {
    return getComponentAsync(browseName, NodeClass.Object).thenApply(UaObjectNode.class::cast);
  }

  /**
   * Get the {@link UaVariableNode} referenced by a HasComponent reference type with a browse name
   * of {@code name}, if it exists.
   *
   * @param name the browse name of the {@link UaVariableNode} component.
   * @return the {@link UaVariableNode} component, if it exists.
   * @throws UaException if the node could not be found or an operation- or service-level error
   *     occurs.
   */
  public UaVariableNode getVariableComponent(String name) throws UaException {
    try {
      return getVariableComponentAsync(name).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get the {@link UaVariableNode} referenced by a HasComponent reference type with a browse name
   * of {@code name}, if it exists.
   *
   * @param namespaceUri the namespace URI the browse name belongs to.
   * @param name the browse name of the {@link UaVariableNode} component.
   * @return the {@link UaVariableNode} component, if it exists.
   * @throws UaException if the node could not be found or an operation- or service-level error
   *     occurs.
   */
  public UaVariableNode getVariableComponent(String namespaceUri, String name) throws UaException {
    try {
      return getVariableComponentAsync(namespaceUri, name).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Get the {@link UaVariableNode} referenced by a HasComponent reference type with a browse name
   * of {@code name}, if it exists.
   *
   * @param browseName the browse name of the {@link UaVariableNode} component.
   * @return the {@link UaVariableNode} component, if it exists.
   * @throws UaException if the node could not be found or an operation- or service-level error
   *     occurs.
   */
  public UaVariableNode getVariableComponent(QualifiedName browseName) throws UaException {
    try {
      return getVariableComponentAsync(browseName).get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  public CompletableFuture<? extends UaVariableNode> getVariableComponentAsync(String name) {
    return getVariableComponentAsync(new QualifiedName(getNodeId().getNamespaceIndex(), name));
  }

  public CompletableFuture<? extends UaVariableNode> getVariableComponentAsync(
      String namespaceUri, String name) {
    UShort namespaceIndex = client.getNamespaceTable().getIndex(namespaceUri);

    if (namespaceIndex != null) {
      return getVariableComponentAsync(new QualifiedName(namespaceIndex, name));
    } else {
      return failedUaFuture(StatusCodes.Bad_NotFound);
    }
  }

  public CompletableFuture<? extends UaVariableNode> getVariableComponentAsync(
      QualifiedName browseName) {
    return getComponentAsync(browseName, NodeClass.Variable).thenApply(UaVariableNode.class::cast);
  }

  public UaObjectTypeNode getTypeDefinition() throws UaException {
    try {
      return getTypeDefinitionAsync().get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  public CompletableFuture<? extends UaObjectTypeNode> getTypeDefinitionAsync() {
    UInteger nodeClassMask = uint(NodeClass.ObjectType.getValue());
    UInteger resultMask = uint(BrowseResultMask.All.getValue());

    CompletableFuture<BrowseResult> future =
        client.browseAsync(
            new BrowseDescription(
                getNodeId(),
                BrowseDirection.Forward,
                NodeIds.HasTypeDefinition,
                false,
                nodeClassMask,
                resultMask));

    return future.thenCompose(
        result -> {
          ReferenceDescription[] references =
              requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

          Optional<CompletableFuture<UaObjectTypeNode>> node =
              Stream.of(references)
                  .flatMap(
                      r -> {
                        Optional<CompletableFuture<UaObjectTypeNode>> opt =
                            r.getNodeId()
                                .toNodeId(client.getNamespaceTable())
                                .map(
                                    id ->
                                        client
                                            .getAddressSpace()
                                            .getNodeAsync(id)
                                            .thenApply(n -> (UaObjectTypeNode) n));

                        return opt.stream();
                      })
                  .findFirst();

          return node.orElse(FutureUtils.failedUaFuture(StatusCodes.Bad_NotFound));
        });
  }

  /**
   * Get the value of the {@link ObjectNodeProperties#NodeVersion} Property, if it exists.
   *
   * @return the value of the NodeVersion Property, if it exists.
   * @see ObjectNodeProperties
   */
  public CompletableFuture<String> getNodeVersionAsync() {
    return getProperty(ObjectNodeProperties.NodeVersion);
  }

  /**
   * Get the value of the {@link ObjectNodeProperties#Icon} Property, if it exists.
   *
   * @return the value of the Icon Property, if it exists.
   * @see ObjectNodeProperties
   */
  public CompletableFuture<ByteString> getIconAsync() {
    return getProperty(ObjectNodeProperties.Icon);
  }

  /**
   * Get the value of the {@link ObjectNodeProperties#NamingRule} Property, if it exists.
   *
   * @return the value of the NamingRule Property, if it exists.
   * @see ObjectNodeProperties
   */
  public CompletableFuture<NamingRuleType> getNamingRuleAsync() {
    return getProperty(ObjectNodeProperties.NamingRule);
  }

  /**
   * Set the value of the {@link ObjectNodeProperties#NodeVersion} Property, if it exists.
   *
   * @param nodeVersion the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ObjectNodeProperties
   */
  public CompletableFuture<StatusCode> setNodeVersionAsync(String nodeVersion) {
    return setProperty(ObjectNodeProperties.NodeVersion, nodeVersion);
  }

  /**
   * Set the value of the {@link ObjectNodeProperties#Icon} Property, if it exists.
   *
   * @param icon the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ObjectNodeProperties
   */
  public CompletableFuture<StatusCode> setIconAsync(ByteString icon) {
    return setProperty(ObjectNodeProperties.Icon, icon);
  }

  /**
   * Set the value of the {@link ObjectNodeProperties#NamingRule} Property, if it exists.
   *
   * @param namingRule the value to set.
   * @return a {@link CompletableFuture} that completes with the {@link StatusCode} of the write
   *     operation.
   * @see ObjectNodeProperties
   */
  public CompletableFuture<StatusCode> setNamingRuleAsync(NamingRuleType namingRule) {
    return setProperty(ObjectNodeProperties.NamingRule, namingRule);
  }

  @Override
  protected DataValue getAttributeValue(AttributeId attributeId) {
    return switch (attributeId) {
      case EventNotifier -> DataValue.valueOnly(new Variant(getEventNotifier()));
      default -> super.getAttributeValue(attributeId);
    };
  }

  @Override
  protected void setAttributeValue(AttributeId attributeId, DataValue value) {
    switch (attributeId) {
      case EventNotifier:
        {
          setEventNotifier((UByte) value.value().value());
          break;
        }
      default:
        {
          super.setAttributeValue(attributeId, value);
        }
    }
  }
}
