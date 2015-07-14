
package dbConnection.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "comment", namespace = "http://dbConnection/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comment", namespace = "http://dbConnection/")
public class Comment {

    @XmlElement(name = "arg0", namespace = "")
    private entity.CommentPub arg0;

    /**
     * 
     * @return
     *     returns CommentPub
     */
    public entity.CommentPub getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(entity.CommentPub arg0) {
        this.arg0 = arg0;
    }

}
