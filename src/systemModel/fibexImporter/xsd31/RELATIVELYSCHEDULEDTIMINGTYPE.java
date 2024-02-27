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
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;


/**
 * Content model for the entity TIMING in the relatively scheduled peculiarity.
 * 
 * <p>Java class for RELATIVELY-SCHEDULED-TIMING-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RELATIVELY-SCHEDULED-TIMING-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.asam.net/xml}DESC" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SCHEDULE-TABLE-NAME" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="POSITION-IN-TABLE" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="DELAY" type="{http://www.w3.org/2001/XMLSchema}duration"/>
 *         &lt;element name="MANUFACTURER-EXTENSION" type="{http://www.asam.net/xml/fbx}MANUFACTURER-RELATIVELY-SCHEDULED-TIMING-EXTENSION" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RELATIVELY-SCHEDULED-TIMING-TYPE", propOrder = {
    "desc",
    "scheduletablename",
    "positionintable",
    "delay",
    "manufacturerextension"
})
public class RELATIVELYSCHEDULEDTIMINGTYPE {

    @XmlElement(name = "DESC", namespace = "http://www.asam.net/xml")
    protected List<DESC> desc;
    @XmlElement(name = "SCHEDULE-TABLE-NAME", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String scheduletablename;
    @XmlElement(name = "POSITION-IN-TABLE")
    protected short positionintable;
    @XmlElement(name = "DELAY", required = true)
    protected Duration delay;
    @XmlElement(name = "MANUFACTURER-EXTENSION")
    protected MANUFACTURERRELATIVELYSCHEDULEDTIMINGEXTENSION manufacturerextension;

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
     * Gets the value of the scheduletablename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCHEDULETABLENAME() {
        return scheduletablename;
    }

    /**
     * Sets the value of the scheduletablename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCHEDULETABLENAME(String value) {
        this.scheduletablename = value;
    }

    /**
     * Gets the value of the positionintable property.
     * 
     */
    public short getPOSITIONINTABLE() {
        return positionintable;
    }

    /**
     * Sets the value of the positionintable property.
     * 
     */
    public void setPOSITIONINTABLE(short value) {
        this.positionintable = value;
    }

    /**
     * Gets the value of the delay property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getDELAY() {
        return delay;
    }

    /**
     * Sets the value of the delay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setDELAY(Duration value) {
        this.delay = value;
    }

    /**
     * Gets the value of the manufacturerextension property.
     * 
     * @return
     *     possible object is
     *     {@link MANUFACTURERRELATIVELYSCHEDULEDTIMINGEXTENSION }
     *     
     */
    public MANUFACTURERRELATIVELYSCHEDULEDTIMINGEXTENSION getMANUFACTUREREXTENSION() {
        return manufacturerextension;
    }

    /**
     * Sets the value of the manufacturerextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link MANUFACTURERRELATIVELYSCHEDULEDTIMINGEXTENSION }
     *     
     */
    public void setMANUFACTUREREXTENSION(MANUFACTURERRELATIVELYSCHEDULEDTIMINGEXTENSION value) {
        this.manufacturerextension = value;
    }

}
