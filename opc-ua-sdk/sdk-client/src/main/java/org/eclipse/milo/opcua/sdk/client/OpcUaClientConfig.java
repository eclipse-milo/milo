/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelectionContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateIdentitySelector;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;

public interface OpcUaClientConfig {

  /**
   * Get the endpoint to connect to.
   *
   * @return the {@link EndpointDescription} to connect to.
   */
  EndpointDescription getEndpoint();

  /**
   * Get the endpoints that were returned during discovery, i.e., during the GetEndpoints service
   * call.
   *
   * <p>If not empty, and Session endpoint validation is enabled, this list will be compared with
   * the list returned by the Server in the CreateSessionResponse when a Session is created.
   *
   * @return the endpoints that were returned during discovery.
   * @see #isSessionEndpointValidationEnabled()
   */
  List<EndpointDescription> getDiscoveryEndpoints();

  /**
   * Get the {@link KeyPair} to use.
   *
   * <p>May be absent if connecting without security, must be present if connecting with security.
   *
   * @return an {@link Optional} containing the {@link KeyPair} to use.
   */
  Optional<KeyPair> getKeyPair();

  /**
   * Get the {@link X509Certificate} to use.
   *
   * <p>May be absent if connecting without security, must be present if connecting with security.
   *
   * @return an {@link Optional} containing the {@link X509Certificate} to use.
   */
  Optional<X509Certificate> getCertificate();

  /**
   * Get the {@link X509Certificate} to use as well as any certificates in the certificate chain.
   *
   * @return the {@link X509Certificate} to use as well as any certificates in the certificate
   *     chain.
   */
  Optional<X509Certificate[]> getCertificateChain();

  /**
   * Get the {@link CertificateManager} used for policy-aware client certificate selection.
   *
   * @return an {@link Optional} containing the configured {@link CertificateManager}, if any.
   */
  default Optional<CertificateManager> getCertificateManager() {
    return Optional.empty();
  }

  /**
   * Get the selector used with {@link #getCertificateManager()} to choose a local client identity.
   *
   * @return the certificate identity selector.
   */
  default CertificateIdentitySelector getCertificateIdentitySelector() {
    return DefaultCertificateIdentitySelector.create();
  }

  /**
   * Get the requested certificate group for client identity selection.
   *
   * @return the requested certificate group ID, or empty when any group may be selected.
   */
  default Optional<NodeId> getCertificateGroupId() {
    return Optional.empty();
  }

  /**
   * Get the requested certificate type for client identity selection.
   *
   * @return the requested certificate type ID, or empty when the policy should choose.
   */
  default Optional<NodeId> getCertificateTypeId() {
    return Optional.empty();
  }

  /**
   * Get a local certificate identity for the chosen endpoint security policy.
   *
   * @param securityPolicyProfile the selected endpoint security-policy profile.
   * @return the selected identity, or empty when no {@link CertificateManager} is configured or no
   *     identity matches.
   * @throws UaException if the selector fails while evaluating identities.
   */
  default Optional<CertificateIdentity> getCertificateIdentity(
      SecurityPolicyProfile securityPolicyProfile) throws UaException {

    Optional<CertificateManager> certificateManager = getCertificateManager();

    if (certificateManager.isEmpty()) {
      return Optional.empty();
    }

    CertificateIdentitySelectionContext context =
        CertificateIdentitySelectionContext.forClientConnectionSetup(
            certificateManager.get(),
            securityPolicyProfile,
            getCertificateGroupId().orElse(null),
            getCertificateTypeId().orElse(null),
            getCertificate().orElse(null));

    return getCertificateIdentitySelector().select(context);
  }

  /**
   * Get the {@link CertificateValidator} this client will use to validate server certificates when
   * connecting.
   *
   * @return the validator this client will use to validate server certificates when connecting.
   */
  CertificateValidator getCertificateValidator();

  /**
   * @return the name of the client application, as a {@link LocalizedText}.
   */
  LocalizedText getApplicationName();

  /**
   * @return a URI for the client's application instance. This should be the same as the URI in the
   *     client certificate, if present.
   */
  String getApplicationUri();

  /**
   * @return the URI for the client's application product.
   */
  String getProductUri();

  /**
   * @return a {@link Supplier} for the session name.
   */
  Supplier<String> getSessionName();

  /**
   * @return the locale ids in priority order for localized strings.
   */
  String[] getSessionLocaleIds();

  /**
   * @return the session timeout, in milliseconds, to request.
   */
  UInteger getSessionTimeout();

  /**
   * @return the request timeout, in milliseconds.
   */
  UInteger getRequestTimeout();

  /**
   * @return the {@link EncodingLimits} used by this client.
   */
  EncodingLimits getEncodingLimits();

  /**
   * @return the maximum size for a response from the server.
   */
  UInteger getMaxResponseMessageSize();

