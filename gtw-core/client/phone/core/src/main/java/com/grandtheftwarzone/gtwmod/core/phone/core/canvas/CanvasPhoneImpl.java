package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;

import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneState;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.annotations.RegisterOptimizedVariable;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.utils.MathUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.core.display.elements.ElementImage;
import me.phoenixra.atumodcore.core.display.elements.canvas.DefaultCanvas;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

@RegisterDisplayElement(templateId = "canvas_phone")
public class CanvasPhoneImpl extends CanvasPhone {


    protected ElementImage phoneModel;
    //divide mainmenu display and display disabled by classes
    protected ElementImage phoneDisplayDefault;

    protected CanvasPhoneLocked phoneDisplayLocked;
    protected CanvasPhoneMainMenu phoneDisplayMainPage;





    private PhoneShapeDrawer phoneShapeDrawer;


    public CanvasPhoneImpl(AtumMod atumMod, DisplayCanvas owner) {
        super(atumMod,owner);
    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor,
                          int mouseX, int mouseY) {
        super.onDraw(resolution, scaleFactor, mouseX, mouseY);

        phoneShapeDrawer.prepareDrawing(resolution, scaleFactor, mouseX, mouseY);

        phoneShapeDrawer.phoneDisplayDrawer.run();
        switch (getState()) {
            case OPENED_DISPLAY:
                if(isLocked()) {
                    phoneDisplayLocked.draw(
                            resolution,
                            scaleFactor,
                            mouseX,
                            mouseY
                    );
                }else {
                    phoneDisplayMainPage.draw(
                            resolution,
                            scaleFactor,
                            mouseX,
                            mouseY
                    );
                }
                break;
            case OPENED_APP:
                int displayX = getDisplayX();
                int displayY = getDisplayY();
                int displayWidth = getDisplayWidth();
                int displayHeight = getDisplayHeight();

                //calculated phone display location depending on a shape
                if (shape == PhoneShape.VERTICAL
                        || shape == PhoneShape.FULL_SCREEN) {
                    drawApp(
                            resolution,
                            scaleFactor,
                            displayX,
                            displayY,
                            displayWidth,
                            displayHeight,
                            mouseX, mouseY
                    );

                } else if (shape == PhoneShape.HORIZONTAL) {
                    int centerX = phoneDisplayDefault.getX() + (phoneDisplayDefault.getWidth() / 2);
                    int centerY = phoneDisplayDefault.getY() + (phoneDisplayDefault.getHeight() / 2);

                    displayX = centerX - (centerY - phoneDisplayDefault.getY());
                    displayY = centerY - (phoneDisplayDefault.getX() +
                            phoneDisplayDefault.getWidth() - centerX);
                    displayWidth = phoneDisplayDefault.getHeight();
                    displayHeight = phoneDisplayDefault.getWidth();
                    int widthScaled = (int) (displayWidth * horizontalShapeScale);
                    int heightScaled = (int) (displayHeight * horizontalShapeScale);
                    drawApp(
                            resolution,
                            scaleFactor,
                            displayX - (widthScaled - displayWidth) / 2,
                            displayY - (heightScaled - displayHeight) / 2,
                            widthScaled,
                            heightScaled,
                            mouseX, mouseY
                    );

                }
                break;
        }
        phoneShapeDrawer.phoneModelDrawer.run();

    }
    protected void drawApp(DisplayResolution resolution,
                           float scaleFactor,
                           int displayX, int displayY,
                           int displayWidth, int displayHeight,
                           int mouseX, int mouseY){
        if(shape == PhoneShape.FULL_SCREEN){
            //separately drawn for better perfomance, cause
            //such shape is used mostly by apps that require
            //performance
            openedApp.draw(this, resolution,
                    scaleFactor,
                    Display.getWidth(),
                    Display.getHeight(),
                    mouseX, mouseY
            );
            return;
        }
        GL11.glEnable(GL_SCISSOR_TEST);
        GL11.glScissor((int) (displayX*scaleFactor),
                (int) (Display.getHeight() - (displayY + displayHeight)*scaleFactor), //invert y, bcz scissor works in a bottom-left corner
                (int) (displayWidth*scaleFactor),
                (int) (displayHeight*scaleFactor)
        );

        GL11.glPushMatrix();
        GL11.glTranslatef(displayX, displayY, 0);

        openedApp.draw(this, resolution,
                scaleFactor,
                displayWidth,
                displayHeight,
                mouseX, mouseY);
        GL11.glPopMatrix();

        GL11.glDisable(GL_SCISSOR_TEST);


    }

