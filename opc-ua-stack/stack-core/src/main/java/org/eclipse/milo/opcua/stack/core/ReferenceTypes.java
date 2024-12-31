/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class ReferenceTypes {
  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.1">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.1</a>
   */
  public static final NodeId References = new NodeId(0, 31);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.3</a>
   */
  public static final NodeId NonHierarchicalReferences = new NodeId(0, 32);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.2</a>
   */
  public static final NodeId HierarchicalReferences = new NodeId(0, 33);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.4</a>
   */
  public static final NodeId HasChild = new NodeId(0, 34);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.6</a>
   */
  public static final NodeId Organizes = new NodeId(0, 35);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.14">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.14</a>
   */
  public static final NodeId HasEventSource = new NodeId(0, 36);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.11</a>
   */
  public static final NodeId HasModellingRule = new NodeId(0, 37);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.13</a>
   */
  public static final NodeId HasEncoding = new NodeId(0, 38);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v104/Core/docs/Part5/D.5.1">https://reference.opcfoundation.org/v104/Core/docs/Part5/D.5.1</a>
   */
  public static final NodeId HasDescription = new NodeId(0, 39);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.12</a>
   */
  public static final NodeId HasTypeDefinition = new NodeId(0, 40);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.16">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.16</a>
   */
  public static final NodeId GeneratesEvent = new NodeId(0, 41);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.17">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.17</a>
   */
  public static final NodeId AlwaysGeneratesEvent = new NodeId(0, 3065);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.5">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.5</a>
   */
  public static final NodeId Aggregates = new NodeId(0, 44);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.10</a>
   */
  public static final NodeId HasSubtype = new NodeId(0, 45);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.9">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.9</a>
   */
  public static final NodeId HasProperty = new NodeId(0, 46);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.7">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.7</a>
   */
  public static final NodeId HasComponent = new NodeId(0, 47);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.15</a>
   */
  public static final NodeId HasNotifier = new NodeId(0, 48);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.8</a>
   */
  public static final NodeId HasOrderedComponent = new NodeId(0, 49);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.11">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.11</a>
   */
  public static final NodeId FromState = new NodeId(0, 51);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.12">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.12</a>
   */
  public static final NodeId ToState = new NodeId(0, 52);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.13">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.13</a>
   */
  public static final NodeId HasCause = new NodeId(0, 53);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.14">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.14</a>
   */
  public static final NodeId HasEffect = new NodeId(0, 54);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.15">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.15</a>
   */
  public static final NodeId HasSubStateMachine = new NodeId(0, 117);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v104/Core/docs/Part11/5.2.3">https://reference.opcfoundation.org/v104/Core/docs/Part11/5.2.3</a>
   */
  public static final NodeId HasHistoricalConfiguration = new NodeId(0, 56);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.23">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.23</a>
   */
  public static final NodeId HasStructuredComponent = new NodeId(0, 24136);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.24">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.24</a>
   */
  public static final NodeId AssociatedWith = new NodeId(0, 24137);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.18">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.18</a>
   */
  public static final NodeId HasArgumentDescription = new NodeId(0, 129);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.19">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.19</a>
   */
  public static final NodeId HasOptionalInputArgumentDescription = new NodeId(0, 131);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.22">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.22</a>
   */
  public static final NodeId IsDeprecated = new NodeId(0, 23562);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.6.3</a>
   */
  public static final NodeId HasGuard = new NodeId(0, 15112);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part19/6.1">https://reference.opcfoundation.org/v105/Core/docs/Part19/6.1</a>
   */
  public static final NodeId HasDictionaryEntry = new NodeId(0, 17597);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.20">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.20</a>
   */
  public static final NodeId HasInterface = new NodeId(0, 17603);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/11.21">https://reference.opcfoundation.org/v105/Core/docs/Part5/11.21</a>
   */
  public static final NodeId HasAddIn = new NodeId(0, 17604);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.2</a>
   */
  public static final NodeId HasTrueSubState = new NodeId(0, 9004);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.3</a>
   */
  public static final NodeId HasFalseSubState = new NodeId(0, 9005);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.4">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.4</a>
   */
  public static final NodeId HasAlarmSuppressionGroup = new NodeId(0, 16361);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.5">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.4.5</a>
   */
  public static final NodeId AlarmGroupMember = new NodeId(0, 16362);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.12">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.12</a>
   */
  public static final NodeId HasCondition = new NodeId(0, 9006);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/7.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/7.2</a>
   */
  public static final NodeId HasEffectDisable = new NodeId(0, 17276);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/7.3">https://reference.opcfoundation.org/v105/Core/docs/Part9/7.3</a>
   */
  public static final NodeId HasEffectEnable = new NodeId(0, 17983);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/7.4">https://reference.opcfoundation.org/v105/Core/docs/Part9/7.4</a>
   */
  public static final NodeId HasEffectSuppressed = new NodeId(0, 17984);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/7.5">https://reference.opcfoundation.org/v105/Core/docs/Part9/7.5</a>
   */
  public static final NodeId HasEffectUnsuppressed = new NodeId(0, 17985);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.6</a>
   */
  public static final NodeId HasPushedSecurityGroup = new NodeId(0, 25345);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.6</a>
   */
  public static final NodeId HasPubSubConnection = new NodeId(0, 14476);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.5</a>
   */
  public static final NodeId DataSetToWriter = new NodeId(0, 14936);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.6</a>
   */
  public static final NodeId HasDataSetWriter = new NodeId(0, 15296);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.9">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.9</a>
   */
  public static final NodeId HasWriterGroup = new NodeId(0, 18804);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.12">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.12</a>
   */
  public static final NodeId HasDataSetReader = new NodeId(0, 15297);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.10">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.10</a>
   */
  public static final NodeId HasReaderGroup = new NodeId(0, 18805);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part17/8.2">https://reference.opcfoundation.org/v105/Core/docs/Part17/8.2</a>
   */
  public static final NodeId AliasFor = new NodeId(0, 23469);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.6.1">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.6.1</a>
   */
  public static final NodeId UsesPriorityMappingTable = new NodeId(0, 25237);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.6.2</a>
   */
  public static final NodeId HasLowerLayerInterface = new NodeId(0, 25238);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.2.2</a>
   */
  public static final NodeId IsExecutableOn = new NodeId(0, 25253);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.4.2</a>
   */
  public static final NodeId Controls = new NodeId(0, 25254);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.5.2</a>
   */
  public static final NodeId Utilizes = new NodeId(0, 25255);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.3.2</a>
   */
  public static final NodeId IsExecutingOn = new NodeId(0, 25265);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.6.2</a>
   */
  public static final NodeId Requires = new NodeId(0, 25256);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.7.2</a>
   */
  public static final NodeId IsPhysicallyConnectedTo = new NodeId(0, 25257);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.8.2</a>
   */
  public static final NodeId RepresentsSameEntityAs = new NodeId(0, 25258);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.9.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.9.2</a>
   */
  public static final NodeId RepresentsSameHardwareAs = new NodeId(0, 25259);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.10.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.10.2</a>
   */
  public static final NodeId RepresentsSameFunctionalityAs = new NodeId(0, 25260);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.11.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.11.2</a>
   */
  public static final NodeId IsHostedBy = new NodeId(0, 25261);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.12.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.12.2</a>
   */
  public static final NodeId HasPhysicalComponent = new NodeId(0, 25262);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/4.13.2">https://reference.opcfoundation.org/v105/Core/docs/Part23/4.13.2</a>
   */
  public static final NodeId HasContainedComponent = new NodeId(0, 25263);

  /**
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part23/1">https://reference.opcfoundation.org/v105/Core/docs/Part23/1</a>
   */
  public static final NodeId HasAttachedComponent = new NodeId(0, 25264);
}
