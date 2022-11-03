package net.jlxxw.robot.filter.core.identity;

import java.util.UUID;
import net.jlxxw.robot.filter.common.encrypt.DesEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Identification
 *
 * @author chunyang.leng
 * @date 2022-11-03 3:18 PM
 */
@Component
public class ClientIdentification {
    @Autowired
    private DesEncryption desEncryption;

    /**
     * create a new client id
     *
     * @return client id ,unique identifier
     */
    public String createClientId() throws Exception {
        String id = UUID.randomUUID().toString();
        return desEncryption.encrypt(id);
    }

    /**
     * verify data
     * @param clientId client id
     * @throws Exception verify failed
     */
    public void verifyClientId(String clientId) throws Exception {
        desEncryption.decrypt(clientId);
    }
}
