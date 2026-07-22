/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

public class OpcUaClientConfigBuilder {

  private EndpointDescription endpoint;
  private List<EndpointDescription> discoveryEndpoints;
  private KeyPair keyPair;
  private X509Certificate certificate;
  private X509Certificate[] certificateChain;
  private CertificateManager certificateManager;
  private CertificateIdentitySelector certificateIdentitySelector =
      DefaultCertificateIdentitySelector.create();
  private @Nullable NodeId certificateGroupId;
  private @Nullable NodeId certificateTypeId;
  private CertificateValidator certificateValidator =
      new CertificateValidator.InsecureCertificateValidator();

  private LocalizedText applicationName =
      LocalizedText.english("Eclipse Milo application name not configured");
  private String applicationUri = "urn:eclipse:milo:client:applicationUriNotConfigured";
  private String productUri = "https://github.com/eclipse-milo/milo";

  private Supplier<String> sessionName;
  private String[] sessionLocaleIds = new String[0];
  private UInteger sessionTimeout = uint(120_000);

  private UInteger requestTimeout = uint(60_000);
  private IdentityProvider identityProvider = new AnonymousProvider();

  private EncodingLimits encodingLimits = EncodingLimits.DEFAULT;
  private UInteger maxResponseMessageSize = uint(0);
  private UInteger maxPendingPublishRequests = uint(UInteger.MAX_VALUE);

  private UInteger keepAliveFailuresAllowed = uint(1);
  private UInteger keepAliveInterval = uint(5000);
  private UInteger keepAliveTimeout = uint(5000);

  private boolean sessionEndpointValidationEnabled = false;

  private @Nullable SecurityKeysListener securityKeysListener;

  public OpcUaClientConfigBuilder setApplicationName(LocalizedText applicationName) {
    this.applicationName = applicationName;
    return this;
  }

