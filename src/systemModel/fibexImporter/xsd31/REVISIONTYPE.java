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
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * For reusability revisions content model is specified using a type definition.
 * 
 * <p>Java class for REVISION-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REVISION-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TEAM-MEMBER-REF" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="ID-REF" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element ref="{http://www.asam.net/xml}REVISION-LABEL" minOccurs="0"/>
 *         &lt;element ref="{http://www.asam.net/xml}STATE" minOccurs="0"/>
 *         &lt;element ref="{http://www.asam.net/xml}DATE" minOccurs="0"/>
 *         &lt;element name="COMPANY-REVISION-INFOS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="COMPANY-REVISION-INFO" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{http://www.asam.net/xml}COMPANY-DATA-REF"/>
 *                             &lt;element ref="{http://www.asam.net/xml}REVISION-LABEL" minOccurs="0"/>
 *                             &lt;element ref="{http://www.asam.net/xml}STATE" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="MODIFICATIONS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="MODIFICATION" type="{http://www.asam.net/xml}MODIFICATION" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REVISION-TYPE", namespace = "http://www.asam.net/xml", propOrder = {
    "teammemberref",
    "revisionlabel",
    "state",
    "date",
    "companyrevisioninfos",
    "modifications"
})
public class REVISIONTYPE {

    @XmlElement(name = "TEAM-MEMBER-REF")
    protected REVISIONTYPE.TEAMMEMBERREF teammemberref;
    @XmlElement(name = "REVISION-LABEL")
    protected String revisionlabel;
    @XmlElement(name = "STATE")
    protected String state;
    @XmlElement(name = "DATE")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "COMPANY-REVISION-INFOS")
    protected REVISIONTYPE.COMPANYREVISIONINFOS companyrevisioninfos;
    @XmlElement(name = "MODIFICATIONS")
    protected REVISIONTYPE.MODIFICATIONS modifications;

    /**
     * Gets the value of the teammemberref property.
     * 
     * @return
     *     possible object is
     *     {@link REVISIONTYPE.TEAMMEMBERREF }
     *     
     */
    public REVISIONTYPE.TEAMMEMBERREF getTEAMMEMBERREF() {
        return teammemberref;
    }

    /**
     * Sets the value of the teammemberref property.
     * 
     * @param value
     *     allowed object is
     *     {@link REVISIONTYPE.TEAMMEMBERREF }
     *     
     */
    public void setTEAMMEMBERREF(REVISIONTYPE.TEAMMEMBERREF value) {
        this.teammemberref = value;
    }

    /**
     * Gets the value of the revisionlabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREVISIONLABEL() {
        return revisionlabel;
    }

    /**
     * Sets the value of the revisionlabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREVISIONLABEL(String value) {
        this.revisionlabel = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTATE() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTATE(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATE() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATE(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the companyrevisioninfos property.
     * 
     * @return
     *     possible object is
     *     {@link REVISIONTYPE.COMPANYREVISIONINFOS }
     *     
     */
    public REVISIONTYPE.COMPANYREVISIONINFOS getCOMPANYREVISIONINFOS() {
        return companyrevisioninfos;
    }

    /**
     * Sets the value of the companyrevisioninfos property.
     * 
     * @param value
     *     allowed object is
     *     {@link REVISIONTYPE.COMPANYREVISIONINFOS }
     *     
     */
    public void setCOMPANYREVISIONINFOS(REVISIONTYPE.COMPANYREVISIONINFOS value) {
        this.companyrevisioninfos = value;
    }

    /**
     * Gets the value of the modifications property.
     * 
     * @return
     *     possible object is
     *     {@link REVISIONTYPE.MODIFICATIONS }
     *     
     */
    public REVISIONTYPE.MODIFICATIONS getMODIFICATIONS() {
        return modifications;
    }

    /**
     * Sets the value of the modifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link REVISIONTYPE.MODIFICATIONS }
     *     
     */
    public void setMODIFICATIONS(REVISIONTYPE.MODIFICATIONS value) {
        this.modifications = value;
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
     *         &lt;element name="COMPANY-REVISION-INFO" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{http://www.asam.net/xml}COMPANY-DATA-REF"/>
     *                   &lt;element ref="{http://www.asam.net/xml}REVISION-LABEL" minOccurs="0"/>
     *                   &lt;element ref="{http://www.asam.net/xml}STATE" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "companyrevisioninfo"
    })
    public static class COMPANYREVISIONINFOS {

        @XmlElement(name = "COMPANY-REVISION-INFO", namespace = "http://www.asam.net/xml", required = true)
        protected List<REVISIONTYPE.COMPANYREVISIONINFOS.COMPANYREVISIONINFO> companyrevisioninfo;

        /**
         * Gets the value of the companyrevisioninfo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the companyrevisioninfo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCOMPANYREVISIONINFO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link REVISIONTYPE.COMPANYREVISIONINFOS.COMPANYREVISIONINFO }
         * 
         * 
         */
        public List<REVISIONTYPE.COMPANYREVISIONINFOS.COMPANYREVISIONINFO> getCOMPANYREVISIONINFO() {
            if (companyrevisioninfo == null) {
                companyrevisioninfo = new ArrayList<REVISIONTYPE.COMPANYREVISIONINFOS.COMPANYREVISIONINFO>();
            }
            return this.companyrevisioninfo;
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
         *         &lt;element ref="{http://www.asam.net/xml}COMPANY-DATA-REF"/>
         *         &lt;element ref="{http://www.asam.net/xml}REVISION-LABEL" minOccurs="0"/>
         *         &lt;element ref="{http://www.asam.net/xml}STATE" minOccurs="0"/>
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
            "companydataref",
            "revisionlabel",
            "state"
        })
        public static class COMPANYREVISIONINFO {

            @XmlElement(name = "COMPANY-DATA-REF", namespace = "http://www.asam.net/xml", required = true)
            protected COMPANYDATAREF companydataref;
            @XmlElement(name = "REVISION-LABEL", namespace = "http://www.asam.net/xml")
            protected String revisionlabel;
            @XmlElement(name = "STATE", namespace = "http://www.asam.net/xml")
            protected String state;

            /**
             * Gets the value of the companydataref property.
             * 
             * @return
             *     possible object is
             *     {@link COMPANYDATAREF }
             *     
             */
            public COMPANYDATAREF getCOMPANYDATAREF() {
                return companydataref;
            }

            /**
             * Sets the value of the companydataref property.
             * 
             * @param value
             *     allowed object is
             *     {@link COMPANYDATAREF }
             *     
             */
            public void setCOMPANYDATAREF(COMPANYDATAREF value) {
                this.companydataref = value;
            }

            /**
             * Gets the value of the revisionlabel property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getREVISIONLABEL() {
                return revisionlabel;
            }

            /**
             * Sets the value of the revisionlabel property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setREVISIONLABEL(String value) {
                this.revisionlabel = value;
            }

            /**
             * Gets the value of the state property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSTATE() {
                return state;
            }

            /**
             * Sets the value of the state property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSTATE(String value) {
                this.state = value;
            }

        }

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
     *         &lt;element name="MODIFICATION" type="{http://www.asam.net/xml}MODIFICATION" maxOccurs="unbounded"/>
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
        "modification"
    })
    public static class MODIFICATIONS {

        @XmlElement(name = "MODIFICATION", namespace = "http://www.asam.net/xml", required = true)
        protected List<MODIFICATION> modification;

        /**
         * Gets the value of the modification property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the modification property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMODIFICATION().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MODIFICATION }
         * 
         * 
         */
        public List<MODIFICATION> getMODIFICATION() {
            if (modification == null) {
                modification = new ArrayList<MODIFICATION>();
            }
            return this.modification;
        }

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
     *       &lt;attribute name="ID-REF" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class TEAMMEMBERREF {

        @XmlAttribute(name = "ID-REF", required = true)
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
        protected Object idref;

        /**
         * Gets the value of the idref property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getIDREF() {
            return idref;
        }

        /**
         * Sets the value of the idref property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setIDREF(Object value) {
            this.idref = value;
        }

    }

}
