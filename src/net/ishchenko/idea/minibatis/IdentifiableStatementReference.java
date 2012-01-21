package net.ishchenko.idea.minibatis;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.PomTargetPsiElementImpl;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.DomTarget;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMapIdentifiableStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.12.11
 * Time: 23:59
 */
public class IdentifiableStatementReference extends PsiPolyVariantReferenceBase<PsiLiteralExpression> {

    public IdentifiableStatementReference(PsiLiteralExpression expression) {
        super(expression);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {

        String[] parts = getElement().getText().replace("\"", "").split("\\.");
        if (parts.length != 2) {
            return ResolveResult.EMPTY_ARRAY;
        }

        String namespace = parts[0];
        String id = parts[1];

        CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatements(namespace, id, processor);
        Collection<SqlMapIdentifiableStatement> processorResults = processor.getResults();
        final ResolveResult[] results = new ResolveResult[processorResults.size()];
        final SqlMapIdentifiableStatement[] statements = processor.getResults().toArray(new SqlMapIdentifiableStatement[processor.getResults().size()]);
        for (int i = 0; i < statements.length; i++) {
            SqlMapIdentifiableStatement statement = statements[i];
            DomTarget target = DomTarget.getTarget(statement);
            if (target != null) {
                XmlElement xmlElement = statement.getXmlElement();
                final String locationString = xmlElement != null ? xmlElement.getContainingFile().getName() : "";
                results[i] = new PsiElementResolveResult(new PomTargetPsiElementImpl(target) {
                    @Override
                    public String getLocationString() {
                        return locationString;
                    }
                });
            }
        }
        return results;

    }

    @NotNull
    public Object[] getVariants() {

        CommonProcessors.CollectProcessor<String> processor = new CommonProcessors.CollectProcessor<String>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatementNames(processor);
        return processor.toArray(new String[processor.getResults().size()]);

    }

}
