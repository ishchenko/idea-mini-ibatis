package net.ishchenko.idea.minibatis.model.sqlmap;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.01.12
 * Time: 21:54
 */
public interface ResultProvider extends DomElement {

    @Attribute("resultClass")
    GenericAttributeValue<TypeAlias> getResultClass();

    @Attribute("resultMap")
    GenericAttributeValue<ResultMap> getResultMap();

}
