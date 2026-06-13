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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * Configuration of a PubSub SecurityGroup: the security policy and key management parameters shared
 * by the writer and reader groups that reference it via {@link SecurityGroupRef}.
 *
 * <p>Message security is configuration-only in v1; the runtime rejects enabled groups configured
 * with any {@code MessageSecurityMode} other than {@code None}.
 */
public final class SecurityGroupConfig {

  private final String name;
  private final String securityGroupId;
  private final @Nullable String securityPolicyUri;
  private final Duration keyLifeTime;
  private final UInteger maxFutureKeyCount;
  private final UInteger maxPastKeyCount;
  private final List<EndpointDescription> keyServices;
  private final Map<QualifiedName, Variant> properties;

  private SecurityGroupConfig(Builder builder, String securityGroupId) {
    this.name = builder.name;
    this.securityGroupId = securityGroupId;
    this.securityPolicyUri = builder.securityPolicyUri;
    this.keyLifeTime = builder.keyLifeTime;
    this.maxFutureKeyCount = builder.maxFutureKeyCount;
    this.maxPastKeyCount = builder.maxPastKeyCount;
    this.keyServices = List.copyOf(builder.keyServices);
    this.properties = Map.copyOf(builder.properties);
  }

  /**
   * Get the name of this SecurityGroup, unique among the SecurityGroups of a {@code PubSubConfig}.
   *
   * @return the name of this SecurityGroup.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the id of this SecurityGroup as known to the Security Key Service; defaults to {@link
   * #getName()} when not explicitly configured.
   *
   * @return the SecurityGroup id.
   */
  public String getSecurityGroupId() {
    return securityGroupId;
  }

  /**
   * Get the URI of the security policy used to protect messages of groups referencing this
   * SecurityGroup.
   *
   * @return the security policy URI, or {@code null} if not configured.
   */
  public @Nullable String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  /**
   * Get the lifetime of a key before it is replaced by the next key in the key rotation.
   *
   * @return the key lifetime.
   */
  public Duration getKeyLifeTime() {
    return keyLifeTime;
  }

  /**
   * Get the maximum number of future keys returned by the Security Key Service.
   *
   * @return the maximum future key count.
   */
  public UInteger getMaxFutureKeyCount() {
    return maxFutureKeyCount;
  }

  /**
   * Get the maximum number of historical keys stored by the Security Key Service.
   *
   * @return the maximum past key count.
   */
  public UInteger getMaxPastKeyCount() {
    return maxPastKeyCount;
  }

  /**
   * Get the endpoints of the Security Key Services that distribute keys for this SecurityGroup.
   *
   * @return the key service endpoints; possibly empty.
   */
  public List<EndpointDescription> getKeyServices() {
    return keyServices;
  }

  /**
   * Get the custom properties of this SecurityGroup, mapped to and from the {@code groupProperties}
   * KeyValuePairs of the Part 14 datatype.
   *
   * @return the custom properties; possibly empty.
   */
  public Map<QualifiedName, Variant> getProperties() {
    return properties;
  }

  /**
   * Get a {@link SecurityGroupRef} that refers to this SecurityGroup by name.
   *
   * @return a {@link SecurityGroupRef} referring to this SecurityGroup.
   */
  public SecurityGroupRef ref() {
    return new SecurityGroupRef(name);
  }

  /**
   * Create a new {@link Builder} initialized with the values of this config.
   *
   * @return a new {@link Builder} initialized with the values of this config.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.securityGroupId = securityGroupId;
    builder.securityPolicyUri = securityPolicyUri;
    builder.keyLifeTime = keyLifeTime;
    builder.maxFutureKeyCount = maxFutureKeyCount;
    builder.maxPastKeyCount = maxPastKeyCount;
    builder.keyServices.addAll(keyServices);
    builder.properties.putAll(properties);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SecurityGroupConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && securityGroupId.equals(that.securityGroupId)
        && Objects.equals(securityPolicyUri, that.securityPolicyUri)
        && keyLifeTime.equals(that.keyLifeTime)
        && maxFutureKeyCount.equals(that.maxFutureKeyCount)
        && maxPastKeyCount.equals(that.maxPastKeyCount)
        && keyServices.equals(that.keyServices)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        securityGroupId,
        securityPolicyUri,
        keyLifeTime,
        maxFutureKeyCount,
        maxPastKeyCount,
        keyServices,
        properties);
  }

  @Override
  public String toString() {
    return "SecurityGroupConfig{name=%s, securityGroupId=%s, securityPolicyUri=%s}"
        .formatted(name, securityGroupId, securityPolicyUri);
  }

  /**
   * Create a new {@link Builder} for a SecurityGroup with the given name.
   *
   * @param name the name of the SecurityGroup.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link SecurityGroupConfig} instances. */
  public static final class Builder {

