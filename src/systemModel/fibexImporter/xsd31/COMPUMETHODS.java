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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://www.asam.net/xml}COMPU-METHOD" maxOccurs="unbounded"/>
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
    "compumethod"
})
@XmlRootElement(name = "COMPU-METHODS", namespace = "http://www.asam.net/xml")
public class COMPUMETHODS {

    @XmlElement(name = "COMPU-METHOD", namespace = "http://www.asam.net/xml", required = true)
    protected List<COMPUMETHOD> compumethod;

    /**
     * Gets the value of the compumethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compumethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCOMPUMETHOD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COMPUMETHOD }
     * 
     * 
     */
    public List<COMPUMETHOD> getCOMPUMETHOD() {
        if (compumethod == null) {
            compumethod = new ArrayList<COMPUMETHOD>();
        }
        return this.compumethod;
    }

}
