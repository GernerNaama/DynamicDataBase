import java.io.DataOutputStream;
import java.io.IOException;

public class MinTreeHeap {
    private Vertex root;
    private int size;


    public MinTreeHeap(int[] A){
        this.size = A.length;
    }

    public int getSize(){
        return size;
    }

    public static void Heapify(int[] A, int i){
        int l = 2*i;
        int n = A.length;
        int smallest;
        if ((l < n) && (A[l] < A[i])){
            smallest = l ;
        }
        else {
            smallest = i ;
        }
        int r = 2*i + 1;
        if ((r < n) && (A[r] < A[smallest])){
            smallest = r ;
        }
        if (smallest != i) {
            int temp = A[i];
            A[i] = A[smallest];
            A[smallest] = temp;
            Heapify(A, smallest);
        }
    }

    public static MinTreeHeap BuildHeapT(int[] A){
        int n = A.length;
        int[] ACopy = new int[n+1];
        for (int i=1; i<n+1; i++){
            ACopy[i] = A[i-1];
        }
        for (int i = n; i>0; i--){
            Heapify(ACopy,i);
        }
        Vertex[] B = new Vertex[n+1] ;
        B[0] = null;
        for (int i=1; i<n+1; i++) {
            int data = ACopy[i];
            B[i] = new Vertex(data);
            B[i].setIndex(i);
        }
        for (int i=1; i<n+1; i++) {
            B[i].setParent(B[i / 2]);
            if (2*i < n+1){
                B[i].setLeft(B[2*i]);
            }
            if (2*i + 1 < n+1) {
                B[i].setRight(B[2*i + 1]);
            }
        }
        B[1].setParent(null);
        MinTreeHeap H = new MinTreeHeap(A);
        H.root = B[1];
        return H;
    }


    private void SwapData(Vertex a,Vertex b){
        int x = a.getData();
        a.setData(b.getData());
        b.setData(x);
    }

    public void HeapInsert (int k){
        size++;
        int[] pathLastToRoot = new int[size];
        int limit = findPathLastToRoot(pathLastToRoot);
        Vertex newVertex = findAndAddLast(limit, pathLastToRoot);
        Heap_Decrease_key(newVertex, k);
    }

    public void Heap_Decrease_key(Vertex curr, int k) {
        if (k > curr.getData()) {
            return;  //Error new key is larger than current key
        }
        curr.setData(k);
        while ((curr.getParent() != null) && (curr.getData() < curr.getParent().getData())) {
            SwapData(curr, curr.getParent());
            curr = curr.getParent();
        }
    }

    public int findPathLastToRoot(int[] pathLastToRoot) {
        int limit = 0;
        for (int i = size; i > 1; i = i / 2) {
            pathLastToRoot[limit+1] = i % 2;
            limit++;
        }
        return limit;
    }

    public Vertex findAndAddLast (int limit, int[] pathLastToRoot){
        Integer myInf = Integer.MAX_VALUE;
        Vertex newVertex = new Vertex(myInf);
        newVertex.setIndex(size);

        Vertex last = root;
        while (limit>0){                          //find the last vertex
            if (limit==1){
                if (pathLastToRoot[limit] == 0) {
                    last.setLeft(newVertex);
                    }
                else {
                    last.setRight(newVertex);
                }
                newVertex.setParent(last);
            }
            if (pathLastToRoot[limit] == 0){
                last = last.getLeft();
            }
            else{
                last = last.getRight();
            }
            limit--;
        }
        return newVertex;
    }


    public int HeapExtractMin() {
        if (size < 1) {
            return -1;
        }
        int min = root.getData();

        int[] pathLastToRoot = new int[size];
        int limit = findPathLastToRoot(pathLastToRoot); //find the path from last to root

        Vertex last = root;
        while (limit>0){                          //find the last vertex
            if (pathLastToRoot[limit] == 0){
                last = last.getLeft();
            }
            else{
                last = last.getRight();
            }
            limit--;
        }
        root.setData(last.getData());       //move last to be root
        if (size % 2 ==0) {                 //remove last form tree
            last.getParent().setLeft(null);
        }
        else {
            last.getParent().setRight(null);
        }
        last.setParent(null);
        last.setIndex(-1);
        size--;
        HeapifyTree(root);
        return min;
    }

