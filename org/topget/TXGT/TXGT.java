package org.topget.TXGT;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.json.JSONObject;

public class TXGT {

    public static int modX(int numA, int modA) {
        int numT = numA;

        if (numT == 0) {
            return 0;
        }

        if (numT > 0) {
            return (numT % modA);
        }

        for (; numT < 0;) {
            numT += modA;
        }

        return numT;
    }

    public static int ensurePositive(int numA, int modA) {
        int numT = numA;
        for (; numT < 0;) {
            numT += modA;
        }

        return numT;
    }

    public static class UByte {
        public int valueM;

        public UByte() {
            valueM = 0;
        }

        public UByte(int c) {
            valueM = modX(c, 256);
        }

        public UByte(byte b) {
            valueM = b & 0xFF;
        }

        public String toString() {
            return String.format("%d", valueM);
        }

        public UByte addAndNew(UByte ub) {
            return new UByte(valueM + ub.valueM);
        }

        public UByte minusAndNew(UByte ub) {
            return new UByte(valueM - ub.valueM);
        }

        public void add(UByte ub) {
            valueM = modX(valueM + ub.valueM, 256);
        }

        public void add(int c) {
            valueM = modX(valueM + c, 256);
        }

        public void add(byte b) {
            valueM = modX(valueM + (b & 0xFF), 256);
        }

        public void minus(UByte ub) {
            valueM = modX(valueM - ub.valueM, 256);
        }

        public void minus(int c) {
            valueM = modX(valueM - c, 256);
        }

        public void minus(byte b) {
            valueM = modX(valueM - (b & 0xFF), 256);
        }

    }

    public static byte byteAddU(byte a, byte b) {
        int aT = a & 0xFF;
        int bT = b & 0xFF;

        return (byte) (modX(aT + bT, 256));
    }

    public static byte byteAddU(int a, int b) {
        return (byte) (modX(a + b, 256));
    }

    public static byte byteAddUV(byte... bs) {
        int tmpc = 0;

        for (byte b : bs) {
            tmpc += (b & 0xFF);
        }

        return (byte) (modX(tmpc, 256));
    }

    public static byte byteAddUV(int... cs) {
        int tmpc = 0;

        for (int c : cs) {
            tmpc += c;
        }

        return (byte) (modX(tmpc, 256));
    }

    public static byte byteMinusU(byte a, byte b) {
        int aT = a & 0xFF;
        int bT = b & 0xFF;

        return (byte) (modX(aT - bT, 256));
    }

    public static byte byteMinusU(int a, int b) {
        return (byte) (modX(a - b, 256));
    }

    public static byte byteMinusUV(byte... bs) {
        int tmpc = 0;

        for (byte b : bs) {
            tmpc -= (b & 0xFF);
        }

        return (byte) (modX(tmpc, 256));
    }

    public static byte byteMinusUV(int... cs) {
        int tmpc = 0;

        for (int c : cs) {
            tmpc -= c;
        }

        return (byte) (modX(tmpc, 256));
    }

    public static DecimalFormat decimalFormat = new DecimalFormat("###.00");

    public static String formatDouble(double f) {
        return decimalFormat.format(f);
    }

    public static String formatDoubleCompact(double f) {
        return String.format("%.2f", f);
    }

    public static String formatInt(int c, int digits) {
        int digitt = digits;
        if (digitt < 0) {
            digitt = 1;
        }

        StringBuilder sb = new StringBuilder(digitt + 1);
        for (int i = 0; i < digitt; i++) {
            sb.append("0");
        }

        return (new DecimalFormat(sb.toString())).format(c);
    }

