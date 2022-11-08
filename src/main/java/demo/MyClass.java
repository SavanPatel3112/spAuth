package demo;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class MyClass {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Engine> engineList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int engineId = scanner.nextInt();
            String engineName = scanner.next();
            String engineType = scanner.next();
            double enginePrice = scanner.nextDouble();

            engineList.add(new Engine(engineId, engineName, engineType, enginePrice));
            scanner.nextLine();
        }

        String engineType = scanner.next();
        String engineName = scanner.next();

        double engine2 = findAvgEnginePriceByType(engineList, engineType);
        if (engine2 == 0)
            log.info("There are no engine with given type");
        else
            log.info("engine average price:{}", engine2);

        List<Engine> engines = searchEngineByName(engineList, engineName);
        log.info("listOfEngine:{}", engines);
    }

    public static double findAvgEnginePriceByType(List<Engine> engine, String engineType) {

        double averagePrice = 0;
        int count = 0;
        for (Engine engine1 : engine) {
            if (engine1.getEngineType().equals(engineType)) {
                for (int i = 0; i < engine1.getEnginePrice(); i++) {
                    /*averagePrice= (int) (engine1.getEnginePrice()/engine.size());*/
                    averagePrice = averagePrice + engine1.getEnginePrice();
                    count++;
                }
            }
        }
        return averagePrice / count;
    }

    public static List<Engine> searchEngineByName(List<Engine> engine, String engineName) {
        List<Engine> engineList = new ArrayList<>();
        for (Engine engine1 : engine) {
            if (engine1.getEngineName().equals(engineName)) {
                engineList.add(engine1);
            }
        }
        engineList.sort(Comparator.comparingInt(Engine::getEngineId));
        return engineList;
    }
}
