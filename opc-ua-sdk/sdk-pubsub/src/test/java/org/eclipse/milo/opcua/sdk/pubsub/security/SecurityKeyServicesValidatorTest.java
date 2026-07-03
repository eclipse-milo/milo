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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Tests for {@link SecurityKeyServicesValidator} against the Part 14 v1.05 Table 40 entry shapes:
 * hard failures (invalid ApplicationType, SERVER entries without an ApplicationUri or a discovery
 * target), the tolerance fallback (a filled {@code endpointUrl} accepted as discovery target with a
 * warning), and the warn-don't-fail rows for fields Table 40 says "shall be null or empty" (or pins
 * to a specific value) on the producer side.
 */
class SecurityKeyServicesValidatorTest {

  private static final String SKS_URI = "urn:milo:sks";
  private static final String DISCOVERY_URL = "opc.tcp://sks.example:4840";

  private static ApplicationDescription server(
      ApplicationType applicationType, String applicationUri, String[] discoveryUrls) {

    return new ApplicationDescription(
        applicationUri,
        null,
        LocalizedText.english("SKS"),
        applicationType,
        null,
        null,
        discoveryUrls);
  }

  private static EndpointDescription entry(
      String endpointUrl, ApplicationDescription server, MessageSecurityMode securityMode) {

    return new EndpointDescription(
        endpointUrl, server, ByteString.NULL_VALUE, securityMode, null, null, null, ubyte(0));
  }

  private static EndpointDescription validPullEntry() {
    return entry(
        null,
        server(ApplicationType.Server, SKS_URI, new String[] {DISCOVERY_URL}),
        MessageSecurityMode.SignAndEncrypt);
  }

  @Test
  void conformantPullEntryPassesWithNoWarnings() throws UaException {
    List<String> warnings = SecurityKeyServicesValidator.validate(List.of(validPullEntry()));

    assertEquals(List.of(), warnings);
  }

  @Test
  void emptyStringEndpointUrlCountsAsAbsent() throws UaException {
    // Table 40: "EndpointUrl ... Shall be null or empty" — the empty string is as conformant
    // as null and must not draw the filled-endpointUrl warning.
    EndpointDescription entry =
        entry(
            "",
            server(ApplicationType.Server, SKS_URI, new String[] {DISCOVERY_URL}),
            MessageSecurityMode.SignAndEncrypt);

    assertEquals(List.of(), SecurityKeyServicesValidator.validateEntry(entry, 0));
  }

  @Test
  void conformantPushEntryPassesWithNoWarnings() throws UaException {
    // CLIENT = keys are pushed via SetSecurityKeys; the SERVER-only requirements
    // (applicationUri, discovery target) do not apply.
    EndpointDescription entry =
        entry(null, server(ApplicationType.Client, null, null), MessageSecurityMode.SignAndEncrypt);

    List<String> warnings = SecurityKeyServicesValidator.validate(List.of(entry));

    assertEquals(List.of(), warnings);
  }

  @Test
  void entryWithoutServerIsRejected() {
    EndpointDescription entry = entry(null, null, MessageSecurityMode.SignAndEncrypt);

    UaException e =
        assertThrows(UaException.class, () -> SecurityKeyServicesValidator.validateEntry(entry, 0));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
  }

