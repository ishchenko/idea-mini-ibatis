package net.ishchenko.idea.minibatis.model.sqlmap;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.03.12
 * Time: 21:44
 */
public interface ParameterMap extends DomElement {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

    @Attribute("class")
    GenericAttributeValue<TypeAlias> getClazz();

}
