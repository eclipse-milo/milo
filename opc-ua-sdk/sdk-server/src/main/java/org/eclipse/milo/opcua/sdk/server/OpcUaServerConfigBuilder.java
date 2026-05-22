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

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionSecurityDiagnosticsAccessMode;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.reverse.ReverseConnectTarget;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.jspecify.annotations.Nullable;

public class OpcUaServerConfigBuilder {

  private Set<EndpointConfig> endpoints = new HashSet<>();
  private Set<ReverseConnectTarget> reverseConnectTargets = new HashSet<>();

  private LocalizedText applicationName =
      LocalizedText.english("server application name not configured");

  private String applicationUri = "server application uri not configured";

  private String productUri = "server product uri not configured";

  private BuildInfo buildInfo = new BuildInfo("", "", "", "", "", DateTime.MIN_VALUE);

  private IdentityValidator identityValidator = AnonymousIdentityValidator.INSTANCE;

  private EncodingLimits encodingLimits = EncodingLimits.DEFAULT;

  private OpcUaServerConfigLimits limits = new OpcUaServerConfigLimits() {};

  private CertificateManager certificateManager;

  private RoleMapper roleMapper;

  private SessionSecurityDiagnosticsAccessMode sessionSecurityDiagnosticsAccessMode =
      SessionSecurityDiagnosticsAccessMode.RESTRICTED;

  private @Nullable SecurityKeysListener securityKeysListener;

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;

  public OpcUaServerConfigBuilder setEndpoints(Set<EndpointConfig> endpointConfigs) {
    this.endpoints = endpointConfigs;
    return this;
  }

  /**
   * Set the server-side Reverse Connect targets to register when the server starts.
   *
   * <p>The builder copies the supplied set so later changes to {@code reverseConnectTargets} do not
   * affect the built configuration.
   *
   * @param reverseConnectTargets the complete set of initial Reverse Connect targets.
   * @return this builder.
   */
  public OpcUaServerConfigBuilder setReverseConnectTargets(
      Set<ReverseConnectTarget> reverseConnectTargets) {

    Objects.requireNonNull(reverseConnectTargets, "reverseConnectTargets");
    reverseConnectTargets.forEach(t -> Objects.requireNonNull(t, "reverseConnectTargets[i]"));
    this.reverseConnectTargets = new HashSet<>(reverseConnectTargets);
    return this;
  }

  /**
   * Add one server-side Reverse Connect target to the initial server configuration.
   *
   * @param target the target to register when the server starts.
   * @return this builder.
   */
  public OpcUaServerConfigBuilder addReverseConnectTarget(ReverseConnectTarget target) {
    Objects.requireNonNull(target, "target");
    this.reverseConnectTargets.add(target);
    return this;
  }

  public OpcUaServerConfigBuilder setApplicationName(LocalizedText applicationName) {
    this.applicationName = applicationName;
    return this;
  }

