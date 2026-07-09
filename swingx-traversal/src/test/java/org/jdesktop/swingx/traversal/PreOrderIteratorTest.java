package org.jdesktop.swingx.traversal;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.junit.jupiter.api.Test;

public class PreOrderIteratorTest {

	@Test
	void testIterator() {
		var iterator = new PreOrderIterator((TreeNode) TreeData.testModel().getRoot());
		StringBuilder b = new StringBuilder();
		while (iterator.hasNext()) {
			b.append("/").append(iterator.next().toString());
		}
		assertThat(b.toString()).isEqualTo("/root/A/B/C/D/E/F/f1/f2/G");
	}

	@Test
	void testProcessor() {
		List<String> list = new ArrayList<>();
		PreOrderIterator.process((TreeNode) TreeData.testModel().getRoot(), node -> list.add(node.toString()));
		assertThat(list.stream().collect(joining("/"))).isEqualTo("root/A/B/C/D/E/F/f1/f2/G");
	}

	@Test
	void testProcessorAgainstIteratorDeepness10MaxChildren5() {
		TreeData.doTreeDataRandomTest(100, new TreeData.Provider() {
			@Override
			public DefaultTreeModel testModel() {
				return TreeData.testModelRandom(10, 5);
			}

			@Override
			public Iterator<TreeNode> iterator(TreeNode root) {
				return new PreOrderIterator(root);
			}

			@Override
			public void process(TreeNode node, Consumer<TreeNode> consumer) {
				PreOrderIterator.process(node, consumer);
			}
		});
	}
}
