package net.ishchenko.idea.minibatis;

import com.intellij.ide.actions.QualifiedNameProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.pom.PomTarget;
import com.intellij.pom.PomTargetPsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomTarget;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMap;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMapIdentifiableStatement;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 30.04.13
 * Time: 0:06
 */
public class IdentifiableStatementQualifiedNameProvider implements QualifiedNameProvider {

    @Nullable
    @Override
    public PsiElement adjustElementToCopy(PsiElement element) {
        if (resolveIdentifiableStatement(element) != null) {
            return element;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public String getQualifiedName(PsiElement element) {
        SqlMapIdentifiableStatement statement = resolveIdentifiableStatement(element);
        if (statement != null) {
            SqlMap sqlMap = statement.getParentOfType(SqlMap.class, true);
            if (sqlMap != null) {
                String namespace = sqlMap.getNamespace().getRawText();
                return (namespace != null ? namespace + "." : "") + statement.getId();
            }
        }
        return null;
    }

    @Override
    public PsiElement qualifiedNameToElement(String fqn, Project project) {
        return null;
    }

    @Override
    public void insertQualifiedName(String fqn, PsiElement element, Editor editor, Project project) {

    }

    private SqlMapIdentifiableStatement resolveIdentifiableStatement(PsiElement element) {
        if (element instanceof PomTargetPsiElement) {
            PomTarget target = ((PomTargetPsiElement) element).getTarget();
            if (target instanceof DomTarget) {
                DomElement domElement = ((DomTarget) target).getDomElement();
                if (domElement instanceof SqlMapIdentifiableStatement) {
                    return (SqlMapIdentifiableStatement) domElement;
                }
            }
        }
        return null;
    }

}
