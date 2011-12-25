package net.ishchenko.idea.minibatis;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.xml.DomFileElement;
import net.ishchenko.idea.minibatis.model.IdentifiableStatement;
import net.ishchenko.idea.minibatis.model.SqlMap;
import net.ishchenko.idea.minibatis.model.SqlMapModel;
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

        Module module = ModuleUtil.findModuleForPsiElement(getElement());
        if (module == null) {
            return null;
        }

        SqlMapModelFactory sqlMapModelFactory = ServiceManager.getService(getElement().getProject(), SqlMapModelFactory.class);
        for (SqlMapModel sqlMapModel : sqlMapModelFactory.getAllModels(module)) {
            for (DomFileElement<SqlMap> root : sqlMapModel.getRoots()) {
                SqlMap sqlMap = root.getRootElement();
                if (namespace.equals(sqlMap.getNamespace().getStringValue())) {
                    for (IdentifiableStatement statement : sqlMap.getIdentifiableStatements()) {
                        if (id.equals(statement.getId().getStringValue())) {
                            return statement.getXmlElement();
                        }
                    }
                }
            }
        }

        return null;
    }

    @NotNull
    public Object[] getVariants() {

        Module module = ModuleUtil.findModuleForPsiElement(getElement());
        if (module == null) {
            return EMPTY_ARRAY;
        }

        List<Object> result = new ArrayList<Object>();
        SqlMapModelFactory sqlMapModelFactory = ServiceManager.getService(getElement().getProject(), SqlMapModelFactory.class);
        for (SqlMapModel sqlMapModel : sqlMapModelFactory.getAllModels(module)) {
            for (DomFileElement<SqlMap> root : sqlMapModel.getRoots()) {
                SqlMap sqlMap = root.getRootElement();
                String namespace = sqlMap.getNamespace().getStringValue();
                if (namespace != null) {
                    for (IdentifiableStatement statement : sqlMap.getIdentifiableStatements()) {
                        String id = statement.getId().getStringValue();
                        if (id != null) {
                            result.add(LookupElementBuilder.create(namespace + "." + id));
                        }
                    }
                }
            }
        }

        return result.toArray(new Object[result.size()]);
    }

}
