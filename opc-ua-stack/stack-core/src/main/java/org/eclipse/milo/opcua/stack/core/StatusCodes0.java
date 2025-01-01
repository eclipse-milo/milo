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

import org.eclipse.milo.opcua.stack.core.util.annotations.Description;

abstract class StatusCodes0 {
  /** The operation succeeded. */
  @Description("The operation succeeded.")
  public static final long Good = 0x00000000L;

  /** The operation was uncertain. */
  @Description("The operation was uncertain.")
  public static final long Uncertain = 0x40000000L;

  /** The operation failed. */
  @Description("The operation failed.")
  public static final long Bad = 0x80000000L;

  /** An unexpected error occurred. */
  @Description("An unexpected error occurred.")
  public static final long Bad_UnexpectedError = 0x80010000L;

  /** An internal error occurred as a result of a programming or configuration error. */
  @Description("An internal error occurred as a result of a programming or configuration error.")
  public static final long Bad_InternalError = 0x80020000L;

  /** Not enough memory to complete the operation. */
  @Description("Not enough memory to complete the operation.")
  public static final long Bad_OutOfMemory = 0x80030000L;

  /** An operating system resource is not available. */
  @Description("An operating system resource is not available.")
  public static final long Bad_ResourceUnavailable = 0x80040000L;

  /** A low level communication error occurred. */
  @Description("A low level communication error occurred.")
  public static final long Bad_CommunicationError = 0x80050000L;

  /** Encoding halted because of invalid data in the objects being serialized. */
  @Description("Encoding halted because of invalid data in the objects being serialized.")
  public static final long Bad_EncodingError = 0x80060000L;

  /** Decoding halted because of invalid data in the stream. */
  @Description("Decoding halted because of invalid data in the stream.")
  public static final long Bad_DecodingError = 0x80070000L;

  /** The message encoding/decoding limits imposed by the stack have been exceeded. */
  @Description("The message encoding/decoding limits imposed by the stack have been exceeded.")
  public static final long Bad_EncodingLimitsExceeded = 0x80080000L;

  /** The request message size exceeds limits set by the server. */
  @Description("The request message size exceeds limits set by the server.")
  public static final long Bad_RequestTooLarge = 0x80B80000L;

  /** The response message size exceeds limits set by the client. */
  @Description("The response message size exceeds limits set by the client.")
  public static final long Bad_ResponseTooLarge = 0x80B90000L;

  /** An unrecognized response was received from the server. */
  @Description("An unrecognized response was received from the server.")
  public static final long Bad_UnknownResponse = 0x80090000L;

  /** The operation timed out. */
  @Description("The operation timed out.")
  public static final long Bad_Timeout = 0x800A0000L;

  /** The server does not support the requested service. */
  @Description("The server does not support the requested service.")
  public static final long Bad_ServiceUnsupported = 0x800B0000L;

  /** The operation was cancelled because the application is shutting down. */
  @Description("The operation was cancelled because the application is shutting down.")
  public static final long Bad_Shutdown = 0x800C0000L;

  /** The operation could not complete because the client is not connected to the server. */
  @Description(
      "The operation could not complete because the client is not connected to the server.")
  public static final long Bad_ServerNotConnected = 0x800D0000L;

  /** The server has stopped and cannot process any requests. */
  @Description("The server has stopped and cannot process any requests.")
  public static final long Bad_ServerHalted = 0x800E0000L;

  /** No processing could be done because there was nothing to do. */
  @Description("No processing could be done because there was nothing to do.")
  public static final long Bad_NothingToDo = 0x800F0000L;

  /** The request could not be processed because it specified too many operations. */
  @Description("The request could not be processed because it specified too many operations.")
  public static final long Bad_TooManyOperations = 0x80100000L;

  /**
   * The request could not be processed because there are too many monitored items in the
   * subscription.
   */
  @Description(
      "The request could not be processed because there are too many monitored items in the"
          + " subscription.")
  public static final long Bad_TooManyMonitoredItems = 0x80DB0000L;

  /** The extension object cannot be (de)serialized because the data type id is not recognized. */
  @Description(
      "The extension object cannot be (de)serialized because the data type id is not recognized.")
  public static final long Bad_DataTypeIdUnknown = 0x80110000L;

  /** The certificate provided as a parameter is not valid. */
  @Description("The certificate provided as a parameter is not valid.")
  public static final long Bad_CertificateInvalid = 0x80120000L;

  /** An error occurred verifying security. */
  @Description("An error occurred verifying security.")
  public static final long Bad_SecurityChecksFailed = 0x80130000L;

  /** The certificate does not meet the requirements of the security policy. */
  @Description("The certificate does not meet the requirements of the security policy.")
  public static final long Bad_CertificatePolicyCheckFailed = 0x81140000L;

  /** The certificate has expired or is not yet valid. */
  @Description("The certificate has expired or is not yet valid.")
  public static final long Bad_CertificateTimeInvalid = 0x80140000L;

  /** An issuer certificate has expired or is not yet valid. */
  @Description("An issuer certificate has expired or is not yet valid.")
  public static final long Bad_CertificateIssuerTimeInvalid = 0x80150000L;

  /** The HostName used to connect to a server does not match a HostName in the certificate. */
  @Description(
      "The HostName used to connect to a server does not match a HostName in the certificate.")
  public static final long Bad_CertificateHostNameInvalid = 0x80160000L;

  /** The URI specified in the ApplicationDescription does not match the URI in the certificate. */
  @Description(
      "The URI specified in the ApplicationDescription does not match the URI in the certificate.")
  public static final long Bad_CertificateUriInvalid = 0x80170000L;

  /** The certificate may not be used for the requested operation. */
  @Description("The certificate may not be used for the requested operation.")
  public static final long Bad_CertificateUseNotAllowed = 0x80180000L;

  /** The issuer certificate may not be used for the requested operation. */
  @Description("The issuer certificate may not be used for the requested operation.")
  public static final long Bad_CertificateIssuerUseNotAllowed = 0x80190000L;

  /** The certificate is not trusted. */
  @Description("The certificate is not trusted.")
  public static final long Bad_CertificateUntrusted = 0x801A0000L;

  /** It was not possible to determine if the certificate has been revoked. */
  @Description("It was not possible to determine if the certificate has been revoked.")
  public static final long Bad_CertificateRevocationUnknown = 0x801B0000L;

