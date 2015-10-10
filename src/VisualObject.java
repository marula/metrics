public class VisualObject {
    private int lineNumber;
    private int R;
    private int G;
    private int B;
    private boolean highlight;
    private int start;
    private int end;

    public VisualObject(int lineNumber, int R, int G, int B, boolean highlight, int start, int end) {
        this.lineNumber = lineNumber;
        this.R = R;
        this.G = G;
        this.B = B;
        this.highlight = highlight;
        this.start = start;
        this.end = end;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getR() {
        return R;
    }

    public int getG() {
        return G;
    }

    public int getB() {
        return B;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String toString() {
        return "(" + lineNumber + "," + R + "," + G + "," + B + "," + highlight + "," + start + "," + end + ")";
    }
}