  public OpcUaClientConfigBuilder setApplicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
    return this;
  }

  public OpcUaClientConfigBuilder setProductUri(String productUri) {
    this.productUri = productUri;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionName(Supplier<String> sessionName) {
    this.sessionName = sessionName;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionLocaleIds(String[] sessionLocaleIds) {
    this.sessionLocaleIds = sessionLocaleIds;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionTimeout(UInteger sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
    return this;
  }

  public OpcUaClientConfigBuilder setRequestTimeout(UInteger requestTimeout) {
    this.requestTimeout = requestTimeout;
    return this;
  }

  public OpcUaClientConfigBuilder setEncodingLimits(EncodingLimits encodingLimits) {
    this.encodingLimits = encodingLimits;
    return this;
  }

  public OpcUaClientConfigBuilder setMaxResponseMessageSize(UInteger maxResponseMessageSize) {
    this.maxResponseMessageSize = maxResponseMessageSize;
    return this;
  }

  public OpcUaClientConfigBuilder setMaxPendingPublishRequests(UInteger maxPendingPublishRequests) {
    this.maxPendingPublishRequests = maxPendingPublishRequests;
    return this;
  }

  public OpcUaClientConfigBuilder setIdentityProvider(IdentityProvider identityProvider) {
    this.identityProvider = identityProvider;
    return this;
  }

  public OpcUaClientConfigBuilder setKeepAliveFailuresAllowed(UInteger keepAliveFailuresAllowed) {
    this.keepAliveFailuresAllowed = keepAliveFailuresAllowed;
    return this;
  }

  public OpcUaClientConfigBuilder setKeepAliveInterval(UInteger keepAliveInterval) {
    this.keepAliveInterval = keepAliveInterval;
    return this;
  }

  public OpcUaClientConfigBuilder setKeepAliveTimeout(UInteger keepAliveTimeout) {
    this.keepAliveTimeout = keepAliveTimeout;
    return this;
  }

  public OpcUaClientConfigBuilder setSessionEndpointValidationEnabled(
      boolean validateSessionEndpointsEnabled) {

    this.sessionEndpointValidationEnabled = validateSessionEndpointsEnabled;
    return this;
  }

  public OpcUaClientConfigBuilder setEndpoint(EndpointDescription endpoint) {
    this.endpoint = endpoint;
    return this;
  }

  public OpcUaClientConfigBuilder setDiscoveryEndpoints(
      List<EndpointDescription> discoveryEndpoints) {

    this.discoveryEndpoints = discoveryEndpoints;
    return this;
  }

  public OpcUaClientConfigBuilder setKeyPair(KeyPair keyPair) {
    this.keyPair = keyPair;
    return this;
  }

  public OpcUaClientConfigBuilder setCertificate(X509Certificate certificate) {
    this.certificate = certificate;
    return this;
  }

  public OpcUaClientConfigBuilder setCertificateChain(X509Certificate[] certificateChain) {
    this.certificateChain = certificateChain;
    return this;
  }

  /**
   * Set the certificate manager used for policy-aware client certificate selection.
   *
   * @param certificateManager the certificate manager to search.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateManager(CertificateManager certificateManager) {
    this.certificateManager = certificateManager;
    return this;
  }

  /**
   * Set the selector used to choose a local certificate identity.
   *
   * @param certificateIdentitySelector the certificate identity selector.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateIdentitySelector(
      CertificateIdentitySelector certificateIdentitySelector) {

    this.certificateIdentitySelector = certificateIdentitySelector;
    return this;
  }

  /**
   * Set the requested certificate group for client identity selection.
   *
   * @param certificateGroupId the certificate group ID, or {@code null} to allow any group.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateGroupId(@Nullable NodeId certificateGroupId) {
    this.certificateGroupId = certificateGroupId;
    return this;
  }

  /**
   * Set the requested certificate type for client identity selection.
   *
   * @param certificateTypeId the certificate type ID, or {@code null} to let the endpoint security
   *     policy choose.
   * @return this builder.
   */
  public OpcUaClientConfigBuilder setCertificateTypeId(@Nullable NodeId certificateTypeId) {
    this.certificateTypeId = certificateTypeId;
    return this;
  }

  public OpcUaClientConfigBuilder setCertificateValidator(
      CertificateValidator certificateValidator) {
    this.certificateValidator = certificateValidator;
    return this;
  }

  public OpcUaClientConfigBuilder setSecurityKeysListener(
      @Nullable SecurityKeysListener securityKeysListener) {
    this.securityKeysListener = securityKeysListener;
    return this;
  }

  public OpcUaClientConfig build() {
    if (sessionName == null) {
      sessionName =
          () ->
              String.format("UaSession:%s:%s", applicationName.text(), System.currentTimeMillis());
    }

    return new OpcUaClientConfigImpl(
        endpoint,
        discoveryEndpoints,
        keyPair,
        certificate,
        certificateChain,
        certificateManager,
        certificateIdentitySelector,
        certificateGroupId,
        certificateTypeId,
        certificateValidator,
        applicationName,
        applicationUri,
        productUri,
        sessionName,
        sessionLocaleIds,
        sessionTimeout,
        requestTimeout,
        encodingLimits,
        maxResponseMessageSize,
        maxPendingPublishRequests,
        identityProvider,
        keepAliveFailuresAllowed,
        keepAliveInterval,
        keepAliveTimeout,
        sessionEndpointValidationEnabled,
        securityKeysListener);
  }

  static class OpcUaClientConfigImpl implements OpcUaClientConfig {

    private final EndpointDescription endpoint;
    private final List<EndpointDescription> discoveryEndpoints;
    private final KeyPair keyPair;
    private final X509Certificate certificate;
    private final X509Certificate[] certificateChain;
    private final CertificateManager certificateManager;
    private final CertificateIdentitySelector certificateIdentitySelector;
    private final @Nullable NodeId certificateGroupId;
    private final @Nullable NodeId certificateTypeId;
    private final CertificateValidator certificateValidator;
    private final LocalizedText applicationName;
    private final String applicationUri;
    private final String productUri;
    private final Supplier<String> sessionName;
    private final String[] sessionLocaleIds;
    private final UInteger sessionTimeout;

    private final UInteger requestTimeout;
    private final EncodingLimits encodingLimits;
    private final UInteger maxResponseMessageSize;
    private final UInteger maxPendingPublishRequests;
    private final IdentityProvider identityProvider;
    private final UInteger keepAliveFailuresAllowed;
    private final UInteger keepAliveInterval;
    private final UInteger keepAliveTimeout;
    private final boolean sessionEndpointValidationEnabled;
    private final @Nullable SecurityKeysListener securityKeysListener;

    OpcUaClientConfigImpl(
        EndpointDescription endpoint,
        List<EndpointDescription> discoveryEndpoints,
        KeyPair keyPair,
        X509Certificate certificate,
        X509Certificate[] certificateChain,
        CertificateManager certificateManager,
        CertificateIdentitySelector certificateIdentitySelector,
        @Nullable NodeId certificateGroupId,
        @Nullable NodeId certificateTypeId,
        CertificateValidator certificateValidator,
        LocalizedText applicationName,
        String applicationUri,
        String productUri,
        Supplier<String> sessionName,
        String[] sessionLocaleIds,
        UInteger sessionTimeout,
        UInteger requestTimeout,
        EncodingLimits encodingLimits,
        UInteger maxResponseMessageSize,
        UInteger maxPendingPublishRequests,
        IdentityProvider identityProvider,
        UInteger keepAliveFailuresAllowed,
        UInteger keepAliveInterval,
        UInteger keepAliveTimeout,
        boolean sessionEndpointValidationEnabled,
        @Nullable SecurityKeysListener securityKeysListener) {

      this.endpoint = endpoint;
      this.discoveryEndpoints = discoveryEndpoints;
      this.keyPair = keyPair;
      this.certificate = certificate;
      this.certificateChain = certificateChain;
      this.certificateManager = certificateManager;
      this.certificateIdentitySelector = certificateIdentitySelector;
      this.certificateGroupId = certificateGroupId;
      this.certificateTypeId = certificateTypeId;
      this.certificateValidator = certificateValidator;
      this.applicationName = applicationName;
      this.applicationUri = applicationUri;
      this.productUri = productUri;
      this.sessionName = sessionName;
      this.sessionLocaleIds = sessionLocaleIds;
      this.sessionTimeout = sessionTimeout;
      this.requestTimeout = requestTimeout;
      this.encodingLimits = encodingLimits;
      this.maxResponseMessageSize = maxResponseMessageSize;
      this.maxPendingPublishRequests = maxPendingPublishRequests;
      this.identityProvider = identityProvider;
      this.keepAliveFailuresAllowed = keepAliveFailuresAllowed;
      this.keepAliveInterval = keepAliveInterval;
      this.keepAliveTimeout = keepAliveTimeout;
      this.sessionEndpointValidationEnabled = sessionEndpointValidationEnabled;
      this.securityKeysListener = securityKeysListener;
    }

    @Override
    public EndpointDescription getEndpoint() {
      return endpoint;
    }

    @Override
    public List<EndpointDescription> getDiscoveryEndpoints() {
      return discoveryEndpoints;
    }

    @Override
    public Optional<KeyPair> getKeyPair() {
      return Optional.ofNullable(keyPair);
    }

    @Override
    public Optional<X509Certificate> getCertificate() {
      return Optional.ofNullable(certificate);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain() {
      return Optional.ofNullable(certificateChain);
    }

    @Override
    public Optional<CertificateManager> getCertificateManager() {
      return Optional.ofNullable(certificateManager);
    }

    @Override
    public CertificateIdentitySelector getCertificateIdentitySelector() {
      return certificateIdentitySelector;
    }

    @Override
    public Optional<NodeId> getCertificateGroupId() {
      return Optional.ofNullable(certificateGroupId);
    }

    @Override
    public Optional<NodeId> getCertificateTypeId() {
      return Optional.ofNullable(certificateTypeId);
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return certificateValidator;
    }

    @Override
    public LocalizedText getApplicationName() {
      return applicationName;
    }

    @Override
    public String getApplicationUri() {
      return applicationUri;
    }

    @Override
    public String getProductUri() {
      return productUri;
    }

    @Override
    public Supplier<String> getSessionName() {
      return sessionName;
    }

    @Override
    public String[] getSessionLocaleIds() {
      return sessionLocaleIds;
    }

    @Override
    public UInteger getSessionTimeout() {
      return sessionTimeout;
    }

    @Override
    public UInteger getRequestTimeout() {
      return requestTimeout;
    }

    @Override
    public EncodingLimits getEncodingLimits() {
      return encodingLimits;
    }

    @Override
    public UInteger getMaxResponseMessageSize() {
      return maxResponseMessageSize;
    }

    @Override
    public UInteger getMaxPendingPublishRequests() {
      return maxPendingPublishRequests;
    }

    @Override
    public IdentityProvider getIdentityProvider() {
      return identityProvider;
    }

    @Override
    public UInteger getKeepAliveFailuresAllowed() {
      return keepAliveFailuresAllowed;
    }

    @Override
    public UInteger getKeepAliveInterval() {
      return keepAliveInterval;
    }

    @Override
    public UInteger getKeepAliveTimeout() {
      return keepAliveTimeout;
    }

    @Override
    public boolean isSessionEndpointValidationEnabled() {
      return sessionEndpointValidationEnabled;
    }

    @Override
    public Optional<SecurityKeysListener> getSecurityKeysListener() {
      return Optional.ofNullable(securityKeysListener);
    }
  }
}
