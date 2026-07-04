/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import java.time.Duration;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * A point-in-time view of the security-key state of a secured writer or reader group, obtained via
 * {@link PubSubService#securityKeyInfo(PubSubHandle)}: token metadata only — never key material.
 * Feeds the Part 14 §9.1.11 {@code SecurityTokenID}/{@code TimeToNextTokenID} LiveValues (Tables
 * 322/331).
 *
 * @param securityGroupId the id of the SecurityGroup as known to the Security Key Service.
 * @param securityPolicyUri the URI of the policy the current keys were generated for, or {@code
 *     null} when the provider did not report one.
 * @param securityTokenId the id of the current security token; token ids start at 1 ({@code
 *     securityKeyInfo} returns {@code null}, not a zero token, before the first successful fetch).
 * @param timeToNextKey the time until the current key is replaced, or {@code null} when unknown or
 *     when the key set is static.
 */
public record SecurityKeyInfo(
    String securityGroupId,
    @Nullable String securityPolicyUri,
    UInteger securityTokenId,
    @Nullable Duration timeToNextKey) {}
