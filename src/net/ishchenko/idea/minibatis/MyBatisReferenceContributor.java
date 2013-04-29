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
                return new PsiReference[]{new IdentifiableStatementReference((PsiLiteral) element), new SqlMapReference((PsiLiteral) element)};
            }
        });

    }

}

