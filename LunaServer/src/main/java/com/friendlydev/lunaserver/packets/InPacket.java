package com.friendlydev.lunaserver.packets;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

/**
 *
 * @author Marc
 */
public class InPacket {
    private short packetCode;
    private ByteArrayInputStream in;
    
    public InPacket(byte[] packetData) throws EOFException {
        in = new ByteArrayInputStream(packetData);
        
        // The packet starts with the (short) code
        packetCode = readShort();
    }
    
    public byte readByte() throws EOFException {
        int byte1 = in.read();
        
        if (byte1 < 0) {
            throw new EOFException();
        }
        return (byte) byte1;
    }
    
    // The numbers are read in little endian byte order
    public short readShort() throws EOFException {
        int byte1 = in.read();
        int byte2 = in.read();
        
        if ((byte1 | byte2) < 0) {
            throw new EOFException();
        }
        return (short) (byte1 + (byte2 << 8));
    }
    
    public int readInt() throws EOFException {
        int byte1 = in.read();
        int byte2 = in.read();
        int byte3 = in.read();
        int byte4 = in.read();
        
        if ((byte1 | byte2 | byte3 | byte4) < 0) {
            throw new EOFException();
        }
        return (byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24));
    }
    
    public long readLong() throws EOFException{
        int byte1 = in.read();
        int byte2 = in.read();
        int byte3 = in.read();
        int byte4 = in.read();
        
        int byte5 = in.read();
        int byte6 = in.read();
        int byte7 = in.read();
        int byte8 = in.read();
        
        if ((byte1 | byte2 | byte3 | byte4 | byte5 | byte6 | byte7 | byte8) < 0) {
            throw new EOFException();
        }
        return ((byte1 & 255) + ((byte2 & 255) << 8) + ((byte3 & 255) << 16) + ((long) (byte4 & 255) << 24) +
                ((long) (byte5 & 255) << 32) + ((long) (byte6 & 255) << 40) + ((long) (byte7 & 255) << 48) + ((long) byte8 << 56));
    }
    
    public boolean readBoolean() throws EOFException {
        int byte1 = in.read();
        
        if (byte1 < 0) {
            throw new EOFException();
        }
        return byte1 == 1;
    }
    
    public String readString() throws EOFException {
        int stringSize = readInt();
        byte[] stringBytes = new byte[stringSize];
        
        for (int i = 0; i < stringSize; i++) {
            int data = in.read();
            if (data < 0) {
                throw new EOFException();
            }
            stringBytes[i] = (byte) data;
        }
        return new String(stringBytes);
    }

    public short getPacketCode() {
        return packetCode;
    }
    
}
