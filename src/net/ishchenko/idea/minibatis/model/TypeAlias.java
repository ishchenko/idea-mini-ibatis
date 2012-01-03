package net.ishchenko.idea.minibatis.model;

import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.01.12
 * Time: 21:50
 */
public interface TypeAlias extends DomElement {

    @NameValue
    @Attribute("alias")
    GenericAttributeValue<String> getAlias();

    @Attribute("type")
    GenericAttributeValue<PsiClass> getType();

}
