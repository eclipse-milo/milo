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
 * Bootstrap and lifecycle ownership for the standard and server-specific namespaces.
 *
 * <p>OpcUaServer registers intrinsic constructors before the generated ns0 initializers. The
 * namespace loader then creates the standard node tree with those exact constructors. Generated
 * constructors only forward attributes; OpcUaNamespace initializes server properties and installs
 * per-Object GetMonitoredItems and ResendData behavior after the nodes exist.
 *
 * <p>ConditionRefresh and ConditionRefresh2 remain type-level Method-node adapters. They use shared
 * generated descriptors and delegate session-aware dispatch to the ConditionManager. The
 * SupportsFilteredRetain capability is initialized on the ConditionType declaration because it has
 * no instance modelling rule.
 */
package org.eclipse.milo.opcua.sdk.server.namespaces;
