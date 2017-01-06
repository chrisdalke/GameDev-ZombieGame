/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.ai.tests;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.tests.steer.SteeringTestBase;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dArriveTest;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dCollisionAvoidanceTest;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dFaceTest;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dJumpTest;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dLookWhereYouAreGoingTest;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dRaycastObstacleAvoidanceTest;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dSeekTest;
import com.badlogic.gdx.ai.tests.steer.box2d.tests.Box2dWanderTest;
import com.badlogic.gdx.ai.tests.steer.bullet.tests.BulletFaceTest;
import com.badlogic.gdx.ai.tests.steer.bullet.tests.BulletFollowPathTest;
import com.badlogic.gdx.ai.tests.steer.bullet.tests.BulletJumpTest;
import com.badlogic.gdx.ai.tests.steer.bullet.tests.BulletLookWhereYouAreGoingTest;
import com.badlogic.gdx.ai.tests.steer.bullet.tests.BulletRaycastObstacleAvoidanceTest;
import com.badlogic.gdx.ai.tests.steer.bullet.tests.BulletSeekTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.formation.Scene2dFormationTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dArriveTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dCollisionAvoidanceTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dFaceTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dFlockingTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dFollowFlowFieldTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dFollowPathTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dHideTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dInterposeTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dLookWhereYouAreGoingTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dPursueTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dRaycastObstacleAvoidanceTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dSeekTest;
import com.badlogic.gdx.ai.tests.steer.scene2d.tests.Scene2dWanderTest;
import com.badlogic.gdx.ai.tests.utils.GdxAiTestUtils;
import com.badlogic.gdx.ai.tests.utils.scene2d.CollapsableWindow;
import com.badlogic.gdx.ai.tests.utils.scene2d.FpsLabel;
import com.badlogic.gdx.ai.tests.utils.scene2d.PauseButton;
import com.badlogic.gdx.ai.tests.utils.scene2d.TabbedPane;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/** Test class for steering behaviors.
 * 
 * @author davebaol */
public class SteeringBehaviorsTest extends ApplicationAdapter {

	public static void main (String[] argv) {
		GdxAiTestUtils.launch(new SteeringBehaviorsTest());
	}

	private static final boolean DEBUG_STAGE = false;

	private static final String[] ENGINES = {"Scene2d", "Box2d", "Bullet"};

	private CollapsableWindow testSelectionWindow;
	Label testHelpLabel;
	TextButton pauseButton;

	// @off - disable libgdx formatter
	// Keep it sorted!
	private SteeringTestBase[][] tests = {
		{ // Scene2d
			new Scene2dArriveTest(this),
			new Scene2dCollisionAvoidanceTest(this),
			new Scene2dFaceTest(this),
			new Scene2dFlockingTest(this),
			new Scene2dFollowFlowFieldTest(this),
			new Scene2dFollowPathTest(this, false),
			new Scene2dFollowPathTest(this, true),
			new Scene2dFormationTest(this, true),
			new Scene2dFormationTest(this, false),
			new Scene2dHideTest(this),
			new Scene2dInterposeTest(this),
			new Scene2dLookWhereYouAreGoingTest(this),
			new Scene2dPursueTest(this),
			new Scene2dRaycastObstacleAvoidanceTest(this),
			new Scene2dSeekTest(this),
			new Scene2dWanderTest(this)
		},
		{ // Box2d
			new Box2dArriveTest(this),
			new Box2dCollisionAvoidanceTest(this),
			new Box2dFaceTest(this),
			new Box2dJumpTest(this),
			new Box2dLookWhereYouAreGoingTest(this),
			new Box2dRaycastObstacleAvoidanceTest(this),
			new Box2dSeekTest(this),
			new Box2dWanderTest(this)
		},
		{ // Bullet
			new BulletFaceTest(this),
			new BulletFollowPathTest(this, false),
			new BulletFollowPathTest(this, true),
			new BulletJumpTest(this),
			new BulletLookWhereYouAreGoingTest(this),
			new BulletRaycastObstacleAvoidanceTest(this),
			new BulletSeekTest(this)
		}
	};
	// @on - enable libgdx formatter

	SteeringTestBase currentTest;

	public Stage stage;
	public float stageWidth;
	public float stageHeight;
	public Skin skin;

	public TextureRegion greenFish;
	public TextureRegion cloud;
	public TextureRegion badlogicSmall;
	public TextureRegion target;

	@Override
	public void create () {
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);

