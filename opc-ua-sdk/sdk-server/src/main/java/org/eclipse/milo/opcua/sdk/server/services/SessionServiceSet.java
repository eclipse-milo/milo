/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.services;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CancelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CloseSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public interface SessionServiceSet {

    CompletableFuture<CreateSessionResponse> onCreateSession(
        ServiceRequestContext context,
        CreateSessionRequest request
    );

    CompletableFuture<ActivateSessionResponse> onActivateSession(
        ServiceRequestContext context,
        ActivateSessionRequest request
    );

    CompletableFuture<CloseSessionResponse> onCloseSession(
        ServiceRequestContext context,
        CloseSessionRequest request
    );

    CompletableFuture<CancelResponse> onCancel(ServiceRequestContext context, CancelRequest request);

}