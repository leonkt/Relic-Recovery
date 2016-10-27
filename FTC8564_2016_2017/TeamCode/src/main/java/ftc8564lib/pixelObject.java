/*
 * Lockdown Framework Library
 * Copyright (c) 2015 Lockdown Team 8564 (lockdown8564.weebly.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ftc8564lib;

public class pixelObject implements Comparable<pixelObject> {

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

    public void addCount() { count++; }

    public void resetCount() { count = 0; }

    public void setColor(int i) { color = i; }

    public int compareTo(pixelObject object)
    {
        if(color > object.color)
        {
            return 1;
        } else if(color == object.color)
        {
            return 0;
        } else {
            return -1;
        }
    }

    public String toString()
    {
        if(color == 0)
        {
            return "Red"+String.valueOf(count);
        } else if(color == 1)
        {
            return "Blue"+String.valueOf(count);
        } else {
            return "Other"+String.valueOf(count);
        }
    }

}
