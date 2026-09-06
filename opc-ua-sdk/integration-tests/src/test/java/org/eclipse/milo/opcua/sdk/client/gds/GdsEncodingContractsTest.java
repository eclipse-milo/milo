/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.client.gds;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.concurrent.TimeUnit;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AuthorizationServiceType;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.AuthorizationServiceTypeNode;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GdsEncodingContractsTest extends AbstractGdsClientTest {

  // Part12 §7.9.4 defines PEM/PFX and a password. Parse the returned format and prove its key
  // belongs to the issued certificate, including password-protected requests.
  @ParameterizedTest
  @CsvSource({"PEM,''", "PEM,secret", "PFX,''", "PFX,secret"})
  void newKeyPairUsesRequestedEncodingAndPassword(String format, String password) throws Exception {
    NodeId application = registerTestApplication();
    NodeId request =
        gdsClient.startNewKeyPairRequest(
            application, null, null, "CN=Test", new String[0], format, password);
    var issued = gdsClient.finishRequest(application, request);
    byte[] bytes = issued.privateKey().bytesOrEmpty();
    PrivateKey key;
    if (format.equals("PFX")) {
      KeyStore pfx = KeyStore.getInstance("PKCS12");
      pfx.load(new ByteArrayInputStream(bytes), password.toCharArray());
      String alias = pfx.aliases().nextElement();
      key = (PrivateKey) pfx.getKey(alias, password.toCharArray());
      assertEquals(
          CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty()),
          pfx.getCertificate(alias));
      if (!password.isEmpty()) {
        assertThrows(
            java.io.IOException.class,
            () ->
                KeyStore.getInstance("PKCS12")
                    .load(new ByteArrayInputStream(bytes), "wrong-password".toCharArray()));
      }
    } else {
      try (var parser =
          new PEMParser(new StringReader(new String(bytes, StandardCharsets.US_ASCII)))) {
        Object parsed = parser.readObject();
        PrivateKeyInfo info;
        if (password.isEmpty()) {
          info = assertInstanceOf(PrivateKeyInfo.class, parsed);
        } else {
          var encrypted = assertInstanceOf(PKCS8EncryptedPrivateKeyInfo.class, parsed);
          info =
              encrypted.decryptPrivateKeyInfo(
                  new JceOpenSSLPKCS8DecryptorProviderBuilder()
                      .setProvider(new BouncyCastleProvider())
                      .build(password.toCharArray()));
        }
        key = new JcaPEMKeyConverter().getPrivateKey(info);
      }
    }
    var certificate = CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty());
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initSign(key);
    signature.update(new byte[] {1, 2, 3});
    byte[] signed = signature.sign();
    signature.initVerify(certificate);
    signature.update(new byte[] {1, 2, 3});
    assertTrue(signature.verify(signed), "returned key must match the issued certificate");
  }

  // A request for an unsupported format must fail at request creation, not silently return DER.
  @Test
  void unsupportedPrivateKeyFormatIsRejected() throws Exception {
    NodeId application = registerTestApplication();
    UaException error =
        assertThrows(
            UaException.class,
            () ->
                gdsClient.startNewKeyPairRequest(
                    application, null, null, "CN=Test", new String[0], "unsupported", null));
    assertEquals(StatusCodes.Bad_InvalidArgument, error.getStatusCode().value());
  }

  // A successful Write service can carry Bad_UserAccessDenied for its one operation. Generated
  // blocking
  // writers promise UaException for that outcome while async writers expose the exact StatusCode.
  @Test
  void generatedBlockingWriterReportsOperationFailureAndStillAcceptsGoodWrites() throws Exception {
    NodeId id = newNodeId("ReadOnlyAuthorizationService");
    UaVariableNode[] property = new UaVariableNode[1];
    testNamespace.configure(
        (context, nodes) -> {
          UaObjectNode object =
              UaObjectNode.builder(context)
                  .setNodeId(id)
                  .setBrowseName(newQualifiedName("AuthorizationService"))
                  .setDisplayName(LocalizedText.english("AuthorizationService"))
                  .build();
          nodes.addNode(object);
          object.setProperty(AuthorizationServiceType.SERVICE_URI, "urn:original");
          property[0] =
              (UaVariableNode)
                  object.getPropertyNode(AuthorizationServiceType.SERVICE_URI).orElseThrow();
          property[0].setAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));
          property[0].setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_ONLY));
        });
    var node =
        new AuthorizationServiceTypeNode(
            client,
            id,
            NodeClass.Object,
            newQualifiedName("AuthorizationService"),
            LocalizedText.english("AuthorizationService"),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            null,
            null,
            null,
            ubyte(0));
    assertEquals("urn:original", node.readServiceUri());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied,
        node.writeServiceUriAsync("urn:rejected").get(10, TimeUnit.SECONDS).value());
    UaException error = assertThrows(UaException.class, () -> node.writeServiceUri("urn:rejected"));
    assertEquals(StatusCodes.Bad_UserAccessDenied, error.getStatusCode().value());
    assertEquals("urn:original", node.readServiceUri());
    property[0].setAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    property[0].setUserAccessLevel(AccessLevel.toValue(AccessLevel.READ_WRITE));
    node.writeServiceUri("urn:accepted");
    assertEquals("urn:accepted", node.readServiceUri());
  }
}
