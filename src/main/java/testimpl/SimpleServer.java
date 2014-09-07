package testimpl;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Simple server implementation.
 *
 * @author Evgenii Morenkov
 */
public class SimpleServer {
    private MBeanServer mbs = null;

    public SimpleServer() {

        // Get the platform MBeanServer
        mbs = ManagementFactory.getPlatformMBeanServer();

        // Unique identification of MBeans
        AttrServer attrServer = new AttrServer();
        ObjectName attrServerName = null;

        try {
            // Uniquely identify the MBeans and register them with the platform MBeanServer
            attrServerName = new ObjectName("FOO:name=AttributeServer");
            mbs.registerMBean(attrServer, attrServerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Utility method: so that the application continues to run
    private static void waitForEnterPressed() {
        try {
            System.out.println("Press  to stop server...");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String argv[]) {
        new SimpleServer();
        System.out.println("testimpl.SimpleServer is running...");
        SimpleServer.waitForEnterPressed();
    }
}
