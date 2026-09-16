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

import java.util.List;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * Thrown when an instantiation cannot proceed: a plan carrying errors was handed to apply, or apply
 * itself failed at one of its stages. The failure names its {@link InstantiationDiagnostic.Phase}
 * and carries the structured {@link InstantiationDiagnostic}s that describe it — a failed
 * instantiation is explainable per declaration path, not a blanket status code.
 *
 * <p>Apply-stage failures leave no residue in the target: staging happens before any storage
 * mutation, and a failed commit — or a failed post-commit method binder — is rolled back to exactly
 * its journaled additions. The exceptions to that guarantee are a failed rollback, reported loudly
 * with {@link InstantiationDiagnostic.Code#ROLLBACK_FAILED} diagnostics carrying what could not be
 * removed, and a mutation a binder made to a reused or shared node before another binder failed,
 * which is not this instantiation's to undo.
 */
public class InstantiationException extends UaException {

  private final InstantiationDiagnostic.Phase phase;
  private final List<InstantiationDiagnostic> diagnostics;

  /**
   * @param statusCode the {@link StatusCode} summarizing the failure.
   * @param phase the lifecycle phase the failure occurred in.
   * @param diagnostics the findings describing the failure; contains at least one error.
   * @param cause the causing exception, if any.
   */
  public InstantiationException(
      long statusCode,
      InstantiationDiagnostic.Phase phase,
      List<InstantiationDiagnostic> diagnostics,
      @Nullable Throwable cause) {

    super(statusCode, buildMessage(phase, diagnostics), cause);

    this.phase = phase;
    this.diagnostics = List.copyOf(diagnostics);
  }

  /**
   * @return the lifecycle phase the failure occurred in.
   */
  public InstantiationDiagnostic.Phase getPhase() {
    return phase;
  }

  /**
   * @return the findings describing the failure; contains at least one {@link
   *     InstantiationDiagnostic.Severity#ERROR} entry.
   */
  public List<InstantiationDiagnostic> getDiagnostics() {
    return diagnostics;
  }

  private static String buildMessage(
      InstantiationDiagnostic.Phase phase, List<InstantiationDiagnostic> diagnostics) {

    long errorCount = diagnostics.stream().filter(InstantiationDiagnostic::isError).count();

    String firstError =
        diagnostics.stream()
            .filter(InstantiationDiagnostic::isError)
            .findFirst()
            .map(d -> d.code() + ": " + d.message())
            .orElse("no error diagnostics");

    return "%s failed (%d error%s): %s"
        .formatted(
            phase == InstantiationDiagnostic.Phase.PLAN ? "plan" : "apply",
            errorCount,
            errorCount == 1 ? "" : "s",
            firstError);
  }
}
