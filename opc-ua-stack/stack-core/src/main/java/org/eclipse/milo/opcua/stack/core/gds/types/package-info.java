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
 * Generated structured DataTypes of the OPC UA Global Discovery Server namespace.
 *
 * <p>The GDS namespace defines a single structure, {@link
 * org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType}, which describes an
 * application registered with a GDS (OPC 10000-12 clause 6.5.5). It follows the same shape as the
 * namespace 0 structures in {@code org.eclipse.milo.opcua.stack.core.types.structured}: an
 * immutable value class with a nested {@code Codec} and namespace-URI-qualified type and encoding
 * ids. Because the ids are URI-qualified, the codec can only be registered against a {@link
 * org.eclipse.milo.opcua.stack.core.NamespaceTable} that already contains the GDS namespace; see
 * {@link org.eclipse.milo.opcua.stack.core.gds.DataTypeInitializer}.
 *
 * <p>Generated code; see the regeneration notes in {@link org.eclipse.milo.opcua.stack.core.gds}.
 */
package org.eclipse.milo.opcua.stack.core.gds.types;
