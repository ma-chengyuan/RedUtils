package me.alan20210202.redutils.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class ConvertUtils {
    private static final Map<Character, List<Boolean>> hexMap;

    static {
        hexMap = new HashMap<>();
        hexMap.put('0', Arrays.asList(false, false, false, false));
        hexMap.put('1', Arrays.asList(false, false, false,  true));
        hexMap.put('2', Arrays.asList(false, false,  true, false));
        hexMap.put('3', Arrays.asList(false, false,  true,  true));
        hexMap.put('4', Arrays.asList(false,  true, false, false));
        hexMap.put('5', Arrays.asList(false,  true, false,  true));
        hexMap.put('6', Arrays.asList(false,  true,  true, false));
        hexMap.put('7', Arrays.asList(false,  true,  true, false));
        hexMap.put('8', Arrays.asList( true, false, false, false));
        hexMap.put('9', Arrays.asList( true, false, false,  true));
        hexMap.put('A', Arrays.asList( true, false,  true, false));
        hexMap.put('B', Arrays.asList( true, false,  true,  true));
        hexMap.put('C', Arrays.asList( true,  true, false, false));
        hexMap.put('D', Arrays.asList( true,  true, false,  true));
        hexMap.put('E', Arrays.asList( true,  true,  true, false));
        hexMap.put('F', Arrays.asList( true,  true,  true, false));
    }

    private static List<Boolean> complement(List<Boolean> x) {
        for (int i = 0; i < x.size(); i++)
            x.set(i, !x.get(i));
        for (int i = x.size() - 1; i >= 0; i--) {
            if (!x.get(i)) {
                x.set(i, true);
                break;
            } else
                x.set(i, false);
        }
        return x;
    }

    private static List<Boolean> fromBigInteger(BigInteger val, int bits) {
        if (val.signum() < 0)
            return complement(fromBigInteger(val.negate(), bits));
        List<Boolean> ret = new ArrayList<>();
        for (int i = 0; i < bits; i++) {
            BigInteger[] res = val.divideAndRemainder(BigInteger.valueOf(2));
            ret.add(res[1].intValue() == 1);
            val = res[0];
        }
        if (val.signum() > 0)
            throw new IllegalArgumentException("Decimal number too big");
        Collections.reverse(ret);
        return ret;
    }

    private static List<Boolean> fromBigDecimal(BigDecimal val, int bits) {
        if (val.signum() < 0)
            return complement(fromDecimalFrac(val.negate().toString(), bits));
        if (val.compareTo(BigDecimal.ONE) >= 0)
            throw new IllegalArgumentException("Invalid decimal input: greater than one");
        List<Boolean> ret = new ArrayList<>();
        for (int i = 0; i < bits; i++) {
            BigDecimal doubleVal = val.add(val);
            BigDecimal rem = doubleVal.setScale(0, RoundingMode.FLOOR);
            ret.add(rem.intValue() == 1);
            val = doubleVal.subtract(rem);
        }
        return ret;
    }

    private static List<Boolean> padLeft(List<Boolean> x, int bits) {
        while (!x.isEmpty() && !x.get(0)) x.remove(0);
        if (x.size() > bits)
            throw new IllegalArgumentException("Input too long to fit in " + bits + "bits");
        while (x.size() < bits) x.add(0, false);
        return x;
    }

    private static List<Boolean> padRight(List<Boolean> x, int bits) {
        while (!x.isEmpty() && !x.get(x.size() - 1)) x.remove(x.size() - 1);
        if (x.size() > bits)
            throw new IllegalArgumentException("Input too long to fit in " + bits + "bits");
        while (x.size() < bits) x.add(false);
        return x;
    }

    private static List<Boolean> fromRawBinStr(String s) {
        return s.chars()
                .mapToObj(c -> c == '1')
                .collect(Collectors.toList());
    }

    private static List<Boolean> fromRawHexStr(String s) {
        return s.chars()
                .mapToObj(x -> (char) x)
                .flatMap(x -> hexMap.get(x).stream())
                .collect(Collectors.toList());
    }

    public static List<Boolean> fromDecimal(String input, int bits) {
        return fromBigInteger(new BigInteger(input), bits);
    }

    public static List<Boolean> fromDecimalFrac(String input, int bits) {
        return fromBigDecimal(new BigDecimal(input), bits);
    }

    public static List<Boolean> fromHexPadLeft(String input, int bits) {
        if (!input.matches("[+-]?[0-9A-Fa-f]+"))
            throw new NumberFormatException();
        if (input.charAt(0) == '+')
            input = input.substring(1);
        if (input.charAt(0) == '-')
            return complement(padLeft(fromRawHexStr(input.substring(1)), bits));
        return padLeft(fromRawHexStr(input), bits);
    }

    public static List<Boolean> fromHexPadRight(String input, int bits) {
        if (!input.matches("[+-]?[0-9A-Fa-f]+"))
            throw new NumberFormatException();
        if (input.charAt(0) == '+')
            input = input.substring(1);
        if (input.charAt(0) == '-')
            return complement(padRight(fromRawHexStr(input.substring(1)), bits));
        return padRight(fromRawHexStr(input), bits);
    }

    public static List<Boolean> fromBinPadLeft(String input, int bits) {
        if (!input.matches("[+-]?[01]+"))
            throw new NumberFormatException();
        if (input.charAt(0) == '+')
            input = input.substring(1);
        if (input.charAt(0) == '-')
            return complement(padLeft(fromRawBinStr(input.substring(1)), bits));
        return padLeft(fromRawBinStr(input), bits);
    }

    public static List<Boolean> fromBinPadRight(String input, int bits) {
        if (!input.matches("[+-]?[01]+"))
            throw new NumberFormatException();
        if (input.charAt(0) == '+')
            input = input.substring(1);
        if (input.charAt(0) == '-')
            return complement(padRight(fromRawBinStr(input.substring(1)), bits));
        return padRight(fromRawBinStr(input), bits);
    }

    public static String toRawBinStr(List<Boolean> x) {
        StringBuilder sb = new StringBuilder();
        for (Boolean b : x)
            sb.append(b ? '1' : '0');
        return sb.toString();
    }

    public static BigInteger toBigInteger(List<Boolean> x) {
        BigInteger ret = BigInteger.ZERO;
        for (Boolean b : x) {
            ret = ret.add(ret);
            if (b) ret = ret.add(BigInteger.ONE);
        }
        return ret;
    }

    public static BigDecimal toBigDecimal(List<Boolean> x) {
        BigDecimal ret = BigDecimal.ZERO;
        x = new ArrayList<>(x);
        Collections.reverse(x);
        for (Boolean b : x) {
            if (b) ret = ret.add(BigDecimal.ONE);
            int OUTPUT_CONV_SCALE = 16;
            ret = ret.divide(new BigDecimal(2), OUTPUT_CONV_SCALE, RoundingMode.HALF_EVEN);
        }
        return ret;
    }

    public static String toDecimal(List<Boolean> x) {
        return toBigInteger(x).toString();
    }

    public static String toDecimalFrac(List<Boolean> x) {
        return toBigDecimal(x).toString();
    }

    public static String toHexPadLeft(List<Boolean> x) {
        x = new LinkedList<>(x);
        while ((x.size() & 3) != 0) x.add(0, false);
        return toBigInteger(x).toString(16);
    }

    public static String toHexPadRight(List<Boolean> x) {
        x = new LinkedList<>(x);
        while ((x.size() & 3) != 0) x.add(false);
        return toBigInteger(x).toString(16);
    }

    public static String separatePadRight(String input, int interval) {
        int begin = 0;
        StringBuilder sb = new StringBuilder();
        do {
            if (begin > 0) sb.append('_');
            sb.append(input, begin, Math.min(begin + interval, input.length()));
            begin += interval;
        } while (begin < input.length());
        return sb.toString();
    }

    public static String separatePadLeft(String input, int interval) {
        input = new StringBuilder(input).reverse().toString();
        return new StringBuilder(separatePadRight(input, interval)).reverse().toString();
    }
}
