/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;

public class PendingBrowse implements Pending<BrowseDescription, BrowseResult> {

  private final CompletableFuture<BrowseResult> future = new CompletableFuture<>();

  private final BrowseDescription browseDescription;

  public PendingBrowse(BrowseDescription browseDescription) {
    this.browseDescription = browseDescription;
  }

  @Override
  public CompletableFuture<BrowseResult> getFuture() {
    return future;
  }

  @Override
  public BrowseDescription getInput() {
    return browseDescription;
  }
}
