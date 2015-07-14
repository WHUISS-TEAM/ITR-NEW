
package dbConnection.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "verifyUserResponse", namespace = "http://dbConnection/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "verifyUserResponse", namespace = "http://dbConnection/")
public class VerifyUserResponse {

    @XmlElement(name = "return", namespace = "")
    private entity.Profile _return;

    /**
     * 
     * @return
     *     returns Profile
     */
    public entity.Profile getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(entity.Profile _return) {
        this._return = _return;
    }

}
