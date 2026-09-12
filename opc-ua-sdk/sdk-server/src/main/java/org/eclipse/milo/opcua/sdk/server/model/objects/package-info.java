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
 * Generated server contracts, nodes and selected views for standard ObjectTypes.
 *
 * <p>Node classes retain server-managed state. Optional node access returns null only for confirmed
 * absence; value getters and setters require an existing member. Local value access is independent
 * of quality, and setters do not create missing children. Construction code can create properties
 * explicitly through UaNode with generated QualifiedProperty metadata.
 *
 * <p>Selected views are constructed through {@link
 * com.digitalpetri.opcua.uanodeset.runtime.server.ServerViews}. Interface selection exposes one
 * contract on the same node; AddIn selection exposes a composed child Object. Views share the
 * actual backing state and validate effective type, rank and dimensions before mutation. A
 * HasInterface reference does not change the applying concrete type's Java inheritance.
 *
 * <p>Method binding uses an explicitly supplied registry and lifetime token. Closing a view closes
 * its observation context, not separately owned bindings. Shared generated behavior belongs in the
 * generator; regenerate the standard model after changing it.
 */
package org.eclipse.milo.opcua.sdk.server.model.objects;
