package net.ishchenko.idea.minibatis.model.mapper;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;

/**
 * Created with IntelliJ IDEA.
 * User: mishchenko
 * Date: 26.05.12
 * Time: 16:21
 */
public interface ResultMap extends DomElement {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

    @Attribute("extends")
    GenericAttributeValue<ResultMap> getExtends();

}
