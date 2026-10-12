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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Thrown when a TypeDefinition's model cannot be compiled: the type graph admits no correct
 * instantiation (unknown or remote types, cycles, missing hierarchy targets, duplicate sibling
 * BrowseNames). No model — partial or otherwise — is produced; the full diagnostic list, including
 * any warnings gathered before the failure, is carried on the exception.
 */
public class ModelCompilationException extends UaException {

  private final NodeId typeDefinitionId;
  private final List<ModelDiagnostic> diagnostics;

  /**
   * @param typeDefinitionId the type whose compilation failed.
   * @param diagnostics the full diagnostic list; contains at least one error.
   */
  public ModelCompilationException(NodeId typeDefinitionId, List<ModelDiagnostic> diagnostics) {
    super(StatusCodes.Bad_TypeDefinitionInvalid, buildMessage(typeDefinitionId, diagnostics));

    this.typeDefinitionId = typeDefinitionId;
    this.diagnostics = List.copyOf(diagnostics);
  }

  /**
   * @return the type whose compilation failed.
   */
  public NodeId getTypeDefinitionId() {
    return typeDefinitionId;
  }

  /**
   * @return the full diagnostic list gathered during the failed compilation; contains at least one
   *     {@link ModelDiagnostic.Severity#ERROR} entry.
   */
  public List<ModelDiagnostic> getDiagnostics() {
    return diagnostics;
  }

  private static String buildMessage(NodeId typeDefinitionId, List<ModelDiagnostic> diagnostics) {
    long errorCount = diagnostics.stream().filter(ModelDiagnostic::isError).count();

    String firstError =
        diagnostics.stream()
            .filter(ModelDiagnostic::isError)
            .findFirst()
            .map(d -> d.code() + ": " + d.message())
            .orElse("no error diagnostics");

    return "model compilation failed for %s (%d error%s): %s"
        .formatted(typeDefinitionId, errorCount, errorCount == 1 ? "" : "s", firstError);
  }
}
