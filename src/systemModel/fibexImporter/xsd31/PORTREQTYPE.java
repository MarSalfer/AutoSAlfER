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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Content model for the entity PORT-REQUIREMENT
 * 
 * <p>Java class for PORT-REQ-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PORT-REQ-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PORT-REFS" type="{http://www.asam.net/xml/fbx}PORT-REFS"/>
 *         &lt;element name="CHANNEL-REFS" type="{http://www.asam.net/xml/fbx}CHANNEL-REFS" minOccurs="0"/>
 *         &lt;element name="CONTROLLER-REFS" type="{http://www.asam.net/xml/fbx}CONTROLLER-REFS" minOccurs="0"/>
 *         &lt;element name="MAX-AGE" type="{http://www.asam.net/xml/fbx}TIME-RANGE-TYPE" minOccurs="0"/>
 *         &lt;element name="GENERATION-TYPE" type="{http://www.asam.net/xml/fbx}GENERATION-TYPE" minOccurs="0"/>
 *         &lt;element name="CYCLE-PERIOD" type="{http://www.asam.net/xml/fbx}TIME-RANGE-TYPE" minOccurs="0"/>
 *         &lt;element name="CYCLE-OFFSET" type="{http://www.asam.net/xml/fbx}TIME-RANGE-TYPE" minOccurs="0"/>
 *         &lt;element name="REDUNDANCY-LVL" type="{http://www.w3.org/2001/XMLSchema}unsignedShort" minOccurs="0"/>
 *         &lt;element name="MANUFACTURER-EXTENSION" type="{http://www.asam.net/xml/fbx}MANUFACTURER-PORT-REQ-EXTENSION" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PORT-REQ-TYPE", propOrder = {
    "portrefs",
    "channelrefs",
    "controllerrefs",
    "maxage",
    "generationtype",
    "cycleperiod",
    "cycleoffset",
    "redundancylvl",
    "manufacturerextension"
})
public class PORTREQTYPE {

    @XmlElement(name = "PORT-REFS", required = true)
    protected PORTREFS portrefs;
    @XmlElement(name = "CHANNEL-REFS")
    protected CHANNELREFS channelrefs;
    @XmlElement(name = "CONTROLLER-REFS")
    protected CONTROLLERREFS controllerrefs;
    @XmlElement(name = "MAX-AGE")
    protected TIMERANGETYPE maxage;
    @XmlElement(name = "GENERATION-TYPE")
    protected GENERATIONTYPE generationtype;
    @XmlElement(name = "CYCLE-PERIOD")
    protected TIMERANGETYPE cycleperiod;
    @XmlElement(name = "CYCLE-OFFSET")
    protected TIMERANGETYPE cycleoffset;
    @XmlElement(name = "REDUNDANCY-LVL")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer redundancylvl;
    @XmlElement(name = "MANUFACTURER-EXTENSION")
    protected MANUFACTURERPORTREQEXTENSION manufacturerextension;

    /**
     * Gets the value of the portrefs property.
     * 
     * @return
     *     possible object is
     *     {@link PORTREFS }
     *     
     */
    public PORTREFS getPORTREFS() {
        return portrefs;
    }

    /**
     * Sets the value of the portrefs property.
     * 
     * @param value
     *     allowed object is
     *     {@link PORTREFS }
     *     
     */
    public void setPORTREFS(PORTREFS value) {
        this.portrefs = value;
    }

    /**
     * Gets the value of the channelrefs property.
     * 
     * @return
     *     possible object is
     *     {@link CHANNELREFS }
     *     
     */
    public CHANNELREFS getCHANNELREFS() {
        return channelrefs;
    }

    /**
     * Sets the value of the channelrefs property.
     * 
     * @param value
     *     allowed object is
     *     {@link CHANNELREFS }
     *     
     */
    public void setCHANNELREFS(CHANNELREFS value) {
        this.channelrefs = value;
    }

    /**
     * Gets the value of the controllerrefs property.
     * 
     * @return
     *     possible object is
     *     {@link CONTROLLERREFS }
     *     
     */
    public CONTROLLERREFS getCONTROLLERREFS() {
        return controllerrefs;
    }

    /**
     * Sets the value of the controllerrefs property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONTROLLERREFS }
     *     
     */
    public void setCONTROLLERREFS(CONTROLLERREFS value) {
        this.controllerrefs = value;
    }

    /**
     * Gets the value of the maxage property.
     * 
     * @return
     *     possible object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public TIMERANGETYPE getMAXAGE() {
        return maxage;
    }

    /**
     * Sets the value of the maxage property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public void setMAXAGE(TIMERANGETYPE value) {
        this.maxage = value;
    }

    /**
     * Gets the value of the generationtype property.
     * 
     * @return
     *     possible object is
     *     {@link GENERATIONTYPE }
     *     
     */
    public GENERATIONTYPE getGENERATIONTYPE() {
        return generationtype;
    }

    /**
     * Sets the value of the generationtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link GENERATIONTYPE }
     *     
     */
    public void setGENERATIONTYPE(GENERATIONTYPE value) {
        this.generationtype = value;
    }

    /**
     * Gets the value of the cycleperiod property.
     * 
     * @return
     *     possible object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public TIMERANGETYPE getCYCLEPERIOD() {
        return cycleperiod;
    }

    /**
     * Sets the value of the cycleperiod property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public void setCYCLEPERIOD(TIMERANGETYPE value) {
        this.cycleperiod = value;
    }

    /**
     * Gets the value of the cycleoffset property.
     * 
     * @return
     *     possible object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public TIMERANGETYPE getCYCLEOFFSET() {
        return cycleoffset;
    }

    /**
     * Sets the value of the cycleoffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIMERANGETYPE }
     *     
     */
    public void setCYCLEOFFSET(TIMERANGETYPE value) {
        this.cycleoffset = value;
    }

    /**
     * Gets the value of the redundancylvl property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getREDUNDANCYLVL() {
        return redundancylvl;
    }

    /**
     * Sets the value of the redundancylvl property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setREDUNDANCYLVL(Integer value) {
        this.redundancylvl = value;
    }

    /**
     * Gets the value of the manufacturerextension property.
     * 
     * @return
     *     possible object is
     *     {@link MANUFACTURERPORTREQEXTENSION }
     *     
     */
    public MANUFACTURERPORTREQEXTENSION getMANUFACTUREREXTENSION() {
        return manufacturerextension;
    }

    /**
     * Sets the value of the manufacturerextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link MANUFACTURERPORTREQEXTENSION }
     *     
     */
    public void setMANUFACTUREREXTENSION(MANUFACTURERPORTREQEXTENSION value) {
        this.manufacturerextension = value;
    }

}
