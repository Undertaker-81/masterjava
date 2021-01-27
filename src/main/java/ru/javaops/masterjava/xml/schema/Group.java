
package ru.javaops.masterjava.xml.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type" type="{http://javaops.ru}state"/>
 *         &lt;element name="project" type="{http://javaops.ru}projectName"/>
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
    "name",
    "type",
    "project"
})
@XmlRootElement(name = "group", namespace = "http://javaops.ru")
public class Group {

    @XmlElement(namespace = "http://javaops.ru", required = true)
    protected String name;
    @XmlElement(namespace = "http://javaops.ru", required = true)
    @XmlSchemaType(name = "string")
    protected State type;
    @XmlElement(namespace = "http://javaops.ru", required = true)
    @XmlSchemaType(name = "string")
    protected ProjectName project;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link State }
     *     
     */
    public State getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link State }
     *     
     */
    public void setType(State value) {
        this.type = value;
    }

    /**
     * Gets the value of the project property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectName }
     *     
     */
    public ProjectName getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectName }
     *     
     */
    public void setProject(ProjectName value) {
        this.project = value;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", project=" + project +
                '}';
    }
}
