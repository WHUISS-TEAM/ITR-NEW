
package dbConnection.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "EditTag", namespace = "http://dbConnection/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EditTag", namespace = "http://dbConnection/")
public class EditTag {

    @XmlElement(name = "arg0", namespace = "")
    private entity.Profile arg0;

    /**
     * 
     * @return
     *     returns Profile
     */
    public entity.Profile getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(entity.Profile arg0) {
        this.arg0 = arg0;
    }

}
