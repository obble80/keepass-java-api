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
import java.security.NoSuchAlgorithmException;

/**
 * $Id: $
 */
public class Database {

    private Header header;
    private Group[] groups;
    private Entry[] entries;
    private ByteBuffer content;

    BlockCipher aesEngine = new AESEngine();
    private byte[] masterKey = Hex.decode("83b62ec2690df02ce7b2f94208469decd93fb0d2febbc2408c86ae7860f5d6af");

    public Database(String file) throws Exception {
        this(new File(file).getCanonicalFile());
    }

    public Database(File file) throws Exception {
        FileChannel channel = new FileInputStream(file).getChannel();

        ByteBuffer bb = ByteBuffer.allocate(Header.LENGTH);
        channel.read(bb);
        header = new Header(bb);

        content = ByteBuffer.allocate((int) (channel.size() - channel.position())).order(ByteOrder.LITTLE_ENDIAN);
        channel.read(content);

        // decrypting content
        decrypt(content);

        content.rewind();

        groups = new Group[header.getGroups()];
        for (int i = 0; i < groups.length; i++) {
            short fieldType;
            GroupBuilder builder = new GroupBuilder();
            while ((fieldType = content.getShort()) != -1) {
                if (fieldType == 0) {
                    continue;
                }
                int fieldSize = content.getInt();
                builder.readField(fieldType, fieldSize, content);
            }
            groups[i] = builder.buildGroup();
        }

        entries = new Entry[header.getEntries()];
        for (int i = 0; i < entries.length; i++) {
            short fieldType;
            EntryBuilder builder = new EntryBuilder();
            while ((fieldType = content.getShort()) != -1) {
                if (fieldType == 0) {
                    continue;
                }
                int fieldSize = content.getInt();
                builder.readField(fieldType, fieldSize, content);
            }
            entries[i] = builder.buildEntry();
        }
    }

    public Header getHeader() throws IOException {
        return header;
    }

    public Group[] getGroups() throws IOException {
        return groups;
    }

    public Entry[] getEntries() {
        return entries;
    }

    private void decrypt(ByteBuffer content) throws Exception {

        byte[] transformedKey = transformKey(masterKey);

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        sha256.update(getHeader().getMasterSeed());
        sha256.update(transformedKey);
        byte[] finalKey = sha256.digest();

        // decrypt main data
        performAESDecrypt(finalKey, content);

        sha256.reset();

        byte[] a = getHeader().getContentsHash();
        byte[] b = sha256.digest(content.array());
//        Hex.encode(a, System.out);
//        System.out.println(printBytes(a));
//        Hex.encode(b, System.out);
//        System.out.println(printBytes(b));
        System.out.println(Arrays.hashCode(a) == Arrays.hashCode(b));
    }

    private void performAESDecrypt(byte[] key, ByteBuffer content) throws IOException, InvalidCipherTextException {
        KeyParameter keyParameter = new KeyParameter(key);

        BufferedBlockCipher cbcCipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(this.aesEngine), new ZeroBytePadding());
        cbcCipher.init(false, new ParametersWithIV(keyParameter, this.header.getEncryptionIV()));

        byte[] result = new byte[content.capacity()];
        int outputLen = cbcCipher.processBytes(content.array(), 0, content.capacity(), result, 0);
        cbcCipher.doFinal(result, outputLen);

        System.arraycopy(result, 0, content.array(), 0, content.capacity());
        content.rewind();
    }

    private byte[] transformKey(byte[] keyToTransform) throws IOException, InvalidCipherTextException, NoSuchAlgorithmException {
        KeyParameter masterSeed2 = new KeyParameter(this.header.getMasterSeed2());

        BufferedBlockCipher ecbCipher = new BufferedBlockCipher(this.aesEngine);
        ecbCipher.init(true, masterSeed2);

        for (int i = 0; i < getHeader().getKeyEncRounds(); i++) {
            byte[] result = new byte[keyToTransform.length];
            int outputLen = ecbCipher.processBytes(keyToTransform, 0, keyToTransform.length, result, 0);
            ecbCipher.doFinal(result, outputLen);
            System.arraycopy(result, 0, keyToTransform, 0, keyToTransform.length);
        }

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        return sha256.digest(keyToTransform);
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

    public ByteBuffer getContent() {
        return content;
    }

    public static void main(String[] args) throws Exception {
        Database db = new Database("Database.kdb");
        System.out.println(new String(db.getContent().array()));
    }
}
