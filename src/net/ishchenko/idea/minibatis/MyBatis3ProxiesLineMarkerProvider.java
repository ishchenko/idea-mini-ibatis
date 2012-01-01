package net.ishchenko.idea.minibatis;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlElement;
import com.intellij.util.Function;
import com.intellij.util.NullableFunction;
import com.intellij.util.xml.DomFileElement;
import net.ishchenko.idea.minibatis.model.IdentifiableStatement;
import net.ishchenko.idea.minibatis.model.Mapper;

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

        if (element instanceof PsiClass) {
            return getLineMarkerForClass((PsiClass) element);
        } else if (element instanceof PsiMethod) {
            return getLineMarkerForMethod((PsiMethod) element);
        }
        return null;

    }

    @Override
    public void collectSlowLineMarkers(List<PsiElement> elements, Collection<LineMarkerInfo> result) {

    }

    private LineMarkerInfo getLineMarkerForClass(PsiClass clazz) {

        PsiIdentifier nameIdentifier = clazz.getNameIdentifier();
        String qualifiedName = clazz.getQualifiedName();
        if (nameIdentifier != null && qualifiedName != null) {
            for (DomFileElement<Mapper> fileElement : ServiceManager.getService(clazz.getProject(), DomFileElementsFinder.class).findMapperFileElements()) {
                Mapper mapper = fileElement.getRootElement();
                if (qualifiedName.equals(mapper.getNamespace().getRawText())) {
                    return new LineMarkerInfo<PsiIdentifier>(
                            nameIdentifier,
                            nameIdentifier.getTextRange(),
                            navigateToDeclarationIcon,
                            Pass.UPDATE_ALL,
                            getTooltipProviderForMapperReference(mapper),
                            getNavigationHandler(mapper.getXmlElement()),
                            GutterIconRenderer.Alignment.CENTER
                    );

                }
            }
        }

        return null;

    }

    private LineMarkerInfo getLineMarkerForMethod(PsiMethod method) {

        PsiIdentifier nameIdentifier = method.getNameIdentifier();
        PsiClass clazz = method.getContainingClass();
        if (clazz != null && nameIdentifier != null) {

            String qualifiedName = clazz.getQualifiedName();
            String methodName = method.getName();
            if (qualifiedName != null) {

                for (DomFileElement<Mapper> fileElement : ServiceManager.getService(method.getProject(), DomFileElementsFinder.class).findMapperFileElements()) {
                    Mapper mapper = fileElement.getRootElement();
                    if (qualifiedName.equals(mapper.getNamespace().getRawText())) {
                        for (IdentifiableStatement statement : mapper.getIdentifiableStatements()) {
                            if (methodName.equals(statement.getId().getRawText())) {
                                return new LineMarkerInfo<PsiIdentifier>(
                                        nameIdentifier,
                                        nameIdentifier.getTextRange(),
                                        navigateToDeclarationIcon,
                                        Pass.UPDATE_ALL,
                                        getTooltipProviderForStatementReference(statement),
                                        getNavigationHandler(statement.getXmlElement()),
                                        GutterIconRenderer.Alignment.CENTER
                                );
                            }
                        }
                    }
                }
            }
        }
        return null;

    }

    private Function<PsiIdentifier, String> getTooltipProviderForMapperReference(final Mapper mapper) {

        return new NullableFunction<PsiIdentifier, String>() {
            @Override
            public String fun(PsiIdentifier psiIdentifier) {
                XmlElement xmlElement = mapper.getXmlElement();
                if (xmlElement != null) {
                    return mapper.getNamespace().getRawText() + " in " + xmlElement.getContainingFile().getName();
                } else {
                    return null;
                }
            }
        };

    }

    private Function<PsiIdentifier, String> getTooltipProviderForStatementReference(final IdentifiableStatement statement) {

        return new NullableFunction<PsiIdentifier, String>() {
            @Override
            public String fun(PsiIdentifier psiIdentifier) {
                XmlElement xmlElement = statement.getXmlElement();
                if (xmlElement != null) {
                    return statement.getXmlElementName() + " in " + xmlElement.getContainingFile().getName();
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
