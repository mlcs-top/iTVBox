package com.github.tvbox.osc.util;


public class BaseR {

    public static String decry_RC4(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(HexString2Bytes(data), key));
    }

    public static String decry_R(String response) {
        if (response.contains("code") && response.contains("msg")){
            return response;
        }
        String[] apiKeys = HawkConfig.MMM_MMM.split("//");
        return BaseR.decry_RC4(response, apiKeys[1]);
    }

    /*
     *解析多线路数据
     * */
    public static String decry_R2(String response) {
        if (response.contains("storeHouse")){
            return response;
        }
        String[] apiKeys = HawkConfig.MMM_MMM.split("//");
        return BaseR.decry_RC4(response, apiKeys[1]);
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte[] state = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }

        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }

        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }

        return state;
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }

        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char)Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char)Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        return (byte) (_b0 ^ _b1);
    }

    private static byte[] RC4Base (byte [] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte[] key = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            assert key != null;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
}