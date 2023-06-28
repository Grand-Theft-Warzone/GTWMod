package me.phoenixra.gtwclient.mainmenu;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.networking.mainmenu.SocketConnectorPlayerInfo;
import me.phoenixra.gtwclient.proxy.ClientProxy;
import me.phoenixra.gtwclient.proxy.CommonProxy;
import me.phoenixra.gtwclient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServerDemo;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

public class CustomMainMenu extends GuiMainMenu {

    private static final ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/background.png");
    private static final ResourceLocation QUIT_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/quit_button.png");
    private static final ResourceLocation SETTINGS_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/settings_button.png");
    private static final ResourceLocation DISCORD_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/discord_button.png");
    private static final ResourceLocation WEBSITE_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/website_button.png");
    private static final ResourceLocation PLAY_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/play_button.png");
    private static final ResourceLocation SINGLEPLAYER_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/singleplayer_button.png");
    private static final ResourceLocation MULTIPLAYER_BUTTON_IMAGE = new ResourceLocation("gtwclient:textures/gui/main_menu/multiplayer_button.png");
    private final int SETTINGS_BUTTON_ID = 0;
    private final int SINGLEPLAYER_BUTTON_ID = 1;
    private final int MULTIPLAYER_BUTTON_ID = 2;
    private final int QUIT_BUTTON_ID = 4;

    private final int PLAY_BUTTON_ID = 100;
    private final int DISCORD_BUTTON_ID = 101;
    private final int WEBSITE_BUTTON_ID = 102;

    private final int TEXT_COLOR = 10636599;


    private final ArrayList<TextAdvanced> textList = new ArrayList<>();

    public CustomMainMenu() {
        super();
    }

    @Override
    public void initGui() {
        super.initGui();
        // Remove existing buttons (optional)
        this.buttonList.clear();
        this.textList.clear();

        this.buttonList.add(
                ButtonAdvanced.builder(QUIT_BUTTON_ID)
                        .x(0,20)
                        .y(height-50,-10)
                        .width(50)
                        .height(50)
                        .image(QUIT_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(SETTINGS_BUTTON_ID)
                        .x(width-50,-20)
                        .y(height-50,-12)
                        .width(50)
                        .height(50)
                        .image(SETTINGS_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(DISCORD_BUTTON_ID)
                        .x(width-50,-20)
                        .y(height-50,-55 - 12)
                        .width(50)
                        .height(50)
                        .image(DISCORD_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(WEBSITE_BUTTON_ID)
                        .x(width-50,-20)
                        .y(height-50,-110 -12)
                        .width(50)
                        .height(50)
                        .image(WEBSITE_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(PLAY_BUTTON_ID)
                        .x(width/2,-50)
                        .y(height-150)
                        .width(100)
                        .height(100)
                        .image(PLAY_BUTTON_IMAGE)
                        .build()
        );
        GuiButton buttonMultiplayer = ButtonAdvanced.builder(MULTIPLAYER_BUTTON_ID)
                .x(width/2 - 68,-100)
                .y(height - 60,-40)
                .width(75)
                .height(75)
                .image(MULTIPLAYER_BUTTON_IMAGE)
                .build();
        this.buttonList.add(
                ButtonAdvanced.builder(SINGLEPLAYER_BUTTON_ID)
                        .x(width/2 - 100,-100)
                        .y(height - 120,-40)
                        .width(85)
                        .height(85)
                        .image(SINGLEPLAYER_BUTTON_IMAGE)
                        .buttonUnder(buttonMultiplayer)
                        .build()
        );
        this.buttonList.add(
                buttonMultiplayer
        );

        //------texts------

        //player name
        this.textList.add(
                new TextAdvanced(
                        ()-> mc.getSession().getUsername(),
                        TEXT_COLOR,
                        100,
                        0,
                        30,
                        0
                )
        );
        //rank
        this.textList.add(
                new TextAdvanced(
                        ()-> ClientProxy.playerData.getRank(),
                        TEXT_COLOR,
                        150,
                        0,
                        65,
                        0
                )
        );
        //gang name
        this.textList.add(
                new TextAdvanced(
                        ()->ClientProxy.playerData.getGang(),
                        TEXT_COLOR,
                        150,
                        0,
                        85,
                        0
                )
        );
        //level
        this.textList.add(
                new TextAdvanced(
                        ()-> String.valueOf(ClientProxy.playerData.getLevel()),
                        TEXT_COLOR,
                        150,
                        0,
                        115,
                        0
                )
        );
        //money
        this.textList.add(
                new TextAdvanced(
                        ()->"$ "+ClientProxy.playerData.getMoney(),
                        TEXT_COLOR,
                        150,
                        0,
                        135,
                        0
                )
        );
        //kills
        this.textList.add(
                new TextAdvanced(
                        ()-> ClientProxy.playerData.getOtherOrDefault("kills","0"),
                        TEXT_COLOR,
                        150,
                        0,
                        155,
                        0
                )
        );
        //deaths
        this.textList.add(
                new TextAdvanced(
                        ()->ClientProxy.playerData.getOtherOrDefault("deaths","0"),
                        TEXT_COLOR,
                        150,
                        0,
                        175,
                        0
                )
        );




    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        if(!ClientProxy.playerData.isUpdatedFromServer() && ClientProxy.playerData.getLastUpdateAttempt() + 10000
                < System.currentTimeMillis()){
            ClientProxy.playerData.setLastUpdateAttempt(System.currentTimeMillis());
            new SocketConnectorPlayerInfo(
                    GTWClient.settings.getPlayerInfoHost(),
                    GTWClient.settings.getPlayerInfoPort()
            ).sendAndRead(Minecraft.getMinecraft().getSession().getProfile().getName());

        }

        this.mc.getTextureManager().bindTexture(BACKGROUND_IMAGE);
        RenderUtils.drawCompleteImage(0,0,this.width,this.height, -1);

        for(GuiButton button : buttonList){
            button.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        for(TextAdvanced text : textList){
            text.drawText(this.mc);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == SETTINGS_BUTTON_ID)
        {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == SINGLEPLAYER_BUTTON_ID)
        {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }

        if (button.id == MULTIPLAYER_BUTTON_ID)
        {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == QUIT_BUTTON_ID)
        {
            this.mc.shutdown();
        }

        if (button.id == 5)
        {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 11)
        {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
        }

        if (button.id == 12)
        {
            ISaveFormat isaveformat = this.mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null)
            {
                this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion"), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning"), I18n.format("selectWorld.deleteButton"), I18n.format("gui.cancel"), 12));
            }
        }

        if(button.id == PLAY_BUTTON_ID) {
            FMLClientHandler.instance().connectToServerAtStartup(
                    GTWClient.settings.getServerHost(),
                    GTWClient.settings.getServerPort()
            );
        }
        if(button.id == DISCORD_BUTTON_ID) {
            try {
                Desktop.getDesktop().browse(new URI(GTWClient.settings.getDiscordLink()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        if(button.id == WEBSITE_BUTTON_ID) {
            try {
                Desktop.getDesktop().browse(new URI(GTWClient.settings.getWebsiteLink()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
