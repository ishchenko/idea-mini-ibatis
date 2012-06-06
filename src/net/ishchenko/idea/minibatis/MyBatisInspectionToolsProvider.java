package net.ishchenko.idea.minibatis;

import com.intellij.codeInspection.InspectionToolProvider;
import net.ishchenko.idea.minibatis.inspections.mapper.NoMapperStatementInspection;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.06.12
 * Time: 11:38
 */
public class MyBatisInspectionToolsProvider implements InspectionToolProvider {

    @Override
    public Class[] getInspectionClasses() {
        return new Class[]{NoMapperStatementInspection.class};
    }

}
