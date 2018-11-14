package relicsorter;

import basemod.BaseMod;
import basemod.interfaces.PreDungeonUpdateSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.IOException;

@SpireInitializer
public class RelicSorter implements PreDungeonUpdateSubscriber{
	
    public static final String NAME = "Relic Sorter";
	public static final String AUTHOR = "Blank The Evil";
	public static final String DESC = "Allows you to send relics to the front of the list by right clicking them.";
	public static BindingEnum binding;
	public static int keyBind;
	public enum BindingEnum {
	    LEFT,
        RIGHT,
        MIDDLE
    }

	public RelicSorter() {
		BaseMod.subscribe(this);
		BaseMod.subscribe(new RelicSorterInit());
        load();
    }

    public static void load(){
	    try {
            SpireConfig config = new SpireConfig("relicSorter", "config");
            config.load();
            String valueE = config.getString("BindingEnum");
            int valueK = config.getInt("KeyBinding");
            if(valueE != null){
                binding = BindingEnum.valueOf(valueE);
            }
            keyBind = valueK;
        }catch(IOException | NumberFormatException e){
	        log("Failed to load settings!");
	        binding = BindingEnum.RIGHT;
	        keyBind = Input.Keys.R;
        }
    }

    public static void save(){
        try {
            SpireConfig config = new SpireConfig("relicSorter", "config");
            config.setString("BindingEnum", binding.toString());
            config.setInt("KeyBinding", keyBind);

            log("Saving keybinding as: " + Input.Keys.toString(keyBind));

            config.save();
        }catch (IOException e){
            log("Failed to save settings!");
        }
    }

    @Override
    public void receivePreDungeonUpdate() {
	    if(AbstractDungeon.player == null || AbstractDungeon.player.relics == null)
	        return;

	    AbstractPlayer player = AbstractDungeon.player;

        boolean clicked = false;
        switch(binding){
            case LEFT:
                clicked = InputHelper.justClickedLeft;
                break;
            case RIGHT:
                clicked = InputHelper.justClickedRight;
                break;
            case MIDDLE:
                clicked = handleMiddleClick();
                break;
        }
        if(!clicked)
            clicked = Gdx.input.isKeyJustPressed(keyBind);

        if(player.relics.size() > 2 && clicked) {
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

                switch (binding) {
                    case LEFT:
                        InputHelper.justClickedLeft = false;
                        break;
                    case RIGHT:
                        InputHelper.justClickedRight = false;
                        break;
                    case MIDDLE:
                        break;
                }
            }
        }
    }

    public static void log(String ... items) {
        System.out.print("RelicSorter ");
        for(String item : items) {
            System.out.print(" : " + item);
        }
        System.out.println();
    }

    public static void initialize() {
        log("Version", "1.1.1");
        new RelicSorter();
    }

    private static void moveRelic(AbstractRelic r1, AbstractRelic r2){
        r1.currentX = r2.currentX;
        r1.hb.x = r2.hb.x;
    }

    private static void moveRelic(AbstractRelic r1, float currentX, float hbX){
        r1.currentX = currentX;
        r1.hb.x= hbX;
    }

    private static boolean justPressedMiddleLast = false;

    private static boolean handleMiddleClick(){
        boolean isMiddleDown = Gdx.input.isButtonPressed(Input.Buttons.MIDDLE);
        if(isMiddleDown && !justPressedMiddleLast) {
            justPressedMiddleLast = true;
            return true;
        }

        if(!isMiddleDown && justPressedMiddleLast)
            justPressedMiddleLast = false;

        return false;
    }
	
}