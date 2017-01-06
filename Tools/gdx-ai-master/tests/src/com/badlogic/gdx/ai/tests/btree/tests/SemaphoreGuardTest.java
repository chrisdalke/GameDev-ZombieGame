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

package com.badlogic.gdx.ai.tests.btree.tests;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.decorator.SemaphoreGuard;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeTestBase;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeViewer;
import com.badlogic.gdx.ai.tests.btree.dog.Dog;
import com.badlogic.gdx.ai.utils.NonBlockingSemaphoreRepository;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.utils.StreamUtils;

/** A simple test to demonstrate the {@link SemaphoreGuard} task.
 * 
 * @author davebaol */
public class SemaphoreGuardTest extends BehaviorTreeTestBase {

	public SemaphoreGuardTest () {
		super("Semaphore Guard", "When Buddy walks Snoopy barks and vice versa");
	}

	@Override
	public Actor createActor (Skin skin) {
		// Create the semaphore
		NonBlockingSemaphoreRepository.clear();
		NonBlockingSemaphoreRepository.addSemaphore("dogSemaphore", 1);

		Reader reader = null;
		try {
			// Parse Buddy's tree
			reader = Gdx.files.internal("data/dogSemaphore.tree").reader();
			BehaviorTreeParser<Dog> parser = new BehaviorTreeParser<Dog>(BehaviorTreeParser.DEBUG_HIGH);
			BehaviorTree<Dog> buddyTree = parser.parse(reader, new Dog("Buddy"));

			// Clone Buddy's tree for Snoopy
			BehaviorTree<Dog> snoopyTree = (BehaviorTree<Dog>)buddyTree.cloneTask();
			snoopyTree.setObject(new Dog("Snoopy"));

			// Create split pane
			BehaviorTreeViewer<?> buddyTreeViewer = createTreeViewer(buddyTree.getObject().name, buddyTree, false, skin);
			BehaviorTreeViewer<?> snoopyTreeViewer = createTreeViewer(snoopyTree.getObject().name, snoopyTree, false, skin);
			return new SplitPane(new ScrollPane(buddyTreeViewer, skin), new ScrollPane(snoopyTreeViewer, skin), true, skin,
				"default-horizontal");
		} finally {
			StreamUtils.closeQuietly(reader);
		}
	}

	@Override
	public void dispose () {
		super.dispose();
		NonBlockingSemaphoreRepository.clear();
	}

}
