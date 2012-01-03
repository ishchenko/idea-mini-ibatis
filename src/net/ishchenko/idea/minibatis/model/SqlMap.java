package net.ishchenko.idea.minibatis.model;

import com.intellij.util.xml.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 23.12.11
 * Time: 23:56
 */
public interface SqlMap extends DomElement {

    @Attribute("namespace")
    GenericAttributeValue<String> getNamespace();

    @SubTagsList({"sql", "statement", "select", "insert", "update", "delete", "procedure"})
    List<IdentifiableStatement> getIdentifiableStatements();
    
    @SubTagList("sql")
    List<IdentifiableStatement> getSqls();

    @SubTagList("statement")
    List<Statement> getStatements();

    @SubTagList("select")
    List<Select> getSelects();

    @SubTagList("insert")
    List<IdentifiableStatement> getInserts();

    @SubTagList("update")
    List<IdentifiableStatement> getUpdates();

    @SubTagList("delete")
    List<IdentifiableStatement> getDeletes();

    @SubTagList("procedure")
    List<Procedure> getProcedures();

    @SubTagList("typeAlias")
    List<TypeAlias> getTypeAliases();
}
