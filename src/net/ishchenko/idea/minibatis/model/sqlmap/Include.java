package net.ishchenko.idea.minibatis.model.sqlmap;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.01.12
 * Time: 20:40
 */
public interface Include extends DomElement {

    @Attribute("refid")
    GenericAttributeValue<SqlMapIdentifiableStatement> getRefid();

}
