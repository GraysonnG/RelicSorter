package relicsorter;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RelicSorter {

    public RelicSorter() {

    }

    public static void log(String ... items) {
        System.out.print("RelicSorter ");
        for(String item : items) {
            System.out.print(" : " + item);
        }
        System.out.println();
    }

    public static void initialize() {
        log("Version", "0.0.1");
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "update")
    public static class PlayerUpdate{
        public static void Postfix(AbstractPlayer player) {
            boolean rightClicked = InputHelper.justClickedRight;
            if(AbstractDungeon.player.relics.size() > 2 && rightClicked) {
                int selIndex = -1;
                for (int i = AbstractDungeon.player.relics.size() - 1; i >= 2; i--) {
                    AbstractRelic relic = AbstractDungeon.player.relics.get(i);
                    if (relic.hb.hovered) {
                        selIndex = i;
                        log("Clicked", relic.name, "" + i);
                    }
                }
                if(selIndex != -1) {
                    float tempCX = AbstractDungeon.player.relics.get(selIndex).currentX;
                    float temphX = AbstractDungeon.player.relics.get(selIndex).hb.x;

                    moveRelic(AbstractDungeon.player.relics.get(selIndex), AbstractDungeon.player.relics.get(1));

                    AbstractRelic temp = AbstractDungeon.player.relics.remove(selIndex);
                    AbstractDungeon.player.relics.add(1, temp);


                    for (int i = 2; i < selIndex; i++) {
                        AbstractRelic r1 = AbstractDungeon.player.relics.get(i);
                        AbstractRelic r2 = AbstractDungeon.player.relics.get(i + 1);
                        log("Moving", r1.name, "to", r2.name);
                        moveRelic(r1, r2);
                    }

                    moveRelic(AbstractDungeon.player.relics.get(selIndex), tempCX, temphX);
                }
            }
        }
    }

    private static void moveRelic(AbstractRelic r1, AbstractRelic r2){
        r1.currentX = r2.currentX;
        r1.hb.x = r2.hb.x;
    }

    private static void moveRelic(AbstractRelic r1, float currentX, float hbX){
        r1.currentX = currentX;
        r1.hb.x= hbX;
    }
}