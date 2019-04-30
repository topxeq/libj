import java.nio.charset.Charset;

import org.topget.TXGT.*;

public class Test {

    public static char getCharCode(String strA) {
        return strA.charAt(0);
    }

    public static void main(String[] args) {

        TXGT.Pl("This is a test.");
        String s = "水电开发计划abc";

        TXGT.Pl("s: %s", TXGT.getBufStringHex(TXGT.getUTF8Bytes(s)));

        char c = getCharCode(s);

        int a = c;

        TXGT.Pl("Code: %d, %c", a, c);



        TXGT.Pl("%d", TXGT.byteAddU(120, 160));
        TXGT.Pl("%d", TXGT.byteMinusU(120, 124));

        TXGT.Pl("%s", new TXGT.UByte(TXGT.byteMinusU(120, 124)));

        TXGT.Pl("%s", "abcABZ%&*&我们大家都很好。");

        String st = TXGT.ulencode("abcABZ%&*&我们大家都很好。");
        TXGT.Pl("%s", st);
        TXGT.Pl("%s", TXGT.uldecode(st));

        st = TXGT.EncodeStringSimple("abcABZ%&*&我们大家都很好。");
        TXGT.Pl("%s", st);
        TXGT.Pl("%s", TXGT.DecodeStringSimple(st));

        st = TXGT.EncryptStringByTXTE("abcABZ%&*&我们大家都很好。", null);
        TXGT.Pl("(%d)%s", st.length(), st);
        TXGT.Pl("Detxte: %s", TXGT.DecryptStringByTXTE(st, null));

        byte[] sBufT = TXGT.getUTF8Bytes("abcABZ%&*&我们大家都很好。");

        TXGT.Pl("test buf(%s): %s", sBufT.length, TXGT.getBufStringDec(sBufT, ","));

        TXGT.Pl("h: %s", TXGT.byteToHex(10));
        TXGT.Pl("h: %s", TXGT.byteToHex(-10));
        TXGT.Pl("h: %s", TXGT.byteToHex(246));

        byte b1 = -10;
        int c1 = -10;
        int c2 = 246;
        int c3 = b1;

        TXGT.Pl("%02X", b1);
        TXGT.Pl("%02X", c1);
        TXGT.Pl("%02X", c2);
        TXGT.Pl("%02X", c3);

    }

}