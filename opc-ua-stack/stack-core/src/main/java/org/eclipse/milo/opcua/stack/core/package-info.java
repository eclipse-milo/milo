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
 * Core OPC UA types shared by the stack and both SDKs: status codes and the exceptions that carry
 * them, well-known identifiers, the namespace and server tables, and the diagnostics that explain a
 * status.
 *
 * <h2>Diagnostics</h2>
 *
 * <p>A diagnostic explains a status code with a vendor-specific symbolic id, text for the user,
 * additional information, and the status of an inner operation. On the wire it is a {@code
 * DiagnosticInfo} (OPC 10000-4, 7.12), which holds indexes into the string table of the response
 * that carries it instead of the strings themselves. Every {@code DiagnosticInfo} in a response
 * shares that one table: the service-level diagnostic in the response header, the per-operation
 * lists, and the lists nested in operation results.
 *
 * <p>Applications never handle the indexes. They work with {@link
 * org.eclipse.milo.opcua.stack.core.Diagnostic}, which holds the resolved strings. Each end of a
 * request converts between the two forms once per response:
 *
 * <ol>
 *   <li>The client asks for diagnostics with the {@link
 *       org.eclipse.milo.opcua.stack.core.ReturnDiagnostics} mask of each request header. The mask
 *       names the fields wanted at the service level and at the operation level. A server returns
 *       only the fields that were requested.
 *   <li>Server code attaches a {@code Diagnostic} to the failure it explains, usually by throwing a
 *       {@link org.eclipse.milo.opcua.stack.core.UaException} that carries one. The server SDK
 *       passes every diagnostic of a response through one {@link
 *       org.eclipse.milo.opcua.stack.core.DiagnosticEncoder}, which applies the mask and the limits
 *       of the specification and builds the response's string table.
 *   <li>The client resolves the response with one {@link
 *       org.eclipse.milo.opcua.stack.core.ResponseDiagnostics}, created from the response header
 *       and used for every list in that response. {@link
 *       org.eclipse.milo.opcua.stack.core.UaServiceFaultException} resolves the service-level
 *       diagnostic of a ServiceFault this way, so its {@code getDiagnostic()} returns the server's
 *       explanation when the server sent one.
 * </ol>
 *
 * <h2>Ownership</h2>
 *
 * <p>This package defines the diagnostic values and the conversion, and enforces the rules that
 * apply to every response: the requested fields, the limits on text length and nesting depth, and a
 * byte budget for the diagnostics of one response. Decisions that depend on the server or the
 * Session belong to the server SDK: which diagnostics to report, which to withhold because they
 * reveal security-related information, and how large a budget the Session's message limits allow.
 *
 * <p>An exception's message never becomes a diagnostic. Write diagnostic text for the client, not
 * for the server log.
 */
package org.eclipse.milo.opcua.stack.core;
