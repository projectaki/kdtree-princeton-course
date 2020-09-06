import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private Queue<Node> forDraw;
    private Queue<Point2D> insideRect = new Queue<>();
    private Point2D champion;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;
        private int depth;

        public Node(Point2D key, int size, int depth, RectHV rect) {
            this.p = key;
            this.size = size;
            this.depth = depth;
            this.rect = rect;
        }
    }

    public KdTree()                               // construct an empty set of points
    {
        forDraw = new Queue<>();

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
        // x = previous
        if (x == null) {
            Node temp = new Node(key, 1, 0, new RectHV(0, 0, 10, 10));
            forDraw.enqueue(temp);
            return temp;
        }

        double xMin;
        double yMin;
        double xMax;
        double yMax;

        int cmp;
        // Odd
        if (x.depth % 2 != 0) {
            cmp = Double.compare(key.y(), x.p.y());
            //Down
            if (cmp < 0) {
                xMin = x.rect.xmin();
                yMin = x.rect.ymin();
                xMax = x.rect.xmax();
                yMax = x.p.y();
            }
            //UP
            else {
                xMin = x.rect.xmin();
                yMin = x.p.y();
                xMax = x.rect.xmax();
                yMax = x.rect.ymax();
            }
            // Even
        } else {
            cmp = Double.compare(key.x(), x.p.x());
            //Left
            if (cmp < 0) {
                xMin = x.rect.xmin();
                yMin = x.rect.ymin();
                xMax = x.p.x();
                yMax = x.rect.ymax();
            }
            //Right
            else {
                xMin = x.p.x();
                yMin = x.rect.ymin();
                xMax = x.rect.xmax();
                yMax = x.rect.ymax();
            }
        }
        if (cmp < 0) {
            x.lb = insert(x.lb, key);
            x.lb.depth = x.depth + 1;
            x.lb.rect = new RectHV(xMin, yMin, xMax, yMax);
            forDraw.enqueue(x.lb);
        } else {
            x.rt = insert(x.rt, key);
            x.rt.depth = x.depth + 1;
            x.rt.rect = new RectHV(xMin, yMin, xMax, yMax);
            forDraw.enqueue(x.rt);
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
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.square(0.5, 0.5, 0.5);
        for (Node n : forDraw) {
            n.p.draw();
            n.rect.draw();

        }
    }

    public Iterable<Point2D> range(RectHV rect)    // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();

        range(root, rect);
        return insideRect;
    }

    private void range(Node x, RectHV rect) {
        if (rect.contains(x.p)) insideRect.enqueue(x.p);

        if (x.lb != null) {
            if (rect.intersects(x.lb.rect)) range(x.lb, rect);
        }
        if (x.rt != null) {
            if (rect.intersects(x.rt.rect)) range(x.rt, rect);
        }
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        nearest(root, p);
        return champion;
    }

    private void nearest(Node x, Point2D p) {
        if (x.depth == 0) {
            champion = root.p;
        }

        double recDistX = x.rect.xmin();
        double recDistY = p.y();
        if (p.distanceTo(champion) > p.distanceTo(new Point2D(recDistX, recDistY))) {
            if (x.lb != null) {
                if (p.distanceTo(x.lb.p) < p.distanceTo(champion)) {
                    champion = x.lb.p;
                    nearest(x.lb, p);
                } else nearest(x.lb, p);
            }
            if (x.rt != null) {
                if (p.distanceTo(x.rt.p) < p.distanceTo(champion)) {
                    champion = x.rt.p;
                    nearest(x.rt, p);
                } else nearest(x.rt, p);
            }
        }
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

        KdTree kd = new KdTree();
        Point2D p1 = new Point2D(0.2, 0.3);
        Point2D p2 = new Point2D(0.3, 0.2);
        Point2D p3 = new Point2D(0.2, 0.1);
        Point2D p4 = new Point2D(0.4, 0.5);
        Point2D p5 = new Point2D(0.7, 0.7);

        kd.insert(p1);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        kd.insert(p5);

        kd.draw();
        RectHV queryRec = new RectHV(0.3, 0.3, 0.7, 0.7);
        queryRec.draw();
        Iterable<Point2D> iter = kd.range(queryRec);
        //System.out.println(iter);
        kd.nearest(new Point2D(1, 1));


    }
}
