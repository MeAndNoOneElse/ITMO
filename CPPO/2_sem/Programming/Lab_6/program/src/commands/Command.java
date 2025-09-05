package commands;

import support.ExecutionResponse;

public abstract class Command {
    private final String name;
    private final String description;
    private boolean withDopArg;
    private int mustArg;
    private int dopArg;

    public boolean getWithDopArg() {
        return withDopArg;
    }

    public int getMustArg() {
        return mustArg;
    }

    public int getDopArg() {
        return dopArg;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public Command(String name, String description, boolean withDopArg, int mustArg, int dopArg) {
        this.name = name;
        this.description = description;
        this.withDopArg = withDopArg;
        this.mustArg = mustArg;
        this.dopArg = dopArg;
    }

    public abstract ExecutionResponse execute(String[] text);


}

