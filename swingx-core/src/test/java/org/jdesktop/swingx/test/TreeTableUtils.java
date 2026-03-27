/**
 * 
 */
package org.jdesktop.swingx.test;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 *
 */
public class TreeTableUtils {
    private TreeTableUtils() {
        //does nothing
    }
    
    public static DefaultTreeTableModel convertDefaultTreeModel(DefaultTreeModel model) {
    	Vector<String> v = new Vector<>();
    	v.add("A");
        DefaultTreeTableModel ttModel = new DefaultTreeTableModel(null, v);
        
        ttModel.setRoot(convertDefaultMutableTreeNode((TreeNode) model.getRoot()));
        
        return ttModel;
    }
    
    @SuppressWarnings("unchecked")
    private static DefaultMutableTreeTableNode convertDefaultMutableTreeNode(TreeNode treeNode) {
        Object userObject = treeNode;
        if (treeNode instanceof DefaultMutableTreeNode) {
            userObject = ((DefaultMutableTreeNode) treeNode).getUserObject();
        }
        DefaultMutableTreeTableNode ttNode = new DefaultMutableTreeTableNode(userObject);
        
        Enumeration<TreeNode> children = (Enumeration<TreeNode>) treeNode.children();
        
        while (children.hasMoreElements()) {
            ttNode.add(convertDefaultMutableTreeNode(children.nextElement()));
        }
        
        return ttNode;
    }
}