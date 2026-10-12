/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Represents values whose data types are described by runtime metadata.
 *
 * <p>Dynamic structures store present members by name; omitting a name differs from storing a null
 * value. Dynamic unions store either no selection or one {@code UnionValue}. A selected member may
 * itself contain a null value. Codecs in the {@code codec} package combine these values with the
 * data type definitions to preserve presence and selection across wire formats.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.core.types;

import org.jspecify.annotations.NullMarked;