    @Override
    public void updateBaseVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateBaseVariables(config, configKey);

        HashMap<PhoneState, Integer> animationDuration = new HashMap<>();
        for (PhoneState phoneState : PhoneState.values()) {
            animationDuration.put(
                    phoneState,
                    config.getIntOrDefault(
                            "animationDuration." + phoneState.name(),
                            25
                    )
            );
        }
        phoneShapeDrawer = new PhoneShapeDrawer(
                this,
                animationDuration
        );


        if (config.hasPath("phoneModel")) {
            Config config1 = config.getSubsection("phoneModel");
            phoneModel = new ElementImage(
                    getAtumMod(),
                    this
            );

            phoneModel.updateVariables(config1, "phoneModel");
            phoneModel.setOriginX(getOriginX());
            phoneModel.setOriginY(getOriginY());
            phoneModel.setOriginWidth(getOriginWidth());
            phoneModel.setOriginHeight(getOriginHeight());
        }
        if (config.hasPath("phoneDisplay")) {
            Config config1 = config.getSubsection("phoneDisplay");
            phoneDisplayDefault = new ElementImage(
                    getAtumMod(),
                    this
            );
            phoneDisplayDefault.updateVariables(config1, "phoneDisplay");


            phoneDisplayDefault.getOriginX().setDefaultValue(
                    getOriginX().getDefaultValue() + phoneDisplayDefault.getOriginX().getDefaultValue()
            );
            phoneDisplayDefault.getOriginY().setDefaultValue(
                    getOriginY().getDefaultValue() + phoneDisplayDefault.getOriginY().getDefaultValue()
            );
            phoneDisplayDefault.getOriginWidth().setDefaultValue(
                    getOriginWidth().getDefaultValue() + phoneDisplayDefault.getOriginWidth().getDefaultValue()
            );
            phoneDisplayDefault.getOriginHeight().setDefaultValue(
                    getOriginHeight().getDefaultValue() + phoneDisplayDefault.getOriginHeight().getDefaultValue()
            );


            phoneDisplayLocked = new CanvasPhoneLocked(
                    getAtumMod(),
                    this
            );
            phoneDisplayLocked.updateVariables(
                    config1.getSubsection("locked"),
                    "phoneDisplayLocked"
            );
            phoneDisplayLocked.getOriginX().setDefaultValue(
                    phoneDisplayDefault.getOriginX().getDefaultValue()
            );
            phoneDisplayLocked.getOriginY().setDefaultValue(
                    phoneDisplayDefault.getOriginY().getDefaultValue()
            );
            phoneDisplayLocked.getOriginWidth().setDefaultValue(
                    phoneDisplayDefault.getOriginWidth().getDefaultValue()
            );
            phoneDisplayLocked.getOriginHeight().setDefaultValue(
                    phoneDisplayDefault.getOriginHeight().getDefaultValue()
            );
            phoneDisplayLocked.updateElements(
                    config1.getSubsection("locked.elements")
            );


            phoneDisplayMainPage = new CanvasPhoneMainMenu(
                    getAtumMod(),
                    this
            );
            phoneDisplayMainPage.updateVariables(
                    config1.getSubsection("mainPage"),
                    "phoneDisplayMainPage"
            );
            phoneDisplayMainPage.getOriginX().setDefaultValue(
                    phoneDisplayDefault.getOriginX().getDefaultValue()
            );
            phoneDisplayMainPage.getOriginY().setDefaultValue(
                    phoneDisplayDefault.getOriginY().getDefaultValue()
            );
            phoneDisplayMainPage.getOriginWidth().setDefaultValue(
                    phoneDisplayDefault.getOriginWidth().getDefaultValue()
            );
            phoneDisplayMainPage.getOriginHeight().setDefaultValue(
                    phoneDisplayDefault.getOriginHeight().getDefaultValue()
            );
            phoneDisplayMainPage.updateElements(
                    config1.getSubsection("mainPage.elements")
            );

        }
    }

    @Override
    public int getDisplayX() {
        return phoneDisplayDefault.getGlobalX();
    }

    @Override
    public int getDisplayY() {
        return phoneDisplayDefault.getGlobalY();
    }

    @Override
    public int getDisplayWidth() {
        return phoneDisplayDefault.getWidth();
    }
    @Override
    public int getDisplayHeight() {
        return phoneDisplayDefault.getHeight();
    }

    @Override
    public void applyResolutionOptimizer(@NotNull DisplayResolution resolution, @NotNull Config config) {
        super.applyResolutionOptimizer(resolution, config);
        if(config.hasPath("phoneDisplay")) {

            phoneDisplayDefault.getOriginX().addOptimizedValue(resolution,
                    getOriginX().getValue(resolution)
                            + config.getInt("phoneDisplay.posX")
            );
            phoneDisplayDefault.getOriginY().addOptimizedValue(resolution,
                    getOriginY().getValue(resolution)
                            + config.getInt("phoneDisplay.posY")
            );
            phoneDisplayDefault.getOriginWidth().addOptimizedValue(resolution,

                            getOriginWidth().getValue(resolution)
                                    + config.getInt("phoneDisplay.width")

            );
            phoneDisplayDefault.getOriginHeight().addOptimizedValue(resolution,
                    getOriginHeight().getValue(resolution)
                            + config.getInt("phoneDisplay.height")

            );

            phoneDisplayLocked.getOriginX().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginX().getValue(resolution)
            );
            phoneDisplayLocked.getOriginY().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginY().getValue(resolution)
            );
            phoneDisplayLocked.getOriginWidth().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginWidth().getValue(resolution)
            );
            phoneDisplayLocked.getOriginHeight().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginHeight().getValue(resolution)
            );


            phoneDisplayMainPage.getOriginX().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginX().getValue(resolution)
            );
            phoneDisplayMainPage.getOriginY().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginY().getValue(resolution)
            );
            phoneDisplayMainPage.getOriginWidth().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginWidth().getValue(resolution)
            );
            phoneDisplayMainPage.getOriginHeight().addOptimizedValue(resolution,
                    phoneDisplayDefault.getOriginHeight().getValue(resolution)
            );

        }
    }

    private void updateState() {
        switch (state) {
            case OPENING:
                phoneModel.setAdditionY(0);
                phoneDisplayDefault.setAdditionY(0);
                state = PhoneState.OPENED_DISPLAY;
                break;
            case CLOSING:
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            case HORIZONTAL_TO_FULL_SCREEN:
            case HORIZONTAL_TO_VERTICAL:
            case VERTICAL_TO_HORIZONTAL:
            case VERTICAL_TO_FULL_SCREEN:
            case FULL_SCREEN_TO_HORIZONTAL:
            case FULL_SCREEN_TO_VERTICAL:
                state = openedApp == null ?
                        PhoneState.OPENED_DISPLAY : PhoneState.OPENED_APP;
                break;
            default:
                break;
        }
    }

    @Override
    protected void setState(@NotNull PhoneState state) {
        if (this.state == PhoneState.CLOSING) return;
        this.state = state;
        phoneShapeDrawer.animationTimer = 0;
        //@TODO add later for better feel of the closing state
        //Mouse.setGrabbed(true);
        //But have to handle the case when
        //other gui is opened while the phone is closing
        //cause in that case the gui for some resoun do not
        //set the mouse grabbed to false
    }

    @Override
    public void changeShape(@NotNull PhoneShape shape) {
        setState(PhoneState.fromShape(this.shape, shape));
        this.shape = shape;
    }




    @Override
    public void setDisplayRenderer(@NotNull DisplayRenderer displayRenderer) {
        super.setDisplayRenderer(displayRenderer);
        if(phoneDisplayLocked!=null){
            phoneDisplayLocked.setDisplayRenderer(displayRenderer);
        }
        if(phoneDisplayMainPage!=null){
            phoneDisplayMainPage.setDisplayRenderer(displayRenderer);
        }
    }

    @Override
    protected BaseElement onClone(BaseElement baseCanvas) {

        CanvasPhoneImpl canvasPhone = (CanvasPhoneImpl) super.onClone(baseCanvas);
        canvasPhone.phoneShapeDrawer = new PhoneShapeDrawer(
                canvasPhone,
                phoneShapeDrawer == null ? new HashMap<>() : phoneShapeDrawer.animationDuration
        );
        if (phoneModel != null) {
            canvasPhone.phoneModel = (ElementImage) phoneModel.clone();
        }
        if (phoneDisplayDefault != null) {
            canvasPhone.phoneDisplayDefault = (ElementImage) phoneDisplayDefault.clone();
        }
        if (phoneDisplayLocked != null) {
            canvasPhone.phoneDisplayLocked = (CanvasPhoneLocked) phoneDisplayLocked.clone();
            canvasPhone.phoneDisplayLocked.phone = canvasPhone;
        }
        if (phoneDisplayMainPage != null) {
            canvasPhone.phoneDisplayMainPage = (CanvasPhoneMainMenu) phoneDisplayMainPage.clone();
            canvasPhone.phoneDisplayMainPage.phone = canvasPhone;
        }
        canvasPhone.openedApp = null;
        return canvasPhone;
    }

    @SubscribeEvent
    public void onDamageReceived(LivingDamageEvent event){
        if(event.getEntityLiving() == Minecraft.getMinecraft().player){
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }


    @Override
    public void onRemove() {
        super.onRemove();
        if(phoneModel!=null) {
            phoneModel.onRemove();
        }
        if(phoneDisplayLocked!=null) {
            phoneDisplayLocked.onRemove();
        }
        if(phoneDisplayMainPage!=null) {
            phoneDisplayMainPage.onRemove();
        }
    }

    private static class PhoneShapeDrawer {
        private CanvasPhoneImpl phone;
        @Getter
        private HashMap<PhoneState, Integer> animationDuration;
        private int animationTimer = 0;

        private Runnable phoneModelDrawer;

        private Runnable phoneDisplayDrawer;

        private PhoneShapeDrawer(CanvasPhoneImpl phone, HashMap<PhoneState, Integer> animationDuration) {
            this.phone = phone;
            this.animationDuration = animationDuration;
        }

        //stage 0 -> phone model
        //stage 1 -> phone display
        public void prepareDrawing(DisplayResolution resolution, float scaleFactor,
                                   int mouseX, int mouseY) {
            PhoneState phoneState = phone.getState();
            ElementImage phoneModel = phone.phoneModel;
            ElementImage phoneDisplay = phone.phoneDisplayDefault;

            //-------WITHOUT ANIMATION-------
            if (phoneState == PhoneState.OPENED_APP
                    || phoneState == PhoneState.OPENED_DISPLAY) {


                if (phone.shape == PhoneShape.VERTICAL) {
                    phoneDisplayDrawer = ()-> {
                        phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    };
                    phoneModelDrawer = ()-> {
                        phoneModel.draw(resolution, scaleFactor, mouseX, mouseY);
                    };
                    return;
                } else if (phone.shape == PhoneShape.HORIZONTAL) {
                    int rotationAngle = -90;
                    int centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    int centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);

                    preparedrawRotated(phoneModel,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            0,
                            rotationAngle,
                            horizontalShapeScale,
                            0
                    );
                } else {
                    phoneModelDrawer = ()-> {

                    };
                }

                return;
            }

            //-------WITH ANIMATION-------
            animationTimer++;
            int animationDuration = this.animationDuration.getOrDefault(
                    phoneState,
                    25
            );

            int animate;
            int rotationAngle;
            float scaleShape;
            int centerX;
            int centerY;
            float rotationAnimation;
            float scaleAnimation;
            double b;
            switch (phoneState) {
                case OPENING:
                    animate = (int) (MathUtils.fastCos(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    ) * (1920 - phoneModel.getOriginY().getValue(resolution))
                    );
                    phoneModel.setAdditionY(animate);
                    phoneDisplay.setAdditionY(animate);
                    phoneDisplayDrawer = () -> {
                        phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    };
                    phoneModelDrawer = () -> {
                        phoneModel.draw(resolution, scaleFactor, mouseX, mouseY);
                    };
                    break;
                case CLOSING:
                    animate = (int) (MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    ) * (1920 - phoneModel.getOriginY().getValue(resolution))
                    );
                    phoneModel.setAdditionY(animate);
                    phoneDisplay.setAdditionY(animate);
                    phoneDisplayDrawer = ()->{
                        phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    };
                    phoneModelDrawer = () ->{
                        phoneModel.draw(resolution, scaleFactor, mouseX, mouseY);
                    };
                    break;
                case VERTICAL_TO_HORIZONTAL:
                    scaleShape = phone.horizontalShapeScale;
                    rotationAngle = -90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape - 1));
                    // Apply transformations
                    preparedrawRotated(phoneModel,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            0,
                            rotationAnimation,
                            1,
                            scaleAnimation
                    );
                    break;
                case HORIZONTAL_TO_VERTICAL:
                    scaleShape = -phone.horizontalShapeScale;
                    rotationAngle = 90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape + 1));
                    // Apply transformations
                    preparedrawRotated(phoneModel,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            rotationAnimation,
                            phone.horizontalShapeScale,
                            scaleAnimation
                    );
                    break;

                case VERTICAL_TO_FULL_SCREEN:
                    scaleShape = 4;
                    rotationAngle = -90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape - 1));
                    // Apply transformations
                    preparedrawRotated(phoneModel,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            0,
                            rotationAnimation,
                            1,
                            scaleAnimation
                    );
                    break;
                case FULL_SCREEN_TO_VERTICAL:
                    scaleShape = -4;
                    rotationAngle = 90;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    rotationAnimation = (int) (b * rotationAngle);
                    scaleAnimation = (float) (b * (scaleShape + 1));
                    // Apply transformations
                    preparedrawRotated(phoneModel,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            rotationAnimation,
                            4,
                            scaleAnimation
                    );
                    break;
                case HORIZONTAL_TO_FULL_SCREEN:
                    scaleShape = 4;
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    scaleAnimation = (float) (b * (scaleShape - 1));
                    // Apply transformations
                    preparedrawRotated(phoneModel,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            0,
                            phone.horizontalShapeScale,
                            scaleAnimation
                    );
                    break;
                case FULL_SCREEN_TO_HORIZONTAL:
                    scaleShape = -(4-phone.horizontalShapeScale);
                    centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);
                    b = MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    );
                    scaleAnimation = (float) (b * (scaleShape + 1));
                    // Apply transformations
                    preparedrawRotated(phoneModel,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            -90,
                            0,
                            4,
                            scaleAnimation
                    );
                    break;
                default:
                    break;

            }
            if (animationTimer >= animationDuration) {
                phone.updateState();
                animationTimer = 0;
            }
        }

        private void preparedrawRotated(ElementImage phoneModel,
                                        ElementImage phoneDisplay,
                                        DisplayResolution resolution,
                                        float scaleFactor,
                                        int mouseX, int mouseY,
                                        int centerX, int centerY,
                                        float rotationAngleDefault,
                                        float rotationAngle,
                                        float scaleDefault,
                                        float scaleNew) {// Translate back


            phoneDisplayDrawer = () ->{
                GL11.glPushMatrix();
                GL11.glTranslatef(centerX, centerY, 0); // Translate to the center
                GL11.glRotatef(rotationAngleDefault + rotationAngle, 0, 0, 1); // Rotate around the center
                GL11.glScalef(scaleDefault + scaleNew, scaleDefault + scaleNew, 1); // Scale to the new size
                GL11.glTranslatef(-centerX, -centerY, 0);
                phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                GL11.glPopMatrix();
            };
            phoneModelDrawer = () -> {
                GL11.glPushMatrix();
                GL11.glTranslatef(centerX, centerY, 0); // Translate to the center
                GL11.glRotatef(rotationAngleDefault + rotationAngle, 0, 0, 1); // Rotate around the center
                GL11.glScalef(scaleDefault + scaleNew, scaleDefault + scaleNew, 1); // Scale to the new size
                GL11.glTranslatef(-centerX, -centerY, 0);
                phoneModel.draw(resolution, scaleFactor, mouseX, mouseY);
                GL11.glPopMatrix();
            };


        }
    }


}
