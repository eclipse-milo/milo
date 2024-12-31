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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;

public class EndpointConfig {

  private final TransportProfile transportProfile;
  private final String bindAddress;
  private final int bindPort;
  private final String hostname;
  private final String path;
  private final Supplier<X509Certificate> certificateSupplier;
  private final SecurityPolicy securityPolicy;
  private final MessageSecurityMode securityMode;
  private final List<UserTokenPolicy> tokenPolicies;

  private EndpointConfig(
      TransportProfile transportProfile,
      String bindAddress,
      int bindPort,
      String hostname,
      String path,
      Supplier<X509Certificate> certificateSupplier,
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      List<UserTokenPolicy> tokenPolicies) {

    this.transportProfile = transportProfile;
    this.bindAddress = bindAddress;
    this.bindPort = bindPort;
    this.hostname = hostname;
    this.path = path;
    this.certificateSupplier = certificateSupplier;
    this.securityPolicy = securityPolicy;
    this.securityMode = securityMode;
    this.tokenPolicies = List.copyOf(tokenPolicies);
  }

  public TransportProfile getTransportProfile() {
    return transportProfile;
  }

  public String getBindAddress() {
    return bindAddress;
  }

  public int getBindPort() {
    return bindPort;
  }

  public String getHostname() {
    return hostname;
  }

  public String getPath() {
    return path;
  }

  @Nullable
  public X509Certificate getCertificate() {
    return certificateSupplier.get();
  }

  public SecurityPolicy getSecurityPolicy() {
    return securityPolicy;
  }

  public MessageSecurityMode getSecurityMode() {
    return securityMode;
  }

  public List<UserTokenPolicy> getTokenPolicies() {
    return tokenPolicies;
  }

  public String getEndpointUrl() {
    String scheme = transportProfile.getScheme();
    String p = path.isEmpty() || path.startsWith("/") ? path : "/" + path;

    return String.format("%s://%s:%s%s", scheme, hostname, bindPort, p);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EndpointConfig that = (EndpointConfig) o;
    return bindPort == that.bindPort
        && transportProfile == that.transportProfile
        && Objects.equal(bindAddress, that.bindAddress)
        && Objects.equal(hostname, that.hostname)
        && Objects.equal(path, that.path)
        && Objects.equal(getCertificate(), that.getCertificate())
        && securityPolicy == that.securityPolicy
        && securityMode == that.securityMode
        && Objects.equal(tokenPolicies, that.tokenPolicies);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        transportProfile,
        bindAddress,
        bindPort,
        hostname,
        path,
        getCertificate(),
        securityPolicy,
        securityMode,
        tokenPolicies);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("transportProfile", transportProfile)
        .add("bindAddress", bindAddress)
        .add("bindPort", bindPort)
        .add("hostname", hostname)
        .add("path", path)
        .add("certificate", getCertificate())
        .add("securityPolicy", securityPolicy)
        .add("securityMode", securityMode)
        .add("tokenPolicies", tokenPolicies)
        .toString();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    /** A {@link UserTokenPolicy} for anonymous access. */
    static final UserTokenPolicy USER_TOKEN_POLICY_ANONYMOUS =
        new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

    TransportProfile transportProfile = TransportProfile.TCP_UASC_UABINARY;
    String bindAddress = "localhost";
    int bindPort = Stack.DEFAULT_TCP_PORT;
    String hostname = "localhost";
    String path = "";
    Supplier<X509Certificate> certificateSupplier = () -> null;
    SecurityPolicy securityPolicy = SecurityPolicy.None;
    MessageSecurityMode securityMode = MessageSecurityMode.None;
    List<UserTokenPolicy> tokenPolicies = new ArrayList<>();

    public Builder setTransportProfile(TransportProfile transportProfile) {
      this.transportProfile = transportProfile;
      return this;
    }

    public Builder setBindAddress(String bindAddress) {
      this.bindAddress = bindAddress;
      return this;
    }

    public Builder setBindPort(int bindPort) {
      this.bindPort = bindPort;
      return this;
    }

    public Builder setHostname(String hostname) {
      this.hostname = hostname;
      return this;
    }

    public Builder setPath(String path) {
      this.path = path;
      return this;
    }

    public Builder setCertificate(@Nullable X509Certificate certificate) {
      this.certificateSupplier = () -> certificate;
      return this;
    }

    public Builder setCertificate(Supplier<X509Certificate> certificateSupplier) {
      this.certificateSupplier = certificateSupplier;
      return this;
    }

    public Builder setSecurityPolicy(SecurityPolicy securityPolicy) {
      this.securityPolicy = securityPolicy;
      return this;
    }

    public Builder setSecurityMode(MessageSecurityMode securityMode) {
      this.securityMode = securityMode;
      return this;
    }

    public Builder addTokenPolicy(UserTokenPolicy tokenPolicy) {
      tokenPolicies.add(tokenPolicy);
      return this;
    }

    public Builder addTokenPolicies(UserTokenPolicy... tokenPolicies) {
      Collections.addAll(this.tokenPolicies, tokenPolicies);
      return this;
    }

    private Builder addTokenPolicies(List<UserTokenPolicy> tokenPolicies) {
      this.tokenPolicies.addAll(tokenPolicies);
      return this;
    }

    public Builder copy() {
      return new Builder()
          .setTransportProfile(transportProfile)
          .setBindAddress(bindAddress)
          .setBindPort(bindPort)
          .setHostname(hostname)
          .setPath(path)
          .setCertificate(certificateSupplier)
          .setSecurityPolicy(securityPolicy)
          .setSecurityMode(securityMode)
          .addTokenPolicies(tokenPolicies);
    }

    public EndpointConfig build() {
      if (securityPolicy != SecurityPolicy.None || securityMode != MessageSecurityMode.None) {

        if (securityPolicy == SecurityPolicy.None) {
          throw new IllegalArgumentException("securityPolicy: " + securityPolicy);
        }
        if (securityMode == MessageSecurityMode.None) {
          throw new IllegalArgumentException("securityMode: " + securityMode);
        }
        if (certificateSupplier.get() == null) {
          throw new IllegalStateException("security requires certificate");
        }
      }

      switch (transportProfile) {
        case HTTPS_UAXML:
        case HTTPS_UAJSON:
        case WSS_UASC_UABINARY:
        case WSS_UAJSON:
          throw new IllegalArgumentException("unsupported transport: " + transportProfile);

        default:
          break;
      }

      List<UserTokenPolicy> tokenPolicies = this.tokenPolicies;

      if (tokenPolicies.isEmpty()) {
        tokenPolicies.add(USER_TOKEN_POLICY_ANONYMOUS);
      }

      return new EndpointConfig(
          transportProfile,
          bindAddress,
          bindPort,
          hostname,
          path,
          certificateSupplier,
          securityPolicy,
          securityMode,
          tokenPolicies);
    }
  }
}
