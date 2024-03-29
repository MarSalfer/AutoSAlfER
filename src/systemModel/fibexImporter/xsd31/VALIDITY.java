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
 * <p>Java class for VALIDITY.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VALIDITY">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="VALID"/>
 *     &lt;enumeration value="NOT-VALID"/>
 *     &lt;enumeration value="NOT-AVAILABLE"/>
 *     &lt;enumeration value="NOT-DEFINED"/>
 *     &lt;enumeration value="ERROR"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VALIDITY", namespace = "http://www.asam.net/xml")
@XmlEnum
public enum VALIDITY {

    VALID("VALID"),
    @XmlEnumValue("NOT-VALID")
    NOT_VALID("NOT-VALID"),
    @XmlEnumValue("NOT-AVAILABLE")
    NOT_AVAILABLE("NOT-AVAILABLE"),
    @XmlEnumValue("NOT-DEFINED")
    NOT_DEFINED("NOT-DEFINED"),
    ERROR("ERROR"),
    OTHER("OTHER");
    private final String value;

    VALIDITY(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VALIDITY fromValue(String v) {
        for (VALIDITY c: VALIDITY.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
