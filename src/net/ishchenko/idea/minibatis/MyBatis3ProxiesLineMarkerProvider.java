package net.ishchenko.idea.minibatis;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Function;
import com.intellij.util.NullableFunction;
import com.intellij.util.xml.DomElement;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.01.12
 * Time: 15:45
 */
public class MyBatis3ProxiesLineMarkerProvider implements LineMarkerProvider {

    private static final Icon navigateToDeclarationIcon = IconLoader.getIcon("/gutter/implementedMethod.png");

    @Override
    public LineMarkerInfo getLineMarkerInfo(PsiElement element) {

        if (element instanceof PsiNameIdentifierOwner) {

            DomFileElementsFinder finder = ServiceManager.getService(element.getProject(), DomFileElementsFinder.class);
            CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<DomElement>();

            if (element instanceof PsiClass) {
                finder.processMappers((PsiClass) element, processor);
            } else if (element instanceof PsiMethod) {
                finder.processMapperStatements((PsiMethod) element, processor);
            }

            PsiElement nameIdentifier = ((PsiNameIdentifierOwner) element).getNameIdentifier();
            if (processor.isFound() && nameIdentifier != null) {
                return new LineMarkerInfo<PsiIdentifier>(
                        (PsiIdentifier) nameIdentifier, //we just know it, ok
                        nameIdentifier.getTextRange(),
                        navigateToDeclarationIcon,
                        Pass.UPDATE_ALL,
                        getTooltipProviderForMapperReference(processor.getFoundValue()),
                        getNavigationHandler(processor.getFoundValue().getXmlElement()),
                        GutterIconRenderer.Alignment.CENTER
                );
            }
        }

        return null;

    }

    @Override
    public void collectSlowLineMarkers(List<PsiElement> elements, Collection<LineMarkerInfo> result) {

    }

    private Function<PsiIdentifier, String> getTooltipProviderForMapperReference(final DomElement element) {

        return new NullableFunction<PsiIdentifier, String>() {
            @Override
            public String fun(PsiIdentifier psiIdentifier) {
                XmlElement xmlElement = element.getXmlElement();
                if (xmlElement != null) {
                    return element.getXmlElementName() + " in " + xmlElement.getContainingFile().getName();
                } else {
                    return null;
                }
            }
        };

    }

    private GutterIconNavigationHandler<PsiIdentifier> getNavigationHandler(final XmlElement statement) {
        return new GutterIconNavigationHandler<PsiIdentifier>() {
            @Override
            public void navigate(MouseEvent e, PsiIdentifier elt) {
                if (statement instanceof Navigatable) {
                    ((Navigatable) statement).navigate(true);
                } else {
                    throw new AssertionError("Could not navigate statement " + statement);
                }
            }
        };
    }

}
