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
 * <p>Object and ObjectType Method lookup follows forward HasComponent references and their
 * registered subtypes, including HasOrderedComponent. The same rule resolves Method declarations on
 * an object's type hierarchy. Organizes references provide navigation and do not establish
 * invocation ownership. Update the server's reference type tree after adding reference types.
 */
package org.eclipse.milo.opcua.sdk.server.nodes;
