package me.hp20.giz5.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hp20.giz5.Giz5Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    private static boolean supportSodium = true;

    /**
     * @reason Disables fire overlay
     * @author hp20
     */
    @Inject(at = @At("HEAD"), method = "renderFireOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", cancellable = true)
    private static void renderFireOverlay(MinecraftClient arg, MatrixStack arg2, CallbackInfo info) {
        BufferBuilder lv = Tessellator.getInstance().getBuffer();
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableTexture();
        Sprite lv2 = ModelLoader.FIRE_1.getSprite();
        arg.getTextureManager().bindTexture(lv2.getAtlas().getId());
        float f = lv2.getMinU();
        float g = lv2.getMaxU();
        float h = (f + g) / 2.0f;
        float i = lv2.getMinV();
        float j = lv2.getMaxV();
        float k = (i + j) / 2.0f;
        float l = lv2.getAnimationFrameDelta();
        float m = MathHelper.lerp(l, f, h);
        float n = MathHelper.lerp(l, g, h);
        float o = MathHelper.lerp(l, i, k);
        float p = MathHelper.lerp(l, j, k);
        for (int r = 0; r < 2; ++r) {
            arg2.push();
            float modifier = (float) -Giz5Mod.getOptions().lowFire / 200;
            arg2.translate((float) (-(r * 2 - 1)) * 0.24f, -0.3f, 0.0);
            arg2.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (r * 2 - 1) * 10.0f));
            Matrix4f lv3 = arg2.peek().getModel();
            lv.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
            lv.vertex(lv3, -0.5f, -0.5f + modifier, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(n, p).next();
            lv.vertex(lv3, 0.5f, -0.5f + modifier, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(m, p).next();
            lv.vertex(lv3, 0.5f, 0.5f + modifier, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(m, o).next();
            lv.vertex(lv3, -0.5f, 0.5f + modifier, -0.5f).color(1.0f, 1.0f, 1.0f, 0.9f).texture(n, o).next();
            lv.end();
            BufferRenderer.draw(lv);
            arg2.pop();
        }
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);

        //sodium support
        Sprite sprite = ModelLoader.FIRE_1.getSprite();
        System.out.println(sprite.getClass().getCanonicalName());
        if (supportSodium) {
            try {
                Method method = sprite.getClass().getMethod("markActive");
                method.invoke(sprite);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                supportSodium = false;
            }
        }
        info.cancel();
    }
}
