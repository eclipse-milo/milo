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
 * Built-in server namespaces initialized during {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServer} construction.
 *
 * <h2>Ownership</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.namespaces.OpcUaNamespace} owns namespace 0 ({@code
 * http://opcfoundation.org/UA/}). At startup it loads the standard OPC UA nodeset, wires live
 * server status and standard Methods, removes optional nodes that the SDK does not implement by
 * default (such as {@code RoleSet} and {@code OPCUANamespaceMetadata.DefaultRolePermissions} /
 * {@code DefaultUserRolePermissions}), and clears nodeset-loaded {@code RolePermissions} while
 * attaching a shared derived {@code UserRolePermissions} filter so both {@code RolePermissions} and
 * {@code UserRolePermissions} report {@code Bad_AttributeIdInvalid} by default, yet explicitly
 * configuring {@code RolePermissions} on any standard node derives session-filtered {@code
 * UserRolePermissions} and enforces {@code ReadRolePermissions} automatically (OPC 10000-3 §5.2.9,
 * §5.2.10).
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.namespaces.ServerNamespace} owns namespace 1 (the
 * server's application URI) and hosts server-managed fragments such as server diagnostics, shutting
 * them down in reverse registration order so child fragments can detach from live node managers
 * cleanly.
 *
 * <h2>Startup order</h2>
 *
 * <p>{@code OpcUaNamespace} starts before {@code ServerNamespace}, so server-owned fragments can
 * bind to and customize the standard namespace-0 nodes without racing nodeset initialization.
 */
package org.eclipse.milo.opcua.sdk.server.namespaces;
