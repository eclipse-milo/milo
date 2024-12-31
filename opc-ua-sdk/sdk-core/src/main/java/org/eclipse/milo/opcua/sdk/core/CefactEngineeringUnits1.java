/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;

abstract class CefactEngineeringUnits1 extends CefactEngineeringUnitsBase {
  public static final EUInformation CODE_4T =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13396,
          LocalizedText.english("pF"),
          LocalizedText.english("picofarad"));

  public static final EUInformation CODE_N90 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5126448,
          LocalizedText.english("kF"),
          LocalizedText.english("kilofarad"));

  public static final EUInformation CODE_A69 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273721,
          LocalizedText.english("F/m"),
          LocalizedText.english("farad per metre"));

  public static final EUInformation CODE_H28 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731448,
          LocalizedText.english("µF/km"),
          LocalizedText.english("microfarad per kilometre"));

  public static final EUInformation CODE_H33 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731699,
          LocalizedText.english("F/km"),
          LocalizedText.english("farad per kilometre"));

  public static final EUInformation CODE_B89 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339769,
          LocalizedText.english("µF/m"),
          LocalizedText.english("microfarad per metre"));

  public static final EUInformation CODE_C42 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404274,
          LocalizedText.english("nF/m"),
          LocalizedText.english("nanofarad per metre"));

  public static final EUInformation CODE_C72 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405042,
          LocalizedText.english("pF/m"),
          LocalizedText.english("picofarad per metre"));

  public static final EUInformation CODE_A26 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272694,
          LocalizedText.english("C·m"),
          LocalizedText.english("coulomb metre"));

  public static final EUInformation CODE_A41 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273201,
          LocalizedText.english("A/m²"),
          LocalizedText.english("ampere per square metre"));

  public static final EUInformation CODE_H31 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731697,
          LocalizedText.english("A/kg"),
          LocalizedText.english("ampere per kilogram"));

  public static final EUInformation CODE_B66 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339254,
          LocalizedText.english("MA/m²"),
          LocalizedText.english("megaampere per square metre"));

  public static final EUInformation CODE_A7 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16695,
          LocalizedText.english("A/mm²"),
          LocalizedText.english("ampere per square millimetre"));

  public static final EUInformation CODE_A4 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16692,
          LocalizedText.english("A/cm²"),
          LocalizedText.english("ampere per square centimetre"));

  public static final EUInformation CODE_B23 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338227,
          LocalizedText.english("kA/m²"),
          LocalizedText.english("kiloampere per square metre"));

  public static final EUInformation CODE_G59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4666681,
          LocalizedText.english("mA/(l·min)"),
          LocalizedText.english("milliampere per litre minute"));

  public static final EUInformation CODE_N93 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5126451,
          LocalizedText.english("A/Pa"),
          LocalizedText.english("ampere per pascal"));

  public static final EUInformation CODE_F57 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4601143,
          LocalizedText.english("mA/(lbf/in²)"),
          LocalizedText.english("milliampere per pound-force per square inch"));

  public static final EUInformation CODE_F59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4601145,
          LocalizedText.english("mA/bar"),
          LocalizedText.english("milliampere per bar"));

  public static final EUInformation CODE_AE =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16709,
          LocalizedText.english("A/m"),
          LocalizedText.english("ampere per metre"));

  public static final EUInformation CODE_B24 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338228,
          LocalizedText.english("kA/m"),
          LocalizedText.english("kiloampere per metre"));

  public static final EUInformation CODE_A3 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16691,
          LocalizedText.english("A/mm"),
          LocalizedText.english("ampere per millimetre"));

  public static final EUInformation CODE_A2 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16690,
          LocalizedText.english("A/cm"),
          LocalizedText.english("ampere per centimetre"));

  public static final EUInformation CODE_F76 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4601654,
          LocalizedText.english("mA/mm"),
          LocalizedText.english("milliampere per millimetre"));

  public static final EUInformation CODE_F08 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4599864,
          LocalizedText.english("mA/in"),
          LocalizedText.english("milliampere per inch"));

  public static final EUInformation CODE_P10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255472,
          LocalizedText.english("C/m"),
          LocalizedText.english("coulomb per metre"));

  public static final EUInformation CODE_D33 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469555,
          LocalizedText.english("T"),
          LocalizedText.english("tesla"));

  public static final EUInformation CODE_C29 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403769,
          LocalizedText.english("mT"),
          LocalizedText.english("millitesla"));

  public static final EUInformation CODE_D81 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470833,
          LocalizedText.english("µT"),
          LocalizedText.english("microtesla"));

  public static final EUInformation CODE_C48 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404280,
          LocalizedText.english("nT"),
          LocalizedText.english("nanotesla"));

  public static final EUInformation CODE_P13 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255475,
          LocalizedText.english("kT"),
          LocalizedText.english("kilotesla"));

  public static final EUInformation CODE_P12 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255474,
          LocalizedText.english("γ"),
          LocalizedText.english("gamma"));

  public static final EUInformation CODE_WEB =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5719362,
          LocalizedText.english("Wb"),
          LocalizedText.english("weber"));

  public static final EUInformation CODE_C33 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404019,
          LocalizedText.english("mWb"),
          LocalizedText.english("milliweber"));

  public static final EUInformation CODE_P11 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255473,
          LocalizedText.english("kWb"),
          LocalizedText.english("kiloweber"));

  public static final EUInformation CODE_D59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470073,
          LocalizedText.english("Wb/m"),
          LocalizedText.english("weber per metre"));

  public static final EUInformation CODE_B56 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338998,
          LocalizedText.english("kWb/m"),
          LocalizedText.english("kiloweber per metre"));

  public static final EUInformation CODE_D60 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470320,
          LocalizedText.english("Wb/mm"),
          LocalizedText.english("weber per millimetre"));

  public static final EUInformation CODE_81 =
      new EUInformation(
          CEFACT_NAMESPACE_URI, 14385, LocalizedText.english("H"), LocalizedText.english("henry"));

  public static final EUInformation CODE_C14 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403508,
          LocalizedText.english("mH"),
          LocalizedText.english("millihenry"));

  public static final EUInformation CODE_B90 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4340016,
          LocalizedText.english("µH"),
          LocalizedText.english("microhenry"));

  public static final EUInformation CODE_C43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404275,
          LocalizedText.english("nH"),
          LocalizedText.english("nanohenry"));

  public static final EUInformation CODE_C73 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405043,
          LocalizedText.english("pH"),
          LocalizedText.english("picohenry"));

  public static final EUInformation CODE_H03 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4730931,
          LocalizedText.english("H/kΩ"),
          LocalizedText.english("henry per kiloohm"));

  public static final EUInformation CODE_H04 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4730932,
          LocalizedText.english("H/Ω"),
          LocalizedText.english("henry per ohm"));

  public static final EUInformation CODE_G98 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4667704,
          LocalizedText.english("µH/kΩ"),
          LocalizedText.english("microhenry per kiloohm"));

  public static final EUInformation CODE_G99 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4667705,
          LocalizedText.english("µH/Ω"),
          LocalizedText.english("microhenry per ohm"));

  public static final EUInformation CODE_H05 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4730933,
          LocalizedText.english("mH/kΩ"),
          LocalizedText.english("millihenry per kiloohm"));

  public static final EUInformation CODE_H06 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4730934,
          LocalizedText.english("mH/Ω"),
          LocalizedText.english("millihenry per ohm"));

  public static final EUInformation CODE_P24 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255732,
          LocalizedText.english("kH"),
          LocalizedText.english("kilohenry"));

  public static final EUInformation CODE_A98 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4274488,
          LocalizedText.english("H/m"),
          LocalizedText.english("henry per metre"));

  public static final EUInformation CODE_B91 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4340017,
          LocalizedText.english("µH/m"),
          LocalizedText.english("microhenry per metre"));

  public static final EUInformation CODE_C44 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404276,
          LocalizedText.english("nH/m"),
          LocalizedText.english("nanohenry per metre"));

  public static final EUInformation CODE_A5 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16693,
          LocalizedText.english("A·m²"),
          LocalizedText.english("ampere square metre"));

  public static final EUInformation CODE_B8 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16952,
          LocalizedText.english("J/m³"),
          LocalizedText.english("joule per cubic metre"));

  public static final EUInformation CODE_OHM =
      new EUInformation(
          CEFACT_NAMESPACE_URI, 5195853, LocalizedText.english("Ω"), LocalizedText.english("ohm"));

  public static final EUInformation CODE_A87 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4274231,
          LocalizedText.english("GΩ"),
          LocalizedText.english("gigaohm"));

  public static final EUInformation CODE_B75 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339509,
          LocalizedText.english("MΩ"),
          LocalizedText.english("megaohm"));

  public static final EUInformation CODE_H44 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731956,
          LocalizedText.english("TΩ"),
          LocalizedText.english("teraohm"));

  public static final EUInformation CODE_B49 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338745,
          LocalizedText.english("kΩ"),
          LocalizedText.english("kiloohm"));

  public static final EUInformation CODE_E45 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535349,
          LocalizedText.english("mΩ"),
          LocalizedText.english("milliohm"));

  public static final EUInformation CODE_B94 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4340020,
          LocalizedText.english("µΩ"),
          LocalizedText.english("microohm"));

  public static final EUInformation CODE_P22 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255730,
          LocalizedText.english("nΩ"),
          LocalizedText.english("nanoohm"));

  public static final EUInformation CODE_M26 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059126,
          LocalizedText.english("GΩ/m"),
          LocalizedText.english("gigaohm per metre"));

  public static final EUInformation CODE_SIE =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5458245,
          LocalizedText.english("S"),
          LocalizedText.english("siemens"));

  public static final EUInformation CODE_B53 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338995,
          LocalizedText.english("kS"),
          LocalizedText.english("kilosiemens"));

  public static final EUInformation CODE_C27 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403767,
          LocalizedText.english("mS"),
          LocalizedText.english("millisiemens"));

  public static final EUInformation CODE_B99 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4340025,
          LocalizedText.english("µS"),
          LocalizedText.english("microsiemens"));

  public static final EUInformation CODE_G42 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4666418,
          LocalizedText.english("µS/cm"),
          LocalizedText.english("microsiemens per centimetre"));

  public static final EUInformation CODE_G43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4666419,
          LocalizedText.english("µS/m"),
          LocalizedText.english("microsiemens per metre"));

  public static final EUInformation CODE_N92 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5126450,
          LocalizedText.english("pS"),
          LocalizedText.english("picosiemens"));

  public static final EUInformation CODE_C61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404785,
          LocalizedText.english("Ω·m"),
          LocalizedText.english("ohm metre"));

  public static final EUInformation CODE_A88 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4274232,
          LocalizedText.english("GΩ·m"),
          LocalizedText.english("gigaohm metre"));

  public static final EUInformation CODE_B76 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339510,
          LocalizedText.english("MΩ·m"),
          LocalizedText.english("megaohm metre"));

  public static final EUInformation CODE_H88 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732984,
          LocalizedText.english("MΩ·km"),
          LocalizedText.english("megaohm kilometre"));

  public static final EUInformation CODE_B50 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338992,
          LocalizedText.english("kΩ·m"),
          LocalizedText.english("kiloohm metre"));

  public static final EUInformation CODE_C60 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404784,
          LocalizedText.english("Ω·cm"),
          LocalizedText.english("ohm centimetre"));

  public static final EUInformation CODE_C23 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403763,
          LocalizedText.english("mΩ·m"),
          LocalizedText.english("milliohm metre"));

  public static final EUInformation CODE_B95 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4340021,
          LocalizedText.english("µΩ·m"),
          LocalizedText.english("microohm metre"));

  public static final EUInformation CODE_C46 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404278,
          LocalizedText.english("nΩ·m"),
          LocalizedText.english("nanoohm metre"));

  public static final EUInformation CODE_M24 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059124,
          LocalizedText.english("Ω·km"),
          LocalizedText.english("ohm kilometre"));

  public static final EUInformation CODE_P23 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255731,
          LocalizedText.english("Ω·cmil/ft"),
          LocalizedText.english("ohm circular-mil per foot"));

  public static final EUInformation CODE_F56 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4601142,
          LocalizedText.english("Ω/km"),
          LocalizedText.english("ohm per kilometre"));

  public static final EUInformation CODE_H26 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731446,
          LocalizedText.english("Ω/m"),
          LocalizedText.english("ohm per metre"));

  public static final EUInformation CODE_H37 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731703,
          LocalizedText.english("MΩ/m"),
          LocalizedText.english("megaohm per metre"));

  public static final EUInformation CODE_F54 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4601140,
          LocalizedText.english("mΩ/m"),
          LocalizedText.english("milliohm per metre"));

  public static final EUInformation CODE_H36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731702,
          LocalizedText.english("MΩ/km"),
          LocalizedText.english("megaohm per kilometre"));

  public static final EUInformation CODE_F55 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4601141,
          LocalizedText.english("Ω/mi"),
          LocalizedText.english("ohm per mile (statute mile)"));

  public static final EUInformation CODE_D10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469040,
          LocalizedText.english("S/m"),
          LocalizedText.english("siemens per metre"));

  public static final EUInformation CODE_H43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731955,
          LocalizedText.english("S/cm"),
          LocalizedText.english("siemens per centimetre"));

  public static final EUInformation CODE_H61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732465,
          LocalizedText.english("mS/cm"),
          LocalizedText.english("millisiemens per centimetre"));

  public static final EUInformation CODE_B77 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339511,
          LocalizedText.english("MS/m"),
          LocalizedText.english("megasiemens per metre"));

  public static final EUInformation CODE_B54 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338996,
          LocalizedText.english("kS/m"),
          LocalizedText.english("kilosiemens per metre"));

  public static final EUInformation CODE_G45 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4666421,
          LocalizedText.english("nS/m"),
          LocalizedText.english("nanosiemens per metre"));

  public static final EUInformation CODE_G44 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4666420,
          LocalizedText.english("nS/cm"),
          LocalizedText.english("nanosiemens per centimetre"));

  public static final EUInformation CODE_L42 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4994098,
          LocalizedText.english("pS/m"),
          LocalizedText.english("picosiemens per metre"));

  public static final EUInformation CODE_C89 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405305,
          LocalizedText.english("H⁻¹"),
          LocalizedText.english("reciprocal henry"));

  public static final EUInformation CODE_P14 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255476,
          LocalizedText.english("J/s"),
          LocalizedText.english("joule per second"));

  public static final EUInformation CODE_D31 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469553,
          LocalizedText.english("TW"),
          LocalizedText.english("terawatt"));

  public static final EUInformation CODE_P15 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255477,
          LocalizedText.english("J/min"),
          LocalizedText.english("joule per minute"));

  public static final EUInformation CODE_P16 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255478,
          LocalizedText.english("J/h"),
          LocalizedText.english("joule per hour"));

  public static final EUInformation CODE_P17 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255479,
          LocalizedText.english("J/d"),
          LocalizedText.english("joule per day"));

  public static final EUInformation CODE_P18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255480,
          LocalizedText.english("kJ/s"),
          LocalizedText.english("kilojoule per second"));

  public static final EUInformation CODE_P19 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255481,
          LocalizedText.english("kJ/min"),
          LocalizedText.english("kilojoule per minute"));

  public static final EUInformation CODE_P20 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255728,
          LocalizedText.english("kJ/h"),
          LocalizedText.english("kilojoule per hour"));

  public static final EUInformation CODE_P21 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255729,
          LocalizedText.english("kJ/d"),
          LocalizedText.english("kilojoule per day"));

  public static final EUInformation CODE_K43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4928563,
          LocalizedText.english("electric hp"),
          LocalizedText.english("horsepower (electric)"));

  public static final EUInformation CODE_C49 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404281,
          LocalizedText.english("nW"),
          LocalizedText.english("nanowatt"));

  public static final EUInformation CODE_C75 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405045,
          LocalizedText.english("pW"),
          LocalizedText.english("picowatt"));

  public static final EUInformation CODE_D46 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469814,
          LocalizedText.english("V·A"),
          LocalizedText.english("volt - ampere"));

  public static final EUInformation CODE_MVA =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5068353,
          LocalizedText.english("MV·A"),
          LocalizedText.english("megavolt - ampere"));

  public static final EUInformation CODE_KVA =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4937281,
          LocalizedText.english("kV·A"),
          LocalizedText.english("kilovolt - ampere"));

  public static final EUInformation CODE_M35 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059381,
          LocalizedText.english("mV·A"),
          LocalizedText.english("millivolt - ampere"));

  public static final EUInformation CODE_D44 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469812,
          LocalizedText.english("var"),
          LocalizedText.english("var"));

  public static final EUInformation CODE_K5 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          19253,
          LocalizedText.english("kvar"),
          LocalizedText.english("kilovolt ampere (reactive)"));

  public static final EUInformation CODE_KVR =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4937298,
          LocalizedText.english("kvar"),
          LocalizedText.english("kilovar"));

  public static final EUInformation CODE_MAR =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5062994,
          LocalizedText.english("mvar"),
          LocalizedText.english("megavar"));

  public static final EUInformation CODE_N91 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5126449,
          LocalizedText.english("1/J"),
          LocalizedText.english("reciprocal joule"));

  public static final EUInformation CODE_M30 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059376,
          LocalizedText.english("1/(V·A·s)"),
          LocalizedText.english("reciprocal volt - ampere reciprocal second"));

  public static final EUInformation CODE_M17 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5058871,
          LocalizedText.english("kHz·m"),
          LocalizedText.english("kilohertz metre"));

  public static final EUInformation CODE_M18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5058872,
          LocalizedText.english("GHz·m"),
          LocalizedText.english("gigahertz metre"));

  public static final EUInformation CODE_M27 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059127,
          LocalizedText.english("MHz·m"),
          LocalizedText.english("megahertz metre"));

  public static final EUInformation CODE_M21 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059121,
          LocalizedText.english("1/kVAh"),
          LocalizedText.english("reciprocal kilovolt - ampere reciprocal hour"));

  public static final EUInformation CODE_H34 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731700,
          LocalizedText.english("Hz·m"),
          LocalizedText.english("hertz metre"));

  public static final EUInformation CODE_H39 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731705,
          LocalizedText.english("MHz·km"),
          LocalizedText.english("megahertz kilometre"));

  public static final EUInformation CODE_C84 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405300,
          LocalizedText.english("rad/m"),
          LocalizedText.english("radian per metre"));

  public static final EUInformation CODE_JM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          19021,
          LocalizedText.english("MJ/m³"),
          LocalizedText.english("megajoule per cubic metre"));

  public static final EUInformation CODE_B14 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4337972,
          LocalizedText.english("J/m⁴"),
          LocalizedText.english("joule per metre to the fourth power"));

  public static final EUInformation CODE_B13 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4337971,
          LocalizedText.english("J/m²"),
          LocalizedText.english("joule per square metre"));

  public static final EUInformation CODE_D1 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          17457,
          LocalizedText.english("s⁻¹/sr"),
          LocalizedText.english("reciprocal second per steradian"));

  public static final EUInformation CODE_D2 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          17458,
          LocalizedText.english("s⁻¹/(sr·m²)"),
          LocalizedText.english("reciprocal second per steradian metre squared"));

  public static final EUInformation CODE_C99 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405561,
          LocalizedText.english("s⁻¹/m²"),
          LocalizedText.english("reciprocal second per metre squared"));

  public static final EUInformation CODE_C93 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405555,
          LocalizedText.english("m⁻²"),
          LocalizedText.english("reciprocal square metre"));

  public static final EUInformation CODE_H47 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731959,
          LocalizedText.english("W/m³"),
          LocalizedText.english("watt per cubic metre"));

  public static final EUInformation CODE_H74 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732724,
          LocalizedText.english("W/m"),
          LocalizedText.english("watt per metre"));

  public static final EUInformation CODE_E43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535347,
          LocalizedText.english("J/cm²"),
          LocalizedText.english("joule per square centimetre"));

  public static final EUInformation CODE_P37 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255991,
          LocalizedText.english("BtuIT/ft²"),
          LocalizedText.english("British thermal unit (international table) per square foot"));

  public static final EUInformation CODE_P38 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255992,
          LocalizedText.english("Btuth/ft²"),
          LocalizedText.english("British thermal unit (thermochemical) per square foot"));

  public static final EUInformation CODE_P39 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255993,
          LocalizedText.english("calth/cm²"),
          LocalizedText.english("calorie (thermochemical) per square centimetre"));

  public static final EUInformation CODE_P40 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256240,
          LocalizedText.english("Ly"),
          LocalizedText.english("langley"));

  public static final EUInformation CODE_D57 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470071,
          LocalizedText.english("W/sr"),
          LocalizedText.english("watt per steradian"));

  public static final EUInformation CODE_D58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470072,
          LocalizedText.english("W/(sr·m²)"),
          LocalizedText.english("watt per steradian square metre"));

  public static final EUInformation CODE_D56 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470070,
          LocalizedText.english("W/(m²·K⁴)"),
          LocalizedText.english("watt per square metre kelvin to the fourth power"));

  public static final EUInformation CODE_D18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469048,
          LocalizedText.english("m·K"),
          LocalizedText.english("metre kelvin"));

  public static final EUInformation CODE_CDL =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4408396,
          LocalizedText.english("cd"),
          LocalizedText.english("candela"));

  public static final EUInformation CODE_P33 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255987,
          LocalizedText.english("kcd"),
          LocalizedText.english("kilocandela"));

  public static final EUInformation CODE_P34 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255988,
          LocalizedText.english("mcd"),
          LocalizedText.english("millicandela"));

  public static final EUInformation CODE_P35 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255989,
          LocalizedText.english("HK"),
          LocalizedText.english("Hefner-Kerze"));

  public static final EUInformation CODE_P36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255990,
          LocalizedText.english("IK"),
          LocalizedText.english("international candle"));

  public static final EUInformation CODE_LUM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5002573,
          LocalizedText.english("lm"),
          LocalizedText.english("lumen"));

  public static final EUInformation CODE_B62 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339250,
          LocalizedText.english("lm·s"),
          LocalizedText.english("lumen second"));

  public static final EUInformation CODE_B59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339001,
          LocalizedText.english("lm·h"),
          LocalizedText.english("lumen hour"));

  public static final EUInformation CODE_A24 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272692,
          LocalizedText.english("cd/m²"),
          LocalizedText.english("candela per square metre"));

  public static final EUInformation CODE_P28 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255736,
          LocalizedText.english("cd/in²"),
          LocalizedText.english("candela per square inch"));

  public static final EUInformation CODE_P29 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255737,
          LocalizedText.english("ftL"),
          LocalizedText.english("footlambert"));

  public static final EUInformation CODE_P30 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255984,
          LocalizedText.english("Lb"),
          LocalizedText.english("lambert"));

  public static final EUInformation CODE_P31 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255985,
          LocalizedText.english("sb"),
          LocalizedText.english("stilb"));

  public static final EUInformation CODE_P32 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255986,
          LocalizedText.english("cd/ft²"),
          LocalizedText.english("candela per square foot"));

  public static final EUInformation CODE_B60 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339248,
          LocalizedText.english("lm/m²"),
          LocalizedText.english("lumen per square metre"));

  public static final EUInformation CODE_LUX =
      new EUInformation(
          CEFACT_NAMESPACE_URI, 5002584, LocalizedText.english("lx"), LocalizedText.english("lux"));

  public static final EUInformation CODE_KLX =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4934744,
          LocalizedText.english("klx"),
          LocalizedText.english("kilolux"));

  public static final EUInformation CODE_P25 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255733,
          LocalizedText.english("lm/ft²"),
          LocalizedText.english("lumen per square foot"));

  public static final EUInformation CODE_P26 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255734,
          LocalizedText.english("ph"),
          LocalizedText.english("phot"));

  public static final EUInformation CODE_P27 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5255735,
          LocalizedText.english("ftc"),
          LocalizedText.english("footcandle"));

  public static final EUInformation CODE_B64 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339252,
          LocalizedText.english("lx·s"),
          LocalizedText.english("lux second"));

  public static final EUInformation CODE_B63 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339251,
          LocalizedText.english("lx·h"),
          LocalizedText.english("lux hour"));

  public static final EUInformation CODE_B61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339249,
          LocalizedText.english("lm/W"),
          LocalizedText.english("lumen per watt"));

  public static final EUInformation CODE_D22 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469298,
          LocalizedText.english("m²/mol"),
          LocalizedText.english("square metre per mole"));

  public static final EUInformation CODE_D9 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          17465,
          LocalizedText.english("dyn/cm²"),
          LocalizedText.english("dyne per square centimetre"));

  public static final EUInformation CODE_A60 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273712,
          LocalizedText.english("erg/cm³"),
          LocalizedText.english("erg per cubic centimetre"));

  public static final EUInformation CODE_C32 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404018,
          LocalizedText.english("mW/m²"),
          LocalizedText.english("milliwatt per square metre"));

  public static final EUInformation CODE_D85 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470837,
          LocalizedText.english("µW/m²"),
          LocalizedText.english("microwatt per square metre"));

  public static final EUInformation CODE_C76 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405046,
          LocalizedText.english("pW/m²"),
          LocalizedText.english("picowatt per square metre"));

  public static final EUInformation CODE_A64 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273716,
          LocalizedText.english("erg/(s·cm²)"),
          LocalizedText.english("erg per second square centimetre"));

  public static final EUInformation CODE_C67 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404791,
          LocalizedText.english("Pa· s/m"),
          LocalizedText.english("pascal second per metre"));

  public static final EUInformation CODE_A50 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273456,
          LocalizedText.english("dyn·s/cm³"),
          LocalizedText.english("dyne second per cubic centimetre"));

  public static final EUInformation CODE_C66 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404790,
          LocalizedText.english("Pa·s/m³"),
          LocalizedText.english("pascal second per cubic metre"));

  public static final EUInformation CODE_A52 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273458,
          LocalizedText.english("dyn·s/cm⁵"),
          LocalizedText.english("dyne second per centimetre to the fifth power"));

  public static final EUInformation CODE_M32 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059378,
          LocalizedText.english("Pa·s/l"),
          LocalizedText.english("pascal second per litre"));

  public static final EUInformation CODE_C58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404536,
          LocalizedText.english("N·s/m"),
          LocalizedText.english("newton second per metre"));

  public static final EUInformation CODE_A51 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273457,
          LocalizedText.english("dyn·s/cm"),
          LocalizedText.english("dyne second per centimetre"));

  public static final EUInformation CODE_P43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256243,
          LocalizedText.english("B/m"),
          LocalizedText.english("bel per metre"));

  public static final EUInformation CODE_H51 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732209,
          LocalizedText.english("dB/km"),
          LocalizedText.english("decibel per kilometre"));

  public static final EUInformation CODE_H52 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732210,
          LocalizedText.english("dB/m"),
          LocalizedText.english("decibel per metre"));

  public static final EUInformation CODE_P42 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256242,
          LocalizedText.english("Pa²·s"),
          LocalizedText.english("pascal squared second"));

  public static final EUInformation CODE_P41 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256241,
          LocalizedText.english("dec"),
          LocalizedText.english("decade (logarithmic)"));

  public static final EUInformation CODE_C34 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404020,
          LocalizedText.english("mol"),
          LocalizedText.english("mole"));

  public static final EUInformation CODE_B45 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338741,
          LocalizedText.english("kmol"),
          LocalizedText.english("kilomole"));

  public static final EUInformation CODE_C18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403512,
          LocalizedText.english("mmol"),
          LocalizedText.english("millimole"));

  public static final EUInformation CODE_FH =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          17992,
          LocalizedText.english("µmol"),
          LocalizedText.english("micromole"));

  public static final EUInformation CODE_P44 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256244,
          LocalizedText.english("lbmol"),
          LocalizedText.english("pound mole"));

  public static final EUInformation CODE_C95 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405557,
          LocalizedText.english("mol⁻¹"),
          LocalizedText.english("reciprocal mole"));

  public static final EUInformation CODE_D74 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470580,
          LocalizedText.english("kg/mol"),
          LocalizedText.english("kilogram per mole"));

  public static final EUInformation CODE_A94 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4274484,
          LocalizedText.english("g/mol"),
          LocalizedText.english("gram per mole"));

  public static final EUInformation CODE_A40 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273200,
          LocalizedText.english("m³/mol"),
          LocalizedText.english("cubic metre per mole"));

  public static final EUInformation CODE_A37 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272951,
          LocalizedText.english("dm³/mol"),
          LocalizedText.english("cubic decimetre per mole"));

  public static final EUInformation CODE_A36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272950,
          LocalizedText.english("cm³/mol"),
          LocalizedText.english("cubic centimetre per mole"));

  public static final EUInformation CODE_B58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339000,
          LocalizedText.english("l/mol"),
          LocalizedText.english("litre per mole"));

  public static final EUInformation CODE_B15 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4337973,
          LocalizedText.english("J/mol"),
          LocalizedText.english("joule per mole"));

  public static final EUInformation CODE_B44 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338740,
          LocalizedText.english("kJ/mol"),
          LocalizedText.english("kilojoule per mole"));

  public static final EUInformation CODE_B16 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4337974,
          LocalizedText.english("J/(mol·K)"),
          LocalizedText.english("joule per mole kelvin"));

  public static final EUInformation CODE_C86 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405302,
          LocalizedText.english("m⁻³"),
          LocalizedText.english("reciprocal cubic metre"));

  public static final EUInformation CODE_H50 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732208,
          LocalizedText.english("cm⁻³"),
          LocalizedText.english("reciprocal cubic centimetre"));

  public static final EUInformation CODE_L20 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993584,
          LocalizedText.english("1/mm³"),
          LocalizedText.english("reciprocal cubic millimetre"));

  public static final EUInformation CODE_K20 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4928048,
          LocalizedText.english("1/ft³"),
          LocalizedText.english("reciprocal cubic foot"));

  public static final EUInformation CODE_K49 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4928569,
          LocalizedText.english("1/in³"),
          LocalizedText.english("reciprocal cubic inch"));

  public static final EUInformation CODE_K63 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4929075,
          LocalizedText.english("1/l"),
          LocalizedText.english("reciprocal litre"));

  public static final EUInformation CODE_M10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5058864,
          LocalizedText.english("1/yd³"),
          LocalizedText.english("reciprocal cubic yard"));

  public static final EUInformation CODE_C36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404022,
          LocalizedText.english("mol/m³"),
          LocalizedText.english("mole per cubic metre"));

  public static final EUInformation CODE_C38 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404024,
          LocalizedText.english("mol/l"),
          LocalizedText.english("mole per litre"));

  public static final EUInformation CODE_C35 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404021,
          LocalizedText.english("mol/dm³"),
          LocalizedText.english("mole per cubic decimetre"));

  public static final EUInformation CODE_B46 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338742,
          LocalizedText.english("kmol/m³"),
          LocalizedText.english("kilomole per cubic metre"));

  public static final EUInformation CODE_E95 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536629,
          LocalizedText.english("mol/s"),
          LocalizedText.english("mole per second"));

  public static final EUInformation CODE_M33 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059379,
          LocalizedText.english("mmol/l"),
          LocalizedText.english("millimole per litre"));

  public static final EUInformation CODE_P51 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256497,
          LocalizedText.english("(mol/kg)/Pa"),
          LocalizedText.english("mol per kilogram pascal"));

  public static final EUInformation CODE_P52 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256498,
          LocalizedText.english("(mol/m³)/Pa"),
          LocalizedText.english("mol per cubic metre pascal"));

  public static final EUInformation CODE_K59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4928825,
          LocalizedText.english("(kmol/m³)/K"),
          LocalizedText.english("kilomole per cubic metre kelvin"));

  public static final EUInformation CODE_K60 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4929072,
          LocalizedText.english("(kmol/m³)/bar"),
          LocalizedText.english("kilomole per cubic metre bar"));

  public static final EUInformation CODE_K93 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4929843,
          LocalizedText.english("1/psi"),
          LocalizedText.english("reciprocal psi"));

  public static final EUInformation CODE_L24 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993588,
          LocalizedText.english("(mol/kg)/K"),
          LocalizedText.english("mole per kilogram kelvin"));

  public static final EUInformation CODE_L25 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993589,
          LocalizedText.english("(mol/kg)/bar"),
          LocalizedText.english("mole per kilogram bar"));

  public static final EUInformation CODE_L26 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993590,
          LocalizedText.english("(mol/l)/K"),
          LocalizedText.english("mole per litre kelvin"));

  public static final EUInformation CODE_L27 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993591,
          LocalizedText.english("(mol/l)/bar"),
          LocalizedText.english("mole per litre bar"));

  public static final EUInformation CODE_L28 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993592,
          LocalizedText.english("(mol/m³)/K"),
          LocalizedText.english("mole per cubic metre kelvin"));

  public static final EUInformation CODE_L29 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993593,
          LocalizedText.english("(mol/m³)/bar"),
          LocalizedText.english("mole per cubic metre bar"));

  public static final EUInformation CODE_C19 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403513,
          LocalizedText.english("mol/kg"),
          LocalizedText.english("mole per kilogram"));

  public static final EUInformation CODE_D93 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4471091,
          LocalizedText.english("s/m³"),
          LocalizedText.english("second per cubic metre"));

  public static final EUInformation CODE_D87 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470839,
          LocalizedText.english("mmol/kg"),
          LocalizedText.english("millimole per kilogram"));

  public static final EUInformation CODE_H68 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732472,
          LocalizedText.english("mmol/g"),
          LocalizedText.english("millimole per gram"));

  public static final EUInformation CODE_P47 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256247,
          LocalizedText.english("kmol/kg"),
          LocalizedText.english("kilomole per kilogram"));

  public static final EUInformation CODE_P48 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256248,
          LocalizedText.english("lbmol/lb"),
          LocalizedText.english("pound mole per pound"));

  public static final EUInformation CODE_KAT =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4931924,
          LocalizedText.english("kat"),
          LocalizedText.english("katal"));

  public static final EUInformation CODE_E94 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536628,
          LocalizedText.english("kmol/s"),
          LocalizedText.english("kilomole per second"));

  public static final EUInformation CODE_P45 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256245,
          LocalizedText.english("lbmol/s"),
          LocalizedText.english("pound mole per second"));

  public static final EUInformation CODE_P46 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256246,
          LocalizedText.english("lbmol/h"),
          LocalizedText.english("pound mole per minute"));

  public static final EUInformation CODE_D43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469811,
          LocalizedText.english("u"),
          LocalizedText.english("unified atomic mass unit"));

  public static final EUInformation CODE_A27 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272695,
          LocalizedText.english("C·m²/V"),
          LocalizedText.english("coulomb metre squared per volt"));

  public static final EUInformation CODE_A32 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272946,
          LocalizedText.english("C/mol"),
          LocalizedText.english("coulomb per mole"));

  public static final EUInformation CODE_D12 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469042,
          LocalizedText.english("S·m²/mol"),
          LocalizedText.english("siemens square metre per mole"));

  public static final EUInformation CODE_K58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4928824,
          LocalizedText.english("kmol/h"),
          LocalizedText.english("kilomole per hour"));

  public static final EUInformation CODE_K61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4929073,
          LocalizedText.english("kmol/min"),
          LocalizedText.english("kilomole per minute"));

  public static final EUInformation CODE_L23 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993587,
          LocalizedText.english("mol/h"),
          LocalizedText.english("mole per hour"));

  public static final EUInformation CODE_L30 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993840,
          LocalizedText.english("mol/min"),
          LocalizedText.english("mole per minute"));

  public static final EUInformation CODE_C82 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405298,
          LocalizedText.english("rad·m²/mol"),
          LocalizedText.english("radian square metre per mole"));

  public static final EUInformation CODE_C83 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405299,
          LocalizedText.english("rad·m²/kg"),
          LocalizedText.english("radian square metre per kilogram"));

  public static final EUInformation CODE_P49 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256249,
          LocalizedText.english("N·m²/A"),
          LocalizedText.english("newton square metre per ampere"));

  public static final EUInformation CODE_P50 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256496,
          LocalizedText.english("Wb·m"),
          LocalizedText.english("weber metre"));

  public static final EUInformation CODE_Q30 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321520,
          LocalizedText.english("pH"),
          LocalizedText.english("pH (potential of Hydrogen)"));

  public static final EUInformation CODE_B18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4337976,
          LocalizedText.english("J·s"),
          LocalizedText.english("joule second"));

  public static final EUInformation CODE_A10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272432,
          LocalizedText.english("A·m²/(J·s)"),
          LocalizedText.english("ampere square metre per joule second"));

  public static final EUInformation CODE_CUR =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4412754,
          LocalizedText.english("Ci"),
          LocalizedText.english("curie"));

  public static final EUInformation CODE_MCU =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5063509,
          LocalizedText.english("mCi"),
          LocalizedText.english("millicurie"));

  public static final EUInformation CODE_M5 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          19765,
          LocalizedText.english("µCi"),
          LocalizedText.english("microcurie"));

  public static final EUInformation CODE_2R =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          12882,
          LocalizedText.english("kCi"),
          LocalizedText.english("kilocurie"));

  public static final EUInformation CODE_BQL =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4346188,
          LocalizedText.english("Bq"),
          LocalizedText.english("becquerel"));

  public static final EUInformation CODE_GBQ =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4670033,
          LocalizedText.english("GBq"),
          LocalizedText.english("gigabecquerel"));

  public static final EUInformation CODE_2Q =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          12881,
          LocalizedText.english("kBq"),
          LocalizedText.english("kilobecquerel"));

  public static final EUInformation CODE_4N =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13390,
          LocalizedText.english("MBq"),
          LocalizedText.english("megabecquerel"));

  public static final EUInformation CODE_H08 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4730936,
          LocalizedText.english("µBq"),
          LocalizedText.english("microbecquerel"));

  public static final EUInformation CODE_A42 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273202,
          LocalizedText.english("Ci/kg"),
          LocalizedText.english("curie per kilogram"));

  public static final EUInformation CODE_A18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272440,
          LocalizedText.english("Bq/kg"),
          LocalizedText.english("becquerel per kilogram"));

  public static final EUInformation CODE_B67 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339255,
          LocalizedText.english("MBq/kg"),
          LocalizedText.english("megabecquerel per kilogram"));

  public static final EUInformation CODE_B25 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338229,
          LocalizedText.english("kBq/kg"),
          LocalizedText.english("kilobecquerel per kilogram"));

  public static final EUInformation CODE_A19 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272441,
          LocalizedText.english("Bq/m³"),
          LocalizedText.english("becquerel per cubic metre"));

  public static final EUInformation CODE_A14 =
      new EUInformation(
          CEFACT_NAMESPACE_URI, 4272436, LocalizedText.english("b"), LocalizedText.english("barn"));

  public static final EUInformation CODE_D24 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469300,
          LocalizedText.english("m²/sr"),
          LocalizedText.english("square metre per steradian"));

  public static final EUInformation CODE_A17 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272439,
          LocalizedText.english("b/sr"),
          LocalizedText.english("barn per steradian"));

  public static final EUInformation CODE_D20 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469296,
          LocalizedText.english("m²/J"),
          LocalizedText.english("square metre per joule"));

  public static final EUInformation CODE_A15 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272437,
          LocalizedText.english("b/eV"),
          LocalizedText.english("barn per electronvolt"));

  public static final EUInformation CODE_D16 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469046,
          LocalizedText.english("cm²/erg"),
          LocalizedText.english("square centimetre per erg"));

  public static final EUInformation CODE_D25 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469301,
          LocalizedText.english("m²/(sr·J)"),
          LocalizedText.english("square metre per steradian joule"));

  public static final EUInformation CODE_A16 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272438,
          LocalizedText.english("b/(sr·eV)"),
          LocalizedText.english("barn per steradian electronvolt"));

  public static final EUInformation CODE_D17 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469047,
          LocalizedText.english("cm²/(sr·erg)"),
          LocalizedText.english("square centimetre per steradian erg"));

  public static final EUInformation CODE_B81 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339761,
          LocalizedText.english("m⁻²/s"),
          LocalizedText.english("reciprocal metre squared reciprocal second"));

  public static final EUInformation CODE_A65 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273717,
          LocalizedText.english("erg/(cm²·s)"),
          LocalizedText.english("erg per square centimetre second"));

  public static final EUInformation CODE_D21 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469297,
          LocalizedText.english("m²/kg"),
          LocalizedText.english("square metre per kilogram"));

  public static final EUInformation CODE_B12 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4337970,
          LocalizedText.english("J/m"),
          LocalizedText.english("joule per metre"));

  public static final EUInformation CODE_A54 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273460,
          LocalizedText.english("eV/m"),
          LocalizedText.english("electronvolt per metre"));

  public static final EUInformation CODE_A58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273464,
          LocalizedText.english("erg/cm"),
          LocalizedText.english("erg per centimetre"));

  public static final EUInformation CODE_D73 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470579,
          LocalizedText.english("J·m²"),
          LocalizedText.english("joule square metre"));

  public static final EUInformation CODE_A55 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273461,
          LocalizedText.english("eV·m²"),
          LocalizedText.english("electronvolt square metre"));

  public static final EUInformation CODE_A66 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273718,
          LocalizedText.english("erg·cm²"),
          LocalizedText.english("erg square centimetre"));

  public static final EUInformation CODE_B20 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338224,
          LocalizedText.english("J·m²/kg"),
          LocalizedText.english("joule square metre per kilogram"));

  public static final EUInformation CODE_A56 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273462,
          LocalizedText.english("eV·m²/kg"),
          LocalizedText.english("electronvolt square metre per kilogram"));

  public static final EUInformation CODE_A67 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273719,
          LocalizedText.english("erg·cm²/g"),
          LocalizedText.english("erg square centimetre per gram"));

  public static final EUInformation CODE_D26 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469302,
          LocalizedText.english("m²/(V·s)"),
          LocalizedText.english("square metre per volt second"));

  public static final EUInformation CODE_H58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732216,
          LocalizedText.english("m/(V·s)"),
          LocalizedText.english("metre per volt second"));

  public static final EUInformation CODE_C87 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405303,
          LocalizedText.english("m⁻³/s"),
          LocalizedText.english("reciprocal cubic metre per second"));

  public static final EUInformation CODE_A95 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4274485,
          LocalizedText.english("Gy"),
          LocalizedText.english("gray"));

  public static final EUInformation CODE_C13 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403507,
          LocalizedText.english("mGy"),
          LocalizedText.english("milligray"));

  public static final EUInformation CODE_C80 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405296,
          LocalizedText.english("rad"),
          LocalizedText.english("rad"));

  public static final EUInformation CODE_A61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273713,
          LocalizedText.english("erg/g"),
          LocalizedText.english("erg per gram"));

  public static final EUInformation CODE_D13 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469043,
          LocalizedText.english("Sv"),
          LocalizedText.english("sievert"));

  public static final EUInformation CODE_C28 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403768,
          LocalizedText.english("mSv"),
          LocalizedText.english("millisievert"));

  public static final EUInformation CODE_D91 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4471089,
          LocalizedText.english("rem"),
          LocalizedText.english("rem"));

  public static final EUInformation CODE_L31 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4993841,
          LocalizedText.english("mrem"),
          LocalizedText.english("milliroentgen aequivalent men"));

  public static final EUInformation CODE_A96 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4274486,
          LocalizedText.english("Gy/s"),
          LocalizedText.english("gray per second"));

  public static final EUInformation CODE_A62 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273714,
          LocalizedText.english("erg/g·s"),
          LocalizedText.english("erg per gram second"));

  public static final EUInformation CODE_CKG =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4410183,
          LocalizedText.english("C/kg"),
          LocalizedText.english("coulomb per kilogram"));

  public static final EUInformation CODE_C8 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          17208,
          LocalizedText.english("mC/kg"),
          LocalizedText.english("millicoulomb per kilogram"));

  public static final EUInformation CODE_2C =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          12867,
          LocalizedText.english("R"),
          LocalizedText.english("roentgen"));

  public static final EUInformation CODE_2Y =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          12889,
          LocalizedText.english("mR"),
          LocalizedText.english("milliroentgen"));

  public static final EUInformation CODE_J53 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4863283,
          LocalizedText.english("C·m²/kg"),
          LocalizedText.english("coulomb square metre per kilogram"));

  public static final EUInformation CODE_KR =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          19282,
          LocalizedText.english("kR"),
          LocalizedText.english("kiloroentgen"));

  public static final EUInformation CODE_A31 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272945,
          LocalizedText.english("C/(kg·s)"),
          LocalizedText.english("coulomb per kilogram second"));

  public static final EUInformation CODE_D6 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          17462,
          LocalizedText.english("R/s"),
          LocalizedText.english("roentgen per second"));

  public static final EUInformation CODE_P54 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256500,
          LocalizedText.english("mGy/s"),
          LocalizedText.english("milligray per second"));

  public static final EUInformation CODE_P55 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256501,
          LocalizedText.english("µGy/s"),
          LocalizedText.english("microgray per second"));

  public static final EUInformation CODE_P56 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256502,
          LocalizedText.english("nGy/s"),
          LocalizedText.english("nanogray per second"));

  public static final EUInformation CODE_P57 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256503,
          LocalizedText.english("Gy/min"),
          LocalizedText.english("gray per minute"));

  public static final EUInformation CODE_P58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256504,
          LocalizedText.english("mGy/min"),
          LocalizedText.english("milligray per minute"));

  public static final EUInformation CODE_P59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256505,
          LocalizedText.english("µGy/min"),
          LocalizedText.english("microgray per minute"));

  public static final EUInformation CODE_P60 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256752,
          LocalizedText.english("nGy/min"),
          LocalizedText.english("nanogray per minute"));

  public static final EUInformation CODE_P61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256753,
          LocalizedText.english("Gy/h"),
          LocalizedText.english("gray per hour"));

  public static final EUInformation CODE_P62 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256754,
          LocalizedText.english("mGy/h"),
          LocalizedText.english("milligray per hour"));

  public static final EUInformation CODE_P63 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256755,
          LocalizedText.english("µGy/h"),
          LocalizedText.english("microgray per hour"));

  public static final EUInformation CODE_P64 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256756,
          LocalizedText.english("nGy/h"),
          LocalizedText.english("nanogray per hour"));

  public static final EUInformation CODE_P65 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256757,
          LocalizedText.english("Sv/s"),
          LocalizedText.english("sievert per second"));

  public static final EUInformation CODE_P66 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256758,
          LocalizedText.english("mSv/s"),
          LocalizedText.english("millisievert per second"));

  public static final EUInformation CODE_P67 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256759,
          LocalizedText.english("µSv/s"),
          LocalizedText.english("microsievert per second"));

  public static final EUInformation CODE_P68 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256760,
          LocalizedText.english("nSv/s"),
          LocalizedText.english("nanosievert per second"));

  public static final EUInformation CODE_P69 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256761,
          LocalizedText.english("rem/s"),
          LocalizedText.english("rem per second"));

  public static final EUInformation CODE_P70 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257008,
          LocalizedText.english("Sv/h"),
          LocalizedText.english("sievert per hour"));

  public static final EUInformation CODE_P71 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257009,
          LocalizedText.english("mSv/h"),
          LocalizedText.english("millisievert per hour"));

  public static final EUInformation CODE_P72 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257010,
          LocalizedText.english("µSv/h"),
          LocalizedText.english("microsievert per hour"));

  public static final EUInformation CODE_P73 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257011,
          LocalizedText.english("nSv/h"),
          LocalizedText.english("nanosievert per hour"));

  public static final EUInformation CODE_P74 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257012,
          LocalizedText.english("Sv/min"),
          LocalizedText.english("sievert per minute"));

  public static final EUInformation CODE_P75 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257013,
          LocalizedText.english("mSv/min"),
          LocalizedText.english("millisievert per minute"));

  public static final EUInformation CODE_P76 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257014,
          LocalizedText.english("µSv/min"),
          LocalizedText.english("microsievert per minute"));

  public static final EUInformation CODE_P77 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257015,
          LocalizedText.english("nSv/min"),
          LocalizedText.english("nanosievert per minute"));

  public static final EUInformation CODE_P78 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257016,
          LocalizedText.english("1/in²"),
          LocalizedText.english("reciprocal square inch"));

  public static final EUInformation CODE_P53 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5256499,
          LocalizedText.english("unit pole"),
          LocalizedText.english("unit pole"));

  public static final EUInformation CODE_C85 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405301,
          LocalizedText.english("Å⁻¹"),
          LocalizedText.english("reciprocal angstrom"));

  public static final EUInformation CODE_D94 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4471092,
          LocalizedText.english("s/(rad·m³)"),
          LocalizedText.english("second per cubic metre radian"));

  public static final EUInformation CODE_C90 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405552,
          LocalizedText.english("J⁻¹/m³"),
          LocalizedText.english("reciprocal joule per cubic metre"));

  public static final EUInformation CODE_C88 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405304,
          LocalizedText.english("eV⁻¹/m³"),
          LocalizedText.english("reciprocal electron volt per cubic metre"));

  public static final EUInformation CODE_A38 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4272952,
          LocalizedText.english("m³/C"),
          LocalizedText.english("cubic metre per coulomb"));

  public static final EUInformation CODE_D48 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469816,
          LocalizedText.english("V/K"),
          LocalizedText.english("volt per kelvin"));

  public static final EUInformation CODE_D49 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469817,
          LocalizedText.english("mV/K"),
          LocalizedText.english("millivolt per kelvin"));

  public static final EUInformation CODE_A6 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16694,
          LocalizedText.english("A/(m²·K²)"),
          LocalizedText.english("ampere per square metre kelvin squared"));

  public static final EUInformation CODE_33 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13107,
          LocalizedText.english("kPa·m²/g"),
          LocalizedText.english("kilopascal square metre per gram"));

  public static final EUInformation CODE_P79 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257017,
          LocalizedText.english("Pa/(kg/m²)"),
          LocalizedText.english("pascal square metre per kilogram"));

  public static final EUInformation CODE_34 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13108,
          LocalizedText.english("kPa/mm"),
          LocalizedText.english("kilopascal per millimetre"));

  public static final EUInformation CODE_H42 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731954,
          LocalizedText.english("Pa/m"),
          LocalizedText.english("pascal per metre"));

  public static final EUInformation CODE_H69 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732473,
          LocalizedText.english("pPa/km"),
          LocalizedText.english("picopascal per kilometre"));

  public static final EUInformation CODE_P80 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257264,
          LocalizedText.english("mPa/m"),
          LocalizedText.english("millipascal per metre"));

  public static final EUInformation CODE_P81 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257265,
          LocalizedText.english("kPa/m"),
          LocalizedText.english("kilopascal per metre"));

  public static final EUInformation CODE_P82 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257266,
          LocalizedText.english("hPa/m"),
          LocalizedText.english("hectopascal per metre"));

  public static final EUInformation CODE_P83 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257267,
          LocalizedText.english("Atm/m"),
          LocalizedText.english("standard atmosphere per metre"));

  public static final EUInformation CODE_P84 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257268,
          LocalizedText.english("at/m"),
          LocalizedText.english("technical atmosphere per metre"));

  public static final EUInformation CODE_P85 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257269,
          LocalizedText.english("Torr/m"),
          LocalizedText.english("torr per metre"));

  public static final EUInformation CODE_P86 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257270,
          LocalizedText.english("psi/in"),
          LocalizedText.english("psi per inch"));

  public static final EUInformation CODE_35 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13109,
          LocalizedText.english("ml/(cm²·s)"),
          LocalizedText.english("millilitre per square centimetre second"));

  public static final EUInformation CODE_P87 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257271,
          LocalizedText.english("(m³/s)/m²"),
          LocalizedText.english("cubic metre per second square metre"));

  public static final EUInformation CODE_OPM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5197901,
          LocalizedText.english("o/min"),
          LocalizedText.english("oscillations per minute"));

  public static final EUInformation CODE_KNM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4935245,
          LocalizedText.english("KN/m2"),
          LocalizedText.english("kilonewton per square metre"));

  public static final EUInformation CODE_Q35 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321525,
          LocalizedText.english("MW/min"),
          LocalizedText.english("megawatts per minute"));

  public static final EUInformation CODE_38 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13112,
          LocalizedText.english("oz/(ft²/cin)"),
          LocalizedText.english("ounce per square foot per 0,01inch"));

  public static final EUInformation CODE_59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13625,
          LocalizedText.english("ppm"),
          LocalizedText.english("part per million"));

  public static final EUInformation CODE_61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13873,
          LocalizedText.english("ppb"),
          LocalizedText.english("part per billion (US)"));

  public static final EUInformation CODE_66 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13878,
          LocalizedText.english("Oe"),
          LocalizedText.english("oersted"));

  public static final EUInformation CODE_76 =
      new EUInformation(
          CEFACT_NAMESPACE_URI, 14134, LocalizedText.english("Gs"), LocalizedText.english("gauss"));

  public static final EUInformation CODE_78 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          14136,
          LocalizedText.english("kGs"),
          LocalizedText.english("kilogauss"));

  public static final EUInformation CODE_2G =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          12871,
          LocalizedText.english("V"),
          LocalizedText.english("volt AC"));

  public static final EUInformation CODE_2H =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          12872,
          LocalizedText.english("V"),
          LocalizedText.english("volt DC"));

  public static final EUInformation CODE_2P =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          12880,
          LocalizedText.english("kbyte"),
          LocalizedText.english("kilobyte"));

  public static final EUInformation CODE_4L =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          13388,
          LocalizedText.english("Mbyte"),
          LocalizedText.english("megabyte"));

  public static final EUInformation CODE_A43 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273203,
          LocalizedText.english("dwt"),
          LocalizedText.english("deadweight tonnage"));

  public static final EUInformation CODE_A47 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273207,
          LocalizedText.english("dtex (g/10km)"),
          LocalizedText.english("decitex"));

  public static final EUInformation CODE_A49 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4273209,
          LocalizedText.english("den (g/9 km)"),
          LocalizedText.english("denier"));

  public static final EUInformation CODE_A99 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4274489,
          LocalizedText.english("bit"),
          LocalizedText.english("bit"));

  public static final EUInformation CODE_AB =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16706,
          LocalizedText.english("pk"),
          LocalizedText.english("bulk pack"));

  public static final EUInformation CODE_AD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16708,
          LocalizedText.english("byte"),
          LocalizedText.english("byte"));

  public static final EUInformation CODE_B1 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          16945,
          LocalizedText.english("barrel (US)/d"),
          LocalizedText.english("barrel (US) per day"));

  public static final EUInformation CODE_B10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4337968,
          LocalizedText.english("bit/s"),
          LocalizedText.english("bit per second"));

  public static final EUInformation CODE_B30 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4338480,
          LocalizedText.english("Gibit"),
          LocalizedText.english("gibibit"));

  public static final EUInformation CODE_B65 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339253,
          LocalizedText.english("Mx"),
          LocalizedText.english("maxwell"));

  public static final EUInformation CODE_B68 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339256,
          LocalizedText.english("Gbit"),
          LocalizedText.english("gigabit"));

  public static final EUInformation CODE_B80 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4339760,
          LocalizedText.english("Gbit/s"),
          LocalizedText.english("gigabit per second"));

  public static final EUInformation CODE_BFT =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4343380,
          LocalizedText.english("fbm"),
          LocalizedText.english("board foot"));

  public static final EUInformation CODE_BPM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4345933,
          LocalizedText.english("BPM"),
          LocalizedText.english("beats per minute"));

  public static final EUInformation CODE_C21 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4403761,
          LocalizedText.english("Kibit"),
          LocalizedText.english("kibibit"));

  public static final EUInformation CODE_C37 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4404023,
          LocalizedText.english("kbit"),
          LocalizedText.english("kilobit"));

  public static final EUInformation CODE_C74 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405044,
          LocalizedText.english("kbit/s"),
          LocalizedText.english("kilobit per second"));

  public static final EUInformation CODE_C79 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4405049,
          LocalizedText.english("kVAh"),
          LocalizedText.english("kilovolt ampere hour"));

  public static final EUInformation CODE_D03 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4468787,
          LocalizedText.english("kW·h/h"),
          LocalizedText.english("kilowatt hour per hour"));

  public static final EUInformation CODE_D11 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469041,
          LocalizedText.english("Mibit"),
          LocalizedText.english("mebibit"));

  public static final EUInformation CODE_D34 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469556,
          LocalizedText.english("tex (g/km)"),
          LocalizedText.english("tex"));

  public static final EUInformation CODE_D36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4469558,
          LocalizedText.english("Mbit"),
          LocalizedText.english("megabit"));

  public static final EUInformation CODE_D78 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4470584,
          LocalizedText.english("MJ/s"),
          LocalizedText.english("megajoule per second"));

  public static final EUInformation CODE_DZN =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4479566,
          LocalizedText.english("DOZ"),
          LocalizedText.english("dozen"));

  public static final EUInformation CODE_E07 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4534327,
          LocalizedText.english("MW·h/h"),
          LocalizedText.english("megawatt hour per hour"));

  public static final EUInformation CODE_E08 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4534328,
          LocalizedText.english("MW/Hz"),
          LocalizedText.english("megawatt per hertz"));

  public static final EUInformation CODE_E10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4534576,
          LocalizedText.english("deg da"),
          LocalizedText.english("degree day"));

  public static final EUInformation CODE_E16 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4534582,
          LocalizedText.english("BtuIT/h"),
          LocalizedText.english("million Btu(IT) per hour"));

  public static final EUInformation CODE_E17 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4534583,
          LocalizedText.english("ft³/s"),
          LocalizedText.english("cubic foot per second"));

  public static final EUInformation CODE_E20 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4534832,
          LocalizedText.english("Mbit/s"),
          LocalizedText.english("megabit per second"));

  public static final EUInformation CODE_E31 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535089,
          LocalizedText.english("m²/l"),
          LocalizedText.english("square metre per litre"));

  public static final EUInformation CODE_E32 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535090,
          LocalizedText.english("l/h"),
          LocalizedText.english("litre per hour"));

  public static final EUInformation CODE_E34 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535092,
          LocalizedText.english("Gbyte"),
          LocalizedText.english("gigabyte"));

  public static final EUInformation CODE_E35 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535093,
          LocalizedText.english("Tbyte"),
          LocalizedText.english("terabyte"));

  public static final EUInformation CODE_E36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535094,
          LocalizedText.english("Pbyte"),
          LocalizedText.english("petabyte"));

  public static final EUInformation CODE_E39 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535097,
          LocalizedText.english("dpi"),
          LocalizedText.english("dots per inch"));

  public static final EUInformation CODE_E40 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535344,
          LocalizedText.english("ppht"),
          LocalizedText.english("part per hundred thousand"));

  public static final EUInformation CODE_E44 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535348,
          LocalizedText.english("kgf·m/cm²"),
          LocalizedText.english("kilogram-force metre per square centimetre"));

  public static final EUInformation CODE_E46 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535350,
          LocalizedText.english("kW·h/m³"),
          LocalizedText.english("kilowatt hour per cubic metre"));

  public static final EUInformation CODE_E47 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535351,
          LocalizedText.english("kW·h/K"),
          LocalizedText.english("kilowatt hour per kelvin"));

  public static final EUInformation CODE_E58 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535608,
          LocalizedText.english("Ebit/s"),
          LocalizedText.english("exabit per second"));

  public static final EUInformation CODE_E59 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535609,
          LocalizedText.english("Eibyte"),
          LocalizedText.english("exbibyte"));

  public static final EUInformation CODE_E60 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535856,
          LocalizedText.english("Pibyte"),
          LocalizedText.english("pebibyte"));

  public static final EUInformation CODE_E61 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535857,
          LocalizedText.english("Tibyte"),
          LocalizedText.english("tebibyte"));

  public static final EUInformation CODE_E62 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535858,
          LocalizedText.english("Gibyte"),
          LocalizedText.english("gibibyte"));

  public static final EUInformation CODE_E63 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535859,
          LocalizedText.english("Mibyte"),
          LocalizedText.english("mebibyte"));

  public static final EUInformation CODE_E64 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535860,
          LocalizedText.english("Kibyte"),
          LocalizedText.english("kibibyte"));

  public static final EUInformation CODE_E65 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535861,
          LocalizedText.english("Eibit/m"),
          LocalizedText.english("exbibit per metre"));

  public static final EUInformation CODE_E66 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535862,
          LocalizedText.english("Eibit/m²"),
          LocalizedText.english("exbibit per square metre"));

  public static final EUInformation CODE_E67 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535863,
          LocalizedText.english("Eibit/m³"),
          LocalizedText.english("exbibit per cubic metre"));

  public static final EUInformation CODE_E68 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535864,
          LocalizedText.english("Gbyte/s"),
          LocalizedText.english("gigabyte per second"));

  public static final EUInformation CODE_E69 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4535865,
          LocalizedText.english("Gibit/m"),
          LocalizedText.english("gibibit per metre"));

  public static final EUInformation CODE_E70 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536112,
          LocalizedText.english("Gibit/m²"),
          LocalizedText.english("gibibit per square metre"));

  public static final EUInformation CODE_E71 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536113,
          LocalizedText.english("Gibit/m³"),
          LocalizedText.english("gibibit per cubic metre"));

  public static final EUInformation CODE_E72 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536114,
          LocalizedText.english("Kibit/m"),
          LocalizedText.english("kibibit per metre"));

  public static final EUInformation CODE_E73 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536115,
          LocalizedText.english("Kibit/m²"),
          LocalizedText.english("kibibit per square metre"));

  public static final EUInformation CODE_E74 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536116,
          LocalizedText.english("Kibit/m³"),
          LocalizedText.english("kibibit per cubic metre"));

  public static final EUInformation CODE_E75 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536117,
          LocalizedText.english("Mibit/m"),
          LocalizedText.english("mebibit per metre"));

  public static final EUInformation CODE_E76 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536118,
          LocalizedText.english("Mibit/m²"),
          LocalizedText.english("mebibit per square metre"));

  public static final EUInformation CODE_E77 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536119,
          LocalizedText.english("Mibit/m³"),
          LocalizedText.english("mebibit per cubic metre"));

  public static final EUInformation CODE_E78 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536120,
          LocalizedText.english("Pbit"),
          LocalizedText.english("petabit"));

  public static final EUInformation CODE_E79 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536121,
          LocalizedText.english("Pbit/s"),
          LocalizedText.english("petabit per second"));

  public static final EUInformation CODE_E80 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536368,
          LocalizedText.english("Pibit/m"),
          LocalizedText.english("pebibit per metre"));

  public static final EUInformation CODE_E81 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536369,
          LocalizedText.english("Pibit/m²"),
          LocalizedText.english("pebibit per square metre"));

  public static final EUInformation CODE_E82 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536370,
          LocalizedText.english("Pibit/m³"),
          LocalizedText.english("pebibit per cubic metre"));

  public static final EUInformation CODE_E83 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536371,
          LocalizedText.english("Tbit"),
          LocalizedText.english("terabit"));

  public static final EUInformation CODE_E84 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536372,
          LocalizedText.english("Tbit/s"),
          LocalizedText.english("terabit per second"));

  public static final EUInformation CODE_E85 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536373,
          LocalizedText.english("Tibit/m"),
          LocalizedText.english("tebibit per metre"));

  public static final EUInformation CODE_E86 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536374,
          LocalizedText.english("Tibit/m³"),
          LocalizedText.english("tebibit per cubic metre"));

  public static final EUInformation CODE_E87 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536375,
          LocalizedText.english("Tibit/m²"),
          LocalizedText.english("tebibit per square metre"));

  public static final EUInformation CODE_E88 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536376,
          LocalizedText.english("bit/m"),
          LocalizedText.english("bit per metre"));

  public static final EUInformation CODE_E89 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536377,
          LocalizedText.english("bit/m²"),
          LocalizedText.english("bit per square metre"));

  public static final EUInformation CODE_E90 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536624,
          LocalizedText.english("cm⁻¹"),
          LocalizedText.english("reciprocal centimetre"));

  public static final EUInformation CODE_E91 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4536625,
          LocalizedText.english("d⁻¹"),
          LocalizedText.english("reciprocal day"));

  public static final EUInformation CODE_F01 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4599857,
          LocalizedText.english("bit/m³"),
          LocalizedText.english("bit per cubic metre"));

  public static final EUInformation CODE_FC =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          17987,
          LocalizedText.english("kft³"),
          LocalizedText.english("thousand cubic foot"));

  public static final EUInformation CODE_FIT =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4606292,
          LocalizedText.english("FIT"),
          LocalizedText.english("failures in time"));

  public static final EUInformation CODE_GB =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          18242,
          LocalizedText.english("gal (US)/d"),
          LocalizedText.english("gallon (US) per day"));

  public static final EUInformation CODE_GFI =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4671049,
          LocalizedText.english("gi F/S"),
          LocalizedText.english("gram of fissile isotope"));

  public static final EUInformation CODE_GIA =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4671809,
          LocalizedText.english("gi (US)"),
          LocalizedText.english("gill (US)"));

  public static final EUInformation CODE_GII =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4671817,
          LocalizedText.english("gi (UK)"),
          LocalizedText.english("gill (UK)"));

  public static final EUInformation CODE_GRO =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4674127,
          LocalizedText.english("gr"),
          LocalizedText.english("gross"));

  public static final EUInformation CODE_H25 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4731445,
          LocalizedText.english("%/K"),
          LocalizedText.english("percent per kelvin"));

  public static final EUInformation CODE_H71 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732721,
          LocalizedText.english("%/mo"),
          LocalizedText.english("percent per month"));

  public static final EUInformation CODE_H72 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732722,
          LocalizedText.english("%/hbar"),
          LocalizedText.english("percent per hectobar"));

  public static final EUInformation CODE_H73 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732723,
          LocalizedText.english("%/daK"),
          LocalizedText.english("percent per decakelvin"));

  public static final EUInformation CODE_H77 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732727,
          LocalizedText.english("MW"),
          LocalizedText.english("module width"));

  public static final EUInformation CODE_H80 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732976,
          LocalizedText.english("U or RU"),
          LocalizedText.english("rack unit"));

  public static final EUInformation CODE_H82 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732978,
          LocalizedText.english("bp"),
          LocalizedText.english("big point"));

  public static final EUInformation CODE_H89 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4732985,
          LocalizedText.english("%/Ω"),
          LocalizedText.english("percent per ohm"));

  public static final EUInformation CODE_H90 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733232,
          LocalizedText.english("%/°"),
          LocalizedText.english("percent per degree"));

  public static final EUInformation CODE_H91 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733233,
          LocalizedText.english("%/10000"),
          LocalizedText.english("percent per ten thousand"));

  public static final EUInformation CODE_H92 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733234,
          LocalizedText.english("%/100000"),
          LocalizedText.english("percent per one hundred thousand"));

  public static final EUInformation CODE_H93 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733235,
          LocalizedText.english("%/100"),
          LocalizedText.english("percent per hundred"));

  public static final EUInformation CODE_H94 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733236,
          LocalizedText.english("%/1000"),
          LocalizedText.english("percent per thousand"));

  public static final EUInformation CODE_H95 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733237,
          LocalizedText.english("%/V"),
          LocalizedText.english("percent per volt"));

  public static final EUInformation CODE_H96 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733238,
          LocalizedText.english("%/bar"),
          LocalizedText.english("percent per bar"));

  public static final EUInformation CODE_H98 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733240,
          LocalizedText.english("%/in"),
          LocalizedText.english("percent per inch"));

  public static final EUInformation CODE_H99 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4733241,
          LocalizedText.english("%/m"),
          LocalizedText.english("percent per metre"));

  public static final EUInformation CODE_HMQ =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4738385,
          LocalizedText.english("Mm³"),
          LocalizedText.english("million cubic metre"));

  public static final EUInformation CODE_J10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862256,
          LocalizedText.english("%/mm"),
          LocalizedText.english("percent per millimetre"));

  public static final EUInformation CODE_J12 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862258,
          LocalizedText.english("‰/psi"),
          LocalizedText.english("per mille per psi"));

  public static final EUInformation CODE_J13 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862259,
          LocalizedText.english("°API"),
          LocalizedText.english("degree API"));

  public static final EUInformation CODE_J14 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862260,
          LocalizedText.english("°Bé"),
          LocalizedText.english("degree Baume (origin scale)"));

  public static final EUInformation CODE_J15 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862261,
          LocalizedText.english("°Bé (US heavy)"),
          LocalizedText.english("degree Baume (US heavy)"));

  public static final EUInformation CODE_J16 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862262,
          LocalizedText.english("°Bé (US light)"),
          LocalizedText.english("degree Baume (US light)"));

  public static final EUInformation CODE_J17 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862263,
          LocalizedText.english("°Balling"),
          LocalizedText.english("degree Balling"));

  public static final EUInformation CODE_J18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862264,
          LocalizedText.english("°Bx"),
          LocalizedText.english("degree Brix"));

  public static final EUInformation CODE_J27 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862519,
          LocalizedText.english("°Oechsle"),
          LocalizedText.english("degree Oechsle"));

  public static final EUInformation CODE_J31 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862769,
          LocalizedText.english("°Tw"),
          LocalizedText.english("degree Twaddell"));

  public static final EUInformation CODE_J38 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4862776,
          LocalizedText.english("Bd"),
          LocalizedText.english("baud"));

  public static final EUInformation CODE_J54 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4863284,
          LocalizedText.english("MBd"),
          LocalizedText.english("megabaud"));

  public static final EUInformation CODE_K3 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          19251,
          LocalizedText.english("kvar·h"),
          LocalizedText.english("kilovolt ampere reactive hour"));

  public static final EUInformation CODE_K50 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4928816,
          LocalizedText.english("kBd"),
          LocalizedText.english("kilobaud"));

  public static final EUInformation CODE_KCC =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4932419,
          LocalizedText.english("kg C₅ H₁₄ClNO"),
          LocalizedText.english("kilogram of choline chloride"));

  public static final EUInformation CODE_KDW =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4932695,
          LocalizedText.english("kg/net eda"),
          LocalizedText.english("kilogram drained net weight"));

  public static final EUInformation CODE_KHY =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4933721,
          LocalizedText.english("kg H₂O₂"),
          LocalizedText.english("kilogram of hydrogen peroxide"));

  public static final EUInformation CODE_KMA =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4934977,
          LocalizedText.english("kg met.am."),
          LocalizedText.english("kilogram of methylamine"));

  public static final EUInformation CODE_KNI =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4935241,
          LocalizedText.english("kg N"),
          LocalizedText.english("kilogram of nitrogen"));

  public static final EUInformation CODE_KPH =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4935752,
          LocalizedText.english("kg KOH"),
          LocalizedText.english("kilogram of potassium hydroxide (caustic potash)"));

  public static final EUInformation CODE_KPO =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4935759,
          LocalizedText.english("kg K₂O"),
          LocalizedText.english("kilogram of potassium oxide"));

  public static final EUInformation CODE_KSD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4936516,
          LocalizedText.english("kg 90 % sdt"),
          LocalizedText.english("kilogram of substance 90 % dry"));

  public static final EUInformation CODE_KSH =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4936520,
          LocalizedText.english("kg NaOH"),
          LocalizedText.english("kilogram of sodium hydroxide (caustic soda)"));

  public static final EUInformation CODE_KUR =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4937042,
          LocalizedText.english("kg U"),
          LocalizedText.english("kilogram of uranium"));

  public static final EUInformation CODE_KWY =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4937561,
          LocalizedText.english("kW/year"),
          LocalizedText.english("kilowatt year"));

  public static final EUInformation CODE_KWO =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4937551,
          LocalizedText.english("kg WO₃"),
          LocalizedText.english("kilogram of tungsten trioxide"));

  public static final EUInformation CODE_M19 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5058873,
          LocalizedText.english("Bft"),
          LocalizedText.english("Beaufort"));

  public static final EUInformation CODE_M25 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059125,
          LocalizedText.english("%/°C"),
          LocalizedText.english("percent per degree Celsius"));

  public static final EUInformation CODE_M36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059382,
          LocalizedText.english("mo (30 days)"),
          LocalizedText.english("30-day month"));

  public static final EUInformation CODE_M37 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5059383,
          LocalizedText.english("y (360 days)"),
          LocalizedText.english("actual/360"));

  public static final EUInformation CODE_M9 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          19769,
          LocalizedText.english("MBTU/kft³"),
          LocalizedText.english("million Btu per 1000 cubic foot"));

  public static final EUInformation CODE_MAH =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5062984,
          LocalizedText.english("Mvar·h"),
          LocalizedText.english("megavolt ampere reactive hour"));

  public static final EUInformation CODE_NIL =
      new EUInformation(
          CEFACT_NAMESPACE_URI, 5130572, LocalizedText.english("()"), LocalizedText.english("nil"));

  public static final EUInformation CODE_NX =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          20056,
          LocalizedText.english("‰"),
          LocalizedText.english("part per thousand"));

  public static final EUInformation CODE_P1 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          20529,
          LocalizedText.english("% or pct"),
          LocalizedText.english("percent"));

  public static final EUInformation CODE_P88 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257272,
          LocalizedText.english("rhe"),
          LocalizedText.english("rhe"));

  public static final EUInformation CODE_P89 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257273,
          LocalizedText.english("lbf·ft/in"),
          LocalizedText.english("pound-force foot per inch"));

  public static final EUInformation CODE_P90 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257520,
          LocalizedText.english("lbf·in/in"),
          LocalizedText.english("pound-force inch per inch"));

  public static final EUInformation CODE_P91 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257521,
          LocalizedText.english("perm (0 ºC)"),
          LocalizedText.english("perm (0 ºC)"));

  public static final EUInformation CODE_P92 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257522,
          LocalizedText.english("perm (23 ºC)"),
          LocalizedText.english("perm (23 ºC)"));

  public static final EUInformation CODE_P93 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257523,
          LocalizedText.english("byte/s"),
          LocalizedText.english("byte per second"));

  public static final EUInformation CODE_P94 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257524,
          LocalizedText.english("kbyte/s"),
          LocalizedText.english("kilobyte per second"));

  public static final EUInformation CODE_P95 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257525,
          LocalizedText.english("Mbyte/s"),
          LocalizedText.english("megabyte per second"));

  public static final EUInformation CODE_P96 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257526,
          LocalizedText.english("1/V"),
          LocalizedText.english("reciprocal volt"));

  public static final EUInformation CODE_P97 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257527,
          LocalizedText.english("1/rad"),
          LocalizedText.english("reciprocal radian"));

  public static final EUInformation CODE_P98 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257528,
          LocalizedText.english("PaΣνB"),
          LocalizedText.english("pascal to the power sum of stoichiometric numbers"));

  public static final EUInformation CODE_P99 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5257529,
          LocalizedText.english("(mol/m³)∑νB"),
          LocalizedText.english("mole per cubiv metre to the power sum of stoichiometric numbers"));

  public static final EUInformation CODE_PLA =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5262401,
          LocalizedText.english("°P"),
          LocalizedText.english("degree Plato"));

  public static final EUInformation CODE_PQ =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          20561,
          LocalizedText.english("ppi"),
          LocalizedText.english("page per inch"));

  public static final EUInformation CODE_PTN =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5264462,
          LocalizedText.english("PTN"),
          LocalizedText.english("portion"));

  public static final EUInformation CODE_Q10 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321008,
          LocalizedText.english("J/T"),
          LocalizedText.english("joule per tesla"));

  public static final EUInformation CODE_Q11 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321009,
          LocalizedText.english("E"),
          LocalizedText.english("erlang"));

  public static final EUInformation CODE_Q12 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321010,
          LocalizedText.english("o"),
          LocalizedText.english("octet"));

  public static final EUInformation CODE_Q13 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321011,
          LocalizedText.english("o/s"),
          LocalizedText.english("octet per second"));

  public static final EUInformation CODE_Q14 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321012,
          LocalizedText.english("Sh"),
          LocalizedText.english("shannon"));

  public static final EUInformation CODE_Q15 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321013,
          LocalizedText.english("Hart"),
          LocalizedText.english("hartley"));

  public static final EUInformation CODE_Q16 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321014,
          LocalizedText.english("nat"),
          LocalizedText.english("natural unit of information"));

  public static final EUInformation CODE_Q17 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321015,
          LocalizedText.english("Sh/s"),
          LocalizedText.english("shannon per second"));

  public static final EUInformation CODE_Q18 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321016,
          LocalizedText.english("Hart/s"),
          LocalizedText.english("hartley per second"));

  public static final EUInformation CODE_Q19 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321017,
          LocalizedText.english("nat/s"),
          LocalizedText.english("natural unit of information per second"));

  public static final EUInformation CODE_Q20 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321264,
          LocalizedText.english("s/kg"),
          LocalizedText.english("second per kilogramm"));

  public static final EUInformation CODE_Q21 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321265,
          LocalizedText.english("W·m²"),
          LocalizedText.english("watt square metre"));

  public static final EUInformation CODE_Q22 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321266,
          LocalizedText.english("1/(Hz·rad·m³)"),
          LocalizedText.english("second per radian cubic metre"));

  public static final EUInformation CODE_Q23 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321267,
          LocalizedText.english("1/Wb"),
          LocalizedText.english("weber to the power minus one"));

  public static final EUInformation CODE_Q24 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321268,
          LocalizedText.english("1/in"),
          LocalizedText.english("reciprocal inch"));

  public static final EUInformation CODE_Q25 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321269,
          LocalizedText.english("dpt"),
          LocalizedText.english("dioptre"));

  public static final EUInformation CODE_Q26 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321270,
          LocalizedText.english("1/1"),
          LocalizedText.english("one per one"));

  public static final EUInformation CODE_Q27 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321271,
          LocalizedText.english("N·m/m²"),
          LocalizedText.english("newton metre per metre"));

  public static final EUInformation CODE_Q28 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321272,
          LocalizedText.english("kg/(m²·Pa·s)"),
          LocalizedText.english("kilogram per square metre pascal second"));

  public static final EUInformation CODE_Q36 =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5321526,
          LocalizedText.english("m2/m3"),
          LocalizedText.english("square metre per cubic metre"));

  public static final EUInformation CODE_QR =
      new EUInformation(
          CEFACT_NAMESPACE_URI, 20818, LocalizedText.english("qr"), LocalizedText.english("quire"));

  public static final EUInformation CODE_QTR =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5330002,
          LocalizedText.english("Qr (UK)"),
          LocalizedText.english("quarter (UK)"));

  public static final EUInformation CODE_TAN =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5521742,
          LocalizedText.english("TAN"),
          LocalizedText.english("total acid number"));

  public static final EUInformation CODE_TKM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5524301,
          LocalizedText.english("t·km"),
          LocalizedText.english("tonne kilometre"));

  public static final EUInformation CODE_TPI =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5525577,
          LocalizedText.english("TPI"),
          LocalizedText.english("teeth per inch"));

  public static final EUInformation CODE_TQD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5525828,
          LocalizedText.english("km³/d"),
          LocalizedText.english("thousand cubic metre per day"));

  public static final EUInformation CODE_VA =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          22081,
          LocalizedText.english("V·A / kg"),
          LocalizedText.english("volt - ampere per kilogram"));

  public static final EUInformation CODE_WA =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          22337,
          LocalizedText.english("W/kg"),
          LocalizedText.english("watt per kilogram"));

  public static final EUInformation CODE_WSD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5722948,
          LocalizedText.english("std"),
          LocalizedText.english("standard"));

  public static final EUInformation CODE_MRW =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5067351,
          LocalizedText.english("m·wk"),
          LocalizedText.english("Metre Week"));

  public static final EUInformation CODE_MKW =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5065559,
          LocalizedText.english("m²· wk"),
          LocalizedText.english("Square Metre Week"));

  public static final EUInformation CODE_MQW =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5067095,
          LocalizedText.english("m³·wk"),
          LocalizedText.english("Cubic Metre Week"));

  public static final EUInformation CODE_HWE =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4740933,
          LocalizedText.english("piece·k"),
          LocalizedText.english("Piece Week"));

  public static final EUInformation CODE_MRD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5067332,
          LocalizedText.english("m·day"),
          LocalizedText.english("Metre Day"));

  public static final EUInformation CODE_MKD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5065540,
          LocalizedText.english("m²·d"),
          LocalizedText.english("Square Metre Day"));

  public static final EUInformation CODE_MQD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5067076,
          LocalizedText.english("m³·d"),
          LocalizedText.english("Cubic Metre Day"));

  public static final EUInformation CODE_HAD =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4735300,
          LocalizedText.english("piece·d"),
          LocalizedText.english("Piece Day"));

  public static final EUInformation CODE_MRM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5067341,
          LocalizedText.english("m·mo"),
          LocalizedText.english("Metre Month"));

  public static final EUInformation CODE_MKM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5065549,
          LocalizedText.english("m²·mo"),
          LocalizedText.english("Square Metre Month"));

  public static final EUInformation CODE_MQM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5067085,
          LocalizedText.english("m³·mo"),
          LocalizedText.english("Cubic Metre Month"));

  public static final EUInformation CODE_HMO =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4738383,
          LocalizedText.english("piece·mo"),
          LocalizedText.english("Piece Month"));

  public static final EUInformation CODE_DBW =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4473431,
          LocalizedText.english("dBW"),
          LocalizedText.english("Decibel watt"));

  public static final EUInformation CODE_DBM =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4473421,
          LocalizedText.english("dBm"),
          LocalizedText.english("Decibel-milliwatts"));

  public static final EUInformation CODE_FNU =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          4607573,
          LocalizedText.english("FNU"),
          LocalizedText.english("Formazin nephelometric unit"));

  public static final EUInformation CODE_NTU =
      new EUInformation(
          CEFACT_NAMESPACE_URI,
          5133397,
          LocalizedText.english("NTU"),
          LocalizedText.english("Nephelometric turbidity unit"));

  static {
    BY_UNIT_ID.put(13396, CODE_4T);
    BY_UNIT_ID.put(5126448, CODE_N90);
    BY_UNIT_ID.put(4273721, CODE_A69);
    BY_UNIT_ID.put(4731448, CODE_H28);
    BY_UNIT_ID.put(4731699, CODE_H33);
    BY_UNIT_ID.put(4339769, CODE_B89);
    BY_UNIT_ID.put(4404274, CODE_C42);
    BY_UNIT_ID.put(4405042, CODE_C72);
    BY_UNIT_ID.put(4272694, CODE_A26);
    BY_UNIT_ID.put(4273201, CODE_A41);
    BY_UNIT_ID.put(4731697, CODE_H31);
    BY_UNIT_ID.put(4339254, CODE_B66);
    BY_UNIT_ID.put(16695, CODE_A7);
    BY_UNIT_ID.put(16692, CODE_A4);
    BY_UNIT_ID.put(4338227, CODE_B23);
    BY_UNIT_ID.put(4666681, CODE_G59);
    BY_UNIT_ID.put(5126451, CODE_N93);
    BY_UNIT_ID.put(4601143, CODE_F57);
    BY_UNIT_ID.put(4601145, CODE_F59);
    BY_UNIT_ID.put(16709, CODE_AE);
    BY_UNIT_ID.put(4338228, CODE_B24);
    BY_UNIT_ID.put(16691, CODE_A3);
    BY_UNIT_ID.put(16690, CODE_A2);
    BY_UNIT_ID.put(4601654, CODE_F76);
    BY_UNIT_ID.put(4599864, CODE_F08);
    BY_UNIT_ID.put(5255472, CODE_P10);
    BY_UNIT_ID.put(4469555, CODE_D33);
    BY_UNIT_ID.put(4403769, CODE_C29);
    BY_UNIT_ID.put(4470833, CODE_D81);
    BY_UNIT_ID.put(4404280, CODE_C48);
    BY_UNIT_ID.put(5255475, CODE_P13);
    BY_UNIT_ID.put(5255474, CODE_P12);
    BY_UNIT_ID.put(5719362, CODE_WEB);
    BY_UNIT_ID.put(4404019, CODE_C33);
    BY_UNIT_ID.put(5255473, CODE_P11);
    BY_UNIT_ID.put(4470073, CODE_D59);
    BY_UNIT_ID.put(4338998, CODE_B56);
    BY_UNIT_ID.put(4470320, CODE_D60);
    BY_UNIT_ID.put(14385, CODE_81);
    BY_UNIT_ID.put(4403508, CODE_C14);
    BY_UNIT_ID.put(4340016, CODE_B90);
    BY_UNIT_ID.put(4404275, CODE_C43);
    BY_UNIT_ID.put(4405043, CODE_C73);
    BY_UNIT_ID.put(4730931, CODE_H03);
    BY_UNIT_ID.put(4730932, CODE_H04);
    BY_UNIT_ID.put(4667704, CODE_G98);
    BY_UNIT_ID.put(4667705, CODE_G99);
    BY_UNIT_ID.put(4730933, CODE_H05);
    BY_UNIT_ID.put(4730934, CODE_H06);
    BY_UNIT_ID.put(5255732, CODE_P24);
    BY_UNIT_ID.put(4274488, CODE_A98);
    BY_UNIT_ID.put(4340017, CODE_B91);
    BY_UNIT_ID.put(4404276, CODE_C44);
    BY_UNIT_ID.put(16693, CODE_A5);
    BY_UNIT_ID.put(16952, CODE_B8);
    BY_UNIT_ID.put(5195853, CODE_OHM);
    BY_UNIT_ID.put(4274231, CODE_A87);
    BY_UNIT_ID.put(4339509, CODE_B75);
    BY_UNIT_ID.put(4731956, CODE_H44);
    BY_UNIT_ID.put(4338745, CODE_B49);
    BY_UNIT_ID.put(4535349, CODE_E45);
    BY_UNIT_ID.put(4340020, CODE_B94);
    BY_UNIT_ID.put(5255730, CODE_P22);
    BY_UNIT_ID.put(5059126, CODE_M26);
    BY_UNIT_ID.put(5458245, CODE_SIE);
    BY_UNIT_ID.put(4338995, CODE_B53);
    BY_UNIT_ID.put(4403767, CODE_C27);
    BY_UNIT_ID.put(4340025, CODE_B99);
    BY_UNIT_ID.put(4666418, CODE_G42);
    BY_UNIT_ID.put(4666419, CODE_G43);
    BY_UNIT_ID.put(5126450, CODE_N92);
    BY_UNIT_ID.put(4404785, CODE_C61);
    BY_UNIT_ID.put(4274232, CODE_A88);
    BY_UNIT_ID.put(4339510, CODE_B76);
    BY_UNIT_ID.put(4732984, CODE_H88);
    BY_UNIT_ID.put(4338992, CODE_B50);
    BY_UNIT_ID.put(4404784, CODE_C60);
    BY_UNIT_ID.put(4403763, CODE_C23);
    BY_UNIT_ID.put(4340021, CODE_B95);
    BY_UNIT_ID.put(4404278, CODE_C46);
    BY_UNIT_ID.put(5059124, CODE_M24);
    BY_UNIT_ID.put(5255731, CODE_P23);
    BY_UNIT_ID.put(4601142, CODE_F56);
    BY_UNIT_ID.put(4731446, CODE_H26);
    BY_UNIT_ID.put(4731703, CODE_H37);
    BY_UNIT_ID.put(4601140, CODE_F54);
    BY_UNIT_ID.put(4731702, CODE_H36);
    BY_UNIT_ID.put(4601141, CODE_F55);
    BY_UNIT_ID.put(4469040, CODE_D10);
    BY_UNIT_ID.put(4731955, CODE_H43);
    BY_UNIT_ID.put(4732465, CODE_H61);
    BY_UNIT_ID.put(4339511, CODE_B77);
    BY_UNIT_ID.put(4338996, CODE_B54);
    BY_UNIT_ID.put(4666421, CODE_G45);
    BY_UNIT_ID.put(4666420, CODE_G44);
    BY_UNIT_ID.put(4994098, CODE_L42);
    BY_UNIT_ID.put(4405305, CODE_C89);
    BY_UNIT_ID.put(5255476, CODE_P14);
    BY_UNIT_ID.put(4469553, CODE_D31);
    BY_UNIT_ID.put(5255477, CODE_P15);
    BY_UNIT_ID.put(5255478, CODE_P16);
    BY_UNIT_ID.put(5255479, CODE_P17);
    BY_UNIT_ID.put(5255480, CODE_P18);
    BY_UNIT_ID.put(5255481, CODE_P19);
    BY_UNIT_ID.put(5255728, CODE_P20);
    BY_UNIT_ID.put(5255729, CODE_P21);
    BY_UNIT_ID.put(4928563, CODE_K43);
    BY_UNIT_ID.put(4404281, CODE_C49);
    BY_UNIT_ID.put(4405045, CODE_C75);
    BY_UNIT_ID.put(4469814, CODE_D46);
    BY_UNIT_ID.put(5068353, CODE_MVA);
    BY_UNIT_ID.put(4937281, CODE_KVA);
    BY_UNIT_ID.put(5059381, CODE_M35);
    BY_UNIT_ID.put(4469812, CODE_D44);
    BY_UNIT_ID.put(19253, CODE_K5);
    BY_UNIT_ID.put(4937298, CODE_KVR);
    BY_UNIT_ID.put(5062994, CODE_MAR);
    BY_UNIT_ID.put(5126449, CODE_N91);
    BY_UNIT_ID.put(5059376, CODE_M30);
    BY_UNIT_ID.put(5058871, CODE_M17);
    BY_UNIT_ID.put(5058872, CODE_M18);
    BY_UNIT_ID.put(5059127, CODE_M27);
    BY_UNIT_ID.put(5059121, CODE_M21);
    BY_UNIT_ID.put(4731700, CODE_H34);
    BY_UNIT_ID.put(4731705, CODE_H39);
    BY_UNIT_ID.put(4405300, CODE_C84);
    BY_UNIT_ID.put(19021, CODE_JM);
    BY_UNIT_ID.put(4337972, CODE_B14);
    BY_UNIT_ID.put(4337971, CODE_B13);
    BY_UNIT_ID.put(17457, CODE_D1);
    BY_UNIT_ID.put(17458, CODE_D2);
    BY_UNIT_ID.put(4405561, CODE_C99);
    BY_UNIT_ID.put(4405555, CODE_C93);
    BY_UNIT_ID.put(4731959, CODE_H47);
    BY_UNIT_ID.put(4732724, CODE_H74);
    BY_UNIT_ID.put(4535347, CODE_E43);
    BY_UNIT_ID.put(5255991, CODE_P37);
    BY_UNIT_ID.put(5255992, CODE_P38);
    BY_UNIT_ID.put(5255993, CODE_P39);
    BY_UNIT_ID.put(5256240, CODE_P40);
    BY_UNIT_ID.put(4470071, CODE_D57);
    BY_UNIT_ID.put(4470072, CODE_D58);
    BY_UNIT_ID.put(4470070, CODE_D56);
    BY_UNIT_ID.put(4469048, CODE_D18);
    BY_UNIT_ID.put(4408396, CODE_CDL);
    BY_UNIT_ID.put(5255987, CODE_P33);
    BY_UNIT_ID.put(5255988, CODE_P34);
    BY_UNIT_ID.put(5255989, CODE_P35);
    BY_UNIT_ID.put(5255990, CODE_P36);
    BY_UNIT_ID.put(5002573, CODE_LUM);
    BY_UNIT_ID.put(4339250, CODE_B62);
    BY_UNIT_ID.put(4339001, CODE_B59);
    BY_UNIT_ID.put(4272692, CODE_A24);
    BY_UNIT_ID.put(5255736, CODE_P28);
    BY_UNIT_ID.put(5255737, CODE_P29);
    BY_UNIT_ID.put(5255984, CODE_P30);
    BY_UNIT_ID.put(5255985, CODE_P31);
    BY_UNIT_ID.put(5255986, CODE_P32);
    BY_UNIT_ID.put(4339248, CODE_B60);
    BY_UNIT_ID.put(5002584, CODE_LUX);
    BY_UNIT_ID.put(4934744, CODE_KLX);
    BY_UNIT_ID.put(5255733, CODE_P25);
    BY_UNIT_ID.put(5255734, CODE_P26);
    BY_UNIT_ID.put(5255735, CODE_P27);
    BY_UNIT_ID.put(4339252, CODE_B64);
    BY_UNIT_ID.put(4339251, CODE_B63);
    BY_UNIT_ID.put(4339249, CODE_B61);
    BY_UNIT_ID.put(4469298, CODE_D22);
    BY_UNIT_ID.put(17465, CODE_D9);
    BY_UNIT_ID.put(4273712, CODE_A60);
    BY_UNIT_ID.put(4404018, CODE_C32);
    BY_UNIT_ID.put(4470837, CODE_D85);
    BY_UNIT_ID.put(4405046, CODE_C76);
    BY_UNIT_ID.put(4273716, CODE_A64);
    BY_UNIT_ID.put(4404791, CODE_C67);
    BY_UNIT_ID.put(4273456, CODE_A50);
    BY_UNIT_ID.put(4404790, CODE_C66);
    BY_UNIT_ID.put(4273458, CODE_A52);
    BY_UNIT_ID.put(5059378, CODE_M32);
    BY_UNIT_ID.put(4404536, CODE_C58);
    BY_UNIT_ID.put(4273457, CODE_A51);
    BY_UNIT_ID.put(5256243, CODE_P43);
    BY_UNIT_ID.put(4732209, CODE_H51);
    BY_UNIT_ID.put(4732210, CODE_H52);
    BY_UNIT_ID.put(5256242, CODE_P42);
    BY_UNIT_ID.put(5256241, CODE_P41);
    BY_UNIT_ID.put(4404020, CODE_C34);
    BY_UNIT_ID.put(4338741, CODE_B45);
    BY_UNIT_ID.put(4403512, CODE_C18);
    BY_UNIT_ID.put(17992, CODE_FH);
    BY_UNIT_ID.put(5256244, CODE_P44);
    BY_UNIT_ID.put(4405557, CODE_C95);
    BY_UNIT_ID.put(4470580, CODE_D74);
    BY_UNIT_ID.put(4274484, CODE_A94);
    BY_UNIT_ID.put(4273200, CODE_A40);
    BY_UNIT_ID.put(4272951, CODE_A37);
    BY_UNIT_ID.put(4272950, CODE_A36);
    BY_UNIT_ID.put(4339000, CODE_B58);
    BY_UNIT_ID.put(4337973, CODE_B15);
    BY_UNIT_ID.put(4338740, CODE_B44);
    BY_UNIT_ID.put(4337974, CODE_B16);
    BY_UNIT_ID.put(4405302, CODE_C86);
    BY_UNIT_ID.put(4732208, CODE_H50);
    BY_UNIT_ID.put(4993584, CODE_L20);
    BY_UNIT_ID.put(4928048, CODE_K20);
    BY_UNIT_ID.put(4928569, CODE_K49);
    BY_UNIT_ID.put(4929075, CODE_K63);
    BY_UNIT_ID.put(5058864, CODE_M10);
    BY_UNIT_ID.put(4404022, CODE_C36);
    BY_UNIT_ID.put(4404024, CODE_C38);
    BY_UNIT_ID.put(4404021, CODE_C35);
    BY_UNIT_ID.put(4338742, CODE_B46);
    BY_UNIT_ID.put(4536629, CODE_E95);
    BY_UNIT_ID.put(5059379, CODE_M33);
    BY_UNIT_ID.put(5256497, CODE_P51);
    BY_UNIT_ID.put(5256498, CODE_P52);
    BY_UNIT_ID.put(4928825, CODE_K59);
    BY_UNIT_ID.put(4929072, CODE_K60);
    BY_UNIT_ID.put(4929843, CODE_K93);
    BY_UNIT_ID.put(4993588, CODE_L24);
    BY_UNIT_ID.put(4993589, CODE_L25);
    BY_UNIT_ID.put(4993590, CODE_L26);
    BY_UNIT_ID.put(4993591, CODE_L27);
    BY_UNIT_ID.put(4993592, CODE_L28);
    BY_UNIT_ID.put(4993593, CODE_L29);
    BY_UNIT_ID.put(4403513, CODE_C19);
    BY_UNIT_ID.put(4471091, CODE_D93);
    BY_UNIT_ID.put(4470839, CODE_D87);
    BY_UNIT_ID.put(4732472, CODE_H68);
    BY_UNIT_ID.put(5256247, CODE_P47);
    BY_UNIT_ID.put(5256248, CODE_P48);
    BY_UNIT_ID.put(4931924, CODE_KAT);
    BY_UNIT_ID.put(4536628, CODE_E94);
    BY_UNIT_ID.put(5256245, CODE_P45);
    BY_UNIT_ID.put(5256246, CODE_P46);
    BY_UNIT_ID.put(4469811, CODE_D43);
    BY_UNIT_ID.put(4272695, CODE_A27);
    BY_UNIT_ID.put(4272946, CODE_A32);
    BY_UNIT_ID.put(4469042, CODE_D12);
    BY_UNIT_ID.put(4928824, CODE_K58);
    BY_UNIT_ID.put(4929073, CODE_K61);
    BY_UNIT_ID.put(4993587, CODE_L23);
    BY_UNIT_ID.put(4993840, CODE_L30);
    BY_UNIT_ID.put(4405298, CODE_C82);
    BY_UNIT_ID.put(4405299, CODE_C83);
    BY_UNIT_ID.put(5256249, CODE_P49);
    BY_UNIT_ID.put(5256496, CODE_P50);
    BY_UNIT_ID.put(5321520, CODE_Q30);
    BY_UNIT_ID.put(4337976, CODE_B18);
    BY_UNIT_ID.put(4272432, CODE_A10);
    BY_UNIT_ID.put(4412754, CODE_CUR);
    BY_UNIT_ID.put(5063509, CODE_MCU);
    BY_UNIT_ID.put(19765, CODE_M5);
    BY_UNIT_ID.put(12882, CODE_2R);
    BY_UNIT_ID.put(4346188, CODE_BQL);
    BY_UNIT_ID.put(4670033, CODE_GBQ);
    BY_UNIT_ID.put(12881, CODE_2Q);
    BY_UNIT_ID.put(13390, CODE_4N);
    BY_UNIT_ID.put(4730936, CODE_H08);
    BY_UNIT_ID.put(4273202, CODE_A42);
    BY_UNIT_ID.put(4272440, CODE_A18);
    BY_UNIT_ID.put(4339255, CODE_B67);
    BY_UNIT_ID.put(4338229, CODE_B25);
    BY_UNIT_ID.put(4272441, CODE_A19);
    BY_UNIT_ID.put(4272436, CODE_A14);
    BY_UNIT_ID.put(4469300, CODE_D24);
    BY_UNIT_ID.put(4272439, CODE_A17);
    BY_UNIT_ID.put(4469296, CODE_D20);
    BY_UNIT_ID.put(4272437, CODE_A15);
    BY_UNIT_ID.put(4469046, CODE_D16);
    BY_UNIT_ID.put(4469301, CODE_D25);
    BY_UNIT_ID.put(4272438, CODE_A16);
    BY_UNIT_ID.put(4469047, CODE_D17);
    BY_UNIT_ID.put(4339761, CODE_B81);
    BY_UNIT_ID.put(4273717, CODE_A65);
    BY_UNIT_ID.put(4469297, CODE_D21);
    BY_UNIT_ID.put(4337970, CODE_B12);
    BY_UNIT_ID.put(4273460, CODE_A54);
    BY_UNIT_ID.put(4273464, CODE_A58);
    BY_UNIT_ID.put(4470579, CODE_D73);
    BY_UNIT_ID.put(4273461, CODE_A55);
    BY_UNIT_ID.put(4273718, CODE_A66);
    BY_UNIT_ID.put(4338224, CODE_B20);
    BY_UNIT_ID.put(4273462, CODE_A56);
    BY_UNIT_ID.put(4273719, CODE_A67);
    BY_UNIT_ID.put(4469302, CODE_D26);
    BY_UNIT_ID.put(4732216, CODE_H58);
    BY_UNIT_ID.put(4405303, CODE_C87);
    BY_UNIT_ID.put(4274485, CODE_A95);
    BY_UNIT_ID.put(4403507, CODE_C13);
    BY_UNIT_ID.put(4405296, CODE_C80);
    BY_UNIT_ID.put(4273713, CODE_A61);
    BY_UNIT_ID.put(4469043, CODE_D13);
    BY_UNIT_ID.put(4403768, CODE_C28);
    BY_UNIT_ID.put(4471089, CODE_D91);
    BY_UNIT_ID.put(4993841, CODE_L31);
    BY_UNIT_ID.put(4274486, CODE_A96);
    BY_UNIT_ID.put(4273714, CODE_A62);
    BY_UNIT_ID.put(4410183, CODE_CKG);
    BY_UNIT_ID.put(17208, CODE_C8);
    BY_UNIT_ID.put(12867, CODE_2C);
    BY_UNIT_ID.put(12889, CODE_2Y);
    BY_UNIT_ID.put(4863283, CODE_J53);
    BY_UNIT_ID.put(19282, CODE_KR);
    BY_UNIT_ID.put(4272945, CODE_A31);
    BY_UNIT_ID.put(17462, CODE_D6);
    BY_UNIT_ID.put(5256500, CODE_P54);
    BY_UNIT_ID.put(5256501, CODE_P55);
    BY_UNIT_ID.put(5256502, CODE_P56);
    BY_UNIT_ID.put(5256503, CODE_P57);
    BY_UNIT_ID.put(5256504, CODE_P58);
    BY_UNIT_ID.put(5256505, CODE_P59);
    BY_UNIT_ID.put(5256752, CODE_P60);
    BY_UNIT_ID.put(5256753, CODE_P61);
    BY_UNIT_ID.put(5256754, CODE_P62);
    BY_UNIT_ID.put(5256755, CODE_P63);
    BY_UNIT_ID.put(5256756, CODE_P64);
    BY_UNIT_ID.put(5256757, CODE_P65);
    BY_UNIT_ID.put(5256758, CODE_P66);
    BY_UNIT_ID.put(5256759, CODE_P67);
    BY_UNIT_ID.put(5256760, CODE_P68);
    BY_UNIT_ID.put(5256761, CODE_P69);
    BY_UNIT_ID.put(5257008, CODE_P70);
    BY_UNIT_ID.put(5257009, CODE_P71);
    BY_UNIT_ID.put(5257010, CODE_P72);
    BY_UNIT_ID.put(5257011, CODE_P73);
    BY_UNIT_ID.put(5257012, CODE_P74);
    BY_UNIT_ID.put(5257013, CODE_P75);
    BY_UNIT_ID.put(5257014, CODE_P76);
    BY_UNIT_ID.put(5257015, CODE_P77);
    BY_UNIT_ID.put(5257016, CODE_P78);
    BY_UNIT_ID.put(5256499, CODE_P53);
    BY_UNIT_ID.put(4405301, CODE_C85);
    BY_UNIT_ID.put(4471092, CODE_D94);
    BY_UNIT_ID.put(4405552, CODE_C90);
    BY_UNIT_ID.put(4405304, CODE_C88);
    BY_UNIT_ID.put(4272952, CODE_A38);
    BY_UNIT_ID.put(4469816, CODE_D48);
    BY_UNIT_ID.put(4469817, CODE_D49);
    BY_UNIT_ID.put(16694, CODE_A6);
    BY_UNIT_ID.put(13107, CODE_33);
    BY_UNIT_ID.put(5257017, CODE_P79);
    BY_UNIT_ID.put(13108, CODE_34);
    BY_UNIT_ID.put(4731954, CODE_H42);
    BY_UNIT_ID.put(4732473, CODE_H69);
    BY_UNIT_ID.put(5257264, CODE_P80);
    BY_UNIT_ID.put(5257265, CODE_P81);
    BY_UNIT_ID.put(5257266, CODE_P82);
    BY_UNIT_ID.put(5257267, CODE_P83);
    BY_UNIT_ID.put(5257268, CODE_P84);
    BY_UNIT_ID.put(5257269, CODE_P85);
    BY_UNIT_ID.put(5257270, CODE_P86);
    BY_UNIT_ID.put(13109, CODE_35);
    BY_UNIT_ID.put(5257271, CODE_P87);
    BY_UNIT_ID.put(5197901, CODE_OPM);
    BY_UNIT_ID.put(4935245, CODE_KNM);
    BY_UNIT_ID.put(5321525, CODE_Q35);
    BY_UNIT_ID.put(13112, CODE_38);
    BY_UNIT_ID.put(13625, CODE_59);
    BY_UNIT_ID.put(13873, CODE_61);
    BY_UNIT_ID.put(13878, CODE_66);
    BY_UNIT_ID.put(14134, CODE_76);
    BY_UNIT_ID.put(14136, CODE_78);
    BY_UNIT_ID.put(12871, CODE_2G);
    BY_UNIT_ID.put(12872, CODE_2H);
    BY_UNIT_ID.put(12880, CODE_2P);
    BY_UNIT_ID.put(13388, CODE_4L);
    BY_UNIT_ID.put(4273203, CODE_A43);
    BY_UNIT_ID.put(4273207, CODE_A47);
    BY_UNIT_ID.put(4273209, CODE_A49);
    BY_UNIT_ID.put(4274489, CODE_A99);
    BY_UNIT_ID.put(16706, CODE_AB);
    BY_UNIT_ID.put(16708, CODE_AD);
    BY_UNIT_ID.put(16945, CODE_B1);
    BY_UNIT_ID.put(4337968, CODE_B10);
    BY_UNIT_ID.put(4338480, CODE_B30);
    BY_UNIT_ID.put(4339253, CODE_B65);
    BY_UNIT_ID.put(4339256, CODE_B68);
    BY_UNIT_ID.put(4339760, CODE_B80);
    BY_UNIT_ID.put(4343380, CODE_BFT);
    BY_UNIT_ID.put(4345933, CODE_BPM);
    BY_UNIT_ID.put(4403761, CODE_C21);
    BY_UNIT_ID.put(4404023, CODE_C37);
    BY_UNIT_ID.put(4405044, CODE_C74);
    BY_UNIT_ID.put(4405049, CODE_C79);
    BY_UNIT_ID.put(4468787, CODE_D03);
    BY_UNIT_ID.put(4469041, CODE_D11);
    BY_UNIT_ID.put(4469556, CODE_D34);
    BY_UNIT_ID.put(4469558, CODE_D36);
    BY_UNIT_ID.put(4470584, CODE_D78);
    BY_UNIT_ID.put(4479566, CODE_DZN);
    BY_UNIT_ID.put(4534327, CODE_E07);
    BY_UNIT_ID.put(4534328, CODE_E08);
    BY_UNIT_ID.put(4534576, CODE_E10);
    BY_UNIT_ID.put(4534582, CODE_E16);
    BY_UNIT_ID.put(4534583, CODE_E17);
    BY_UNIT_ID.put(4534832, CODE_E20);
    BY_UNIT_ID.put(4535089, CODE_E31);
    BY_UNIT_ID.put(4535090, CODE_E32);
    BY_UNIT_ID.put(4535092, CODE_E34);
    BY_UNIT_ID.put(4535093, CODE_E35);
    BY_UNIT_ID.put(4535094, CODE_E36);
    BY_UNIT_ID.put(4535097, CODE_E39);
    BY_UNIT_ID.put(4535344, CODE_E40);
    BY_UNIT_ID.put(4535348, CODE_E44);
    BY_UNIT_ID.put(4535350, CODE_E46);
    BY_UNIT_ID.put(4535351, CODE_E47);
    BY_UNIT_ID.put(4535608, CODE_E58);
    BY_UNIT_ID.put(4535609, CODE_E59);
    BY_UNIT_ID.put(4535856, CODE_E60);
    BY_UNIT_ID.put(4535857, CODE_E61);
    BY_UNIT_ID.put(4535858, CODE_E62);
    BY_UNIT_ID.put(4535859, CODE_E63);
    BY_UNIT_ID.put(4535860, CODE_E64);
    BY_UNIT_ID.put(4535861, CODE_E65);
    BY_UNIT_ID.put(4535862, CODE_E66);
    BY_UNIT_ID.put(4535863, CODE_E67);
    BY_UNIT_ID.put(4535864, CODE_E68);
    BY_UNIT_ID.put(4535865, CODE_E69);
    BY_UNIT_ID.put(4536112, CODE_E70);
    BY_UNIT_ID.put(4536113, CODE_E71);
    BY_UNIT_ID.put(4536114, CODE_E72);
    BY_UNIT_ID.put(4536115, CODE_E73);
    BY_UNIT_ID.put(4536116, CODE_E74);
    BY_UNIT_ID.put(4536117, CODE_E75);
    BY_UNIT_ID.put(4536118, CODE_E76);
    BY_UNIT_ID.put(4536119, CODE_E77);
    BY_UNIT_ID.put(4536120, CODE_E78);
    BY_UNIT_ID.put(4536121, CODE_E79);
    BY_UNIT_ID.put(4536368, CODE_E80);
    BY_UNIT_ID.put(4536369, CODE_E81);
    BY_UNIT_ID.put(4536370, CODE_E82);
    BY_UNIT_ID.put(4536371, CODE_E83);
    BY_UNIT_ID.put(4536372, CODE_E84);
    BY_UNIT_ID.put(4536373, CODE_E85);
    BY_UNIT_ID.put(4536374, CODE_E86);
    BY_UNIT_ID.put(4536375, CODE_E87);
    BY_UNIT_ID.put(4536376, CODE_E88);
    BY_UNIT_ID.put(4536377, CODE_E89);
    BY_UNIT_ID.put(4536624, CODE_E90);
    BY_UNIT_ID.put(4536625, CODE_E91);
    BY_UNIT_ID.put(4599857, CODE_F01);
    BY_UNIT_ID.put(17987, CODE_FC);
    BY_UNIT_ID.put(4606292, CODE_FIT);
    BY_UNIT_ID.put(18242, CODE_GB);
    BY_UNIT_ID.put(4671049, CODE_GFI);
    BY_UNIT_ID.put(4671809, CODE_GIA);
    BY_UNIT_ID.put(4671817, CODE_GII);
    BY_UNIT_ID.put(4674127, CODE_GRO);
    BY_UNIT_ID.put(4731445, CODE_H25);
    BY_UNIT_ID.put(4732721, CODE_H71);
    BY_UNIT_ID.put(4732722, CODE_H72);
    BY_UNIT_ID.put(4732723, CODE_H73);
    BY_UNIT_ID.put(4732727, CODE_H77);
    BY_UNIT_ID.put(4732976, CODE_H80);
    BY_UNIT_ID.put(4732978, CODE_H82);
    BY_UNIT_ID.put(4732985, CODE_H89);
    BY_UNIT_ID.put(4733232, CODE_H90);
    BY_UNIT_ID.put(4733233, CODE_H91);
    BY_UNIT_ID.put(4733234, CODE_H92);
    BY_UNIT_ID.put(4733235, CODE_H93);
    BY_UNIT_ID.put(4733236, CODE_H94);
    BY_UNIT_ID.put(4733237, CODE_H95);
    BY_UNIT_ID.put(4733238, CODE_H96);
    BY_UNIT_ID.put(4733240, CODE_H98);
    BY_UNIT_ID.put(4733241, CODE_H99);
    BY_UNIT_ID.put(4738385, CODE_HMQ);
    BY_UNIT_ID.put(4862256, CODE_J10);
    BY_UNIT_ID.put(4862258, CODE_J12);
    BY_UNIT_ID.put(4862259, CODE_J13);
    BY_UNIT_ID.put(4862260, CODE_J14);
    BY_UNIT_ID.put(4862261, CODE_J15);
    BY_UNIT_ID.put(4862262, CODE_J16);
    BY_UNIT_ID.put(4862263, CODE_J17);
    BY_UNIT_ID.put(4862264, CODE_J18);
    BY_UNIT_ID.put(4862519, CODE_J27);
    BY_UNIT_ID.put(4862769, CODE_J31);
    BY_UNIT_ID.put(4862776, CODE_J38);
    BY_UNIT_ID.put(4863284, CODE_J54);
    BY_UNIT_ID.put(19251, CODE_K3);
    BY_UNIT_ID.put(4928816, CODE_K50);
    BY_UNIT_ID.put(4932419, CODE_KCC);
    BY_UNIT_ID.put(4932695, CODE_KDW);
    BY_UNIT_ID.put(4933721, CODE_KHY);
    BY_UNIT_ID.put(4934977, CODE_KMA);
    BY_UNIT_ID.put(4935241, CODE_KNI);
    BY_UNIT_ID.put(4935752, CODE_KPH);
    BY_UNIT_ID.put(4935759, CODE_KPO);
    BY_UNIT_ID.put(4936516, CODE_KSD);
    BY_UNIT_ID.put(4936520, CODE_KSH);
    BY_UNIT_ID.put(4937042, CODE_KUR);
    BY_UNIT_ID.put(4937561, CODE_KWY);
    BY_UNIT_ID.put(4937551, CODE_KWO);
    BY_UNIT_ID.put(5058873, CODE_M19);
    BY_UNIT_ID.put(5059125, CODE_M25);
    BY_UNIT_ID.put(5059382, CODE_M36);
    BY_UNIT_ID.put(5059383, CODE_M37);
    BY_UNIT_ID.put(19769, CODE_M9);
    BY_UNIT_ID.put(5062984, CODE_MAH);
    BY_UNIT_ID.put(5130572, CODE_NIL);
    BY_UNIT_ID.put(20056, CODE_NX);
    BY_UNIT_ID.put(20529, CODE_P1);
    BY_UNIT_ID.put(5257272, CODE_P88);
    BY_UNIT_ID.put(5257273, CODE_P89);
    BY_UNIT_ID.put(5257520, CODE_P90);
    BY_UNIT_ID.put(5257521, CODE_P91);
    BY_UNIT_ID.put(5257522, CODE_P92);
    BY_UNIT_ID.put(5257523, CODE_P93);
    BY_UNIT_ID.put(5257524, CODE_P94);
    BY_UNIT_ID.put(5257525, CODE_P95);
    BY_UNIT_ID.put(5257526, CODE_P96);
    BY_UNIT_ID.put(5257527, CODE_P97);
    BY_UNIT_ID.put(5257528, CODE_P98);
    BY_UNIT_ID.put(5257529, CODE_P99);
    BY_UNIT_ID.put(5262401, CODE_PLA);
    BY_UNIT_ID.put(20561, CODE_PQ);
    BY_UNIT_ID.put(5264462, CODE_PTN);
    BY_UNIT_ID.put(5321008, CODE_Q10);
    BY_UNIT_ID.put(5321009, CODE_Q11);
    BY_UNIT_ID.put(5321010, CODE_Q12);
    BY_UNIT_ID.put(5321011, CODE_Q13);
    BY_UNIT_ID.put(5321012, CODE_Q14);
    BY_UNIT_ID.put(5321013, CODE_Q15);
    BY_UNIT_ID.put(5321014, CODE_Q16);
    BY_UNIT_ID.put(5321015, CODE_Q17);
    BY_UNIT_ID.put(5321016, CODE_Q18);
    BY_UNIT_ID.put(5321017, CODE_Q19);
    BY_UNIT_ID.put(5321264, CODE_Q20);
    BY_UNIT_ID.put(5321265, CODE_Q21);
    BY_UNIT_ID.put(5321266, CODE_Q22);
    BY_UNIT_ID.put(5321267, CODE_Q23);
    BY_UNIT_ID.put(5321268, CODE_Q24);
    BY_UNIT_ID.put(5321269, CODE_Q25);
    BY_UNIT_ID.put(5321270, CODE_Q26);
    BY_UNIT_ID.put(5321271, CODE_Q27);
    BY_UNIT_ID.put(5321272, CODE_Q28);
    BY_UNIT_ID.put(5321526, CODE_Q36);
    BY_UNIT_ID.put(20818, CODE_QR);
    BY_UNIT_ID.put(5330002, CODE_QTR);
    BY_UNIT_ID.put(5521742, CODE_TAN);
    BY_UNIT_ID.put(5524301, CODE_TKM);
    BY_UNIT_ID.put(5525577, CODE_TPI);
    BY_UNIT_ID.put(5525828, CODE_TQD);
    BY_UNIT_ID.put(22081, CODE_VA);
    BY_UNIT_ID.put(22337, CODE_WA);
    BY_UNIT_ID.put(5722948, CODE_WSD);
    BY_UNIT_ID.put(5067351, CODE_MRW);
    BY_UNIT_ID.put(5065559, CODE_MKW);
    BY_UNIT_ID.put(5067095, CODE_MQW);
    BY_UNIT_ID.put(4740933, CODE_HWE);
    BY_UNIT_ID.put(5067332, CODE_MRD);
    BY_UNIT_ID.put(5065540, CODE_MKD);
    BY_UNIT_ID.put(5067076, CODE_MQD);
    BY_UNIT_ID.put(4735300, CODE_HAD);
    BY_UNIT_ID.put(5067341, CODE_MRM);
    BY_UNIT_ID.put(5065549, CODE_MKM);
    BY_UNIT_ID.put(5067085, CODE_MQM);
    BY_UNIT_ID.put(4738383, CODE_HMO);
    BY_UNIT_ID.put(4473431, CODE_DBW);
    BY_UNIT_ID.put(4473421, CODE_DBM);
    BY_UNIT_ID.put(4607573, CODE_FNU);
    BY_UNIT_ID.put(5133397, CODE_NTU);
  }
}