  /** It was not possible to determine if the issuer certificate has been revoked. */
  @Description("It was not possible to determine if the issuer certificate has been revoked.")
  public static final long Bad_CertificateIssuerRevocationUnknown = 0x801C0000L;

  /** The certificate has been revoked. */
  @Description("The certificate has been revoked.")
  public static final long Bad_CertificateRevoked = 0x801D0000L;

  /** The issuer certificate has been revoked. */
  @Description("The issuer certificate has been revoked.")
  public static final long Bad_CertificateIssuerRevoked = 0x801E0000L;

  /** The certificate chain is incomplete. */
  @Description("The certificate chain is incomplete.")
  public static final long Bad_CertificateChainIncomplete = 0x810D0000L;

  /** User does not have permission to perform the requested operation. */
  @Description("User does not have permission to perform the requested operation.")
  public static final long Bad_UserAccessDenied = 0x801F0000L;

  /** The user identity token is not valid. */
  @Description("The user identity token is not valid.")
  public static final long Bad_IdentityTokenInvalid = 0x80200000L;

  /** The user identity token is valid but the server has rejected it. */
  @Description("The user identity token is valid but the server has rejected it.")
  public static final long Bad_IdentityTokenRejected = 0x80210000L;

  /** The specified secure channel is no longer valid. */
  @Description("The specified secure channel is no longer valid.")
  public static final long Bad_SecureChannelIdInvalid = 0x80220000L;

  /** The timestamp is outside the range allowed by the server. */
  @Description("The timestamp is outside the range allowed by the server.")
  public static final long Bad_InvalidTimestamp = 0x80230000L;

  /** The nonce does appear to be not a random value or it is not the correct length. */
  @Description("The nonce does appear to be not a random value or it is not the correct length.")
  public static final long Bad_NonceInvalid = 0x80240000L;

  /** The session id is not valid. */
  @Description("The session id is not valid.")
  public static final long Bad_SessionIdInvalid = 0x80250000L;

  /** The session was closed by the client. */
  @Description("The session was closed by the client.")
  public static final long Bad_SessionClosed = 0x80260000L;

  /** The session cannot be used because ActivateSession has not been called. */
  @Description("The session cannot be used because ActivateSession has not been called.")
  public static final long Bad_SessionNotActivated = 0x80270000L;

  /** The subscription id is not valid. */
  @Description("The subscription id is not valid.")
  public static final long Bad_SubscriptionIdInvalid = 0x80280000L;

  /** The header for the request is missing or invalid. */
  @Description("The header for the request is missing or invalid.")
  public static final long Bad_RequestHeaderInvalid = 0x802A0000L;

  /** The timestamps to return parameter is invalid. */
  @Description("The timestamps to return parameter is invalid.")
  public static final long Bad_TimestampsToReturnInvalid = 0x802B0000L;

  /** The request was cancelled by the client. */
  @Description("The request was cancelled by the client.")
  public static final long Bad_RequestCancelledByClient = 0x802C0000L;

  /** Too many arguments were provided. */
  @Description("Too many arguments were provided.")
  public static final long Bad_TooManyArguments = 0x80E50000L;

  /**
   * The server requires a license to operate in general or to perform a service or operation, but
   * existing license is expired.
   */
  @Description(
      "The server requires a license to operate in general or to perform a service or operation,"
          + " but existing license is expired.")
  public static final long Bad_LicenseExpired = 0x810E0000L;

  /**
   * The server has limits on number of allowed operations / objects, based on installed licenses,
   * and these limits where exceeded.
   */
  @Description(
      "The server has limits on number of allowed operations / objects, based on installed"
          + " licenses, and these limits where exceeded.")
  public static final long Bad_LicenseLimitsExceeded = 0x810F0000L;

  /**
   * The server does not have a license which is required to operate in general or to perform a
   * service or operation.
   */
  @Description(
      "The server does not have a license which is required to operate in general or to perform a"
          + " service or operation.")
  public static final long Bad_LicenseNotAvailable = 0x81100000L;

  /** The subscription was transferred to another session. */
  @Description("The subscription was transferred to another session.")
  public static final long Good_SubscriptionTransferred = 0x002D0000L;

  /** The processing will complete asynchronously. */
  @Description("The processing will complete asynchronously.")
  public static final long Good_CompletesAsynchronously = 0x002E0000L;

  /** Sampling has slowed down due to resource limitations. */
  @Description("Sampling has slowed down due to resource limitations.")
  public static final long Good_Overload = 0x002F0000L;

  /** The value written was accepted but was clamped. */
  @Description("The value written was accepted but was clamped.")
  public static final long Good_Clamped = 0x00300000L;

  /**
   * Communication with the data source is defined, but not established, and there is no last known
   * value available.
   */
  @Description(
      "Communication with the data source is defined, but not established, and there is no last"
          + " known value available.")
  public static final long Bad_NoCommunication = 0x80310000L;

  /** Waiting for the server to obtain values from the underlying data source. */
  @Description("Waiting for the server to obtain values from the underlying data source.")
  public static final long Bad_WaitingForInitialData = 0x80320000L;

  /** The syntax of the node id is not valid. */
  @Description("The syntax of the node id is not valid.")
  public static final long Bad_NodeIdInvalid = 0x80330000L;

  /** The node id refers to a node that does not exist in the server address space. */
  @Description("The node id refers to a node that does not exist in the server address space.")
  public static final long Bad_NodeIdUnknown = 0x80340000L;

  /** The attribute is not supported for the specified Node. */
  @Description("The attribute is not supported for the specified Node.")
  public static final long Bad_AttributeIdInvalid = 0x80350000L;

  /** The syntax of the index range parameter is invalid. */
  @Description("The syntax of the index range parameter is invalid.")
  public static final long Bad_IndexRangeInvalid = 0x80360000L;

  /** No data exists within the range of indexes specified. */
  @Description("No data exists within the range of indexes specified.")
  public static final long Bad_IndexRangeNoData = 0x80370000L;

  /** The data encoding is invalid. */
  @Description("The data encoding is invalid.")
  public static final long Bad_DataEncodingInvalid = 0x80380000L;

  /** The server does not support the requested data encoding for the node. */
  @Description("The server does not support the requested data encoding for the node.")
  public static final long Bad_DataEncodingUnsupported = 0x80390000L;

  /** The access level does not allow reading or subscribing to the Node. */
  @Description("The access level does not allow reading or subscribing to the Node.")
  public static final long Bad_NotReadable = 0x803A0000L;

  /** The access level does not allow writing to the Node. */
  @Description("The access level does not allow writing to the Node.")
  public static final long Bad_NotWritable = 0x803B0000L;

