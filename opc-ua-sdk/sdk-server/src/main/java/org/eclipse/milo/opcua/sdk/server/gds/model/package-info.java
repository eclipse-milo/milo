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
 * Generated server-side model of the OPC UA Global Discovery Server namespace.
 *
 * <p>This package gives a server that hosts the GDS namespace ({@code
 * http://opcfoundation.org/UA/GDS/}, OPC 10000-12) typed node classes for its ObjectTypes; the
 * classes themselves live in {@code org.eclipse.milo.opcua.sdk.server.gds.model.objects}. Milo does
 * not implement a GDS: the Directory methods, application registry, certificate authority, and
 * trust list storage are left to the hosting application. Nothing in the server SDK invokes this
 * package on its own.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.gds.model.ObjectTypeInitializer} registers the node
 * classes with the server's {@link org.eclipse.milo.opcua.sdk.server.ObjectTypeManager}. It
 * resolves the GDS namespace index through the server's {@link
 * org.eclipse.milo.opcua.stack.core.NamespaceTable}, so the hosting application must add the GDS
 * namespace URI to the table before calling it, and the call is not part of the namespace 0 startup
 * path. {@link org.eclipse.milo.opcua.sdk.server.gds.model.VariableTypeInitializer} is empty
 * because the GDS namespace defines no VariableTypes. The {@code ApplicationRecordDataType} codec
 * is registered separately with {@link org.eclipse.milo.opcua.stack.core.gds.DataTypeInitializer}.
 *
 * <h2>Regeneration</h2>
 *
 * <p>Every class in this package and its {@code objects} subpackage is generated and must not be
 * edited by hand. The source is the {@code gds-model-server} module of <a
 * href="https://github.com/kevinherron/opc-ua-gds-model">opc-ua-gds-model</a> at commit {@code
 * efa229d} (GDS NodeSet2 1.05.07), copied in with {@code com.digitalpetri.opcua.gds.server}
 * rewritten to this package and {@code com.digitalpetri.opcua.gds.server.objects} rewritten to
 * {@code org.eclipse.milo.opcua.sdk.server.gds.model.objects}. See {@code
 * docs/features/gds-client.md} for the procedure.
 */
package org.eclipse.milo.opcua.sdk.server.gds.model;
