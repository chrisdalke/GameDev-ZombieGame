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

package com.badlogic.gdx.ai.tests.steer.box2d.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.limiters.LinearAccelerationLimiter;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.ParallelSideRayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.ai.tests.SteeringBehaviorsTest;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dRaycastCollisionDetector;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dSteeringEntity;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dSteeringTest;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** A class to test and experiment with the {@link RaycastObstacleAvoidance} behavior.
 * 
 * @autor davebaol */
public class Box2dRaycastObstacleAvoidanceTest extends Box2dSteeringTest {
	Box2dSteeringEntity character;
	int rayConfigurationIndex;
	RayConfigurationBase<Vector2>[] rayConfigurations;
	RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidanceSB;
	boolean drawDebug;
	ShapeRenderer shapeRenderer;

	private Body[] walls;
	private int[] walls_hw;
	private int[] walls_hh;

	private Vector2 tmp = new Vector2();
	private Vector2 tmp2 = new Vector2();
	private Batch spriteBatch;

	public Box2dRaycastObstacleAvoidanceTest (SteeringBehaviorsTest container) {
		super(container, "Raycast Obstacle Avoidance");
	}

	@Override
	public void create () {
		super.create();

		drawDebug = true;

		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();

		createRandomWalls(8);

		character = createSteeringEntity(world, container.greenFish);
		character.setMaxLinearSpeed(2);
		character.setMaxLinearAcceleration(1);

		@SuppressWarnings("unchecked")
		RayConfigurationBase<Vector2>[] localRayConfigurations = new RayConfigurationBase[] {
			new SingleRayConfiguration<Vector2>(character, Box2dSteeringTest.pixelsToMeters(100)),
			new ParallelSideRayConfiguration<Vector2>(character, Box2dSteeringTest.pixelsToMeters(100),
				character.getBoundingRadius()),
			new CentralRayWithWhiskersConfiguration<Vector2>(character, Box2dSteeringTest.pixelsToMeters(100),
				Box2dSteeringTest.pixelsToMeters(40), 35 * MathUtils.degreesToRadians)};
		rayConfigurations = localRayConfigurations;
		rayConfigurationIndex = 0;
		RaycastCollisionDetector<Vector2> raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
		raycastObstacleAvoidanceSB = new RaycastObstacleAvoidance<Vector2>(character, rayConfigurations[rayConfigurationIndex],
			raycastCollisionDetector, Box2dSteeringTest.pixelsToMeters(1000));

		Wander<Vector2> wanderSB = new Wander<Vector2>(character) //
			// Don't use Face internally because independent facing is off
			.setFaceEnabled(false) //
			// We don't need a limiter supporting angular components because Face is not used
			// No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget for the same reason
			.setLimiter(new LinearAccelerationLimiter(4)) //
			.setWanderOffset(3) //
			.setWanderOrientation(5) //
			.setWanderRadius(1) //
			.setWanderRate(MathUtils.PI2 * 4);

		PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(character, 0.0001f) //
			.add(raycastObstacleAvoidanceSB) //
			.add(wanderSB);

		character.setSteeringBehavior(prioritySteeringSB);

		inputProcessor = null;

		Table detailTable = new Table(container.skin);

		detailTable.row();
		addMaxLinearAccelerationController(detailTable, character, 0, 30, .5f);

		detailTable.row();
		final Label labelDistFromBoundary = new Label("Distance from Boundary ["
			+ raycastObstacleAvoidanceSB.getDistanceFromBoundary() + "]", container.skin);
		detailTable.add(labelDistFromBoundary);
		detailTable.row();
		Slider distFromBoundary = new Slider(0, 60, 1f, false, container.skin);
		distFromBoundary.setValue(raycastObstacleAvoidanceSB.getDistanceFromBoundary());
		distFromBoundary.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				Slider slider = (Slider)actor;
				raycastObstacleAvoidanceSB.setDistanceFromBoundary(slider.getValue());
				labelDistFromBoundary.setText("Distance from Boundary [" + slider.getValue() + "]");
			}
		});
		detailTable.add(distFromBoundary);

		detailTable.row();
		final Label labelRayConfig = new Label("Ray Configuration", container.skin);
		detailTable.add(labelRayConfig);
		detailTable.row();
		SelectBox<String> rayConfig = new SelectBox<String>(container.skin);
		rayConfig.setItems(new String[] {"Single Ray", "Parallel Side Rays", "Central Ray with Whiskers"});
		rayConfig.setSelectedIndex(0);
		rayConfig.addListener(new ChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				SelectBox<String> selectBox = (SelectBox<String>)actor;
				rayConfigurationIndex = selectBox.getSelectedIndex();
				raycastObstacleAvoidanceSB.setRayConfiguration(rayConfigurations[rayConfigurationIndex]);
			}
		});
		detailTable.add(rayConfig);

		detailTable.row();
		addSeparator(detailTable);

		detailTable.row();
		CheckBox debug = new CheckBox("Draw Rays", container.skin);
		debug.setChecked(drawDebug);
		debug.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				CheckBox checkBox = (CheckBox)event.getListenerActor();
				drawDebug = checkBox.isChecked();
			}
		});
		detailTable.add(debug);

		detailTable.row();
		addSeparator(detailTable);

		detailTable.row();
		addMaxLinearSpeedController(detailTable, character, 0, 15, .5f);

		detailWindow = createDetailWindow(detailTable);
	}

	@Override
	public void update () {
		super.update();

		// Update the character
		character.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void draw () {
		// Draw the walls
		for (int i = 0; i < walls.length; i++) {
			renderBox(shapeRenderer, walls[i], walls_hw[i], walls_hh[i]);
		}

		if (drawDebug) {
			Ray<Vector2>[] rays = rayConfigurations[rayConfigurationIndex].getRays();
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(1, 0, 0, 1);
			transform.idt();
			shapeRenderer.setTransformMatrix(transform);
			for (int i = 0; i < rays.length; i++) {
				Ray<Vector2> ray = rays[i];
				tmp.set(ray.start);
				tmp.x = Box2dSteeringTest.metersToPixels(tmp.x);
				tmp.y = Box2dSteeringTest.metersToPixels(tmp.y);
				tmp2.set(ray.end);
				tmp2.x = Box2dSteeringTest.metersToPixels(tmp2.x);
				tmp2.y = Box2dSteeringTest.metersToPixels(tmp2.y);
				shapeRenderer.line(tmp, tmp2);
			}
			shapeRenderer.end();
		}

		// Draw the character
		spriteBatch.begin();
		character.draw(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public void dispose () {
		super.dispose();
		shapeRenderer.dispose();
		spriteBatch.dispose();
	}

	private void createRandomWalls (int n) {
		PolygonShape groundPoly = new PolygonShape();

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyType.StaticBody;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundPoly;
		fixtureDef.filter.groupIndex = 0;

		walls = new Body[n];
		walls_hw = new int[n];
		walls_hh = new int[n];
		for (int i = 0; i < n; i++) {
			groundBodyDef.position.set(Box2dSteeringTest.pixelsToMeters(MathUtils.random(50, (int)container.stageWidth - 50)),
				Box2dSteeringTest.pixelsToMeters(MathUtils.random(50, (int)container.stageHeight - 50)));
			walls[i] = world.createBody(groundBodyDef);
			walls_hw[i] = (int)MathUtils.randomTriangular(20, 150);
			walls_hh[i] = (int)MathUtils.randomTriangular(30, 80);
			groundPoly.setAsBox(Box2dSteeringTest.pixelsToMeters(walls_hw[i]), Box2dSteeringTest.pixelsToMeters(walls_hh[i]));
			walls[i].createFixture(fixtureDef);

		}
		groundPoly.dispose();
	}
}
