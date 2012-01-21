package net.ishchenko.idea.minibatis.model.mapper;

import com.intellij.util.xml.*;
import net.ishchenko.idea.minibatis.model.sqlmap.Include;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 23.12.11
 * Time: 23:43
 */
public interface MapperIdentifiableStatement extends DomElement {

    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();

    @SubTagList("include")
    List<Include> getIncludes();

}
