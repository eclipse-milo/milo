/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

/**
 * What the instantiated graph is for, per OPC UA Part 3 §6.4.4.3: ordinary instances carry no
 * ModellingRules, while instances created to serve as InstanceDeclarations of a new type must
 * retain them.
 */
public enum InstantiationPurpose {

  /** An ordinary instance: {@code HasModellingRule} references are not replicated. The default. */
  NORMAL_INSTANCE,

  /**
   * An instance created as an InstanceDeclaration for type-authoring workflows: each realized
   * declaration keeps its {@code HasModellingRule} reference.
   */
  INSTANCE_DECLARATION
}
