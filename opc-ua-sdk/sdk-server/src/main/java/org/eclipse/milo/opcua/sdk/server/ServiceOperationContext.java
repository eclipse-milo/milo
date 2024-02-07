/*
 * Copyright (c) 2023 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.Optional;

import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jetbrains.annotations.Nullable;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class ServiceOperationContext<T> implements AccessContext {

    private final Session session;
    private final DiagnosticsContext<T> diagnosticsContext;

    private final String auditEntryId;

    private final UInteger timeoutHint;

    private final ExtensionObject additionalHeader;

    public ServiceOperationContext(@Nullable Session session) {
        this(session, new DiagnosticsContext<>(), "", uint(0), null);
    }

    public ServiceOperationContext(
        @Nullable Session session,
        DiagnosticsContext<T> diagnosticsContext,
        @Nullable String auditEntryId,
        UInteger timeoutHint,
        ExtensionObject additionalHeader
    ) {

        this.session = session;
        this.diagnosticsContext = diagnosticsContext;
        this.auditEntryId = auditEntryId;
        this.timeoutHint = timeoutHint;
        this.additionalHeader = additionalHeader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Session> getSession() {
        return Optional.ofNullable(session);
    }

    public DiagnosticsContext<T> getDiagnosticsContext() {
        return diagnosticsContext;
    }

    public @Nullable String getAuditEntryId() {
        return auditEntryId;
    }

    public UInteger getTimeoutHint() {
        return timeoutHint;
    }

    public ExtensionObject getAdditionalHeader() {
        return additionalHeader;
    }

}