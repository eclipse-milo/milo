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
 * Named output contracts for standard Methods with multiple return values.
 *
 * <p>Client calls return these values together, and server handlers return the same model contract.
 * A result interface can preserve covariance when a subtype specializes an inherited Method. These
 * types describe application outputs; a StatusCode output is separate from the Call operation
 * status and its argument diagnostics. Common output consumers do not require either SDK side.
 *
 * <p>The contracts are generated from the standard NodeSet. Change the generator and regenerate the
 * affected contracts when changing their representation.
 */
package org.eclipse.milo.opcua.sdk.core.model.methods;
