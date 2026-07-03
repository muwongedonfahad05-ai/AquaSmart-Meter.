/*
 * AquaSmart - Smart Water Meter System
 * Kalangala Infrastructure Services Ltd
 */
package aquasmart.meter;

/**
 * SmartMeter represents a prepaid smart water meter
 * Manages credit balance and valve control
 */
public class SmartMeter {
    // Private fields (encapsulation)
    private String meterId;
    private double creditBalance;
    private boolean valveOpen;
    
    // Constants
    private static final double COST_PER_LITRE = 50.0; // UGX 50 per litre
    
    /**
     * Constructor - Creates a new SmartMeter
     * @param meterId Unique meter identifier
     * @param openingCredit Initial credit in UGX
     */
    public SmartMeter(String meterId, double openingCredit) {
        this.meterId = meterId;
        this.creditBalance = Math.max(0, openingCredit);
        this.valveOpen = this.creditBalance > 0;
        System.out.println("Meter " + meterId + " created. Initial balance: UGX " + creditBalance);
    }
    
    /**
     * Loads a water token and re-opens the valve
     * @param amount Amount in UGX
     * @return New credit balance
     */
    public double loadToken(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid token amount. Must be positive.");
            return creditBalance;
        }
        
        creditBalance += amount;
        valveOpen = true;
        System.out.println("Token loaded: UGX " + amount + " | New balance: UGX " + creditBalance + " | Valve: OPEN");
        return creditBalance;
    }
    
    /**
     * Records water consumption
     * @param litres Amount in litres
     * @return true if water dispensed, false if blocked
     */
    public boolean recordConsumption(double litres) {
        // Validate consumption amount
        if (litres <= 0) {
            System.out.println("Invalid consumption amount. Must be positive.");
            return false;
        }
        
        // Check if valve is closed
        if (!valveOpen) {
            System.out.println("Valve CLOSED. Please load a token.");
            return false;
        }
        
        // Calculate cost
        double cost = litres * COST_PER_LITRE;
        
        // Check if credit is sufficient
        if (creditBalance < cost) {
            // Use remaining credit
            double remainingLitres = creditBalance / COST_PER_LITRE;
            System.out.println("Insufficient credit. Dispensing " + String.format("%.2f", remainingLitres) + " litres");
            creditBalance = 0;
            valveOpen = false;
            System.out.println("Credit exhausted. Valve CLOSED.");
            return false;
        }
        
        // Sufficient credit - dispense water
        creditBalance -= cost;
        System.out.println("Dispensed " + litres + " litres. Cost: UGX " + cost + " | Remaining: UGX " + creditBalance);
        
        // Check if credit reached zero
        if (creditBalance <= 0) {
            creditBalance = 0;
            valveOpen = false;
            System.out.println("Credit exhausted. Valve CLOSED.");
        }
        
        return true;
    }
    
    // Getters (encapsulation)
    public String getMeterId() { return meterId; }
    public double getCreditBalance() { return creditBalance; }
    public boolean isValveOpen() { return valveOpen; }
    
    /**
     * Display meter status
     */
    public void displayStatus() {
        System.out.println("\n===== METER STATUS =====");
        System.out.println("Meter ID: " + meterId);
        System.out.println("Balance: UGX " + creditBalance);
        System.out.println("Valve: " + (valveOpen ? "OPEN" : "CLOSED"));
        System.out.println("=======================\n");
    }
}