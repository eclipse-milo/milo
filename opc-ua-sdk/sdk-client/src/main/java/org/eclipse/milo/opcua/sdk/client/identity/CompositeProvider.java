/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import java.util.Iterator;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A composite {@link IdentityProvider} that tries its component {@link IdentityProvider}s in the
 * order provided.
 */
public class CompositeProvider implements IdentityProvider {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<IdentityProvider> providers;

  public CompositeProvider(IdentityProvider... providers) {
    this.providers = List.of(providers);
  }

  public CompositeProvider(List<IdentityProvider> providers) {
    this.providers = List.copyOf(providers);
  }

  @Override
  public SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception {

    Iterator<IdentityProvider> iterator = providers.iterator();

    while (iterator.hasNext()) {
      IdentityProvider provider = iterator.next();

      try {
        return provider.getIdentityToken(endpoint, serverNonce);
      } catch (Exception e) {
        if (!iterator.hasNext()) {
          throw e;
        }

        logger.debug("IdentityProvider={} failed, trying next...", provider.toString());
      }
    }

    throw new Exception("no sufficient UserTokenPolicy found");
  }

  @Override
  public String toString() {
    return "CompositeProvider{" + "providers=" + providers + '}';
  }

  public static CompositeProvider of(IdentityProvider... providers) {
    return new CompositeProvider(providers);
  }
}
