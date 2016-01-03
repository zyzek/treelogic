package logic;

import logic.conn.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Tree {

}

class TreeNode {
    ArrayList<WFF> forms;
    ArrayList<WFF> handled;

    TreeNode parent;
    ArrayList<TreeNode> children;

    ArrayList<WFF> basicForms;
    boolean closed;


    public TreeNode(ArrayList<WFF> forms, TreeNode parent, ArrayList<WFF> basicForms) {
        this.forms = forms;
        this.handled = new ArrayList<WFF>();

        this.parent = parent;
        this.children = new ArrayList<TreeNode>();
        
        this.basicForms = basicForms;
        this.closed = false;

        Iterator<WFF> iter = this.forms.iterator();

        while (iter.hasNext()) {
            WFF form = iter.next();

            if (form.isBasic()) {
                iter.remove();
                this.handled.add(form);
                this.addBasicForm(form);
            }
        }

        this.checkClosure();
    }
    
    public void addBasicForm(WFF form) {
        if (form.isBasic()) {
            if (form instanceof Falsum) {
                form = new Negation(new Verum());
            } else if (form instanceof Negation && ((Negation)form).sub instanceof Falsum) {
                form = new Verum();
            }
            if (!this.basicForms.contains(form)) {
                this.basicForms.add(form);
            }
        }
    }

    // Check to see if any basic formula and its negation are true on this path.
    public void checkClosure() {
       boolean nowClosed = !this.children.isEmpty();

       for (TreeNode child : this.children) {
           if (!child.closed) {
                nowClosed = false;
                break;
           }
       }

       if (!nowClosed) {
          for (int i = 0; i < this.basicForms.size(); ++i) {
            for (int j = i + 1; j < this.basicForms.size(); ++j) {
                if (this.basicForms.get(i).contradicts(this.basicForms.get(j))) {
                    nowClosed = true;
                    i = this.basicForms.size();
                    break;
                }
            }
          }
       }

       if (nowClosed) {
            this.closed = true;
            if (this.parent != null) {
                this.parent.checkClosure();
            }
       }

    }
    
    
    // Add a collection of formulae to the leaves of a subtree.
    public void addLeafForms(ArrayList<WFF> newForms) {
        if (!this.closed) {
            if (this.children.isEmpty()) {
                for (WFF form : newForms) {
                    if (form.isBasic()) {
                        this.handled.add(form);
                        this.basicForms.add(form);
                    } else {
                        this.forms.add(form);
                    }
                }
            } else {
                for (TreeNode child : this.children) {
                    child.addLeafForms(newForms);
                }
            }
        }

        this.checkClosure();
    }


    // Add a collection of formulae as children of each leaf of a subtree.
    public void addLeafBranches(ArrayList<WFF> newChildren) {
        if (!this.closed) {
            if (this.children.isEmpty()) {
                for (WFF form : newChildren) {
                    ArrayList<WFF> newFormList = new ArrayList<WFF>();
                    newFormList.add(form);
                    
                    ArrayList<WFF> newBasicForms = new ArrayList<WFF>();
                    for (WFF bForm : this.basicForms) {
                        newBasicForms.add(bForm);
                    }

                    TreeNode newChild = new TreeNode(newFormList, this, newBasicForms);
                    this.children.add(newChild);
                }
            } else {
                for (TreeNode child : children) {
                    child.addLeafBranches(newChildren); 
                }
            }
        }
    }

}
