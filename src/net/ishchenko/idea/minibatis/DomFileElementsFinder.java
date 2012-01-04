package net.ishchenko.idea.minibatis;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import net.ishchenko.idea.minibatis.model.IdentifiableStatement;
import net.ishchenko.idea.minibatis.model.Mapper;
import net.ishchenko.idea.minibatis.model.SqlMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.01.12
 * Time: 16:03
 */
public class DomFileElementsFinder {

    private final Project project;
    private final DomService domService;

    public DomFileElementsFinder(Project project, DomService domService) {
        this.project = project;
        this.domService = domService;
    }

    public List<DomFileElement<SqlMap>> findSqlMapFileElements() {
        return domService.getFileElements(SqlMap.class, project, GlobalSearchScope.allScope(project));
    }

    private List<DomFileElement<Mapper>> findMapperFileElements() {
        return domService.getFileElements(Mapper.class, project, GlobalSearchScope.allScope(project));
    }

    public void processMappers(@NotNull PsiClass clazz, @NotNull Processor<? super Mapper> processor) {
        if (clazz.isInterface()) {
            PsiIdentifier nameIdentifier = clazz.getNameIdentifier();
            String qualifiedName = clazz.getQualifiedName();
            if (nameIdentifier != null && qualifiedName != null) {
                for (DomFileElement<Mapper> fileElement : findMapperFileElements()) {
                    Mapper mapper = fileElement.getRootElement();
                    if (qualifiedName.equals(mapper.getNamespace().getRawText())) {
                        if (!processor.process(mapper)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    public void processMapperStatements(@NotNull PsiMethod method, @NotNull Processor<? super IdentifiableStatement> processor) {

        PsiClass clazz = method.getContainingClass();
        if (clazz != null && clazz.isInterface()) {

            String qualifiedName = clazz.getQualifiedName();
            String methodName = method.getName();
            if (qualifiedName != null) {
                for (DomFileElement<Mapper> fileElement : findMapperFileElements()) {
                    Mapper mapper = fileElement.getRootElement();
                    if (qualifiedName.equals(mapper.getNamespace().getRawText())) {
                        for (IdentifiableStatement statement : mapper.getIdentifiableStatements()) {
                            if (methodName.equals(statement.getId().getRawText())) {
                                if (!processor.process(statement)) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}