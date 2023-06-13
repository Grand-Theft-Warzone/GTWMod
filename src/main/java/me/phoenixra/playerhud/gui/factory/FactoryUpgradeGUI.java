package me.phoenixra.playerhud.gui.factory;

import me.phoenixra.playerhud.gui.GuiAction;
import me.phoenixra.playerhud.gui.GuiID;
import me.phoenixra.playerhud.gui.GuiSession;
import me.phoenixra.playerhud.gui.api.BaseGUI;
import me.phoenixra.playerhud.gui.api.BaseGuiButton;
import me.phoenixra.playerhud.gui.api.BaseGuiText;
import me.phoenixra.playerhud.hud.HudElement;
import me.phoenixra.playerhud.networking.gui.PacketGUIAction;
import net.minecraft.util.ResourceLocation;

public class FactoryUpgradeGUI extends BaseGUI {

    protected FactoryUpgradeGUI(FactoryGUI parent) {

        super(
                parent,
                new ResourceLocation(
                        "playerhud:textures/gui/factories/"+
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
                        "playerhud:textures/gui/factories/"+
                                "button_back_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "playerhud:textures/gui/factories/"+
                                "button_back_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "playerhud:textures/gui/factories/"+
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
                        "playerhud:textures/gui/factories/"+
                                "button_upgrade_production_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "playerhud:textures/gui/factories/"+
                                "button_upgrade_production_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "playerhud:textures/gui/factories/"+
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
                        "playerhud:textures/gui/factories/"+
                                "button_upgrade_storage_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "playerhud:textures/gui/factories/"+
                                "button_upgrade_storage_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "playerhud:textures/gui/factories/"+
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