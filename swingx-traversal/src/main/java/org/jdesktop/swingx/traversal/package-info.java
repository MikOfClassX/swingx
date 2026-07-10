/**
 * This package provides Iterators for different tree traversals. At the moment this uses TreeNode (Swing) as a
 * node class.
 *
 * Every Iterator contains a process method to traverse a tree without using an Iterator and providing a consumer
 * for each visited Node.
 *
 * Every Iterator contains a stream method to build a Java stream from the Iterator.
 *
 * Supported are preorder, postorder and breadth first traversal.
 *
 *
 * <pre>{@code
 * // iterator
 * new PreOrderIterator(root node)
 *
 * // processor
 * PreOrderIterator.process(root node, node -> do something);
 *
 * // stream
 * PreOrderIterator.stream(root node);
 * }</pre>
 */
package org.jdesktop.swingx.traversal;