  public OpcUaServerConfigBuilder setApplicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
    return this;
  }

  public OpcUaServerConfigBuilder setProductUri(String productUri) {
    this.productUri = productUri;
    return this;
  }

  public OpcUaServerConfigBuilder setBuildInfo(BuildInfo buildInfo) {
    this.buildInfo = buildInfo;
    return this;
  }

  public OpcUaServerConfigBuilder setEncodingLimits(EncodingLimits encodingLimits) {
    this.encodingLimits = encodingLimits;
    return this;
  }

  public OpcUaServerConfigBuilder setLimits(OpcUaServerConfigLimits limits) {
    this.limits = limits;
    return this;
  }

  public OpcUaServerConfigBuilder setIdentityValidator(IdentityValidator identityValidator) {
    this.identityValidator = identityValidator;
    return this;
  }

  public OpcUaServerConfigBuilder setCertificateManager(CertificateManager certificateManager) {
    this.certificateManager = certificateManager;
    return this;
  }

  public OpcUaServerConfigBuilder setRoleMapper(RoleMapper roleMapper) {
    this.roleMapper = roleMapper;
    return this;
  }

  /**
   * Set the authorization mode for Session security diagnostics and the diagnostics enabled flag.
   *
   * @param accessMode the authorization mode.
   * @return this builder.
   */
  public OpcUaServerConfigBuilder setSessionSecurityDiagnosticsAccessMode(
      SessionSecurityDiagnosticsAccessMode accessMode) {

    this.sessionSecurityDiagnosticsAccessMode = requireNonNull(accessMode);
    return this;
  }

  public OpcUaServerConfigBuilder setSecurityKeysListener(
      @Nullable SecurityKeysListener securityKeysListener) {
    this.securityKeysListener = securityKeysListener;
    return this;
  }

  public OpcUaServerConfigBuilder setExecutor(ExecutorService executor) {
    this.executor = executor;
    return this;
  }

  public OpcUaServerConfigBuilder setScheduledExecutor(ScheduledExecutorService scheduledExecutor) {
    this.scheduledExecutor = scheduledExecutor;
    return this;
  }

  public OpcUaServerConfig build() {
    if (executor == null) {
      executor = Stack.sharedExecutor();
    }
    if (scheduledExecutor == null) {
      scheduledExecutor = Stack.sharedScheduledExecutor();
    }

    return new OpcUaServerConfigImpl(
        endpoints,
        reverseConnectTargets,
        applicationName,
        applicationUri,
        productUri,
        buildInfo,
        identityValidator,
        encodingLimits,
        limits,
        certificateManager,
        roleMapper,
        sessionSecurityDiagnosticsAccessMode,
        securityKeysListener,
        executor,
        scheduledExecutor);
  }

  public static final class OpcUaServerConfigImpl implements OpcUaServerConfig {

    private final Set<EndpointConfig> endpoints;
    private final Set<ReverseConnectTarget> reverseConnectTargets;
    private final LocalizedText applicationName;
    private final String applicationUri;
    private final String productUri;
    private final BuildInfo buildInfo;
    private final IdentityValidator identityValidator;
    private final EncodingLimits encodingLimits;
    private final OpcUaServerConfigLimits limits;
    private final CertificateManager certificateManager;
    private final RoleMapper roleMapper;
    private final SessionSecurityDiagnosticsAccessMode sessionSecurityDiagnosticsAccessMode;
    private final @Nullable SecurityKeysListener securityKeysListener;
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutorService;

    public OpcUaServerConfigImpl(
        Set<EndpointConfig> endpoints,
        Set<ReverseConnectTarget> reverseConnectTargets,
        LocalizedText applicationName,
        String applicationUri,
        String productUri,
        BuildInfo buildInfo,
        IdentityValidator identityValidator,
        EncodingLimits encodingLimits,
        OpcUaServerConfigLimits limits,
        CertificateManager certificateManager,
        RoleMapper roleMapper,
        @Nullable SecurityKeysListener securityKeysListener,
        ExecutorService executor,
        ScheduledExecutorService scheduledExecutorService) {

      this(
          endpoints,
          reverseConnectTargets,
          applicationName,
          applicationUri,
          productUri,
          buildInfo,
          identityValidator,
          encodingLimits,
          limits,
          certificateManager,
          roleMapper,
          SessionSecurityDiagnosticsAccessMode.RESTRICTED,
          securityKeysListener,
          executor,
          scheduledExecutorService);
    }

    public OpcUaServerConfigImpl(
        Set<EndpointConfig> endpoints,
        Set<ReverseConnectTarget> reverseConnectTargets,
        LocalizedText applicationName,
        String applicationUri,
        String productUri,
        BuildInfo buildInfo,
        IdentityValidator identityValidator,
        EncodingLimits encodingLimits,
        OpcUaServerConfigLimits limits,
        CertificateManager certificateManager,
        RoleMapper roleMapper,
        SessionSecurityDiagnosticsAccessMode sessionSecurityDiagnosticsAccessMode,
        @Nullable SecurityKeysListener securityKeysListener,
        ExecutorService executor,
        ScheduledExecutorService scheduledExecutorService) {

      this.endpoints = endpoints;
      this.reverseConnectTargets = Set.copyOf(reverseConnectTargets);
      this.applicationName = applicationName;
      this.applicationUri = applicationUri;
      this.productUri = productUri;
      this.buildInfo = buildInfo;
      this.identityValidator = identityValidator;
      this.encodingLimits = encodingLimits;
      this.limits = limits;
      this.certificateManager = certificateManager;
      this.roleMapper = roleMapper;
      this.sessionSecurityDiagnosticsAccessMode =
          requireNonNull(sessionSecurityDiagnosticsAccessMode);
      this.securityKeysListener = securityKeysListener;
      this.executor = executor;
      this.scheduledExecutorService = scheduledExecutorService;
    }

    /**
     * Source-compatibility overload for callers that configure Session security diagnostics but
     * pre-date Reverse Connect target support.
     *
     * <p>Equivalent to the canonical constructor with {@link Set#of()} as the {@code
     * reverseConnectTargets} set.
     */
    public OpcUaServerConfigImpl(
        Set<EndpointConfig> endpoints,
        LocalizedText applicationName,
        String applicationUri,
        String productUri,
        BuildInfo buildInfo,
        IdentityValidator identityValidator,
        EncodingLimits encodingLimits,
        OpcUaServerConfigLimits limits,
        CertificateManager certificateManager,
        RoleMapper roleMapper,
        SessionSecurityDiagnosticsAccessMode sessionSecurityDiagnosticsAccessMode,
        @Nullable SecurityKeysListener securityKeysListener,
        ExecutorService executor,
        ScheduledExecutorService scheduledExecutorService) {

      this(
          endpoints,
          Set.of(),
          applicationName,
          applicationUri,
          productUri,
          buildInfo,
          identityValidator,
          encodingLimits,
          limits,
          certificateManager,
          roleMapper,
          sessionSecurityDiagnosticsAccessMode,
          securityKeysListener,
          executor,
          scheduledExecutorService);
    }

    /**
     * Source-compatibility overload for callers that pre-date Reverse Connect target support.
     *
     * <p>Equivalent to the canonical constructor with {@link Set#of()} as the {@code
     * reverseConnectTargets} set.
     */
    public OpcUaServerConfigImpl(
        Set<EndpointConfig> endpoints,
        LocalizedText applicationName,
        String applicationUri,
        String productUri,
        BuildInfo buildInfo,
        IdentityValidator identityValidator,
        EncodingLimits encodingLimits,
        OpcUaServerConfigLimits limits,
        CertificateManager certificateManager,
        RoleMapper roleMapper,
        @Nullable SecurityKeysListener securityKeysListener,
        ExecutorService executor,
        ScheduledExecutorService scheduledExecutorService) {

      this(
          endpoints,
          Set.of(),
          applicationName,
          applicationUri,
          productUri,
          buildInfo,
          identityValidator,
          encodingLimits,
          limits,
          certificateManager,
          roleMapper,
          securityKeysListener,
          executor,
          scheduledExecutorService);
    }

    @Override
    public IdentityValidator getIdentityValidator() {
      return identityValidator;
    }

    @Override
    public BuildInfo getBuildInfo() {
      return buildInfo;
    }

    @Override
    public Set<EndpointConfig> getEndpoints() {
      return endpoints;
    }

    @Override
    public Set<ReverseConnectTarget> getReverseConnectTargets() {
      return reverseConnectTargets;
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
    public EncodingLimits getEncodingLimits() {
      return encodingLimits;
    }

    @Override
    public OpcUaServerConfigLimits getLimits() {
      return limits;
    }

    @Override
    public CertificateManager getCertificateManager() {
      return certificateManager;
    }

    @Override
    public Optional<RoleMapper> getRoleMapper() {
      return Optional.ofNullable(roleMapper);
    }

    @Override
    public SessionSecurityDiagnosticsAccessMode getSessionSecurityDiagnosticsAccessMode() {
      return sessionSecurityDiagnosticsAccessMode;
    }

    @Override
    public Optional<SecurityKeysListener> getSecurityKeysListener() {
      return Optional.ofNullable(securityKeysListener);
    }

    @Override
    public ExecutorService getExecutor() {
      return executor;
    }

    @Override
    public ScheduledExecutorService getScheduledExecutorService() {
      return scheduledExecutorService;
    }
  }
}