    public void HeapifyTree(Vertex vertex){
        Vertex smallest;
        if (vertex.getLeft() != null && vertex.getLeft().getData() < vertex.getData()){
            smallest = vertex.getLeft();
        }
        else {
            smallest = vertex;
        }
        if (vertex.getRight() != null && vertex.getRight().getData() < smallest.getData()){
            smallest = vertex.getRight();
        }
        if (smallest != vertex){
            int temp = smallest.getData();
            smallest.setData(vertex.getData());
            vertex.setData(temp);
            HeapifyTree(smallest);
        }
    }


    private static void inOrder(Vertex node, int[] heapArr) {
        if (node == null) {
            return;
        }
        inOrder(node.getLeft(),heapArr);
        heapArr[node.getIndex()-1] = node.getData();
        inOrder(node.getRight(),heapArr);
    }

    public void printByLayer(DataOutputStream out) throws IOException{
        int[] heapArr = new int[size];
        inOrder(root, heapArr);

        out.writeBytes(Integer.toString(heapArr[0]));
        out.writeBytes(System.lineSeparator());
        int j = 1;
        for (int i = 2; i<size; i++){
            int limitIndex = power(i);
            if (j >= size){
                break;
            }
            while ((j < limitIndex-1) && (j < size)){
                out.writeBytes(Integer.toString(heapArr[j]));
                if ((j+1 < limitIndex-1) && (j<size-1)) {
                    out.writeBytes(",");
                }
                j++;
            }
            out.writeBytes(System.lineSeparator());
        }
    }

    public int power(int power){
        int result = 1;
        for (int i=0; i<power; i++) {
            result = result * 2;
        }
        return result;
    }



    /* swap
    Vertex tempVertex = new Vertex(curr.getData());
            Vertex parent = curr.getParent();
            tempVertex.setParent(parent);
            tempVertex.setLeft(curr.getLeft());
            tempVertex.setRight(curr.getRight());
            tempVertex.setIndex(curr.getIndex());
            if (curr.getIndex() % 2 == 0){
                curr.setLeft(parent);
                curr.setRight(parent.getRight());
            }
            else {
                curr.setRight(parent);
                curr.setLeft(parent.getLeft());
            }
            curr.setParent(parent.getParent());
            curr.setIndex(parent.getIndex());

            parent.setParent(curr);
            parent.setLeft(tempVertex.getLeft());
            parent.setRight(tempVertex.getRight());
            parent.setIndex(tempVertex.getIndex());
     */


    /*
    public void printByLayer(DataOutputStream out) throws IOException{
        // Base Case
        if(root == null)
            return;
        Vertex[] q = new Vertex[size];
        int queue_size = 0;
        int front = 0, end =0;
        q[end++] = root;
        queue_size++;
        while(true)
        {
            // nodeCount (queue size) indicates number of nodes
            // at current level.
            int nodeCount = queue_size;
            if(nodeCount == 0)
                break;
            // Dequeue all nodes of current level and Enqueue all
            // nodes of next level
            while(nodeCount > 0) {
                Vertex current = q[front];
                nodeCount--;
                queue_size--;
                front++;
                out.writeBytes(Integer.toString(current.getData()));
                if (current.getLeft() != null) {
                    q[end++] = current.getLeft();
                    queue_size++;
                }
                if (current.getRight() != null) {
                    q[end++] = current.getRight();
                    queue_size++;
                }
                if(nodeCount!=0)
                    out.writeBytes(", ");
            }
            out.writeBytes(System.lineSeparator());
        }
    }
     */

}
