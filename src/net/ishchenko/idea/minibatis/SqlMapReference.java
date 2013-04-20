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
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 20.04.13
 * Time: 22:01
 */
public class SqlMapReference extends PsiPolyVariantReferenceBase<PsiLiteralExpression> {

    public SqlMapReference(PsiLiteralExpression expression) {
        super(expression);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {

        String rawText = getElement().getText();

        //rawText contains quotes, i.e. only "x" count
        if (rawText.length() < 3) {
            return ResolveResult.EMPTY_ARRAY;
        }

        CommonProcessors.CollectUniquesProcessor<SqlMap> processor = new CommonProcessors.CollectUniquesProcessor<SqlMap>();
        String noQuotesText = rawText.substring(1, rawText.length() - 1);
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMaps(noQuotesText, processor);

        Collection<SqlMap> processorResults = processor.getResults();
        final List<ResolveResult> results = new ArrayList<ResolveResult>(processorResults.size());
        final SqlMap[] sqlMaps = processorResults.toArray(new SqlMap[processorResults.size()]);
        for (SqlMap sqlMap : sqlMaps) {
            DomTarget target = DomTarget.getTarget(sqlMap);
            if (target != null) {
                XmlElement xmlElement = sqlMap.getXmlElement();
                final String locationString = xmlElement != null ? xmlElement.getContainingFile().getName() : "";
                results.add(new PsiElementResolveResult(new PomTargetPsiElementImpl(target) {
                    @Override
                    public String getLocationString() {
                        return locationString;
                    }
                }));
            }
        }
        return results.toArray(ResolveResult.EMPTY_ARRAY);

    }

    @NotNull
    public Object[] getVariants() {

        CommonProcessors.CollectProcessor<String> processor = new CommonProcessors.CollectProcessor<String>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapNamespaceNames(processor);
        return processor.toArray(new String[processor.getResults().size()]);

    }


}
