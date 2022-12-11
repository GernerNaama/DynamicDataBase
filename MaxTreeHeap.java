import java.io.DataOutputStream;
import java.io.IOException;

public class MaxTreeHeap {
    private Vertex root;
    private int size;


    public MaxTreeHeap(int[] A){
        this.size = A.length;
    }

    public Vertex getRoot(){
        return root;
    }
    public int getSize(){
        return size;
    }


    public static void Heapify(int[] A, int i){
        int l = 2*i;
        int n = A.length;
        int biggest;
        if ((l < n) && (A[l] > A[i])){
            biggest = l ;
        }
        else {
            biggest = i ;
        }
        int r = 2*i + 1;
        if ((r < n) && (A[r] > A[biggest])){
            biggest = r ;
        }
        if (biggest != i) {
            int temp = A[i];
            A[i] = A[biggest];
            A[biggest] = temp;
            Heapify(A, biggest);
        }
    }

    public static MaxTreeHeap BuildHeapT(int[] A){
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
        MaxTreeHeap H = new MaxTreeHeap(A);
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
        Heap_Increase_key(newVertex, k);
    }

    public void Heap_Increase_key(Vertex curr, int k) {
        if (k < curr.getData()) {
            return;  //Error new key is smaller than current key
        }
        curr.setData(k);
        while ((curr.getParent() != null) && (curr.getData() > curr.getParent().getData())) {
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
        Integer myMinusInf = Integer.MIN_VALUE;
        Vertex newVertex = new Vertex(myMinusInf);
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


    public int HeapExtractMax() {
        if (size < 1) {
            return -1;
        }
        int max = root.getData();

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
        return max;
    }

    public void HeapifyTree(Vertex vertex){
        Vertex biggest;
        if (vertex.getLeft() != null && vertex.getLeft().getData() > vertex.getData()){
            biggest = vertex.getLeft();
        }
        else {
            biggest = vertex;
        }
        if (vertex.getRight() != null && vertex.getRight().getData() > biggest.getData()){
            biggest = vertex.getRight();
        }
        if (biggest != vertex){
            int temp = biggest.getData();
            biggest.setData(vertex.getData());
            vertex.setData(temp);
            HeapifyTree(biggest);
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

}