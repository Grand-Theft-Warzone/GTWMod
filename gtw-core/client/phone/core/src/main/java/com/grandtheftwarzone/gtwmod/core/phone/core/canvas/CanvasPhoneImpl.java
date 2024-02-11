package com.grandtheftwarzone.gtwmod.core.phone.core.canvas;

import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneApp;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneShape;
import com.grandtheftwarzone.gtwmod.api.gui.phone.PhoneState;
import com.grandtheftwarzone.gtwmod.api.gui.phone.canvas.CanvasPhone;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.annotations.RegisterOptimizedVariable;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.variables.OptimizedVariableInt;
import me.phoenixra.atumodcore.api.utils.MathUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.core.display.elements.ElementImage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

@RegisterDisplayElement(templateId = "canvas_phone")
public class CanvasPhoneImpl extends CanvasPhone {


    private List<IconPosition> appIconPositions;


    protected ElementImage background;
    protected ElementImage phoneDisplay;
    @RegisterOptimizedVariable

    private OptimizedVariableInt iconPadding;
    @RegisterOptimizedVariable
    private OptimizedVariableInt iconsPerRow;


    private float horizontalShapeScale = 1.5f;


    private PhoneShapeDrawer phoneShapeDrawer;

    private boolean init;

    public CanvasPhoneImpl(AtumMod atumMod, DisplayCanvas owner) {
        super(atumMod,owner);
        iconPadding = new OptimizedVariableInt("iconPadding",10);
        iconsPerRow = new OptimizedVariableInt("iconsPerRow",3);

    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor,
                          int mouseX, int mouseY) {
        super.onDraw(resolution, scaleFactor, mouseX, mouseY);

        phoneShapeDrawer.draw(resolution, scaleFactor, mouseX, mouseY);
        switch (state) {
            case OPENED_DISPLAY:
                if (!init) {
                    appIconPositions = IconPosition.calculateIconLayout(
                            phoneDisplay.getX(),
                            phoneDisplay.getY(),
                            phoneDisplay.getWidth(),
                            phoneDisplay.getHeight(),
                            getApps().size(),
                            iconsPerRow.getValue(resolution),
                            iconPadding.getValue(resolution)
                    );
                    init = true;
                }

                for (int i = 0; i < getApps().size(); i++) {
                    getApps().get(i).drawIcon(this, resolution,
                            appIconPositions.get(i).x,
                            appIconPositions.get(i).y,
                            appIconPositions.get(i).size,
                            isAppHovered(
                                    appIconPositions.get(i).x,
                                    appIconPositions.get(i).y,
                                    appIconPositions.get(i).size,
                                    mouseX,
                                    mouseY
                            )
                    );
                }
                break;
            case OPENED_APP:
                int displayX = phoneDisplay.getX();
                int displayY = phoneDisplay.getY();
                int displayWidth = phoneDisplay.getWidth();
                int displayHeight = phoneDisplay.getHeight();

                //calculated phone display location depending on a shape
                if (shape == PhoneShape.VERTICAL) {
                    drawApp(resolution,
                            scaleFactor,
                            displayX,
                            displayY,
                            displayWidth,
                            displayHeight,
                            mouseX, mouseY
                    );

                } else if (shape == PhoneShape.HORIZONTAL) {
                    int centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    int centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);

                    displayX = centerX - (centerY - phoneDisplay.getY());
                    displayY = centerY - (phoneDisplay.getX() +
                            phoneDisplay.getWidth() - centerX);
                    displayWidth = phoneDisplay.getHeight();
                    displayHeight = phoneDisplay.getWidth();
                    int widthScaled = (int) (displayWidth * horizontalShapeScale);
                    int heightScaled = (int) (displayHeight * horizontalShapeScale);
                    drawApp(resolution,
                            scaleFactor,
                            displayX - (widthScaled - displayWidth) / 2,
                            displayY - (heightScaled - displayHeight) / 2,
                            widthScaled,
                            heightScaled,
                            mouseX, mouseY);

                } else {
                    //separately drawnfor better perfomance, cause
                    //such shape is used mostly by apps that require
                    //performance
                    openedApp.draw(this, resolution,
                            scaleFactor,
                            Display.getWidth(),
                            Display.getHeight(),
                            mouseX, mouseY);
                }
                break;
        }
    }
    private void drawApp(DisplayResolution resolution,
                         float scaleFactor,
                         int displayX, int displayY,
                         int displayWidth, int displayHeight,
                         int mouseX, int mouseY){
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

        iconPadding.setDefaultValue(
                config.getIntOrDefault("iconPadding", 10)
        );
        iconsPerRow.setDefaultValue(
                config.getIntOrDefault("iconsPerRow", 3)
        );

        horizontalShapeScale = (float) config.getDoubleOrDefault("horizontalShapeScale", 1.5f);

        if (config.hasPath("background")) {
            Config config1 = config.getSubsection("background");
            background = new ElementImage(
                    getAtumMod(),
                    this
            );

            background.updateVariables(config1, "background");
            background.setOriginX(getOriginX());
            background.setOriginY(getOriginY());
            background.setOriginWidth(getOriginWidth());
            background.setOriginHeight(getOriginHeight());
        }
        if (config.hasPath("phoneDisplay")) {
            Config config1 = config.getSubsection("phoneDisplay");
            phoneDisplay = new ElementImage(
                    getAtumMod(),
                    this
            );
            phoneDisplay.updateVariables(config1, "phoneDisplay");


            phoneDisplay.getOriginX().setDefaultValue(
                    getOriginX().getDefaultValue() + phoneDisplay.getOriginX().getDefaultValue()
            );
            phoneDisplay.getOriginY().setDefaultValue(
                    getOriginY().getDefaultValue() + phoneDisplay.getOriginY().getDefaultValue()
            );
            phoneDisplay.getOriginWidth().setDefaultValue(
                    getOriginWidth().getDefaultValue() + phoneDisplay.getOriginWidth().getDefaultValue()
            );
            phoneDisplay.getOriginHeight().setDefaultValue(
                    getOriginHeight().getDefaultValue() + phoneDisplay.getOriginHeight().getDefaultValue()
            );

        }
    }

    @Override
    protected int getPhoneDisplayX() {
        return phoneDisplay.getX();
    }

    @Override
    protected int getPhoneDisplayY() {
        return phoneDisplay.getY();
    }

    @Override
    public void applyResolutionOptimizer(@NotNull DisplayResolution resolution, @NotNull Config config) {
        super.applyResolutionOptimizer(resolution, config);
        if(config.hasPath("phoneDisplay")) {

            phoneDisplay.getOriginX().addOptimizedValue(resolution,
                    getOriginX().getValue(resolution) + config.getInt("phoneDisplay.posX")
            );
            phoneDisplay.getOriginY().addOptimizedValue(resolution,
                    getOriginY().getValue(resolution) + config.getInt("phoneDisplay.posY")
            );
            phoneDisplay.getOriginWidth().addOptimizedValue(resolution,
                    getOriginWidth().getValue(resolution) + config.getInt("phoneDisplay.width")
            );
            phoneDisplay.getOriginHeight().addOptimizedValue(resolution,
                    getOriginHeight().getValue(resolution) + config.getInt("phoneDisplay.height")
            );

        }
    }

    private void updateState() {
        switch (state) {
            case OPENING:
                background.setAdditionY(0);
                phoneDisplay.setAdditionY(0);
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


    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseInputEvent.Pre e) {
        int pressed = Mouse.getEventButton();
        PhoneState phoneState = getState();
        if (phoneState != PhoneState.OPENED_DISPLAY) {
            return;
        }
        if (Mouse.getEventButtonState() && pressed == 0) {
            for (int i = 0; i < getApps().size(); i++) {
                PhoneApp app = getApps().get(i);
                if (isAppHovered(
                        appIconPositions.get(i).x,
                        appIconPositions.get(i).y,
                        appIconPositions.get(i).size,
                        getLastMouseX(),
                        getLastMouseY()
                )) {
                    openApp(app);
                    return;
                }
            }
        }
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean b) {

    }

    private boolean isAppHovered(int x, int y, int size,
                                 int mouseX, int mouseY) {
        return mouseX >= x &&
                mouseX <= x + size
                && mouseY >= y
                && mouseY <= y + size;
    }


    @Override
    protected BaseElement onClone(BaseElement baseCanvas) {

        CanvasPhoneImpl canvasPhone = (CanvasPhoneImpl) super.onClone(baseCanvas);
        canvasPhone.init = false;
        canvasPhone.phoneShapeDrawer = new PhoneShapeDrawer(
                canvasPhone,
                phoneShapeDrawer == null ? new HashMap<>() : phoneShapeDrawer.animationDuration
        );
        if (background != null) {
            canvasPhone.background = (ElementImage) background.clone();
        }
        if (phoneDisplay != null) {
            canvasPhone.phoneDisplay = (ElementImage) phoneDisplay.clone();
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

    private static class PhoneShapeDrawer {
        private CanvasPhoneImpl phone;
        @Getter
        private HashMap<PhoneState, Integer> animationDuration;
        private int animationTimer = 0;

        private PhoneShapeDrawer(CanvasPhoneImpl phone, HashMap<PhoneState, Integer> animationDuration) {
            this.phone = phone;
            this.animationDuration = animationDuration;
        }

        public void draw(DisplayResolution resolution, float scaleFactor,
                         int mouseX, int mouseY) {
            PhoneState phoneState = phone.getState();
            ElementImage background = phone.background;
            ElementImage phoneDisplay = phone.phoneDisplay;

            //-------WITHOUT ANIMATION-------
            if (phoneState == PhoneState.OPENED_APP
                    || phoneState == PhoneState.OPENED_DISPLAY) {


                if (phone.shape == PhoneShape.VERTICAL) {
                    background.draw(resolution, scaleFactor, mouseX, mouseY);
                    phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    return;
                } else if (phone.shape == PhoneShape.HORIZONTAL) {
                    int rotationAngle = -90;
                    int centerX = phoneDisplay.getX() + (phoneDisplay.getWidth() / 2);
                    int centerY = phoneDisplay.getY() + (phoneDisplay.getHeight() / 2);

                    drawRotated(background,
                            phoneDisplay,
                            resolution,
                            scaleFactor,
                            mouseX, mouseY,
                            centerX, centerY,
                            0,
                            rotationAngle,
                            phone.horizontalShapeScale,
                            0
                    );
                } else {
                    RenderUtils.fill(
                            0,
                            0,
                            Display.getWidth(),
                            Display.getHeight(),
                            0x000000,
                            1.0f
                    );
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
                    ) * (1920 - background.getOriginY().getValue(resolution))
                    );
                    background.setAdditionY(animate);
                    phoneDisplay.setAdditionY(animate);
                    background.draw(resolution, scaleFactor, mouseX, mouseY);
                    phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
                    break;
                case CLOSING:
                    animate = (int) (MathUtils.fastSin(
                            Math.PI * 0.5 *
                                    ((double) animationTimer / animationDuration)
                    ) * (1920 - background.getOriginY().getValue(resolution))
                    );
                    background.setAdditionY(animate);
                    phoneDisplay.setAdditionY(animate);
                    background.draw(resolution, scaleFactor, mouseX, mouseY);
                    phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);
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
                    drawRotated(background,
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
                    drawRotated(background,
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
                    drawRotated(background,
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
                    drawRotated(background,
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
                    drawRotated(background,
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
                    drawRotated(background,
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

        private void drawRotated(ElementImage background,
                                 ElementImage phoneDisplay,
                                 DisplayResolution resolution,
                                 float scaleFactor,
                                 int mouseX, int mouseY,
                                 int centerX, int centerY,
                                 float rotationAngleDefault,
                                 float rotationAngle,
                                 float scaleDefault,
                                 float scaleNew) {
            GL11.glPushMatrix();
            GL11.glTranslatef(centerX, centerY, 0); // Translate to the center
            GL11.glRotatef(rotationAngleDefault + rotationAngle, 0, 0, 1); // Rotate around the center
            GL11.glScalef(scaleDefault + scaleNew, scaleDefault + scaleNew, 1); // Scale to the new size
            GL11.glTranslatef(-centerX, -centerY, 0); // Translate back

            background.draw(resolution, scaleFactor, mouseX, mouseY);
            phoneDisplay.draw(resolution, scaleFactor, mouseX, mouseY);

            // Restore the matrix state
            GL11.glPopMatrix();
        }
    }


}
