/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Publishes legacy OPC Binary Schema dictionaries for server namespaces.
 *
 * <p>Applications register the dictionary namespace URI before starting {@link
 * org.eclipse.milo.opcua.sdk.server.dtd.BinaryDataTypeDictionaryManager}. The manager creates the
 * dictionary Variable and its NamespaceUri Property in the supplied node manager. Registered
 * structures contribute schema entries and description and encoding nodes. The dictionary Value is
 * serialized on demand; shutdown removes the manager's nodes and their children.
 */
@Deprecated(forRemoval = true, since = "1.0.0")
package org.eclipse.milo.opcua.sdk.server.dtd;
