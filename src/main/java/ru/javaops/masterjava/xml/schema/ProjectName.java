
package ru.javaops.masterjava.xml.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for projectName.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="projectName">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TopJAVA"/>
 *     &lt;enumeration value="MasterJava"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "projectName", namespace = "http://javaops.ru")
@XmlEnum
public enum ProjectName {

    @XmlEnumValue("TopJAVA")
    TOP_JAVA("TopJAVA"),
    @XmlEnumValue("MasterJava")
    MASTER_JAVA("MasterJava");
    private final String value;

    ProjectName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProjectName fromValue(String v) {
        for (ProjectName c: ProjectName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
