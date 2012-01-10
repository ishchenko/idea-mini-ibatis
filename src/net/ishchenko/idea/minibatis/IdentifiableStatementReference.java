package net.ishchenko.idea.minibatis;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.pom.references.PomService;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.DomTarget;
import net.ishchenko.idea.minibatis.model.IdentifiableStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        CommonProcessors.FindFirstProcessor<IdentifiableStatement> processor = new CommonProcessors.FindFirstProcessor<IdentifiableStatement>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatements(namespace, id, processor);
        if (processor.isFound()) {
            DomTarget target = DomTarget.getTarget(processor.getFoundValue());
            return target != null ? PomService.convertToPsi(target) : null;
        } else {
            return null;
        }

    }

    @NotNull
    public Object[] getVariants() {

        CommonProcessors.CollectProcessor<String> processor = new CommonProcessors.CollectProcessor<String>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatementNames(processor);
        return processor.toArray(new String[processor.getResults().size()]);

    }

}
