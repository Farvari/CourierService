package model;

public class Package {
    private final String id;
    private final String description;
    private final double weightKg;

    public Package(String id, String description, double weightKg) {
        this.id = id;
        this.description = description;
        this.weightKg = weightKg;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public double getWeightKg() { return weightKg; }
}
