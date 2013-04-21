package net.ishchenko.idea.minibatis;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.JavaConstantExpressionEvaluator;
import com.intellij.psi.impl.PomTargetPsiElementImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.DomTarget;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMapIdentifiableStatement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

        String value = tryComputeConcatenatedValue();
        if (value.length() == 0) {
            return ResolveResult.EMPTY_ARRAY;
        }

        String[] parts = dotPattern.split(value);

        if (parts.length == 1) {
            return findResults("", parts[0]).toArray(ResolveResult.EMPTY_ARRAY);
        } else {
            List<ResolveResult> results = new ArrayList<ResolveResult>();
            for (int i = 0; i < parts.length - 1; i++) {
                results.addAll(findResults(concatBefore(parts, i), concatAfter(parts, i + 1)));
            }
            return results.toArray(new ResolveResult[results.size()]);
        }

    }

    @NotNull
    public Object[] getVariants() {

        CommonProcessors.CollectProcessor<String> processor = new CommonProcessors.CollectProcessor<String>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatementNames(processor);
        return processor.toArray(new String[processor.getResults().size()]);

    }

    private String tryComputeConcatenatedValue() {

        PsiPolyadicExpression parentExpression = PsiTreeUtil.getParentOfType(getElement(), PsiPolyadicExpression.class);

        if (parentExpression != null) {
            StringBuilder computedValue = new StringBuilder();
            for (PsiExpression operand : parentExpression.getOperands()) {
                if (operand instanceof PsiReference) {
                    PsiElement probableDefinition = ((PsiReference) operand).resolve();
                    if (probableDefinition instanceof PsiVariable) {
                        PsiExpression initializer = ((PsiVariable) probableDefinition).getInitializer();
                        if (initializer != null) {
                            Object value = JavaConstantExpressionEvaluator.computeConstantExpression(initializer, true);
                            if (value instanceof String) {
                                computedValue.append(value);
                            }
                        }
                    }
                } else {
                    Object value = JavaConstantExpressionEvaluator.computeConstantExpression(operand, true);
                    if (value instanceof String) {
                        computedValue.append(value);
                    }
                }
            }
            return computedValue.toString();
        } else {
            String rawText = getElement().getText();
            //with quotes, i.e. at least "x" count
            if (rawText.length() < 3) {
                return "";
            }
            //clean up quotes
            return rawText.substring(1, rawText.length() - 1);
        }

    }

    private List<ResolveResult> findResults(String namespace, String id) {

        CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement>();
        ServiceManager.getService(getElement().getProject(), DomFileElementsFinder.class).processSqlMapStatements(namespace, id, processor);

        Collection<SqlMapIdentifiableStatement> processorResults = processor.getResults();
        final List<ResolveResult> results = new ArrayList<ResolveResult>(processorResults.size());
        final SqlMapIdentifiableStatement[] statements = processorResults.toArray(new SqlMapIdentifiableStatement[processorResults.size()]);
        for (SqlMapIdentifiableStatement statement : statements) {
            DomTarget target = DomTarget.getTarget(statement);
            if (target != null) {
                XmlElement xmlElement = statement.getXmlElement();
                final String locationString = xmlElement != null ? xmlElement.getContainingFile().getName() : "";
                results.add(new PsiElementResolveResult(new PomTargetPsiElementImpl(target) {
                    @Override
                    public String getLocationString() {
                        return locationString;
                    }
                }));
            }
        }
        return results;
    }

    private String concatBefore(String[] parts, int before) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= before; i++) {
            sb.append(parts[i]);
            if (i + 1 <= before) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    private String concatAfter(String[] parts, int after) {
        StringBuilder sb = new StringBuilder();
        for (int i = after; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i + 1 < parts.length) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

}
