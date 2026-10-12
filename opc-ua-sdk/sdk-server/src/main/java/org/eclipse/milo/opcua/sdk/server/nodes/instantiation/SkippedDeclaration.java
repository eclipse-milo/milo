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
 * One declaration occurrence a plan decided not to realize, with a machine-readable reason — "why
 * isn't my node there?" is answerable from the plan, without a debugger.
 *
 * @param declaration the skipped occurrence.
 * @param reason why it was skipped.
 */
public record SkippedDeclaration(InstanceDeclaration declaration, Reason reason) {

  /** Why a declaration occurrence was not realized. */
  public enum Reason {

    /** An Optional declaration the request did not select. */
    OPTIONAL_NOT_SELECTED,

    /** The declaration was explicitly excluded by the request. */
    EXCLUDED,

    /**
     * An ancestor occurrence was omitted, so this occurrence's BrowsePath does not exist — the
     * valid exception to "Mandatory shall exist" (Part 3 §6.4.4.4.2). Never an orphan: nothing
     * beneath an omitted ancestor is created.
     */
    ANCESTOR_OMITTED,

    /**
     * A placeholder extension point ({@code OptionalPlaceholder} / {@code MandatoryPlaceholder});
     * never auto-instantiated — creation goes through placeholder expansion.
     */
    PLACEHOLDER,

    /** An {@code ExposesItsArray} declaration; surfaced but never materialized. */
    EXPOSES_ITS_ARRAY,

    /** A vendor-rule declaration the request did not explicitly select. */
    VENDOR_RULE,

    /**
     * A descendant of a Method realized as {@link PlannedNode.Materialization#SHARE}: the shared
     * Method's existing children serve every instance.
     */
    METHOD_SHARED
  }
}
