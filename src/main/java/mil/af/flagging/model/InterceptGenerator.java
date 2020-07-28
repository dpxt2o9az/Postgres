package mil.af.flagging.model;

import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class InterceptGenerator {

    private final RandomUtils rng = new RandomUtils();

    private final List<Country> countries;

    public InterceptGenerator(Collection<Country> countries) {
        this.countries = new ArrayList<>(countries);
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

    public Collection<Intercept> createInterceptsWithConflicts(int icptCount, int conflictCount) {
        List<Intercept> icpts = new ArrayList<>(createIntercepts(icptCount));
        for (int i = 0; i < conflictCount; i++) {
            Intercept i1 = icpts.get(rng.nextInt(icpts.size()));
            Intercept i2 = icpts.get(rng.nextInt(icpts.size()));
            i1.setWranglerId(i2.getWranglerId());
        }
        return icpts;
    }

    public Collection<Intercept> createIntercepts(int count) {
        Collection<Intercept> collection = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Intercept icpt = createIntercept();
            collection.add(icpt);
        }
        return collection;
    }

    public Intercept createIntercept() {
        Intercept i = new Intercept();
        i.setElnot(rng.randomString(5));

        Date now = new Date();
        Date sixMonthsAgo = Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusMonths(6).toInstant());
        i.setTimeProcessed(between(sixMonthsAgo, now));
        i.setIntUpTime(between(sixMonthsAgo, now));
        i.setIntDownTime(between(i.getIntUpTime(), now));

        i.setCountryCode(countries.get(rng.nextInt(countries.size())).countryCode);
        i.setLatitude(rng.nextDouble() * 360 - 180);
        i.setLongitude(rng.nextDouble() * 180 - 90);
        i.setSemiMajor(rng.nextDouble() * 100);
        i.setSemiMinor(rng.nextDouble() * i.getSemiMajor());
        i.setOrientation(rng.nextDouble() * 180);

        i.setModType(rng.randomString(2));
        i.setRfs(rng.randomDoubleList(rng.nextInt(10) + 1));
        i.setPris(rng.randomDoubleList(rng.nextInt(20) + 1));
        i.setPds(rng.randomDoubleList(rng.nextInt(5) + 1));

        i.setScanType(rng.randomString(2));
        i.setScanPeriod(rng.nextDouble());

        i.setWranglerId(rng.randomString(10));
        i.setReadOutStation(rng.randomString(2));

        return i;
    }
}
