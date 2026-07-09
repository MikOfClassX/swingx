package org.jdesktop.swingx.traversal;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;
import javax.swing.tree.TreeNode;

/**
 * PreOrder Iterator for TreeNodes.
 * @param <M> base clss derived from TreeNode that this Iterator returns.
 */
public class PreOrderIterator<M extends TreeNode> implements Iterator<M> {
	protected Deque<M> stack = new ArrayDeque<>();

	public PreOrderIterator(M rootNode) {
		stack.push(rootNode);
	}

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
	public M next() {
		M node = stack.pop();
		for (int i = node.getChildCount() - 1; i >= 0; i--) {
			stack.push((M) node.getChildAt(i));
		}
		return node;
	}

	/**
	 * Simple processor that traverses nodes in pre order and calls the given consumer.
	 * @param <T>
	 * @param rootNode
	 * @param consumer
	 */
	public static <T extends TreeNode> void process(T rootNode, Consumer<T> consumer) {
		consumer.accept(rootNode);
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			process((T) rootNode.getChildAt(i), consumer);
		}
	}
}
