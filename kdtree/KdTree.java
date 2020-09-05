import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {
    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;
        private int depth;

        public Node(Point2D key, int size, int depth) {
            this.p = key;
            this.size = size;
            this.depth = depth;
        }
    }

    public KdTree()                               // construct an empty set of points
    {

    }

    public boolean isEmpty()                      // is the set empty?
    {
        return size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p);


    }

    private Node insert(Node x, Point2D key) {

        if (x == null) return new Node(key, 1, 0);

        //Odd
        int cmp;
        if (x.depth % 2 != 0) {
            cmp = Double.compare(key.y(), x.p.y());
        } else {
            cmp = Double.compare(key.x(), x.p.x());
        }
        if (cmp < 0) {
            x.lb = insert(x.lb, key);
            x.lb.depth = x.depth + 1;
        } else {
            x.rt = insert(x.rt, key);
            x.rt.depth = x.depth + 1;
        }
        x.size = 1 + size(x.lb) + size(x.rt);

        return x;


    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return get(p) != null;
    }

    private Point2D get(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = root;
        while (x != null) {
            System.out.println("CURRENT:" + x.p);
            System.out.println("CURENT DPETH:" + x.depth);
            System.out.println("LEFT CHILD:" + x.lb.p);
            //System.out.println("RIGHT CHILD:" + x.rt.p);
            int cmp;
            if (x.depth % 2 != 0) {
                cmp = Double.compare(p.y(), x.p.y());
                if (cmp < 0) x = x.lb;
                else if (cmp > 0) x = x.rt;
                else {
                    if (Double.compare(p.x(), x.p.x()) == 0) {

                        return x.p;
                    } else x = x.lb;


                }
            } else {
                cmp = Double.compare(p.x(), x.p.x());

                if (cmp < 0) x = x.lb;
                else if (cmp > 0) x = x.rt;
                else {
                    if (Double.compare(p.y(), x.p.y()) == 0) {

                        return x.p;

                    } else x = x.rt;


                }
            }


        }
        return null;


    }


    public void draw()                         // draw all points to standard draw
    {

    }

    public Iterable<Point2D> range(RectHV rect)    // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> insideRect = new Stack<>();

        return insideRect;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();

        return new Point2D(0, 0);
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

        KdTree kd = new KdTree();
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(0, 1);
        Point2D p3 = new Point2D(1, 2);
        Point2D p4 = new Point2D(2, 1);
        Point2D p5 = new Point2D(2, 2);
        Point2D p6 = new Point2D(0, 2);
        Point2D p7 = new Point2D(2, 0);
        Point2D p8 = new Point2D(3, 5);
        kd.insert(p1);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        kd.insert(p5);
        kd.insert(p6);
        kd.insert(p7);
        kd.insert(p8);
        System.out.println(kd.root.rt.rt.lb.p);
    }
}
