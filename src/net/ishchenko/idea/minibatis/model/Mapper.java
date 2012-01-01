package net.ishchenko.idea.minibatis.model;

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

    @SubTagsList({"sql", "statement", "select", "insert", "update", "delete", "procedure"})
    List<IdentifiableStatement> getIdentifiableStatements();
    
    @SubTagList("sql")
    List<IdentifiableStatement> getSqls();

    @SubTagList("statement")
    List<IdentifiableStatement> getStatements();

    @SubTagList("select")
    List<IdentifiableStatement> getSelects();

    @SubTagList("insert")
    List<IdentifiableStatement> getInserts();

    @SubTagList("update")
    List<IdentifiableStatement> getUpdates();

    @SubTagList("delete")
    List<IdentifiableStatement> getDeletes();

    @SubTagList("procedure")
    List<IdentifiableStatement> getProcedures();

}
