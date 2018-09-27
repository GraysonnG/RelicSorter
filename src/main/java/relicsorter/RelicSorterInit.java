package relicsorter;

import basemod.*;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import relicsorter.util.TextureLoader;

import java.util.ArrayList;

public class RelicSorterInit implements PostInitializeSubscriber {

	private InputProcessor oldInputProcessor;
	private static final float DEFAULT_X = 350f;
	private static final float DEFAULT_Y = 650f;

	private static final float MOUSEBIND_DEFAULT_Y = DEFAULT_Y - 50f;
	private static final float MOUSEBIND_DEFAULT_LABEL_Y= DEFAULT_Y;

	private static final float MOUSEBIND_LABEL_1_X = DEFAULT_X + 50f;
	private static final float MOUSEBIND_LABEL_2_X = DEFAULT_X + 225f;
	private static final float MOUSEBIND_LABEL_3_X = DEFAULT_X + 450f;

	private static final float MOUSEBIND_BUTTON_1_X = DEFAULT_X + 100f;
	private static final float MOUSEBIND_BUTTON_2_X = DEFAULT_X + 300f;
	private static final float MOUSEBIND_BUTTON_3_X = DEFAULT_X + 500f;

	private static final float KEYBIND_BUTTON_X = DEFAULT_X + 25f;
	private static final float KEYBIND_BUTTON_Y = DEFAULT_Y - 275f;
	private static final float KEYBIND_LABEL_X = DEFAULT_X + 150f;
	private static final float KEYBIND_LABEL_Y = KEYBIND_BUTTON_Y + 50f;


	private ArrayList<ModToggleButton> radioButtons = new ArrayList<>();

	@Override
	public void receivePostInitialize() {
		ModPanel settingsPanel = new ModPanel();

		ModLabel mouseBindingLabel = new ModLabel("Mouse Binding:", DEFAULT_X + 25f, DEFAULT_Y + 75f, settingsPanel, (me) -> {});
		ModLabel keyBindingLabel = new ModLabel("Keyboard Binding:", DEFAULT_X + 25f, KEYBIND_LABEL_Y + 75f, settingsPanel, (me) -> {});

		ModLabel keyBindingButtonLabel = new ModLabel("", KEYBIND_LABEL_X, KEYBIND_LABEL_Y, settingsPanel, (me)-> {
			if(me.parent.waitingOnEvent){
				me.text = "Press Key";
			}else{
				me.text = "Change sorting hotkey ( " + Input.Keys.toString(RelicSorter.keyBind) + " )";
			}
		});

		ModButton keyBindingButton = new ModButton(KEYBIND_BUTTON_X, KEYBIND_BUTTON_Y, settingsPanel, (me) -> {
			me.parent.waitingOnEvent = true;
			oldInputProcessor = Gdx.input.getInputProcessor();
			Gdx.input.setInputProcessor(new InputAdapter() {
				@Override
				public boolean keyUp(int keycode){
					RelicSorter.keyBind = keycode;
					RelicSorter.save();
					me.parent.waitingOnEvent = false;
					Gdx.input.setInputProcessor(oldInputProcessor);
					return true;
				}
			});
		});

		ModToggleButton leftClickButton = new ModToggleButton(MOUSEBIND_BUTTON_1_X, MOUSEBIND_DEFAULT_Y,
			settingsPanel, (me) -> {
				doRadioButton(0);
				RelicSorter.save();
			});

		ModToggleButton middleClickButton = new ModToggleButton(MOUSEBIND_BUTTON_2_X, MOUSEBIND_DEFAULT_Y,
			settingsPanel, (me) -> {
			doRadioButton(1);
			RelicSorter.save();
		});

		ModToggleButton rightClickButton = new ModToggleButton(MOUSEBIND_BUTTON_3_X, MOUSEBIND_DEFAULT_Y,
			settingsPanel, (me) -> {
			doRadioButton(2);
			RelicSorter.save();
		});

		ModLabel leftClickLabel = new ModLabel("Left Click", MOUSEBIND_LABEL_1_X, MOUSEBIND_DEFAULT_LABEL_Y, settingsPanel, (me) -> {});
		ModLabel middleClickLabel = new ModLabel("Middle Click", MOUSEBIND_LABEL_2_X, MOUSEBIND_DEFAULT_LABEL_Y, settingsPanel, (me) -> {});
		ModLabel rightClickLabel = new ModLabel("Right Click", MOUSEBIND_LABEL_3_X, MOUSEBIND_DEFAULT_LABEL_Y, settingsPanel, (me) -> {});

		settingsPanel.addUIElement(mouseBindingLabel);
		settingsPanel.addUIElement(keyBindingLabel);

		settingsPanel.addUIElement(leftClickLabel);
		settingsPanel.addUIElement(middleClickLabel);
		settingsPanel.addUIElement(rightClickLabel);

		settingsPanel.addUIElement(leftClickButton);
		settingsPanel.addUIElement(middleClickButton);
		settingsPanel.addUIElement(rightClickButton);

		settingsPanel.addUIElement(keyBindingButton);
		settingsPanel.addUIElement(keyBindingButtonLabel);

		radioButtons.add(leftClickButton);
		radioButtons.add(middleClickButton);
		radioButtons.add(rightClickButton);

		BaseMod.registerModBadge(
			TextureLoader.getTexture("img/relicsorter/badge.png"), RelicSorter.NAME, RelicSorter.AUTHOR, RelicSorter.DESC,
			settingsPanel);

		doRadioButton(-1);
	}

	public void doRadioButton(int ID){
		switch(ID){
			case -1:
				switch (RelicSorter.binding){
					case LEFT:
						radioButtons.get(0).enabled = true;
						break;
					case RIGHT:
						radioButtons.get(2).enabled = true;
						break;
					case MIDDLE:
						radioButtons.get(1).enabled = true;
						break;
				}
				break;
			case 0:
				RelicSorter.binding = RelicSorter.BindingEnum.LEFT;
				break;
			case 1:
				RelicSorter.binding = RelicSorter.BindingEnum.MIDDLE;
				break;
			case 2:
				RelicSorter.binding = RelicSorter.BindingEnum.RIGHT;
				break;
		}

		if(ID == -1)
			return;

		for(ModToggleButton button : radioButtons){
			button.enabled = false;
		}

		radioButtons.get(ID).enabled = true;
	}
}
