package net.jlxxw.robot.filter.common.encrypt;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import net.jlxxw.robot.filter.config.properties.encrypt.RobotFilterEncryptProperties;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chunyang.leng
 * @date 2022-11-03 3:21 PM
 */
@Component
public class DesEncryption {

    @Autowired
    private RobotFilterEncryptProperties robotFilterEncryptProperties;

    @PostConstruct
    private void init() {
        if (robotFilterEncryptProperties.isEnabled()){
            // Self inspection and test encrypt
            String uuid = UUID.randomUUID().toString();
            try {
                String encryptStr = encrypt(uuid);
                String decrypt = decrypt(encryptStr);
                if (!decrypt.equals(uuid)){
                    throw new BeanCreationException("decrypt not equals old value !!!");
                }
            } catch (Exception e) {
                throw new BeanCreationException("test encrypt failed: " + e.getMessage());
            }

        }
    }

    /**
     * 加密
     *
     * @param dataSource 原始字符串
     * @return 加密后的字符串
     */
    public String encrypt(String dataSource) throws Exception {
        if (!robotFilterEncryptProperties.isEnabled()){
            return dataSource;
        }
        DESKeySpec desKey = new DESKeySpec(robotFilterEncryptProperties.getPassword().getBytes());
        //创建一个密匙工厂，获取secretKey
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKey);
        //指定获取DES的Cipher对象
        Cipher cipher = Cipher.getInstance("DES");
        //用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());
        //数据加密
        byte[] bytes = cipher.doFinal(dataSource.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(bytes);

    }

    /**
     * 解密
     *
     * @param src 待解密的字符串
     * @return 解密后的字符串
     */
    public String decrypt(String src) throws Exception {
        if (!robotFilterEncryptProperties.isEnabled()){
            return src;
        }
        // 创建一个DESKeySpec对象，PASSWORD可任意指定
        DESKeySpec desKey = new DESKeySpec(robotFilterEncryptProperties.getPassword().getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 生成密钥
        SecretKey secretkey = keyFactory.generateSecret(desKey);
        // 指定获取DES的Cipher对象
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secretkey, new SecureRandom());
        // 真正开始解密操作
        byte[] decode = Base64.getDecoder().decode(src);
        byte[] bytes = cipher.doFinal(decode);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
