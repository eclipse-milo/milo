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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * The message security settings in effect for a group or dataset reader after resolving the Part 14
 * inheritance chain: configuration root defaults → group → reader override.
 *
 * <p>Resolution rules (Part 14 §6.2.5.4, §6.2.9.9–6.2.9.11):
 *
 * <ul>
 *   <li>{@code mode} and {@code securityGroup} come from the reader override when one is present,
 *       else from the group; with neither, mode is {@code None} and there is no security group.
 *   <li>{@code keyServices} is the first non-empty of: the active override's key services, the
 *       group's key services, and {@link PubSubConfig#defaultSecurityKeyServices()}; else empty.
 * </ul>
 *
 * <p>Writers have no per-writer override; only the two factories below exist. The Phase 4 runtime
 * resolves security at the group level only ({@link #forGroup}); {@link #forReader} serves
 * key-provider construction and Phase 5.
 *
 * @param mode the effective {@link MessageSecurityMode}.
 * @param securityGroup the effective {@link SecurityGroupRef}, or {@code null} if none is
 *     configured.
 * @param keyServices the effective Security Key Service endpoints; possibly empty.
 */
public record EffectiveMessageSecurity(
    MessageSecurityMode mode,
    @Nullable SecurityGroupRef securityGroup,
    List<EndpointDescription> keyServices) {

  /**
   * Create a new {@link EffectiveMessageSecurity}.
   *
   * @param mode the effective {@link MessageSecurityMode}.
   * @param securityGroup the effective {@link SecurityGroupRef}, or {@code null} if none is
   *     configured.
   * @param keyServices the effective Security Key Service endpoints; possibly empty.
   */
  public EffectiveMessageSecurity {
    keyServices = List.copyOf(keyServices);
  }

  /**
   * Resolve the effective message security for a writer or reader group.
   *
   * @param config the containing {@link PubSubConfig}, providing the root key service defaults.
   * @param group the group's {@link MessageSecurityConfig}, or {@code null} if the group has none.
   * @return the effective message security for the group.
   */
  public static EffectiveMessageSecurity forGroup(
      PubSubConfig config, @Nullable MessageSecurityConfig group) {

    return resolve(config, group, null);
  }

  /**
   * Resolve the effective message security for a dataset reader.
   *
   * @param config the containing {@link PubSubConfig}, providing the root key service defaults.
   * @param group the reader group's {@link MessageSecurityConfig}, or {@code null} if the group has
   *     none.
   * @param readerOverride the reader's {@link MessageSecurityConfig} override, or {@code null} if
   *     the reader inherits from its group.
   * @return the effective message security for the reader.
   */
  public static EffectiveMessageSecurity forReader(
      PubSubConfig config,
      @Nullable MessageSecurityConfig group,
      @Nullable MessageSecurityConfig readerOverride) {

    return resolve(config, group, readerOverride);
  }

  private static EffectiveMessageSecurity resolve(
      PubSubConfig config,
      @Nullable MessageSecurityConfig group,
      @Nullable MessageSecurityConfig readerOverride) {

    MessageSecurityConfig active = readerOverride != null ? readerOverride : group;

    MessageSecurityMode mode = active != null ? active.getMode() : MessageSecurityMode.None;
    SecurityGroupRef securityGroup = active != null ? active.getSecurityGroup() : null;

    List<EndpointDescription> keyServices = List.of();
    if (readerOverride != null && !readerOverride.getKeyServices().isEmpty()) {
      keyServices = readerOverride.getKeyServices();
    } else if (group != null && !group.getKeyServices().isEmpty()) {
      keyServices = group.getKeyServices();
    } else if (!config.defaultSecurityKeyServices().isEmpty()) {
      keyServices = config.defaultSecurityKeyServices();
    }

    return new EffectiveMessageSecurity(mode, securityGroup, keyServices);
  }
}
