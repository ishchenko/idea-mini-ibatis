package net.ishchenko.idea.minibatis;

import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileDescription;
import net.ishchenko.idea.minibatis.model.mapper.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Max
 * Date: 01.01.12
 * Time: 18:38
 */
public class MapperDescription extends DomFileDescription<Mapper> {

    public MapperDescription() {
        super(Mapper.class, "mapper");
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        //todo: maybe use namespace policy?
        XmlTag rootTag = file.getRootTag();
        return rootTag != null && rootTag.getName().equals(getRootTagName());
    }

}
