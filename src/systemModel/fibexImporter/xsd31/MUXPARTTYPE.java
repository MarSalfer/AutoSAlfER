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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Content model for the entities STATIC-PART and DYNAMIC-PART
 * 
 * <p>Java class for MUX-PART-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MUX-PART-TYPE">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.asam.net/xml/fbx}NAMED-ELEMENT-TYPE">
 *       &lt;sequence>
 *         &lt;element name="SEGMENT-POSITIONS" type="{http://www.asam.net/xml/fbx}SEGMENT-POSITIONS"/>
 *         &lt;element name="MANUFACTURER-EXTENSION" type="{http://www.asam.net/xml/fbx}MANUFACTURER-MUX-PART-EXTENSION" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MUX-PART-TYPE", propOrder = {
    "segmentpositions",
    "manufacturerextension"
})
@XmlSeeAlso({
    DYNAMICPART.class,
    STATICPART.class
})
public class MUXPARTTYPE
    extends NAMEDELEMENTTYPE
{

    @XmlElement(name = "SEGMENT-POSITIONS", required = true)
    protected SEGMENTPOSITIONS segmentpositions;
    @XmlElement(name = "MANUFACTURER-EXTENSION")
    protected MANUFACTURERMUXPARTEXTENSION manufacturerextension;

    /**
     * Gets the value of the segmentpositions property.
     * 
     * @return
     *     possible object is
     *     {@link SEGMENTPOSITIONS }
     *     
     */
    public SEGMENTPOSITIONS getSEGMENTPOSITIONS() {
        return segmentpositions;
    }

    /**
     * Sets the value of the segmentpositions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SEGMENTPOSITIONS }
     *     
     */
    public void setSEGMENTPOSITIONS(SEGMENTPOSITIONS value) {
        this.segmentpositions = value;
    }

    /**
     * Gets the value of the manufacturerextension property.
     * 
     * @return
     *     possible object is
     *     {@link MANUFACTURERMUXPARTEXTENSION }
     *     
     */
    public MANUFACTURERMUXPARTEXTENSION getMANUFACTUREREXTENSION() {
        return manufacturerextension;
    }

    /**
     * Sets the value of the manufacturerextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link MANUFACTURERMUXPARTEXTENSION }
     *     
     */
    public void setMANUFACTUREREXTENSION(MANUFACTURERMUXPARTEXTENSION value) {
        this.manufacturerextension = value;
    }

}
