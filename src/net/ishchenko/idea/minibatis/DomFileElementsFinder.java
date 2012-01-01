package net.ishchenko.idea.minibatis;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import net.ishchenko.idea.minibatis.model.Mapper;
import net.ishchenko.idea.minibatis.model.SqlMap;

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

    public List<DomFileElement<Mapper>> findMapperFileElements() {
        return domService.getFileElements(Mapper.class, project, GlobalSearchScope.allScope(project));
    }

}
