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
import javax.xml.bind.annotation.XmlType;


/**
 * System state that must rule that the referencing timing is executed. To enable the description of combined states the occurence of SYSTEM-TRIGGER as well as of SYSTEM-STATE is unbounded. Use one trigger with many states, if these states must rule simultaneously (AND related) for execution. Use a separate trigger for each state if only one state needs to rule for execution.
 * 
 * <p>Java class for SYSTEM-TRIGGER complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SYSTEM-TRIGGER">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="SYSTEM-STATE" type="{http://www.asam.net/xml/fbx}EXECUTE-CONDITION-SYSTEM-STATE-TYPE"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SYSTEM-TRIGGER", propOrder = {
    "systemstate"
})
public class SYSTEMTRIGGER {

    @XmlElement(name = "SYSTEM-STATE", required = true)
    protected List<EXECUTECONDITIONSYSTEMSTATETYPE> systemstate;

    /**
     * Gets the value of the systemstate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the systemstate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSYSTEMSTATE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EXECUTECONDITIONSYSTEMSTATETYPE }
     * 
     * 
     */
    public List<EXECUTECONDITIONSYSTEMSTATETYPE> getSYSTEMSTATE() {
        if (systemstate == null) {
            systemstate = new ArrayList<EXECUTECONDITIONSYSTEMSTATETYPE>();
        }
        return this.systemstate;
    }

}
