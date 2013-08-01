package net.ishchenko.idea.minibatis;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.ide.hierarchy.HierarchyBrowser;
import com.intellij.ide.hierarchy.HierarchyProvider;
import com.intellij.ide.hierarchy.call.JavaCallHierarchyProvider;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.pom.PomTarget;
import com.intellij.pom.PomTargetPsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomTarget;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMapIdentifiableStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 02.05.13
 * Time: 21:40
 */
public class IdentifiableStatementHierarchyProvider implements HierarchyProvider {

    private HierarchyProvider delegate = new JavaCallHierarchyProvider();

    @Nullable
    @Override
    public PsiElement getTarget(@NotNull DataContext dataContext) {

        final Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        if (project == null) return null;

        Editor editor = LangDataKeys.EDITOR.getData(dataContext);
        if (editor == null) return null;

        PsiElement psiElement = LangDataKeys.PSI_ELEMENT.getData(dataContext);
        if (!isIdentifiableStatement(psiElement)) {
            return null;
        }
        assert psiElement != null;
        Collection<PsiReference> refs = ReferencesSearch.search(psiElement).findAll();
        if (refs.size() == 1) {
            PsiElement element = refs.iterator().next().getElement();
            PsiMethod parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod.class, true);
            if (parentMethod == null) {
                HintManager.getInstance().showErrorHint(editor, "No direct usages in methods found, no hierarchy can be built");
                return null;
            } else {
                return parentMethod;
            }
        } else {
            HintManager.getInstance().showErrorHint(editor, "Multiple usages found, no hierarchy can be built");
            return null;
        }

    }

    @NotNull
    @Override
    public HierarchyBrowser createHierarchyBrowser(PsiElement target) {
        if (target instanceof NavigationItem && ((NavigationItem) target).canNavigate()) {
            ((NavigationItem) target).navigate(false);
        }
        return delegate.createHierarchyBrowser(target);
    }

    @Override
    public void browserActivated(@NotNull HierarchyBrowser hierarchyBrowser) {
        delegate.browserActivated(hierarchyBrowser);
    }

    private boolean isIdentifiableStatement(PsiElement element) {
        if (element instanceof PomTargetPsiElement) {
            PomTarget target = ((PomTargetPsiElement) element).getTarget();
            if (target instanceof DomTarget) {
                DomElement domElement = ((DomTarget) target).getDomElement();
                if (domElement instanceof SqlMapIdentifiableStatement) {
                    return true;
                }
            }
        }
        return false;
    }

}
