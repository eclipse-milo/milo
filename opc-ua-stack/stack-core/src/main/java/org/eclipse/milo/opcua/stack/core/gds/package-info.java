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
 * Generated model for the OPC UA Global Discovery Server namespace, {@code
 * http://opcfoundation.org/UA/GDS/} (OPC 10000-12).
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.gds.GdsNodeIds} holds one {@link
 * org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId} constant per node in the GDS
 * NodeSet, expressed against the namespace URI rather than an index. The GDS namespace index
 * differs from server to server, so a constant must be resolved through the {@link
 * org.eclipse.milo.opcua.stack.core.NamespaceTable} of the peer it will be used with, for example
 * {@code GdsNodeIds.Directory.toNodeIdOrThrow(client.getNamespaceTable())}.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.gds.DataTypeInitializer} registers the codec for the
 * one structure the namespace defines, {@link
 * org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType}, with a {@link
 * org.eclipse.milo.opcua.stack.core.types.DataTypeManager}. It is not part of the namespace 0
 * startup path and must be invoked explicitly, with a {@link
 * org.eclipse.milo.opcua.stack.core.NamespaceTable} that already contains the GDS namespace URI. On
 * the client side {@code org.eclipse.milo.opcua.sdk.client.gds.GdsClient} (module {@code
 * milo-sdk-client-gds}) does this during creation; on the server side an application hosting the
 * GDS namespace calls it after adding the URI to the server's table.
 *
 * <h2>Regeneration</h2>
 *
 * <p>Every class in this package and in {@code org.eclipse.milo.opcua.stack.core.gds.types} is
 * generated and must not be edited by hand. The source is the {@code gds-model-core} module of <a
 * href="https://github.com/kevinherron/opc-ua-gds-model">opc-ua-gds-model</a> at commit {@code
 * 297d894} (GDS NodeSet2 1.05.07 on a 1.05.07 base model), copied in with the package prefix {@code
 * com.digitalpetri.opcua.gds} rewritten to {@code org.eclipse.milo.opcua.stack.core.gds} and the
 * Eclipse Milo license header prepended. To pick up a newer NodeSet, regenerate in that repository
 * first, then recopy; the procedure is documented in {@code docs/features/gds-client.md}.
 */
package org.eclipse.milo.opcua.stack.core.gds;
