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
 * Encodes and decodes dynamic values using the definitions in a DataType tree.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory} selects a codec for
 * each definition. The encoding context supplies nested codecs, while the tree resolves field types
 * and their inherited builtin representation. Decoded values retain their DataType metadata so they
 * can later be encoded through the same context.
 *
 * <p>Structure definitions determine wire field order and optional-field mask positions. Omitting a
 * value does not change the position of any later optional field. Applications supply members by
 * name; codecs translate those members into the declared wire layout.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.core.types.codec;

import org.jspecify.annotations.NullMarked;
