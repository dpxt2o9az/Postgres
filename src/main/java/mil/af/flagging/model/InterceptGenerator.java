package mil.af.flagging.model;

import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class InterceptGenerator {

    private static Random RNG = new Random();

    public static void seed(long seed) {
        RNG = new Random(seed);
    }

    public static String randomString(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        char[] array = new char[length];
        for (int i = 0; i < array.length; i++) {
            array[i] = chars.charAt(RNG.nextInt(chars.length()));
        }
        return String.valueOf(array);
    }

    public static List<Double> randomDoubleList(int length) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(RNG.nextDouble());
        }
        return list;
    }

    public static Instant between(Instant startInclusive, Instant endExclusive) {
        long startSeconds = startInclusive.getEpochSecond();
        long endSeconds = endExclusive.getEpochSecond();
        long random = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);
        return Instant.ofEpochSecond(random);
    }

    public static Instant after(Instant startInclusive) {
        return between(startInclusive, Instant.MAX);
    }

    public static Date between(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return new Date(randomMillisSinceEpoch);
    }

    public static Collection<Intercept> createInterceptsWithConflicts(int icptCount, int conflictCount) {
        List<Intercept> icpts = new ArrayList<>(createIntercepts(icptCount));
        for (int i = 0; i < conflictCount; i++) {
            Intercept i1 = icpts.get(RNG.nextInt(icpts.size()));
            Intercept i2 = icpts.get(RNG.nextInt(icpts.size()));
            i1.setWranglerId(i2.getWranglerId());
        }
        return icpts;
    }

    public static Collection<Intercept> createIntercepts(int count) {
        Collection<Intercept> collection = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Intercept icpt = InterceptGenerator.createIntercept();
            collection.add(icpt);
        }
        return collection;
    }

    public static Intercept createIntercept() {
        Intercept i = new Intercept();
        i.setElnot(randomString(5));

        Date now = new Date();
        Date sixMonthsAgo = Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusMonths(6).toInstant());
        i.setTimeProcessed(between(sixMonthsAgo, now));
        i.setIntUpTime(between(sixMonthsAgo, now));
        i.setIntDownTime(between(i.getIntUpTime(), now));

        i.setCountryCode("AA");
        i.setLatitude(RNG.nextDouble() * 360 - 180);
        i.setLongitude(RNG.nextDouble() * 180 - 90);
        i.setMajor(RNG.nextDouble() * 100);
        i.setMinor(RNG.nextDouble() * i.getMajor());
        i.setOrientation(RNG.nextDouble() * 180);

        i.setModType(randomString(2));
        i.setRfs(randomDoubleList(RNG.nextInt(10) + 1));
        i.setPris(randomDoubleList(RNG.nextInt(20) + 1));
        i.setPds(randomDoubleList(RNG.nextInt(5) + 1));

        i.setScanType(randomString(2));
        i.setScanPeriod(RNG.nextDouble());

        i.setWranglerId(randomString(10));

        return i;
    }
}
