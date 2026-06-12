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
 * Message mapping settings for a {@link WriterGroupConfig}: UADP ({@link UadpWriterGroupSettings})
 * or JSON ({@link JsonWriterGroupSettings}).
 *
 * <p>The chosen mapping, combined with the connection's transport, determines the transport profile
 * URI derived when mapping to the Part 14 configuration model.
 *
 * <p><b>API evolution:</b> although this interface is sealed, new permitted subtypes may be added
 * in future releases. Adding a permit is source-compatible only for callers whose switches include
 * a {@code default} branch, so switches over the current permits should include one.
 */
public sealed interface WriterGroupMessageSettings
    permits UadpWriterGroupSettings, JsonWriterGroupSettings {}
