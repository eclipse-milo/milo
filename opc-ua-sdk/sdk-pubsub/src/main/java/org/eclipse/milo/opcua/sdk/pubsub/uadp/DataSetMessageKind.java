/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.uadp;

/** The kind of a decoded DataSetMessage. */
public enum DataSetMessageKind {

  /** A key frame: carries a value for every field of the dataset. */
  KEY_FRAME,

  /** A delta frame: carries values only for fields that changed, with explicit field indices. */
  DELTA_FRAME,

  /** A keep-alive message: carries no fields. */
  KEEP_ALIVE,

  /** An event message: carries the fields of one event. */
  EVENT
}
