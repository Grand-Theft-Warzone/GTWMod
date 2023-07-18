package me.phoenixra.gtwclient.mainmenu;

import me.phoenixra.gtwclient.GTWClient;
import me.phoenixra.gtwclient.proxy.ClientProxy;
import me.phoenixra.gtwclient.utils.RenderUtils;
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
    private final ArrayList<ImageAdvanced> imageList = new ArrayList<>();

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
                        .x((scaleFactor)-> (int)(40 / scaleFactor))
                        .y((scaleFactor)-> (int)(height - 120 / scaleFactor))
                        .width((scaleFactor)-> (int)(100 / scaleFactor))
                        .height((scaleFactor)-> (int)(100 / scaleFactor))
                        .image(QUIT_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(SETTINGS_BUTTON_ID)
                        .x((scaleFactor)-> (int)(width - 140 / scaleFactor))
                        .y((scaleFactor)-> (int)(height - 124 / scaleFactor))
                        .width((scaleFactor)-> (int)(100 / scaleFactor))
                        .height((scaleFactor)-> (int)(100 / scaleFactor))
                        .image(SETTINGS_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(DISCORD_BUTTON_ID)
                        .x((scaleFactor)-> (int)(width - 140 / scaleFactor))
                        .y((scaleFactor)-> (int)(height - 244 / scaleFactor))
                        .width((scaleFactor)-> (int)(100 / scaleFactor))
                        .height((scaleFactor)-> (int)(100 / scaleFactor))
                        .image(DISCORD_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(WEBSITE_BUTTON_ID)
                        .x((scaleFactor)-> (int)(width - 140 / scaleFactor))
                        .y((scaleFactor)-> (int)(height - 364 / scaleFactor))
                        .width((scaleFactor)-> (int)(100 / scaleFactor))
                        .height((scaleFactor)-> (int)(100 / scaleFactor))
                        .image(WEBSITE_BUTTON_IMAGE)
                        .build()
        );
        this.buttonList.add(
                ButtonAdvanced.builder(PLAY_BUTTON_ID)
                        .x((scaleFactor)-> (int)(width/2 - 200 / scaleFactor))
                        .y((scaleFactor)-> (int)(height - 350 / scaleFactor))
                        .width((scaleFactor)-> (int)(320 / scaleFactor))
                        .height((scaleFactor)-> (int)(320 / scaleFactor))
                        .image(PLAY_BUTTON_IMAGE)
                        .build()
        );
        GuiButton buttonMultiplayer = ButtonAdvanced.builder(MULTIPLAYER_BUTTON_ID)
                .x((scaleFactor)-> (int)(width/2 - 508 / scaleFactor))
                .y((scaleFactor)-> (int)(height - 237 / scaleFactor))
                .width((scaleFactor)-> (int)(230 / scaleFactor))
                .height((scaleFactor)-> (int)(230 / scaleFactor))
                .image(MULTIPLAYER_BUTTON_IMAGE)
                .build();
        this.buttonList.add(
                ButtonAdvanced.builder(SINGLEPLAYER_BUTTON_ID)
                        .x((scaleFactor)-> (int)(width/2 - 590 / scaleFactor))
                        .y((scaleFactor)-> (int)(height - 437 / scaleFactor))
                        .width((scaleFactor)-> (int)(270 / scaleFactor))
                        .height((scaleFactor)-> (int)(270 / scaleFactor))
                        .image(SINGLEPLAYER_BUTTON_IMAGE)
                        .buttonUnder(buttonMultiplayer)
                        .build()
        );
        this.buttonList.add(
                buttonMultiplayer
        );

        //------texts------
        FontRenderer fontRenderer = mc.fontRenderer;

        //player name
        this.textList.add(
                new TextAdvanced(
                        ()-> mc.getSession().getUsername(),
                        TEXT_COLOR,
                        (scaleFactor)-> (int)(270 / (scaleFactor)),
                        (scaleFactor)-> (int)(80 / (scaleFactor)),
                        (scaleFactor)-> 2.7F / scaleFactor,
                        (scaleFactor)-> 2.7F / scaleFactor
                )
        );


        //rank
        this.textList.add(
                new TextAdvanced(
                        ()-> ClientProxy.playerData.getRank(),
                        TEXT_COLOR,
                        (scaleFactor)-> (int)(210 / (scaleFactor)),
                        (scaleFactor)-> (int)(135 / (scaleFactor)),
                        (scaleFactor)-> 2.7F / scaleFactor,
                        (scaleFactor)-> 2.7F / scaleFactor
                )
        );
        //gang name
        this.textList.add(
                new TextAdvanced(
                        ()->ClientProxy.playerData.getGang(),
                        TEXT_COLOR,
                        (scaleFactor)-> (int)(210 / (scaleFactor)),
                        (scaleFactor)-> (int)(180 / (scaleFactor)),
                        (scaleFactor)-> 2.7F / scaleFactor,
                        (scaleFactor)-> 2.7F / scaleFactor
                )
        );
        //level
        this.textList.add(
                new TextAdvanced(
                        ()-> String.valueOf(ClientProxy.playerData.getLevel()),
                        TEXT_COLOR,
                        (scaleFactor)-> (int)(225 / (scaleFactor)),
                        (scaleFactor)-> (int)(219 / (scaleFactor)),
                        (scaleFactor)-> 2.7F / scaleFactor,
                        (scaleFactor)-> 2.7F / scaleFactor
                )
        );
        //money
        this.textList.add(
                new TextAdvanced(
                        ()->"$ "+ClientProxy.playerData.getMoney(),
                        TEXT_COLOR,
                        (scaleFactor)-> (int)(245 / (scaleFactor)),
                        (scaleFactor)-> (int)(267 / (scaleFactor)),
                        (scaleFactor)-> 2.7F / scaleFactor,
                        (scaleFactor)-> 2.7F / scaleFactor
                )
        );
        //kills
        this.textList.add(
                new TextAdvanced(
                        ()-> ClientProxy.playerData.getOtherOrDefault("kills","0"),
                        TEXT_COLOR,
                        (scaleFactor)-> (int)(215 / (scaleFactor)),
                        (scaleFactor)-> (int)(307 / (scaleFactor)),
                        (scaleFactor)-> 2.7F / scaleFactor,
                        (scaleFactor)-> 2.7F / scaleFactor
                )
        );
        //deaths
        this.textList.add(
                new TextAdvanced(
                        ()->ClientProxy.playerData.getOtherOrDefault("deaths","0"),
                        TEXT_COLOR,
                        (scaleFactor)-> (int)(245 / (scaleFactor)),
                        (scaleFactor)-> (int)(350 / (scaleFactor)),
                        (scaleFactor)-> 2.7F / scaleFactor,
                        (scaleFactor)-> 2.7F / scaleFactor
                )
        );


        //----images----
        imageList.add(new ImageAdvanced(
                (scaleFactor)-> (int)(414 / (scaleFactor)),
                (scaleFactor)-> (int)(193 / (scaleFactor)),
                (scaleFactor)-> 4.0f / scaleFactor,
                (scaleFactor)-> 3.55f / scaleFactor,
                 ClientProxy::bindPlayerSkinTexture,
                32,
                32,
                32,
                32
        ));



    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.mc.getTextureManager().bindTexture(BACKGROUND_IMAGE);
        RenderUtils.drawCompleteImage(0,0,this.width,this.height, -1);

        for(GuiButton button : buttonList){
            button.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        for(TextAdvanced text : textList){
            text.drawText(this.mc);
        }
        for(ImageAdvanced image : imageList){
            image.drawImage(this);
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
