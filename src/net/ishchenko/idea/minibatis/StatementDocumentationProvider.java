package net.ishchenko.idea.minibatis;

import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMapIdentifiableStatement;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.02.12
 * Time: 22:01
 */
public class StatementDocumentationProvider implements DocumentationProvider {

    private static final Pattern dotPattern = Pattern.compile("\\.");

    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Override
    public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
        return null;
    }

    @Override
    public String generateDoc(PsiElement element, PsiElement originalElement) {

        if (originalElement instanceof PsiJavaToken) {

            String rawText = originalElement.getText();
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

            DomFileElementsFinder finder = ServiceManager.getService(originalElement.getProject(), DomFileElementsFinder.class);
            CommonProcessors.FindFirstProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.FindFirstProcessor<SqlMapIdentifiableStatement>();
            finder.processSqlMapStatements(namespace, id, processor);
            if (processor.isFound()) {
                SqlMapIdentifiableStatement statement = processor.getFoundValue();
                XmlElement xmlElement = statement.getXmlElement();
                if (xmlElement != null) {
                    return "<pre>" + StringUtil.escapeXml(xmlElement.getText()) + "</pre>";
                }
            }

            return null;

        }
        return null;
    }

    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
        return null;
    }

    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
        return null;
    }

}
