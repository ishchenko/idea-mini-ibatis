package net.ishchenko.idea.minibatis.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.Required;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 23.12.11
 * Time: 23:43
 */
public interface IdentifiableStatement extends DomElement {

    @NotNull
    @Required
    @Attribute("id")
    public GenericAttributeValue<String> getId();

}
