package com.perceptron;

import java.util.HashMap;
import java.util.Map;

public class Neuron {

    private double value;
    private Map<Neuron, Double> enter = new HashMap<>();
    private Map<Neuron, Double> exit = new HashMap<>();
    private double sigma;

    public Neuron() {
    }

    public void activation() {
        for (Neuron n : enter.keySet()) {
            value += n.getValue() * enter.get(n);
        }
        value = 1 / (1 + Math.pow(Math.E, -value));
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Map<Neuron, Double> getEnter() {
        return enter;
    }

    public Map<Neuron, Double> getExit() {
        return exit;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
}
