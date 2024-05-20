package com.isa.pipeline;

import com.isa.block.Block;

import java.util.ArrayList;
import java.util.Collection;

public class TestPipeline {

    private final Collection<Block> steps;

    public TestPipeline(Collection<Block> steps) {
        this.steps = new ArrayList<>(steps);
    }

    public void execute() {
        for (final Block step : steps) {
            step.process();
        }
    }
}