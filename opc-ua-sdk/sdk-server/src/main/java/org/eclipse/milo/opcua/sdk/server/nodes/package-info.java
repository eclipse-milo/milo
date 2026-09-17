/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Mutable server nodes used by generated information models and application address spaces.
 *
 * <p>A node owns its attributes and attribute filters; its NodeManager owns registration and
 * reference storage. Generated model nodes add typed accessors over these primitives. Applications
 * can extend the node classes or install filters and method handlers to provide runtime behavior.
 *
 * <p>Method handlers may belong to a separate lifecycle owner, such as a Condition wrapper. Such
 * owners should release their handler with {@link
 * org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode#compareAndSetInvocationHandler} so cleanup
 * cannot remove a handler installed later. A call that already obtained the old handler may finish
 * after replacement; changing the handler controls subsequent dispatch.
 *
 * <p>An Object can also hold its own handler for one of its Method nodes, set with {@link
 * org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode#setMethodHandler(NodeId,
 * MethodInvocationHandler)}. Call dispatch resolves the Method through the ObjectId's ownership
 * rules first, then uses the Object's handler for that Method node and falls back to the Method
 * node's handler. The two are independent: replacing one never touches the other. Handler lifetime
 * is node lifetime.
 *
 * <p>Object and ObjectType Method lookup follows forward HasComponent references and their
 * subtypes, including HasOrderedComponent, as recorded in the server's ReferenceTypeTree. A
 * namespace that adds ReferenceTypes after that tree was first built must call {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServer#updateReferenceTypeTree()}, the same rule Browse
 * already imposes. The same lookup resolves Method declarations on an object's type hierarchy.
 * Organizes references provide navigation and do not establish invocation ownership.
 */
package org.eclipse.milo.opcua.sdk.server.nodes;
