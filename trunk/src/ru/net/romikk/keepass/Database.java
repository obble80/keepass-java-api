package ru.net.romikk.keepass;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.*;
import org.bouncycastle.util.encoders.Hex;

import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.security.MessageDigest;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 22, 2008
 * Time: 4:10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Database {
    FileChannel channel;
    Header header;
    Group[] groups;

    byte[] masterKey = Hex.decode("83b62ec2690df02ce7b2f94208469decd93fb0d2febbc2408c86ae7860f5d6af");

    public Database() throws FileNotFoundException {
        channel = new FileInputStream("Database.kdb").getChannel();
    }

    public Header getHeader() throws IOException {
        if (header == null) {
            ByteBuffer bb = ByteBuffer.allocate(124);
            channel.read(bb, 0);
            header = new Header(bb);
        }
        return header;
    }

    public Group[] getGroups() throws IOException {
        if (groups == null) {
            channel.position(124);
            groups = new Group[getHeader().getGroups()];
            for (int i = 0; i < groups.length; i++) {
                ByteBuffer meta = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN);
                channel.read(meta);
                short fieldType = meta.getShort();
                int fieldSize = meta.getInt();
                ByteBuffer fieldData = ByteBuffer.allocate(fieldSize).order(ByteOrder.LITTLE_ENDIAN);
                channel.read(fieldData);
                groups[i] = new Group(fieldType, fieldData);
            }
        }
        return groups;
    }

    public byte[] processECBCipher(byte[] abyKey, byte[] abyMsg) throws InvalidCipherTextException {
        BlockCipher engine = new AESEngine();
        BufferedBlockCipher cipher = new BufferedBlockCipher(engine);
        KeyParameter oKeyParameter = new KeyParameter(abyKey);

        cipher.init(true, oKeyParameter);

        byte[] abyOut = new byte[cipher.getOutputSize(abyMsg.length)];

        int outputLen = cipher.processBytes(abyMsg, 0, abyMsg.length, abyOut, 0);

        cipher.doFinal(abyOut, outputLen);

        return abyOut;
    }

    public byte[] processCBCCipher(byte[] abyKey, byte[] abyMsg, byte[] iv) throws InvalidCipherTextException {
        BlockCipher engine = new AESEngine();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine), new ZeroBytePadding());
        KeyParameter oKeyParameter = new KeyParameter(abyKey);

        cipher.init(false, new ParametersWithIV(oKeyParameter, iv));

        byte[] abyOut = new byte[cipher.getOutputSize(abyMsg.length)];

        int outputLen = cipher.processBytes(abyMsg, 0, abyMsg.length, abyOut, 0);

        cipher.doFinal(abyOut, outputLen);

        return abyOut;
    }

    public ByteBuffer decrypt() throws Exception {

        byte[] key = masterKey;
        for (int i = 0; i < getHeader().getKeyEncRounds(); i++) {
            key = processECBCipher(getHeader().getMasterSeed2(), key);
        }

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] transformedKey = sha256.digest(key);

        sha256.reset();
        sha256.update(getHeader().getMasterSeed());
        sha256.update(transformedKey);
        byte[] finalKey = sha256.digest();


        ByteBuffer content = ByteBuffer.allocate((int) channel.size() - 124).order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer plainContent = ByteBuffer.allocate((int) channel.size() - 124).order(ByteOrder.LITTLE_ENDIAN);
        channel.read(content, 124);
        plainContent.put(processCBCCipher(finalKey, content.array(), getHeader().getEncryptionIV()));

        sha256.reset();

        byte[] a = getHeader().getContentsHash();
        byte[] b = sha256.digest(plainContent.array());
//        Hex.encode(a, System.out);
//        System.out.println(printBytes(a));
//        Hex.encode(b, System.out);
//        System.out.println(printBytes(b));
        System.out.println(Arrays.hashCode(a) == Arrays.hashCode(b));
        return plainContent;
    }
    private static String printBytes(byte[] b)
    {
        StringBuilder sb = new StringBuilder(8*b.length);
        for(int i = 0; i<b.length; i++)
        {
            sb.append(printByte(b[i])+"|");
        }
        return sb.toString();
    }

    private static String printByte(byte b)
    {
        StringBuilder sb = new StringBuilder(8);
        for(int i = 0; i<8; i++)
        {
            sb.append(((b >> i) & 0x1) == 1 ? "1" : "0");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
//        new Database().decrypt();
        System.out.println(new String(new Database().decrypt().array()));
    }
}
