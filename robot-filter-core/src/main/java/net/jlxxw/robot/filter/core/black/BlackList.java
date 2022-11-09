package net.jlxxw.robot.filter.core.black;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import net.jlxxw.robot.filter.common.event.AddClientToBlackListEvent;
import net.jlxxw.robot.filter.config.properties.filter.FilterProperties;
import net.jlxxw.robot.filter.config.properties.filter.RuleProperties;
import net.jlxxw.robot.filter.config.properties.filter.servlet.RobotFilterServletFilterProperties;
import net.jlxxw.robot.filter.core.lru.TimeOutLru;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-09 4:29 PM
 */
@Component
public class BlackList {
    /**
     * key rule name
     * value : key ip,value other
     */
    private Map<String, TimeOutLru<String, ClientInfo>> ipBlackLru = new ConcurrentHashMap<>();

    /**
     * key rule name
     * value : key client id,value other
     */
    private Map<String, TimeOutLru<String, ClientInfo>> clientIdLru = new ConcurrentHashMap<>();

    @Autowired
    private RobotFilterServletFilterProperties servletFilterProperties;

    // todo spring cloud gateway support

    private FilterProperties filterProperties;

    @PostConstruct
    private void init() {
        filterProperties = servletFilterProperties.getFilter();
        List<RuleProperties> rules = filterProperties.getRules();
        for (RuleProperties rule : rules) {
            String name = rule.getName();
            if (ipBlackLru.containsKey(name)) {
                throw new BeanCreationException(" rule name " + name + " already exists !!!");
            }
            if (clientIdLru.containsKey(name)) {
                throw new BeanCreationException(" rule name " + name + " already exists !!!");
            }
            long expiryTime = -1;
            boolean blacklisted = rule.isAllowRemoveBlacklisted();
            if (blacklisted) {
                expiryTime = rule.getBlacklistedTime();
            }

            // no max
            TimeOutLru<String, ClientInfo> ipBlackLru = new TimeOutLru<>(-1, expiryTime, 16);
            this.ipBlackLru.put(name, ipBlackLru);
            // no max
            TimeOutLru<String, ClientInfo> clientIdBlackLru = new TimeOutLru<>(-1, expiryTime, 16);
            this.clientIdLru.put(name, clientIdBlackLru);
        }

    }

    @EventListener(AddClientToBlackListEvent.class)
    public void addIpBlackLru(AddClientToBlackListEvent event) {
        RuleProperties properties = event.getRuleProperties();
        // rule name
        String name = properties.getName();
        TimeOutLru<String, ClientInfo> ipLru = ipBlackLru.get(name);

        String ip = event.getIp();
        String clientId = event.getClientId();
        ClientInfo info = ipLru.get(ip);
        if (Objects.isNull(info)) {
            info = new ClientInfo();
            info.setRule(event.getRuleProperties());
            ipLru.put(ip, info);
        }
        info.addIdentities(clientId);
    }

    @EventListener(AddClientToBlackListEvent.class)
    public void addClientIdToBlackLru(AddClientToBlackListEvent event) {
        RuleProperties properties = event.getRuleProperties();
        String name = properties.getName();
        TimeOutLru<String, ClientInfo> clientLru = clientIdLru.get(name);

        String ip = event.getIp();
        String clientId = event.getClientId();
        ClientInfo info = clientLru.get(clientId);
        if (Objects.isNull(info)) {
            info = new ClientInfo();
            info.setRule(event.getRuleProperties());
            clientLru.put(clientId, info);
        }
        info.addIdentities(ip);
    }

    public RuleProperties inBlackList(String info) {

        Set<Map.Entry<String, TimeOutLru<String, ClientInfo>>> entries = ipBlackLru.entrySet();
        for (Map.Entry<String, TimeOutLru<String, ClientInfo>> entry : entries) {
            TimeOutLru<String, ClientInfo> value = entry.getValue();
            ClientInfo clientInfo = value.get(info);
            if (Objects.nonNull(clientInfo)) {
                return clientInfo.getRule();
            }
        }

        Set<Map.Entry<String, TimeOutLru<String, ClientInfo>>> entries2 = clientIdLru.entrySet();
        for (Map.Entry<String, TimeOutLru<String, ClientInfo>> entry : entries2) {
            TimeOutLru<String, ClientInfo> value = entry.getValue();
            ClientInfo clientInfo = value.get(info);
            if (Objects.nonNull(clientInfo)) {
                return clientInfo.getRule();
            }
        }
        return null;
    }

}
