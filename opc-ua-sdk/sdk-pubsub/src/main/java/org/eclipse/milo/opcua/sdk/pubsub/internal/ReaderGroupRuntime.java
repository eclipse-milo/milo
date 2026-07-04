/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.DataSetReaderConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MessageSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.ReaderGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupRef;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * Runtime for one ReaderGroup: holds the DataSetReader runtimes and ensures the connection's
 * subscriber channel is open while the group is active.
 */
final class ReaderGroupRuntime extends AbstractComponentRuntime {

  private final PubSubServiceImpl service;
  private final ConnectionRuntime connection;
  private final ReaderGroupConfig config;

  /** Whether this group's configured mode is Sign or SignAndEncrypt (Invalid counts as None). */
  private final boolean secured;

  /** The SecurityGroup providing this group's keys; non-null when {@link #secured}. */
  private final @Nullable SecurityGroupRef securityGroupRef;

  private volatile List<DataSetReaderRuntime> readers;

  ReaderGroupRuntime(
      PubSubServiceImpl service, ConnectionRuntime connection, ReaderGroupConfig config) {

    super(
        ComponentType.READER_GROUP,
        connection.path() + "/" + config.getName(),
        connection,
        config.isEnabled());

    this.service = service;
    this.connection = connection;
    this.config = config;

    MessageSecurityConfig security = config.getMessageSecurity();
    this.secured = PubSubServiceImpl.isSecured(security);
    this.securityGroupRef = security != null && secured ? security.getSecurityGroup() : null;

    var readers = new ArrayList<DataSetReaderRuntime>();
    for (DataSetReaderConfig readerConfig : config.getDataSetReaders()) {
      readers.add(new DataSetReaderRuntime(service, connection, this, readerConfig));
    }
    this.readers = List.copyOf(readers);
  }

  ReaderGroupConfig config() {
    return config;
  }

  List<DataSetReaderRuntime> readerRuntimes() {
    return readers;
  }

  @Override
  List<? extends AbstractComponentRuntime> children() {
    return readers;
  }

  /**
   * Whether startup completes as soon as {@link #activate()} returns: a secured group stays {@code
   * PreOperational} — its readers Paused, so nothing is processed — until the {@link
   * SecurityKeyManager} completes its first key fetch.
   */
  @Override
  boolean startupCompletesImmediately() {
    return !secured;
  }

  @Override
  void activate() throws UaException {
    checkMessageSecurity();

    connection.ensureSubscriberChannel();

    if (secured) {
      MessageSecurityConfig security = config.getMessageSecurity();
      SecurityGroupRef ref = securityGroupRef;
      if (security == null || ref == null) {
        // unreachable: checkMessageSecurity() rejects a secured group without a ref
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "secured reader group '%s' has no SecurityGroup reference".formatted(path()));
      }
      // non-blocking: the fetch runs on the scheduler and completes startup from its callback
      service
          .getSecurityKeyManager()
          .attachSubscriber(this, security, service.requireSecurityGroup(ref));
    }
  }

  @Override
  void deactivate() {
    if (secured && securityGroupRef != null) {
      // reconfigure and dispose both flow through deactivate, so detach is covered everywhere
      service.getSecurityKeyManager().detach(this, securityGroupRef);
    }
  }

  /**
   * The current token id and time to the next key switch of this group's SecurityGroup, or {@code
   * null} when the group is not secured (or not attached): the source for the future Part 14
   * §9.1.11 {@code SecurityTokenID}/{@code TimeToNextTokenID} LiveValues.
   */
  SecurityKeyManager.@Nullable SecurityGroupKeyView securityKeyView() {
    SecurityGroupRef ref = securityGroupRef;
    return ref != null ? service.getSecurityKeyManager().view(ref) : null;
  }

  /** Release all resources of this runtime. The runtime is unusable afterwards. */
  void dispose() {
    readers.forEach(DataSetReaderRuntime::dispose);
  }

  void addReaderRuntime(DataSetReaderRuntime reader) {
    var readers = new ArrayList<>(this.readers);
    readers.add(reader);
    this.readers = List.copyOf(readers);
  }

  void removeReaderRuntime(DataSetReaderRuntime reader) {
    var readers = new ArrayList<>(this.readers);
    readers.remove(reader);
    this.readers = List.copyOf(readers);
  }

  @Nullable DataSetReaderRuntime findReaderRuntime(String name) {
    for (DataSetReaderRuntime reader : readers) {
      if (reader.config().getName().equals(name)) {
        return reader;
      }
    }
    return null;
  }

  /**
   * Activation-time copy of the startup/reconfigure message security gate ({@link
   * PubSubServiceImpl#checkReaderGroupMessageSecurity}), covering groups enabled after startup: the
   * state machine maps the throw to {@code PubSubState.Error} with the {@code
   * Bad_ConfigurationError} status.
   */
  private void checkMessageSecurity() throws UaException {
    try {
      service.checkReaderGroupMessageSecurity(config, path());
    } catch (UaException e) {
      service
          .getDiagnostics()
          .error(
              path(),
              e.getStatusCode(),
              e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      throw e;
    }
  }
}