    private final String name;
    private @Nullable String securityGroupId;
    private @Nullable String securityPolicyUri;
    private Duration keyLifeTime = Duration.ofHours(1);
    private UInteger maxFutureKeyCount = uint(0);
    private UInteger maxPastKeyCount = uint(0);
    private final List<EndpointDescription> keyServices = new ArrayList<>();
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set the id of this SecurityGroup as known to the Security Key Service.
     *
     * @param securityGroupId the SecurityGroup id.
     * @return this {@link Builder}.
     */
    public Builder securityGroupId(String securityGroupId) {
      this.securityGroupId = securityGroupId;
      return this;
    }

    /**
     * Set the URI of the security policy used to protect messages of groups referencing this
     * SecurityGroup.
     *
     * @param securityPolicyUri the security policy URI.
     * @return this {@link Builder}.
     */
    public Builder securityPolicyUri(String securityPolicyUri) {
      this.securityPolicyUri = securityPolicyUri;
      return this;
    }

    /**
     * Set the lifetime of a key before it is replaced by the next key in the key rotation.
     *
     * @param keyLifeTime the key lifetime.
     * @return this {@link Builder}.
     */
    public Builder keyLifeTime(Duration keyLifeTime) {
      this.keyLifeTime = keyLifeTime;
      return this;
    }

    /**
     * Set the maximum number of future keys returned by the Security Key Service.
     *
     * @param count the maximum future key count.
     * @return this {@link Builder}.
     */
    public Builder maxFutureKeyCount(UInteger count) {
      this.maxFutureKeyCount = count;
      return this;
    }

    /**
     * Set the maximum number of historical keys stored by the Security Key Service.
     *
     * @param count the maximum past key count.
     * @return this {@link Builder}.
     */
    public Builder maxPastKeyCount(UInteger count) {
      this.maxPastKeyCount = count;
      return this;
    }

    /**
     * Add the endpoint of a Security Key Service that distributes keys for this SecurityGroup.
     *
     * @param endpoint the key service endpoint to add.
     * @return this {@link Builder}.
     */
    public Builder keyService(EndpointDescription endpoint) {
      this.keyServices.add(endpoint);
      return this;
    }

    /**
     * Add a custom property, mapped to and from the {@code groupProperties} KeyValuePairs of the
     * Part 14 datatype.
     *
     * @param name the property name.
     * @param value the property value.
     * @return this {@link Builder}.
     */
    public Builder property(QualifiedName name, Variant value) {
      this.properties.put(name, value);
      return this;
    }

    /**
     * Build a {@link SecurityGroupConfig} from the values configured on this {@link Builder}.
     *
     * @return a new {@link SecurityGroupConfig}.
     * @throws PubSubConfigValidationException if the configured values are invalid.
     */
    public SecurityGroupConfig build() {
      if (name.isEmpty()) {
        throw new PubSubConfigValidationException("SecurityGroupConfig: name must be non-empty");
      }
      if (keyLifeTime.isNegative()) {
        throw new PubSubConfigValidationException(
            "SecurityGroupConfig '%s': keyLifeTime must not be negative".formatted(name));
      }

      String securityGroupId = this.securityGroupId;
      if (securityGroupId == null || securityGroupId.isEmpty()) {
        securityGroupId = name;
      }

      return new SecurityGroupConfig(this, securityGroupId);
    }
  }
}
