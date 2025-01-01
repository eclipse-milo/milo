/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

/**
 * An {@link AddressSpace} fragment that is part of an {@link AddressSpaceComposite}.
 *
 * <p>Fragments have an {@link AddressSpaceFilter} that indicate which service operations for which
 * Nodes can be delegated to them.
 */
public interface AddressSpaceFragment extends AddressSpace {

  /**
   * Get the {@link AddressSpaceFilter} for this {@link AddressSpace}.
   *
   * @return the {@link AddressSpaceFilter} for this {@link AddressSpace}.
   */
  AddressSpaceFilter getFilter();
}
