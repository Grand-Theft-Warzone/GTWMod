package com.grandtheftwarzone.gtwmod.core.display.gui.factory;

import com.grandtheftwarzone.gtwmod.api.gui.GuiAction;
import com.grandtheftwarzone.gtwmod.core.display.gui.GuiID;
import com.grandtheftwarzone.gtwmod.core.display.gui.api.BaseGUI;
import com.grandtheftwarzone.gtwmod.core.display.gui.api.BaseGuiButton;
import com.grandtheftwarzone.gtwmod.core.display.gui.api.BaseGuiText;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import net.minecraft.util.ResourceLocation;

public class FactoryUpgradeGUI extends BaseGUI {

    protected FactoryUpgradeGUI(FactoryGUI parent) {
        super(
                parent,
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                parent.getFactoryType()+"/image_upgrade.png"
                ),
                247,
                247
        );
    }
    @Override
    public void initGui() {
        addBaseButton(new BaseGuiButton(
                        this,
                        1,
                        220,
                        5,
                        23,
                        26
                ).setImageDefault(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_back_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_back_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_back_click.png"
                )
                ).addActionOnClick(parent::closeChildGui)
        );

        addBaseButton(new BaseGuiButton(
                        this,
                        2,
                        20,
                        200,
                        94,
                        22
                ).setImageDefault(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_upgrade_production_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_upgrade_production_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_upgrade_production_click.png"
                )
                ).addActionOnClick(this::onProductionUpgradeClick)
        );
        addBaseButton(new BaseGuiButton(
                        this,
                        3,
                        132,
                        200,
                        94,
                        22
                ).setImageDefault(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_upgrade_storage_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_upgrade_storage_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwmod:textures/gui/factories/"+
                                "button_upgrade_storage_click.png"
                )
                ).addActionOnClick(this::onStorageUpgradeClick)
        );
        //TEXT PRODUCTION PRICE
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("productionUpgradePrice"),
                AtumColor.WHITE.toInt(),
                93,
                75
        ));
        //TEXT STORAGE PRICE
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("storageUpgradePrice"),
                AtumColor.WHITE.toInt(),
                105,
                108
        ));
    }

    @Override
    public int getId() {
        return GuiID.FACTORY;
    }



    private void onProductionUpgradeClick(){
        sendGuiActionPacket(
                mc.player.getUniqueID(),
                getId(),
                GuiAction.FACTORY_UPGRADE_PRODUCTION
        );
    }
    private void onStorageUpgradeClick(){
        sendGuiActionPacket(
                mc.player.getUniqueID(),
                getId(),
                GuiAction.FACTORY_UPGRADE_STORAGE
        );
    }
}