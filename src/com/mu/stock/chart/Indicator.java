package com.mu.stock.chart;

import org.jfree.chart.renderer.xy.StandardXYItemRenderer;

/**
 *
 * @author Peng Mu
 */
public class Indicator
{
    public String name;
    public String params;
    public int weight;
    public String plot;
    public Class render = StandardXYItemRenderer.class;
    public IDType type = IDType.Indicator;
    public Indicator(Indicator id)
    {
        this.name = id.name;
        this.params = id.params;
        this.weight = id.weight;
        this.plot = id.plot;
        this.render = id.render;
    }
    public Indicator(String name, String params, int weight, String plot)
    {
        this.name = name;
        this.params = params;
        this.weight = weight;
        this.plot = plot;
    }
    public Indicator(String name, String params, int weight, String plot, Class render)
    {
        this(name, params, weight, plot);
        this.render = render;
    }
    public Indicator(String name, String params, int weight, String plot, Class render, IDType type)
    {
        this(name, params, weight, plot, render);
        this.type = type;

    }

    public String toString()
    {
        return String.format("%s(%s)", name, params);
    }

}
