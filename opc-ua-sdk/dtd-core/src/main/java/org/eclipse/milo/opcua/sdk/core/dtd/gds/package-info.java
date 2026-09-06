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
 * Legacy DataTypeDictionary registration for the OPC UA Global Discovery Server namespace.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.core.dtd.gds.BinaryDataTypeDictionaryInitializer} adds the
 * {@code ApplicationRecordDataType} entry to the OPC UA Binary type dictionary, for applications
 * that still exchange type information through OPC UA 1.03-style dictionaries instead of the
 * DataTypeDefinition attribute. Like the rest of {@code org.eclipse.milo.opcua.sdk.core.dtd} it is
 * deprecated for removal; new code should register the codec through {@link
 * org.eclipse.milo.opcua.stack.core.gds.DataTypeInitializer} instead.
 *
 * <p>Generated code, copied from the {@code gds-model-core} module of <a
 * href="https://github.com/kevinherron/opc-ua-gds-model">opc-ua-gds-model</a> at commit {@code
 * 297d894}; see the regeneration notes in {@link org.eclipse.milo.opcua.stack.core.gds}.
 */
@Deprecated(forRemoval = true, since = "1.2.0")
package org.eclipse.milo.opcua.sdk.core.dtd.gds;
