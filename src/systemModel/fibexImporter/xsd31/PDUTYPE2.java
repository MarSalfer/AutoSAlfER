//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.21 at 02:41:15 PM MESZ 
//


package systemModel.fibexImporter.xsd31;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PDUTYPE2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PDUTYPE2">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="APPLICATION"/>
 *     &lt;enumeration value="BAP"/>
 *     &lt;enumeration value="DIAG-REQUEST"/>
 *     &lt;enumeration value="DIAG-RESPONSE"/>
 *     &lt;enumeration value="DIAG-STATE"/>
 *     &lt;enumeration value="NM"/>
 *     &lt;enumeration value="OTHER"/>
 *     &lt;enumeration value="SERVICE"/>
 *     &lt;enumeration value="TPL"/>
 *     &lt;enumeration value="XCP_PRE_CONFIGURED"/>
 *     &lt;enumeration value="XCP_RUNTIME_CONFIGURED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PDUTYPE2")
@XmlEnum
public enum PDUTYPE2 {

    APPLICATION("APPLICATION"),
    BAP("BAP"),
    @XmlEnumValue("DIAG-REQUEST")
    DIAG_REQUEST("DIAG-REQUEST"),
    @XmlEnumValue("DIAG-RESPONSE")
    DIAG_RESPONSE("DIAG-RESPONSE"),
    @XmlEnumValue("DIAG-STATE")
    DIAG_STATE("DIAG-STATE"),
    NM("NM"),
    OTHER("OTHER"),
    SERVICE("SERVICE"),
    TPL("TPL"),
    XCP_PRE_CONFIGURED("XCP_PRE_CONFIGURED"),
    XCP_RUNTIME_CONFIGURED("XCP_RUNTIME_CONFIGURED");
    private final String value;

    PDUTYPE2(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PDUTYPE2 fromValue(String v) {
        for (PDUTYPE2 c: PDUTYPE2 .values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
