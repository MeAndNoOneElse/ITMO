/**
 * Simple test to verify JSON formatting works correctly
 * This test doesn't require FastCGI library
 */
public class JsonTest {
    public static void main(String[] args) {
        System.out.println("Testing JSON formatting...\n");
        
        // Test success response
        String successJson = buildSuccessResponse(1.0, 2.5, 3.0, true);
        System.out.println("Success Response:");
        System.out.println(successJson);
        System.out.println();
        
        // Test error response
        String errorJson = buildErrorResponse("Invalid parameters");
        System.out.println("Error Response:");
        System.out.println(errorJson);
        System.out.println();
        
        // Verify no extra newlines
        if (successJson.contains("\n")) {
            System.err.println("ERROR: JSON contains newline characters!");
        } else {
            System.out.println("✓ Success JSON has no newlines");
        }
        
        if (errorJson.contains("\n")) {
            System.err.println("ERROR: JSON contains newline characters!");
        } else {
            System.out.println("✓ Error JSON has no newlines");
        }
        
        // Test validation
        System.out.println("\nTesting Y validation (valid range: [-3, 5])...");
        testValidation(-3.1);   // Should be invalid (below range)
        testValidation(-3.0);   // Should be valid (boundary)
        testValidation(-2.9);   // Should be valid
        testValidation(0.0);    // Should be valid
        testValidation(4.9);    // Should be valid
        testValidation(5.0);    // Should be valid (boundary)
        testValidation(5.1);    // Should be invalid (above range)
    }
    
    private static String buildSuccessResponse(double x, double y, double r, boolean result) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":true,");
        json.append("\"x\":").append(x).append(",");
        json.append("\"y\":").append(y).append(",");
        json.append("\"r\":").append(r).append(",");
        json.append("\"result\":").append(result);
        json.append("}");
        return json.toString();
    }
    
    private static String buildErrorResponse(String message) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\":false,");
        json.append("\"error\":\"").append(message).append("\"");
        json.append("}");
        return json.toString();
    }
    
    private static boolean validateY(double y) {
        // Y must be in range [-3, 5] - inclusive boundaries
        // Invalid if: y < -3 OR y > 5
        return !(y < -3 || y > 5);
    }
    
    private static void testValidation(double y) {
        boolean valid = validateY(y);
        System.out.printf("Y = %.1f: %s%n", y, valid ? "✓ Valid" : "✗ Invalid");
    }
}
