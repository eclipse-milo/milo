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

import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics.ComponentDiagnostics;

/**
 * Identity of one per-component diagnostic counter, mirroring the counter components of {@link
 * ComponentDiagnostics} (see each record component for its exact tick sites and semantics).
 *
 * <p>Used as the key of {@link ComponentDiagnostics#timeFirstChange(PubSubCounter)} and as the
 * stable counter identity for information-model exposure.
 */
public enum PubSubCounter {
  /** See {@link ComponentDiagnostics#networkMessagesSent()}. */
  NETWORK_MESSAGES_SENT,
  /** See {@link ComponentDiagnostics#networkMessagesReceived()}. */
  NETWORK_MESSAGES_RECEIVED,
  /** See {@link ComponentDiagnostics#dataSetMessagesSent()}. */
  DATA_SET_MESSAGES_SENT,
  /** See {@link ComponentDiagnostics#dataSetMessagesReceived()}. */
  DATA_SET_MESSAGES_RECEIVED,
  /** See {@link ComponentDiagnostics#decodeErrors()}. */
  DECODE_ERRORS,
  /** See {@link ComponentDiagnostics#sourceErrors()}. */
  SOURCE_ERRORS,
  /** See {@link ComponentDiagnostics#staleSequenceMessages()}. */
  STALE_SEQUENCE_MESSAGES,
  /** See {@link ComponentDiagnostics#invalidSequenceMessages()}. */
  INVALID_SEQUENCE_MESSAGES,
  /** See {@link ComponentDiagnostics#encryptionErrors()}. */
  ENCRYPTION_ERRORS,
  /** See {@link ComponentDiagnostics#decryptionErrors()}. */
  DECRYPTION_ERRORS,
  /** See {@link ComponentDiagnostics#invalidSignatureMessages()}. */
  INVALID_SIGNATURE_MESSAGES,
  /** See {@link ComponentDiagnostics#unknownTokenMessages()}. */
  UNKNOWN_TOKEN_MESSAGES,
  /** See {@link ComponentDiagnostics#staleKeyMessages()}. */
  STALE_KEY_MESSAGES,
  /** See {@link ComponentDiagnostics#failedTransmissions()}. */
  FAILED_TRANSMISSIONS,
  /** See {@link ComponentDiagnostics#failedDataSetMessages()}. */
  FAILED_DATA_SET_MESSAGES,
  /** See {@link ComponentDiagnostics#stateError()}. */
  STATE_ERROR,
  /** See {@link ComponentDiagnostics#stateOperationalByMethod()}. */
  STATE_OPERATIONAL_BY_METHOD,
  /** See {@link ComponentDiagnostics#stateOperationalByParent()}. */
  STATE_OPERATIONAL_BY_PARENT,
  /** See {@link ComponentDiagnostics#stateOperationalFromError()}. */
  STATE_OPERATIONAL_FROM_ERROR,
  /** See {@link ComponentDiagnostics#statePausedByParent()}. */
  STATE_PAUSED_BY_PARENT,
  /** See {@link ComponentDiagnostics#stateDisabledByMethod()}. */
  STATE_DISABLED_BY_METHOD
}
