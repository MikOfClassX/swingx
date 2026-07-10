package org.jdesktop.swingx.traversal;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.jdesktop.swingx.traversal.TreeData.Provider;
import org.junit.jupiter.api.Test;

public class PostOrderIteratorTest {

	@Test
	void testIterator() {
		var iterator = new PostOrderIterator((TreeNode) TreeData.testModel().getRoot());
		StringBuilder b = new StringBuilder();
		while (iterator.hasNext()) {
			final TreeNode next = iterator.next();
			b.append("/").append(next.toString());
		}
		assertThat(b.toString()).isEqualTo("/B/C/A/E/f1/f2/F/G/D/root");
	}

	@Test
	void testProcessor() {
		List<String> list = new ArrayList<>();
		PostOrderIterator.process((TreeNode) TreeData.testModel().getRoot(), node -> list.add(node.toString()));
		assertThat(list.stream().collect(joining("/"))).isEqualTo("B/C/A/E/f1/f2/F/G/D/root");
	}

	@Test
	void testStream() {
		assertThat(PostOrderIterator.stream((TreeNode) TreeData.testModel().getRoot())
						.map(node -> node.toString())
						.collect(joining("/")))
				.isEqualTo("B/C/A/E/f1/f2/F/G/D/root");
	}

	@Test
	void testProcessorAgainstIteratorDeepness10MaxChildren5() {
		TreeData.doTreeDataRandomTest(100, new Provider() {
			@Override
			public DefaultTreeModel testModel() {
				return TreeData.testModelRandom(10, 5);
			}

			@Override
			public Iterator<TreeNode> iterator(TreeNode root) {
				return new PostOrderIterator(root);
			}

			@Override
			public void process(TreeNode node, Consumer<TreeNode> consumer) {
				PostOrderIterator.process(node, consumer);
			}

			@Override
			public Stream<TreeNode> stream(TreeNode root) {
				return PostOrderIterator.stream(root);
			}
		});
	}
}
