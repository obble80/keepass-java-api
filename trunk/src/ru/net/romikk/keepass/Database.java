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
import java.io.IOException;
import java.io.File;
import java.util.Arrays;
import java.security.MessageDigest;

/**
 * $Id: $
 */
public class Database {
    private BlockCipher aesEngine = new AESEngine();
    private BufferedBlockCipher ecbCipher = new BufferedBlockCipher(aesEngine);
    private BufferedBlockCipher cbcCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(aesEngine), new ZeroBytePadding());

    private Header header;
    private Group[] groups;
    private ByteBuffer plainContent;

    private byte[] masterKey = Hex.decode("83b62ec2690df02ce7b2f94208469decd93fb0d2febbc2408c86ae7860f5d6af");

    public Database(String file) throws Exception {
        this(new File(file).getCanonicalFile());
    }

    public Database(File file) throws Exception {
        FileChannel channel = new FileInputStream(file).getChannel();

        ByteBuffer bb = ByteBuffer.allocate(Header.LENGTH);
        channel.read(bb);
        header = new Header(bb);

        ByteBuffer content = ByteBuffer.allocate((int) (channel.size() - channel.position())).order(ByteOrder.LITTLE_ENDIAN);
        channel.read(content);
        plainContent = decrypt(content);
        plainContent.position(0);

        groups = new Group[header.getGroups()];
        for (int i = 0; i < groups.length; i++) {
            short fieldType = 0;
            GroupBuilder builder = new GroupBuilder();
            while ((fieldType = plainContent.getShort()) != -1) {
                if(fieldType == 0 ) {
                    continue;
                }
                int fieldSize = plainContent.getInt();
                byte[] fieldData = new byte[fieldSize];
                plainContent.get(fieldData);
                builder.addField(fieldType, ByteBuffer.wrap(fieldData).order(ByteOrder.LITTLE_ENDIAN));
            }
            groups[i] = builder.buildGroup();
        }
    }

    public Header getHeader() throws IOException {
        return header;
    }

    public Group[] getGroups() throws IOException {
        if (groups == null) {
            plainContent.position(0);
        }
        return groups;
    }

    public byte[] processECBCipher(KeyParameter key, byte[] data) throws InvalidCipherTextException {

        ecbCipher.init(true, key);

        byte[] toReturn = new byte[ecbCipher.getOutputSize(data.length)];

        int outputLen = ecbCipher.processBytes(data, 0, data.length, toReturn, 0);

        ecbCipher.doFinal(toReturn, outputLen);

        return toReturn;
    }

    public byte[] processCBCCipher(KeyParameter key, byte[] data, byte[] iv) throws InvalidCipherTextException {

        cbcCipher.init(false, new ParametersWithIV(key, iv));

        byte[] toReturn = new byte[cbcCipher.getOutputSize(data.length)];

        int outputLen = cbcCipher.processBytes(data, 0, data.length, toReturn, 0);

        cbcCipher.doFinal(toReturn, outputLen);

        return toReturn;
    }

    private ByteBuffer decrypt(ByteBuffer content) throws Exception {

        byte[] keyToTransform = masterKey;
        KeyParameter masterSeed2 = new KeyParameter(getHeader().getMasterSeed2());
        for (int i = 0; i < getHeader().getKeyEncRounds(); i++) {
            keyToTransform = processECBCipher(masterSeed2, keyToTransform);
        }

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] transformedKey = sha256.digest(keyToTransform);

        sha256.reset();
        sha256.update(getHeader().getMasterSeed());
        sha256.update(transformedKey);
        KeyParameter finalKey = new KeyParameter(sha256.digest());

        ByteBuffer toReturn = ByteBuffer.allocate(content.capacity()).order(ByteOrder.LITTLE_ENDIAN);
        toReturn.put(processCBCCipher(finalKey, content.array(), getHeader().getEncryptionIV()));

        sha256.reset();

        byte[] a = getHeader().getContentsHash();
        byte[] b = sha256.digest(toReturn.array());
//        Hex.encode(a, System.out);
//        System.out.println(printBytes(a));
//        Hex.encode(b, System.out);
//        System.out.println(printBytes(b));
        System.out.println(Arrays.hashCode(a) == Arrays.hashCode(b));

        return toReturn;
    }

    private static String printBytes(byte[] b) {
        StringBuilder sb = new StringBuilder(8 * b.length);
        for (int i = 0; i < b.length; i++) {
            sb.append(printByte(b[i]) + "|");
        }
        return sb.toString();
    }

    private static String printByte(byte b) {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(((b >> i) & 0x1) == 1 ? "1" : "0");
        }
        return sb.toString();
    }

    public ByteBuffer getPlainContent() {
        return plainContent;
    }

    public static void main(String[] args) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
        Database db = new Database("Database.kdb");
        System.out.println(new String(db.getPlainContent().array()));
    }
}
