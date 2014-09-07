package monitor.attribute.parser;

import testimpl.AttrServerMBean;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

public class JmxConnectTest {

    public static void main(String[] args) throws IOException, MalformedObjectNameException {
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + 1617 + "/jmxrmi");
        JMXConnector jmxConnector = JMXConnectorFactory.connect(url);
        MBeanServerConnection mbeanServerConnection = jmxConnector.getMBeanServerConnection();
        //ObjectName should be same as your MBean name
        ObjectName mbeanName = new ObjectName("FOO:name=AttributeServer");

        //Get MBean proxy instance that will be used to make calls to registered MBean
        AttrServerMBean mbeanProxy =
                MBeanServerInvocationHandler.newProxyInstance(
                        mbeanServerConnection, mbeanName, AttrServerMBean.class, true);
        System.out.println(mbeanProxy);
        mbeanProxy.getAttributeByName("Test1");
        System.out.println(mbeanProxy.getAttributeByName("Test1"));
    }
}
