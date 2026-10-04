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
 * Executes the Wiki migration examples with one Milo version per Maven project and JVM.
 *
 * <p>Each project owns its local test servers, trust material, and shared-resource cleanup. The
 * JSON fixture exports or imports an explicit Binary interchange directory between the old and
 * current JVMs; the two Milo versions must not share a class loader. The revocation comparison uses
 * an in-memory CA and CRLs without external distribution points. Old-version runs with and without
 * the JDK module opening are separate processes, since that setting affects initialization.
 */
package org.eclipse.milo.examples.migration;
