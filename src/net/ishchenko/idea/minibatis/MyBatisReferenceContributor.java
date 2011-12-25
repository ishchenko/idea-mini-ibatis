package net.ishchenko.idea.minibatis;

import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 17.12.11
 * Time: 20:36
 */
public class MyBatisReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {

        registrar.registerReferenceProvider(PsiJavaPatterns.psiLiteral(), new PsiReferenceProvider() {

            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

                if (!(element instanceof PsiLiteralExpression)) {
                    return PsiReference.EMPTY_ARRAY;
                }

                PsiElement parent = element.getParent().getParent();
                if (parent == null || !(parent instanceof PsiMethodCallExpression)) {
                    return PsiReference.EMPTY_ARRAY;
                }

                return new PsiReference[]{new IdentifiableStatementReference((PsiLiteralExpression) element)};

            }

        });

    }

}

