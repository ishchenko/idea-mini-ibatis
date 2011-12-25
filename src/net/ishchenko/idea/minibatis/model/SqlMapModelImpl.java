package net.ishchenko.idea.minibatis.model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.model.impl.DomModelImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 24.12.11
 * Time: 1:05
 */
public class SqlMapModelImpl extends DomModelImpl<SqlMap> implements SqlMapModel {

    public SqlMapModelImpl(@NotNull Set<XmlFile> configFiles, Project project) {
        super(configFiles, SqlMap.class, project);
    }
}
