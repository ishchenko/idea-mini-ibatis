package net.ishchenko.idea.minibatis.model.sqlmap;

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
    List<SqlMapIdentifiableStatement> getIdentifiableStatements();
    
    @SubTagList("sql")
    List<SqlMapIdentifiableStatement> getSqls();

    @SubTagList("statement")
    List<Statement> getStatements();

    @SubTagList("select")
    List<Select> getSelects();

    @SubTagList("insert")
    List<SqlMapIdentifiableStatement> getInserts();

    @SubTagList("update")
    List<SqlMapIdentifiableStatement> getUpdates();

    @SubTagList("delete")
    List<SqlMapIdentifiableStatement> getDeletes();

    @SubTagList("procedure")
    List<Procedure> getProcedures();

    @SubTagList("typeAlias")
    List<TypeAlias> getTypeAliases();
}
