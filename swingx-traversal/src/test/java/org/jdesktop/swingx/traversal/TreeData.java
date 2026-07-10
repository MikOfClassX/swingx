package org.jdesktop.swingx.traversal;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * Some tools to generate testdata to use Iterators and processors.
 */
public class TreeData {

	private static final Random random = new Random();

	public static DefaultTreeModel testModelRandom(int deepness, int maxChildren) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultTreeModel model = new DefaultTreeModel(root);
		buildSubTree(root, deepness, maxChildren);
		return model;
	}

	public static DefaultTreeModel testModel() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultTreeModel model = new DefaultTreeModel(root);
		DefaultMutableTreeNode a = node("A");
		a.add(node("B"));
		a.add(node("C"));
		root.add(a);
		DefaultMutableTreeNode d = node("D");
		d.add(node("E"));
		final DefaultMutableTreeNode f = node("F");
		d.add(f);
		d.add(node("G"));
		f.add(node("f1"));
		f.add(node("f2"));
		root.add(d);
		return model;
	}

	private static void buildSubTree(DefaultMutableTreeNode root, int deepness, int maxChildren) {
		int c = random.nextInt(maxChildren);
		for (int i = 0; i < c; i++) {
			DefaultMutableTreeNode node = node(UUID.randomUUID().toString());
			root.add(node);
			if (deepness > 1) {
				buildSubTree(node, deepness - 1, maxChildren);
			}
		}
	}

	public static DefaultMutableTreeNode node(String title) {
		return new DefaultMutableTreeNode(title);
	}

	/**
	 * Provide test methods with the iterators or the processors.
	 */
	public interface Provider {
		DefaultTreeModel testModel();

		Iterator<TreeNode> iterator(TreeNode root);

		void process(TreeNode node, Consumer<TreeNode> consumer);

		Stream<TreeNode> stream(TreeNode root);
	}

	public static void doTreeDataRandomTest(int rounds, Provider provider) {
		for (int i = 0; i < rounds; i++) {
			final AtomicInteger counter = new AtomicInteger();

			DefaultTreeModel model = null;
			while (counter.intValue() < 5) {
				counter.set(0);
				model = provider.testModel();
				PreOrderIterator.process((TreeNode) model.getRoot(), n -> counter.incrementAndGet());
			}

			// by iterator
			var iterator = provider.iterator((TreeNode) model.getRoot());
			StringBuilder strIterator = new StringBuilder();
			while (iterator.hasNext()) {
				strIterator.append("/").append(iterator.next().toString());
			}

			// by processor
			List<String> list = new ArrayList<>();
			provider.process((TreeNode) model.getRoot(), node -> list.add(node.toString()));
			var strProcess = "/" + list.stream().collect(joining("/"));

			// by stream
			var strStream = "/"
					+ provider.stream((TreeNode) model.getRoot())
							.map(node -> node.toString())
							.collect(joining("/"));

			assertThat(counter.intValue())
					.as("run " + i + " should have more nodes")
					.isGreaterThanOrEqualTo(5);
			assertThat(strProcess).isEqualTo(strIterator.toString());
			assertThat(strStream).isEqualTo(strIterator.toString());
		}
	}
}
