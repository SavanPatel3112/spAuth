package demo;

public class Engine {

    int engineId;

    String engineName ;

    String engineType ;

    double enginePrice ;

    public int getEngineId() {
        return engineId;
    }

    public void setEngineId(int engineId) {
        this.engineId = engineId;
    }

    public String getEngineName() {
        return engineName;
    }

    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public double getEnginePrice() {
        return enginePrice;
    }

    public void setEnginePrice(double enginePrice) {
        this.enginePrice = enginePrice;
    }

    public Engine(int engineId, String engineName, String engineType, double enginePrice) {
        this.engineId = engineId;
        this.engineName = engineName;
        this.engineType = engineType;
        this.enginePrice = enginePrice;
    }

    @Override
    public String toString() {
        return "Engine{" +
                "engineId=" + engineId +
                ", engineName='" + engineName + '\'' +
                ", engineType='" + engineType + '\'' +
                ", enginePrice=" + enginePrice +
                '}';
    }
}
