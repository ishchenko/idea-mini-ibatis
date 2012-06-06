package net.ishchenko.idea.minibatis.inspections.mapper;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.06.12
 * Time: 11:47
 */
public abstract class MyBatisInspection extends BaseJavaLocalInspectionTool {

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "iBATIS/MyBatis issues";
    }

    @NotNull
    @Override
    public String getShortName() {
        return this.getClass().getName();
    }

}
