/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * Authorizes calls to the PubSub-related ns0 Methods handled by {@link ServerPubSub}.
 *
 * <p>Contract: {@link StatusCode#GOOD} means the call is allowed; any bad code denies the call and
 * is surfaced verbatim as the {@code CallMethodResult} status. Channel security-mode checks (e.g.
 * the SignAndEncrypt requirement of {@code GetSecurityKeys}) are <em>not</em> the authorizer's job;
 * they are performed by the Method handlers before the authorizer is consulted.
 *
 * <p>Implementations can base their decision on anything the {@link Session} exposes, e.g. {@link
 * Session#getRoleIds()} (empty iff the server has no {@code RoleMapper} configured), {@link
 * Session#getIdentity()}, or {@link Session#getClientDescription()}.
 *
 * <p>Configured via {@link ServerPubSubOptions.Builder#methodAuthorizer(PubSubMethodAuthorizer)};
 * when none is configured a default posture applies: with a {@code RoleMapper} configured the
 * well-known roles ConfigureAdmin ({@code i=15716}), SecurityKeyServerAdmin ({@code i=25565}), and
 * SecurityKeyServerAccess ({@code i=25603}) are required; without a {@code RoleMapper}, callers are
 * allowed unless a SecurityGroup carries explicit non-empty RolePermissions, in which case access
 * to that group's keys is denied (fail closed on explicit restrictions).
 */
public interface PubSubMethodAuthorizer {

  /**
   * Authorize a PubSub configuration operation.
   *
   * <p>Consulted — with any bad code surfaced verbatim as the Call result — by every
   * remote-configuration Method handler when {@link
   * ServerPubSubOptions#isAllowRemoteConfiguration()} is enabled: the eight {@code
   * PubSubConfiguration} file-model handlers (Open, Close, Read, Write, GetPosition, SetPosition,
   * ReserveIds, CloseAndUpdate — including the read-side methods; the whole file surface is
   * configure-gated) and the Enable/Disable handlers on every component Status object. Also
   * consulted by the diagnostics {@code Reset} handlers — the ns0 root ({@code i=17421}) and every
   * per-component Diagnostics object — whenever {@link ServerPubSubOptions#isDiagnosticsEnabled()}
   * activates the exposure (Part 14 §9.1.11.3: the caller "must be authorized to modify the
   * configuration"), independent of {@code allowRemoteConfiguration}.
   *
   * @param session the {@link Session} the Call arrived on.
   * @return {@link StatusCode#GOOD} to allow the call, or a bad code to deny it.
   */
  StatusCode checkConfigure(Session session);

  /**
   * Authorize a Security Key Service management operation.
   *
   * <p>Consulted by {@code CloseAndUpdate} — once per call, in addition to {@link #checkConfigure}
   * — when the ConfigurationReferences contain SecurityGroup references (Part 14 Table 239 bit 11):
   * denial fails each SecurityGroup reference per-element with the returned code, never the method.
   * The SKS management Methods (AddSecurityGroup, RemoveSecurityGroup, ...) remain unimplemented.
   *
   * @param session the {@link Session} the Call arrived on.
   * @return {@link StatusCode#GOOD} to allow the call, or a bad code to deny it.
   */
  StatusCode checkSksAdmin(Session session);

  /**
   * Authorize access to the key material of one SecurityGroup, i.e. a {@code GetSecurityKeys} call.
   *
   * <p>Called before the existence of {@code securityGroupId} is checked: implementations should
   * take care that an unauthorized caller cannot distinguish an unknown SecurityGroup from a known
   * but denied one.
   *
   * @param session the {@link Session} the Call arrived on.
   * @param securityGroupId the id of the SecurityGroup whose keys are requested; never null or
   *     empty.
   * @return {@link StatusCode#GOOD} to allow the call, or a bad code to deny it.
   */
  StatusCode checkKeyAccess(Session session, String securityGroupId);
}
