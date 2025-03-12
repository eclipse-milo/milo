/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.methods.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaServerNode;
import org.eclipse.milo.opcua.sdk.server.nodes.factories.NodeFactory;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ManagedAddressSpace implements AddressSpace {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UaNodeContext nodeContext;
  private final NodeFactory nodeFactory;

  private final OpcUaServer server;
  private final UaNodeManager nodeManager;

  public ManagedAddressSpace(OpcUaServer server) {
    this(server, new UaNodeManager());
  }

  public ManagedAddressSpace(OpcUaServer server, UaNodeManager nodeManager) {
    this.server = server;

    this.nodeManager = nodeManager;

    nodeContext =
        new UaNodeContext() {
          @Override
          public OpcUaServer getServer() {
            return ManagedAddressSpace.this.getServer();
          }

          @Override
          public NodeManager<UaNode> getNodeManager() {
            return ManagedAddressSpace.this.getNodeManager();
          }
        };

    nodeFactory = createNodeFactory();
  }

  protected NodeFactory createNodeFactory() {
    return new NodeFactory(nodeContext);
  }

  protected OpcUaServer getServer() {
    return server;
  }

  public UaNodeContext getNodeContext() {
    return nodeContext;
  }

  public NodeFactory getNodeFactory() {
    return nodeFactory;
  }

  public UaNodeManager getNodeManager() {
    return nodeManager;
  }

  @Override
  public List<ReferenceResult> browse(
      BrowseContext context, ViewDescription view, List<NodeId> nodeIds) {
    var results = new ArrayList<ReferenceResult>();

    for (NodeId nodeId : nodeIds) {
      if (nodeManager.containsNode(nodeId)) {
        List<Reference> references = nodeManager.getReferences(nodeId);

        logger.debug("Browsed {} references for {}", references.size(), nodeId);

        results.add(ReferenceResult.of(references));
      } else {
        results.add(ReferenceResult.unknown());
      }
    }

    return results;
  }

  @Override
  public ReferenceResult.ReferenceList gather(
      BrowseContext context, ViewDescription viewDescription, NodeId nodeId) {
    List<Reference> references = nodeManager.getReferences(nodeId);

    logger.debug("Gathered {} references for {}", references.size(), nodeId);

    return ReferenceResult.of(references);
  }

  @Override
  public List<DataValue> read(
      ReadContext context,
      Double maxAge,
      TimestampsToReturn timestamps,
      List<ReadValueId> readValueIds) {

    var results = new ArrayList<DataValue>(readValueIds.size());

    for (ReadValueId readValueId : readValueIds) {
      UaServerNode node = nodeManager.get(readValueId.getNodeId());

      if (node != null) {
        DataValue value =
            AttributeReader.readAttribute(
                context,
                node,
                readValueId.getAttributeId(),
                timestamps,
                readValueId.getIndexRange(),
                readValueId.getDataEncoding());

        logger.debug(
            "read: nodeId={}, attributeId={}, value={}",
            node.getNodeId(),
            AttributeId.from(readValueId.getAttributeId()).map(Object::toString).orElse("unknown"),
            value);

        results.add(value);
      } else {
        results.add(new DataValue(StatusCodes.Bad_NodeIdUnknown));
      }
    }

    return results;
  }

  @Override
  public List<StatusCode> write(WriteContext context, List<WriteValue> writeValues) {

    var results = new ArrayList<StatusCode>(writeValues.size());

    for (WriteValue writeValue : writeValues) {
      UaServerNode node = nodeManager.get(writeValue.getNodeId());

      if (node != null) {
        StatusCode result =
            AttributeWriter.writeAttribute(
                context,
                node,
                writeValue.getAttributeId(),
                writeValue.getValue(),
                writeValue.getIndexRange());

        results.add(result);

        logger.debug(
            "write: nodeId={}, attributeId={}, value={}, result={}",
            node.getNodeId(),
            AttributeId.from(writeValue.getAttributeId()).map(Object::toString).orElse("unknown"),
            writeValue.getValue().value(),
            result);
      } else {
        results.add(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
      }
    }

    return results;
  }

  /**
   * Invoke one or more methods belonging to this {@link AddressSpace}.
   *
   * @param context the {@link CallContext}.
   * @param requests The {@link CallMethodRequest}s for the methods to invoke.
   */
  @Override
  public List<CallMethodResult> call(CallContext context, List<CallMethodRequest> requests) {
    var results = new ArrayList<CallMethodResult>(requests.size());

    Semaphore semaphore = context.getSession().map(Session::getCallSemaphore).orElse(null);

    for (CallMethodRequest request : requests) {
      if (semaphore == null || semaphore.tryAcquire()) {
        try {
          MethodInvocationHandler handler =
              getInvocationHandler(request.getObjectId(), request.getMethodId());

          results.add(handler.invoke(context, request));
        } catch (UaException e) {
          results.add(
              new CallMethodResult(
                  e.getStatusCode(), new StatusCode[0], new DiagnosticInfo[0], new Variant[0]));
        } catch (Throwable t) {
          LoggerFactory.getLogger(getClass())
              .error(
                  "Uncaught Throwable invoking method handler for methodId={}.",
                  request.getMethodId(),
                  t);

          results.add(
              new CallMethodResult(
                  new StatusCode(StatusCodes.Bad_InternalError),
                  new StatusCode[0],
                  new DiagnosticInfo[0],
                  new Variant[0]));
        } finally {
          if (semaphore != null) {
            semaphore.release();
          }
        }
      } else {
        results.add(
            new CallMethodResult(
                new StatusCode(StatusCodes.Bad_ResourceUnavailable),
                new StatusCode[0],
                new DiagnosticInfo[0],
                new Variant[0]));
      }
    }

    return results;
  }

  /**
   * Get the {@link MethodInvocationHandler} for the method identified by {@code methodId}.
   *
   * @param objectId the {@link NodeId} identifying the object the method will be invoked on.
   * @param methodId the {@link NodeId} identifying the method.
   * @return the {@link MethodInvocationHandler} for {@code methodId}.
   * @throws UaException a {@link UaException} containing the appropriate operation result if either
   *     the object or method can't be found.
   */
  protected MethodInvocationHandler getInvocationHandler(NodeId objectId, NodeId methodId)
      throws UaException {
    UaNode node =
        nodeManager
            .getNode(objectId)
            .orElseThrow(() -> new UaException(StatusCodes.Bad_NodeIdUnknown));

    UaMethodNode methodNode = null;

    if (node instanceof UaObjectNode objectNode) {
      methodNode = objectNode.findMethodNode(methodId);
    } else if (node instanceof UaObjectTypeNode objectTypeNode) {
      methodNode = objectTypeNode.findMethodNode(methodId);
    }

    if (methodNode != null) {
      return methodNode.getInvocationHandler();
    } else {
      throw new UaException(StatusCodes.Bad_MethodInvalid);
    }
  }
}
