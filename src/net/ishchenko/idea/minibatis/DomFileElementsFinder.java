package net.ishchenko.idea.minibatis;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import net.ishchenko.idea.minibatis.model.mapper.Mapper;
import net.ishchenko.idea.minibatis.model.mapper.MapperIdentifiableStatement;
import net.ishchenko.idea.minibatis.model.sqlmap.ResultMap;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMap;
import net.ishchenko.idea.minibatis.model.sqlmap.SqlMapIdentifiableStatement;
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
    private final Application application;

    public DomFileElementsFinder(Project project, DomService domService, Application application) {
        this.project = project;
        this.domService = domService;
        this.application = application;
    }

    public void processSqlMapStatements(@NotNull String targetNamespace, @NotNull String targetId, @NotNull Processor<? super SqlMapIdentifiableStatement> processor) {

        nsloop:
        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap sqlMap = fileElement.getRootElement();
            String namespace = sqlMap.getNamespace().getRawText();
            if (targetNamespace.equals(namespace) || targetNamespace.length() == 0 && namespace == null) {
                for (SqlMapIdentifiableStatement statement : sqlMap.getIdentifiableStatements()) {
                    if (targetId.equals(statement.getId().getRawText())) {
                        if (!processor.process(statement)) {
                            return;
                        }
                        continue nsloop;
                    }
                }
            }
        }

    }

    public void processSqlMapStatementNames(@NotNull Processor<String> processor) {

        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap rootElement = fileElement.getRootElement();
            String namespace = rootElement.getNamespace().getRawText();
            for (SqlMapIdentifiableStatement statement : rootElement.getIdentifiableStatements()) {
                String id = statement.getId().getRawText();
                if (id != null && (namespace != null && !processor.process(namespace + "." + id) || namespace == null && !processor.process(id))) {
                    return;
                }
            }
        }

    }

    public void processResultMaps(@NotNull String targetNamespace, @NotNull String targetId, @NotNull Processor<? super ResultMap> processor) {

        nsloop:
        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap rootElement = fileElement.getRootElement();
            String namespace = rootElement.getNamespace().getRawText();
            if (targetNamespace.equals(namespace) || targetNamespace.length() == 0 && namespace == null) {
                for (ResultMap resultMap : rootElement.getResultMaps()) {
                    if (targetId.equals(resultMap.getId().getRawText())) {
                        if (!processor.process(resultMap)) {
                            return;
                        }
                        continue nsloop;
                    }
                }
            }

        }

    }

    public void processResultMapNames(@NotNull Processor<String> processor) {

        for (DomFileElement<SqlMap> fileElement : findSqlMapFileElements()) {
            SqlMap rootElement = fileElement.getRootElement();
            String namespace = rootElement.getNamespace().getRawText();
            for (ResultMap resultMap : rootElement.getResultMaps()) {
                String id = resultMap.getId().getRawText();
                if (id != null && (namespace != null && !processor.process(namespace + "." + id) || namespace == null && !processor.process(id))) {
                    return;
                }
            }
        }

    }

    public void processMappers(@NotNull final PsiClass clazz, @NotNull final Processor<? super Mapper> processor) {
        application.runReadAction(new Runnable() {
            @Override
            public void run() {
                if (clazz.isInterface()) {
                    PsiIdentifier nameIdentifier = clazz.getNameIdentifier();
                    String qualifiedName = clazz.getQualifiedName();
                    if (nameIdentifier != null && qualifiedName != null) {
                        processMappers(qualifiedName, processor);
                    }
                }
            }
        });
    }

    public void processMapperStatements(@NotNull final PsiMethod method, @NotNull final Processor<? super MapperIdentifiableStatement> processor) {

        application.runReadAction(new Runnable() {
            @Override
            public void run() {
                PsiClass clazz = method.getContainingClass();
                if (clazz != null && clazz.isInterface()) {
                    String qualifiedName = clazz.getQualifiedName();
                    String methodName = method.getName();
                    if (qualifiedName != null) {
                        processMapperStatements(qualifiedName, methodName, processor);
                    }
                }
            }
        });

    }

    private void processMappers(String className, Processor<? super Mapper> processor) {
        for (DomFileElement<Mapper> fileElement : findMapperFileElements()) {
            Mapper mapper = fileElement.getRootElement();
            if (className.equals(mapper.getNamespace().getRawText())) {
                if (!processor.process(mapper)) {
                    return;
                }
            }
        }
    }

    private void processMapperStatements(String className, String methodName, Processor<? super MapperIdentifiableStatement> processor) {
        for (DomFileElement<Mapper> fileElement : findMapperFileElements()) {
            Mapper mapper = fileElement.getRootElement();
            if (className.equals(mapper.getNamespace().getRawText())) {
                for (MapperIdentifiableStatement statement : mapper.getIdentifiableStatements()) {
                    if (methodName.equals(statement.getId().getRawText())) {
                        if (!processor.process(statement)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private List<DomFileElement<SqlMap>> findSqlMapFileElements() {
        return domService.getFileElements(SqlMap.class, project, GlobalSearchScope.allScope(project));
    }

    private List<DomFileElement<Mapper>> findMapperFileElements() {
        return domService.getFileElements(Mapper.class, project, GlobalSearchScope.allScope(project));
    }

}


