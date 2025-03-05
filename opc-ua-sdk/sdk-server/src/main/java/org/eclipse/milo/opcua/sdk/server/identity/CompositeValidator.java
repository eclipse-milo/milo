/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A composite {@link IdentityValidator} that tries its component {@link IdentityValidator}s in the
 * order provided.
 */
public class CompositeValidator implements IdentityValidator {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<IdentityValidator> validators;

  public CompositeValidator(IdentityValidator... validators) {
    this(List.of(validators));
  }

  public CompositeValidator(List<IdentityValidator> validators) {
    this.validators = List.copyOf(validators);
  }

  @Override
  public Identity validateIdentityToken(
      Session session, UserIdentityToken token, UserTokenPolicy policy, SignatureData signature)
      throws UaException {

    Iterator<IdentityValidator> iterator =
        validators.stream()
            .filter(v -> v.getSupportedTokenTypes().contains(policy.getTokenType()))
            .iterator();

    while (iterator.hasNext()) {
      IdentityValidator validator = iterator.next();

      try {
        return validator.validateIdentityToken(session, token, policy, signature);
      } catch (Exception e) {
        if (!iterator.hasNext()) {
          throw e;
        }

        logger.debug("IdentityValidator={} failed, trying next...", validator.toString());
      }
    }

    throw new UaException(StatusCodes.Bad_IdentityTokenInvalid);
  }

  @Override
  public Set<UserTokenType> getSupportedTokenTypes() {
    return validators.stream()
        .flatMap(v -> v.getSupportedTokenTypes().stream())
        .collect(Collectors.toSet());
  }
}
