//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.21 at 02:41:15 PM MESZ 
//


package systemModel.fibexImporter.xsd31;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Content model for the entity SIGNAL-INSTANCE
 * 
 * <p>Java class for SIGNAL-INSTANCE-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SIGNAL-INSTANCE-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.asam.net/xml/fbx}COMMON-LAYOUT-DETAILS"/>
 *         &lt;sequence>
 *           &lt;element name="SIGNAL-REF" type="{http://www.asam.net/xml/fbx}SIGNAL-REF"/>
 *           &lt;element name="SIGNAL-UPDATE-BIT-POSITION" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="MANUFACTURER-EXTENSION" type="{http://www.asam.net/xml/fbx}MANUFACTURER-SIGNAL-INSTANCE-EXTENSION" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SIGNAL-INSTANCE-TYPE", propOrder = {
    "bitposition",
    "ishighlowbyteorder",
    "sequencenumber",
    "signalref",
    "signalupdatebitposition",
    "manufacturerextension"
})
public class SIGNALINSTANCETYPE {

    @XmlElement(name = "BIT-POSITION")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer bitposition;
    @XmlElement(name = "IS-HIGH-LOW-BYTE-ORDER")
    protected Boolean ishighlowbyteorder;
    @XmlElement(name = "SEQUENCE-NUMBER")
    @XmlSchemaType(name = "unsignedShort")
    protected Integer sequencenumber;
    @XmlElement(name = "SIGNAL-REF", required = true)
    protected SIGNALREF signalref;
    @XmlElement(name = "SIGNAL-UPDATE-BIT-POSITION")
    @XmlSchemaType(name = "unsignedInt")
    protected Long signalupdatebitposition;
    @XmlElement(name = "MANUFACTURER-EXTENSION")
    protected MANUFACTURERSIGNALINSTANCEEXTENSION manufacturerextension;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the bitposition property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBITPOSITION() {
        return bitposition;
    }

    /**
     * Sets the value of the bitposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBITPOSITION(Integer value) {
        this.bitposition = value;
    }

    /**
     * Gets the value of the ishighlowbyteorder property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isISHIGHLOWBYTEORDER() {
        return ishighlowbyteorder;
    }

    /**
     * Sets the value of the ishighlowbyteorder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setISHIGHLOWBYTEORDER(Boolean value) {
        this.ishighlowbyteorder = value;
    }

    /**
     * Gets the value of the sequencenumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSEQUENCENUMBER() {
        return sequencenumber;
    }

    /**
     * Sets the value of the sequencenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSEQUENCENUMBER(Integer value) {
        this.sequencenumber = value;
    }

    /**
     * Gets the value of the signalref property.
     * 
     * @return
     *     possible object is
     *     {@link SIGNALREF }
     *     
     */
    public SIGNALREF getSIGNALREF() {
        return signalref;
    }

    /**
     * Sets the value of the signalref property.
     * 
     * @param value
     *     allowed object is
     *     {@link SIGNALREF }
     *     
     */
    public void setSIGNALREF(SIGNALREF value) {
        this.signalref = value;
    }

    /**
     * Gets the value of the signalupdatebitposition property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSIGNALUPDATEBITPOSITION() {
        return signalupdatebitposition;
    }

    /**
     * Sets the value of the signalupdatebitposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSIGNALUPDATEBITPOSITION(Long value) {
        this.signalupdatebitposition = value;
    }

    /**
     * Gets the value of the manufacturerextension property.
     * 
     * @return
     *     possible object is
     *     {@link MANUFACTURERSIGNALINSTANCEEXTENSION }
     *     
     */
    public MANUFACTURERSIGNALINSTANCEEXTENSION getMANUFACTUREREXTENSION() {
        return manufacturerextension;
    }

    /**
     * Sets the value of the manufacturerextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link MANUFACTURERSIGNALINSTANCEEXTENSION }
     *     
     */
    public void setMANUFACTUREREXTENSION(MANUFACTURERSIGNALINSTANCEEXTENSION value) {
        this.manufacturerextension = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

}
