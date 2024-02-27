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


/**
 * Switched area of the multiplexer. The content changes dependent on the switch
 * 
 * <p>Java class for DYNAMIC-PART complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DYNAMIC-PART">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.asam.net/xml/fbx}MUX-PART-TYPE">
 *       &lt;sequence>
 *         &lt;element name="SWITCHED-PDU-INSTANCES" type="{http://www.asam.net/xml/fbx}SWITCHED-PDU-INSTANCES"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DYNAMIC-PART", propOrder = {
    "switchedpduinstances"
})
public class DYNAMICPART
    extends MUXPARTTYPE
{

    @XmlElement(name = "SWITCHED-PDU-INSTANCES", required = true)
    protected SWITCHEDPDUINSTANCES switchedpduinstances;

    /**
     * Gets the value of the switchedpduinstances property.
     * 
     * @return
     *     possible object is
     *     {@link SWITCHEDPDUINSTANCES }
     *     
     */
    public SWITCHEDPDUINSTANCES getSWITCHEDPDUINSTANCES() {
        return switchedpduinstances;
    }

    /**
     * Sets the value of the switchedpduinstances property.
     * 
     * @param value
     *     allowed object is
     *     {@link SWITCHEDPDUINSTANCES }
     *     
     */
    public void setSWITCHEDPDUINSTANCES(SWITCHEDPDUINSTANCES value) {
        this.switchedpduinstances = value;
    }

}
