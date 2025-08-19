package org.eclipse.milo.opcua.sdk.client.session;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SessionEndpointValidationTest {

  private static final String TRANSPORT_OPC_TCP = Stack.TCP_UASC_UABINARY_TRANSPORT_URI;

  private static final String TRANSPORT_HTTPS = Stack.HTTPS_UABINARY_TRANSPORT_URI;

  @Test
  @DisplayName("Returns without validation when no discovery endpoints match the transport profile")
  void testNoDiscoveryForTransportReturns() {
    List<EndpointDescription> discovery =
        List.of(
            endpoint(
                "urn:app",
                "opc.tcp://a:4840",
                MessageSecurityMode.None,
                "none",
                userTokens("anon"),
                TRANSPORT_HTTPS,
                (short) 1));

    List<EndpointDescription> session =
        List.of(
            endpoint(
                "urn:app",
                "opc.tcp://a:4840",
                MessageSecurityMode.None,
                "none",
                userTokens("anon"),
                TRANSPORT_OPC_TCP,
                (short) 1));

    assertDoesNotThrow(
        () -> SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));
  }

  @Test
  @DisplayName("Throws when filtered sizes differ")
  void testSizeMismatchThrows() {
    List<EndpointDescription> discovery = new ArrayList<>();
    discovery.add(
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.None,
            "none",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1));
    discovery.add(
        endpoint(
            "urn:app",
            "opc.tcp://b:4840",
            MessageSecurityMode.None,
            "none",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1));

    List<EndpointDescription> session =
        List.of(
            endpoint(
                "urn:app",
                "opc.tcp://a:4840",
                MessageSecurityMode.None,
                "none",
                userTokens("anon"),
                TRANSPORT_OPC_TCP,
                (short) 1));

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, ex.getStatusCode().getValue());
  }

  @Test
  @DisplayName("Passes when all endpoints match regardless of order")
  void testAllMatchDifferentOrder() {
    EndpointDescription a =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.None,
            "none",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1);
    EndpointDescription b =
        endpoint(
            "urn:app",
            "opc.tcp://b:4840",
            MessageSecurityMode.SignAndEncrypt,
            "policy#Basic256Sha256",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 2);

    List<EndpointDescription> discovery = List.of(a, b);
    List<EndpointDescription> session = List.of(b, a);

    assertDoesNotThrow(
        () -> SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));
  }

  enum DiffKind {
    ApplicationUri,
    EndpointUrl,
    SecurityMode,
    SecurityPolicyUri,
    UserIdentityTokens,
    SecurityLevel
  }

  static Stream<Arguments> differingEndpoints() {
    return Stream.of(
        Arguments.of(DiffKind.ApplicationUri),
        Arguments.of(DiffKind.EndpointUrl),
        Arguments.of(DiffKind.SecurityMode),
        Arguments.of(DiffKind.SecurityPolicyUri),
        Arguments.of(DiffKind.UserIdentityTokens),
        Arguments.of(DiffKind.SecurityLevel));
  }

  @ParameterizedTest(name = "Mismatch due to differing {0} throws UaException")
  @MethodSource("differingEndpoints")
  void testEndpointMismatchThrows(DiffKind kind) {
    EndpointDescription discoveryEp =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.Sign,
            "policy#A",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1);

    EndpointDescription sessionEp;
    switch (kind) {
      case ApplicationUri ->
          sessionEp =
              endpoint(
                  "urn:app2",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case EndpointUrl ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://DIFF:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case SecurityMode ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.None,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case SecurityPolicyUri ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#B",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case UserIdentityTokens ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("DIFF"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case SecurityLevel ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 2);
      default -> throw new IllegalStateException("Unexpected kind: " + kind);
    }

    List<EndpointDescription> discovery = List.of(discoveryEp);
    List<EndpointDescription> session = List.of(sessionEp);

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, ex.getStatusCode().getValue());
  }

  private static EndpointDescription endpoint(
      String applicationUri,
      String endpointUrl,
      MessageSecurityMode securityMode,
      String securityPolicyUri,
      UserTokenPolicy[] userTokens,
      String transportProfileUri,
      short securityLevel) {

    ApplicationDescription serverDesc =
        new ApplicationDescription(
            applicationUri,
            "product:uri",
            new LocalizedText("en", "name"),
            ApplicationType.Server,
            null,
            null,
            new String[0]);

    return new EndpointDescription(
        endpointUrl,
        serverDesc,
        null,
        securityMode,
        securityPolicyUri,
        userTokens,
        transportProfileUri,
        UByte.valueOf(securityLevel));
  }

  private static UserTokenPolicy[] userTokens(String policyId) {
    return new UserTokenPolicy[] {
      new UserTokenPolicy(policyId, UserTokenType.Anonymous, null, null, null)
    };
  }
}
