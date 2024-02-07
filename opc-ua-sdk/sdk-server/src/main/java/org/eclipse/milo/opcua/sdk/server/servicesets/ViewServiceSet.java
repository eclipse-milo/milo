/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterNodesResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.UnregisterNodesResponse;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;

public interface ViewServiceSet {

    CompletableFuture<BrowseResponse> onBrowse(ServiceRequestContext context, BrowseRequest request);

    CompletableFuture<BrowseNextResponse> onBrowseNext(ServiceRequestContext context, BrowseNextRequest request);

    CompletableFuture<TranslateBrowsePathsToNodeIdsResponse> onTranslateBrowsePaths(
        ServiceRequestContext context,
        TranslateBrowsePathsToNodeIdsRequest request
    );

    CompletableFuture<RegisterNodesResponse> onRegisterNodes(
        ServiceRequestContext context,
        RegisterNodesRequest request
    );

    CompletableFuture<UnregisterNodesResponse> onUnregisterNodes(
        ServiceRequestContext context,
        UnregisterNodesRequest request
    );

}