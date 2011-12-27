package net.ishchenko.idea.minibatis;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import net.ishchenko.idea.minibatis.model.IdentifiableStatement;
import net.ishchenko.idea.minibatis.model.SqlMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.12.11
 * Time: 23:59
 */
public class IdentifiableStatementReference extends PsiReferenceBase<PsiLiteralExpression> {

    public IdentifiableStatementReference(PsiLiteralExpression expression) {
        super(expression);
    }

    @Nullable
    public PsiElement resolve() {

        String[] parts = getElement().getText().replace("\"", "").split("\\.");
        if (parts.length != 2) {
            return null;
        }

        String namespace = parts[0];
        String id = parts[1];

        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap sqlMap = fileElement.getRootElement();
            if (namespace.equals(sqlMap.getNamespace().getRawText())) {
                for (IdentifiableStatement statement : sqlMap.getIdentifiableStatements()) {
                    if (id.equals(statement.getId().getRawText())) {
                        return statement.getXmlElement();
                    }
                }
            }
        }

        return null;
    }

    @NotNull
    public Object[] getVariants() {

        List<Object> result = new ArrayList<Object>();

        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {

            SqlMap rootElement = fileElement.getRootElement();
            String namespace = rootElement.getNamespace().getRawText();
            if (namespace != null) {
                for (IdentifiableStatement statement : rootElement.getIdentifiableStatements()) {
                    String id = statement.getId().getRawText();
                    if (id != null) {
                        result.add(LookupElementBuilder.create(namespace + "." + id));
                    }
                }
            }
        }

        return result.toArray(new Object[result.size()]);

    }

    private List<DomFileElement<SqlMap>> findSqlMapFileElements() {
        return DomService.getInstance().getFileElements(SqlMap.class, getElement().getProject(), GlobalSearchScope.projectScope(getElement().getProject()));
    }

}