  /** The value was out of range. */
  @Description("The value was out of range.")
  public static final long Bad_OutOfRange = 0x803C0000L;

  /** The requested operation is not supported. */
  @Description("The requested operation is not supported.")
  public static final long Bad_NotSupported = 0x803D0000L;

  /** A requested item was not found or a search operation ended without success. */
  @Description("A requested item was not found or a search operation ended without success.")
  public static final long Bad_NotFound = 0x803E0000L;

  /** The object cannot be used because it has been deleted. */
  @Description("The object cannot be used because it has been deleted.")
  public static final long Bad_ObjectDeleted = 0x803F0000L;

  /** Requested operation is not implemented. */
  @Description("Requested operation is not implemented.")
  public static final long Bad_NotImplemented = 0x80400000L;

  /** The monitoring mode is invalid. */
  @Description("The monitoring mode is invalid.")
  public static final long Bad_MonitoringModeInvalid = 0x80410000L;

  /** The monitoring item id does not refer to a valid monitored item. */
  @Description("The monitoring item id does not refer to a valid monitored item.")
  public static final long Bad_MonitoredItemIdInvalid = 0x80420000L;

  /** The monitored item filter parameter is not valid. */
  @Description("The monitored item filter parameter is not valid.")
  public static final long Bad_MonitoredItemFilterInvalid = 0x80430000L;

  /** The server does not support the requested monitored item filter. */
  @Description("The server does not support the requested monitored item filter.")
  public static final long Bad_MonitoredItemFilterUnsupported = 0x80440000L;

  /** A monitoring filter cannot be used in combination with the attribute specified. */
  @Description("A monitoring filter cannot be used in combination with the attribute specified.")
  public static final long Bad_FilterNotAllowed = 0x80450000L;

  /** A mandatory structured parameter was missing or null. */
  @Description("A mandatory structured parameter was missing or null.")
  public static final long Bad_StructureMissing = 0x80460000L;

  /** The event filter is not valid. */
  @Description("The event filter is not valid.")
  public static final long Bad_EventFilterInvalid = 0x80470000L;

  /** The content filter is not valid. */
  @Description("The content filter is not valid.")
  public static final long Bad_ContentFilterInvalid = 0x80480000L;

  /** An unrecognized operator was provided in a filter. */
  @Description("An unrecognized operator was provided in a filter.")
  public static final long Bad_FilterOperatorInvalid = 0x80C10000L;

  /**
   * A valid operator was provided, but the server does not provide support for this filter
   * operator.
   */
  @Description(
      "A valid operator was provided, but the server does not provide support for this filter"
          + " operator.")
  public static final long Bad_FilterOperatorUnsupported = 0x80C20000L;

  /**
   * The number of operands provided for the filter operator was less then expected for the operand
   * provided.
   */
  @Description(
      "The number of operands provided for the filter operator was less then expected for the"
          + " operand provided.")
  public static final long Bad_FilterOperandCountMismatch = 0x80C30000L;

  /** The operand used in a content filter is not valid. */
  @Description("The operand used in a content filter is not valid.")
  public static final long Bad_FilterOperandInvalid = 0x80490000L;

  /** The referenced element is not a valid element in the content filter. */
  @Description("The referenced element is not a valid element in the content filter.")
  public static final long Bad_FilterElementInvalid = 0x80C40000L;

  /** The referenced literal is not a valid value. */
  @Description("The referenced literal is not a valid value.")
  public static final long Bad_FilterLiteralInvalid = 0x80C50000L;

  /** The continuation point provide is longer valid. */
  @Description("The continuation point provide is longer valid.")
  public static final long Bad_ContinuationPointInvalid = 0x804A0000L;

  /** The operation could not be processed because all continuation points have been allocated. */
  @Description(
      "The operation could not be processed because all continuation points have been allocated.")
  public static final long Bad_NoContinuationPoints = 0x804B0000L;

  /** The reference type id does not refer to a valid reference type node. */
  @Description("The reference type id does not refer to a valid reference type node.")
  public static final long Bad_ReferenceTypeIdInvalid = 0x804C0000L;

  /** The browse direction is not valid. */
  @Description("The browse direction is not valid.")
  public static final long Bad_BrowseDirectionInvalid = 0x804D0000L;

  /** The node is not part of the view. */
  @Description("The node is not part of the view.")
  public static final long Bad_NodeNotInView = 0x804E0000L;

  /** The number was not accepted because of a numeric overflow. */
  @Description("The number was not accepted because of a numeric overflow.")
  public static final long Bad_NumericOverflow = 0x81120000L;

  /** The ServerUri is not a valid URI. */
  @Description("The ServerUri is not a valid URI.")
  public static final long Bad_ServerUriInvalid = 0x804F0000L;

  /** No ServerName was specified. */
  @Description("No ServerName was specified.")
  public static final long Bad_ServerNameMissing = 0x80500000L;

  /** No DiscoveryUrl was specified. */
  @Description("No DiscoveryUrl was specified.")
  public static final long Bad_DiscoveryUrlMissing = 0x80510000L;

  /** The semaphore file specified by the client is not valid. */
  @Description("The semaphore file specified by the client is not valid.")
  public static final long Bad_SempahoreFileMissing = 0x80520000L;

  /** The security token request type is not valid. */
  @Description("The security token request type is not valid.")
  public static final long Bad_RequestTypeInvalid = 0x80530000L;

  /** The security mode does not meet the requirements set by the server. */
  @Description("The security mode does not meet the requirements set by the server.")
  public static final long Bad_SecurityModeRejected = 0x80540000L;

  /** The security policy does not meet the requirements set by the server. */
  @Description("The security policy does not meet the requirements set by the server.")
  public static final long Bad_SecurityPolicyRejected = 0x80550000L;

  /** The server has reached its maximum number of sessions. */
  @Description("The server has reached its maximum number of sessions.")
  public static final long Bad_TooManySessions = 0x80560000L;

  /** The user token signature is missing or invalid. */
  @Description("The user token signature is missing or invalid.")
  public static final long Bad_UserSignatureInvalid = 0x80570000L;

  /** The signature generated with the client certificate is missing or invalid. */
  @Description("The signature generated with the client certificate is missing or invalid.")
  public static final long Bad_ApplicationSignatureInvalid = 0x80580000L;

