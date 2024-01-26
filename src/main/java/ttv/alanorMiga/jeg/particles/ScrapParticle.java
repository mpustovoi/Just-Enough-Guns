package ttv.alanorMiga.jeg.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ttv.alanorMiga.jeg.init.ModItems;

@OnlyIn(Dist.CLIENT)
public class ScrapParticle extends TextureSheetParticle {
    private final float uo;
    private final float vo;

    ScrapParticle(ClientLevel pLevel, double pX, double pY, double pZ, ItemStack itemStack) {
        super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
        var model = Minecraft.getInstance().getItemRenderer().getModel(itemStack, pLevel, (LivingEntity) null, 0);
        this.setSprite(model.getOverrides().resolve(model, itemStack, pLevel, null, 0).getParticleIcon(net.minecraftforge.client.model.data.EmptyModelData.INSTANCE));
        this.gravity = 0.75F;
        this.friction = 0.999F;
        this.hasPhysics = true;
        this.xd *= (double) 0.8F;
        this.yd *= (double) 0.8F;
        this.zd *= (double) 0.8F;
        this.yd = (double) (this.random.nextFloat() * 0.225F + 0.22F);
        this.quadSize = 0.1F;
        this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public float getQuadSize(float pScaleFactor) {
        float f = ((float) this.age + pScaleFactor) / (float) this.lifetime;
        return this.quadSize * (1.0F - f * f);
    }

    public void tick() {
        super.tick();
        if (!this.removed) {
            float f = (float) this.age / (float) this.lifetime;
            if (this.random.nextFloat() > f) {
                //this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }

    }

    protected float getU0() {
        return this.sprite.getU((double) ((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    protected float getU1() {
        return this.sprite.getU((double) (this.uo / 4.0F * 16.0F));
    }

    protected float getV0() {
        return this.sprite.getV((double) (this.vo / 4.0F * 16.0F));
    }

    protected float getV1() {
        return this.sprite.getV((double) ((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            ScrapParticle scrapParticle = new ScrapParticle(pLevel, pX, pY, pZ, new ItemStack(ModItems.SCRAP.get()));
            scrapParticle.setSpriteFromAge(this.sprite);
            return scrapParticle;
        }
    }
}