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
 * Selected signal
 * 
 * <p>Java class for INCLUDED-SIGNAL-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="INCLUDED-SIGNAL-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.asam.net/xml/fbx}SIGNAL-INSTANCE-REF"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "INCLUDED-SIGNAL-TYPE", propOrder = {
    "signalinstanceref"
})
public class INCLUDEDSIGNALTYPE {

    @XmlElement(name = "SIGNAL-INSTANCE-REF", required = true)
    protected SIGNALINSTANCEREF signalinstanceref;

    /**
     * Gets the value of the signalinstanceref property.
     * 
     * @return
     *     possible object is
     *     {@link SIGNALINSTANCEREF }
     *     
     */
    public SIGNALINSTANCEREF getSIGNALINSTANCEREF() {
        return signalinstanceref;
    }

    /**
     * Sets the value of the signalinstanceref property.
     * 
     * @param value
     *     allowed object is
     *     {@link SIGNALINSTANCEREF }
     *     
     */
    public void setSIGNALINSTANCEREF(SIGNALINSTANCEREF value) {
        this.signalinstanceref = value;
    }

}