  /**
   * The client did not provide at least one software certificate that is valid and meets the
   * profile requirements for the server.
   */
  @Description(
      "The client did not provide at least one software certificate that is valid and meets the"
          + " profile requirements for the server.")
  public static final long Bad_NoValidCertificates = 0x80590000L;

  /** The server does not support changing the user identity assigned to the session. */
  @Description("The server does not support changing the user identity assigned to the session.")
  public static final long Bad_IdentityChangeNotSupported = 0x80C60000L;

  /** The request was cancelled by the client with the Cancel service. */
  @Description("The request was cancelled by the client with the Cancel service.")
  public static final long Bad_RequestCancelledByRequest = 0x805A0000L;

  /** The parent node id does not to refer to a valid node. */
  @Description("The parent node id does not to refer to a valid node.")
  public static final long Bad_ParentNodeIdInvalid = 0x805B0000L;

  /**
   * The reference could not be created because it violates constraints imposed by the data model.
   */
  @Description(
      "The reference could not be created because it violates constraints imposed by the data"
          + " model.")
  public static final long Bad_ReferenceNotAllowed = 0x805C0000L;

  /**
   * The requested node id was reject because it was either invalid or server does not allow node
   * ids to be specified by the client.
   */
  @Description(
      "The requested node id was reject because it was either invalid or server does not allow node"
          + " ids to be specified by the client.")
  public static final long Bad_NodeIdRejected = 0x805D0000L;

  /** The requested node id is already used by another node. */
  @Description("The requested node id is already used by another node.")
  public static final long Bad_NodeIdExists = 0x805E0000L;

  /** The node class is not valid. */
  @Description("The node class is not valid.")
  public static final long Bad_NodeClassInvalid = 0x805F0000L;

  /** The browse name is invalid. */
  @Description("The browse name is invalid.")
  public static final long Bad_BrowseNameInvalid = 0x80600000L;

  /** The browse name is not unique among nodes that share the same relationship with the parent. */
  @Description(
      "The browse name is not unique among nodes that share the same relationship with the parent.")
  public static final long Bad_BrowseNameDuplicated = 0x80610000L;

  /** The node attributes are not valid for the node class. */
  @Description("The node attributes are not valid for the node class.")
  public static final long Bad_NodeAttributesInvalid = 0x80620000L;

  /** The type definition node id does not reference an appropriate type node. */
  @Description("The type definition node id does not reference an appropriate type node.")
  public static final long Bad_TypeDefinitionInvalid = 0x80630000L;

  /** The source node id does not reference a valid node. */
  @Description("The source node id does not reference a valid node.")
  public static final long Bad_SourceNodeIdInvalid = 0x80640000L;

  /** The target node id does not reference a valid node. */
  @Description("The target node id does not reference a valid node.")
  public static final long Bad_TargetNodeIdInvalid = 0x80650000L;

  /** The reference type between the nodes is already defined. */
  @Description("The reference type between the nodes is already defined.")
  public static final long Bad_DuplicateReferenceNotAllowed = 0x80660000L;

  /** The server does not allow this type of self reference on this node. */
  @Description("The server does not allow this type of self reference on this node.")
  public static final long Bad_InvalidSelfReference = 0x80670000L;

  /** The reference type is not valid for a reference to a remote server. */
  @Description("The reference type is not valid for a reference to a remote server.")
  public static final long Bad_ReferenceLocalOnly = 0x80680000L;

  /** The server will not allow the node to be deleted. */
  @Description("The server will not allow the node to be deleted.")
  public static final long Bad_NoDeleteRights = 0x80690000L;

  /** The server was not able to delete all target references. */
  @Description("The server was not able to delete all target references.")
  public static final long Uncertain_ReferenceNotDeleted = 0x40BC0000L;

  /** The server index is not valid. */
  @Description("The server index is not valid.")
  public static final long Bad_ServerIndexInvalid = 0x806A0000L;

  /** The view id does not refer to a valid view node. */
  @Description("The view id does not refer to a valid view node.")
  public static final long Bad_ViewIdUnknown = 0x806B0000L;

  /** The view timestamp is not available or not supported. */
  @Description("The view timestamp is not available or not supported.")
  public static final long Bad_ViewTimestampInvalid = 0x80C90000L;

  /** The view parameters are not consistent with each other. */
  @Description("The view parameters are not consistent with each other.")
  public static final long Bad_ViewParameterMismatch = 0x80CA0000L;

  /** The view version is not available or not supported. */
  @Description("The view version is not available or not supported.")
  public static final long Bad_ViewVersionInvalid = 0x80CB0000L;

  /** The list of references may not be complete because the underlying system is not available. */
  @Description(
      "The list of references may not be complete because the underlying system is not available.")
  public static final long Uncertain_NotAllNodesAvailable = 0x40C00000L;

  /**
   * The server should have followed a reference to a node in a remote server but did not. The
   * result set may be incomplete.
   */
  @Description(
      "The server should have followed a reference to a node in a remote server but did not. The"
          + " result set may be incomplete.")
  public static final long Good_ResultsMayBeIncomplete = 0x00BA0000L;

  /** The provided Nodeid was not a type definition nodeid. */
  @Description("The provided Nodeid was not a type definition nodeid.")
  public static final long Bad_NotTypeDefinition = 0x80C80000L;

  /**
   * One of the references to follow in the relative path references to a node in the address space
   * in another server.
   */
  @Description(
      "One of the references to follow in the relative path references to a node in the address"
          + " space in another server.")
  public static final long Uncertain_ReferenceOutOfServer = 0x406C0000L;

  /** The requested operation has too many matches to return. */
  @Description("The requested operation has too many matches to return.")
  public static final long Bad_TooManyMatches = 0x806D0000L;

  /** The requested operation requires too many resources in the server. */
  @Description("The requested operation requires too many resources in the server.")
  public static final long Bad_QueryTooComplex = 0x806E0000L;

  /** The requested operation has no match to return. */
  @Description("The requested operation has no match to return.")
  public static final long Bad_NoMatch = 0x806F0000L;

  /** The max age parameter is invalid. */
  @Description("The max age parameter is invalid.")
  public static final long Bad_MaxAgeInvalid = 0x80700000L;

  /** The operation is not permitted over the current secure channel. */
  @Description("The operation is not permitted over the current secure channel.")
  public static final long Bad_SecurityModeInsufficient = 0x80E60000L;

  /** The history details parameter is not valid. */
  @Description("The history details parameter is not valid.")
  public static final long Bad_HistoryOperationInvalid = 0x80710000L;

