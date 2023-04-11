package com.Jeka8833.DataStructureKPI.work.pr;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBEasyFont;
import org.lwjgl.system.*;

import java.nio.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.Jeka8833.DataStructureKPI.work.ExitException;
import com.Jeka8833.DataStructureKPI.work.StepDownException;
import com.Jeka8833.DataStructureKPI.work.Task;
import org.jetbrains.annotations.NotNull;

public class PR4 implements Task {
    private static final Path OUT_PATH = Paths.get("DP0102");
    private static final Vector2i PADDING_LEFT_BOTTOM_CORNER = new Vector2i(50, 35);

    private final Vector2i windowSize = new Vector2i(300, 300);


    @Override
    public void run() throws ExitException, StepDownException {
        final CustomList<Vector2d> data = readFile(OUT_PATH);
        if (data == null) throw new StepDownException();

        final Vector2d maxValues = findMax(data);

        System.out.println("Max values: " + maxValues);

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        // The window handle
        long window2 = glfwCreateWindow(windowSize.x, windowSize.y, "Hello World!", NULL, NULL);
        if (window2 == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window2, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window2, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window2,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically


        glfwSetWindowSizeCallback(window2, (window1, windowWidth, windowHeight) -> {
            windowSize.set(windowWidth, windowHeight);

            glViewport(0, 0, windowSize.x, windowSize.y);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, windowSize.x, windowSize.y, 0, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
        });


        // Make the OpenGL context current
        glfwMakeContextCurrent(window2);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window2);


        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glViewport(0, 0, windowSize.x, windowSize.y);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowSize.x, windowSize.y, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window2)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glColor4f(1, 1, 1, 1);
            glBegin(GL_LINES);
            glVertex3f(0, windowSize.y - PADDING_LEFT_BOTTOM_CORNER.y, 0);
            glVertex3f(windowSize.x, windowSize.y - PADDING_LEFT_BOTTOM_CORNER.y, 0);

            glVertex3f(PADDING_LEFT_BOTTOM_CORNER.x, 0, 0);
            glVertex3f(PADDING_LEFT_BOTTOM_CORNER.x, windowSize.y, 0);

            Vector2d workArea = new Vector2d(new Vector2i(windowSize).sub(PADDING_LEFT_BOTTOM_CORNER));

            Vector2d lastElement = null;
            for (Vector2d next : data) {
                Vector2d scaled = new Vector2d(next).div(maxValues).mul(workArea);

                if (lastElement != null) {
                    glVertex3d(PADDING_LEFT_BOTTOM_CORNER.x + lastElement.x, workArea.y - lastElement.y, 0);
                    glVertex3d(PADDING_LEFT_BOTTOM_CORNER.x + scaled.x, workArea.y - scaled.y, 0);
                }
                lastElement = scaled;
            }
            glEnd();

            glColor4f(1, 1, 1, 0.5f);
            glEnableClientState(GL_VERTEX_ARRAY);
            for (int i = 1; i < 5; i++) {
                double posY = workArea.y - workArea.y / 5 * i;
                glBegin(GL_LINES);
                glVertex3d(PADDING_LEFT_BOTTOM_CORNER.x - 5, posY, 0);
                glVertex3d(windowSize.x, posY, 0);
                glEnd();

                glPushMatrix();
                glScalef(2, 2, 1f);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
                String text = String.format("%.2f", maxValues.y / 5 * i);
                ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
                int quads = STBEasyFont.stb_easy_font_print(0, (float) (posY / 2) - 3, text, null, charBuffer);
                glVertexPointer(2, GL_FLOAT, 16, charBuffer);
                glDrawArrays(GL_QUADS, 0, quads * 4);
                glPopMatrix();
            }
            for (int i = 1; i < 5; i++) {
                double posX = PADDING_LEFT_BOTTOM_CORNER.x + workArea.x / 5 * i;
                glBegin(GL_LINES);
                glVertex3d(posX, 0, 0);
                glVertex3d(posX, workArea.y + 5, 0);
                glEnd();

                glPushMatrix();
                glScalef(2, 2, 1f);
                glBindBuffer(GL_ARRAY_BUFFER, 0);
                String text = String.format("%.2f", maxValues.x / 5 * i);
                ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
                int quads = STBEasyFont.stb_easy_font_print((float) (posX / 2) - 10, (float) (workArea.y / 2) + 7, text, null, charBuffer);
                glVertexPointer(2, GL_FLOAT, 16, charBuffer);
                glDrawArrays(GL_QUADS, 0, quads * 4);
                glPopMatrix();
            }
            glDisableClientState(GL_VERTEX_ARRAY);

            glfwSwapBuffers(window2);

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }


        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window2);
        glfwDestroyWindow(window2);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    @Nullable
    private static PR4.CustomList<Vector2d> readFile(final Path path) {
        try {
            final List<String> rows = Files.readAllLines(path, StandardCharsets.UTF_8);

            CustomList<Vector2d> output = null;
            for (String s : rows) {
                final String[] col = s.split(" ", 2);

                final Vector2d row = new Vector2d(Double.parseDouble(col[0]), Double.parseDouble(col[1]));
                if (output == null)
                    output = new CustomList<>(row);
                else
                    output = output.add(row);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Contract("_->new")
    private static Vector2d findMax(@NotNull CustomList<Vector2d> data) {
        Vector2d max = new Vector2d(Double.MIN_VALUE, Double.MIN_VALUE);

        for (Vector2d next : data) max.max(next);

        return max;
    }

    @Override
    public @NotNull String name() {
        return "Практикум №4. Використання списків для збереження та відображення графічної інформації.";
    }

    public static class CustomList<T> implements Iterable<T> {
        private final T value;
        private final CustomList<T> next;

        public CustomList(T value) {
            this(value, null);
        }

        private CustomList(T value, CustomList<T> next) {
            this.value = value;
            this.next = next;
        }

        public CustomList<T> add(T next) {
            return new CustomList<>(next, this);
        }

        @NotNull
        @Override
        public Iterator<T> iterator() {
            final CustomList<T> thisObject = this;

            return new Iterator<T>() {
                private CustomList<T> current = thisObject;

                @Override
                public boolean hasNext() {
                    return current != null;
                }

                @Override
                public T next() {
                    T value = current.value;
                    current = current.next;
                    return value;
                }
            };
        }
    }
}
