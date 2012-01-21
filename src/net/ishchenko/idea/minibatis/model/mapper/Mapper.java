package net.ishchenko.idea.minibatis.model.mapper;

import com.intellij.util.xml.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.01.12
 * Time: 18:37
 */

public interface Mapper extends DomElement {

    @Attribute("namespace")
    GenericAttributeValue<String> getNamespace();

    @SubTagsList({"sql", "select", "insert", "update", "delete"})
    List<MapperIdentifiableStatement> getIdentifiableStatements();
    
    @SubTagList("sql")
    List<MapperIdentifiableStatement> getSqls();

    @SubTagList("select")
    List<MapperIdentifiableStatement> getSelects();

    @SubTagList("insert")
    List<MapperIdentifiableStatement> getInserts();

    @SubTagList("update")
    List<MapperIdentifiableStatement> getUpdates();

    @SubTagList("delete")
    List<MapperIdentifiableStatement> getDeletes();


}
