package me.phoenixra.gtwclient.gui.factory;


import me.phoenixra.gtwclient.gui.GuiAction;
import me.phoenixra.gtwclient.gui.GuiID;
import me.phoenixra.gtwclient.gui.GuiSession;
import me.phoenixra.gtwclient.gui.api.BaseGUI;
import me.phoenixra.gtwclient.gui.api.BaseGuiBar;
import me.phoenixra.gtwclient.gui.api.BaseGuiButton;
import me.phoenixra.gtwclient.gui.api.BaseGuiText;
import me.phoenixra.gtwclient.playerhud.HudElement;
import me.phoenixra.gtwclient.networking.gui.PacketGUIAction;
import net.minecraft.util.ResourceLocation;


public class FactoryGUI extends BaseGUI {


    private final String factoryType;
    public FactoryGUI(String factoryType, GuiSession guiSession){
        super(
                null,
                guiSession,
                new ResourceLocation(
                "gtwclient:textures/gui/factories/"+factoryType+"/image_main.png"
                ),
                248,
                248
        );
        this.factoryType = factoryType;

    }
    @Override
    public void initGui() {
        if(child!=null){
            openChildGui(child);
            return;
        }

        //TEXT OWNER
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("factoryOwner"),
                HudElement.COLOR_WHITE,
                129,
                54
        ));
        //TEXT LEVEL
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("level"),
                HudElement.COLOR_WHITE,
                129,
                81
        ));
        //TEXT PRODUCTION INFO
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("productionInfo"),
                HudElement.COLOR_WHITE,
                55,
                152
        ));
        //TEXT STORAGE INFO
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("storageInfo"),
                HudElement.COLOR_WHITE,
                182,
                152
        ));
        //TEXT PRODUCTION EFFICIENCY
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("productionEfficiency"),
                HudElement.COLOR_WHITE,
                55,
                172
        ));
        //TEXT STORAGE EFFICIENCY
        addGuiText(new BaseGuiText(
                this,
                () -> getGuiSession().getData("storageEfficiency"),
                HudElement.COLOR_WHITE,
                182,
                172
        ));

        double upgradeDelay = getDelayPercentage();
        if(upgradeDelay>0){
            addGuiBar(new BaseGuiBar(
                    this,
                    this::getDelayPercentage,
                    0xAEED7A,
                    true,
                    50,220,
                    150,
                    10
            ));
            return;
        }
        //COLLECT BUTTON
       addBaseButton(new BaseGuiButton(
               this,
               1,
               40,
               221,
               60,
               20
               ).setImageDefault(
                       new ResourceLocation(
                               "gtwclient:textures/gui/factories/"+
                               "button_collect_default.png"
                       )
               ).setImageOnHover(
                       new ResourceLocation(
                               "gtwclient:textures/gui/factories/"+
                               "button_collect_hover.png"
                       )
               ).setImageOnClick(
                       new ResourceLocation(
                               "gtwclient:textures/gui/factories/"+
                               "button_collect_click.png"
                       )
               ).addActionOnClick(this::collectButtonClicked)
       );
       //UPGRADE BUTTON
        addBaseButton(new BaseGuiButton(
                        this,
                        2,
                        104,
                        220,
                        52,
                        22
                ).setImageDefault(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_upgrade_click.png"
                )
                ).addActionOnClick(this::upgradeButtonClicked)
        );

        //CLAIM BUTTON
        addBaseButton(new BaseGuiButton(
                        this,
                        3,
                        160,
                        220,
                        52,
                        22
                ).setImageDefault(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_claim_default.png"
                )
                ).setImageOnHover(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_claim_hover.png"
                )
                ).setImageOnClick(
                new ResourceLocation(
                        "gtwclient:textures/gui/factories/"+
                                "button_claim_click.png"
                )
                ).addActionOnClick(this::claimButtonClicked)
        );
    }


    private void collectButtonClicked(){
        sendGuiActionPacket(
                new PacketGUIAction(
                        mc.player.getUniqueID().toString(),
                        getId(),
                        GuiAction.FACTORY_COLLECT
                )
        );
    }
    private void upgradeButtonClicked(){
        openChildGui(new FactoryUpgradeGUI(this));
    }
    private void claimButtonClicked(){
        sendGuiActionPacket(
                new PacketGUIAction(
                        mc.player.getUniqueID().toString(),
                        getId(),
                        GuiAction.FACTORY_CLAIM
                )
        );
    }


    private double getDelayPercentage(){
        double value = Double.parseDouble(getGuiSession().getData("upgradeDelay"));
        return 100*value;
    }

    public String getFactoryType() {
        return factoryType;
    }

    @Override
    public int getId() {
        return GuiID.FACTORY;
    }


}
