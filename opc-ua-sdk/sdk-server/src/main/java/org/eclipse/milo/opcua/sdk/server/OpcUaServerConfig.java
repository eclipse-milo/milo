/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;

public interface OpcUaServerConfig {

  /** A {@link UserTokenPolicy} for anonymous access. */
  UserTokenPolicy USER_TOKEN_POLICY_ANONYMOUS =
      new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

  /** A {@link UserTokenPolicy} for username-based access. */
  UserTokenPolicy USER_TOKEN_POLICY_USERNAME =
      new UserTokenPolicy(
          "username", UserTokenType.UserName, null, null, SecurityPolicy.Basic256.getUri());

  UserTokenPolicy USER_TOKEN_POLICY_X509 =
      new UserTokenPolicy(
          "certificate", UserTokenType.Certificate, null, null, SecurityPolicy.Basic256.getUri());

  /**
   * @return the {@link EndpointConfig}s for this server.
   */
  Set<EndpointConfig> getEndpoints();

  /**
   * Get the application name for the server.
   *
   * <p>This will be used in the {@link ApplicationDescription} returned to clients.
   *
   * @return the application name for the server.
   */
  LocalizedText getApplicationName();

  /**
   * Get the application uri for the server.
   *
   * <p>This will be used in the {@link ApplicationDescription} returned to clients. <b>The
   * application uri must match the application uri used on the server's application instance
   * certificate.</b>
   *
   * @return the application uri for the server.
   */
  String getApplicationUri();

  /**
   * Get the product uri for the server.
   *
   * <p>This will be used in the {@link ApplicationDescription} returned to clients.
   *
   * @return the product uri for the server.
   */
  String getProductUri();

  /**
   * @return the server {@link BuildInfo}.
   */
  BuildInfo getBuildInfo();

  /**
   * Get the {@link IdentityValidator} for the server.
   *
   * @return the {@link IdentityValidator} for the server.
   * @see AnonymousIdentityValidator
   * @see UsernameIdentityValidator
   * @see X509IdentityValidator
   * @see CompositeValidator
   */
  IdentityValidator getIdentityValidator();

  /**
   * @return the configured {@link EncodingLimits}.
   */
  EncodingLimits getEncodingLimits();

  /**
   * @return the {@link OpcUaServerConfigLimits}.
   */
  OpcUaServerConfigLimits getLimits();

  /**
   * @return the {@link CertificateManager} for this server.
   */
  CertificateManager getCertificateManager();

  Optional<RoleMapper> getRoleMapper();

  /**
   * @return the {@link ExecutorService} for this server.
   */
  ExecutorService getExecutor();

  /**
   * @return the {@link ScheduledExecutorService} used by the {@link OpcUaServer} being configured.
   */
  ScheduledExecutorService getScheduledExecutorService();

  /**
   * @return a {@link OpcUaServerConfigBuilder}.
   */
  static OpcUaServerConfigBuilder builder() {
    return new OpcUaServerConfigBuilder();
  }

  /**
   * Copy the values from an existing {@link OpcUaServerConfig} into a new {@link
   * OpcUaServerConfigBuilder}. This builder can be used to make any desired modifications before
   * invoking {@link OpcUaServerConfigBuilder#build()} to produce a new config.
   *
   * @param config the {@link OpcUaServerConfig} to copy from.
   * @return a {@link OpcUaServerConfigBuilder} pre-populated with values from {@code config}.
   */
  static OpcUaServerConfigBuilder copy(OpcUaServerConfig config) {
    var builder = new OpcUaServerConfigBuilder();

    builder.setEndpoints(config.getEndpoints());
    builder.setApplicationName(config.getApplicationName());
    builder.setApplicationUri(config.getApplicationUri());
    builder.setProductUri(config.getProductUri());
    builder.setBuildInfo(config.getBuildInfo());
    builder.setEncodingLimits(config.getEncodingLimits());
    builder.setLimits(config.getLimits());
    builder.setIdentityValidator(config.getIdentityValidator());
    builder.setCertificateManager(config.getCertificateManager());
    builder.setExecutor(config.getExecutor());
    builder.setScheduledExecutor(config.getScheduledExecutorService());

    return builder;
  }

  /**
   * Copy the values from an existing {@link OpcUaServerConfig} into a new {@link
   * OpcUaServerConfigBuilder} and then submit the builder to the provided consumer for
   * modification.
   *
   * @param config the {@link OpcUaServerConfig} to copy from.
   * @param consumer a {@link Consumer} that may modify the builder.
   * @return a {@link OpcUaServerConfig} built from the builder provided to {@code consumer}.
   */
  static OpcUaServerConfig copy(
      OpcUaServerConfig config, Consumer<OpcUaServerConfigBuilder> consumer) {
    OpcUaServerConfigBuilder builder = copy(config);

    consumer.accept(builder);

    return builder.build();
  }
}
