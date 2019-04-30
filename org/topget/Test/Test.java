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
    }

}