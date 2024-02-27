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
 * Content model for the entity PORT in the ECU related peculiarity.
 * 
 * <p>Java class for ECU-PORT-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ECU-PORT-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.asam.net/xml}DESC" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.asam.net/xml/fbx}FRAME-TRIGGERING-REF"/>
 *         &lt;choice>
 *           &lt;element name="INCLUDED-PDUS">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="INCLUDED-PDU" type="{http://www.asam.net/xml/fbx}INCLUDED-PDU-TYPE" maxOccurs="unbounded"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="COMPLETE-FRAME">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="MANUFACTURER-EXTENSION" type="{http://www.asam.net/xml/fbx}MANUFACTURER-PORT-EXTENSION" minOccurs="0"/>
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
@XmlType(name = "ECU-PORT-TYPE", propOrder = {
    "desc",
    "frametriggeringref",
    "includedpdus",
    "completeframe",
    "manufacturerextension"
})
public class ECUPORTTYPE {

    @XmlElement(name = "DESC", namespace = "http://www.asam.net/xml")
    protected List<DESC> desc;
    @XmlElement(name = "FRAME-TRIGGERING-REF", required = true)
    protected FRAMETRIGGERINGREF frametriggeringref;
    @XmlElement(name = "INCLUDED-PDUS")
    protected ECUPORTTYPE.INCLUDEDPDUS includedpdus;
    @XmlElement(name = "COMPLETE-FRAME")
    protected ECUPORTTYPE.COMPLETEFRAME completeframe;
    @XmlElement(name = "MANUFACTURER-EXTENSION")
    protected MANUFACTURERPORTEXTENSION manufacturerextension;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

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
     * Gets the value of the frametriggeringref property.
     * 
     * @return
     *     possible object is
     *     {@link FRAMETRIGGERINGREF }
     *     
     */
    public FRAMETRIGGERINGREF getFRAMETRIGGERINGREF() {
        return frametriggeringref;
    }

    /**
     * Sets the value of the frametriggeringref property.
     * 
     * @param value
     *     allowed object is
     *     {@link FRAMETRIGGERINGREF }
     *     
     */
    public void setFRAMETRIGGERINGREF(FRAMETRIGGERINGREF value) {
        this.frametriggeringref = value;
    }

    /**
     * Gets the value of the includedpdus property.
     * 
     * @return
     *     possible object is
     *     {@link ECUPORTTYPE.INCLUDEDPDUS }
     *     
     */
    public ECUPORTTYPE.INCLUDEDPDUS getINCLUDEDPDUS() {
        return includedpdus;
    }

    /**
     * Sets the value of the includedpdus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ECUPORTTYPE.INCLUDEDPDUS }
     *     
     */
    public void setINCLUDEDPDUS(ECUPORTTYPE.INCLUDEDPDUS value) {
        this.includedpdus = value;
    }

    /**
     * Gets the value of the completeframe property.
     * 
     * @return
     *     possible object is
     *     {@link ECUPORTTYPE.COMPLETEFRAME }
     *     
     */
    public ECUPORTTYPE.COMPLETEFRAME getCOMPLETEFRAME() {
        return completeframe;
    }

    /**
     * Sets the value of the completeframe property.
     * 
     * @param value
     *     allowed object is
     *     {@link ECUPORTTYPE.COMPLETEFRAME }
     *     
     */
    public void setCOMPLETEFRAME(ECUPORTTYPE.COMPLETEFRAME value) {
        this.completeframe = value;
    }

    /**
     * Gets the value of the manufacturerextension property.
     * 
     * @return
     *     possible object is
     *     {@link MANUFACTURERPORTEXTENSION }
     *     
     */
    public MANUFACTURERPORTEXTENSION getMANUFACTUREREXTENSION() {
        return manufacturerextension;
    }

    /**
     * Sets the value of the manufacturerextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link MANUFACTURERPORTEXTENSION }
     *     
     */
    public void setMANUFACTUREREXTENSION(MANUFACTURERPORTEXTENSION value) {
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class COMPLETEFRAME {


    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="INCLUDED-PDU" type="{http://www.asam.net/xml/fbx}INCLUDED-PDU-TYPE" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "includedpdu"
    })
    public static class INCLUDEDPDUS {

        @XmlElement(name = "INCLUDED-PDU", required = true)
        protected List<INCLUDEDPDUTYPE> includedpdu;

        /**
         * Gets the value of the includedpdu property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the includedpdu property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getINCLUDEDPDU().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link INCLUDEDPDUTYPE }
         * 
         * 
         */
        public List<INCLUDEDPDUTYPE> getINCLUDEDPDU() {
            if (includedpdu == null) {
                includedpdu = new ArrayList<INCLUDEDPDUTYPE>();
            }
            return this.includedpdu;
        }

    }

}