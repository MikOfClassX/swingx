package org.jdesktop.swingx.traversal;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.swing.tree.TreeNode;

/**
 * BreadthFirst Iterator for TreeNodes.
 * @param <M> base clss derived from TreeNode that this Iterator returns.
 */
public class BreadthFirstIterator<M extends TreeNode> implements Iterator<M> {
	protected Deque<M> stack = new ArrayDeque<>();

	public BreadthFirstIterator(M rootNode) {
		stack.push(rootNode);
	}

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
	public M next() {
		M node = stack.pop();
		for (int i = 0; i < node.getChildCount(); i++) {
			stack.offer((M) node.getChildAt(i));
		}
		return node;
	}

	/**
	 * Simple processor that traverses nodes in breadth first and calls the given consumer.
	 * @param <T>
	 * @param rootNode
	 * @param consumer
	 */
	public static <T extends TreeNode> void process(T rootNode, Consumer<T> consumer) {
		Deque<T> stack = new ArrayDeque<>();
		stack.push(rootNode);

		while (!stack.isEmpty()) {
			var node = stack.pop();
			consumer.accept(node);
			for (int i = 0; i < node.getChildCount(); i++) {
				stack.offer((T) node.getChildAt(i));
			}
		}
	}

	/**
	 * Stream derived from the Iterator.
	 * @param <T>
	 * @param rootNode
	 * @return
	 */
	public static <T extends TreeNode> Stream<T> stream(T rootNode) {
		Spliterator<T> spliterator =
				Spliterators.spliteratorUnknownSize(new BreadthFirstIterator(rootNode), Spliterator.NONNULL);
		return StreamSupport.stream(spliterator, false);
	}
}
