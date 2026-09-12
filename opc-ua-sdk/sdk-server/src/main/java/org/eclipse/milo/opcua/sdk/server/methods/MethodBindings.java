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
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An application-owned lifetime for ObjectId-specific handlers on shared Method nodes.
 *
 * <p>Pass one registry to bindings across namespaces on the same server. Bind replaces only the
 * selected object's registration. Other objects use their registration or the exact handler
 * captured before installation. A competing registry cannot wrap this registry's dispatcher.
 *
 * <p>Close tokens or call {@link #removeObject(NodeId)} before removing owner nodes. Close the
 * registry during namespace shutdown. Cleanup is idempotent and does not drain callbacks already
 * selected. Raw handler replacement is authoritative. After ownership loss on any managed Method is
 * observed, the registry refuses further binds; existing registrations on unaffected Methods remain
 * callable.
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
  private final Map<UaMethodNode, Dispatcher> dispatchers = new IdentityHashMap<>();
  private @Nullable OpcUaServer server;
  private boolean closed;
  private boolean displaced;

  /** Create an unattached registry; the first successful bind selects its server. */
  public MethodBindings() {}

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
   * @throws IllegalStateException if closed, displaced, attached to another server, or another
   *     registry owns the Method's dispatcher.
   */
  public MethodBinding bind(
      UaNode invocationOwner, UaMethodNode method, MethodInvocationHandler validatedHandler)
      throws UaException {
    requireNonNull(invocationOwner);
    requireNonNull(method);
    requireNonNull(validatedHandler);
    OpcUaServer selectedServer = invocationOwner.getNodeContext().getServer();
    if (method.getNodeContext().getServer() != selectedServer) {
      throw new IllegalArgumentException("Owner and Method belong to different servers");
    }
    validateOwnership(selectedServer, invocationOwner, method);
    validateMetadata(method, validatedHandler);

    synchronized (this) {
      if (closed) throw new IllegalStateException("Method bindings are closed");
      if (displaced) throw new IllegalStateException("A Method dispatcher was replaced externally");
      if (server != null && server != selectedServer) {
        throw new IllegalStateException("Method bindings belong to another server");
      }
      for (Dispatcher existing : dispatchers.values()) {
        existing.ensureOwnership();
      }
      Dispatcher dispatcher = dispatchers.get(method);
      if (dispatcher == null) {
        while (true) {
          MethodInvocationHandler previous = method.getInvocationHandler();
          if (previous instanceof Dispatcher) {
            throw new IllegalStateException("Another registry owns this Method");
          }
          Dispatcher candidate = new Dispatcher(this, method, previous);
          if (method.compareAndSetInvocationHandler(previous, candidate)) {
            dispatcher = candidate;
            dispatchers.put(method, dispatcher);
            break;
          }
        }
      } else {
        dispatcher.ensureOwnership();
        if (!dispatcher.installed) {
          if (!method.compareAndSetInvocationHandler(dispatcher.fallback, dispatcher)) {
            dispatcher.displaced = true;
            displaced = true;
            throw new IllegalStateException("Method dispatcher was replaced externally");
          }
          dispatcher.installed = true;
        }
      }
      server = selectedServer;
      Registration registration =
          new Registration(dispatcher, invocationOwner.getNodeId(), validatedHandler);
      dispatcher.registrations.put(invocationOwner.getNodeId(), registration);
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
    for (Dispatcher dispatcher : dispatchers.values()) {
      dispatcher.registrations.remove(objectId);
      dispatcher.releaseIfEmpty();
    }
  }

  /**
   * Release all registrations and restore captured fallbacks only while still owning each Method.
   */
  @Override
  public synchronized void close() {
    if (closed) return;
    closed = true;
    for (Dispatcher dispatcher : dispatchers.values()) {
      dispatcher.registrations.clear();
      dispatcher.releaseIfEmpty();
    }
    dispatchers.clear();
  }

  private static void validateOwnership(OpcUaServer server, UaNode owner, UaMethodNode method)
      throws UaException {
    if (server.getAddressSpaceManager().getManagedNode(owner.getNodeId()).orElse(null) != owner
        || server.getAddressSpaceManager().getManagedNode(method.getNodeId()).orElse(null)
            != method) {
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

  private static void validateMetadata(UaMethodNode method, MethodInvocationHandler handler)
      throws UaException {
    if (handler instanceof AbstractMethodInvocationHandler typed) {
      if (typed.getNode() != method
          || !sameArguments(method.getInputArguments(), typed.getInputArguments())
          || !sameArguments(method.getOutputArguments(), typed.getOutputArguments())) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch, "Handler argument metadata does not match Method");
      }
    }
  }

  private static boolean sameArguments(
      Argument @Nullable [] actual, Argument @Nullable [] expected) {
    int actualLength = actual == null ? 0 : actual.length;
    int expectedLength = expected == null ? 0 : expected.length;
    if (actualLength != expectedLength) return false;
    for (int i = 0; i < actualLength; i++) {
      Argument left = actual[i];
      Argument right = expected[i];
      if (left == null
          || right == null
          || left.getDataType() == null
          || right.getDataType() == null
          || left.getValueRank() == null
          || right.getValueRank() == null
          || !Objects.equals(left.getName(), right.getName())
          || !Objects.equals(left.getDataType(), right.getDataType())
          || !Objects.equals(left.getValueRank(), right.getValueRank())
          || !sameDimensions(left.getArrayDimensions(), right.getArrayDimensions())) return false;
    }
    return true;
  }

  private static boolean sameDimensions(
      org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger @Nullable [] left,
      org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger @Nullable [] right) {
    if (left != null && Arrays.stream(left).anyMatch(Objects::isNull)) return false;
    if (right != null && Arrays.stream(right).anyMatch(Objects::isNull)) return false;
    int leftLength = left == null ? 0 : left.length;
    int rightLength = right == null ? 0 : right.length;
    return leftLength == 0 && rightLength == 0 || Arrays.equals(left, right);
  }

  private static final class Dispatcher implements MethodInvocationHandler {
    final MethodBindings registry;
    final UaMethodNode method;
    final MethodInvocationHandler fallback;
    final Map<NodeId, Registration> registrations = new HashMap<>();
    boolean installed = true;
    boolean displaced;

    Dispatcher(MethodBindings registry, UaMethodNode method, MethodInvocationHandler fallback) {
      this.registry = registry;
      this.method = method;
      this.fallback = fallback;
    }

    void ensureOwnership() {
      MethodInvocationHandler expected = installed ? this : fallback;
      if (displaced || method.getInvocationHandler() != expected) {
        displaced = true;
        registry.displaced = true;
        throw new IllegalStateException("Method dispatcher was replaced externally");
      }
    }

    void releaseIfEmpty() {
      if (registrations.isEmpty() && installed) {
        if (!method.compareAndSetInvocationHandler(this, fallback)) {
          displaced = true;
          registry.displaced = true;
        }
        installed = false;
      }
    }

    @Override
    public CallMethodResult invoke(AccessContext context, CallMethodRequest request) {
      MethodInvocationHandler selected;
      synchronized (registry) {
        Registration registration =
            registry.closed ? null : registrations.get(request.getObjectId());
        selected = registration == null ? fallback : registration.handler;
      }
      return selected.invoke(context, request);
    }
  }

  private static final class Registration implements MethodBinding {
    final Dispatcher dispatcher;
    final NodeId objectId;
    final MethodInvocationHandler handler;

    Registration(Dispatcher dispatcher, NodeId objectId, MethodInvocationHandler handler) {
      this.dispatcher = dispatcher;
      this.objectId = objectId;
      this.handler = handler;
    }

    @Override
    public NodeId objectId() {
      return objectId;
    }

    @Override
    public NodeId methodId() {
      return dispatcher.method.getNodeId();
    }

    @Override
    public void close() {
      synchronized (dispatcher.registry) {
        dispatcher.registrations.remove(objectId, this);
        dispatcher.releaseIfEmpty();
      }
    }
  }
}
