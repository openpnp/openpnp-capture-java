import org.junit.Test;
import org.openpnp.capture.OpenPnpCapture;

public class LoadLibraryTest {
    @Test
    public void testLoadLibrary() {
        OpenPnpCapture capture = new OpenPnpCapture();
        System.out.println("Version: " + capture.getLibraryVersion());
    }
}
