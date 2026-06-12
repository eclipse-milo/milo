/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * Message security configuration for a writer or reader group: the security mode, an optional
 * {@link SecurityGroupRef}, and the key service parameters used to obtain key material.
 *
 * <p>Message security is configuration-only in v1; the runtime rejects enabled groups configured
 * with any mode other than {@link MessageSecurityMode#None}.
 */
public final class MessageSecurityConfig {

  private final MessageSecurityMode mode;
  private final @Nullable SecurityGroupRef securityGroup;
  private final @Nullable String securityPolicyUri;
  private final List<EndpointDescription> keyServices;

  private MessageSecurityConfig(Builder builder) {
    this.mode = builder.mode;
    this.securityGroup = builder.securityGroup;
    this.securityPolicyUri = builder.securityPolicyUri;
    this.keyServices = List.copyOf(builder.keyServices);
  }

  /**
   * Get the security mode applied to NetworkMessages of the group.
   *
   * @return the {@link MessageSecurityMode}.
   */
  public MessageSecurityMode getMode() {
    return mode;
  }

  /**
   * Get the reference to the SecurityGroup that provides key material for the group.
   *
   * @return the {@link SecurityGroupRef}, or {@code null} if not configured.
   */
  public @Nullable SecurityGroupRef getSecurityGroup() {
    return securityGroup;
  }

  /**
   * Get the URI of the security policy used to protect messages of the group.
   *
   * @return the security policy URI, or {@code null} if not configured.
   */
  public @Nullable String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  /**
   * Get the endpoints of the Security Key Services that distribute keys for the group.
   *
   * @return the key service endpoints; possibly empty.
   */
  public List<EndpointDescription> getKeyServices() {
    return keyServices;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this config.
   *
   * @return a new {@link Builder} initialized with the values of this config.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.mode = mode;
    builder.securityGroup = securityGroup;
    builder.securityPolicyUri = securityPolicyUri;
    builder.keyServices.addAll(keyServices);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof MessageSecurityConfig that)) {
      return false;
    }
    return mode == that.mode
        && Objects.equals(securityGroup, that.securityGroup)
        && Objects.equals(securityPolicyUri, that.securityPolicyUri)
        && keyServices.equals(that.keyServices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, securityGroup, securityPolicyUri, keyServices);
  }

  @Override
  public String toString() {
    return "MessageSecurityConfig{mode=%s, securityGroup=%s, securityPolicyUri=%s}"
        .formatted(mode, securityGroup, securityPolicyUri);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link MessageSecurityConfig} instances. */
  public static final class Builder {

    private MessageSecurityMode mode = MessageSecurityMode.None;
    private @Nullable SecurityGroupRef securityGroup;
    private @Nullable String securityPolicyUri;
    private final List<EndpointDescription> keyServices = new ArrayList<>();

    private Builder() {}

    /**
     * Set the security mode applied to NetworkMessages of the group.
     *
     * @param mode the {@link MessageSecurityMode}.
     * @return this {@link Builder}.
     */
    public Builder mode(MessageSecurityMode mode) {
      this.mode = mode;
      return this;
    }

    /**
     * Set the reference to the SecurityGroup that provides key material for the group.
     *
     * @param ref the {@link SecurityGroupRef}.
     * @return this {@link Builder}.
     */
    public Builder securityGroup(SecurityGroupRef ref) {
      this.securityGroup = ref;
      return this;
    }

    /**
     * Set the URI of the security policy used to protect messages of the group.
     *
     * @param securityPolicyUri the security policy URI.
     * @return this {@link Builder}.
     */
    public Builder securityPolicyUri(String securityPolicyUri) {
      this.securityPolicyUri = securityPolicyUri;
      return this;
    }

    /**
     * Set the endpoints of the Security Key Services that distribute keys for the group.
     *
     * @param endpoints the key service endpoints.
     * @return this {@link Builder}.
     */
    public Builder keyServices(List<EndpointDescription> endpoints) {
      this.keyServices.clear();
      this.keyServices.addAll(endpoints);
      return this;
    }

    /**
     * Build a {@link MessageSecurityConfig} from the values configured on this {@link Builder}.
     *
     * @return a new {@link MessageSecurityConfig}.
     */
    public MessageSecurityConfig build() {
      return new MessageSecurityConfig(this);
    }
  }
}