    public static String getStackTrace(Throwable e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e2) {
            return "";
        }
    }

    public static byte[] hex2bytes(String strA) {
        if (strA == null) {
            return null;
        }

        String strT = strA.trim();

        int dataLen = strT.length() / 2;

        byte[] buffer = new byte[dataLen];

        for (int i = 0; i < dataLen; i++) {
            buffer[i] = TXGT.hex2Byte(strT.substring(i * 2, i * 2 + 2));
        }

        return buffer;

    }

    public static byte hex2Byte(String hexStrA) {
        return (byte) (Integer.decode("0x" + hexStrA).intValue());
    }

    public static String bytes2Hex(byte[] b, int lenA) {
        String stmp;
        StringBuilder sb = new StringBuilder("");
        if (lenA < 0) {
            lenA = b.length;
        }

        for (int n = 0; n < lenA; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }

        return sb.toString().toUpperCase().trim();
    }

    public static byte[] copyBufferSection(byte[] bufA, int idxA, int lengthA) {
        byte[] rt = new byte[lengthA];
        System.arraycopy(bufA, idxA, rt, 0, lengthA);

        return rt;
    }

    public static String intToHex(int c) {
        return Integer.toHexString(c).toUpperCase();
    }

    public static String shortToHexEnsureDigits(int c) {
        String tmps = Integer.toHexString(c).toUpperCase();
        if (tmps.length() < 1) {
            return "0000";
        } else if (tmps.length() == 1) {
            return "000" + tmps;
        } else if (tmps.length() == 2) {
            return "00" + tmps;
        } else if (tmps.length() == 3) {
            return "0" + tmps;
        } else {
            return tmps;
        }
    }

    public static String getBufString(byte[] bufA, int bytesA) {
        StringBuilder sb = new StringBuilder(bytesA + 1);
        for (int i = 0; i < bytesA; i++) {
            sb.append(bufA[i]);
        }

        return sb.toString();
    }

    public static String getBufStringHex(byte[] bufA, int bytesA) {
        StringBuilder sb = new StringBuilder(bytesA + 1);
        for (int i = 0; i < bytesA; i++) {
            sb.append(Integer.toHexString(bufA[i] & 0xFF).toUpperCase() + " ");
        }

        return sb.toString();
    }

    public static String getBufStringHex(byte[] bufA) {
        return getBufStringHex(bufA, "");
    }

    public static String getBufStringHex(byte[] bufA, String sepA) {
        if (bufA == null) {
            return "";
        }
        String sepT = sepA;
        if (sepT == null) {
            sepT = "";
        }

        StringBuilder sb = new StringBuilder(bufA.length + 1);
        for (int i = 0; i < bufA.length; i++) {
            if (i != 0) {
                sb.append(sepT);
            }

            sb.append(Integer.toHexString(bufA[i] & 0xFF).toUpperCase());
        }

        return sb.toString();
    }

    public static String getBufStringHexWithIndex(byte[] bufA, int bytesA) {
        StringBuilder sb = new StringBuilder(bytesA + 1);
        for (int i = 0; i < bytesA; i++) {
            sb.append("" + i + ":" + Integer.toHexString(bufA[i] & 0xFF).toUpperCase() + " ");
        }

        return sb.toString();
    }

    public static String getIntBufStringHex(int[] bufA, int countA) {
        StringBuilder sb = new StringBuilder(countA + 1);
        for (int i = 0; i < countA; i++) {
            sb.append(TXGT.shortToHexEnsureDigits(bufA[i]));
        }

        return sb.toString();
    }

    public static String getIntBufStringHexWithIndex(int[] bufA, int countA) {
        StringBuilder sb = new StringBuilder(countA + 1);
        for (int i = 0; i < countA; i++) {
            sb.append("" + i + ":" + Integer.toHexString(bufA[i]).toUpperCase() + " ");
        }

        return sb.toString();
    }

    public static boolean isNullOrEmptyString(String strA) {
        if (strA == null) {
            return true;
        }

        if (strA.isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean isNullOrEmptyStringTrim(String strA) {
        if (strA == null) {
            return true;
        }

        if (strA.trim().isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean strToBool(String strA, boolean defaultA) {
        if (strA == null) {
            return defaultA;
        }

        if (strA.equalsIgnoreCase("true") || strA.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }
    }

    public static String boolToStr(boolean valueA) {
        if (valueA) {
            return "true";
        } else {
            return "false";
        }
    }

    public static String[] loadListFromStringRemoveEmpty(String strA) {
        if (strA == null) {
            return null;
        }

        String[] tal = strA.replaceAll("\\r", "").split("\\n");

        ArrayList<String> al = new ArrayList<String>();

        for (int i = 0; i < tal.length; i++) {
            if (!TXGT.isNullOrEmptyStringTrim(tal[i])) {
                al.add(tal[i]);
            }
        }

        String[] newArray = new String[al.size()];
        al.toArray(newArray);
        return newArray;
    }

    public static String RegReplace(String strA, String patternA, String replaceA) {
        return strA.replaceAll(patternA, replaceA);
    }

    public static byte[] getUTF8Bytes(String strA) {
        try {
            return strA.getBytes("UTF-8");
        } catch (Exception e) {
            return strA.getBytes();
        }
    }

    public static String md5Encrypt(String dataStr) {
        try {

            MessageDigest m = MessageDigest.getInstance("MD5");

            m.update(dataStr.getBytes("UTF8"));

            byte s[] = m.digest();

            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }

            return result;
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return "";
    }

    public static boolean isHex(byte c) {
        if (('0' <= c) && (c <= '9')) {
            return true;
        }

        if (('a' <= c) && (c <= 'f')) {
            return true;
        }

        if (('A' <= c) && (c <= 'F')) {
            return true;
        }

        return false;
    }

    public static byte unhex(byte c) {
        if (('0' <= c) && (c <= '9')) {
            return (byte) (c - '0');
        }

        if (('a' <= c) && (c <= 'f')) {
            return (byte) (c - 'a' + 10);
        }

        if (('A' <= c) && (c <= 'F')) {
            return (byte) (c - 'A' + 10);
        }

        return 0;
    }

    public static String ulencode(String strA) {
        if (strA == null) {
            return null;
        }

        byte[] bufT = getUTF8Bytes(strA);

        int lenT = bufT.length;

        StringBuilder sbuf = new StringBuilder(lenT);

        String tableStrT = "0123456789ABCDEF";

        byte v;

        for (int i = 0; i < lenT; i++) {
            v = bufT[i];

            if (!(((v >= '0') && (v <= '9')) || ((v >= 'a') && (v <= 'z')) || ((v >= 'A') && (v <= 'Z')))) {
                sbuf.append('_');
                sbuf.append(tableStrT.charAt((v & 0xFF) >> 4));
                sbuf.append(tableStrT.charAt((v & 0xFF) & 15));
            } else {
                sbuf.append((char) (v));
            }
        }

        return sbuf.toString();

    }

    public static String ByteArrayOutputStreamToUTF8String(ByteArrayOutputStream streamA) {
        try {
            return streamA.toString("utf-8");
        } catch (Exception e) {
            return streamA.toString();
        }
    }

    public static String uldecode(String strA) {

        byte[] strBufT = getUTF8Bytes(strA);

        int lenT = strBufT.length;

        ByteArrayOutputStream bufT = new ByteArrayOutputStream(lenT);

        int c;

        for (int i = 0; i < lenT;) {
            if (strBufT[i] == '_') {
                if (i + 2 >= lenT) {
                    return strA;
                }

                c = ((unhex(strBufT[i + 1]) & 0xFF) << 4) | (unhex(strBufT[i + 2]) & 0xFF);

                bufT.write(c);

                i += 3;
            } else {
                bufT.write((strBufT[i]));
                i++;
            }
        }

        return ByteArrayOutputStreamToUTF8String(bufT);

    }

    public static String getCurrentDateTime(String formatA) {
        if (formatA == null) {
            return (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()))
                    .format(new Date(System.currentTimeMillis()));
        } else {
            return (new SimpleDateFormat(formatA, Locale.getDefault())).format(new Date(System.currentTimeMillis()));
        }
    }

    public static String getCompactStringFromTimeTick(long timeTickA) {
        return (new SimpleDateFormat("HH:mm", Locale.getDefault())).format(new Date(timeTickA));
    }

    public static String getCompactStringFromTimeInt(int timeA) {
        if (timeA < 0) {
            return "00:00";
        }

        return String.format("%02d:%02d", timeA / 60, timeA % 60);
    }

    public static int getTimeIntFromCompactString(String strA) {
        if (TXGT.isNullOrEmptyStringTrim(strA)) {
            return 0;
        }

        String[] aryt = strA.split(":");
        if (aryt.length < 2) {
            return 0;
        }

        return TXGT.strToInt(aryt[0], 0) * 60 + TXGT.strToInt(aryt[1], 0);
    }

    public static String getCurrentDate(String formatA) {
        if (formatA == null) {
            return (new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()))
                    .format(new Date(System.currentTimeMillis()));
        } else {
            return (new SimpleDateFormat(formatA, Locale.getDefault())).format(new Date(System.currentTimeMillis()));
        }
    }

    public static int strToInt(String strA, int defaultA) {
        int c;
        try {
            c = Integer.parseInt(strA);
            return c;
        } catch (Exception e) {
            return defaultA;
        }
    }

    public static float strToFloat(String strA, float defaultA) {
        float c;
        try {
            c = Float.parseFloat(strA);
            return c;
        } catch (Exception e) {
            return defaultA;
        }
    }

    public static double strToDouble(String strA, double defaultA) {
        double c;
        try {
            c = Double.parseDouble(strA);
            return c;
        } catch (Exception e) {
            return defaultA;
        }
    }

    public static String intStrToValidStr(String strA, String defaultA) {
        int c;
        try {
            c = Integer.parseInt(strA);
            return Integer.toString(c);
        } catch (Exception e) {
            return defaultA;
        }
    }

    public static boolean isValidIntStr(String strA) {
        try {
            Integer.parseInt(strA);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getSafeString(String strA, String defaultA) {
        if (strA == null) {
            return defaultA;
        }

        return strA;
    }

    public static String returnEmptyIfStrBelowZero(String strA) {
        if (strA == null) {
            return "";
        }

        double d = TXGT.strToDouble(strA, -1);
        if (d < 0) {
            return "";
        } else {
            return strA;
        }
    }

    public static String getCurrentTimeOnly(String formatA) {
        if (formatA == null) {
            return (new SimpleDateFormat("HH:mm:ss", Locale.getDefault())).format(new Date(System.currentTimeMillis()));
        } else {
            return (new SimpleDateFormat(formatA, Locale.getDefault())).format(new Date(System.currentTimeMillis()));
        }
    }

    public static void sleep(long msecsA) {
        try {
            Thread.sleep(msecsA);

        } catch (Exception e) {

        }
    }

    public static String addPrefix(String strA, int totalLenA, String prefixA) {
        if (strA == null) {
            return null;
        }

        int addlen = totalLenA - strA.length();
        if (addlen < 1) {
            return strA;
        }

        StringBuilder sb = new StringBuilder(totalLenA + 1);
        for (int i = 0; i < addlen; i++) {
            sb.append(prefixA);
        }
        sb.append(strA);

        return sb.toString();
    }

    public static final Random mRandomGen = new Random(System.currentTimeMillis());

    public static double getRandomDouble() {
        synchronized (mRandomGen) {
            return mRandomGen.nextDouble();
        }
    }

    public static int getRandomInt() {
        synchronized (mRandomGen) {
            return mRandomGen.nextInt();
        }
    }

    // [0, rangeA)
    public static int getRandomInt(int rangeA) {
        synchronized (mRandomGen) {
            return mRandomGen.nextInt(rangeA);
        }
    }

    public static int absInt(int a) {
        if (a < 0) {
            return -a;
        } else {
            return a;
        }
    }

    public static int getRandomIntRange(int minA, int maxA) {
        synchronized (mRandomGen) {
            return mRandomGen.nextInt(maxA - minA + 1) + minA;
        }
    }

    public static long getMaxLong(long a, long b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    public static double getMaxDouble(double a, double b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    public static byte[] intToByteArray(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    public static void setShortToByteArrayHL(int resA, byte[] bufA, int idxA) {
        bufA[idxA] = (byte) ((resA >> 8) & 0xff);// 次低位
        bufA[idxA + 1] = (byte) (resA & 0xff);// 最低位
    }

    public static int byteArrayToShort(byte[] res) {
        // res = InversionByte(res);
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
        return targets;
    }

    public static int byteArrayToShortHL(byte[] res) {
        // res = InversionByte(res);
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[1] & 0xff) | ((res[0] << 8) & 0xff00); // | 表示安位或
        return targets;
    }

    public static int byteArrayToShortHL(byte[] res, int idxA) {
        // res = InversionByte(res);
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[1 + idxA] & 0xff) | ((res[idxA] << 8) & 0xff00); // | 表示安位或
        return targets;
    }

    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public static int getMaxIndexInIntArray(int[] aryA, int startIdxA, int endIdxA) {
        int max = -1;
        for (int i = startIdxA; i <= endIdxA; i++) {
            if (aryA[i] > max) {
                max = aryA[i];
            }
        }

        return max;
    }

    public static void Fatalf(String formatA, String errStrA) {
        if (IsErrorString(errStrA)) {
            Pl(formatA, GetErrorString(errStrA));

            System.exit(1);
        }
    }

    public static void Fatal(String errStrA) {
        if (IsErrorString(errStrA)) {
            Pl("Error: %s", GetErrorString(errStrA));

            System.exit(1);
        }
    }

    public static void Fatal(List listA) {
        String failStrT = GetFailResult(listA);

        if (failStrT != null) {
            Pl("Error: %s", failStrT);

            System.exit(1);
        }
    }

    public static String LoadStringFromFile(String fileNameA) {
        try {
            StringBuffer sb = new StringBuffer();

            InputStream is = new FileInputStream(fileNameA);

            String line;

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");

                line = reader.readLine();
            }

            reader.close();
            is.close();

            return sb.toString();
        } catch (Exception e) {
            return GenerateErrorString(e.getLocalizedMessage());
        }
    }

    public static byte[] readStreamData(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static byte[] downloadWebData(String path, int timeoutSecs) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int timeout = timeoutSecs;
            if (timeout < 0) {
                timeout = 30;
            }
            conn.setConnectTimeout(timeout * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return TXGT.readStreamData(inStream);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String downloadWebString(String path, int timeoutSecs) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int timeout = timeoutSecs;
            if (timeout < 0) {
                timeout = 30;
            }
            conn.setConnectTimeout(timeout * 1000);
            conn.setUseCaches(false); // 不使用缓冲
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                byte[] buf = TXGT.readStreamData(inStream);
                if (buf == null) {
                    return null;
                }
                return new String(buf);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String GenerateErrorString(String errStrA) {
        return "TXERROR:" + errStrA;
    }

    public static String GenerateErrorStringF(String formatA, Object... argsA) {
        return String.format("TXERROR:" + formatA, argsA);
    }

    public static boolean IsErrorString(String errStrA) {
        return errStrA.startsWith("TXERROR:");
    }

    public static String GetErrorString(String errStrA) {
        return errStrA.substring(8);
    }

    public static String downloadWebPage(String urlString, int timeoutSecs) {
        HttpURLConnection conn = null; // 连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL(urlString); // URL对象
            conn = (HttpURLConnection) url.openConnection(); // 使用URL打开一个链接
            // conn.setDoInput(true); //允许输入流，即允许下载
            // conn.setDoOutput(true); //允许输出流，即允许上传
            int timeout = timeoutSecs;
            if (timeout < 0) {
                timeout = 30;
            }
            conn.setConnectTimeout(timeout * 1000);
            conn.setUseCaches(false); // 不使用缓冲
            conn.setRequestMethod("GET"); // 使用get请求
            is = conn.getInputStream(); // 获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return resultData;
    }

    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null; // 存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    public static String downloadWebPageByPost(String strUrlPath, Map<String, String> params, String encodeA) {

        String encodeT;
        if (TXGT.isNullOrEmptyStringTrim(encodeA)) {
            encodeT = "utf-8";
        } else {
            encodeT = encodeA.trim();
        }

        byte[] data = getRequestData(params, encodeT).toString().getBytes();// 获得请求体
        try {

            URL url = new URL(strUrlPath);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000); // 设置连接超时时间
            httpURLConnection.setDoInput(true); // 打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true); // 打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST"); // 设置以Post方式提交数据
            httpURLConnection.setUseCaches(false); // 使用Post方式不能使用缓存
            // 设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 获得输出流，向服务器写入数据
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode(); // 获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream); // 处理服务器的响应结果
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return "Exception: " + e.getMessage();
            // return null;
        }

        return null;
    }

    public static ArrayList GenerateList(Object... argsA) {
        ArrayList<Object> listT = new ArrayList<Object>();

        for (Object v : argsA) {
            listT.add(v);
        }

        return listT;
    }

    public static String GetSwitchWithDefaultValue(String[] argsA, String switchStrA, String defaultA) {
        String tmpStrT = "";

        for (String v : argsA) {
            if (v.startsWith(switchStrA)) {
                tmpStrT = v.substring(switchStrA.length());

                if (tmpStrT.startsWith("\"") && tmpStrT.endsWith("\"")) {
                    return tmpStrT.substring(1, tmpStrT.length() - 1);
                }

                return tmpStrT;
            }

        }

        return defaultA;

    }

    public static boolean IfSwitchExists(String[] argsA, String switchStrA) {
        for (String v : argsA) {
            if (v.startsWith(switchStrA)) {
                return true;
            }

        }

        return false;
    }

    public static String GetParameterByIndexWithDefaultValue(String[] argsA, int idxA, String defaultA) {
        if (idxA == -1) {
            idxA = 1;
        }

        if ((idxA >= argsA.length) || (idxA < 0)) {
            return defaultA;
        }

        int cnt = 0;

        for (String argT : argsA) {
            if (argT.startsWith("-")) {
                continue;
            }

            if (cnt == idxA) {
                if (argT.startsWith("\"") && argT.startsWith("\"")) {
                    return argT.substring(1, argT.length() - 1);
                }

                return argT;
            }

            cnt++;

        }

        return defaultA;
    }

    public static boolean IsSuccessResult(List listA) {
        if (listA == null) {
            return false;
        }

        if (listA.size() < 2) {
            return false;
        }

        String statusT = (String) (listA.get(0));

        if ((statusT == null) || (statusT.equals(""))) {
            return true;
        }

        return false;
    }

    // return null if not fail
    public static String GetFailResult(List listA) {
        if (listA == null) {
            return "result null";
        }

        if (listA.size() < 2) {
            return "invalid result length";
        }

        String statusT = (String) (listA.get(0));

        if ((statusT == null) || (statusT.equals(""))) {
            return null;
        }

        return (String) (listA.get(0));
    }

    public static List PostRequestBytesX(String urlA, byte[] postDataA, int timeoutSecsA) {
        try {
            URL url = new URL(urlA);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(timeoutSecsA * 1000);

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);

            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setUseCaches(false);

            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(postDataA.length));

            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postDataA);

            int responseT = httpURLConnection.getResponseCode();

            if (responseT == HttpURLConnection.HTTP_OK) {
                InputStream inputStreamT = httpURLConnection.getInputStream();

                ByteArrayOutputStream byteArrayOutputStreamT = new ByteArrayOutputStream();

                byte[] bufT = new byte[1024];
                int lenT = 0;

                while ((lenT = inputStreamT.read(bufT)) != -1) {
                    byteArrayOutputStreamT.write(bufT, 0, lenT);
                }

                return GenerateList("", byteArrayOutputStreamT.toByteArray());
            }

            return GenerateList(String.format("failed to post, status code: %d", responseT), null);
        } catch (Exception e) {
            return GenerateList(e.getLocalizedMessage(), null);
        }
    }

    public static String ReplaceLineEnds(String strA, String replacementA) {
        String tmps = strA.replace("\r", "");
        tmps = strA.replace("\n", replacementA);
        return tmps;
    }

    public static String RestoreLineEnds(String strA, String replacementA) {
        String tmps = strA.replace(replacementA, "\n");
        return tmps;
    }

    public static String simpleMapToString(HashMap<String, String> mapA) {
        if (mapA == null) {
            return null;
        }

        String kk, vv;

        StringBuilder sb = new StringBuilder(1000);
        for (Map.Entry<String, String> entry : mapA.entrySet()) {
            kk = entry.getKey().replace("=", "`EQ`");
            vv = ReplaceLineEnds(entry.getValue(), "#CR#");

            sb.append(kk + "=" + vv + "\n");
        }

        return sb.toString().trim();
    }

    public static HashMap<String, String> getSimpleMapFromString(String strA) {
        if (strA == null) {
            return null;
        }

        HashMap<String, String> mapt = new HashMap<String, String>();

        String[] aryt = strA.split("\n");
        String[] tmplineary;

        for (String v : aryt) {
            tmplineary = v.split("=", 2);
            if (tmplineary.length >= 2) {
                mapt.put(tmplineary[0].replace("`EQ`", "="), RestoreLineEnds(tmplineary[1], "#CR#"));
            }
        }

        return mapt;
    }

    public static String generateTXResultString(String statusA, String valueA) {
        JSONObject mapT = new JSONObject();

        mapT.put("Status", statusA);
        mapT.put("Value", valueA);

        return mapT.toString();

    }

    public static String getTXResultCompactString(String resultA, String defaultA) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(resultA);
        } catch (Exception e) {
            jsonObject = null;
        }

        if (jsonObject == null) {
            return defaultA;
        }

        String statusStr;
        try {
            statusStr = jsonObject.getString("Status");
        } catch (Exception e) {
            statusStr = null;
        }

        if (statusStr == null) {
            return defaultA;
        }

        String valueStr;
        try {
            valueStr = jsonObject.getString("Value");
        } catch (Exception e) {
            valueStr = null;
        }

        if (valueStr == null) {
            return defaultA;
        }

        return statusStr + " (" + valueStr + ")";
    }

    public static String getTXResultMessage(String resultA, String defaultA) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(resultA);
        } catch (Exception e) {
            jsonObject = null;
        }

        if (jsonObject == null) {
            return defaultA;
        }

        String valueStr;
        try {
            valueStr = jsonObject.getString("Value");
        } catch (Exception e) {
            valueStr = null;
        }

        if (valueStr == null) {
            return defaultA;
        }

        return valueStr;
    }

    public static String getTXResultObject(String resultA, String defaultA) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(resultA);
        } catch (Exception e) {
            jsonObject = null;
        }

        if (jsonObject == null) {
            return defaultA;
        }

        String valueStr;
        try {
            valueStr = jsonObject.getString("Object");
        } catch (Exception e) {
            valueStr = null;
        }

        if (valueStr == null) {
            return defaultA;
        }

        return valueStr;
    }

    public static String getTXResultFailMessage(String resultA) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(resultA);
        } catch (Exception e) {
            jsonObject = null;
        }

        if (jsonObject == null) {
            return "invalid data structure: " + resultA;
        }

        String statusStr;
        try {
            statusStr = jsonObject.getString("Status");
            if (statusStr.equalsIgnoreCase("success")) {
                return null;
            }
        } catch (Exception e) {
            statusStr = null;
        }

        if (statusStr == null) {
            return "invalid status";
        }

        String valueStr;
        try {
            valueStr = jsonObject.getString("Value");
        } catch (Exception e) {
            valueStr = null;
        }

        if (valueStr == null) {
            return "invalid value";
        }

        return valueStr;
    }

    public static boolean getTXResultStatus(String resultA) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(resultA);
            String statusStr = jsonObject.getString("Status");
            if (statusStr.equalsIgnoreCase("success")) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    public static String Spr(String formatA, Object... argsA) {
        return String.format(formatA, argsA);
    }

    public static void Pr(String formatA, Object... argsA) {
        System.out.printf(formatA, argsA);
    }

    public static void Pl(String formatA, Object... argsA) {
        System.out.printf(formatA + "\n", argsA);
    }

    public static void PlException(String formatA, Exception e) {
        System.out.printf(formatA + "\n", e.getLocalizedMessage());
    }

    public static void main(String[] args) throws Exception {
        System.out.println(args);
    }

}
