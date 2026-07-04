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

import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.sdk.server.model.objects.PubSubStatusType;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;

/**
 * The Enable/Disable Method handlers minted by the info model fragment on every component Status
 * object when {@link ServerPubSubOptions#isAllowRemoteConfiguration()} is {@code true} (Part 14
 * §9.1.10; pinned decision R1).
 *
 * <p>Check order (K17.2, adapted): session &rarr; arguments (none) &rarr; authorization via the
 * effective {@link PubSubMethodAuthorizer} (whose code is surfaced verbatim) &rarr; existence
 * &rarr; state rule &rarr; work. Remote-configuration Methods have no spec channel-security
 * minimum, so no channel-mode gate exists; a session-less invocation is rejected with {@code
 * Bad_UserAccessDenied} (the uniform Phase 5 posture, D29).
 *
 * <p>State rules (§9.1.10.2/§9.1.10.3): Enable is rejected with {@code Bad_InvalidState} unless the
 * current State is Disabled; Disable is rejected with {@code Bad_InvalidState} iff the current
 * State is Disabled. (Disable's result-table description reads "The state of the Object is not
 * operational." — a spec typo; the body sentence "The Server shall reject Disable Method calls if
 * the current State is Disabled" governs, so Disable on a Paused or Error component succeeds.)
 *
 * <p>The handlers call {@link PubSubService#enable}/{@link PubSubService#disable} directly (D22)
 * and do no diagnostics counter work — the engine attributes the METHOD cause to those calls
 * regardless of caller (D30). Enable/Disable are <b>not</b> configuration mutations: they never
 * save to a {@link PubSubConfigurationStore} and they do not run through {@link
 * ManagedPubSubService}'s post-apply hooks (R8) — never wire a persistence hook to them.
 *
 * <p>Handles are resolved per invocation through the {@code handleLookup} captured at mint time
 * (name components, never parsed paths — R11). An empty lookup answers {@code Bad_NotFound}; this
 * is reachable only in the window between an engine apply and the fragment rebuild that deletes the
 * node. The state-check-then-call pair is not atomic against concurrent reconfigures or enables;
 * the window is inherent (the engine offers no compare-and-enable) and harmless — the worst case is
 * a redundant enable/disable.
 */
final class PubSubStatusMethods {

  private PubSubStatusMethods() {}

  /** The {@code Enable} handler: rejected unless the current State is Disabled. */
  static final class EnableHandler extends PubSubStatusType.EnableMethod {

    private final PubSubService service;
    private final PubSubMethodAuthorizer authorizer;
    private final Supplier<Optional<PubSubHandle>> handleLookup;

    EnableHandler(
        UaMethodNode node,
        PubSubService service,
        PubSubMethodAuthorizer authorizer,
        Supplier<Optional<PubSubHandle>> handleLookup) {

      super(node);
      this.service = service;
      this.authorizer = authorizer;
      this.handleLookup = handleLookup;
    }

    @Override
    protected void invoke(InvocationContext context) throws UaException {
      PubSubHandle handle = checkedHandle(context, authorizer, handleLookup);

      try {
        if (service.state(handle) != PubSubState.Disabled) {
          throw new UaException(StatusCodes.Bad_InvalidState);
        }
        service.enable(handle);
      } catch (IllegalArgumentException e) {
        // the handle was invalidated between lookup and call
        throw new UaException(StatusCodes.Bad_NotFound);
      }
    }
  }

  /** The {@code Disable} handler: rejected iff the current State is Disabled. */
  static final class DisableHandler extends PubSubStatusType.DisableMethod {

    private final PubSubService service;
    private final PubSubMethodAuthorizer authorizer;
    private final Supplier<Optional<PubSubHandle>> handleLookup;

    DisableHandler(
        UaMethodNode node,
        PubSubService service,
        PubSubMethodAuthorizer authorizer,
        Supplier<Optional<PubSubHandle>> handleLookup) {

      super(node);
      this.service = service;
      this.authorizer = authorizer;
      this.handleLookup = handleLookup;
    }

    @Override
    protected void invoke(InvocationContext context) throws UaException {
      PubSubHandle handle = checkedHandle(context, authorizer, handleLookup);

      try {
        if (service.state(handle) == PubSubState.Disabled) {
          throw new UaException(StatusCodes.Bad_InvalidState);
        }
        service.disable(handle);
      } catch (IllegalArgumentException e) {
        // the handle was invalidated between lookup and call
        throw new UaException(StatusCodes.Bad_NotFound);
      }
    }
  }

  /**
   * The shared session &rarr; authorization &rarr; existence prefix of the K17.2 check order.
   *
   * @return the component's current handle.
   * @throws UaException {@code Bad_UserAccessDenied} for a session-less invocation (D29), the
   *     authorizer's code verbatim on denial, or {@code Bad_NotFound} when the component no longer
   *     exists.
   */
  private static PubSubHandle checkedHandle(
      InvocationContext context,
      PubSubMethodAuthorizer authorizer,
      Supplier<Optional<PubSubHandle>> handleLookup)
      throws UaException {

    Session session =
        context.getSession().orElseThrow(() -> new UaException(StatusCodes.Bad_UserAccessDenied));

    StatusCode checkResult = authorizer.checkConfigure(session);
    if (checkResult.isBad()) {
      throw new UaException(checkResult);
    }

    return handleLookup.get().orElseThrow(() -> new UaException(StatusCodes.Bad_NotFound));
  }
}
