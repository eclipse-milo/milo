/*
 * Copyright (c) 2022 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.IdentityValidator;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.ServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;

public class OpcUaServerConfigBuilder {

    private Set<EndpointConfig> endpoints = new HashSet<>();

    private LocalizedText applicationName = LocalizedText
        .english("server application name not configured");

    private String applicationUri = "server application uri not configured";

    private String productUri = "server product uri not configured";

    private BuildInfo buildInfo = new BuildInfo(
        "",
        "",
        "",
        "",
        "",
        DateTime.MIN_VALUE
    );

    private IdentityValidator<?> identityValidator = AnonymousIdentityValidator.INSTANCE;

    private EncodingLimits encodingLimits = EncodingLimits.DEFAULT;

    private OpcUaServerConfigLimits limits = new OpcUaServerConfigLimits() {};

    private CertificateManager certificateManager;
    private TrustListManager trustListManager;
    private ServerCertificateValidator certificateValidator;

    private ExecutorService executor;
    private ScheduledExecutorService scheduledExecutor;


    public OpcUaServerConfigBuilder setEndpoints(Set<EndpointConfig> endpointConfigs) {
        this.endpoints = endpointConfigs;
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

    public OpcUaServerConfigBuilder setIdentityValidator(IdentityValidator<?> identityValidator) {
        this.identityValidator = identityValidator;
        return this;
    }

    public OpcUaServerConfigBuilder setCertificateManager(CertificateManager certificateManager) {
        this.certificateManager = certificateManager;
        return this;
    }

    public OpcUaServerConfigBuilder setTrustListManager(TrustListManager trustListManager) {
        this.trustListManager = trustListManager;
        return this;
    }

    public OpcUaServerConfigBuilder setCertificateValidator(ServerCertificateValidator certificateValidator) {
        this.certificateValidator = certificateValidator;
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
            applicationName,
            applicationUri,
            productUri,
            buildInfo,
            identityValidator,
            encodingLimits,
            limits,
            certificateManager,
            trustListManager,
            certificateValidator,
            executor,
            scheduledExecutor
        );
    }


    public static final class OpcUaServerConfigImpl implements OpcUaServerConfig {

        private final Set<EndpointConfig> endpoints;
        private final LocalizedText applicationName;
        private final String applicationUri;
        private final String productUri;
        private final BuildInfo buildInfo;
        private final IdentityValidator<?> identityValidator;
        private final EncodingLimits encodingLimits;
        private final OpcUaServerConfigLimits limits;
        private final CertificateManager certificateManager;
        private final TrustListManager trustListManager;
        private final ServerCertificateValidator certificateValidator;
        private final ExecutorService executor;
        private final ScheduledExecutorService scheduledExecutorService;

        public OpcUaServerConfigImpl(
            Set<EndpointConfig> endpoints,
            LocalizedText applicationName,
            String applicationUri,
            String productUri,
            BuildInfo buildInfo,
            IdentityValidator<?> identityValidator,
            EncodingLimits encodingLimits,
            OpcUaServerConfigLimits limits,
            CertificateManager certificateManager,
            TrustListManager trustListManager,
            ServerCertificateValidator certificateValidator,
            ExecutorService executor,
            ScheduledExecutorService scheduledExecutorService
        ) {

            this.endpoints = endpoints;
            this.applicationName = applicationName;
            this.applicationUri = applicationUri;
            this.productUri = productUri;
            this.buildInfo = buildInfo;
            this.identityValidator = identityValidator;
            this.encodingLimits = encodingLimits;
            this.limits = limits;
            this.certificateManager = certificateManager;
            this.trustListManager = trustListManager;
            this.certificateValidator = certificateValidator;
            this.executor = executor;
            this.scheduledExecutorService = scheduledExecutorService;
        }

        @Override
        public IdentityValidator<?> getIdentityValidator() {
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
        public TrustListManager getTrustListManager() {
            return trustListManager;
        }

        @Override
        public ServerCertificateValidator getCertificateValidator() {
            return certificateValidator;
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
