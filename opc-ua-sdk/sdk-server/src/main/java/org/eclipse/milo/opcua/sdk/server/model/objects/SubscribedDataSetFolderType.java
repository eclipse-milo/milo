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
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeAddDataSetFolderDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeAddDataSetFolderHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeAddSubscribedDataSetDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeAddSubscribedDataSetHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeRemoveDataSetFolderDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeRemoveDataSetFolderHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeRemoveSubscribedDataSetDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.SubscribedDataSetFolderTypeRemoveSubscribedDataSetHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1</a>
 */
public interface SubscribedDataSetFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.2
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getAddSubscribedDataSetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.2
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
  MethodBinding bindAddSubscribedDataSet(
      MethodBindings bindings, SubscribedDataSetFolderTypeAddSubscribedDataSetHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.2
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
  MethodBinding bindAddSubscribedDataSetDetailed(
      MethodBindings bindings,
      SubscribedDataSetFolderTypeAddSubscribedDataSetDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.3
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getRemoveSubscribedDataSetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.3
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
  MethodBinding bindRemoveSubscribedDataSet(
      MethodBindings bindings, SubscribedDataSetFolderTypeRemoveSubscribedDataSetHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.3
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
  MethodBinding bindRemoveSubscribedDataSetDetailed(
      MethodBindings bindings,
      SubscribedDataSetFolderTypeRemoveSubscribedDataSetDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getAddDataSetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.4
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
  MethodBinding bindAddDataSetFolder(
      MethodBindings bindings, SubscribedDataSetFolderTypeAddDataSetFolderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.4
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
  MethodBinding bindAddDataSetFolderDetailed(
      MethodBindings bindings, SubscribedDataSetFolderTypeAddDataSetFolderDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.5
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getRemoveDataSetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.5
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
  MethodBinding bindRemoveDataSetFolder(
      MethodBindings bindings, SubscribedDataSetFolderTypeRemoveDataSetFolderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.5
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
  MethodBinding bindRemoveDataSetFolderDetailed(
      MethodBindings bindings,
      SubscribedDataSetFolderTypeRemoveDataSetFolderDetailedHandler handler)
      throws UaException;
}
