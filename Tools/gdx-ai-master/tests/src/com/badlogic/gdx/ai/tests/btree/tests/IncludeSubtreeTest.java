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

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibrary;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeTestBase;
import com.badlogic.gdx.ai.tests.btree.BehaviorTreeViewer;
import com.badlogic.gdx.ai.tests.btree.dog.Dog;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** A simple test to demonstrate subtree inclusion both eager (at clone-time) and lazy (at run-time).
 * 
 * @author davebaol */
public class IncludeSubtreeTest extends BehaviorTreeTestBase {

	private boolean lazy;

	public IncludeSubtreeTest (boolean lazy) {
		super("Include Subtree" + (lazy ? " Lazily" : ""));
		this.lazy = lazy;
	}

	@Override
	public Actor createActor (Skin skin) {
		BehaviorTreeLibraryManager libraryManager = BehaviorTreeLibraryManager.getInstance();
		libraryManager.setLibrary(new BehaviorTreeLibrary(BehaviorTreeParser.DEBUG_HIGH));

		String name = lazy ? "data/dogIncludeLazy.tree" : "data/dogInclude.tree";
		BehaviorTree<Dog> tree = libraryManager.createBehaviorTree(name, new Dog("Buddy"));
		BehaviorTreeViewer<?> treeViewer = createTreeViewer(tree.getObject().name, tree, true, skin);

		return new ScrollPane(treeViewer, skin);
	}

}