  /** The server does not support the requested operation. */
  @Description("The server does not support the requested operation.")
  public static final long Bad_HistoryOperationUnsupported = 0x80720000L;

  /** The defined timestamp to return was invalid. */
  @Description("The defined timestamp to return was invalid.")
  public static final long Bad_InvalidTimestampArgument = 0x80BD0000L;

  /**
   * The server does not support writing the combination of value, status and timestamps provided.
   */
  @Description(
      "The server does not support writing the combination of value, status and timestamps"
          + " provided.")
  public static final long Bad_WriteNotSupported = 0x80730000L;

  /** The value supplied for the attribute is not of the same type as the attribute's value. */
  @Description(
      "The value supplied for the attribute is not of the same type as the attribute's value.")
  public static final long Bad_TypeMismatch = 0x80740000L;

  /** The method id does not refer to a method for the specified object. */
  @Description("The method id does not refer to a method for the specified object.")
  public static final long Bad_MethodInvalid = 0x80750000L;

  /** The client did not specify all of the input arguments for the method. */
  @Description("The client did not specify all of the input arguments for the method.")
  public static final long Bad_ArgumentsMissing = 0x80760000L;

  /** The executable attribute does not allow the execution of the method. */
  @Description("The executable attribute does not allow the execution of the method.")
  public static final long Bad_NotExecutable = 0x81110000L;

  /** The server has reached its maximum number of subscriptions. */
  @Description("The server has reached its maximum number of subscriptions.")
  public static final long Bad_TooManySubscriptions = 0x80770000L;

  /** The server has reached the maximum number of queued publish requests. */
  @Description("The server has reached the maximum number of queued publish requests.")
  public static final long Bad_TooManyPublishRequests = 0x80780000L;

  /** There is no subscription available for this session. */
  @Description("There is no subscription available for this session.")
  public static final long Bad_NoSubscription = 0x80790000L;

  /** The sequence number is unknown to the server. */
  @Description("The sequence number is unknown to the server.")
  public static final long Bad_SequenceNumberUnknown = 0x807A0000L;

  /**
   * The Server does not support retransmission queue and acknowledgement of sequence numbers is not
   * available.
   */
  @Description(
      "The Server does not support retransmission queue and acknowledgement of sequence numbers is"
          + " not available.")
  public static final long Good_RetransmissionQueueNotSupported = 0x00DF0000L;

  /** The requested notification message is no longer available. */
  @Description("The requested notification message is no longer available.")
  public static final long Bad_MessageNotAvailable = 0x807B0000L;

  /**
   * The client of the current session does not support one or more Profiles that are necessary for
   * the subscription.
   */
  @Description(
      "The client of the current session does not support one or more Profiles that are necessary"
          + " for the subscription.")
  public static final long Bad_InsufficientClientProfile = 0x807C0000L;

  /** The sub-state machine is not currently active. */
  @Description("The sub-state machine is not currently active.")
  public static final long Bad_StateNotActive = 0x80BF0000L;

  /** An equivalent rule already exists. */
  @Description("An equivalent rule already exists.")
  public static final long Bad_AlreadyExists = 0x81150000L;

  /** The server cannot process the request because it is too busy. */
  @Description("The server cannot process the request because it is too busy.")
  public static final long Bad_TcpServerTooBusy = 0x807D0000L;

  /** The type of the message specified in the header invalid. */
  @Description("The type of the message specified in the header invalid.")
  public static final long Bad_TcpMessageTypeInvalid = 0x807E0000L;

  /** The SecureChannelId and/or TokenId are not currently in use. */
  @Description("The SecureChannelId and/or TokenId are not currently in use.")
  public static final long Bad_TcpSecureChannelUnknown = 0x807F0000L;

  /** The size of the message chunk specified in the header is too large. */
  @Description("The size of the message chunk specified in the header is too large.")
  public static final long Bad_TcpMessageTooLarge = 0x80800000L;

  /** There are not enough resources to process the request. */
  @Description("There are not enough resources to process the request.")
  public static final long Bad_TcpNotEnoughResources = 0x80810000L;

  /** An internal error occurred. */
  @Description("An internal error occurred.")
  public static final long Bad_TcpInternalError = 0x80820000L;

  /** The server does not recognize the QueryString specified. */
  @Description("The server does not recognize the QueryString specified.")
  public static final long Bad_TcpEndpointUrlInvalid = 0x80830000L;

  /** The request could not be sent because of a network interruption. */
  @Description("The request could not be sent because of a network interruption.")
  public static final long Bad_RequestInterrupted = 0x80840000L;

  /** Timeout occurred while processing the request. */
  @Description("Timeout occurred while processing the request.")
  public static final long Bad_RequestTimeout = 0x80850000L;

  /** The secure channel has been closed. */
  @Description("The secure channel has been closed.")
  public static final long Bad_SecureChannelClosed = 0x80860000L;

  /** The token has expired or is not recognized. */
  @Description("The token has expired or is not recognized.")
  public static final long Bad_SecureChannelTokenUnknown = 0x80870000L;

  /** The sequence number is not valid. */
  @Description("The sequence number is not valid.")
  public static final long Bad_SequenceNumberInvalid = 0x80880000L;

  /** The applications do not have compatible protocol versions. */
  @Description("The applications do not have compatible protocol versions.")
  public static final long Bad_ProtocolVersionUnsupported = 0x80BE0000L;

  /** There is a problem with the configuration that affects the usefulness of the value. */
  @Description(
      "There is a problem with the configuration that affects the usefulness of the value.")
  public static final long Bad_ConfigurationError = 0x80890000L;

  /**
   * The variable should receive its value from another variable, but has never been configured to
   * do so.
   */
  @Description(
      "The variable should receive its value from another variable, but has never been configured"
          + " to do so.")
  public static final long Bad_NotConnected = 0x808A0000L;

  /**
   * There has been a failure in the device/data source that generates the value that has affected
   * the value.
   */
  @Description(
      "There has been a failure in the device/data source that generates the value that has"
          + " affected the value.")
  public static final long Bad_DeviceFailure = 0x808B0000L;

  /**
   * There has been a failure in the sensor from which the value is derived by the device/data
   * source.
   */
  @Description(
      "There has been a failure in the sensor from which the value is derived by the device/data"
          + " source.")
  public static final long Bad_SensorFailure = 0x808C0000L;

  /** The source of the data is not operational. */
  @Description("The source of the data is not operational.")
  public static final long Bad_OutOfService = 0x808D0000L;

