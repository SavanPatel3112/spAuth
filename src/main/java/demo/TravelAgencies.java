package demo;
public class TravelAgencies {
    int regNo ;
    String agencyName ;
    String packageType;
    int price ;
    boolean flightFacility;
    public int getRegNo() {
        return regNo;
    }
    public void setRegNo(int regNo) {
        this.regNo = regNo;
    }
    public String getAgencyName() {
        return agencyName;
    }
    public String getPackageType() {
        return packageType;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public boolean isFlightFacility() {
        return flightFacility;
    }
    public TravelAgencies(int regNo, String agencyName, String packageType, int price, boolean flightFacility) {
        this.regNo = regNo;
        this.agencyName = agencyName;
        this.packageType = packageType;
        this.price = price;
        this.flightFacility = flightFacility;
    }
    @Override
    public String toString() {
        return "TravelAgencies{" +
                "regNo=" + regNo +
                ", agencyName='" + agencyName + '\'' +
                ", packageType='" + packageType + '\'' +
                ", price=" + price +
                ", flightFacility=" + flightFacility +
                '}';
    }
}
