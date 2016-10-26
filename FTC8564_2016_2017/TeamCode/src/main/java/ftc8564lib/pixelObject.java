package ftc8564lib;

public class pixelObject {

    int color;
    int count;
    int xpos;

    public pixelObject(int color, int count, int xpos)
    {
        this.color = color;
        this.count = count;
        this.xpos = xpos;
    }

    public int getColor() { return color; }

    public int getCount() { return count; }

    public int getXpos() { return xpos; }

}
