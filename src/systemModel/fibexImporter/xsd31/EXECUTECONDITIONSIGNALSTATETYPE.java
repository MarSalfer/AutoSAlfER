//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.21 at 02:41:15 PM MESZ 
//


package systemModel.fibexImporter.xsd31;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EXECUTE-CONDITION-SIGNAL-STATE-TYPE.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EXECUTE-CONDITION-SIGNAL-STATE-TYPE">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="VALUE_CHANGED"/>
 *     &lt;enumeration value="VALUE_NOT_CHANGED"/>
 *     &lt;enumeration value="VALUE_DEFAULT"/>
 *     &lt;enumeration value="VALUE_NOT_DEFAULT"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EXECUTE-CONDITION-SIGNAL-STATE-TYPE")
@XmlEnum
public enum EXECUTECONDITIONSIGNALSTATETYPE {

    VALUE_CHANGED,
    VALUE_NOT_CHANGED,
    VALUE_DEFAULT,
    VALUE_NOT_DEFAULT,
    OTHER;

    public String value() {
        return name();
    }

    public static EXECUTECONDITIONSIGNALSTATETYPE fromValue(String v) {
        return valueOf(v);
    }

}
