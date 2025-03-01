/*
 *  Copyright (c) 2025 the Eclipse Milo Authors
 *
 *  This program and the accompanying materials are made
 *  available under the terms of the Eclipse Public License 2.0
 *  which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

abstract class NodeIds3 extends NodeIds4 {
  public static final NodeId PubSubDiagnosticsType_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19683));

  public static final NodeId PubSubDiagnosticsType_TotalError = new NodeId(UShort.MIN, uint(19684));

  public static final NodeId PubSubDiagnosticsType_TotalError_Active =
      new NodeId(UShort.MIN, uint(19685));

  public static final NodeId PubSubDiagnosticsType_TotalError_Classification =
      new NodeId(UShort.MIN, uint(19686));

  public static final NodeId PubSubDiagnosticsType_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19687));

  public static final NodeId PubSubDiagnosticsType_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19688));

  public static final NodeId PubSubDiagnosticsType_Reset = new NodeId(UShort.MIN, uint(19689));

  public static final NodeId PubSubDiagnosticsType_SubError = new NodeId(UShort.MIN, uint(19690));

  public static final NodeId PubSubDiagnosticsType_Counters = new NodeId(UShort.MIN, uint(19691));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19692));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19693));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19694));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19695));

  public static final NodeId PubSubDiagnosticsType_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19696));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19697));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByMethod_Active =
      new NodeId(UShort.MIN, uint(19698));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19699));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19700));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19701));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19702));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalByParent_Active =
      new NodeId(UShort.MIN, uint(19703));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19704));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19705));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19706));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19707));

  public static final NodeId PubSubDiagnosticsType_Counters_StateOperationalFromError_Active =
      new NodeId(UShort.MIN, uint(19708));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19709));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19710));

  public static final NodeId
      PubSubDiagnosticsType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19711));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19712));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19713));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_Classification =
      new NodeId(UShort.MIN, uint(19714));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19715));

  public static final NodeId PubSubDiagnosticsType_Counters_StatePausedByParent_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19716));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19717));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(19718));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_Classification =
      new NodeId(UShort.MIN, uint(19719));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19720));

  public static final NodeId PubSubDiagnosticsType_Counters_StateDisabledByMethod_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19721));

  public static final NodeId PubSubDiagnosticsType_LiveValues = new NodeId(UShort.MIN, uint(19722));

  public static final NodeId DiagnosticsLevel = new NodeId(UShort.MIN, uint(19723));

  public static final NodeId DiagnosticsLevel_EnumStrings = new NodeId(UShort.MIN, uint(19724));

  public static final NodeId PubSubDiagnosticsCounterType = new NodeId(UShort.MIN, uint(19725));

  public static final NodeId PubSubDiagnosticsCounterType_Active =
      new NodeId(UShort.MIN, uint(19726));

  public static final NodeId PubSubDiagnosticsCounterType_Classification =
      new NodeId(UShort.MIN, uint(19727));

  public static final NodeId PubSubDiagnosticsCounterType_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19728));

  public static final NodeId PubSubDiagnosticsCounterType_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19729));

  public static final NodeId PubSubDiagnosticsCounterClassification =
      new NodeId(UShort.MIN, uint(19730));

  public static final NodeId PubSubDiagnosticsCounterClassification_EnumStrings =
      new NodeId(UShort.MIN, uint(19731));

  public static final NodeId PubSubDiagnosticsRootType = new NodeId(UShort.MIN, uint(19732));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues =
      new NodeId(UShort.MIN, uint(19777));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetWriters =
      new NodeId(UShort.MIN, uint(19778));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19779));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(19780));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19781));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_OperationalDataSetWriters =
      new NodeId(UShort.MIN, uint(19782));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19783));

  public static final NodeId PubSubDiagnosticsRootType_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(19784));

  public static final NodeId
      PubSubDiagnosticsRootType_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19785));

  public static final NodeId PubSubDiagnosticsConnectionType = new NodeId(UShort.MIN, uint(19786));

  public static final NodeId PubSubDiagnosticsConnectionType_LiveValues =
      new NodeId(UShort.MIN, uint(19831));

  public static final NodeId PubSubDiagnosticsConnectionType_LiveValues_ResolvedAddress =
      new NodeId(UShort.MIN, uint(19832));

  public static final NodeId
      PubSubDiagnosticsConnectionType_LiveValues_ResolvedAddress_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19833));

  public static final NodeId PubSubDiagnosticsWriterGroupType = new NodeId(UShort.MIN, uint(19834));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters =
      new NodeId(UShort.MIN, uint(19848));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19849));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19850));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19851));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19852));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19853));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19854));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19855));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19856));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19857));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19858));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19859));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19860));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19861));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19862));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19863));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19864));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19865));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19866));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19867));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19868));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19869));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19870));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19871));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19872));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19873));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19874));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(19875));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19876));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19877));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19878));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues =
      new NodeId(UShort.MIN, uint(19879));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages =
      new NodeId(UShort.MIN, uint(19880));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_Active =
      new NodeId(UShort.MIN, uint(19881));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(19882));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19883));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_SentNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19884));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions =
      new NodeId(UShort.MIN, uint(19885));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_Active =
      new NodeId(UShort.MIN, uint(19886));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_Classification =
          new NodeId(UShort.MIN, uint(19887));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19888));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_FailedTransmissions_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19889));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors =
      new NodeId(UShort.MIN, uint(19890));

  public static final NodeId PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_Active =
      new NodeId(UShort.MIN, uint(19891));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(19892));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19893));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_Counters_EncryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19894));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_ConfiguredDataSetWriters =
      new NodeId(UShort.MIN, uint(19895));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_ConfiguredDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19896));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_OperationalDataSetWriters =
      new NodeId(UShort.MIN, uint(19897));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_OperationalDataSetWriters_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19898));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_SecurityTokenID =
      new NodeId(UShort.MIN, uint(19899));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19900));

  public static final NodeId PubSubDiagnosticsWriterGroupType_LiveValues_TimeToNextTokenID =
      new NodeId(UShort.MIN, uint(19901));

  public static final NodeId
      PubSubDiagnosticsWriterGroupType_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19902));

  public static final NodeId PubSubDiagnosticsReaderGroupType = new NodeId(UShort.MIN, uint(19903));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters =
      new NodeId(UShort.MIN, uint(19917));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19918));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19919));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19920));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(19921));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(19922));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19923));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19924));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19925));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19926));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19927));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19928));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19929));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19930));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19931));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19932));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19933));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19934));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(19935));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19936));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19937));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(19938));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(19939));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(19940));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19941));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19942));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(19943));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(19944));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(19945));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19946));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19947));

  public static final NodeId PubSubDiagnosticsReaderGroupType_LiveValues =
      new NodeId(UShort.MIN, uint(19948));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages =
      new NodeId(UShort.MIN, uint(19949));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_Active =
          new NodeId(UShort.MIN, uint(19950));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(19951));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19952));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19953));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages =
          new NodeId(UShort.MIN, uint(19954));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_Active =
          new NodeId(UShort.MIN, uint(19955));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(19956));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19957));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_ReceivedInvalidNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19958));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors =
      new NodeId(UShort.MIN, uint(19959));

  public static final NodeId PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_Active =
      new NodeId(UShort.MIN, uint(19960));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(19961));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19962));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_Counters_DecryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19963));

  public static final NodeId PubSubDiagnosticsReaderGroupType_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(19964));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19965));

  public static final NodeId PubSubDiagnosticsReaderGroupType_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(19966));

  public static final NodeId
      PubSubDiagnosticsReaderGroupType_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19967));

  public static final NodeId PubSubDiagnosticsDataSetWriterType =
      new NodeId(UShort.MIN, uint(19968));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters =
      new NodeId(UShort.MIN, uint(19982));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateError =
      new NodeId(UShort.MIN, uint(19983));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(19984));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(19985));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19986));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19987));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(19988));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(19989));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(19990));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19991));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19992));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(19993));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(19994));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(19995));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(19996));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(19997));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(19998));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(19999));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(20000));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20001));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20002));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(20003));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(20004));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(20005));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20006));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20007));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(20008));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(20009));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(20010));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20011));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20012));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues =
      new NodeId(UShort.MIN, uint(20013));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages =
      new NodeId(UShort.MIN, uint(20014));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(20015));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(20016));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20017));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20018));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_MessageSequenceNumber =
      new NodeId(UShort.MIN, uint(20019));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20020));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_StatusCode =
      new NodeId(UShort.MIN, uint(20021));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20022));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_MajorVersion =
      new NodeId(UShort.MIN, uint(20023));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20024));

  public static final NodeId PubSubDiagnosticsDataSetWriterType_LiveValues_MinorVersion =
      new NodeId(UShort.MIN, uint(20025));

  public static final NodeId
      PubSubDiagnosticsDataSetWriterType_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20026));

  public static final NodeId PubSubDiagnosticsDataSetReaderType =
      new NodeId(UShort.MIN, uint(20027));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters =
      new NodeId(UShort.MIN, uint(20041));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateError =
      new NodeId(UShort.MIN, uint(20042));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(20043));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(20044));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20045));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20046));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(20047));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_Active =
          new NodeId(UShort.MIN, uint(20048));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(20049));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20050));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20051));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(20052));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_Active =
          new NodeId(UShort.MIN, uint(20053));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(20054));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20055));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20056));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(20057));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_Active =
          new NodeId(UShort.MIN, uint(20058));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(20059));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20060));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20061));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(20062));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_Active =
          new NodeId(UShort.MIN, uint(20063));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(20064));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20065));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20066));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(20067));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_Active =
          new NodeId(UShort.MIN, uint(20068));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(20069));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20070));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20071));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues =
      new NodeId(UShort.MIN, uint(20072));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages =
      new NodeId(UShort.MIN, uint(20073));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_Active =
          new NodeId(UShort.MIN, uint(20074));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_Classification =
          new NodeId(UShort.MIN, uint(20075));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20076));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_FailedDataSetMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20077));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors =
      new NodeId(UShort.MIN, uint(20078));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_Active =
      new NodeId(UShort.MIN, uint(20079));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_Classification =
          new NodeId(UShort.MIN, uint(20080));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20081));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_Counters_DecryptionErrors_TimeFirstChange =
          new NodeId(UShort.MIN, uint(20082));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_MessageSequenceNumber =
      new NodeId(UShort.MIN, uint(20083));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_MessageSequenceNumber_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20084));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_StatusCode =
      new NodeId(UShort.MIN, uint(20085));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_StatusCode_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20086));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_MajorVersion =
      new NodeId(UShort.MIN, uint(20087));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_MajorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20088));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_MinorVersion =
      new NodeId(UShort.MIN, uint(20089));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_MinorVersion_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20090));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_SecurityTokenID =
      new NodeId(UShort.MIN, uint(20091));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20092));

  public static final NodeId PubSubDiagnosticsDataSetReaderType_LiveValues_TimeToNextTokenID =
      new NodeId(UShort.MIN, uint(20093));

  public static final NodeId
      PubSubDiagnosticsDataSetReaderType_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(20094));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20095));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(20096));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(20097));

  public static final NodeId
      CertificateGroupType_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20098));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(20099));

  public static final NodeId CertificateGroupType_CertificateExpired_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(20100));

  public static final NodeId CertificateGroupType_CertificateExpired_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(20101));

  public static final NodeId CertificateGroupType_CertificateExpired_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(20102));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleEnabled =
      new NodeId(UShort.MIN, uint(20103));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound =
      new NodeId(UShort.MIN, uint(20104));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(20105));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(20106));

  public static final NodeId CertificateGroupType_CertificateExpired_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(20107));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState =
      new NodeId(UShort.MIN, uint(20108));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_Id =
      new NodeId(UShort.MIN, uint(20109));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_Name =
      new NodeId(UShort.MIN, uint(20110));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_Number =
      new NodeId(UShort.MIN, uint(20111));

  public static final NodeId
      CertificateGroupType_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20112));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(20113));

  public static final NodeId
      CertificateGroupType_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20114));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(20115));

  public static final NodeId CertificateGroupType_CertificateExpired_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(20116));

  public static final NodeId CertificateGroupType_CertificateExpired_OnDelay =
      new NodeId(UShort.MIN, uint(20117));

  public static final NodeId CertificateGroupType_CertificateExpired_OffDelay =
      new NodeId(UShort.MIN, uint(20118));

  public static final NodeId CertificateGroupType_CertificateExpired_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(20119));

  public static final NodeId CertificateGroupType_CertificateExpired_FirstInGroup =
      new NodeId(UShort.MIN, uint(20120));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState =
      new NodeId(UShort.MIN, uint(20121));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_Id =
      new NodeId(UShort.MIN, uint(20122));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_Name =
      new NodeId(UShort.MIN, uint(20123));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_Number =
      new NodeId(UShort.MIN, uint(20124));

  public static final NodeId
      CertificateGroupType_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20125));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20126));

  public static final NodeId
      CertificateGroupType_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20127));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(20128));

  public static final NodeId CertificateGroupType_CertificateExpired_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(20129));

  public static final NodeId CertificateGroupType_CertificateExpired_ReAlarmTime =
      new NodeId(UShort.MIN, uint(20130));

  public static final NodeId CertificateGroupType_CertificateExpired_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(20131));

  public static final NodeId CertificateGroupType_CertificateExpired_Silence =
      new NodeId(UShort.MIN, uint(20132));

  public static final NodeId CertificateGroupType_CertificateExpired_Suppress =
      new NodeId(UShort.MIN, uint(20133));

  public static final NodeId CertificateGroupType_CertificateExpired_Unsuppress =
      new NodeId(UShort.MIN, uint(20134));

  public static final NodeId CertificateGroupType_CertificateExpired_RemoveFromService =
      new NodeId(UShort.MIN, uint(20135));

  public static final NodeId CertificateGroupType_CertificateExpired_PlaceInService =
      new NodeId(UShort.MIN, uint(20136));

  public static final NodeId CertificateGroupType_CertificateExpired_Reset =
      new NodeId(UShort.MIN, uint(20137));

  public static final NodeId CertificateGroupType_CertificateExpired_NormalState =
      new NodeId(UShort.MIN, uint(20138));

  public static final NodeId CertificateGroupType_CertificateExpired_ExpirationDate =
      new NodeId(UShort.MIN, uint(20139));

  public static final NodeId CertificateGroupType_CertificateExpired_ExpirationLimit =
      new NodeId(UShort.MIN, uint(20140));

  public static final NodeId CertificateGroupType_CertificateExpired_CertificateType =
      new NodeId(UShort.MIN, uint(20141));

  public static final NodeId CertificateGroupType_CertificateExpired_Certificate =
      new NodeId(UShort.MIN, uint(20142));

  public static final NodeId CertificateGroupType_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(20143));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EventId =
      new NodeId(UShort.MIN, uint(20144));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EventType =
      new NodeId(UShort.MIN, uint(20145));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SourceNode =
      new NodeId(UShort.MIN, uint(20146));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SourceName =
      new NodeId(UShort.MIN, uint(20147));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Time =
      new NodeId(UShort.MIN, uint(20148));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ReceiveTime =
      new NodeId(UShort.MIN, uint(20149));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LocalTime =
      new NodeId(UShort.MIN, uint(20150));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Message =
      new NodeId(UShort.MIN, uint(20151));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Severity =
      new NodeId(UShort.MIN, uint(20152));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionClassId =
      new NodeId(UShort.MIN, uint(20153));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionClassName =
      new NodeId(UShort.MIN, uint(20154));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionSubClassId =
      new NodeId(UShort.MIN, uint(20155));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionSubClassName =
      new NodeId(UShort.MIN, uint(20156));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConditionName =
      new NodeId(UShort.MIN, uint(20157));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_BranchId =
      new NodeId(UShort.MIN, uint(20158));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Retain =
      new NodeId(UShort.MIN, uint(20159));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState =
      new NodeId(UShort.MIN, uint(20160));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_Id =
      new NodeId(UShort.MIN, uint(20161));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_Name =
      new NodeId(UShort.MIN, uint(20162));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_Number =
      new NodeId(UShort.MIN, uint(20163));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20164));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_TransitionTime =
      new NodeId(UShort.MIN, uint(20165));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20166));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_TrueState =
      new NodeId(UShort.MIN, uint(20167));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_EnabledState_FalseState =
      new NodeId(UShort.MIN, uint(20168));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Quality =
      new NodeId(UShort.MIN, uint(20169));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Quality_SourceTimestamp =
      new NodeId(UShort.MIN, uint(20170));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LastSeverity =
      new NodeId(UShort.MIN, uint(20171));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LastSeverity_SourceTimestamp =
      new NodeId(UShort.MIN, uint(20172));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Comment =
      new NodeId(UShort.MIN, uint(20173));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Comment_SourceTimestamp =
      new NodeId(UShort.MIN, uint(20174));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ClientUserId =
      new NodeId(UShort.MIN, uint(20175));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Disable =
      new NodeId(UShort.MIN, uint(20176));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Enable =
      new NodeId(UShort.MIN, uint(20177));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AddComment =
      new NodeId(UShort.MIN, uint(20178));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AddComment_InputArguments =
      new NodeId(UShort.MIN, uint(20179));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState =
      new NodeId(UShort.MIN, uint(20180));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_Id =
      new NodeId(UShort.MIN, uint(20181));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_Name =
      new NodeId(UShort.MIN, uint(20182));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_Number =
      new NodeId(UShort.MIN, uint(20183));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20184));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20185));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20186));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_TrueState =
      new NodeId(UShort.MIN, uint(20187));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AckedState_FalseState =
      new NodeId(UShort.MIN, uint(20188));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState =
      new NodeId(UShort.MIN, uint(20189));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_Id =
      new NodeId(UShort.MIN, uint(20190));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_Name =
      new NodeId(UShort.MIN, uint(20191));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_Number =
      new NodeId(UShort.MIN, uint(20192));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20193));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20194));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20195));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_TrueState =
      new NodeId(UShort.MIN, uint(20196));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ConfirmedState_FalseState =
      new NodeId(UShort.MIN, uint(20197));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Acknowledge =
      new NodeId(UShort.MIN, uint(20198));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Acknowledge_InputArguments =
      new NodeId(UShort.MIN, uint(20199));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Confirm =
      new NodeId(UShort.MIN, uint(20200));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Confirm_InputArguments =
      new NodeId(UShort.MIN, uint(20201));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState =
      new NodeId(UShort.MIN, uint(20202));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_Id =
      new NodeId(UShort.MIN, uint(20203));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_Name =
      new NodeId(UShort.MIN, uint(20204));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_Number =
      new NodeId(UShort.MIN, uint(20205));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20206));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_TransitionTime =
      new NodeId(UShort.MIN, uint(20207));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20208));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_TrueState =
      new NodeId(UShort.MIN, uint(20209));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ActiveState_FalseState =
      new NodeId(UShort.MIN, uint(20210));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_InputNode =
      new NodeId(UShort.MIN, uint(20211));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState =
      new NodeId(UShort.MIN, uint(20212));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_Id =
      new NodeId(UShort.MIN, uint(20213));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_Name =
      new NodeId(UShort.MIN, uint(20214));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_Number =
      new NodeId(UShort.MIN, uint(20215));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20216));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20217));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20218));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_TrueState =
      new NodeId(UShort.MIN, uint(20219));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedState_FalseState =
      new NodeId(UShort.MIN, uint(20220));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState =
      new NodeId(UShort.MIN, uint(20221));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_Id =
      new NodeId(UShort.MIN, uint(20222));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_Name =
      new NodeId(UShort.MIN, uint(20223));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_Number =
      new NodeId(UShort.MIN, uint(20224));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20225));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20226));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20227));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_TrueState =
      new NodeId(UShort.MIN, uint(20228));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OutOfServiceState_FalseState =
      new NodeId(UShort.MIN, uint(20229));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState =
      new NodeId(UShort.MIN, uint(20230));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState =
      new NodeId(UShort.MIN, uint(20231));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_Id =
      new NodeId(UShort.MIN, uint(20232));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20233));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20234));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20235));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition =
      new NodeId(UShort.MIN, uint(20236));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20237));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20238));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20239));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20240));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20241));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_AvailableStates =
      new NodeId(UShort.MIN, uint(20242));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20243));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_UnshelveTime =
      new NodeId(UShort.MIN, uint(20244));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_TimedShelve =
      new NodeId(UShort.MIN, uint(20245));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20246));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_Unshelve =
      new NodeId(UShort.MIN, uint(20247));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ShelvingState_OneShotShelve =
      new NodeId(UShort.MIN, uint(20248));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SuppressedOrShelved =
      new NodeId(UShort.MIN, uint(20249));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_MaxTimeShelved =
      new NodeId(UShort.MIN, uint(20250));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleEnabled =
      new NodeId(UShort.MIN, uint(20251));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound =
      new NodeId(UShort.MIN, uint(20252));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound_ListId =
      new NodeId(UShort.MIN, uint(20253));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound_AgencyId =
      new NodeId(UShort.MIN, uint(20254));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_AudibleSound_VersionId =
      new NodeId(UShort.MIN, uint(20255));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState =
      new NodeId(UShort.MIN, uint(20256));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_Id =
      new NodeId(UShort.MIN, uint(20257));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_Name =
      new NodeId(UShort.MIN, uint(20258));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_Number =
      new NodeId(UShort.MIN, uint(20259));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20260));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_TransitionTime =
      new NodeId(UShort.MIN, uint(20261));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20262));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_TrueState =
      new NodeId(UShort.MIN, uint(20263));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_SilenceState_FalseState =
      new NodeId(UShort.MIN, uint(20264));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OnDelay =
      new NodeId(UShort.MIN, uint(20265));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_OffDelay =
      new NodeId(UShort.MIN, uint(20266));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_FirstInGroupFlag =
      new NodeId(UShort.MIN, uint(20267));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_FirstInGroup =
      new NodeId(UShort.MIN, uint(20268));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState =
      new NodeId(UShort.MIN, uint(20269));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_Id =
      new NodeId(UShort.MIN, uint(20270));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_Name =
      new NodeId(UShort.MIN, uint(20271));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_Number =
      new NodeId(UShort.MIN, uint(20272));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20273));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_TransitionTime =
      new NodeId(UShort.MIN, uint(20274));

  public static final NodeId
      CertificateGroupType_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20275));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_TrueState =
      new NodeId(UShort.MIN, uint(20276));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LatchedState_FalseState =
      new NodeId(UShort.MIN, uint(20277));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ReAlarmTime =
      new NodeId(UShort.MIN, uint(20278));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_ReAlarmRepeatCount =
      new NodeId(UShort.MIN, uint(20279));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Silence =
      new NodeId(UShort.MIN, uint(20280));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Suppress =
      new NodeId(UShort.MIN, uint(20281));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Unsuppress =
      new NodeId(UShort.MIN, uint(20282));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_RemoveFromService =
      new NodeId(UShort.MIN, uint(20283));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_PlaceInService =
      new NodeId(UShort.MIN, uint(20284));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_Reset =
      new NodeId(UShort.MIN, uint(20285));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_NormalState =
      new NodeId(UShort.MIN, uint(20286));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_TrustListId =
      new NodeId(UShort.MIN, uint(20287));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_LastUpdateTime =
      new NodeId(UShort.MIN, uint(20288));

  public static final NodeId CertificateGroupType_TrustListOutOfDate_UpdateFrequency =
      new NodeId(UShort.MIN, uint(20289));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20290));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired =
      new NodeId(UShort.MIN, uint(20291));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(20292));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(20293));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(20294));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(20295));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(20296));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(20297));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(20298));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(20299));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(20300));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(20301));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(20302));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20303));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20304));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(20305));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(20306));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(20307));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(20308));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20309));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20310));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20311));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20312));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20313));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20314));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20315));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20316));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(20317));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20318));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(20319));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20320));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(20321));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20322));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(20323));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(20324));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(20325));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(20326));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20327));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(20328));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(20329));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(20330));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(20331));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20332));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20333));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20334));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20335));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20336));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(20337));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20338));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20339));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20340));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20341));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20342));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20343));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20344));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20345));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(20346));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20347));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(20348));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20349));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(20350));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20351));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20352));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20353));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20354));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20355));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20356));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20357));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20358));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(20359));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(20360));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20361));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20362));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20363));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20364));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20365));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20366));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20367));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20368));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20369));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20370));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20371));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20372));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20373));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20374));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20375));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20376));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20377));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(20378));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20379));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20380));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20381));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20382));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20383));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20384));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20385));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20386));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20387));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20388));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20389));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20390));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20391));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20392));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20393));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20394));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20395));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20396));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20397));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20398));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20399));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(20400));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20401));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20402));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20403));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(20404));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20405));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20406));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20407));

  public static final NodeId DataSetOrderingType = new NodeId(UShort.MIN, uint(20408));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_SecurityTokenID =
          new NodeId(UShort.MIN, uint(20409));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20410));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20411));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20412));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20413));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20414));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(20415));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(20416));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20417));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(20418));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(20419));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20420));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20421));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20422));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20423));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20424));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20425));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20426));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20427));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20428));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20429));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(20430));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(20431));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(20432));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(20433));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(20434));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(20435));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(20436));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(20437));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(20438));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(20439));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(20440));

  public static final NodeId CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(20441));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(20442));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(20443));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(20444));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(20445));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(20446));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(20447));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(20448));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(20449));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(20450));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(20451));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(20452));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20453));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20454));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(20455));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(20456));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(20457));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(20458));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20459));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20460));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20461));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20462));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20463));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20464));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20465));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20466));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(20467));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20468));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(20469));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20470));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(20471));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20472));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(20473));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(20474));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(20475));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(20476));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20477));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(20478));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(20479));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(20480));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(20481));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20482));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20483));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20484));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20485));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20486));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(20487));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20488));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20489));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20490));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20491));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20492));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20493));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20494));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20495));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(20496));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20497));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(20498));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20499));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(20500));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20501));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20502));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20503));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20504));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20505));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20506));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20507));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20508));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(20509));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(20510));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20511));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20512));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20513));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20514));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20515));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20516));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20517));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20518));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20519));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20520));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20521));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20522));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20523));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20524));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20525));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20526));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20527));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(20528));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20529));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20530));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20531));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20532));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20533));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20534));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20535));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20536));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20537));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20538));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20539));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20540));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20541));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20542));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20543));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20544));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20545));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20546));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20547));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20548));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20549));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(20550));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20551));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20552));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20553));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(20554));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20555));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20556));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20557));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20558));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20559));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20560));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20561));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20562));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(20563));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(20564));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20565));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(20566));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(20567));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20568));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20569));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20570));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20571));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20572));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20573));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20574));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20575));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20576));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20577));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(20578));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(20579));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(20580));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(20581));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(20582));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(20583));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(20584));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(20585));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(20586));

  public static final NodeId
      CertificateGroupFolderType_DefaultApplicationGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20587));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20588));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired =
      new NodeId(UShort.MIN, uint(20589));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(20590));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(20591));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(20592));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(20593));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Time =
      new NodeId(UShort.MIN, uint(20594));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(20595));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(20596));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(20597));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(20598));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(20599));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(20600));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20601));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20602));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(20603));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(20604));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(20605));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(20606));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20607));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20608));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20609));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20610));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20611));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20612));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20613));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20614));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(20615));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20616));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(20617));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20618));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(20619));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20620));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(20621));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(20622));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(20623));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(20624));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20625));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(20626));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(20627));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(20628));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(20629));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20630));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20631));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20632));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20633));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20634));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(20635));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20636));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20637));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20638));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20639));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20640));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20641));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20642));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20643));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(20644));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20645));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(20646));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20647));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(20648));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20649));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20650));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20651));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20652));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20653));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20654));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20655));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20656));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(20657));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(20658));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20659));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20660));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20661));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20662));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20663));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20664));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20665));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20666));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20667));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20668));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20669));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20670));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20671));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20672));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20673));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20674));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20675));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(20676));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20677));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20678));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20679));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20680));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20681));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20682));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20683));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20684));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20685));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20686));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20687));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20688));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20689));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20690));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20691));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20692));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20693));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20694));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20695));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20696));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20697));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(20698));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20699));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20700));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20701));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(20702));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20703));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20704));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20705));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20706));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20707));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20708));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20709));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20710));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(20711));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(20712));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20713));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(20714));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(20715));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20716));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20717));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20718));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20719));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20720));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20721));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20722));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20723));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20724));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20725));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(20726));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(20727));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(20728));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(20729));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(20730));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Reset =
      new NodeId(UShort.MIN, uint(20731));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(20732));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(20733));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(20734));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(20735));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(20736));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(20737));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(20738));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(20739));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(20740));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(20741));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Time =
      new NodeId(UShort.MIN, uint(20742));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(20743));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(20744));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(20745));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(20746));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(20747));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(20748));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20749));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20750));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(20751));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(20752));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(20753));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(20754));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20755));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20756));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20757));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20758));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20759));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20760));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20761));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20762));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(20763));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20764));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(20765));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20766));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(20767));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20768));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(20769));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(20770));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(20771));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(20772));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20773));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(20774));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(20775));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(20776));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(20777));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20778));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20779));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20780));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20781));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20782));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(20783));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20784));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20785));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20786));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20787));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20788));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20789));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20790));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20791));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(20792));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20793));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(20794));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20795));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(20796));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20797));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20798));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20799));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20800));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20801));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20802));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20803));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20804));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(20805));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(20806));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20807));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20808));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20809));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20810));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20811));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20812));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20813));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20814));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20815));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20816));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20817));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20818));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20819));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20820));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20821));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20822));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20823));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(20824));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20825));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20826));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20827));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20828));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20829));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20830));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20831));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20832));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20833));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20834));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20835));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20836));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20837));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20838));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20839));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20840));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20841));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20842));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20843));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20844));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20845));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(20846));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20847));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20848));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20849));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(20850));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(20851));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(20852));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(20853));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20854));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20855));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20856));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(20857));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(20858));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(20859));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(20860));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(20861));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(20862));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(20863));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(20864));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(20865));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(20866));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20867));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20868));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20869));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(20870));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(20871));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(20872));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(20873));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(20874));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(20875));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(20876));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(20877));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(20878));

  public static final NodeId CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_Reset =
      new NodeId(UShort.MIN, uint(20879));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(20880));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(20881));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(20882));

  public static final NodeId
      CertificateGroupFolderType_DefaultHttpsGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20883));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(20884));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired =
      new NodeId(UShort.MIN, uint(20885));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(20886));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(20887));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(20888));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(20889));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(20890));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(20891));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(20892));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(20893));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(20894));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(20895));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(20896));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(20897));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(20898));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(20899));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(20900));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(20901));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(20902));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(20903));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(20904));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(20905));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20906));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(20907));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20908));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(20909));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(20910));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(20911));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20912));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(20913));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20914));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(20915));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(20916));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(20917));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(20918));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(20919));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(20920));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(20921));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(20922));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(20923));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(20924));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(20925));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20926));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20927));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20928));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(20929));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(20930));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(20931));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(20932));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(20933));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(20934));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20935));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20936));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20937));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(20938));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(20939));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(20940));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(20941));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(20942));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(20943));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(20944));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(20945));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(20946));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(20947));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20948));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(20949));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20950));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(20951));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(20952));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(20953));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(20954));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(20955));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(20956));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(20957));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20958));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(20959));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20960));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(20961));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(20962));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(20963));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(20964));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(20965));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(20966));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20967));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(20968));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20969));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(20970));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(20971));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(20972));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(20973));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(20974));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(20975));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(20976));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(20977));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(20978));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(20979));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(20980));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(20981));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(20982));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(20983));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(20984));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(20985));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(20986));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(20987));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(20988));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(20989));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(20990));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(20991));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(20992));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(20993));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(20994));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(20995));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(20996));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(20997));

  public static final NodeId VersionTime = new NodeId(UShort.MIN, uint(20998));

  public static final NodeId SessionlessInvokeResponseType = new NodeId(UShort.MIN, uint(20999));

  public static final NodeId SessionlessInvokeResponseType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21000));

  public static final NodeId SessionlessInvokeResponseType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21001));

  public static final NodeId OpcUa_BinarySchema_FieldTargetDataType =
      new NodeId(UShort.MIN, uint(21002));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_SecurityTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21003));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_TimeToNextTokenID =
          new NodeId(UShort.MIN, uint(21004));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_Diagnostics_LiveValues_TimeToNextTokenID_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21005));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_SubscribedDataSet =
      new NodeId(UShort.MIN, uint(21006));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(21007));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21008));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_CreateTargetVariables =
      new NodeId(UShort.MIN, uint(21009));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateTargetVariables_InputArguments =
          new NodeId(UShort.MIN, uint(21010));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateTargetVariables_OutputArguments =
          new NodeId(UShort.MIN, uint(21011));

  public static final NodeId ReaderGroupType_DataSetReaderName_Placeholder_CreateDataSetMirror =
      new NodeId(UShort.MIN, uint(21012));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateDataSetMirror_InputArguments =
          new NodeId(UShort.MIN, uint(21013));

  public static final NodeId
      ReaderGroupType_DataSetReaderName_Placeholder_CreateDataSetMirror_OutputArguments =
          new NodeId(UShort.MIN, uint(21014));

  public static final NodeId ReaderGroupType_Diagnostics = new NodeId(UShort.MIN, uint(21015));

  public static final NodeId ReaderGroupType_Diagnostics_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21016));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation =
      new NodeId(UShort.MIN, uint(21017));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_Active =
      new NodeId(UShort.MIN, uint(21018));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_Classification =
      new NodeId(UShort.MIN, uint(21019));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21020));

  public static final NodeId ReaderGroupType_Diagnostics_TotalInformation_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21021));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError =
      new NodeId(UShort.MIN, uint(21022));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_Active =
      new NodeId(UShort.MIN, uint(21023));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_Classification =
      new NodeId(UShort.MIN, uint(21024));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21025));

  public static final NodeId ReaderGroupType_Diagnostics_TotalError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21026));

  public static final NodeId ReaderGroupType_Diagnostics_Reset =
      new NodeId(UShort.MIN, uint(21027));

  public static final NodeId ReaderGroupType_Diagnostics_SubError =
      new NodeId(UShort.MIN, uint(21028));

  public static final NodeId ReaderGroupType_Diagnostics_Counters =
      new NodeId(UShort.MIN, uint(21029));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError =
      new NodeId(UShort.MIN, uint(21030));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_Active =
      new NodeId(UShort.MIN, uint(21031));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_Classification =
      new NodeId(UShort.MIN, uint(21032));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_DiagnosticsLevel =
      new NodeId(UShort.MIN, uint(21033));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateError_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21034));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod =
      new NodeId(UShort.MIN, uint(21035));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_Active =
      new NodeId(UShort.MIN, uint(21036));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_Classification =
          new NodeId(UShort.MIN, uint(21037));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21038));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21039));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByParent =
      new NodeId(UShort.MIN, uint(21040));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_Active =
      new NodeId(UShort.MIN, uint(21041));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_Classification =
          new NodeId(UShort.MIN, uint(21042));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21043));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21044));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalFromError =
      new NodeId(UShort.MIN, uint(21045));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_Active =
      new NodeId(UShort.MIN, uint(21046));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_Classification =
          new NodeId(UShort.MIN, uint(21047));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21048));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateOperationalFromError_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21049));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StatePausedByParent =
      new NodeId(UShort.MIN, uint(21050));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StatePausedByParent_Active =
      new NodeId(UShort.MIN, uint(21051));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StatePausedByParent_Classification =
          new NodeId(UShort.MIN, uint(21052));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StatePausedByParent_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21053));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StatePausedByParent_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21054));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod =
      new NodeId(UShort.MIN, uint(21055));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_Active =
      new NodeId(UShort.MIN, uint(21056));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_Classification =
          new NodeId(UShort.MIN, uint(21057));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21058));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_StateDisabledByMethod_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21059));

  public static final NodeId ReaderGroupType_Diagnostics_LiveValues =
      new NodeId(UShort.MIN, uint(21060));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages =
      new NodeId(UShort.MIN, uint(21061));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_Active =
      new NodeId(UShort.MIN, uint(21062));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(21063));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21064));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21065));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages =
      new NodeId(UShort.MIN, uint(21066));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_Active =
          new NodeId(UShort.MIN, uint(21067));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_Classification =
          new NodeId(UShort.MIN, uint(21068));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21069));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_ReceivedInvalidNetworkMessages_TimeFirstChange =
          new NodeId(UShort.MIN, uint(21070));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors =
      new NodeId(UShort.MIN, uint(21071));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors_Active =
      new NodeId(UShort.MIN, uint(21072));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors_Classification =
      new NodeId(UShort.MIN, uint(21073));

  public static final NodeId
      ReaderGroupType_Diagnostics_Counters_DecryptionErrors_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21074));

  public static final NodeId ReaderGroupType_Diagnostics_Counters_DecryptionErrors_TimeFirstChange =
      new NodeId(UShort.MIN, uint(21075));

  public static final NodeId ReaderGroupType_Diagnostics_LiveValues_ConfiguredDataSetReaders =
      new NodeId(UShort.MIN, uint(21076));

  public static final NodeId
      ReaderGroupType_Diagnostics_LiveValues_ConfiguredDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21077));

  public static final NodeId ReaderGroupType_Diagnostics_LiveValues_OperationalDataSetReaders =
      new NodeId(UShort.MIN, uint(21078));

  public static final NodeId
      ReaderGroupType_Diagnostics_LiveValues_OperationalDataSetReaders_DiagnosticsLevel =
          new NodeId(UShort.MIN, uint(21079));

  public static final NodeId ReaderGroupType_TransportSettings =
      new NodeId(UShort.MIN, uint(21080));

  public static final NodeId ReaderGroupType_MessageSettings = new NodeId(UShort.MIN, uint(21081));

  public static final NodeId ReaderGroupType_AddDataSetReader = new NodeId(UShort.MIN, uint(21082));

  public static final NodeId ReaderGroupType_AddDataSetReader_InputArguments =
      new NodeId(UShort.MIN, uint(21083));

  public static final NodeId ReaderGroupType_AddDataSetReader_OutputArguments =
      new NodeId(UShort.MIN, uint(21084));

  public static final NodeId ReaderGroupType_RemoveDataSetReader =
      new NodeId(UShort.MIN, uint(21085));

  public static final NodeId ReaderGroupType_RemoveDataSetReader_InputArguments =
      new NodeId(UShort.MIN, uint(21086));

  public static final NodeId PubSubGroupTypeAddReaderMethodType =
      new NodeId(UShort.MIN, uint(21087));

  public static final NodeId PubSubGroupTypeAddReaderMethodType_InputArguments =
      new NodeId(UShort.MIN, uint(21088));

  public static final NodeId PubSubGroupTypeAddReaderMethodType_OutputArguments =
      new NodeId(UShort.MIN, uint(21089));

  public static final NodeId ReaderGroupTransportType = new NodeId(UShort.MIN, uint(21090));

  public static final NodeId ReaderGroupMessageType = new NodeId(UShort.MIN, uint(21091));

  public static final NodeId DataSetWriterType_DataSetWriterId =
      new NodeId(UShort.MIN, uint(21092));

  public static final NodeId DataSetWriterType_DataSetFieldContentMask =
      new NodeId(UShort.MIN, uint(21093));

  public static final NodeId DataSetWriterType_KeyFrameCount = new NodeId(UShort.MIN, uint(21094));

  public static final NodeId DataSetWriterType_MessageSettings =
      new NodeId(UShort.MIN, uint(21095));

  public static final NodeId DataSetWriterMessageType = new NodeId(UShort.MIN, uint(21096));

  public static final NodeId DataSetReaderType_PublisherId = new NodeId(UShort.MIN, uint(21097));

  public static final NodeId DataSetReaderType_WriterGroupId = new NodeId(UShort.MIN, uint(21098));

  public static final NodeId DataSetReaderType_DataSetWriterId =
      new NodeId(UShort.MIN, uint(21099));

  public static final NodeId DataSetReaderType_DataSetMetaData =
      new NodeId(UShort.MIN, uint(21100));

  public static final NodeId DataSetReaderType_DataSetFieldContentMask =
      new NodeId(UShort.MIN, uint(21101));

  public static final NodeId DataSetReaderType_MessageReceiveTimeout =
      new NodeId(UShort.MIN, uint(21102));

  public static final NodeId DataSetReaderType_MessageSettings =
      new NodeId(UShort.MIN, uint(21103));

  public static final NodeId DataSetReaderMessageType = new NodeId(UShort.MIN, uint(21104));

  public static final NodeId UadpWriterGroupMessageType = new NodeId(UShort.MIN, uint(21105));

  public static final NodeId UadpWriterGroupMessageType_GroupVersion =
      new NodeId(UShort.MIN, uint(21106));

  public static final NodeId UadpWriterGroupMessageType_DataSetOrdering =
      new NodeId(UShort.MIN, uint(21107));

  public static final NodeId UadpWriterGroupMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21108));

  public static final NodeId UadpWriterGroupMessageType_SamplingOffset =
      new NodeId(UShort.MIN, uint(21109));

  public static final NodeId UadpWriterGroupMessageType_PublishingOffset =
      new NodeId(UShort.MIN, uint(21110));

  public static final NodeId UadpDataSetWriterMessageType = new NodeId(UShort.MIN, uint(21111));

  public static final NodeId UadpDataSetWriterMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21112));

  public static final NodeId UadpDataSetWriterMessageType_ConfiguredSize =
      new NodeId(UShort.MIN, uint(21113));

  public static final NodeId UadpDataSetWriterMessageType_NetworkMessageNumber =
      new NodeId(UShort.MIN, uint(21114));

  public static final NodeId UadpDataSetWriterMessageType_DataSetOffset =
      new NodeId(UShort.MIN, uint(21115));

  public static final NodeId UadpDataSetReaderMessageType = new NodeId(UShort.MIN, uint(21116));

  public static final NodeId UadpDataSetReaderMessageType_GroupVersion =
      new NodeId(UShort.MIN, uint(21117));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21118));

  public static final NodeId UadpDataSetReaderMessageType_NetworkMessageNumber =
      new NodeId(UShort.MIN, uint(21119));

  public static final NodeId UadpDataSetReaderMessageType_DataSetClassId =
      new NodeId(UShort.MIN, uint(21120));

  public static final NodeId UadpDataSetReaderMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21121));

  public static final NodeId UadpDataSetReaderMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21122));

  public static final NodeId UadpDataSetReaderMessageType_PublishingInterval =
      new NodeId(UShort.MIN, uint(21123));

  public static final NodeId UadpDataSetReaderMessageType_ProcessingOffset =
      new NodeId(UShort.MIN, uint(21124));

  public static final NodeId UadpDataSetReaderMessageType_ReceiveOffset =
      new NodeId(UShort.MIN, uint(21125));

  public static final NodeId JsonWriterGroupMessageType = new NodeId(UShort.MIN, uint(21126));

  public static final NodeId JsonWriterGroupMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21127));

  public static final NodeId JsonDataSetWriterMessageType = new NodeId(UShort.MIN, uint(21128));

  public static final NodeId JsonDataSetWriterMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21129));

  public static final NodeId JsonDataSetReaderMessageType = new NodeId(UShort.MIN, uint(21130));

  public static final NodeId JsonDataSetReaderMessageType_NetworkMessageContentMask =
      new NodeId(UShort.MIN, uint(21131));

  public static final NodeId JsonDataSetReaderMessageType_DataSetMessageContentMask =
      new NodeId(UShort.MIN, uint(21132));

  public static final NodeId DatagramWriterGroupTransportType = new NodeId(UShort.MIN, uint(21133));

  public static final NodeId DatagramWriterGroupTransportType_MessageRepeatCount =
      new NodeId(UShort.MIN, uint(21134));

  public static final NodeId DatagramWriterGroupTransportType_MessageRepeatDelay =
      new NodeId(UShort.MIN, uint(21135));

  public static final NodeId BrokerWriterGroupTransportType = new NodeId(UShort.MIN, uint(21136));

  public static final NodeId BrokerWriterGroupTransportType_QueueName =
      new NodeId(UShort.MIN, uint(21137));

  public static final NodeId BrokerDataSetWriterTransportType = new NodeId(UShort.MIN, uint(21138));

  public static final NodeId BrokerDataSetWriterTransportType_QueueName =
      new NodeId(UShort.MIN, uint(21139));

  public static final NodeId BrokerDataSetWriterTransportType_MetaDataQueueName =
      new NodeId(UShort.MIN, uint(21140));

  public static final NodeId BrokerDataSetWriterTransportType_MetaDataUpdateTime =
      new NodeId(UShort.MIN, uint(21141));

  public static final NodeId BrokerDataSetReaderTransportType = new NodeId(UShort.MIN, uint(21142));

  public static final NodeId BrokerDataSetReaderTransportType_QueueName =
      new NodeId(UShort.MIN, uint(21143));

  public static final NodeId BrokerDataSetReaderTransportType_MetaDataQueueName =
      new NodeId(UShort.MIN, uint(21144));

  public static final NodeId NetworkAddressType = new NodeId(UShort.MIN, uint(21145));

  public static final NodeId NetworkAddressType_NetworkInterface =
      new NodeId(UShort.MIN, uint(21146));

  public static final NodeId NetworkAddressUrlType = new NodeId(UShort.MIN, uint(21147));

  public static final NodeId NetworkAddressUrlType_Url = new NodeId(UShort.MIN, uint(21149));

  public static final NodeId WriterGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21150));

  public static final NodeId NetworkAddressDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21151));

  public static final NodeId NetworkAddressUrlDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21152));

  public static final NodeId ReaderGroupDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21153));

  public static final NodeId PubSubConfigurationDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21154));

  public static final NodeId DatagramWriterGroupTransportDataType_Encoding_DefaultBinary =
      new NodeId(UShort.MIN, uint(21155));

  public static final NodeId OpcUa_BinarySchema_WriterGroupDataType =
      new NodeId(UShort.MIN, uint(21156));

  public static final NodeId OpcUa_BinarySchema_WriterGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21157));

  public static final NodeId OpcUa_BinarySchema_WriterGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21158));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressDataType =
      new NodeId(UShort.MIN, uint(21159));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21160));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21161));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressUrlDataType =
      new NodeId(UShort.MIN, uint(21162));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressUrlDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21163));

  public static final NodeId OpcUa_BinarySchema_NetworkAddressUrlDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21164));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupDataType =
      new NodeId(UShort.MIN, uint(21165));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21166));

  public static final NodeId OpcUa_BinarySchema_ReaderGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21167));

  public static final NodeId OpcUa_BinarySchema_PubSubConfigurationDataType =
      new NodeId(UShort.MIN, uint(21168));

  public static final NodeId OpcUa_BinarySchema_PubSubConfigurationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21169));

  public static final NodeId OpcUa_BinarySchema_PubSubConfigurationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21170));

  public static final NodeId OpcUa_BinarySchema_DatagramWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(21171));

  public static final NodeId
      OpcUa_BinarySchema_DatagramWriterGroupTransportDataType_DataTypeVersion =
          new NodeId(UShort.MIN, uint(21172));

  public static final NodeId
      OpcUa_BinarySchema_DatagramWriterGroupTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(21173));

  public static final NodeId WriterGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21174));

  public static final NodeId NetworkAddressDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21175));

  public static final NodeId NetworkAddressUrlDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21176));

  public static final NodeId ReaderGroupDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21177));

  public static final NodeId PubSubConfigurationDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21178));

  public static final NodeId DatagramWriterGroupTransportDataType_Encoding_DefaultXml =
      new NodeId(UShort.MIN, uint(21179));

  public static final NodeId OpcUa_XmlSchema_WriterGroupDataType =
      new NodeId(UShort.MIN, uint(21180));

  public static final NodeId OpcUa_XmlSchema_WriterGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21181));

  public static final NodeId OpcUa_XmlSchema_WriterGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21182));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressDataType =
      new NodeId(UShort.MIN, uint(21183));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21184));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21185));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressUrlDataType =
      new NodeId(UShort.MIN, uint(21186));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressUrlDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21187));

  public static final NodeId OpcUa_XmlSchema_NetworkAddressUrlDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21188));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupDataType =
      new NodeId(UShort.MIN, uint(21189));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21190));

  public static final NodeId OpcUa_XmlSchema_ReaderGroupDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21191));

  public static final NodeId OpcUa_XmlSchema_PubSubConfigurationDataType =
      new NodeId(UShort.MIN, uint(21192));

  public static final NodeId OpcUa_XmlSchema_PubSubConfigurationDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21193));

  public static final NodeId OpcUa_XmlSchema_PubSubConfigurationDataType_DictionaryFragment =
      new NodeId(UShort.MIN, uint(21194));

  public static final NodeId OpcUa_XmlSchema_DatagramWriterGroupTransportDataType =
      new NodeId(UShort.MIN, uint(21195));

  public static final NodeId OpcUa_XmlSchema_DatagramWriterGroupTransportDataType_DataTypeVersion =
      new NodeId(UShort.MIN, uint(21196));

  public static final NodeId
      OpcUa_XmlSchema_DatagramWriterGroupTransportDataType_DictionaryFragment =
          new NodeId(UShort.MIN, uint(21197));

  public static final NodeId WriterGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21198));

  public static final NodeId NetworkAddressDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21199));

  public static final NodeId NetworkAddressUrlDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21200));

  public static final NodeId ReaderGroupDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21201));

  public static final NodeId PubSubConfigurationDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21202));

  public static final NodeId DatagramWriterGroupTransportDataType_Encoding_DefaultJson =
      new NodeId(UShort.MIN, uint(21203));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21204));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21205));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21206));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21207));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21208));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21209));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(21210));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(21211));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21212));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(21213));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(21214));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21215));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21216));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21217));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21218));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21219));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21220));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21221));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21222));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21223));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21224));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(21225));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(21226));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(21227));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(21228));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(21229));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(21230));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(21231));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(21232));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(21233));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(21234));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(21235));

  public static final NodeId CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate =
      new NodeId(UShort.MIN, uint(21236));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(21237));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(21238));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(21239));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(21240));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(21241));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(21242));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(21243));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(21244));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(21245));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(21246));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(21247));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21248));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21249));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(21250));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(21251));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(21252));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(21253));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21254));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21255));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21256));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21257));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21258));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21259));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21260));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21261));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(21262));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21263));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(21264));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21265));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(21266));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21267));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(21268));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(21269));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(21270));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(21271));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21272));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(21273));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(21274));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(21275));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(21276));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21277));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21278));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21279));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21280));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21281));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(21282));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21283));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21284));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21285));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21286));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21287));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21288));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21289));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21290));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(21291));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21292));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(21293));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21294));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(21295));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21296));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21297));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21298));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21299));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21300));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21301));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21302));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21303));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(21304));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(21305));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21306));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21307));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21308));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21309));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21310));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21311));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21312));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21313));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21314));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21315));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21316));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21317));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21318));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21319));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21320));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21321));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21322));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(21323));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21324));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21325));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21326));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21327));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21328));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21329));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21330));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21331));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21332));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21333));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21334));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21335));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21336));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21337));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21338));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21339));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21340));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21341));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21342));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21343));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21344));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(21345));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21346));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21347));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21348));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(21349));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21350));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21351));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21352));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21353));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21354));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21355));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21356));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21357));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(21358));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(21359));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21360));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(21361));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(21362));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21363));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21364));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21365));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21366));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21367));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21368));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21369));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21370));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21371));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21372));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(21373));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(21374));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(21375));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(21376));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(21377));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(21378));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(21379));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(21380));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(21381));

  public static final NodeId
      CertificateGroupFolderType_DefaultUserTokenGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21382));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21383));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired =
          new NodeId(UShort.MIN, uint(21384));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(21385));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(21386));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(21387));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(21388));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(21389));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(21390));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(21391));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(21392));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(21393));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(21394));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(21395));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21396));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21397));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(21398));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(21399));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(21400));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(21401));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21402));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21403));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21404));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21405));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21406));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21407));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21408));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21409));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(21410));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21411));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(21412));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21413));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(21414));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21415));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(21416));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(21417));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(21418));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(21419));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21420));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(21421));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(21422));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(21423));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(21424));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21425));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21426));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21427));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21428));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21429));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(21430));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21431));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21432));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21433));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21434));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21435));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21436));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21437));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21438));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(21439));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21440));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(21441));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21442));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(21443));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21444));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21445));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21446));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21447));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21448));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21449));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21450));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21451));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(21452));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(21453));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21454));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21455));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21456));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21457));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21458));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21459));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21460));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21461));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21462));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21463));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21464));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21465));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21466));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21467));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21468));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21469));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21470));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(21471));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21472));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21473));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21474));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21475));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21476));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21477));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21478));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21479));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21480));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21481));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21482));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21483));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21484));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21485));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21486));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21487));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21488));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21489));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21490));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21491));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21492));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(21493));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21494));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21495));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21496));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(21497));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21498));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21499));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21500));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21501));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21502));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21503));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21504));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21505));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(21506));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(21507));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21508));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(21509));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(21510));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21511));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21512));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21513));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21514));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21515));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21516));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21517));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21518));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21519));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21520));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(21521));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(21522));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(21523));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(21524));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(21525));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(21526));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(21527));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(21528));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(21529));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(21530));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(21531));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(21532));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(21533));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(21534));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(21535));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(21536));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(21537));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(21538));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(21539));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(21540));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(21541));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(21542));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(21543));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21544));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21545));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(21546));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(21547));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(21548));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(21549));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21550));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21551));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21552));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21553));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21554));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21555));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21556));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21557));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(21558));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21559));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(21560));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21561));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(21562));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21563));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(21564));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(21565));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(21566));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(21567));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21568));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(21569));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(21570));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(21571));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(21572));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21573));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21574));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21575));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21576));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21577));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(21578));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21579));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21580));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21581));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21582));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21583));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21584));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21585));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21586));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(21587));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21588));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(21589));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21590));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(21591));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21592));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21593));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21594));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21595));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21596));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21597));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21598));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21599));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(21600));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(21601));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21602));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21603));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21604));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21605));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21606));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21607));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21608));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21609));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21610));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21611));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21612));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21613));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21614));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21615));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21616));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21617));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21618));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(21619));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21620));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21621));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21622));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21623));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21624));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21625));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21626));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21627));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21628));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21629));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21630));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21631));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21632));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21633));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21634));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21635));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21636));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21637));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21638));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21639));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21640));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(21641));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21642));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21643));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21644));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(21645));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21646));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21647));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21648));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21649));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21650));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21651));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21652));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21653));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(21654));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(21655));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21656));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(21657));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(21658));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21659));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21660));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21661));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21662));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21663));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21664));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21665));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21666));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21667));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21668));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(21669));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(21670));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(21671));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(21672));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(21673));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(21674));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(21675));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(21676));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(21677));

  public static final NodeId
      CertificateGroupFolderType_AdditionalGroup_Placeholder_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21678));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21679));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(21680));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(21681));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(21682));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(21683));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(21684));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(21685));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(21686));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(21687));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(21688));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(21689));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(21690));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(21691));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21692));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21693));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(21694));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(21695));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(21696));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(21697));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21698));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21699));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21700));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21701));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21702));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21703));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21704));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21705));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(21706));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21707));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(21708));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21709));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(21710));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21711));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(21712));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(21713));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(21714));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(21715));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21716));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(21717));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(21718));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(21719));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(21720));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21721));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21722));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21723));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21724));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21725));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(21726));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21727));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21728));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21729));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21730));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21731));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21732));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21733));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21734));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(21735));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21736));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(21737));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21738));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(21739));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21740));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21741));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21742));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21743));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21744));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21745));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21746));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21747));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(21748));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(21749));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21750));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21751));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21752));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21753));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21754));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21755));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21756));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21757));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21758));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21759));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21760));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21761));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21762));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21763));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21764));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21765));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21766));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(21767));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21768));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21769));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21770));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21771));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21772));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21773));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21774));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21775));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21776));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21777));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21778));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21779));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21780));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21781));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21782));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21783));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21784));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21785));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21786));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21787));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21788));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(21789));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21790));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21791));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21792));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(21793));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21794));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21795));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21796));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21797));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21798));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21799));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21800));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21801));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(21802));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(21803));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21804));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(21805));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(21806));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21807));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21808));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21809));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21810));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21811));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21812));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21813));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21814));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21815));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21816));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(21817));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(21818));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(21819));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(21820));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(21821));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(21822));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(21823));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(21824));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(21825));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(21826));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(21827));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(21828));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(21829));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(21830));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(21831));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(21832));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(21833));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(21834));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(21835));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(21836));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(21837));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(21838));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(21839));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21840));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21841));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(21842));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(21843));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(21844));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(21845));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21846));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21847));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21848));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21849));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21850));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21851));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(21852));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(21853));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(21854));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21855));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(21856));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21857));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(21858));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(21859));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(21860));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(21861));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(21862));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(21863));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(21864));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(21865));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(21866));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(21867));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(21868));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21869));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21870));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21871));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(21872));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(21873));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(21874));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(21875));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(21876));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(21877));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21878));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21879));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21880));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(21881));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(21882));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(21883));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(21884));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(21885));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(21886));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(21887));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(21888));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(21889));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(21890));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21891));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(21892));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21893));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(21894));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(21895));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(21896));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(21897));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(21898));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(21899));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(21900));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21901));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21902));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21903));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(21904));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(21905));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(21906));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(21907));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(21908));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(21909));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21910));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21911));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21912));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(21913));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(21914));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(21915));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(21916));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(21917));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(21918));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(21919));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21920));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(21921));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(21922));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(21923));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(21924));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(21925));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21926));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(21927));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(21928));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(21929));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(21930));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(21931));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(21932));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(21933));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(21934));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(21935));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(21936));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(21937));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(21938));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(21939));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(21940));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(21941));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(21942));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(21943));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(21944));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21945));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(21946));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21947));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(21948));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(21949));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(21950));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(21951));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(21952));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(21953));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(21954));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(21955));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(21956));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(21957));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21958));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(21959));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21960));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(21961));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(21962));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(21963));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(21964));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(21965));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(21966));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(21967));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(21968));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(21969));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(21970));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(21971));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(21972));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(21973));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultApplicationGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21974));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(21975));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(21976));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(21977));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(21978));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(21979));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(21980));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(21981));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(21982));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(21983));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(21984));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(21985));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(21986));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(21987));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(21988));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(21989));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(21990));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(21991));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(21992));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(21993));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(21994));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(21995));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(21996));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(21997));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(21998));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(21999));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(22000));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(22001));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(22002));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22003));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(22004));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22005));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(22006));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22007));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(22008));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(22009));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(22010));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(22011));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(22012));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(22013));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(22014));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(22015));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(22016));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22017));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22018));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22019));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(22020));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(22021));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(22022));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(22023));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(22024));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(22025));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22026));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22027));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22028));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(22029));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(22030));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Acknowledge =
          new NodeId(UShort.MIN, uint(22031));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(22032));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Confirm =
          new NodeId(UShort.MIN, uint(22033));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(22034));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState =
          new NodeId(UShort.MIN, uint(22035));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_Id =
          new NodeId(UShort.MIN, uint(22036));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_Name =
          new NodeId(UShort.MIN, uint(22037));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_Number =
          new NodeId(UShort.MIN, uint(22038));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22039));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(22040));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22041));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(22042));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(22043));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_InputNode =
          new NodeId(UShort.MIN, uint(22044));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState =
          new NodeId(UShort.MIN, uint(22045));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(22046));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(22047));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(22048));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22049));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22050));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22051));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(22052));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(22053));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState =
          new NodeId(UShort.MIN, uint(22054));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(22055));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(22056));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(22057));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22058));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(22059));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22060));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(22061));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(22062));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState =
          new NodeId(UShort.MIN, uint(22063));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(22064));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(22065));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(22066));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(22067));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22068));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(22069));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(22070));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(22071));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(22072));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(22073));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22074));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(22075));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(22076));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(22077));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(22078));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(22079));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(22080));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(22081));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(22082));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(22083));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleEnabled =
          new NodeId(UShort.MIN, uint(22084));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound =
          new NodeId(UShort.MIN, uint(22085));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(22086));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(22087));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(22088));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState =
          new NodeId(UShort.MIN, uint(22089));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_Id =
          new NodeId(UShort.MIN, uint(22090));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_Name =
          new NodeId(UShort.MIN, uint(22091));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_Number =
          new NodeId(UShort.MIN, uint(22092));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22093));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(22094));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22095));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(22096));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(22097));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OnDelay =
          new NodeId(UShort.MIN, uint(22098));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_OffDelay =
          new NodeId(UShort.MIN, uint(22099));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(22100));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_FirstInGroup =
          new NodeId(UShort.MIN, uint(22101));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState =
          new NodeId(UShort.MIN, uint(22102));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_Id =
          new NodeId(UShort.MIN, uint(22103));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_Name =
          new NodeId(UShort.MIN, uint(22104));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_Number =
          new NodeId(UShort.MIN, uint(22105));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22106));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22107));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22108));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(22109));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(22110));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ReAlarmTime =
          new NodeId(UShort.MIN, uint(22111));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(22112));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Silence =
          new NodeId(UShort.MIN, uint(22113));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Suppress =
          new NodeId(UShort.MIN, uint(22114));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Unsuppress =
          new NodeId(UShort.MIN, uint(22115));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_RemoveFromService =
          new NodeId(UShort.MIN, uint(22116));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_PlaceInService =
          new NodeId(UShort.MIN, uint(22117));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Reset =
          new NodeId(UShort.MIN, uint(22118));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_NormalState =
          new NodeId(UShort.MIN, uint(22119));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ExpirationDate =
          new NodeId(UShort.MIN, uint(22120));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_ExpirationLimit =
          new NodeId(UShort.MIN, uint(22121));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_CertificateType =
          new NodeId(UShort.MIN, uint(22122));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_CertificateExpired_Certificate =
          new NodeId(UShort.MIN, uint(22123));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate =
          new NodeId(UShort.MIN, uint(22124));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EventId =
          new NodeId(UShort.MIN, uint(22125));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EventType =
          new NodeId(UShort.MIN, uint(22126));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SourceNode =
          new NodeId(UShort.MIN, uint(22127));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SourceName =
          new NodeId(UShort.MIN, uint(22128));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Time =
          new NodeId(UShort.MIN, uint(22129));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ReceiveTime =
          new NodeId(UShort.MIN, uint(22130));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LocalTime =
          new NodeId(UShort.MIN, uint(22131));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Message =
          new NodeId(UShort.MIN, uint(22132));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Severity =
          new NodeId(UShort.MIN, uint(22133));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassId =
          new NodeId(UShort.MIN, uint(22134));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionClassName =
          new NodeId(UShort.MIN, uint(22135));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(22136));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(22137));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConditionName =
          new NodeId(UShort.MIN, uint(22138));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_BranchId =
          new NodeId(UShort.MIN, uint(22139));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Retain =
          new NodeId(UShort.MIN, uint(22140));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState =
          new NodeId(UShort.MIN, uint(22141));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Id =
          new NodeId(UShort.MIN, uint(22142));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Name =
          new NodeId(UShort.MIN, uint(22143));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_Number =
          new NodeId(UShort.MIN, uint(22144));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22145));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(22146));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22147));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(22148));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(22149));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Quality =
          new NodeId(UShort.MIN, uint(22150));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22151));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity =
          new NodeId(UShort.MIN, uint(22152));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22153));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Comment =
          new NodeId(UShort.MIN, uint(22154));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22155));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ClientUserId =
          new NodeId(UShort.MIN, uint(22156));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Disable =
          new NodeId(UShort.MIN, uint(22157));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Enable =
          new NodeId(UShort.MIN, uint(22158));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AddComment =
          new NodeId(UShort.MIN, uint(22159));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(22160));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState =
          new NodeId(UShort.MIN, uint(22161));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Id =
          new NodeId(UShort.MIN, uint(22162));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Name =
          new NodeId(UShort.MIN, uint(22163));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_Number =
          new NodeId(UShort.MIN, uint(22164));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22165));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22166));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22167));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(22168));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(22169));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState =
          new NodeId(UShort.MIN, uint(22170));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(22171));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(22172));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(22173));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22174));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22175));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22176));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_TrueState =
          new NodeId(UShort.MIN, uint(22177));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ConfirmedState_FalseState =
          new NodeId(UShort.MIN, uint(22178));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge =
          new NodeId(UShort.MIN, uint(22179));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Acknowledge_InputArguments =
          new NodeId(UShort.MIN, uint(22180));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Confirm =
          new NodeId(UShort.MIN, uint(22181));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Confirm_InputArguments =
          new NodeId(UShort.MIN, uint(22182));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState =
          new NodeId(UShort.MIN, uint(22183));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Id =
          new NodeId(UShort.MIN, uint(22184));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Name =
          new NodeId(UShort.MIN, uint(22185));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_Number =
          new NodeId(UShort.MIN, uint(22186));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22187));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TransitionTime =
          new NodeId(UShort.MIN, uint(22188));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22189));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_TrueState =
          new NodeId(UShort.MIN, uint(22190));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ActiveState_FalseState =
          new NodeId(UShort.MIN, uint(22191));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_InputNode =
          new NodeId(UShort.MIN, uint(22192));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState =
          new NodeId(UShort.MIN, uint(22193));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Id =
          new NodeId(UShort.MIN, uint(22194));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Name =
          new NodeId(UShort.MIN, uint(22195));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_Number =
          new NodeId(UShort.MIN, uint(22196));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22197));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22198));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22199));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_TrueState =
          new NodeId(UShort.MIN, uint(22200));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedState_FalseState =
          new NodeId(UShort.MIN, uint(22201));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState =
          new NodeId(UShort.MIN, uint(22202));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Id =
          new NodeId(UShort.MIN, uint(22203));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Name =
          new NodeId(UShort.MIN, uint(22204));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_Number =
          new NodeId(UShort.MIN, uint(22205));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22206));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TransitionTime =
          new NodeId(UShort.MIN, uint(22207));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22208));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_TrueState =
          new NodeId(UShort.MIN, uint(22209));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OutOfServiceState_FalseState =
          new NodeId(UShort.MIN, uint(22210));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState =
          new NodeId(UShort.MIN, uint(22211));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState =
          new NodeId(UShort.MIN, uint(22212));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Id =
          new NodeId(UShort.MIN, uint(22213));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Name =
          new NodeId(UShort.MIN, uint(22214));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_Number =
          new NodeId(UShort.MIN, uint(22215));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_CurrentState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22216));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition =
          new NodeId(UShort.MIN, uint(22217));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Id =
          new NodeId(UShort.MIN, uint(22218));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Name =
          new NodeId(UShort.MIN, uint(22219));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_Number =
          new NodeId(UShort.MIN, uint(22220));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_TransitionTime =
          new NodeId(UShort.MIN, uint(22221));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_LastTransition_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22222));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableStates =
          new NodeId(UShort.MIN, uint(22223));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_AvailableTransitions =
          new NodeId(UShort.MIN, uint(22224));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_UnshelveTime =
          new NodeId(UShort.MIN, uint(22225));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve =
          new NodeId(UShort.MIN, uint(22226));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_TimedShelve_InputArguments =
          new NodeId(UShort.MIN, uint(22227));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_Unshelve =
          new NodeId(UShort.MIN, uint(22228));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ShelvingState_OneShotShelve =
          new NodeId(UShort.MIN, uint(22229));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SuppressedOrShelved =
          new NodeId(UShort.MIN, uint(22230));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_MaxTimeShelved =
          new NodeId(UShort.MIN, uint(22231));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleEnabled =
          new NodeId(UShort.MIN, uint(22232));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound =
          new NodeId(UShort.MIN, uint(22233));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_ListId =
          new NodeId(UShort.MIN, uint(22234));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_AgencyId =
          new NodeId(UShort.MIN, uint(22235));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_AudibleSound_VersionId =
          new NodeId(UShort.MIN, uint(22236));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState =
          new NodeId(UShort.MIN, uint(22237));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Id =
          new NodeId(UShort.MIN, uint(22238));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Name =
          new NodeId(UShort.MIN, uint(22239));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_Number =
          new NodeId(UShort.MIN, uint(22240));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22241));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TransitionTime =
          new NodeId(UShort.MIN, uint(22242));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22243));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_TrueState =
          new NodeId(UShort.MIN, uint(22244));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_SilenceState_FalseState =
          new NodeId(UShort.MIN, uint(22245));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OnDelay =
          new NodeId(UShort.MIN, uint(22246));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_OffDelay =
          new NodeId(UShort.MIN, uint(22247));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroupFlag =
          new NodeId(UShort.MIN, uint(22248));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_FirstInGroup =
          new NodeId(UShort.MIN, uint(22249));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState =
          new NodeId(UShort.MIN, uint(22250));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Id =
          new NodeId(UShort.MIN, uint(22251));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Name =
          new NodeId(UShort.MIN, uint(22252));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_Number =
          new NodeId(UShort.MIN, uint(22253));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22254));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22255));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22256));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_TrueState =
          new NodeId(UShort.MIN, uint(22257));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LatchedState_FalseState =
          new NodeId(UShort.MIN, uint(22258));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmTime =
          new NodeId(UShort.MIN, uint(22259));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_ReAlarmRepeatCount =
          new NodeId(UShort.MIN, uint(22260));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Silence =
          new NodeId(UShort.MIN, uint(22261));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Suppress =
          new NodeId(UShort.MIN, uint(22262));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Unsuppress =
          new NodeId(UShort.MIN, uint(22263));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_RemoveFromService =
          new NodeId(UShort.MIN, uint(22264));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_PlaceInService =
          new NodeId(UShort.MIN, uint(22265));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_Reset =
          new NodeId(UShort.MIN, uint(22266));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_NormalState =
          new NodeId(UShort.MIN, uint(22267));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_TrustListId =
          new NodeId(UShort.MIN, uint(22268));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_LastUpdateTime =
          new NodeId(UShort.MIN, uint(22269));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultHttpsGroup_TrustListOutOfDate_UpdateFrequency =
          new NodeId(UShort.MIN, uint(22270));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_TrustList_UpdateFrequency =
          new NodeId(UShort.MIN, uint(22271));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired =
          new NodeId(UShort.MIN, uint(22272));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EventId =
          new NodeId(UShort.MIN, uint(22273));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EventType =
          new NodeId(UShort.MIN, uint(22274));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SourceNode =
          new NodeId(UShort.MIN, uint(22275));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_SourceName =
          new NodeId(UShort.MIN, uint(22276));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Time =
          new NodeId(UShort.MIN, uint(22277));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ReceiveTime =
          new NodeId(UShort.MIN, uint(22278));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LocalTime =
          new NodeId(UShort.MIN, uint(22279));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Message =
          new NodeId(UShort.MIN, uint(22280));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Severity =
          new NodeId(UShort.MIN, uint(22281));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionClassId =
          new NodeId(UShort.MIN, uint(22282));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionClassName =
          new NodeId(UShort.MIN, uint(22283));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassId =
          new NodeId(UShort.MIN, uint(22284));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionSubClassName =
          new NodeId(UShort.MIN, uint(22285));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConditionName =
          new NodeId(UShort.MIN, uint(22286));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_BranchId =
          new NodeId(UShort.MIN, uint(22287));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Retain =
          new NodeId(UShort.MIN, uint(22288));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState =
          new NodeId(UShort.MIN, uint(22289));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_Id =
          new NodeId(UShort.MIN, uint(22290));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_Name =
          new NodeId(UShort.MIN, uint(22291));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_Number =
          new NodeId(UShort.MIN, uint(22292));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22293));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_TransitionTime =
          new NodeId(UShort.MIN, uint(22294));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22295));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_TrueState =
          new NodeId(UShort.MIN, uint(22296));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_EnabledState_FalseState =
          new NodeId(UShort.MIN, uint(22297));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Quality =
          new NodeId(UShort.MIN, uint(22298));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Quality_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22299));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LastSeverity =
          new NodeId(UShort.MIN, uint(22300));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_LastSeverity_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22301));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Comment =
          new NodeId(UShort.MIN, uint(22302));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Comment_SourceTimestamp =
          new NodeId(UShort.MIN, uint(22303));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ClientUserId =
          new NodeId(UShort.MIN, uint(22304));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Disable =
          new NodeId(UShort.MIN, uint(22305));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_Enable =
          new NodeId(UShort.MIN, uint(22306));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AddComment =
          new NodeId(UShort.MIN, uint(22307));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AddComment_InputArguments =
          new NodeId(UShort.MIN, uint(22308));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState =
          new NodeId(UShort.MIN, uint(22309));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_Id =
          new NodeId(UShort.MIN, uint(22310));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_Name =
          new NodeId(UShort.MIN, uint(22311));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_Number =
          new NodeId(UShort.MIN, uint(22312));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22313));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22314));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_EffectiveTransitionTime =
          new NodeId(UShort.MIN, uint(22315));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_TrueState =
          new NodeId(UShort.MIN, uint(22316));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_AckedState_FalseState =
          new NodeId(UShort.MIN, uint(22317));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState =
          new NodeId(UShort.MIN, uint(22318));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Id =
          new NodeId(UShort.MIN, uint(22319));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Name =
          new NodeId(UShort.MIN, uint(22320));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_Number =
          new NodeId(UShort.MIN, uint(22321));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_EffectiveDisplayName =
          new NodeId(UShort.MIN, uint(22322));

  public static final NodeId
      ServerConfigurationType_CertificateGroups_DefaultUserTokenGroup_CertificateExpired_ConfirmedState_TransitionTime =
          new NodeId(UShort.MIN, uint(22323));
}
