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
 * Client support for the OPC UA Global Discovery Server (GDS, OPC 10000-12): application
 * registration, certificate requests through the Pull Model, and trust list retrieval.
 *
 * <p>GDS client support uses two modules. {@code milo-sdk-client} contributes the generated GDS
 * model: {@link org.eclipse.milo.opcua.sdk.client.gds.model.ObjectTypeInitializer}, {@link
 * org.eclipse.milo.opcua.sdk.client.gds.model.VariableTypeInitializer}, and the typed node classes
 * in {@code org.eclipse.milo.opcua.sdk.client.gds.model.objects}. {@code milo-sdk-client-gds}
 * contributes the hand-written layer described here, which an application opts into by adding that
 * module as a dependency.
 *
 * <h2>Entry points</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.client.gds.GdsClient} wraps a connected {@link
 * org.eclipse.milo.opcua.sdk.client.OpcUaClient}. Creating it resolves the GDS namespace index,
 * registers the {@code ApplicationRecordDataType} codec and the GDS ObjectTypes with the client,
 * and locates the {@code Directory} object. It then exposes every {@code DirectoryType} and {@code
 * CertificateDirectoryType} method as a typed Java method with a blocking and an asynchronous form,
 * plus two reads every workflow needs next: the {@code CertificateTypes} of a CertificateGroup and
 * the {@code LastUpdateTime} and {@code UpdateFrequency} of a TrustList.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.client.gds.TrustListReader} reads a TrustList object with
 * the FileType Open, Read, and Close methods and decodes the body into a {@link
 * org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType}. {@link
 * org.eclipse.milo.opcua.sdk.client.gds.TrustListApplier} installs that structure into a {@link
 * org.eclipse.milo.opcua.stack.core.security.TrustListManager}, replacing each list the structure
 * marks as specified through one {@code TrustListManager.update} call, and can build the structure
 * back from one manager snapshot.
 *
 * <h2>Data flow</h2>
 *
 * <p>The Pull Model (Part 12 §7.6) runs against the {@code Directory} object: find or register the
 * application record, list the certificate groups the GDS manages for it, resolve the
 * CertificateTypeId to request for each desired certificate type with {@link
 * org.eclipse.milo.opcua.sdk.client.gds.GdsClient#resolveCertificateTypeId}, ask whether an update
 * is required, submit a signing request, and poll {@code FinishRequest} until it stops failing with
 * {@code Bad_NothingToDo}. The group's TrustList is read whenever its {@code LastUpdateTime} moves
 * past the time the application last applied it, and is applied to the {@code TrustListManager} of
 * the matching local certificate group.
 *
 * <h2>Boundaries</h2>
 *
 * <p>The package is protocol code only. It makes no trust decisions and holds no state beyond the
 * namespace index: the application chooses the identity and {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateValidator} of the client that connects to
 * the GDS, decides when to run each step and how to handle rejections, stores the {@code
 * ApplicationId} and pending {@code RequestId}s across restarts, verifies issued certificates
 * against its own key pairs, and decides which {@code TrustListManager} receives a pulled list.
 * Method results are returned as the GDS reports them; a Bad status is thrown as a {@link
 * org.eclipse.milo.opcua.stack.core.UaException} carrying the original code and is never retried
 * here. Certificate type resolution is local and reports {@code Bad_NotSupported} when the
 * advertised types are incompatible with the desired type.
 *
 * <p>Nothing in this package runs during namespace 0 startup. The GDS namespace index is only known
 * once a client has read a server's namespace array, so the model registrations happen in {@code
 * GdsClient.create} against that client's live {@link
 * org.eclipse.milo.opcua.stack.core.NamespaceTable}.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.client.gds;

import org.jspecify.annotations.NullMarked;
