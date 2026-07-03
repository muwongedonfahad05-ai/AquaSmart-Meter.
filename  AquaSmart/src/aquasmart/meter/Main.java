
package aquasmart.meter;


public class Main {
    
    public static void main(String[] args) {
        System.out.println("==================================");
        System.out.println("   AQUASMART WATER METER SYSTEM");
        System.out.println("   Kalangala Infrastructure Services");
        System.out.println("==================================\n");
        
        // Test Scenario 1: Create meter with initial credit
        System.out.println(">>> SCENARIO 1: Create Meter");
        SmartMeter meter = new SmartMeter("KIS-001", 5000.0);
        meter.displayStatus();
        
        // Test Scenario 2: Normal consumption
        System.out.println("\n>>> SCENARIO 2: Normal Water Consumption");
        meter.recordConsumption(50.0);  // 50 litres
        meter.displayStatus();
        
        // Test Scenario 3: Consumption exceeding credit
        System.out.println("\n>>> SCENARIO 3: Consumption Exceeding Credit");
        meter.recordConsumption(80.0);  // 80 litres (should fail)
        meter.displayStatus();
        
        // Test Scenario 4: Load token and reopen valve
        System.out.println("\n>>> SCENARIO 4: Load Token");
        meter.loadToken(3000.0);
        meter.displayStatus();
        
        // Test Scenario 5: Normal consumption after token load
        System.out.println("\n>>> SCENARIO 5: Consumption After Token Load");
        meter.recordConsumption(20.0);
        meter.displayStatus();
        
        System.out.println("\n==================================");
        System.out.println("   SYSTEM TEST COMPLETE");
        System.out.println("==================================");
    }
}