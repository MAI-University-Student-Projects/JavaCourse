package org.mai.dep810.stream_api_lesson;

enum Petal {
    SMALL, MEDIUM, LARGE
}

public class Iris {
    //длина чашелистника
    private final double _sepalLength;

    //ширина чашелистника
    private final double _sepalWidth;

    //длина лепестка
    private final double _petalLength;

    //ширина лепестка
    private final double _petalWidth;

    //вид
    private final String _species;

    public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String species) {
        this._sepalLength = sepalLength;
        this._sepalWidth = sepalWidth;
        this._petalLength = petalLength;
        this._petalWidth = petalWidth;
        this._species = species;
    }

    public double getSepalLength() {
        return _sepalLength;
    }

    public double getSepalWidth() {
        return _sepalWidth;
    }

    public double getPetalLength() {
        return _petalLength;
    }

    public double getPetalWidth() {
        return _petalWidth;
    }

    public String getSpecies() {
        return _species;
    }

    static Iris parse(String line) {
        String[] parts = line.split(",");

        return new Iris(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                parts[4]
        );
    }

    @Override
    public String toString() {
        return "Iris{" +
                "sepalLength=" + _sepalLength +
                ", sepalWidth=" + _sepalWidth +
                ", petalLength=" + _petalLength +
                ", petalWidth=" + _petalWidth +
                ", species='" + _species + '\'' +
                '}';
    }

    public Petal classifyByPetal() {
        double patelSquare = _petalLength * _petalWidth;
        if(patelSquare < 2.0) {
            return Petal.SMALL;
        } else if(patelSquare < 5.0) {
            return Petal.MEDIUM;
        } else return Petal.LARGE;
    }
}
