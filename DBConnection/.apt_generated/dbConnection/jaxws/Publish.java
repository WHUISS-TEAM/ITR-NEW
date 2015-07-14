
package dbConnection.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Publish", namespace = "http://dbConnection/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Publish", namespace = "http://dbConnection/")
public class Publish {

    @XmlElement(name = "arg0", namespace = "")
    private entity.Publishment arg0;

    /**
     * 
     * @return
     *     returns Publishment
     */
    public entity.Publishment getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(entity.Publishment arg0) {
        this.arg0 = arg0;
    }

}
