public class Vertex {
    private int data;
    private int index;
    private Vertex left;
    private Vertex right;
    private Vertex parent;

    public Vertex(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.parent = null;
        this.index = -1;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    public void setLeft(Vertex left) {
        this.left = left;
    }

    public void setRight(Vertex right) {
        this.right = right;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public int getData() {
        return this.data;
    }
    public void setData(int data) {
        this.data = data;
    }
    public int getIndex() {
        return this.index;
    }
    public Vertex getParent() {
        return this.parent;
    }
    public Vertex getLeft() {
        return left;
    }
    public Vertex getRight() {
        return right;
    }
}
