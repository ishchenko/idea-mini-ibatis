package net.ishchenko.idea.minibatis;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.model.impl.DomModelFactory;
import net.ishchenko.idea.minibatis.model.SqlMap;
import net.ishchenko.idea.minibatis.model.SqlMapModel;
import net.ishchenko.idea.minibatis.model.SqlMapModelImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.12.11
 * Time: 0:12
 */
public class SqlMapModelFactory extends DomModelFactory<SqlMap, SqlMapModel, PsiElement> {

    public SqlMapModelFactory(Project project) {
        super(SqlMap.class, project, "sqlMapModelFactory");
    }

    @Override
    protected List<SqlMapModel> computeAllModels(@NotNull final Module scope) {

        final List<SqlMapModel> result = new ArrayList<SqlMapModel>();

        for (VirtualFile sourceRoot : ProjectRootManager.getInstance(getProject()).getContentSourceRoots()) {
            PsiDirectory directory = PsiManager.getInstance(getProject()).findDirectory(sourceRoot);
            if (directory != null) {
                directory.accept(new XmlRecursiveElementVisitor() {
                    @Override
                    public void visitXmlFile(XmlFile file) {
                        DomFileElement fileElement = DomManager.getDomManager(getProject()).getFileElement(file, DomElement.class);
                        if (fileElement != null && fileElement.getRootElement() instanceof SqlMap) {
                            result.add(new SqlMapModelImpl(Collections.singleton(file), getProject()));
                        }
                    }
                });
            }
        }

        return result;

    }

    @Override
    protected SqlMapModel createCombinedModel(@NotNull Set<XmlFile> configFiles, @NotNull DomFileElement<SqlMap> mergedModel, SqlMapModel firstModel, Module scope) {
        return new SqlMapModelImpl(configFiles, getProject());
    }
}
