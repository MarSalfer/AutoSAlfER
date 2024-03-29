//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.21 at 02:41:15 PM MESZ 
//


package systemModel.fibexImporter.xsd31;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;


/**
 * Maximum allowable deviation
 * 
 * <p>Java class for TOLERANCE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TOLERANCE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MIN-VALUE" type="{http://www.w3.org/2001/XMLSchema}duration"/>
 *         &lt;element name="MAX-VALUE" type="{http://www.w3.org/2001/XMLSchema}duration"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TOLERANCE", propOrder = {
    "minvalue",
    "maxvalue"
})
public class TOLERANCE {

    @XmlElement(name = "MIN-VALUE", required = true)
    protected Duration minvalue;
    @XmlElement(name = "MAX-VALUE", required = true)
    protected Duration maxvalue;

    /**
     * Gets the value of the minvalue property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getMINVALUE() {
        return minvalue;
    }

    /**
     * Sets the value of the minvalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setMINVALUE(Duration value) {
        this.minvalue = value;
    }

    /**
     * Gets the value of the maxvalue property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getMAXVALUE() {
        return maxvalue;
    }

    /**
     * Sets the value of the maxvalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setMAXVALUE(Duration value) {
        this.maxvalue = value;
    }

}
