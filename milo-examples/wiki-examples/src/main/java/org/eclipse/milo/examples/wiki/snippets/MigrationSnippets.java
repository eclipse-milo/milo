/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.wiki.snippets;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.*;
import org.eclipse.milo.opcua.stack.core.security.CertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.CertificateStore;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/** The 1.2 side of the before-and-after samples in the 1.2.0 migration guide. */
public final class MigrationSnippets {

  private MigrationSnippets() {}

  static OpcUaClientConfig clientConfig(
      EndpointDescription endpoint,
      KeyPair keyPair,
      X509Certificate[] chain,
      CertificateValidator validator,
      String applicationUri) {
    // snippet:client-identity:start
    var config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setApplicationUri(applicationUri)
            .setCertificateIdentity(keyPair, chain)
            .setCertificateValidator(validator)
            .build();
    // snippet:client-identity:end
    return config;
  }

  static DefaultCertificateManager certificateManager(
      TrustListManager trust,
      CertificateStore store,
      CertificateFactory factory,
      CertificateValidator validator,
      CertificateQuarantine quarantine)
      throws Exception {
    // snippet:certificate-group:start
    var group = new DefaultCertificateGroup(trust, store, quarantine, validator);
    factory.createMissingCertificates(group);
    var manager = new DefaultCertificateManager(group);
    // snippet:certificate-group:end
    return manager;
  }

  static UaObjectNode createFile(
      OpcUaServer server,
      ManagedAddressSpace space,
      NodeId parentId,
      NodeId instanceId,
      QualifiedName browseName)
      throws Exception {
    // snippet:node-factory:start
    var request =
        InstantiationRequest.of(UaObjectNode.class, NodeIds.FileType)
            .nodeId(instanceId)
            .browseName(browseName)
            .displayName(LocalizedText.english("File:Example"))
            .parent(parentId, NodeIds.Organizes)
            .legacyPathStrings()
            .target(space.getNodeManager())
            .build();
    UaObjectNode node = server.getNodeInstantiator().instantiate(request).root();
    // snippet:node-factory:end
    return node;
  }
}