  /**
   * @return the maximum number of outstanding {@link PublishRequest}s allowed at any given time.
   */
  UInteger getMaxPendingPublishRequests();

  /**
   * @return an {@link IdentityProvider} to use when activating a session.
   */
  IdentityProvider getIdentityProvider();

  /**
   * @return the number of consecutive keep-alive request failures allowed before a connection is
   *     determined to be in error state.
   */
  UInteger getKeepAliveFailuresAllowed();

  /**
   * @return the interval, in milliseconds, between consecutive keep-alive requests.
   */
  UInteger getKeepAliveInterval();

  /**
   * @return the amount of time to wait, in milliseconds, for a keep-alive request before timing
   *     out.
   */
  UInteger getKeepAliveTimeout();

  /**
   * Whether validation of the endpoints returned in CreateSessionResponse against the discovery
   * endpoints is enabled. The discovery endpoints must be configured and non-empty.
   *
   * @return true if validation of the endpoints returned in CreateSessionResponse against the
   *     discovery endpoints is enabled.
   * @see #getDiscoveryEndpoints()
   */
  boolean isSessionEndpointValidationEnabled();

  /**
   * Get the {@link SecurityKeysListener} to be notified when symmetric security keys are derived
   * during OpenSecureChannel handshakes.
   *
   * @return an {@link Optional} containing the {@link SecurityKeysListener}, if configured.
   */
  default Optional<SecurityKeysListener> getSecurityKeysListener() {
    return Optional.empty();
  }

  /**
   * @return a new {@link OpcUaClientConfigBuilder}.
   */
  static OpcUaClientConfigBuilder builder() {
    return new OpcUaClientConfigBuilder();
  }

  /**
   * Copy the values from an existing {@link OpcUaClientConfig} into a new {@link
   * OpcUaClientConfigBuilder}. This builder can be used to make any desired modifications before
   * invoking {@link OpcUaClientConfigBuilder#build()} to produce a new config.
   *
   * @param config the {@link OpcUaClientConfig} to copy from.
   * @return a {@link OpcUaClientConfigBuilder} pre-populated with values from {@code config}.
   */
  static OpcUaClientConfigBuilder copy(OpcUaClientConfig config) {
    OpcUaClientConfigBuilder builder = new OpcUaClientConfigBuilder();

    builder.setEndpoint(config.getEndpoint());
    config.getKeyPair().ifPresent(builder::setKeyPair);
    builder.setDiscoveryEndpoints(new ArrayList<>(config.getDiscoveryEndpoints()));
    config.getCertificate().ifPresent(builder::setCertificate);
    config.getCertificateChain().ifPresent(builder::setCertificateChain);
    config.getCertificateManager().ifPresent(builder::setCertificateManager);
    builder.setCertificateIdentitySelector(config.getCertificateIdentitySelector());
    builder.setCertificateGroupId(config.getCertificateGroupId().orElse(null));
    builder.setCertificateTypeId(config.getCertificateTypeId().orElse(null));
    builder.setApplicationName(config.getApplicationName());
    builder.setApplicationUri(config.getApplicationUri());
    builder.setProductUri(config.getProductUri());
    builder.setSessionName(config.getSessionName());
    builder.setSessionTimeout(config.getSessionTimeout());
    builder.setRequestTimeout(config.getRequestTimeout());
    builder.setMaxResponseMessageSize(config.getMaxResponseMessageSize());
    builder.setMaxPendingPublishRequests(config.getMaxPendingPublishRequests());
    builder.setIdentityProvider(config.getIdentityProvider());
    builder.setKeepAliveFailuresAllowed(config.getKeepAliveFailuresAllowed());
    builder.setKeepAliveInterval(config.getKeepAliveInterval());
    builder.setKeepAliveTimeout(config.getKeepAliveTimeout());
    builder.setSessionLocaleIds(config.getSessionLocaleIds());
    builder.setSessionEndpointValidationEnabled(config.isSessionEndpointValidationEnabled());
    config.getSecurityKeysListener().ifPresent(builder::setSecurityKeysListener);

    return builder;
  }

  /**
   * Copy the values from an existing {@link OpcUaClientConfig} into a new {@link
   * OpcUaClientConfigBuilder} and then submit the builder to the provided consumer for
   * modification.
   *
   * @param config the {@link OpcUaClientConfig} to copy from.
   * @param consumer a {@link Consumer} that may modify the builder.
   * @return a {@link OpcUaClientConfig} built from the builder provided to {@code consumer}.
   */
  static OpcUaClientConfig copy(
      OpcUaClientConfig config, Consumer<OpcUaClientConfigBuilder> consumer) {

    OpcUaClientConfigBuilder builder = copy(config);

    consumer.accept(builder);

    return builder.build();
  }
}
