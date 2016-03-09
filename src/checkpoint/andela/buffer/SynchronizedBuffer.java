package checkpoint.andela.buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynchronizedBuffer<T extends Object> implements Buffer<T> {

    private List<T> list;

    private Map<String, Integer> lastAccessedTracker;

    protected SynchronizedBuffer() {
        list = new ArrayList<>();
        lastAccessedTracker = new HashMap<>();
    }

    @Override
    public void addToBuffer(T item) {
        synchronized (this) {
            addToList(item);
        }
    }

    private void addToList(T item) {
        list.add(item);
    }

    @Override
    public void addListToBuffer(List<T> itemsList) {
        synchronized (this) {
            if (itemsList.size() > 0) {
                for (T item : itemsList) {
                    addToList(item);
                }
            }
        }
    }

    @Override
    public String registerClientForTracking() {
        synchronized (this) {
            String randomKey = Double.toString(Math.random() * 100000);
            lastAccessedTracker.put(randomKey, 0);
            return randomKey;
        }
    }

    @Override
    public boolean checkIfNewData(String issuedKey) {
        synchronized (this) {
            return checkIfKeyExists(issuedKey) && checkForNewData(issuedKey);
        }
    }

    private boolean checkIfKeyExists(String issuedKey) {
        return lastAccessedTracker.containsKey(issuedKey);
    }

    private boolean checkForNewData(String issuedKey) {
        return lastAccessedTracker.get(issuedKey) < lastAccessedTracker.size();
    }

    @Override
    public List<T> getLatestData(String issuedKey) {
        synchronized (this) {
            List<T> latestData = new ArrayList<>();
            if (checkIfKeyExists(issuedKey)) {
                getLatestBatch(issuedKey, latestData);
            }
            return latestData;
        }
    }

    private void getLatestBatch(String issuedkey, List<T> latestBatch) {
        int lastFetchPosition = lastAccessedTracker.get(issuedkey);
        int listSize = list.size();
        for (int i = lastFetchPosition; i < listSize; i++) {
            latestBatch.add(list.get(i));
        }
        lastAccessedTracker.put(issuedkey, listSize);
    }

}
