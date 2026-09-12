/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.methods.ExtensionFieldsTypeAddExtensionFieldDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ExtensionFieldsTypeAddExtensionFieldHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ExtensionFieldsTypeRemoveExtensionFieldDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.ExtensionFieldsTypeRemoveExtensionFieldHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.2</a>
 */
public interface ExtensionFieldsType extends BaseObjectType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.3
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  MethodNode getAddExtensionFieldMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.3
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindAddExtensionField(
      MethodBindings bindings, ExtensionFieldsTypeAddExtensionFieldHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.3
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindAddExtensionFieldDetailed(
      MethodBindings bindings, ExtensionFieldsTypeAddExtensionFieldDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.4
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  MethodNode getRemoveExtensionFieldMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.4
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindRemoveExtensionField(
      MethodBindings bindings, ExtensionFieldsTypeRemoveExtensionFieldHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.4
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindRemoveExtensionFieldDetailed(
      MethodBindings bindings, ExtensionFieldsTypeRemoveExtensionFieldDetailedHandler handler)
      throws UaException;
}
