package com.ylw.url2epub.utils.digest;

import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    // KeyGenerator 提供对称密钥生成器的功能,支持各种算法
    @SuppressWarnings("unused")
    private KeyGenerator keygen;
    // SecretKey 负责保存对称密钥
    private SecretKey deskey;
    // Cipher负责完成加密或解密工作
    private Cipher c;
    // 该字节数组负责保存加密的结果
    private byte[] cipherByte;

    public AES(String key) throws Exception {
        // 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
        keygen = KeyGenerator.getInstance("AES");
        // 生成密钥
        // deskey = keygen.generateKey();
        deskey = new SecretKeySpec(Hex.hexStringToBytes(key), "AES");
        // System.out.println("KEY:" +
        // Hex.bytesToHexString(deskey.getEncoded()));
        // 生成Cipher对象,指定其支持的DES算法
        c = Cipher.getInstance("AES");
    }

    /**
     * 对字符串加密
     * 
     * @param str
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] encrytor(String str) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        c.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] src = str.getBytes();
        // 加密，结果保存进cipherByte
        cipherByte = c.doFinal(src);
        return cipherByte;
    }

    /**
     * 对字符串解密
     * 
     * @param buff
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] decryptor(byte[] buff) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
        c.init(Cipher.DECRYPT_MODE, deskey);
        cipherByte = c.doFinal(buff);
        return cipherByte;
    }

}
