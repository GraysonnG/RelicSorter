package relicsorter;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import relicsorter.util.TextureLoader;

@SpireInitializer
public class RelicSorter implements PostInitializeSubscriber{
	
    private static final String NAME = "Relic Sorter";
	private static final String AUTHOR = "Blank The Evil";
	private static final String DESC = "Allows you to send relics to the front of the list by right clicking them.";

	public RelicSorter() {
		BaseMod.subscribe(this);
    }
    
    public void receivePostInitialize() {
		BaseMod.registerModBadge(TextureLoader.getTexture("img/relicsorter/badge.png"), NAME, AUTHOR, DESC, null);
	}

    public static void log(String ... items) {
        System.out.print("RelicSorter ");
        for(String item : items) {
            System.out.print(" : " + item);
        }
        System.out.println();
    }

    public static void initialize() {
        log("Version", "1.0.0");
        new RelicSorter();
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.characters.AbstractPlayer", method = "update")
    public static class PlayerUpdate{
        public static void Postfix(AbstractPlayer player) {
            boolean rightClicked = InputHelper.justClickedRight;
            if(player.relics.size() > 2 && rightClicked) {
                int selIndex = -1;
                for (int i = player.relics.size() - 1; i >= 2; i--) {
                    AbstractRelic relic = player.relics.get(i);
                    if (relic.hb.hovered) {
                        selIndex = i;
                        log("Clicked", relic.name, "" + i);
                    }
                }
                if(selIndex != -1) {
                    float tempCX = player.relics.get(selIndex).currentX;
                    float temphX = player.relics.get(selIndex).hb.x;

                    moveRelic(player.relics.get(selIndex), player.relics.get(1));

                    AbstractRelic temp = player.relics.remove(selIndex);
                    player.relics.add(1, temp);


                    for (int i = 2; i < selIndex; i++) {
                        AbstractRelic r1 = player.relics.get(i);
                        AbstractRelic r2 = player.relics.get(i + 1);
                        log("Moving", r1.name, "to", r2.name);
                        moveRelic(r1, r2);
                    }

                    moveRelic(player.relics.get(selIndex), tempCX, temphX);
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