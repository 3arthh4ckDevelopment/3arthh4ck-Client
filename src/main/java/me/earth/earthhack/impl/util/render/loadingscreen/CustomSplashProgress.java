package me.earth.earthhack.impl.util.render.loadingscreen;

import me.earth.earthhack.api.util.interfaces.Globals;
import me.earth.earthhack.impl.Earthhack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.asm.FMLSanityChecker;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.SharedDrawable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.lwjgl.opengl.GL11.*;

/**
 * More than half of this class code is from {@link SplashProgress}
 */

public class CustomSplashProgress implements Globals {
    private static Drawable d;
    private static volatile boolean pause;
    private static volatile boolean done;
    private static Thread thread;
    private static volatile Throwable threadError;
    private static final Lock lock;
    private static SplashFontRenderer fontRenderer;
    private static final IResourcePack mcPack;
    private static final IResourcePack fmlPack;
    private static IResourcePack miscPack;
    private static Texture fontTexture;
    private static Texture logoTexture;
    private static Properties config;
    private static int backgroundColor;
    static boolean isDisplayVSyncForced;
    static final Semaphore mutex;
    private static final IntBuffer buf;

    private static String getString(String name, String def) {
        String value = config.getProperty(name, def);
        config.setProperty(name, value);
        return value;
    }

    private static int getHex(String name, int def) {
        return Integer.decode(getString(name, "0x" + Integer.toString(def, 16).toUpperCase()));
    }

