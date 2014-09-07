package monitor.attribute.parser;

import monitor.attribute.Attribute;
import monitor.attribute.Catalog;
import monitor.attribute.exception.BuildCatalogException;
import testimpl.AttrServerMBean;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration parser for jmx agent.
 *
 * @author Evgenii Morenkov
 */
public class JmxConfParser implements IParser {
    public static final String SERVER_NAME = "FOO:name=AttributeServer";
    public static final int PORT_NUM = 1617;
    private HashMap<String, Integer> keyMap = new HashMap<>();

    private AttrServerMBean attrServerMBean;
    private String jmxServerUrl;

    private Catalog catalog;

    /**
     * Constructor. Retrieve  attrServerMBean proxy.
     *
     * @param jmxServerUrl - jmx server url.
     * @throws IOException
     * @throws MalformedObjectNameException
     */
    public JmxConfParser(String jmxServerUrl) throws IOException, MalformedObjectNameException {
        this.jmxServerUrl = jmxServerUrl;
        JMXServiceURL url = new JMXServiceURL(jmxServerUrl);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(url);
        MBeanServerConnection mbeanServerConnection = jmxConnector.getMBeanServerConnection();
        //ObjectName should be same as your MBean name
        ObjectName mbeanName = new ObjectName(SERVER_NAME);

        //Get MBean proxy instance that will be used to make calls to registered MBean
        attrServerMBean = MBeanServerInvocationHandler.newProxyInstance(
                mbeanServerConnection, mbeanName, AttrServerMBean.class, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getNewCatalog() throws BuildCatalogException {
        catalog = new Catalog();
        ArrayList<Attribute> attributes = attrServerMBean.getAttributes();
        ConcurrentHashMap<String, Attribute> attrMap = new ConcurrentHashMap<>();
        for (Attribute attribute : attributes) {
            attrMap.put(attribute.getAttrName(), attribute);
        }
        catalog.setAttrMap(attrMap);
        return catalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAttribute(Attribute attribute) {
        attrServerMBean.updateAttribute(attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute getAttribute(String name) {
        return attrServerMBean.getAttributeByName(name);
    }

    /**
     * Gets Holds all attribute. USE Collections.synchronizedMap..
     *
     * @return Value of Holds all attribute. USE Collections.synchronizedMap..
     */
    public ConcurrentHashMap<String, Attribute> getAttrMap() {
        return catalog == null ? null : catalog.getAttrMap();
    }
}
