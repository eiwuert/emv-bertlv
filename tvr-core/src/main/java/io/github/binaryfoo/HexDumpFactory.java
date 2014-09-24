package io.github.binaryfoo;

import io.github.binaryfoo.hex.ByteElement;
import io.github.binaryfoo.hex.HexDumpElement;
import io.github.binaryfoo.hex.WhitespaceElement;

import java.util.ArrayList;
import java.util.List;

public class HexDumpFactory {
    public List<HexDumpElement> splitIntoByteLengthStrings(String hexString, int startIndexInBytes) {
        List<HexDumpElement> elements = new ArrayList<HexDumpElement>();
        int byteOffset = startIndexInBytes;
        for (int i = 0; i < hexString.length(); ) {
            if (hexString.charAt(i) == ' ') {
                elements.add(new WhitespaceElement("<br>"));
                i++;
            } else {
                elements.add(new ByteElement(hexString.substring(i, i + 2), byteOffset++));
                i += 2;
            }
        }
        return elements;
    }
}