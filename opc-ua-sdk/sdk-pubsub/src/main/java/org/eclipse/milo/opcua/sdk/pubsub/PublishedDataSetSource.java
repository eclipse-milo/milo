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

import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * Publisher-side data source for a PublishedDataSet: pulled for a snapshot of the current field
 * values each publish cycle.
 *
 * <p>Exceptions thrown by a source never cause a missed publish cycle; the affected fields are
 * published with a {@code Bad_InternalError} status and the error is reported via diagnostics.
 */
@FunctionalInterface
public interface PublishedDataSetSource {

  /**
   * Read a snapshot of the current field values of the dataset.
   *
   * @param context the read context describing the dataset and its fields.
   * @return a {@link DataSetSnapshot} of the current field values.
   * @throws UaException if the snapshot could not be read.
   */
  DataSetSnapshot read(PublishedDataSetReadContext context) throws UaException;
}
