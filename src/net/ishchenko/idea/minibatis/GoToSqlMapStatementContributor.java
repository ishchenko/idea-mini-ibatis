package net.ishchenko.idea.minibatis;

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.ElementPresentationManager;
import com.intellij.util.xml.model.gotosymbol.GoToSymbolProvider;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMapIdentifiableStatement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Max
 * Date: 02.09.13
 * Time: 21:12
 */
public class GoToSqlMapStatementContributor extends GoToSymbolProvider {

    private static final Pattern dotPattern = Pattern.compile("\\.");

    @Override
    protected void addNames(@NotNull Module module, Set<String> result) {
        ServiceManager.getService(module.getProject(), DomFileElementsFinder.class).processSqlMapStatementNames(new CommonProcessors.CollectProcessor<String>(result));
    }

    @Override
    protected void addItems(@NotNull Module module, String name, List<NavigationItem> result) {
        String[] parts = dotPattern.split(name);
        if (parts.length == 1) {
            result.addAll(findResults("", parts[0], module.getProject()));
        } else {
            for (int i = 0; i < parts.length - 1; i++) {
                result.addAll(findResults(concatBefore(parts, i), concatAfter(parts, i + 1), module.getProject()));
            }
        }
    }

    @Override
    protected boolean acceptModule(Module module) {
        return true;
    }

    private List<NavigationItem> findResults(String namespace, String id, Project project) {

        CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement> processor = new CommonProcessors.CollectUniquesProcessor<SqlMapIdentifiableStatement>();
        ServiceManager.getService(project, DomFileElementsFinder.class).processSqlMapStatements(namespace, id, processor);
        Collection<SqlMapIdentifiableStatement> processorResults = processor.getResults();
        final List<NavigationItem> results = new ArrayList<NavigationItem>(processorResults.size());
        for (SqlMapIdentifiableStatement statement : processorResults) {
            XmlElement psiElement = statement.getId().getXmlElement();
            String value = statement.getId().getStringValue();
            if (psiElement != null && value != null) {
                final Icon icon = ElementPresentationManager.getIcon(statement);
                if (namespace.length() > 0) {
                    results.add(new BaseNavigationItem(psiElement, namespace + "." + value, icon));
                } else {
                    results.add(new BaseNavigationItem(psiElement, value, icon));
                }
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
