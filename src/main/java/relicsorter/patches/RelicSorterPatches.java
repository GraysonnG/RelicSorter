package relicsorter.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import relicsorter.util.MathHelper;

public class RelicSorterPatches {

	private static final float ANIMATION_SPEED = 30f;

	@SpirePatch(clz = AbstractRelic.class, method = SpirePatch.CLASS)
	public static class RelicSorterFields {
		public static SpireField<Boolean> isMoving = new SpireField<>(() -> false);
	}

	@SpirePatch(clz = AbstractRelic.class, method = "update")
	public static class RelicSorterTargetFix {
		@SpirePostfixPatch
		public static void updatePatch(AbstractRelic __instance) {
			if(RelicSorterFields.isMoving.get(__instance)) {
				boolean shouldStayMoving = !MathHelper.fuzzyEquals(__instance.targetX, __instance.currentX, 0.1f);
				if (shouldStayMoving) {
					__instance.currentX = MathUtils.lerp(__instance.currentX, __instance.targetX, Gdx.graphics.getDeltaTime() * ANIMATION_SPEED);
				} else {
					__instance.currentX = __instance.targetX;
					RelicSorterFields.isMoving.set(__instance, false);
					AbstractDungeon.topPanel.adjustRelicHbs();
					//__instance.hb.move(__instance.currentX, __instance.currentY);
				}
			}
		}
	}
}
