package net.ishchenko.idea.minibatis.model.sqlmap;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.xml.*;
import net.ishchenko.idea.minibatis.ResultMapReference;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 10.04.12
 * Time: 22:53
 */
public interface Result extends DomElement {

    @NameValue
    @Attribute("property")
    GenericAttributeValue<String> getProperty();

    @NameValue
    @Attribute("typeHandler")
    GenericAttributeValue<TypeAlias> getTypeHandler();

    @Attribute("resultMap")
    @Referencing(ResultMapReferenceConverter.class)
    GenericAttributeValue<ResultMap> getResultMap();

    class ResultMapReferenceConverter implements CustomReferenceConverter {

        @NotNull
        @Override
        public PsiReference[] createReferences(GenericDomValue genericDomValue, PsiElement element, ConvertContext context) {
            return new PsiReference[]{new ResultMapReference(element)};
        }

    }

}