  /** The deadband filter is not valid. */
  @Description("The deadband filter is not valid.")
  public static final long Bad_DeadbandFilterInvalid = 0x808E0000L;

  /**
   * Communication to the data source has failed. The variable value is the last value that had a
   * good quality.
   */
  @Description(
      "Communication to the data source has failed. The variable value is the last value that had a"
          + " good quality.")
  public static final long Uncertain_NoCommunicationLastUsableValue = 0x408F0000L;

  /** Whatever was updating this value has stopped doing so. */
  @Description("Whatever was updating this value has stopped doing so.")
  public static final long Uncertain_LastUsableValue = 0x40900000L;

  /** The value is an operational value that was manually overwritten. */
  @Description("The value is an operational value that was manually overwritten.")
  public static final long Uncertain_SubstituteValue = 0x40910000L;

  /**
   * The value is an initial value for a variable that normally receives its value from another
   * variable.
   */
  @Description(
      "The value is an initial value for a variable that normally receives its value from another"
          + " variable.")
  public static final long Uncertain_InitialValue = 0x40920000L;

  /** The value is at one of the sensor limits. */
  @Description("The value is at one of the sensor limits.")
  public static final long Uncertain_SensorNotAccurate = 0x40930000L;

  /** The value is outside of the range of values defined for this parameter. */
  @Description("The value is outside of the range of values defined for this parameter.")
  public static final long Uncertain_EngineeringUnitsExceeded = 0x40940000L;

  /**
   * The value is derived from multiple sources and has less than the required number of Good
   * sources.
   */
  @Description(
      "The value is derived from multiple sources and has less than the required number of Good"
          + " sources.")
  public static final long Uncertain_SubNormal = 0x40950000L;

  /** The value has been overridden. */
  @Description("The value has been overridden.")
  public static final long Good_LocalOverride = 0x00960000L;

  /** This Condition refresh failed, a Condition refresh operation is already in progress. */
  @Description(
      "This Condition refresh failed, a Condition refresh operation is already in progress.")
  public static final long Bad_RefreshInProgress = 0x80970000L;

  /** This condition has already been disabled. */
  @Description("This condition has already been disabled.")
  public static final long Bad_ConditionAlreadyDisabled = 0x80980000L;

  /** This condition has already been enabled. */
  @Description("This condition has already been enabled.")
  public static final long Bad_ConditionAlreadyEnabled = 0x80CC0000L;

  /** Property not available, this condition is disabled. */
  @Description("Property not available, this condition is disabled.")
  public static final long Bad_ConditionDisabled = 0x80990000L;

  /** The specified event id is not recognized. */
  @Description("The specified event id is not recognized.")
  public static final long Bad_EventIdUnknown = 0x809A0000L;

  /** The event cannot be acknowledged. */
  @Description("The event cannot be acknowledged.")
  public static final long Bad_EventNotAcknowledgeable = 0x80BB0000L;

  /** The dialog condition is not active. */
  @Description("The dialog condition is not active.")
  public static final long Bad_DialogNotActive = 0x80CD0000L;

  /** The response is not valid for the dialog. */
  @Description("The response is not valid for the dialog.")
  public static final long Bad_DialogResponseInvalid = 0x80CE0000L;

  /** The condition branch has already been acknowledged. */
  @Description("The condition branch has already been acknowledged.")
  public static final long Bad_ConditionBranchAlreadyAcked = 0x80CF0000L;

  /** The condition branch has already been confirmed. */
  @Description("The condition branch has already been confirmed.")
  public static final long Bad_ConditionBranchAlreadyConfirmed = 0x80D00000L;

  /** The condition has already been shelved. */
  @Description("The condition has already been shelved.")
  public static final long Bad_ConditionAlreadyShelved = 0x80D10000L;

  /** The condition is not currently shelved. */
  @Description("The condition is not currently shelved.")
  public static final long Bad_ConditionNotShelved = 0x80D20000L;

  /** The shelving time not within an acceptable range. */
  @Description("The shelving time not within an acceptable range.")
  public static final long Bad_ShelvingTimeOutOfRange = 0x80D30000L;

  /** No data exists for the requested time range or event filter. */
  @Description("No data exists for the requested time range or event filter.")
  public static final long Bad_NoData = 0x809B0000L;

  /** No data found to provide upper or lower bound value. */
  @Description("No data found to provide upper or lower bound value.")
  public static final long Bad_BoundNotFound = 0x80D70000L;

  /** The server cannot retrieve a bound for the variable. */
  @Description("The server cannot retrieve a bound for the variable.")
  public static final long Bad_BoundNotSupported = 0x80D80000L;

  /** Data is missing due to collection started/stopped/lost. */
  @Description("Data is missing due to collection started/stopped/lost.")
  public static final long Bad_DataLost = 0x809D0000L;

  /**
   * Expected data is unavailable for the requested time range due to an un-mounted volume, an
   * off-line archive or tape, or similar reason for temporary unavailability.
   */
  @Description(
      "Expected data is unavailable for the requested time range due to an un-mounted volume, an"
          + " off-line archive or tape, or similar reason for temporary unavailability.")
  public static final long Bad_DataUnavailable = 0x809E0000L;

  /** The data or event was not successfully inserted because a matching entry exists. */
  @Description("The data or event was not successfully inserted because a matching entry exists.")
  public static final long Bad_EntryExists = 0x809F0000L;

  /** The data or event was not successfully updated because no matching entry exists. */
  @Description("The data or event was not successfully updated because no matching entry exists.")
  public static final long Bad_NoEntryExists = 0x80A00000L;

  /**
   * The client requested history using a timestamp format the server does not support (i.e
   * requested ServerTimestamp when server only supports SourceTimestamp).
   */
  @Description(
      "The client requested history using a timestamp format the server does not support (i.e"
          + " requested ServerTimestamp when server only supports SourceTimestamp).")
  public static final long Bad_TimestampNotSupported = 0x80A10000L;

  /** The data or event was successfully inserted into the historical database. */
  @Description("The data or event was successfully inserted into the historical database.")
  public static final long Good_EntryInserted = 0x00A20000L;

  /** The data or event field was successfully replaced in the historical database. */
  @Description("The data or event field was successfully replaced in the historical database.")
  public static final long Good_EntryReplaced = 0x00A30000L;

  /**
   * The value is derived from multiple values and has less than the required number of Good values.
   */
  @Description(
      "The value is derived from multiple values and has less than the required number of Good"
          + " values.")
  public static final long Uncertain_DataSubNormal = 0x40A40000L;

