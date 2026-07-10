package org.jdesktop.swingx.traversal;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.junit.jupiter.api.Test;

public class BreadthFirstIteratorTest {
	@Test
	void testIterator() {
		var iterator = new BreadthFirstIterator((TreeNode) TreeData.testModel().getRoot());
		StringBuilder b = new StringBuilder();
		while (iterator.hasNext()) {
			b.append("/").append(iterator.next().toString());
		}
		assertThat(b.toString()).isEqualTo("/root/A/D/B/C/E/F/G/f1/f2");
	}

	@Test
	void testProcessor() {
		List<String> list = new ArrayList<>();
		BreadthFirstIterator.process((TreeNode) TreeData.testModel().getRoot(), node -> list.add(node.toString()));
		assertThat(list.stream().collect(joining("/"))).isEqualTo("root/A/D/B/C/E/F/G/f1/f2");
	}

	@Test
	void testStream() {
		assertThat(BreadthFirstIterator.stream((TreeNode) TreeData.testModel().getRoot())
						.map(node -> node.toString())
						.collect(joining("/")))
				.isEqualTo("root/A/D/B/C/E/F/G/f1/f2");
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
				return new BreadthFirstIterator(root);
			}

			@Override
			public void process(TreeNode node, Consumer<TreeNode> consumer) {
				BreadthFirstIterator.process(node, consumer);
			}

			@Override
			public Stream<TreeNode> stream(TreeNode root) {
				return BreadthFirstIterator.stream(root);
			}
		});
	}
}
