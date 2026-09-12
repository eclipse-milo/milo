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
 * Generated server contracts, nodes and selected views for standard VariableTypes.
 *
 * <p>Optional node getters can report confirmed absence. Value access requires the member and
 * preserves a present null value. Local reads are independent of quality; raw DataValue access
 * exposes status and timestamps. Generated setters update existing nodes and preserve decoding or
 * validation failures.
 *
 * <p>Selected VariableType views retain generic backing Variable nodes through {@link
 * com.digitalpetri.opcua.uanodeset.runtime.server.ServerViews}. They expose the selected contract
 * without casting to a concrete generated node class. Writes prove the actual Variable's current
 * DataType, ValueRank and dimensions before mutation.
 *
 * <p>These classes are generated from the standard NodeSet. Change the generator and regenerate the
 * affected model classes when changing their shared accessors.
 */
package org.eclipse.milo.opcua.sdk.server.model.variables;
