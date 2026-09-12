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
 * Generated client node interfaces and implementations for namespace-zero ObjectTypes.
 *
 * <p>AddressSpace constructs these typed nodes after resolving their type definition. Attribute
 * accessors expose local snapshots; explicit read and write methods communicate with the server.
 * Asynchronous property writers return the operation StatusCode. Their blocking counterparts throw
 * UaException for a non-Good operation result, independently of whether the Write service completed
 * normally.
 *
 * <p>Optional node lookup returns null only for confirmed absence. Value access requires the node;
 * a present null value remains valid. Local reads use retained state independently of quality,
 * while remote convenience reads require Good status. Setters do not create missing members.
 *
 * <p>Selected views use {@link com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews} to
 * retain node identity and backing state. Interface views expose one contract on the same node;
 * AddIn views expose composed child Objects. Effective writes validate actual type, rank and
 * dimensions before mutation or Write. Closing the context cancels its owned discovery work and
 * makes further view access fail.
 *
 * <p>The classes are generated from the standard NodeSet. Change the generator and regenerate the
 * affected model classes when modifying their shared accessors.
 */
package org.eclipse.milo.opcua.sdk.client.model.objects;
