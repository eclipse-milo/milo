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
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A composite {@link IdentityProvider} that tries its component {@link IdentityProvider}s in the
 * order provided.
 *
 * <p>The same order is used for CreateSession additional-header negotiation and for ActivateSession
 * token creation. A provider that cannot use the selected endpoint should throw, allowing the
 * composite to try the next provider. A provider that can use the endpoint but does not need
 * additional-header negotiation should return {@code null} or an empty optional.
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
  public Optional<SecurityPolicy> getUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    Iterator<IdentityProvider> iterator = providers.iterator();

    while (iterator.hasNext()) {
      IdentityProvider provider = iterator.next();

      try {
        return provider.getUserTokenSecurityPolicy(endpoint);
      } catch (Exception e) {
        if (!iterator.hasNext()) {
          throw e;
        }

        logger.debug("IdentityProvider={} failed, trying next...", provider.toString());
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<SecurityPolicy> getEccUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    Iterator<IdentityProvider> iterator = providers.iterator();

    while (iterator.hasNext()) {
      IdentityProvider provider = iterator.next();

      try {
        return provider.getEccUserTokenSecurityPolicy(endpoint);
      } catch (Exception e) {
        if (!iterator.hasNext()) {
          throw e;
        }

        logger.debug("IdentityProvider={} failed, trying next...", provider.toString());
      }
    }

    return Optional.empty();
  }

  @Override
  public ExtensionObject getCreateSessionAdditionalHeader(
      EncodingContext context, EndpointDescription endpoint) throws Exception {

    Iterator<IdentityProvider> iterator = providers.iterator();

    while (iterator.hasNext()) {
      IdentityProvider provider = iterator.next();

      try {
        return provider.getCreateSessionAdditionalHeader(context, endpoint);
      } catch (Exception e) {
        if (!iterator.hasNext()) {
          throw e;
        }

        logger.debug("IdentityProvider={} failed, trying next...", provider.toString());
      }
    }

    return null;
  }

  @Override
  public SignedIdentityToken getIdentityToken(IdentityProviderContext context) throws Exception {
    Iterator<IdentityProvider> iterator = providers.iterator();

    while (iterator.hasNext()) {
      IdentityProvider provider = iterator.next();

      try {
        return provider.getIdentityToken(context);
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
