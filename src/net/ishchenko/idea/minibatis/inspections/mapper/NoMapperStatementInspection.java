package net.ishchenko.idea.minibatis.inspections.mapper;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import net.ishchenko.idea.minibatis.DomFileElementsFinder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 02.06.12
 * Time: 11:39
 */
public class NoMapperStatementInspection extends MyBatisInspection {

    @Override
    public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
        PsiClass containingClass = method.getContainingClass();
        if (containingClass != null && containingClass.isInterface()) {

            PsiIdentifier methodName = method.getNameIdentifier();
            DomFileElementsFinder finder = ServiceManager.getService(containingClass.getProject(), DomFileElementsFinder.class);
            boolean existsMapperStatement = finder.existsMapperStatement(method);

            boolean containsIbatisAnnotation = false;
            for (PsiAnnotation annotation : method.getModifierList().getAnnotations()) {
                String annotationName = annotation.getQualifiedName();
                if (annotationName != null && annotationName.startsWith("org.apache.ibatis.annotations")) {
                    containsIbatisAnnotation = true;
                    break;
                }
            }

            if (!existsMapperStatement && !containsIbatisAnnotation && methodName != null) {
                return new ProblemDescriptor[]{
                        manager.createProblemDescriptor(methodName, "Statement with id=\"#ref\" not defined in mapper xml", (LocalQuickFix) null, ProblemHighlightType.GENERIC_ERROR, isOnTheFly)
                };
            }
        }
        return null;
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Mapper interface method not defined in xml";
    }

}
