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

/**
 * Message mapping settings for a {@link DataSetWriterConfig}: UADP ({@link
 * UadpDataSetWriterSettings}) or JSON ({@link JsonDataSetWriterSettings}).
 *
 * <p>The mapping chosen at the writer level must agree with the mapping chosen by the containing
 * {@link WriterGroupConfig}; this is validated when the full configuration is assembled.
 *
 * <p><b>API evolution:</b> although this interface is sealed, new permitted subtypes may be added
 * in future releases. Adding a permit is source-compatible only for callers whose switches include
 * a {@code default} branch, so switches over the current permits should include one.
 */
public sealed interface DataSetWriterMessageSettings
    permits UadpDataSetWriterSettings, JsonDataSetWriterSettings {}
