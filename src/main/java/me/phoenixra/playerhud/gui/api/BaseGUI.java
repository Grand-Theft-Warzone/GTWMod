package me.phoenixra.playerhud.gui.api;


import me.phoenixra.playerhud.gui.GuiSession;
import me.phoenixra.playerhud.networking.gui.PacketGUIAction;
import me.phoenixra.playerhud.proxy.ClientProxy;
import me.phoenixra.playerhud.proxy.CommonProxy;
import me.phoenixra.playerhud.utils.MathUtils;
import me.phoenixra.playerhud.utils.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.renderer.GlStateManager.*;
public abstract class BaseGUI extends GuiScreen {
    protected final BaseGUI parent;
    protected BaseGUI child;

    private final GuiSession guiSession;

    protected ResourceLocation GUI_IMAGE_MAIN;


    protected int sizeX;
    protected int sizeY;


    protected List<BaseGuiButton> guiButtonList = new ArrayList<>();
    protected List<BaseGuiText> textList = new ArrayList<>();
    protected List<BaseGuiBar> barList = new ArrayList<>();

    private boolean initializedMouse;

    protected BaseGUI(BaseGUI parent,
                      GuiSession guiSession,
                      ResourceLocation mainImage,
                      int sizeX,
                      int sizeY
    ){
        GUI_IMAGE_MAIN = mainImage;
        this.guiSession = guiSession;

        this.sizeX = sizeX;
        this.sizeY = sizeY;;

        this.parent = parent;

        mc = Minecraft.getMinecraft();
    }
    protected BaseGUI(BaseGUI parent,
                      ResourceLocation mainImage,
                      int sizeX,
                      int sizeY
    ){
        this(parent, parent.guiSession, mainImage,sizeX,sizeY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(child != null){
            child.drawScreen(mouseX,mouseY,partialTicks);
            return;
        }else {
            if(parent==null&&!initializedMouse){
                Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
                initializedMouse = true;
            }
        }

        int x;
        int y;
        if(mc.displayWidth>640&&mc.displayHeight>480){
            x = mc.displayWidth/4-sizeX/2;
            y = mc.displayHeight/4-sizeY/2;
        }else{
            x = mc.displayWidth/2-sizeX/2;
            y = mc.displayHeight/2-sizeY/2;
        }
        mc.getTextureManager().bindTexture(GUI_IMAGE_MAIN);
        drawTexturedModalRect(
                x,
                y,
                0,
                0,
                sizeX,
                sizeY
        );

        for(BaseGuiText text : textList){
            text.drawText(mc,x,y);
        }
        for(BaseGuiBar bar : barList){
            bar.drawBar(x,y);
        }
        for(BaseGuiButton button : guiButtonList){
            button.guiX = x;
            button.guiY = y;
            if(button.pressed){
                button.currentTexture = button.BUTTON_IMAGE_CLICKED;
            }else{
                if(button.isMouseOver()){
                    button.currentTexture = button.BUTTON_IMAGE_HOVERED;
                }else {
                    button.currentTexture = button.BUTTON_IMAGE_DEFAULT;
                }
            }
        }



        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        pushMatrix();
        color(1.0F, 1.0F, 1.0F, 1.0F);
        super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
        popMatrix();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        ClientProxy.playerData.setOpenedGui(null);
        try {
            sendGuiActionPacket(
                    new PacketGUIAction(
                            mc.player.getUniqueID().toString(),
                            getId(),
                            -1
                    )
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void reload(){
        guiButtonList.clear();
        buttonList.clear();
        textList.clear();
        barList.clear();
        child = null;
        initGui();
    }

    public BaseGUI addBaseButton(BaseGuiButton button){
        guiButtonList.add(button);
        addButton(button);
        return this;
    }
    public BaseGUI addGuiText(BaseGuiText text){
        textList.add(text);
        return this;
    }
    public BaseGUI addGuiBar(BaseGuiBar bar){
        barList.add(bar);
        return this;
    }

    public void openChildGui(BaseGUI gui){
        guiButtonList.clear();
        textList.clear();
        buttonList.clear();
        barList.clear();
        gui.initGui();
        buttonList.addAll(gui.guiButtonList);
        child = gui;

    }
    public void closeChildGui(){
        guiButtonList.clear();
        textList.clear();
        buttonList.clear();
        barList.clear();
        child = null;
        initGui();
    }



    protected void sendGuiActionPacket(PacketGUIAction packet){
        CommonProxy.NETWORK_CHANNEL.sendToServer(packet);
    }
    public GuiSession getGuiSession() {
        return guiSession;
    }
    public abstract int getId();
}
