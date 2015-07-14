
package dbConnection.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "loadCommentResponse", namespace = "http://dbConnection/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loadCommentResponse", namespace = "http://dbConnection/")
public class LoadCommentResponse {

    @XmlElement(name = "return", namespace = "")
    private List<entity.CommentPub> _return;

    /**
     * 
     * @return
     *     returns List<CommentPub>
     */
    public List<entity.CommentPub> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(List<entity.CommentPub> _return) {
        this._return = _return;
    }

}
