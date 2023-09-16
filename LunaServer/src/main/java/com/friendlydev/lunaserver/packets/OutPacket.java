package com.friendlydev.lunaserver.packets;

import java.awt.Point;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author Marc
 */
public class OutPacket {
    private short packetCode;
    private ByteArrayOutputStream out;
    
    public OutPacket(short packetCode) {
        this.packetCode = packetCode;
        out = new ByteArrayOutputStream();
        
        // Start the packet with the (short) code
        writeShort(packetCode);
    }
    
    public void writeByte(byte b) {
        out.write(b);
    }
    
    // The numbers are written in little endian byte order
    public void writeShort(short s) {
        out.write(s & 0xFF);
        out.write((s >> 8) & 0xFF);
    }
    
    public void writeInt(int i) {
        out.write(i & 0xFF);
        out.write((i >> 8) & 0xFF);
        out.write((i >> 16) & 0xFF);
        out.write((i >> 24) & 0xFF);
    }
    
    public void writeLong(long l) {
        out.write((byte) l & 0xFF);
        out.write((byte) (l >> 8) & 0xFF);
        out.write((byte) (l >> 16) & 0xFF);
        out.write((byte) (l >> 24) & 0xFF);
        
        out.write((byte) (l >> 32) & 0xFF);
        out.write((byte) (l >> 40) & 0xFF);
        out.write((byte) (l >> 48) & 0xFF);
        out.write((byte) (l >> 56) & 0xFF);
    }
    
    public void writeBoolean(boolean b) {
        writeByte((byte) (b ? 1 : 0));
    }
    
    /**
     * Writes a String with the byte size in front
     * @param s String that should be written to the packet
     */
    public void writeString(String s) {
        byte[] stringBytes = s.getBytes();
        
        writeInt(stringBytes.length);
        out.writeBytes(stringBytes);
    }
    
    public void writePoint(Point p) {
        writeInt(p.x);
        writeInt(p.y);
    }

    public short getPacketCode() {
        return packetCode;
    }
    
    /**
     * Creates and returns a byte array with the packet data. The packet starts with an int indicating the packet size
     * @return A byte array with the packet data
     */
    public byte[] getPacketData() {
        byte[] content = out.toByteArray();
        int packetSize = content.length;
        ByteArrayOutputStream packetData = new ByteArrayOutputStream();
        
        packetData.write(packetSize & 0xFF);
        packetData.write((packetSize >> 8) & 0xFF);
        packetData.write((packetSize >> 16) & 0xFF);
        packetData.write((packetSize >> 24) & 0xFF);
        
        packetData.writeBytes(content);
        
        return packetData.toByteArray();
    }
    
}
