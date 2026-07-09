package org.jdesktop.swingx.traversal;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.Consumer;
import javax.swing.tree.TreeNode;

/**
 * PostOrder Iterator for TreeNodes.
 * @param <M> base class derived from TreeNode that this Iterator returns.
 */
public class PostOrderIterator<M extends TreeNode> implements Iterator<M> {
	protected static class NodeData<M> {
		protected final M node;
		protected int actualIndex = -1;

		public NodeData(M node) {
			this.node = node;
		}
	}

	protected Deque<NodeData<M>> stack = new ArrayDeque<>();

	public PostOrderIterator(M rootNode) {
		stack.push(new NodeData<>(rootNode));
	}

	@Override
	public boolean hasNext() {
		return !stack.isEmpty();
	}

	@Override
	public M next() {
		var node = stack.peek();
		if (node.node.getChildCount() == 0) {
			stack.pop();
		} else {
			if (node.actualIndex == -1) {
				node = toLeftMostChild(node);
			} else {
				node.actualIndex++;
				if (node.actualIndex < node.node.getChildCount()) {
					node = new NodeData((M) node.node.getChildAt(node.actualIndex));
					stack.push(node);
					node = toLeftMostChild(node);
				} else stack.pop();
			}
		}
		return node.node;
	}

	private NodeData<M> toLeftMostChild(NodeData<M> node) {
		while (node.node.getChildCount() > 0) {
			node.actualIndex = 0;
			node = new NodeData(node.node.getChildAt(0));
			stack.push(node);
		}
		node = stack.pop();
		return node;
	}

	/**
	 * Simple processor that traverses nodes in post order and calls the given consumer.
	 * @param <T>
	 * @param rootNode
	 * @param consumer
	 */
	public static <T extends TreeNode> void process(T rootNode, Consumer<T> consumer) {
		for (int i = 0; i < rootNode.getChildCount(); i++) {
			process((T) rootNode.getChildAt(i), consumer);
		}
		consumer.accept(rootNode);
	}
}
