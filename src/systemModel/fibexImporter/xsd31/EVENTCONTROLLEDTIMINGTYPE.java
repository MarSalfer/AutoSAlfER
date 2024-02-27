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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Content model for the entity TIMING in the event controlled peculiarity.
 * 
 * <p>Java class for EVENT-CONTROLLED-TIMING-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EVENT-CONTROLLED-TIMING-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DEBOUNCE-TIME-RANGE" type="{http://www.asam.net/xml/fbx}TIME-RANGE-TYPE"/>
 *         &lt;element name="ACTIVE-CONDITION" type="{http://www.asam.net/xml/fbx}ACTIVE-CONDITION-TYPE" minOccurs="0"/>
 *         &lt;element name="SEND-CONDITION" type="{http://www.asam.net/xml/fbx}EXECUTE-CONDITION-TYPE" minOccurs="0"/>
 *         &lt;element name="FINAL-REPETITIONS" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" minOccurs="0"/>
 *         &lt;element ref="{http://www.asam.net/xml}DESC" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MANUFACTURER-EXTENSION" type="{http://www.asam.net/xml/fbx}MANUFACTURER-EVENT-CONTROLLED-TIMING-EXTENSION" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EVENT-CONTROLLED-TIMING-TYPE", propOrder = {
    "debouncetimerange",
    "activecondition",
    "sendcondition",
    "finalrepetitions",
    "desc",
    "manufacturerextension"
})
public class EVENTCONTROLLEDTIMINGTYPE {

    @XmlElement(name = "DEBOUNCE-TIME-RANGE", required = true)
    protected TIMERANGETYPE debouncetimerange;
    @XmlElement(name = "ACTIVE-CONDITION")
    protected ACTIVECONDITIONTYPE activecondition;
    @XmlElement(name = "SEND-CONDITION")
    protected EXECUTECONDITIONTYPE sendcondition;
    @XmlElement(name = "FINAL-REPETITIONS")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer finalrepetitions;
    @XmlElement(name = "DESC", namespace = "http://www.asam.net/xml")
    protected List<DESC> desc;
    @XmlElement(name = "MANUFACTURER-EXTENSION")
    protected MANUFACTUREREVENTCONTROLLEDTIMINGEXTENSION manufacturerextension;

    /**
     * Gets the value of the debouncetimerange property.
     * 
     * @return
     *     possible object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public TIMERANGETYPE getDEBOUNCETIMERANGE() {
        return debouncetimerange;
    }

    /**
     * Sets the value of the debouncetimerange property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public void setDEBOUNCETIMERANGE(TIMERANGETYPE value) {
        this.debouncetimerange = value;
    }

    /**
     * Gets the value of the activecondition property.
     * 
     * @return
     *     possible object is
     *     {@link ACTIVECONDITIONTYPE }
     *     
     */
    public ACTIVECONDITIONTYPE getACTIVECONDITION() {
        return activecondition;
    }

    /**
     * Sets the value of the activecondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ACTIVECONDITIONTYPE }
     *     
     */
    public void setACTIVECONDITION(ACTIVECONDITIONTYPE value) {
        this.activecondition = value;
    }

    /**
     * Gets the value of the sendcondition property.
     * 
     * @return
     *     possible object is
     *     {@link EXECUTECONDITIONTYPE }
     *     
     */
    public EXECUTECONDITIONTYPE getSENDCONDITION() {
        return sendcondition;
    }

    /**
     * Sets the value of the sendcondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link EXECUTECONDITIONTYPE }
     *     
     */
    public void setSENDCONDITION(EXECUTECONDITIONTYPE value) {
        this.sendcondition = value;
    }

    /**
     * Gets the value of the finalrepetitions property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFINALREPETITIONS() {
        return finalrepetitions;
    }

    /**
     * Sets the value of the finalrepetitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFINALREPETITIONS(Integer value) {
        this.finalrepetitions = value;
    }

    /**
     * Gets the value of the desc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the desc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDESC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DESC }
     * 
     * 
     */
    public List<DESC> getDESC() {
        if (desc == null) {
            desc = new ArrayList<DESC>();
        }
        return this.desc;
    }

    /**
     * Gets the value of the manufacturerextension property.
     * 
     * @return
     *     possible object is
     *     {@link MANUFACTUREREVENTCONTROLLEDTIMINGEXTENSION }
     *     
     */
    public MANUFACTUREREVENTCONTROLLEDTIMINGEXTENSION getMANUFACTUREREXTENSION() {
        return manufacturerextension;
    }

    /**
     * Sets the value of the manufacturerextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link MANUFACTUREREVENTCONTROLLEDTIMINGEXTENSION }
     *     
     */
    public void setMANUFACTUREREXTENSION(MANUFACTUREREVENTCONTROLLEDTIMINGEXTENSION value) {
        this.manufacturerextension = value;
    }

}