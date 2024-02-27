//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.21 at 02:41:15 PM MESZ 
//


package systemModel.fibexImporter.xsd31;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * Reserved space providing arbitrary schema extendibility without affecting the FIBEX standard.
 * 
 * <p>Java class for MANUFACTURER-ABSTRACT-EXTENSION complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MANUFACTURER-ABSTRACT-EXTENSION">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;any processContents='lax' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MANUFACTURER-ABSTRACT-EXTENSION", propOrder = {
    "any"
})
@XmlSeeAlso({
    MANUFACTURERIDENTIFIEREXTENSION.class,
    MANUFACTURERCONNECTOREXTENSION.class,
    MANUFACTURERSUBFRAMEEXTENSION.class,
    MANUFACTURERPDUEXTENSION.class,
    MANUFACTURERCONNECTORMAPPINGEXTENSION.class,
    MANUFACTURERFRAMEMAPPINGEXTENSION.class,
    MANUFACTURERGWDIAGNOSISDATAEXTENSION.class,
    MANUFACTURERSWITCHEXTENSION.class,
    MANUFACTURERCONTROLLEREXTENSION.class,
    MANUFACTURERFRAMEEXTENSION.class,
    MANUFACTURERCYCLICTIMINGEXTENSION.class,
    MANUFACTURERCLUSTEREXTENSION.class,
    MANUFACTURERSIGNALEXTENSION.class,
    MANUFACTURERMUXPARTEXTENSION.class,
    MANUFACTURERFUNCTIONREQEXTENSION.class,
    MANUFACTUREROPTIMISEDMAPPINGEXTENSION.class,
    MANUFACTURERSIGNALMAPPINGEXTENSION.class,
    MANUFACTURERFRAMETRIGGERINGEXTENSION.class,
    MANUFACTURERSIGNALGROUPEXTENSION.class,
    MANUFACTURERPDUMAPPINGEXTENSION.class,
    MANUFACTURERSIGNALINSTANCEEXTENSION.class,
    MANUFACTURERPORTREQEXTENSION.class,
    MANUFACTURERECUEXTENSION.class,
    MANUFACTURERCODINGEXTENSION.class,
    MANUFACTURERPDUINSTANCEEXTENSION.class,
    MANUFACTURERGATEWAYEXTENSION.class,
    MANUFACTURERPROJECTEXTENSION.class,
    MANUFACTURERCOMPOSITEEXTENSION.class,
    MANUFACTURERFUNCTIONEXTENSION.class,
    MANUFACTUREREVENTCONTROLLEDTIMINGEXTENSION.class,
    MANUFACTURERCHANNELEXTENSION.class,
    MANUFACTURERRELATIVELYSCHEDULEDTIMINGEXTENSION.class,
    MANUFACTURERPORTEXTENSION.class,
    MANUFACTURERPDUTRIGGERINGEXTENSION.class,
    MANUFACTURERABSOLUTELYSCHEDULEDTIMINGEXTENSION.class,
    MANUFACTURERREQUESTCONTROLLEDTIMINGEXTENSION.class
})
public abstract class MANUFACTURERABSTRACTEXTENSION {

    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link Element }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
