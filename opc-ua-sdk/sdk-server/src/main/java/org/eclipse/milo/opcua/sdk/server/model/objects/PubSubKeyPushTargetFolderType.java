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
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeAddPushTargetDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeAddPushTargetFolderDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeAddPushTargetFolderHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeAddPushTargetHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTargetDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTargetFolderDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTargetFolderHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTargetHandler;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1</a>
 */
public interface PubSubKeyPushTargetFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  MethodNode getAddPushTargetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
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
  MethodBinding bindAddPushTarget(
      MethodBindings bindings, PubSubKeyPushTargetFolderTypeAddPushTargetHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
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
  MethodBinding bindAddPushTargetDetailed(
      MethodBindings bindings, PubSubKeyPushTargetFolderTypeAddPushTargetDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  MethodNode getRemovePushTargetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
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
  MethodBinding bindRemovePushTarget(
      MethodBindings bindings, PubSubKeyPushTargetFolderTypeRemovePushTargetHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
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
  MethodBinding bindRemovePushTargetDetailed(
      MethodBindings bindings, PubSubKeyPushTargetFolderTypeRemovePushTargetDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getAddPushTargetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
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
  MethodBinding bindAddPushTargetFolder(
      MethodBindings bindings, PubSubKeyPushTargetFolderTypeAddPushTargetFolderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
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
  MethodBinding bindAddPushTargetFolderDetailed(
      MethodBindings bindings,
      PubSubKeyPushTargetFolderTypeAddPushTargetFolderDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getRemovePushTargetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
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
  MethodBinding bindRemovePushTargetFolder(
      MethodBindings bindings, PubSubKeyPushTargetFolderTypeRemovePushTargetFolderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
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
  MethodBinding bindRemovePushTargetFolderDetailed(
      MethodBindings bindings,
      PubSubKeyPushTargetFolderTypeRemovePushTargetFolderDetailedHandler handler)
      throws UaException;
}
