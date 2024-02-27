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
 * Content model for the entity CONNECTOR with platform specific extendability.
 * 
 * <p>Java class for CONNECTOR-TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CONNECTOR-TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.asam.net/xml/fbx}CHANNEL-REF"/>
 *         &lt;element ref="{http://www.asam.net/xml/fbx}CONTROLLER-REF" minOccurs="0"/>
 *         &lt;element name="INPUTS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="INPUT-PORT" type="{http://www.asam.net/xml/fbx}ECU-PORT-TYPE" maxOccurs="unbounded"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OUTPUTS" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="OUTPUT-PORT" type="{http://www.asam.net/xml/fbx}ECU-PORT-TYPE" maxOccurs="unbounded"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="MANUFACTURER-EXTENSION" type="{http://www.asam.net/xml/fbx}MANUFACTURER-CONNECTOR-EXTENSION" minOccurs="0"/>
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
@XmlType(name = "CONNECTOR-TYPE", propOrder = {
    "channelref",
    "controllerref",
    "inputs",
    "outputs",
    "manufacturerextension"
})
public class CONNECTORTYPE {

    @XmlElement(name = "CHANNEL-REF", required = true)
    protected CHANNELREF channelref;
    @XmlElement(name = "CONTROLLER-REF")
    protected CONTROLLERREF controllerref;
    @XmlElement(name = "INPUTS")
    protected CONNECTORTYPE.INPUTS inputs;
    @XmlElement(name = "OUTPUTS")
    protected CONNECTORTYPE.OUTPUTS outputs;
    @XmlElement(name = "MANUFACTURER-EXTENSION")
    protected MANUFACTURERCONNECTOREXTENSION manufacturerextension;
    @XmlAttribute(name = "ID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the channelref property.
     * 
     * @return
     *     possible object is
     *     {@link CHANNELREF }
     *     
     */
    public CHANNELREF getCHANNELREF() {
        return channelref;
    }

    /**
     * Sets the value of the channelref property.
     * 
     * @param value
     *     allowed object is
     *     {@link CHANNELREF }
     *     
     */
    public void setCHANNELREF(CHANNELREF value) {
        this.channelref = value;
    }

    /**
     * Gets the value of the controllerref property.
     * 
     * @return
     *     possible object is
     *     {@link CONTROLLERREF }
     *     
     */
    public CONTROLLERREF getCONTROLLERREF() {
        return controllerref;
    }

    /**
     * Sets the value of the controllerref property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONTROLLERREF }
     *     
     */
    public void setCONTROLLERREF(CONTROLLERREF value) {
        this.controllerref = value;
    }

    /**
     * Gets the value of the inputs property.
     * 
     * @return
     *     possible object is
     *     {@link CONNECTORTYPE.INPUTS }
     *     
     */
    public CONNECTORTYPE.INPUTS getINPUTS() {
        return inputs;
    }

    /**
     * Sets the value of the inputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONNECTORTYPE.INPUTS }
     *     
     */
    public void setINPUTS(CONNECTORTYPE.INPUTS value) {
        this.inputs = value;
    }

    /**
     * Gets the value of the outputs property.
     * 
     * @return
     *     possible object is
     *     {@link CONNECTORTYPE.OUTPUTS }
     *     
     */
    public CONNECTORTYPE.OUTPUTS getOUTPUTS() {
        return outputs;
    }

    /**
     * Sets the value of the outputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONNECTORTYPE.OUTPUTS }
     *     
     */
    public void setOUTPUTS(CONNECTORTYPE.OUTPUTS value) {
        this.outputs = value;
    }

    /**
     * Gets the value of the manufacturerextension property.
     * 
     * @return
     *     possible object is
     *     {@link MANUFACTURERCONNECTOREXTENSION }
     *     
     */
    public MANUFACTURERCONNECTOREXTENSION getMANUFACTUREREXTENSION() {
        return manufacturerextension;
    }

    /**
     * Sets the value of the manufacturerextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link MANUFACTURERCONNECTOREXTENSION }
     *     
     */
    public void setMANUFACTUREREXTENSION(MANUFACTURERCONNECTOREXTENSION value) {
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
     *       &lt;choice>
     *         &lt;element name="INPUT-PORT" type="{http://www.asam.net/xml/fbx}ECU-PORT-TYPE" maxOccurs="unbounded"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "inputport"
    })
    public static class INPUTS {

        @XmlElement(name = "INPUT-PORT")
        protected List<ECUPORTTYPE> inputport;

        /**
         * Gets the value of the inputport property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the inputport property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getINPUTPORT().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ECUPORTTYPE }
         * 
         * 
         */
        public List<ECUPORTTYPE> getINPUTPORT() {
            if (inputport == null) {
                inputport = new ArrayList<ECUPORTTYPE>();
            }
            return this.inputport;
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
     *       &lt;choice>
     *         &lt;element name="OUTPUT-PORT" type="{http://www.asam.net/xml/fbx}ECU-PORT-TYPE" maxOccurs="unbounded"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "outputport"
    })
    public static class OUTPUTS {

        @XmlElement(name = "OUTPUT-PORT")
        protected List<ECUPORTTYPE> outputport;

        /**
         * Gets the value of the outputport property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the outputport property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getOUTPUTPORT().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ECUPORTTYPE }
         * 
         * 
         */
        public List<ECUPORTTYPE> getOUTPUTPORT() {
            if (outputport == null) {
                outputport = new ArrayList<ECUPORTTYPE>();
            }
            return this.outputport;
        }

    }

}