  /** No data exists for the requested time range or event filter. */
  @Description("No data exists for the requested time range or event filter.")
  public static final long Good_NoData = 0x00A50000L;

  /** The data or event field was successfully replaced in the historical database. */
  @Description("The data or event field was successfully replaced in the historical database.")
  public static final long Good_MoreData = 0x00A60000L;

  /** The requested number of Aggregates does not match the requested number of NodeIds. */
  @Description("The requested number of Aggregates does not match the requested number of NodeIds.")
  public static final long Bad_AggregateListMismatch = 0x80D40000L;

  /** The requested Aggregate is not support by the server. */
  @Description("The requested Aggregate is not support by the server.")
  public static final long Bad_AggregateNotSupported = 0x80D50000L;

  /** The aggregate value could not be derived due to invalid data inputs. */
  @Description("The aggregate value could not be derived due to invalid data inputs.")
  public static final long Bad_AggregateInvalidInputs = 0x80D60000L;

  /** The aggregate configuration is not valid for specified node. */
  @Description("The aggregate configuration is not valid for specified node.")
  public static final long Bad_AggregateConfigurationRejected = 0x80DA0000L;

  /**
   * The request specifies fields which are not valid for the EventType or cannot be saved by the
   * historian.
   */
  @Description(
      "The request specifies fields which are not valid for the EventType or cannot be saved by the"
          + " historian.")
  public static final long Good_DataIgnored = 0x00D90000L;

  /**
   * The request was rejected by the server because it did not meet the criteria set by the server.
   */
  @Description(
      "The request was rejected by the server because it did not meet the criteria set by the"
          + " server.")
  public static final long Bad_RequestNotAllowed = 0x80E40000L;

  /** The request has not been processed by the server yet. */
  @Description("The request has not been processed by the server yet.")
  public static final long Bad_RequestNotComplete = 0x81130000L;

  /** The device identity needs a ticket before it can be accepted. */
  @Description("The device identity needs a ticket before it can be accepted.")
  public static final long Bad_TicketRequired = 0x811F0000L;

  /** The device identity needs a ticket before it can be accepted. */
  @Description("The device identity needs a ticket before it can be accepted.")
  public static final long Bad_TicketInvalid = 0x81200000L;

  /** The value does not come from the real source and has been edited by the server. */
  @Description("The value does not come from the real source and has been edited by the server.")
  public static final long Good_Edited = 0x00DC0000L;

  /** There was an error in execution of these post-actions. */
  @Description("There was an error in execution of these post-actions.")
  public static final long Good_PostActionFailed = 0x00DD0000L;

  /**
   * The related EngineeringUnit has been changed but the Variable Value is still provided based on
   * the previous unit.
   */
  @Description(
      "The related EngineeringUnit has been changed but the Variable Value is still provided based"
          + " on the previous unit.")
  public static final long Uncertain_DominantValueChanged = 0x40DE0000L;

  /** A dependent value has been changed but the change has not been applied to the device. */
  @Description(
      "A dependent value has been changed but the change has not been applied to the device.")
  public static final long Good_DependentValueChanged = 0x00E00000L;

  /**
   * The related EngineeringUnit has been changed but this change has not been applied to the
   * device. The Variable Value is still dependent on the previous unit but its status is currently
   * Bad.
   */
  @Description(
      "The related EngineeringUnit has been changed but this change has not been applied to the"
          + " device. The Variable Value is still dependent on the previous unit but its status is"
          + " currently Bad.")
  public static final long Bad_DominantValueChanged = 0x80E10000L;

  /**
   * A dependent value has been changed but the change has not been applied to the device. The
   * quality of the dominant variable is uncertain.
   */
  @Description(
      "A dependent value has been changed but the change has not been applied to the device. The"
          + " quality of the dominant variable is uncertain.")
  public static final long Uncertain_DependentValueChanged = 0x40E20000L;

  /**
   * A dependent value has been changed but the change has not been applied to the device. The
   * quality of the dominant variable is Bad.
   */
  @Description(
      "A dependent value has been changed but the change has not been applied to the device. The"
          + " quality of the dominant variable is Bad.")
  public static final long Bad_DependentValueChanged = 0x80E30000L;

  /**
   * It is delivered with a dominant Variable value when a dependent Variable has changed but the
   * change has not been applied.
   */
  @Description(
      "It is delivered with a dominant Variable value when a dependent Variable has changed but the"
          + " change has not been applied.")
  public static final long Good_Edited_DependentValueChanged = 0x01160000L;

  /**
   * It is delivered with a dependent Variable value when a dominant Variable has changed but the
   * change has not been applied.
   */
  @Description(
      "It is delivered with a dependent Variable value when a dominant Variable has changed but the"
          + " change has not been applied.")
  public static final long Good_Edited_DominantValueChanged = 0x01170000L;

  /**
   * It is delivered with a dependent Variable value when a dominant or dependent Variable has
   * changed but change has not been applied.
   */
  @Description(
      "It is delivered with a dependent Variable value when a dominant or dependent Variable has"
          + " changed but change has not been applied.")
  public static final long Good_Edited_DominantValueChanged_DependentValueChanged = 0x01180000L;

  /** It is delivered with a Variable value when Variable has changed but the value is not legal. */
  @Description(
      "It is delivered with a Variable value when Variable has changed but the value is not legal.")
  public static final long Bad_Edited_OutOfRange = 0x81190000L;

  /**
   * It is delivered with a Variable value when a source Variable has changed but the value is not
   * legal.
   */
  @Description(
      "It is delivered with a Variable value when a source Variable has changed but the value is"
          + " not legal.")
  public static final long Bad_InitialValue_OutOfRange = 0x811A0000L;

  /**
   * It is delivered with a dependent Variable value when a dominant Variable has changed and the
   * value is not legal.
   */
  @Description(
      "It is delivered with a dependent Variable value when a dominant Variable has changed and the"
          + " value is not legal.")
  public static final long Bad_OutOfRange_DominantValueChanged = 0x811B0000L;

  /**
   * It is delivered with a dependent Variable value when a dominant Variable has changed, the value
   * is not legal and the change has not been applied.
   */
  @Description(
      "It is delivered with a dependent Variable value when a dominant Variable has changed, the"
          + " value is not legal and the change has not been applied.")
  public static final long Bad_Edited_OutOfRange_DominantValueChanged = 0x811C0000L;

