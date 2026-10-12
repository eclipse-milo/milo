/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An application-owned lifetime for ObjectId-specific handlers on shared Method nodes.
 *
 * <p>Bind validates the owner's relationship to the Method and the handler's argument metadata,
 * then installs the handler on the Method for that ObjectId with {@link
 * UaMethodNode#setInvocationHandler(NodeId, MethodInvocationHandler)}. Bind replaces only the
 * selected object's registration. Objects without a registration use the Method's default handler,
 * which this registry never changes.
 *
 * <p>Close tokens or call {@link #removeObject(NodeId)} before removing owner nodes. Close the
 * registry during namespace shutdown. Cleanup is idempotent, releases only registrations this
 * registry still owns, and does not drain callbacks already selected.
 *
 * <pre>{@code
 * try (MethodBindings bindings = new MethodBindings()) {
 *   MethodBinding token = bindings.bind(owner, method, handler);
 *   // Close token before removing owner, or close bindings for the whole application lifetime.
 * }
 * }</pre>
 */
@NullMarked
public final class MethodBindings implements AutoCloseable {
  private final Map<Key, Registration> registrations = new HashMap<>();
  private boolean closed;

  /**
   * Bind a validated handler to this owner on a shared Method without changing argument Properties.
   *
   * <p>The owner and Method must be live nodes on the same server and have a callable component
   * relationship. Modelled instance Methods cannot be bound on an ObjectType. ConditionManager
   * handlers keep their precedence and cannot be bound through this registry. For an
   * AbstractMethodInvocationHandler, the node and argument data types, ranks, dimensions and names
   * must match the Method metadata. Raw handlers remain responsible for their own input validation.
   * Actual calls still pass through normal ownership and service access checks.
   *
   * @param invocationOwner the Object or ObjectType used as invocation ObjectId.
   * @param method the shared Method to dispatch.
   * @param validatedHandler the synchronous handler; no application callback runs during bind.
   * @return an identity-bearing lifetime token for this registration.
   * @throws UaException with Bad_NodeIdUnknown for removed nodes, Bad_MethodInvalid for invalid
   *     ownership, Bad_TypeMismatch for incompatible metadata, or Bad_NotSupported for a preempting
   *     ConditionManager handler.
   * @throws IllegalArgumentException if nodes belong to different servers.
   * @throws IllegalStateException if the registry is closed.
   */
  public MethodBinding bind(
      UaNode invocationOwner, UaMethodNode method, MethodInvocationHandler validatedHandler)
      throws UaException {
    requireNonNull(invocationOwner);
    requireNonNull(method);
    requireNonNull(validatedHandler);
    OpcUaServer server = invocationOwner.getNodeContext().getServer();
    if (method.getNodeContext().getServer() != server) {
      throw new IllegalArgumentException("Owner and Method belong to different servers");
    }
    validateOwnership(server, invocationOwner, method);
    validateMetadata(method, validatedHandler);

    synchronized (this) {
      if (closed) throw new IllegalStateException("Method bindings are closed");
      Registration registration =
          new Registration(this, invocationOwner.getNodeId(), method, validatedHandler);
      method.setInvocationHandler(registration.objectId, validatedHandler);
      registrations.put(registration.key(), registration);
      return registration;
    }
  }

  /**
   * Remove all registrations for this ObjectId, without removing its nodes or draining callbacks.
   *
   * @param objectId the owner whose registrations to release.
   */
  public synchronized void removeObject(NodeId objectId) {
    requireNonNull(objectId);
    registrations
        .values()
        .removeIf(
            registration -> {
              if (registration.objectId.equals(objectId)) {
                registration.release();
                return true;
              }
              return false;
            });
  }

  /** Release every registration this registry still owns. */
  @Override
  public synchronized void close() {
    if (closed) return;
    closed = true;
    registrations.values().forEach(Registration::release);
    registrations.clear();
  }

  private static void validateOwnership(OpcUaServer server, UaNode owner, UaMethodNode method)
      throws UaException {
    // Identity, not just NodeId presence: a node removed and recreated under the same NodeId is a
    // different instance and must be bound again through the new instance.
    if (isStale(server, owner) || isStale(server, method)) {
      throw new UaException(StatusCodes.Bad_NodeIdUnknown);
    }
    UaMethodNode owned;
    if (owner instanceof UaObjectNode object) {
      owned = object.findMethodNode(method.getNodeId());
    } else if (owner instanceof UaObjectTypeNode type) {
      if (method.getModellingRuleNode().isPresent()) {
        throw new UaException(
            StatusCodes.Bad_MethodInvalid,
            "An ObjectType cannot own an invocation of an instance declaration");
      }
      owned = type.findMethodNode(method.getNodeId());
    } else {
      throw new UaException(
          StatusCodes.Bad_MethodInvalid, "Method owner must be an Object or ObjectType");
    }
    if (server
        .getConditionManager()
        .findMethodInvocationHandler(owner.getNodeId(), method.getNodeId())
        .isPresent()) {
      throw new UaException(
          StatusCodes.Bad_NotSupported, "ConditionManager handles this Method relationship");
    }
    if (owned != method) {
      throw new UaException(StatusCodes.Bad_MethodInvalid);
    }
  }

  /** A node is stale once the address space no longer manages this exact instance. */
  private static boolean isStale(OpcUaServer server, UaNode node) {
    return server.getAddressSpaceManager().getManagedNode(node.getNodeId()).orElse(null) != node;
  }

  private static void validateMetadata(UaMethodNode method, MethodInvocationHandler handler)
      throws UaException {
    if (handler instanceof AbstractMethodInvocationHandler typed) {
      if (typed.getNode() != method
          || argumentsDiffer(method.getInputArguments(), typed.getInputArguments())
          || argumentsDiffer(method.getOutputArguments(), typed.getOutputArguments())) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch, "Handler argument metadata does not match Method");
      }
    }
  }

  private static boolean argumentsDiffer(
      Argument @Nullable [] actual, Argument @Nullable [] expected) {
    Argument[] left = actual == null ? new Argument[0] : actual;
    Argument[] right = expected == null ? new Argument[0] : expected;
    if (left.length != right.length) return true;
    for (int i = 0; i < left.length; i++) {
      if (!sameArgument(left[i], right[i])) return true;
    }
    return false;
  }

  /** Compare everything but Description; a null DataType or ValueRank never matches. */
  private static boolean sameArgument(@Nullable Argument left, @Nullable Argument right) {
    if (left == null || right == null) return false;
    if (left.getDataType() == null || left.getValueRank() == null) return false;
    return Objects.equals(left.getName(), right.getName())
        && left.getDataType().equals(right.getDataType())
        && left.getValueRank().equals(right.getValueRank())
        && sameDimensions(left.getArrayDimensions(), right.getArrayDimensions());
  }

  /** Null and empty both mean unspecified dimensions. */
  private static boolean sameDimensions(UInteger @Nullable [] left, UInteger @Nullable [] right) {
    return Arrays.equals(
        left == null ? new UInteger[0] : left, right == null ? new UInteger[0] : right);
  }

  private record Key(NodeId objectId, NodeId methodId) {}

  /**
   * Deliberately a class, not a record: tokens are compared by identity so that closing a stale
   * token never releases an equal-valued registration that replaced it.
   */
  @SuppressWarnings("ClassCanBeRecord")
  private static final class Registration implements MethodBinding {
    final MethodBindings registry;
    final NodeId objectId;
    final UaMethodNode method;
    final MethodInvocationHandler handler;

    Registration(
        MethodBindings registry,
        NodeId objectId,
        UaMethodNode method,
        MethodInvocationHandler handler) {
      this.registry = registry;
      this.objectId = objectId;
      this.method = method;
      this.handler = handler;
    }

    Key key() {
      return new Key(objectId, method.getNodeId());
    }

    /** Remove this handler from the Method unless a later registration already replaced it. */
    void release() {
      method.removeInvocationHandler(objectId, handler);
    }

    @Override
    public NodeId objectId() {
      return objectId;
    }

    @Override
    public NodeId methodId() {
      return method.getNodeId();
    }

    @Override
    public void close() {
      synchronized (registry) {
        if (registry.registrations.remove(key(), this)) {
          release();
        }
      }
    }
  }
}
