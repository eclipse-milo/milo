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
 * Generated client-side model of the OPC UA Global Discovery Server ObjectTypes.
 *
 * <p>Each GDS ObjectType (OPC 10000-12) is represented by an interface and a {@code *Node}
 * implementation, mirroring the namespace 0 model in {@code
 * org.eclipse.milo.opcua.sdk.client.model.objects}: {@link
 * org.eclipse.milo.opcua.sdk.client.gds.model.objects.DirectoryType} and {@link
 * org.eclipse.milo.opcua.sdk.client.gds.model.objects.CertificateDirectoryType} for the {@code
 * Directory} object, the KeyCredential and AuthorizationService folder and service types, and the
 * GDS audit event types. The node classes navigate components and properties; they do not wrap the
 * Directory methods. Typed method calls are provided by {@code
 * org.eclipse.milo.opcua.sdk.client.gds.GdsClient} in the {@code milo-sdk-client-gds} module.
 *
 * <p>Instances of these classes are produced by {@link
 * org.eclipse.milo.opcua.sdk.client.AddressSpace} once the types are registered with the client's
 * {@link org.eclipse.milo.opcua.sdk.client.ObjectTypeManager} by {@link
 * org.eclipse.milo.opcua.sdk.client.gds.model.ObjectTypeInitializer}. That initializer resolves the
 * GDS namespace index through the client's live {@link
 * org.eclipse.milo.opcua.stack.core.NamespaceTable}, so it can only run against a connected client
 * and is not part of the namespace 0 startup path.
 *
 * <h2>Regeneration</h2>
 *
 * <p>Every class in this package, together with {@link
 * org.eclipse.milo.opcua.sdk.client.gds.model.ObjectTypeInitializer} and {@link
 * org.eclipse.milo.opcua.sdk.client.gds.model.VariableTypeInitializer}, is generated and must not
 * be edited by hand. The source is the {@code gds-model-client} module of <a
 * href="https://github.com/kevinherron/opc-ua-gds-model">opc-ua-gds-model</a> at commit {@code
 * 297d894} (GDS NodeSet2 1.05.07), copied in with {@code com.digitalpetri.opcua.gds.client}
 * rewritten to {@code org.eclipse.milo.opcua.sdk.client.gds.model} and {@code
 * com.digitalpetri.opcua.gds.client.objects} rewritten to this package. See {@code
 * docs/features/gds-client.md} for the procedure.
 */
package org.eclipse.milo.opcua.sdk.client.gds.model.objects;
