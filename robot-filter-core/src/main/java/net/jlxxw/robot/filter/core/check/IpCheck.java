package net.jlxxw.robot.filter.core.check;

import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * ip address check
 * @author chunyang.leng
 * @date 2022-11-09 2:55 PM
 */
@Component
public class IpCheck {


    /**
     * check ip in set collection
     * @param ip check this ip
     * @param ipSet ip or ip range
     * @return true if in set collection, false otherwise
     */
    public boolean checkIpInSet(String ip, Set<String> ipSet){
        if (StringUtils.isBlank(ip)){
            return false;
        }
        if (CollectionUtils.isEmpty(ipSet)){
            return false;
        }
        if (ipSet.contains(ip)){
            return true;
        }
        for (String ipAndRange : ipSet) {
            if (inRange(ip,ipAndRange)){
                return true;
            }
        }
        return false;
    }



    private  boolean inRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
            | (Integer.parseInt(ips[1]) << 16)
            | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
            | (Integer.parseInt(cidrIps[1]) << 16)
            | (Integer.parseInt(cidrIps[2]) << 8)
            | Integer.parseInt(cidrIps[3]);

        return (ipAddr & mask) == (cidrIpAddr & mask);
    }
}
