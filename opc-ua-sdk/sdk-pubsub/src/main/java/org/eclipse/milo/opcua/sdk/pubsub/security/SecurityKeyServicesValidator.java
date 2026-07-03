/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.security;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * Validates SecurityKeyServices entries against Part 14 v1.05 Table 40, which repurposes generic
 * {@link EndpointDescription} fields as SKS identity records.
 *
 * <p>Validation posture: hard failures throw {@link UaException} with {@code
 * Bad_ConfigurationError} naming the entry index; soft non-conformances are returned as warning
 * strings for the caller to log at WARN. Config builders and mappers never call this — foreign
 * configs with non-conformant entries must survive a lossless round trip; validation happens where
 * entries are consumed: SKS pull-provider construction now, and the Phase 5 remote-configuration
 * write path later.
 *
 * <p>Rules:
 *
 * <ul>
 *   <li>Hard: {@code server} present with an ApplicationType of SERVER (pull) or CLIENT (push) —
 *       CLIENTANDSERVER and DISCOVERYSERVER are "Invalid value" per Table 40.
 *   <li>Hard (SERVER entries): non-empty {@code server.applicationUri}, and at least one of a
 *       non-empty {@code server.discoveryUrls} element or — tolerance, with a warning — a non-empty
 *       {@code endpointUrl} usable as a discovery target.
 *   <li>Warn: filled {@code endpointUrl}, {@code serverCertificate}, {@code
 *       server.gatewayServerUri}, or {@code server.discoveryProfileUri}; {@code securityMode !=
 *       SignAndEncrypt}; {@code securityLevel != 0}. These are "shall"s on the producer of the
 *       config; a consumer that can still resolve the SKS operates with a diagnostic instead of
 *       hard-failing.
 * </ul>
 */
public final class SecurityKeyServicesValidator {

  private SecurityKeyServicesValidator() {}

  /**
   * Validate a list of SecurityKeyServices entries.
   *
   * @param entries the entries to validate.
   * @return warnings for soft non-conformances, each naming the entry index; possibly empty.
   * @throws UaException with {@code Bad_ConfigurationError}, naming the entry index, on the first
   *     hard violation.
   */
  public static List<String> validate(List<EndpointDescription> entries) throws UaException {
    List<String> warnings = new ArrayList<>();
    for (int i = 0; i < entries.size(); i++) {
      warnings.addAll(validateEntry(entries.get(i), i));
    }
    return warnings;
  }

  /**
   * Validate a single SecurityKeyServices entry.
   *
   * @param entry the entry to validate.
   * @param index the index of the entry in its containing array, used in messages.
   * @return warnings for soft non-conformances; possibly empty.
   * @throws UaException with {@code Bad_ConfigurationError} on a hard violation.
   */
  public static List<String> validateEntry(EndpointDescription entry, int index)
      throws UaException {

    String path = "securityKeyServices[%d]".formatted(index);

    ApplicationDescription server = entry.getServer();
    if (server == null) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          path + ": server (ApplicationDescription) is required");
    }

    ApplicationType applicationType = server.getApplicationType();
    if (applicationType != ApplicationType.Server && applicationType != ApplicationType.Client) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          path + ": invalid ApplicationType " + applicationType + " (must be Server or Client)");
    }

    List<String> warnings = new ArrayList<>();

    if (applicationType == ApplicationType.Server) {
      if (nullOrEmpty(server.getApplicationUri())) {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError, path + ": server.applicationUri is required");
      }

      boolean hasDiscoveryUrl = hasNonEmptyElement(server.getDiscoveryUrls());
      boolean hasEndpointUrl = !nullOrEmpty(entry.getEndpointUrl());

      if (!hasDiscoveryUrl && !hasEndpointUrl) {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            path + ": server.discoveryUrls or endpointUrl must provide a discovery target");
      }
      if (!hasDiscoveryUrl) {
        warnings.add(
            path + ": server.discoveryUrls is empty; endpointUrl is used as a discovery target");
      }
    }

    if (!nullOrEmpty(entry.getEndpointUrl())) {
      warnings.add(path + ": endpointUrl should be null or empty");
    }
    if (entry.getServerCertificate() != null && !entry.getServerCertificate().isNullOrEmpty()) {
      warnings.add(path + ": serverCertificate should be null or empty");
    }
    if (!nullOrEmpty(server.getGatewayServerUri())) {
      warnings.add(path + ": server.gatewayServerUri should be null or empty");
    }
    if (!nullOrEmpty(server.getDiscoveryProfileUri())) {
      warnings.add(path + ": server.discoveryProfileUri should be null or empty");
    }
    if (entry.getSecurityMode() != MessageSecurityMode.SignAndEncrypt) {
      warnings.add(
          path + ": securityMode should be SignAndEncrypt, got " + entry.getSecurityMode());
    }
    if (entry.getSecurityLevel() != null && entry.getSecurityLevel().intValue() != 0) {
      warnings.add(path + ": securityLevel should be 0, got " + entry.getSecurityLevel());
    }

    return warnings;
  }

  private static boolean hasNonEmptyElement(String @Nullable [] values) {
    if (values == null) {
      return false;
    }
    for (String value : values) {
      if (!nullOrEmpty(value)) {
        return true;
      }
    }
    return false;
  }

  private static boolean nullOrEmpty(@Nullable String value) {
    return value == null || value.isEmpty();
  }
}
