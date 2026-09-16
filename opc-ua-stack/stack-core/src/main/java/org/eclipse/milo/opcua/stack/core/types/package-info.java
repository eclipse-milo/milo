/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Connects OPC UA data type and encoding ids to codecs used by encoding contexts. {@link
 * org.eclipse.milo.opcua.stack.core.types.DataTypeInitializer} populates standard types;
 * applications can register custom codecs through {@link
 * org.eclipse.milo.opcua.stack.core.types.DataTypeManager}.
 *
 * <p>Registrations also translate encoding ids to data type ids without inspecting value bodies.
 * This lets JSON envelopes identify an opaque Binary or XML body by its data type, while Binary and
 * XML wrappers retain their encoding ids. Custom managers must implement identity lookup to support
 * these conversions; callers cannot infer an unknown association from an opaque body. Superseded
 * encoding associations are excluded from identity lookup, even when their codecs remain available,
 * so conversion cannot relabel an old body with a replacement encoding id.
 *
 * <p>Ordinary registrations belong to the application and can overwrite existing lookups. For
 * temporary types, acquire a registration handle and retain it for as long as values may need
 * encoding or decoding. Matching acquisitions share the same codec instance and mappings; the last
 * handle removes only lookups still owned by that registration. Borrowed application registrations
 * and later replacements survive cleanup.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager} coordinates
 * registration, lookup, and release. A sequence of lookups is not a snapshot. Callers coordinate
 * codec lifetime with node installation and removal, and remain responsible for model definition
 * compatibility. Type dictionaries are registered independently of codec handles.
 */
package org.eclipse.milo.opcua.stack.core.types;
