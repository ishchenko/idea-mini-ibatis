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
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.12.11
 * Time: 23:59
 */
public class IdentifiableStatementReference extends PsiPolyVariantReferenceBase<PsiLiteralExpression> {
    
    private static final Pattern dotPattern = Pattern.compile("\\.");

    public IdentifiableStatementReference(PsiLiteralExpression expression) {
        super(expression);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {

        String rawText = getElement().getText();
        String[] parts = dotPattern.split(rawText.substring(1, rawText.length() - 1), 2);

        String namespace;
        String id;

        if (parts.length == 2) {
            namespace = parts[0];
            id = parts[1];
        } else {
            namespace = "";
            id = parts[0];
        }

        CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatements(namespace, id, processor);

        Collection<SqlMapIdentifiableStatement> processorResults = processor.getResults();
        final ResolveResult[] results = new ResolveResult[processorResults.size()];
        final SqlMapIdentifiableStatement[] statements = processorResults.toArray(new SqlMapIdentifiableStatement[processorResults.size()]);
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
