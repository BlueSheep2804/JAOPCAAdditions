package io.github.bluesheep2804.jaopcaadditions.modules;

import com.mojang.logging.LogUtils;
import io.github.bluesheep2804.jaopcaadditions.recipes.ShapedRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.forms.IForm;
import thelm.jaopca.api.forms.IFormRequest;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.items.IItemInfo;
import thelm.jaopca.api.materials.IMaterial;
import thelm.jaopca.api.materials.MaterialType;
import thelm.jaopca.api.modules.IModule;
import thelm.jaopca.api.modules.IModuleData;
import thelm.jaopca.api.modules.JAOPCAModule;

import java.util.*;

@JAOPCAModule
public class GearsModule implements IModule {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final JAOPCAApi api = JAOPCAApi.instance();
    private final IForm gearForm = api.newForm(this, "gears", api.itemFormType()).setMaterialTypes(MaterialType.INGOTS);

    @Override
    public String getName() {
        return "additions_gears";
    }

    @Override
    public Set<MaterialType> getMaterialTypes() {
        return EnumSet.allOf(MaterialType.class);
    }

    @Override
    public List<IFormRequest> getFormRequests() {
        LOGGER.info(Collections.singletonList(this.api.newFormRequest(this, this.gearForm)).toString());
        return Collections.singletonList(this.api.newFormRequest(this, this.gearForm));
    }

    @Override
    public void onCommonSetup(IModuleData moduleData, FMLCommonSetupEvent event) {
        IMiscHelper miscHelper = api.miscHelper();
        for (IMaterial material : gearForm.getMaterials()) {
            ResourceLocation materialLocation = miscHelper.getTagLocation(material.getType().getFormName(), material.getName());
            IItemInfo gearInfo = api.itemFormType().getMaterialFormInfo(gearForm, material);
            api.registerRecipe(
                    new ResourceLocation("jaopcaadditions", "gears.from_material." + material.getName()),
                    new ShapedRecipeSerializer(
                            new String[]{
                                    " M ",
                                    "M M",
                                    " M "
                            },
                            Map.of(
                                    "M", materialLocation
                            )
                            ,gearInfo,1
                    )
            );
        }
    }
}