  @ParameterizedTest
  @EnumSource(
      value = ApplicationType.class,
      names = {"ClientAndServer", "DiscoveryServer"})
  void invalidApplicationTypesAreRejected(ApplicationType applicationType) {
    // Table 40: "CLIENTANDSERVER — Invalid value. DISCOVERYSERVER — Invalid value."
    EndpointDescription entry =
        entry(
            null,
            server(applicationType, SKS_URI, new String[] {DISCOVERY_URL}),
            MessageSecurityMode.SignAndEncrypt);

    UaException e =
        assertThrows(UaException.class, () -> SecurityKeyServicesValidator.validateEntry(entry, 0));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("securityKeyServices[0]"),
        "message must name the entry: " + e.getMessage());
  }

  @Test
  void serverEntryWithoutApplicationUriIsRejected() {
    for (String applicationUri : new String[] {null, ""}) {
      EndpointDescription entry =
          entry(
              null,
              server(ApplicationType.Server, applicationUri, new String[] {DISCOVERY_URL}),
              MessageSecurityMode.SignAndEncrypt);

      UaException e =
          assertThrows(
              UaException.class, () -> SecurityKeyServicesValidator.validateEntry(entry, 0));

      assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    }
  }

  @Test
  void serverEntryWithoutAnyDiscoveryTargetIsRejected() {
    // Neither discoveryUrls (null, empty, or empty-element-only) nor endpointUrl provides a
    // discovery target.
    String[][] discoveryUrlVariants = {null, new String[0], new String[] {""}};

    for (String[] discoveryUrls : discoveryUrlVariants) {
      EndpointDescription entry =
          entry(
              null,
              server(ApplicationType.Server, SKS_URI, discoveryUrls),
              MessageSecurityMode.SignAndEncrypt);

      UaException e =
          assertThrows(
              UaException.class, () -> SecurityKeyServicesValidator.validateEntry(entry, 0));

      assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    }
  }

  @Test
  void endpointUrlFallbackPassesWithWarnings() throws UaException {
    // The tolerance row: no discoveryUrls, but a non-empty endpointUrl usable as a discovery
    // target (open62541-ecosystem configs fill endpointUrl). Passes, with warnings for both
    // the fallback and the Table 40 "EndpointUrl shall be null or empty" non-conformance.
    EndpointDescription entry =
        entry(
            DISCOVERY_URL,
            server(ApplicationType.Server, SKS_URI, null),
            MessageSecurityMode.SignAndEncrypt);

    List<String> warnings = SecurityKeyServicesValidator.validateEntry(entry, 0);

    assertEquals(2, warnings.size(), "unexpected warnings: " + warnings);
    assertTrue(warnings.get(0).contains("discoveryUrls"), "unexpected warnings: " + warnings);
    assertTrue(warnings.get(1).contains("endpointUrl"), "unexpected warnings: " + warnings);
  }

  @Test
  void softNonConformancesWarnButDoNotFail() throws UaException {
    // Every warn-only row at once: filled endpointUrl (with discoveryUrls also present),
    // filled serverCertificate, gatewayServerUri, discoveryProfileUri, securityMode != S&E,
    // securityLevel != 0.
    EndpointDescription entry =
        new EndpointDescription(
            "opc.tcp://sks.example:4840/endpoint",
            new ApplicationDescription(
                SKS_URI,
                null,
                LocalizedText.english("SKS"),
                ApplicationType.Server,
                "urn:milo:gateway",
                "http://milo/discovery-profile",
                new String[] {DISCOVERY_URL}),
            ByteString.of(new byte[] {1, 2, 3}),
            MessageSecurityMode.Sign,
            null,
            null,
            null,
            ubyte(3));

    List<String> warnings = SecurityKeyServicesValidator.validateEntry(entry, 0);

    assertEquals(6, warnings.size(), "unexpected warnings: " + warnings);
    assertTrue(warnings.stream().anyMatch(w -> w.contains("endpointUrl")));
    assertTrue(warnings.stream().anyMatch(w -> w.contains("serverCertificate")));
    assertTrue(warnings.stream().anyMatch(w -> w.contains("gatewayServerUri")));
    assertTrue(warnings.stream().anyMatch(w -> w.contains("discoveryProfileUri")));
    assertTrue(warnings.stream().anyMatch(w -> w.contains("securityMode")));
    assertTrue(warnings.stream().anyMatch(w -> w.contains("securityLevel")));
    assertTrue(warnings.stream().allMatch(w -> w.contains("securityKeyServices[0]")));
  }

  @Test
  void validateNamesTheFailingEntryIndex() {
    EndpointDescription invalid =
        entry(
            null,
            server(ApplicationType.ClientAndServer, SKS_URI, new String[] {DISCOVERY_URL}),
            MessageSecurityMode.SignAndEncrypt);

    UaException e =
        assertThrows(
            UaException.class,
            () -> SecurityKeyServicesValidator.validate(List.of(validPullEntry(), invalid)));

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(
        e.getMessage().contains("securityKeyServices[1]"),
        "message must name the entry: " + e.getMessage());
  }

  @Test
  void validateAggregatesWarningsAcrossEntriesWithPerEntryIndices() throws UaException {
    EndpointDescription fallback =
        entry(
            DISCOVERY_URL,
            server(ApplicationType.Server, SKS_URI, null),
            MessageSecurityMode.SignAndEncrypt);

    List<String> warnings =
        SecurityKeyServicesValidator.validate(List.of(validPullEntry(), fallback));

    assertEquals(2, warnings.size(), "unexpected warnings: " + warnings);
    assertTrue(warnings.stream().allMatch(w -> w.contains("securityKeyServices[1]")));
  }
}
