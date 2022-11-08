package demo;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Solution {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<TravelAgencies> travelAgencies = new ArrayList<>();

        for (int i=0;i<4;i++){
            int regNo = scanner.nextInt();
            String agencyName = scanner.next();
            String packageType= scanner.next();
            int price = scanner.nextInt();
            boolean flightFacility = Boolean.parseBoolean(scanner.next());

            travelAgencies.add(new TravelAgencies(regNo,agencyName,packageType,price,flightFacility));
            scanner.nextLine();
        }
        int regNo = scanner.nextInt();
        String packageType= scanner.next();

        int travelAgencies2 = findAgencyWithHighestPackagePrice(travelAgencies);
        log.info("travel agencies:{}",travelAgencies2);

        TravelAgencies travelAgencies1 = agencyDetailsForGivenIdAndType(travelAgencies,regNo,packageType);
        assert travelAgencies1 != null;
        log.info("Concatinated:{}",travelAgencies1.getAgencyName().concat(String.valueOf(travelAgencies1.getPrice())));
    }

    public static int findAgencyWithHighestPackagePrice (List<TravelAgencies> travelAgencies){
        int maxPrice = 0;

        for (TravelAgencies travelAgency : travelAgencies) {
            if (travelAgency.getPrice()>maxPrice){
                maxPrice=travelAgency.getPrice();
            }
            return maxPrice;
        }
        return maxPrice;
    }
    public static TravelAgencies agencyDetailsForGivenIdAndType(List<TravelAgencies> travelAgencies, int regNo , String packageType){
        for (TravelAgencies travelAgency : travelAgencies) {
            if (travelAgency.isFlightFacility() && travelAgency.getRegNo() == regNo && travelAgency.getPackageType().equalsIgnoreCase(packageType)) {
                return travelAgency;
            }
        }
        return null;
    }
}
