package me.phoenixra.gtwclient.gui.factory;

import me.phoenixra.gtwclient.gui.GuiAction;
import me.phoenixra.gtwclient.gui.GuiID;
import me.phoenixra.gtwclient.gui.api.BaseGUI;
import me.phoenixra.gtwclient.gui.api.BaseGuiButton;
import me.phoenixra.gtwclient.gui.api.BaseGuiText;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.networking.gui.PacketGUIAction;
import net.minecraft.util.ResourceLocation;

public class FactoryUpgradeGUI extends BaseGUI {

    protected FactoryUpgradeGUI(FactoryGUI parent) {

        super(
                parent,
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
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
                        "gtwclient:textures/gui/factories/"+
                                "button_back_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_back_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
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
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_production_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_production_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
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
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_storage_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_storage_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_storage_click.png"
                )
                ).addActionOnClick(this::onStorageUpgradeClick)
        );

        //TEXT PRODUCTION PRICE
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("productionUpgradePrice"),
                HudElement.COLOR_WHITE,
                93,
                75
        ));
        //TEXT STORAGE PRICE
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("storageUpgradePrice"),
                HudElement.COLOR_WHITE,
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
                new PacketGUIAction(
                        mc.player.getUniqueID().toString(),
                        getId(),
                        GuiAction.FACTORY_UPGRADE_PRODUCTION
                )
        );
    }
    private void onStorageUpgradeClick(){
        sendGuiActionPacket(
                new PacketGUIAction(
                        mc.player.getUniqueID().toString(),
                        getId(),
                        GuiAction.FACTORY_UPGRADE_STORAGE
                )
        );
    }
}