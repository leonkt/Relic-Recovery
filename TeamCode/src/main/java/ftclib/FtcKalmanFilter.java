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

package ftclib;

public class FtcKalmanFilter {

    private final String instance;
    private double kQ;
    private double kR;
    private double prevP;
    private double prevXEst;
    private boolean init;

    private static double DEFAULT_KQ = 0.022;
    private static double DEFAULT_KR = 0.617;

    public FtcKalmanFilter(final String instance, double kQ, double kR) {

        this.instance = instance;
        this.kQ = kQ;
        this.kR = kR;

    }

    public FtcKalmanFilter(final String instance) {

        this(instance, DEFAULT_KQ, DEFAULT_KR);

    }

    /**
     * filterData reduces the noise in the data
     *
     * @param data is the data to be filtered by a sensor
     */
    public double filterData(double data) {

        if(!init) {
            prevXEst = data;
            init = true;
        }
        double tempP = prevP + kQ;
        double k = tempP/(tempP+kR);
        double xEst = prevXEst + k*(data-prevXEst);

        prevP = (1 - k)*tempP;
        prevXEst = xEst;

        return prevXEst;

    }

}
