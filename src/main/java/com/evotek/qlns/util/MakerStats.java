/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author linhlh2
 */
public class MakerStats {

    public MakerStats(String name) {
        //_name = name;
    }

    public void add(String caller, int initSize, int finalSize) {
        SizeSample stat = null;

        synchronized (this._map) {
            stat = this._map.get(caller);

            if (stat == null) {
                stat = new SizeSample(caller, initSize);

                this._map.put(caller, stat);
            }

            this._count++;
        }

        synchronized (stat) {
            stat.add(finalSize);
        }
    }

    public void display(PrintStream printer) {
        printer.println("caller,min,max,range,samples,average,initial");

        List<SizeSample> list = new ArrayList<SizeSample>(this._map.size());

        list.addAll(this._map.values());

        list = ListUtil.sort(list);

        int maxSize = 0;
        int sampleSize = 0;
        int totalSize = 0;

        for (int i = 0; i < list.size(); i++) {
            SizeSample stat = list.get(i);

            printer.print(stat.getCaller());
            printer.print(",");
            printer.print(stat.getMinSize());
            printer.print(",");
            printer.print(stat.getMaxSize());
            printer.print(",");
            printer.print(stat.getMaxSize() - stat.getMinSize());
            printer.print(",");
            printer.print(stat.getSamplesSize());
            printer.print(",");
            printer.print(stat.getTotalSize() / stat.getSamplesSize());
            printer.print(",");
            printer.println(stat.getInitSize());

            sampleSize += stat.getSamplesSize();
            totalSize += stat.getTotalSize();

            if (stat.getMaxSize() > maxSize) {
                maxSize = stat.getMaxSize();
            }
        }

        int avg = 0;

        if (sampleSize > 0) {
            avg = totalSize / sampleSize;
        }

        printer.print("SAMPLES=");
        printer.print(sampleSize);
        printer.print(", AVERAGE=");
        printer.print(avg);
        printer.print(", MAX=");
        printer.println(maxSize);
    }

    //private String _name;
    private Map<String, SizeSample> _map = new HashMap<String, SizeSample>();
    private int _count;

    private class SizeSample implements Comparable<SizeSample> {

        public SizeSample(String caller, int initSize) {
            this._caller = caller;
            this._initSize = initSize;
            this._minSize = Integer.MAX_VALUE;
            this._maxSize = Integer.MIN_VALUE;
        }

        public void add(int finalSize) {
            if (finalSize < this._minSize) {
                this._minSize = finalSize;
            }

            if (finalSize > this._maxSize) {
                this._maxSize = finalSize;
            }

            this._samplesSize++;
            this._totalSize += finalSize;
        }

        public String getCaller() {
            return this._caller;
        }

        public int getInitSize() {
            return this._initSize;
        }

        public int getMaxSize() {
            return this._maxSize;
        }

        public int getMinSize() {
            return this._minSize;
        }

        public int getSamplesSize() {
            return this._samplesSize;
        }

        public int getTotalSize() {
            return this._totalSize;
        }

        @Override
		public int compareTo(SizeSample other) {
            int thisAvg = 0;

            if (this._samplesSize > 0) {
                thisAvg = this._totalSize / this._samplesSize;
            }

            int otherAvg = 0;

            if (other.getSamplesSize() > 0) {
                otherAvg = other.getTotalSize() / other.getSamplesSize();
            }

            return otherAvg - thisAvg;
        }

        private String _caller;
        private int _initSize;
        private int _maxSize;
        private int _minSize;
        private int _samplesSize;
        private int _totalSize;

    }
}
