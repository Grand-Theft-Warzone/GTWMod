package me.phoenixra.gtwclient.fml.test.textures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Nullable;

import me.phoenixra.gtwclient.fml.test.ResourceWrappingInputStream;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.KHRDebug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LegacyV2Adapter;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;


public final class TextureLoader {

    private static final Field FIELD_RES_MANAGER_MAP;
    private static final Field FIELD_FALLBACK_LIST;
    private static final Field FIELD_ABS_PACK_FILE;
    private static final Field FIELD_LEGACY_ADAPTOR_PACK;
    private static final Method METHOD_FILE_PACK_GETTER;

    static {
        Class<FileResourcePack> filePack = FileResourcePack.class;

        Method filePackGetter = null;

        FIELD_RES_MANAGER_MAP = getField(SimpleReloadableResourceManager.class, Map.class);
        FIELD_FALLBACK_LIST = getField(FallbackResourceManager.class, List.class);
        FIELD_ABS_PACK_FILE = getField(AbstractResourcePack.class, File.class);
        FIELD_LEGACY_ADAPTOR_PACK = getField(LegacyV2Adapter.class, IResourcePack.class);

        for (Method m : filePack.getDeclaredMethods()) {
            if ((m.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            if (!m.getReturnType().equals(ZipFile.class)) {
                continue;
            }
            if (m.getParameterCount() != 0) {
                continue;
            }
            filePackGetter = m;
            break;
        }

        filePackGetter.setAccessible(true);

        METHOD_FILE_PACK_GETTER = filePackGetter;
    }

    private static Field getField(Class<?> in, Class<?> fldType) {
        for (Field f : in.getDeclaredFields()) {
            if ((f.getModifiers() & Modifier.STATIC) != 0) {
                continue;
            }
            if (f.getType().equals(fldType)) {
                f.setAccessible(true);
                return f;
            }
        }
        throw new IllegalStateException("Failed to find a field!");
    }

    @Nullable
    public static InputStream openResourceStream(ResourceLocation location) throws IOException {

        System.out.println("[debug] Opening resource " + location);

        if ("config".equals(location.getResourceDomain())) {
            File fle = new File("config/customloadingscreen/" + location.getResourcePath());
            if (fle.exists()) {
                System.out.println("[debug]   - Found resource file at " + fle);
                try {
                    return new FileInputStream(fle);
                } catch (FileNotFoundException fnfe) {
                    System.out.println("[debug]   x Missing file!!");
                }
            } else {
                System.out.println("[debug]   x Missing file at " + fle + ", falling back to resources.");

            }
        }

        IResourceManager resManager = Minecraft.getMinecraft().getResourceManager();

        try {
            IResource res = resManager.getResource(location);
            System.out.println("[debug]   - Found resource: " + res.getResourcePackName() + " : " + res);
            return new ResourceWrappingInputStream(res);
        } catch (IOException e) {
            System.out.println("[debug]   x Failed to find resource, falling back to manual iteration....");
        }

        if (resManager instanceof SimpleReloadableResourceManager) {
            SimpleReloadableResourceManager srm = (SimpleReloadableResourceManager) resManager;

            System.out.println("[debug]   Manually interating over MC's SimpleResourceManager");

            Map<?, ?> map;
            try {
                map = (Map<?, ?>) FIELD_RES_MANAGER_MAP.get(srm);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println("[debug]   x Failed to retrieve the map!");
                return null;
            }

            Object value = map.get(location.getResourceDomain());

            if (value == null) {
                System.out.println("[debug]   - Found no entry for the domain!");
                return null;
            }

            System.out.println("[debug]   - Looking at " + value);

            FallbackResourceManager fallback = (FallbackResourceManager) value;

            List<?> list;

            try {
                list = (List<?>) FIELD_FALLBACK_LIST.get(fallback);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println("[debug]    x Failed to retrieve the list!");
                return null;
            }

            for (Object o : list) {
                IResourcePack pack = (IResourcePack) o;

                if (pack instanceof LegacyV2Adapter) {
                    try {
                        pack = (IResourcePack) FIELD_LEGACY_ADAPTOR_PACK.get(pack);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        System.out.println("[debug]    x Failed to retrieve the backing resource pack!");
                    }
                }

                if (pack instanceof AbstractResourcePack) {
                    File file;

                    try {
                        file = (File) FIELD_ABS_PACK_FILE.get(pack);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        System.out.println("[debug]     x Failed to retrieve the file!");
                        continue;
                    }

                    String realPath = "assets/" + location.getResourceDomain() + "/" + location.getResourcePath();

                    if (pack instanceof FileResourcePack) {
                        FileResourcePack frp = (FileResourcePack) pack;
                        System.out.println("[debug]   - Looking at FileResourcePack " + file);

                        ZipFile zip;
                        try {
                            zip = (ZipFile) METHOD_FILE_PACK_GETTER.invoke(pack);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            System.out.println("[debug]     x Failed to retrieve the ZipFile!");
                            continue;
                        }

                        ZipEntry entry = zip.getEntry(realPath);

                        if (entry != null) {
                            System.out.println("[debug]   - Found ZipEntry " + entry);

                            try {
                                return zip.getInputStream(entry);
                            } catch (IOException e) {
                                System.out.println("[debug]   - Failed to open ZipEntry ");
                            }
                        }

                        continue;
                    } else if (pack instanceof FolderResourcePack) {
                        FolderResourcePack fldr = (FolderResourcePack) pack;

                        System.out.println("[debug]   - Looking at FolderResourcePack " + file);

                        File target = new File(file, realPath);

                        if (target.isFile()) {
                            System.out.println("[debug]   - Found target file " + target);

                            return new FileInputStream(target);
                        }

                        continue;
                    }
                }

                System.out.println("[debug]   - Looking at unknown ResourcePack " + pack.getClass());

                try {
                    return pack.getInputStream(location);
                } catch (IOException e) {
                    System.out.println("[debug]   - Failed to open stream as " + e.getClass() + ":" + e.getMessage());
                }
            }
        } else {
            System.out.println(
                    "[debug]   Unknown IResourceManager " + resManager.getClass() + ", aborting manual iteration!"
            );
        }

        return null;
    }

    public static void bindTexture(TextureManager manager, ResourceLocation location) {
        ITextureObject current = manager.getTexture(location);

        if (current != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, current.getGlTextureId());
            return;
        }

        SimpleTexture texture = new ClsTexture(location);

        if (GLContext.getCapabilities().GL_KHR_debug) {
            KHRDebug.glPushDebugGroup(KHRDebug.GL_DEBUG_SOURCE_APPLICATION, 10, "CLS_LoadCustomTexture");
        }
        manager.loadTexture(location, texture);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());

        if (GLContext.getCapabilities().GL_KHR_debug) {
            KHRDebug.glPopDebugGroup();
        }

        if (GLContext.getCapabilities().GL_KHR_debug) {
            KHRDebug.glPushDebugGroup(KHRDebug.GL_DEBUG_SOURCE_APPLICATION, 10, "CLS_Render");
        }
    }

    public static PreScannedImageData preScan(ResourceLocation res) {

        try {
            ClsTexture clsTexture = new ClsTexture(res);
            clsTexture.loadImage(Minecraft.getMinecraft().getResourceManager());

            return new PreScannedImageData(clsTexture);

        } catch (IOException io) {
            System.out.println("Failed to pre-load the texture " + res);
            return null;
        }
    }

    public static class PreScannedImageData {

        public final ClsTexture texture;

        public PreScannedImageData(ClsTexture texture) {
            this.texture = texture;
        }

        public void bind(TextureManager manager) {
            ITextureObject current = manager.getTexture(texture.location());
            if (current != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, current.getGlTextureId());
            } else {
                manager.loadTexture(texture.location(), texture);
            }
        }
    }
}
