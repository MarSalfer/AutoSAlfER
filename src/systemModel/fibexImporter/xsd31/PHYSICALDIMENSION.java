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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Physical dimension definition
 * 
 * <p>Java class for PHYSICAL-DIMENSION complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PHYSICAL-DIMENSION">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.asam.net/xml}NAME-DETAILS"/>
 *         &lt;element name="LENGTH-EXP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="MASS-EXP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="TIME-EXP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="CURRENT-EXP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="TEMPERATURE-EXP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="MOLAR-AMOUNT-EXP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="LUMINOUS-INTENSITY-EXP" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
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
@XmlType(name = "PHYSICAL-DIMENSION", namespace = "http://www.asam.net/xml", propOrder = {
    "shortname",
    "longname",
    "desc",
    "lengthexp",
    "massexp",
    "timeexp",
    "currentexp",
    "temperatureexp",
    "molaramountexp",
    "luminousintensityexp"
})
public class PHYSICALDIMENSION {

    @XmlElement(name = "SHORT-NAME", required = true)
    protected String shortname;
    @XmlElement(name = "LONG-NAME")
    protected List<LONGNAME> longname;
    @XmlElement(name = "DESC")
    protected List<DESC> desc;
    @XmlElement(name = "LENGTH-EXP")
    protected Double lengthexp;
    @XmlElement(name = "MASS-EXP")
    protected Double massexp;
    @XmlElement(name = "TIME-EXP")
    protected Double timeexp;
    @XmlElement(name = "CURRENT-EXP")
    protected Double currentexp;
    @XmlElement(name = "TEMPERATURE-EXP")
    protected Double temperatureexp;
    @XmlElement(name = "MOLAR-AMOUNT-EXP")
    protected Double molaramountexp;
    @XmlElement(name = "LUMINOUS-INTENSITY-EXP")
    protected Double luminousintensityexp;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the shortname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHORTNAME() {
        return shortname;
    }

    /**
     * Sets the value of the shortname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHORTNAME(String value) {
        this.shortname = value;
    }

    /**
     * Gets the value of the longname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the longname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLONGNAME().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LONGNAME }
     * 
     * 
     */
    public List<LONGNAME> getLONGNAME() {
        if (longname == null) {
            longname = new ArrayList<LONGNAME>();
        }
        return this.longname;
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
     * Gets the value of the lengthexp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLENGTHEXP() {
        return lengthexp;
    }

    /**
     * Sets the value of the lengthexp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLENGTHEXP(Double value) {
        this.lengthexp = value;
    }

    /**
     * Gets the value of the massexp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMASSEXP() {
        return massexp;
    }

    /**
     * Sets the value of the massexp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMASSEXP(Double value) {
        this.massexp = value;
    }

    /**
     * Gets the value of the timeexp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTIMEEXP() {
        return timeexp;
    }

    /**
     * Sets the value of the timeexp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTIMEEXP(Double value) {
        this.timeexp = value;
    }

    /**
     * Gets the value of the currentexp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getCURRENTEXP() {
        return currentexp;
    }

    /**
     * Sets the value of the currentexp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setCURRENTEXP(Double value) {
        this.currentexp = value;
    }

    /**
     * Gets the value of the temperatureexp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTEMPERATUREEXP() {
        return temperatureexp;
    }

    /**
     * Sets the value of the temperatureexp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTEMPERATUREEXP(Double value) {
        this.temperatureexp = value;
    }

    /**
     * Gets the value of the molaramountexp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMOLARAMOUNTEXP() {
        return molaramountexp;
    }

    /**
     * Sets the value of the molaramountexp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMOLARAMOUNTEXP(Double value) {
        this.molaramountexp = value;
    }

    /**
     * Gets the value of the luminousintensityexp property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLUMINOUSINTENSITYEXP() {
        return luminousintensityexp;
    }

    /**
     * Sets the value of the luminousintensityexp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLUMINOUSINTENSITYEXP(Double value) {
        this.luminousintensityexp = value;
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