		greenFish = new TextureRegion(new Texture("data/green_fish.png"));
		cloud = new TextureRegion(new Texture("data/particle-cloud.png"));
		badlogicSmall = new TextureRegion(new Texture("data/badlogicsmall.jpg"));
		target = new TextureRegion(new Texture("data/target.png"));

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		stage = new Stage();
		stage.setDebugAll(DEBUG_STAGE);
		stageWidth = stage.getWidth();
		stageHeight = stage.getHeight();

		Gdx.input.setInputProcessor(new InputMultiplexer(stage));

		// Add translucent panel (it's only visible when AI is paused)
		final Image translucentPanel = new Image(skin, "translucent");
		translucentPanel.setSize(stageWidth, stageHeight);
		translucentPanel.setVisible(false);
		stage.addActor(translucentPanel);

		// Create test selection window
		Array<List<String>> engineTests = new Array<List<String>>();
		for (int k = 0; k < tests.length; k++) {
			engineTests.add(createTestList(k));
		}
		testSelectionWindow = addTestSelectionWindow("Behaviors", ENGINES, engineTests, 0, -1);

		// Create status bar
		Table statusBar = new Table(skin);
		statusBar.left().bottom();
		statusBar.row().height(26);
		statusBar.add(pauseButton = new PauseButton(translucentPanel, skin)).width(90).left();
		statusBar.add(new FpsLabel("FPS: ", skin)).padLeft(15);
		statusBar.add(testHelpLabel = new Label("", skin)).padLeft(15);
		stage.addActor(statusBar);

		// Set selected behavior
		changeTest(0, 0);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render current steering behavior test
		if (currentTest != null) {
			if (!pauseButton.isChecked()) {
				// Update AI time
				GdxAI.getTimepiece().update(Gdx.graphics.getDeltaTime());

				// Update test
				currentTest.update();
			}
			// Draw test
			currentTest.draw();
		}

		stage.act();
		stage.draw();
	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		stage.getViewport().update(width, height, true);
		stageWidth = width;
		stageHeight = height;
	}

	@Override
	public void dispose () {
		if (currentTest != null) currentTest.dispose();

		stage.dispose();
		skin.dispose();

		// Dispose textures
		greenFish.getTexture().dispose();
		cloud.getTexture().dispose();
		badlogicSmall.getTexture().dispose();
		target.getTexture().dispose();
	}

	private List<String> createTestList (final int engineIndex) {
		// Create test names
		int numTests = tests[engineIndex].length;
		String[] testNames = new String[numTests];
		for (int i = 0; i < numTests; i++) {
			testNames[i] = tests[engineIndex][i].testName;
		}

		final List<String> testList = new List<String>(skin);
		testList.setItems(testNames);
		testList.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if (!testSelectionWindow.isCollapsed() && getTapCount() == 2) {
					changeTest(engineIndex, testList.getSelectedIndex());
					testSelectionWindow.collapse();
				}
			}
		});
		return testList;
	}

	private CollapsableWindow addTestSelectionWindow (String title, String[] tabTitles, Array<List<String>> tabLists,
		float x, float y) {
		if (tabTitles.length != tabLists.size)
			throw new IllegalArgumentException("tabTitles and tabList must have the same size.");
		CollapsableWindow window = new CollapsableWindow(title, skin);
		window.row();
		TabbedPane tabbedPane = new TabbedPane(skin);
		for (int i = 0; i < tabLists.size; i++) {
			ScrollPane pane = new ScrollPane(tabLists.get(i), skin);
			pane.setFadeScrollBars(false);
			pane.setScrollX(0);
			pane.setScrollY(0);

			tabbedPane.addTab(" " + tabTitles[i] + " ", pane);
		}
		window.add(tabbedPane);
		window.pack();
		window.pack();
		if (window.getHeight() > stage.getHeight()) {
			window.setHeight(stage.getHeight());
		}
		window.setX(x < 0 ? stage.getWidth() - (window.getWidth() - (x + 1)) : x);
		window.setY(y < 0 ? stage.getHeight() - (window.getHeight() - (y + 1)) : y);
		window.layout();
		window.collapse();
		stage.addActor(window);

		return window;
	}

	private void changeTest (int engineIndex, int testIndex) {
		// Remove the old test and its window
		if (currentTest != null) {
			if (currentTest.getDetailWindow() != null) currentTest.getDetailWindow().remove();
			currentTest.dispose();
		}

		// Add the new test and its window
		currentTest = tests[engineIndex][testIndex];
		currentTest.create();
		testHelpLabel.setText(currentTest.getHelpMessage());
		InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
		if (im.size() > 1) im.removeProcessor(1);
		if (currentTest.getInputProcessor() != null) im.addProcessor(currentTest.getInputProcessor());
		if (currentTest.getDetailWindow() != null) stage.addActor(currentTest.getDetailWindow());
	}

}
