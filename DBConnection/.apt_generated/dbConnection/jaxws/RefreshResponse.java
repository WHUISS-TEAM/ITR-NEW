
package dbConnection.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "RefreshResponse", namespace = "http://dbConnection/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefreshResponse", namespace = "http://dbConnection/")
public class RefreshResponse {

    @XmlElement(name = "return", namespace = "")
    private List<utils.ReceiveModel> _return;

    /**
     * 
     * @return
     *     returns List<ReceiveModel>
     */
    public List<utils.ReceiveModel> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(List<utils.ReceiveModel> _return) {
        this._return = _return;
    }

}