    public static void start() throws IOException {
        File configFile = new File(mc.gameDir, "config/splash.properties");
        File parent = configFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        config = new Properties();
        try (Reader r = new InputStreamReader(Files.newInputStream(configFile.toPath()), StandardCharsets.UTF_8)) {
            config.load(r);
        }
        catch (IOException e3) {
            FMLLog.log.info("Could not load splash.properties, will create a default one");
        }
        backgroundColor = getHex("background", 0xFFFFFF);
        ResourceLocation fontLoc = new ResourceLocation(getString("fontTexture", "textures/font/ascii.png"));
        ResourceLocation logoLoc = new ResourceLocation("earthhack:textures/gui/phobus.png");
        File miscPackFile = new File(mc.gameDir, getString("resourcePackPath", "resources"));
        try (Writer w = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            config.store(w, "Splash screen properties");
        }
        catch (IOException e) {
            FMLLog.log.error("Could not save the splash.properties file", e);
        }
        miscPack = createResourcePack(miscPackFile);
        FMLCommonHandler.instance().registerCrashCallable(new ICrashCallable() {
            public String call() {
                return "' Vendor: '" + GL11.glGetString(7936) + "' Version: '" + GL11.glGetString(7938) + "' Renderer: '" + GL11.glGetString(7937) + "'";
            }

            public String getLabel() {
                return "GL info";
            }
        });

        CrashReport report = CrashReport.makeCrashReport(new Throwable(), "Loading screen debug info");
        StringBuilder systemDetailsBuilder = new StringBuilder();
        report.getCategory().appendToStringBuilder(systemDetailsBuilder);
        FMLLog.log.info(systemDetailsBuilder.toString());
        try {
            d = new SharedDrawable(Display.getDrawable());
            Display.getDrawable().releaseContext();
            d.makeCurrent();
        }
        catch (LWJGLException e2) {
            FMLLog.log.error("Error starting SplashProgress:", e2);
            disableSplash(e2);
        }

        (thread = new Thread(new Runnable() {
            private long updateTiming;
            private long framecount;

            @Override
            public void run() {
                setGL();
                fontTexture = new Texture(fontLoc, null);
                logoTexture = new Texture(logoLoc, null, false);
                glEnable(GL_TEXTURE_2D);
                fontRenderer = new SplashFontRenderer();
                glDisable(GL_TEXTURE_2D);
                while (!done) {
                    framecount++;
                    ProgressManager.ProgressBar first = null;
                    ProgressManager.ProgressBar penult = null;
                    ProgressManager.ProgressBar last = null;
                    Iterator<ProgressManager.ProgressBar> i = ProgressManager.barIterator();

                    while(i.hasNext()) {
                        if (first == null) {
                            first = i.next();
                        } else {
                            penult = last;
                            last = i.next();
                        }
                    }

                    GL11.glPushMatrix();
                    GL11.glClear(16384);
                    int w = Display.getWidth();
                    int h = Display.getHeight();
                    GL11.glViewport(0, 0, w, h);
                    GL11.glMatrixMode(5889);
                    GL11.glLoadIdentity();
                    GL11.glOrtho((320 - w / 2), (320 + w / 2), (240 + h / 2), (240 - h / 2), -1.0, 1.0);
                    GL11.glMatrixMode(5888);
                    GL11.glLoadIdentity();
                    int left = 320 - w / 2;
                    int right = 320 + w / 2;
                    int bottom = 240 + h / 2;
                    int top = 240 - h / 2;

                    GL11.glPushMatrix();
                    glEnable(GL_TEXTURE_2D);
                    logoTexture.bind();
                    glBegin(GL_QUADS);
                    logoTexture.texCoord(0, 0, 0);
                    glVertex2f(left, top);
                    logoTexture.texCoord(0, 0, 1);
                    glVertex2f(left, bottom);
                    logoTexture.texCoord(0, 1, 1);
                    glVertex2f(right, bottom);
                    logoTexture.texCoord(0, 1, 0);
                    glVertex2f(right, top);
                    glEnd();
                    glDisable(GL_TEXTURE_2D);
                    GL11.glPopMatrix();

                    setColor(0x222222);
                    int barTop = bottom - 75;
                    GL11.glBegin(7);
                    GL11.glVertex2f(left, bottom);
                    GL11.glVertex2f(right, bottom);
                    GL11.glVertex2f(right, barTop);
                    GL11.glVertex2f(left, barTop);
                    GL11.glEnd();

                    GL11.glPushMatrix();
                    int scale = 2;
                    int fontY = bottom - 35 - fontRenderer.FONT_HEIGHT;
                    int fontX = left + 15;
                    String progress = "Initializing...";
                    if (first != null)
                        progress = first.getTitle() + " - " + first.getMessage();
                    setColor(16777215);
                    GL11.glScalef(scale, scale, 1.0f);
                    GL11.glEnable(3553);
                    fontRenderer.drawString(Earthhack.NAME + " | " + progress, fontX / scale, fontY / scale, 16777215);
                    GL11.glDisable(3553);
                    GL11.glPopMatrix();

                    if (first != null) {
                        GL11.glPushMatrix();
                        String progressPercentage = first.getStep() / first.getSteps() * 100 + "%";
                        setColor(16777215);
                        GL11.glScalef(scale, scale, 1.0f);
                        GL11.glEnable(3553);
                        fontRenderer.drawString(progressPercentage, (right - 50 - fontRenderer.getStringWidth(progressPercentage)) / scale, (bottom - 35 - fontRenderer.FONT_HEIGHT) / scale, 16777215);
                        GL11.glDisable(3553);
                        GL11.glPopMatrix();
                    }
                    GL11.glPopMatrix();

                    mutex.acquireUninterruptibly();
                    long updateStart = System.nanoTime();
                    Display.update();
                    long dur = System.nanoTime() - updateStart;
                    if (framecount < 200L) {
                        updateTiming += dur;
                    }
                    mutex.release();
                    if (pause) {
                        clearGL();
                        setGL();
                    }
                    if (framecount >= 200L && updateTiming > 1000000000L) {
                        if (!isDisplayVSyncForced) {
                            isDisplayVSyncForced = true;
                            FMLLog.log.info("Using alternative sync timing : {} frames of Display.update took {} nanos", 200, updateTiming);
                        }
                        try {
                            Thread.sleep(16L);
                        }
                        catch (InterruptedException ignored) {}
                    }
                    else {
                        if (framecount == 200L) {
                            FMLLog.log.info("Using sync timing. {} frames of Display.update took {} nanos", 200, updateTiming);
                        }
                        Display.sync(100);
                    }
                }
                clearGL();
            }

            private void setColor(int color) {
                GL11.glColor3ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF));
            }

            private void setGL() {
                lock.lock();
                try {
                    Display.getDrawable().makeCurrent();
                }
                catch (LWJGLException e) {
                    FMLLog.log.error("Error setting GL context:", e);
                    throw new RuntimeException(e);
                }
                backgroundColor = Color.cyan.getRGB();
                GL11.glClearColor((backgroundColor >> 16 & 0xFF) / 255.0f, (backgroundColor >> 8 & 0xFF) / 255.0f, (backgroundColor & 0xFF) / 255.0f, 1.0f);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
            }

            private void clearGL() {
                mc.displayWidth = Display.getWidth();
                mc.displayHeight = Display.getHeight();
                mc.resize(mc.displayWidth, mc.displayHeight);
                GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glEnable(2929);
                GL11.glDepthFunc(515);
                GL11.glEnable(3008);
                GL11.glAlphaFunc(516, 0.1f);
                try {
                    Display.getDrawable().releaseContext();
                }
                catch (LWJGLException e) {
                    FMLLog.log.error("Error releasing GL context:", e);
                    throw new RuntimeException(e);
                }
                finally {
                    lock.unlock();
                }
            }
        })).setUncaughtExceptionHandler((t, e) -> {
            FMLLog.log.error("Splash thread Exception", e);
            threadError = e;
        });
        thread.start();
        checkThreadState();
    }

    private static void checkThreadState() {
        if (thread.getState() == Thread.State.TERMINATED || threadError != null) {
            throw new IllegalStateException("Splash thread", threadError);
        }
    }

    @Deprecated
    public static void pause() {
        checkThreadState();
        pause = true;
        lock.lock();
        try {
            d.releaseContext();
            Display.getDrawable().makeCurrent();
        } catch (LWJGLException e) {
            FMLLog.log.error("Error setting GL context:", e);
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public static void resume() {
        checkThreadState();
        pause = false;
        try {
            Display.getDrawable().releaseContext();
            d.makeCurrent();
        } catch (LWJGLException e) {
            FMLLog.log.error("Error releasing GL context:", e);
            throw new RuntimeException(e);
        }
        lock.unlock();
    }

    public static void finish() {
        try {
            checkThreadState();
            done = true;
            thread.join();
            GL11.glFlush();
            d.releaseContext();
            Display.getDrawable().makeCurrent();
            fontTexture.delete();
            logoTexture.delete();
        }
        catch (Exception e) {
            FMLLog.log.error("Error finishing SplashProgress:", e);
            disableSplash(e);
        }
    }

    private static void disableSplash(Exception e) {
        if (disableSplash()) {
            throw new EnhancedRuntimeException(e) {
                protected void printStackTrace(WrappedPrintStream stream) {
                    stream.println("SplashProgress has detected a error loading Minecraft.");
                    stream.println("This can sometimes be caused by bad video drivers.");
                    stream.println("We have automatically disabled the new Splash Screen in config/splash.properties.");
                    stream.println("Try reloading minecraft before reporting any errors.");
                }
            };
        }
        throw new EnhancedRuntimeException(e) {
            protected void printStackTrace(WrappedPrintStream stream) {
                stream.println("SplashProgress has detected a error loading Minecraft.");
                stream.println("This can sometimes be caused by bad video drivers.");
                stream.println("Please try disabling the new Splash Screen in config/splash.properties.");
                stream.println("After doing so, try reloading minecraft before reporting any errors.");
            }
        };
    }

    private static boolean disableSplash() {
        File configFile = new File(mc.gameDir, "config/splash.properties");
        File parent = configFile.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        config.setProperty("enabled", "false");
        try (Writer w = new OutputStreamWriter(Files.newOutputStream(configFile.toPath()), StandardCharsets.UTF_8)) {
            config.store(w, "Splash screen properties");
        } catch (IOException e) {
            FMLLog.log.error("Could not save the splash.properties file", e);
            return false;
        }
        return true;
    }

    private static IResourcePack createResourcePack(File file) {
        return (file.isDirectory() ? new FolderResourcePack(file) : new FileResourcePack(file));
    }

    private static InputStream open(ResourceLocation loc, @Nullable ResourceLocation fallback, boolean allowResourcePack) throws IOException {
        if (!allowResourcePack) {
            return mcPack.getInputStream(loc);
        } else if (miscPack.resourceExists(loc)) {
            return miscPack.getInputStream(loc);
        } else if (fmlPack.resourceExists(loc)) {
            return fmlPack.getInputStream(loc);
        } else {
            return !mcPack.resourceExists(loc) && fallback != null ? open(fallback, null, true) : mcPack.getInputStream(loc);
        }
    }

    static {
        pause = false;
        done = false;
        lock = new ReentrantLock(true);
        mcPack = mc.defaultResourcePack;
        fmlPack = createResourcePack(FMLSanityChecker.fmlLocation);
        isDisplayVSyncForced = false;
        mutex = new Semaphore(1);
        buf = BufferUtils.createIntBuffer(4194304);
    }

    private static class Texture {
        private final ResourceLocation location;
        private final int name;
        private final int width;
        private final int height;
        private final int size;

        public Texture(ResourceLocation location, @Nullable ResourceLocation fallback) {
            this(location, fallback, true);
        }

        public Texture(ResourceLocation location, @Nullable ResourceLocation fallback, boolean allowRP) {
            InputStream s = null;

            try {
                this.location = location;
                s = open(location, fallback, allowRP);
                ImageInputStream stream = ImageIO.createImageInputStream(s);
                Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
                if (!readers.hasNext()) {
                    throw new IOException("No suitable reader found for image" + location);
                }

                ImageReader reader = readers.next();
                reader.setInput(stream);
                int frames = reader.getNumImages(true);
                BufferedImage[] images = new BufferedImage[frames];

                int height;
                for (height = 0; height < frames; ++height) {
                    images[height] = reader.read(height);
                }

                reader.dispose();
                this.width = images[0].getWidth();
                height = images[0].getHeight();
                int i;
                if (height > this.width && height % this.width == 0) {
                    frames = height / this.width;
                    BufferedImage original = images[0];
                    height = this.width;
                    images = new BufferedImage[frames];

                    for (i = 0; i < frames; ++i) {
                        images[i] = original.getSubimage(0, i * height, this.width, height);
                    }
                }
                this.height = height;

                int size;
                for (size = 1; size / this.width * (size / height) < frames; size *= 2) { }

                this.size = size;
                GL11.glEnable(3553);
                synchronized(SplashProgress.class) {
                    this.name = GL11.glGenTextures();
                    GL11.glBindTexture(3553, this.name);
                }

                GL11.glTexParameteri(3553, 10241, 9728);
                GL11.glTexParameteri(3553, 10240, 9728);
                GL11.glTexImage2D(3553, 0, 6408, size, size, 0, 32993, 33639, (IntBuffer)null);
                SplashProgress.checkGLError("Texture creation");

                for (i = 0; i * (size / this.width) < frames; ++i) {
                    for(int j = 0; i * (size / this.width) + j < frames && j < size / this.width; ++j) {
                        buf.clear();
                        BufferedImage image = images[i * (size / this.width) + j];

                        for(int k = 0; k < height; ++k) {
                            for(int l = 0; l < this.width; ++l) {
                                buf.put(image.getRGB(l, k));
                            }
                        }

                        buf.position(0).limit(this.width * height);
                        GL11.glTexSubImage2D(3553, 0, j * this.width, i * height, this.width, height, 32993, 33639, buf);
                        SplashProgress.checkGLError("Texture uploading");
                    }
                }

                GL11.glBindTexture(3553, 0);
                GL11.glDisable(3553);
            } catch (IOException e) {
                FMLLog.log.error("Error reading texture from file: {}", location, e);
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(s);
            }

        }

        public ResourceLocation getLocation() {
            return location;
        }

        public void bind() {
            GL11.glBindTexture(3553, name);
        }

        public void delete() {
            GL11.glDeleteTextures(name);
        }

        public float getU(int frame, float u) {
            return (float) width * ((float)(frame % (size / width)) + u) / size;
        }

        public float getV(int frame, float v) {
            return (float) height * ((float)(frame / (size / width)) + v) / size;
        }

        public void texCoord(int frame, float u, float v) {
            GL11.glTexCoord2f( getU(frame, u), getV(frame, v));
        }
    }

    private static class SplashFontRenderer extends FontRenderer {
        public SplashFontRenderer() {
            super(mc.gameSettings, fontTexture.getLocation(), (TextureManager) null, false);
            super.onResourceManagerReload((IResourceManager) null);
        }

        protected void bindTexture(@Nonnull ResourceLocation location) {
            if (location != this.locationFontTexture) {
                throw new IllegalArgumentException();
            } else {
                fontTexture.bind();
            }
        }

        @Nonnull
        protected IResource getResource(@Nonnull ResourceLocation location) throws IOException {
            DefaultResourcePack pack = mc.defaultResourcePack;
            return new SimpleResource(pack.getPackName(), location, pack.getInputStream(location), (InputStream)null, (MetadataSerializer)null);
        }
    }
}
