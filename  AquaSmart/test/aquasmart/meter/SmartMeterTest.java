/*
 * JUnit Tests for SmartMeter
 * AquaSmart Water Meter System
 */
package aquasmart.meter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SmartMeter
 */
public class SmartMeterTest {
    
    private SmartMeter meter;
    
    @BeforeEach
    public void setUp() {
        // Create meter with UGX 1000 initial credit
        meter = new SmartMeter("TEST-001", 1000.0);
        System.out.println("Test setup complete. Meter with UGX 1000 credit.");
    }
    
    /**
     * Test 1: Loading a token re-opens a closed valve
     */
    @Test
    public void testLoadTokenReopensClosedValve() {
        System.out.println("\n>>> TEST 1: Loading token re-opens closed valve");
        
        // Use all credit (20 litres at UGX 50/litre = 1000 UGX)
        boolean consumed = meter.recordConsumption(20.0);
        assertFalse(meter.isValveOpen(), "Valve should be closed after credit exhausted");
        assertEquals(0.0, meter.getCreditBalance(), 0.01);
        
        System.out.println("Before token load - Valve: " + (meter.isValveOpen() ? "OPEN" : "CLOSED"));
        
        // Load new token
        double newBalance = meter.loadToken(2000.0);
        
        // Verify valve re-opened
        assertTrue(meter.isValveOpen(), "Valve should be open after loading token");
        assertEquals(2000.0, newBalance, 0.01);
        assertEquals(2000.0, meter.getCreditBalance(), 0.01);
        
        System.out.println("After token load - Valve: " + (meter.isValveOpen() ? "OPEN" : "CLOSED"));
        System.out.println("TEST 1 PASSED ✓");
    }
    
    /**
     * Test 2: Consumption beyond available credit closes the valve
     */
    @Test
    public void testConsumptionBeyondCreditClosesValve() {
        System.out.println("\n>>> TEST 2: Consumption beyond credit closes valve");
        
        // Initial state
        assertTrue(meter.isValveOpen(), "Valve should be open initially");
        assertEquals(1000.0, meter.getCreditBalance(), 0.01);
        System.out.println("Initial: Balance = UGX " + meter.getCreditBalance() + ", Valve: " + (meter.isValveOpen() ? "OPEN" : "CLOSED"));
        
        // Try to consume 25 litres (cost 1250 UGX, 250 more than available)
        boolean result = meter.recordConsumption(25.0);
        
        // Should return false
        assertFalse(result, "Should return false when insufficient credit");
        
        // Valve should be closed
        assertFalse(meter.isValveOpen(), "Valve should be closed after insufficient credit");
        
        // Balance should be 0 (used remaining credit)
        assertEquals(0.0, meter.getCreditBalance(), 0.01);
        
        System.out.println("Final: Balance = UGX " + meter.getCreditBalance() + ", Valve: " + (meter.isValveOpen() ? "OPEN" : "CLOSED"));
        System.out.println("TEST 2 PASSED ✓");
    }
    
    /**
     * Test 3: Normal consumption keeps valve open
     */
    @Test
    public void testNormalConsumptionKeepsValveOpen() {
        System.out.println("\n>>> TEST 3: Normal consumption keeps valve open");
        
        // Consume 10 litres (500 UGX)
        boolean result = meter.recordConsumption(10.0);
        
        assertTrue(result, "Should return true for successful consumption");
        assertTrue(meter.isValveOpen(), "Valve should remain open with credit remaining");
        assertEquals(500.0, meter.getCreditBalance(), 0.01);
        
        System.out.println("After consumption: Balance = UGX " + meter.getCreditBalance() + ", Valve: " + (meter.isValveOpen() ? "OPEN" : "CLOSED"));
        System.out.println("TEST 3 PASSED ✓");
    }
    
    /**
     * Test 4: Invalid token amount
     */
    @Test
    public void testInvalidTokenAmount() {
        System.out.println("\n>>> TEST 4: Invalid token amount");
        
        double initialBalance = meter.getCreditBalance();
        
        // Try to load negative amount
        double result = meter.loadToken(-1000.0);
        
        // Balance should remain unchanged
        assertEquals(initialBalance, result, 0.01);
        assertEquals(initialBalance, meter.getCreditBalance(), 0.01);
        assertTrue(meter.isValveOpen(), "Valve should remain open");
        
        System.out.println("Invalid token amount rejected. Balance unchanged: UGX " + meter.getCreditBalance());
        System.out.println("TEST 4 PASSED ✓");
    }
    
    /**
     * Test 5: Meter creation with zero credit
     */
    @Test
    public void testMeterCreationWithZeroCredit() {
        System.out.println("\n>>> TEST 5: Meter creation with zero credit");
        
        SmartMeter zeroMeter = new SmartMeter("ZERO-001", 0.0);
        
        assertFalse(zeroMeter.isValveOpen(), "Valve should be closed with zero credit");
        assertEquals(0.0, zeroMeter.getCreditBalance(), 0.01);
        
        // Should not allow consumption
        boolean consumed = zeroMeter.recordConsumption(1.0);
        assertFalse(consumed, "Should not allow consumption with zero credit");
        
        System.out.println("Zero credit meter: Valve " + (zeroMeter.isValveOpen() ? "OPEN" : "CLOSED"));
        System.out.println("TEST 5 PASSED ✓");
    }
}