  /**
   * It is delivered with a dependent Variable value when a dominant or dependent Variable has
   * changed and the value is not legal.
   */
  @Description(
      "It is delivered with a dependent Variable value when a dominant or dependent Variable has"
          + " changed and the value is not legal.")
  public static final long Bad_OutOfRange_DominantValueChanged_DependentValueChanged = 0x811D0000L;

  /**
   * It is delivered with a dependent Variable value when a dominant or dependent Variable has
   * changed, the value is not legal and the change has not been applied.
   */
  @Description(
      "It is delivered with a dependent Variable value when a dominant or dependent Variable has"
          + " changed, the value is not legal and the change has not been applied.")
  public static final long Bad_Edited_OutOfRange_DominantValueChanged_DependentValueChanged =
      0x811E0000L;

  /** The communication layer has raised an event. */
  @Description("The communication layer has raised an event.")
  public static final long Good_CommunicationEvent = 0x00A70000L;

  /** The system is shutting down. */
  @Description("The system is shutting down.")
  public static final long Good_ShutdownEvent = 0x00A80000L;

  /** The operation is not finished and needs to be called again. */
  @Description("The operation is not finished and needs to be called again.")
  public static final long Good_CallAgain = 0x00A90000L;

  /** A non-critical timeout occurred. */
  @Description("A non-critical timeout occurred.")
  public static final long Good_NonCriticalTimeout = 0x00AA0000L;

  /** One or more arguments are invalid. */
  @Description("One or more arguments are invalid.")
  public static final long Bad_InvalidArgument = 0x80AB0000L;

  /** Could not establish a network connection to remote server. */
  @Description("Could not establish a network connection to remote server.")
  public static final long Bad_ConnectionRejected = 0x80AC0000L;

  /** The server has disconnected from the client. */
  @Description("The server has disconnected from the client.")
  public static final long Bad_Disconnect = 0x80AD0000L;

  /** The network connection has been closed. */
  @Description("The network connection has been closed.")
  public static final long Bad_ConnectionClosed = 0x80AE0000L;

  /**
   * The operation cannot be completed because the object is closed, uninitialized or in some other
   * invalid state.
   */
  @Description(
      "The operation cannot be completed because the object is closed, uninitialized or in some"
          + " other invalid state.")
  public static final long Bad_InvalidState = 0x80AF0000L;

  /** Cannot move beyond end of the stream. */
  @Description("Cannot move beyond end of the stream.")
  public static final long Bad_EndOfStream = 0x80B00000L;

  /** No data is currently available for reading from a non-blocking stream. */
  @Description("No data is currently available for reading from a non-blocking stream.")
  public static final long Bad_NoDataAvailable = 0x80B10000L;

  /** The asynchronous operation is waiting for a response. */
  @Description("The asynchronous operation is waiting for a response.")
  public static final long Bad_WaitingForResponse = 0x80B20000L;

  /** The asynchronous operation was abandoned by the caller. */
  @Description("The asynchronous operation was abandoned by the caller.")
  public static final long Bad_OperationAbandoned = 0x80B30000L;

  /**
   * The stream did not return all data requested (possibly because it is a non-blocking stream).
   */
  @Description(
      "The stream did not return all data requested (possibly because it is a non-blocking"
          + " stream).")
  public static final long Bad_ExpectedStreamToBlock = 0x80B40000L;

  /** Non blocking behaviour is required and the operation would block. */
  @Description("Non blocking behaviour is required and the operation would block.")
  public static final long Bad_WouldBlock = 0x80B50000L;

  /** A value had an invalid syntax. */
  @Description("A value had an invalid syntax.")
  public static final long Bad_SyntaxError = 0x80B60000L;

  /** The operation could not be finished because all available connections are in use. */
  @Description("The operation could not be finished because all available connections are in use.")
  public static final long Bad_MaxConnectionsReached = 0x80B70000L;

  /** The value may not be accurate because the transducer is in manual mode. */
  @Description("The value may not be accurate because the transducer is in manual mode.")
  public static final long Uncertain_TransducerInManual = 0x42080000L;

  /** The value is simulated. */
  @Description("The value is simulated.")
  public static final long Uncertain_SimulatedValue = 0x42090000L;

  /** The value may not be accurate due to a sensor calibration fault. */
  @Description("The value may not be accurate due to a sensor calibration fault.")
  public static final long Uncertain_SensorCalibration = 0x420A0000L;

  /** The value may not be accurate due to a configuration issue. */
  @Description("The value may not be accurate due to a configuration issue.")
  public static final long Uncertain_ConfigurationError = 0x420F0000L;

  /**
   * The value source supports cascade handshaking and the value has been Initialized based on an
   * initialization request from a cascade secondary.
   */
  @Description(
      "The value source supports cascade handshaking and the value has been Initialized based on an"
          + " initialization request from a cascade secondary.")
  public static final long Good_CascadeInitializationAcknowledged = 0x04010000L;

  /**
   * The value source supports cascade handshaking and is requesting initialization of a cascade
   * primary.
   */
  @Description(
      "The value source supports cascade handshaking and is requesting initialization of a cascade"
          + " primary.")
  public static final long Good_CascadeInitializationRequest = 0x04020000L;

  /**
   * The value source supports cascade handshaking, however, the source’s current state does not
   * allow for cascade.
   */
  @Description(
      "The value source supports cascade handshaking, however, the source’s current state does not"
          + " allow for cascade.")
  public static final long Good_CascadeNotInvited = 0x04030000L;

  /**
   * The value source supports cascade handshaking, however, the source has not selected the
   * corresponding cascade primary for use.
   */
  @Description(
      "The value source supports cascade handshaking, however, the source has not selected the"
          + " corresponding cascade primary for use.")
  public static final long Good_CascadeNotSelected = 0x04040000L;

  /** There is a fault state condition active in the value source. */
  @Description("There is a fault state condition active in the value source.")
  public static final long Good_FaultStateActive = 0x04070000L;

  /** A fault state condition is being requested of the destination. */
  @Description("A fault state condition is being requested of the destination.")
  public static final long Good_InitiateFaultState = 0x04080000L;

  /** The value is accurate, and the signal source supports cascade handshaking. */
  @Description("The value is accurate, and the signal source supports cascade handshaking.")
  public static final long Good_Cascade = 0x04090000L;

  /** The DataSet specified for the DataSetWriter creation is invalid. */
  @Description("The DataSet specified for the DataSetWriter creation is invalid.")
  public static final long Bad_DataSetIdInvalid = 0x80E70000L;
}
